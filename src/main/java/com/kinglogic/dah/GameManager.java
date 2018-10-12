/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

import com.google.gson.Gson;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;
import spark.Session;

/**
 *
 * @author chris
 */
public class GameManager {
    public static final int MAX_LOBBIES = 10;
    private static GameManager instance;
    private Lobby[] lobbies;
    private final Gson gson;
    
    private GameManager(){
        lobbies = new Lobby[MAX_LOBBIES];
        for (int i = 0; i < lobbies.length; i++) {
            lobbies[i] = new Lobby(i);
        }
        gson = new Gson();
    }
    
    /**
     * There should only ever be one instance of the GameManager ever available
     * @return 
     */
    public static GameManager getInstance(){
        if(instance == null)
           instance = new GameManager(); 
        return instance;
    }
    
    /**
     * Returns the Lobby with that lobby_id 
     * @param lobby_id must be >=0 and < MAX_LOBBIES
     * @return 
     */
    public String getLobby(int lobby_id){
        if(lobby_id < lobbies.length && lobby_id >= 0)
            return gson.toJson(lobbies[lobby_id], Lobby.class);
        else
            return null;
    }
    
    /**
     * Get the data of all lobbies that are not in progress
     * @param amount must be > 0 and < MAX_LOBBIES
     * @param offset must be > 0 and < MAX_LOBBIES
     * @return 
     */    
    public String getJoinableLobbies(int amount, int offset){
        //verify the parameters are acceptable
        if(amount < 0 || offset < 0 || offset > MAX_LOBBIES || amount > MAX_LOBBIES)
            return null;
        
        //Search through the lobbies
        ArrayList<Lobby> lobbyList = new ArrayList<>();
        int numberReturned = 0;
        for (int i = offset; i < lobbies.length && numberReturned < amount && i < lobbies.length; i++) {
            if(!lobbies[i].isIsReleased() && !lobbies[i].isFull() && !lobbies[i].isIsInProgress()){
                lobbyList.add(lobbies[i]);
                numberReturned++;
            }
        }
        return gson.toJson(lobbyList);
    }
    
    /**
     * Checkouts the lobby with admin_id as the admin 
     * @param admin the sesion to give admin privs to
     * @return the Lobby object's data iff the operation was completed successfully
     */
    public String CheckoutLobby(Session admin){
        int lobby_id = -1;
        int counter = 0;
        //find a released lobby
        while(lobby_id < 0 && counter < lobbies.length){
            if(lobbies[counter].isIsReleased())
                lobby_id = counter;
            counter++;
        }
        if(lobby_id < lobbies.length && lobby_id >= 0){//the lobby is valid
            if(lobbies[lobby_id].isIsReleased())//the lobby is released
                if(lobbies[lobby_id].Checkout(admin.id(), admin.attribute("NAME"))){//the lobby gets successfully checked out
                    admin.attribute("CURRENT_GAME", lobby_id);
                    return  gson.toJson(lobbies[lobby_id], Lobby.class);
                }
        }
        return null;
    }
    
    /**
     * Release control of the lobby so it can be reused
     * @param lobby_id
     * @param accesor_id
     * @return 
     */
    public String ReleaseLobby(Session admin){
        String accesor_id = admin.id();
        if(admin.attribute("CURRENT_GAME") == null)
            return null;
        int lobby_id = admin.attribute("CURRENT_GAME");
        if(lobby_id < lobbies.length && lobby_id >= 0){//the lobby is valid
            if(accesor_id.compareTo(lobbies[lobby_id].getAdminUuid()) == 0)//if its the admin releasing it
                if(lobbies[lobby_id].Release()){//it successfully releases
                    admin.removeAttribute("CURRENT_GAME");
                    return gson.toJson(lobbies[lobby_id], Lobby.class);
                }
        }
        return null;
    }

    /**
     * Attempt to add the session to the game. Cant do that if the session already
     *      has a game, nor if the lobby is not released, nor if they do not have
     *      the correct password.
     * @param playerSession
     * @param lobby_id
     * @param password
     * @return 
     */
    public boolean JoinLobby(Session playerSession, int lobby_id, String password){
        if(playerSession.attribute("CURRENT_GAME") != null)
            return false;
        if(lobby_id < lobbies.length && lobby_id >= 0){//the lobby is valid
            if(!lobbies[lobby_id].isIsReleased()){//the lobby is released
                if(lobbies[lobby_id].JoinPlayer(playerSession.id(),playerSession.attribute("NAME"), password)){//the lobby gets successfully joined
                    playerSession.attribute("CURRENT_GAME", lobby_id);
                    return true;
                }
            }      
        }
        return false;
    }
    
    /**
     * Allow a player to leave the lobby they're currently in.
     * If the player is the admin, pass control to another player
     * If there are no players in the lobby then Release the lobby
     * @param playerSession
     * @return 
     */
    public boolean LeaveLobby(Session playerSession){
        String accesor_id = playerSession.id();
        if(playerSession.attribute("CURRENT_GAME") == null)
            return false;
        int lobby_id = playerSession.attribute("CURRENT_GAME");
        if(lobby_id < lobbies.length && lobby_id >= 0){//the lobby is valid
            if(accesor_id.compareTo(lobbies[lobby_id].getAdminUuid()) == 0){//if its the admin leaving 
                //set another user as the admin, if there are no others. release the game
                if(lobbies[lobby_id].RemovePlayer(accesor_id)){//player successfully left
                    playerSession.removeAttribute("CURRENT_GAME");
                    if(lobbies[lobby_id].getPlayers().size() >= 1)
                        lobbies[lobby_id].setAdminUuid(lobbies[lobby_id].getPlayers().get(0));
                    else
                        lobbies[lobby_id].Release();
                    return true;
                } 
            }else{//its just some player leaving
                if(lobbies[lobby_id].RemovePlayer(accesor_id)){//player successfully left
                    playerSession.removeAttribute("CURRENT_GAME");
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Update the session's name in the lobby data
     * @param playerSession 
     */
    public void UpdatePlayerName(Session playerSession){
        if(playerSession.attribute("CURRENT_GAME") != null && playerSession.attribute("NAME") != null){
            int lobby_id = playerSession.attribute("CURRENT_GAME");
            if(lobby_id < lobbies.length && lobby_id >= 0){//the lobby is valid
                lobbies[lobby_id].updatePlayerName(playerSession.id(), playerSession.attribute("NAME"));
                
            }
        }
    }
    
    /**
     * Precondition: Session attribute "CURRENT_GAME" should != null
     * Updates the lobbies state if the session is the admin
     * @param adminSession
     * @param name
     * @param maxRounds
     * @param maxPlayers
     * @param password 
     */
    public String UpdateLobbySettings(Session admin, String name, int maxRounds, int maxPlayers, String password){
        String accesor_id = admin.id();
        if(admin.attribute("CURRENT_GAME") == null){
            System.err.println("admin current game = null");
            return null;}
        int lobby_id = admin.attribute("CURRENT_GAME");
        System.err.println("lobbyid "+lobby_id);
        if(lobby_id < lobbies.length && lobby_id >= 0){//the lobby is valid
            if(accesor_id.compareTo(lobbies[lobby_id].getAdminUuid()) == 0){//if its the admin releasing it
                lobbies[lobby_id].setName(name);
                lobbies[lobby_id].setMaxPlayers(maxPlayers);
                lobbies[lobby_id].setMaxRounds(maxRounds);
                if(password == null)
                    lobbies[lobby_id].setPassword(null);
                else if(password.compareTo("") == 0)
                    lobbies[lobby_id].setPassword(null);
                else
                    lobbies[lobby_id].setPassword(password);
            }
        }
        return gson.toJson(lobbies[lobby_id], Lobby.class);  
    }
    
    /**
     * Attempt to begin the game
     * @param admin the player trying to begin the game
     * @return the state of the lobby
     */
    public String StartGame(Session admin){
        String accesor_id = admin.id();
        int lobby_id = admin.attribute("CURRENT_GAME");
        if(lobby_id < lobbies.length && lobby_id >= 0){//the lobby is valid
            if(accesor_id.compareTo(lobbies[lobby_id].getAdminUuid()) == 0){//if its the admin stating it
                lobbies[lobby_id].StartGame();
            }
        }
        return gson.toJson(lobbies[lobby_id], Lobby.class);  
    }
    
    /**
     * Submit a guess to the game. Ensures the integrity of the gamestate before
     *      accepting the data.
     * @param player that is submitting the guess
     * @param gameID the game that the player is currently in
     * @param guess the guess to be uploaded
     * @return true iff the guess has been added to the game
     */
    public boolean SubmitGuess(String player, int gameID, String guess){
         Lobby current = lobbies[gameID];
         if(!current.isIsInProgress())
             return false;
         Player data = current.getPlayerData(player);
         if(data == null)
             return false;
         if(guess == null)
             return false;
         Page newPage = Page.BuildGuess(player, guess);
         if( data.currentBooklet.submit(player, current.getRoundNumber(), newPage)){
             data.submitted = true;
             current.CheckRoundAdvance();
             return true;
         }
         return false;         
    }
    
    /**
     * Submit a drawing to the game. Ensures the integrity of the gamestate before
     *      accepting the data. Uploads the image from the stream to the Resource 
     *      Manager
     * @param player that is submitting the drawing
     * @param gameID the game that the player is currently in
     * @param stream the part's stream to be uploaded
     * @return true iff the image has been added to the game
     */
    public String SubmitDrawing(String player, int gameID, InputStream stream){
         Lobby current = lobbies[gameID];
         if(!current.isIsInProgress())
             return null;
         Player data = current.getPlayerData(player);
         if(data == null)
             return null;
         
         int bookletIndex = current.getBookletIndex(data.currentBooklet);
         String path = ResourceManager.getInstance().saveImage(gameID, bookletIndex, current.getRoundNumber(), stream);
         Page newPage = Page.BuildDrawing(player, path);
         if( data.currentBooklet.submit(player, current.getRoundNumber(), newPage)){
             data.submitted = true;
             current.CheckRoundAdvance();
             return path;
         }
         return null;
    }

    
    /**
     * Meant only for unit testing
     */
    private void NUKE(){
        instance = null;
    }
}
