/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author chris
 */
public class Lobby {
    private int id;
    private String name;
    private boolean isReleased;
    private String adminUuid;
    private String password;
    private boolean isInProgress;
    private int roundNumber;
    private int maxRounds;
    private int maxPlayers;
    private final ArrayList<String> players;
    private final HashMap<String, Player> playerData;
    

    public Lobby(int id) {
        this.id = id;
        name = id+"";
        password = null;
        isReleased = true;
        isInProgress = false;
        roundNumber = -1;
        maxRounds = 10;
        maxPlayers = 10;
        players = new ArrayList<>();
        playerData = new HashMap<>();
        
    }

    /**
     * Assigns the lobby as in use with the newAdminID as the admin
     * @param newAdminID
     * @param newAdminName
     * @return 
     */
    public boolean Checkout(String newAdminID, String newAdminName){
        if(Reset()){
            this.isReleased = false;
            this.adminUuid = newAdminID;
            this.players.add(this.adminUuid);
            Player admin = new Player();
            admin.name = newAdminName;
            this.playerData.put(newAdminID, admin);
            return true;
        }
        return false;
    }
    
    /**
     * Resets the lobby to the WAITING FOR PLAYERS state, with the players still in the lobby
     * iff the admin is the one restarting the game
     * @param accessor_id
     * @return 
     */
    public boolean Restart(String accessor_id){
        if(accessor_id.compareTo(this.adminUuid) == 0){
            isInProgress = false;
            roundNumber = -1;
            return true;
        }
        return false;
    }
    
    /**
     * Allow the booklet to be reset
     * @return 
     */
    public boolean Release(){
        isReleased = true;
        return true;
    }
    
    /**
     * Completely resets the Lobby iff it has been released
     * @return 
     */
    public boolean Reset(){
        if(this.isIsReleased()){
            adminUuid = null;
            isReleased = true;
            setPassword(null);
            isInProgress = false;
            roundNumber = -1;
            maxRounds = 10;
            maxPlayers = 10;
            players.clear();
            playerData.clear();
            return true;
        }
        return false;
    }
    
    /**
     * Allow a player to join the Lobby iff the game hasnt started, the lobby is checkedout, the player knows the password,
     * @param playerID
     * @param password
     * @return 
     */
    public boolean JoinPlayer(String playerID, String playerName, String password){
        if(!isReleased && !isInProgress){//the lobby is in the waiting for players state
            if(this.getPassword() == null//the lobby is public
                    || (this.getPassword() != null && this.getPassword().compareTo(password) == 0))//the lobby is private and the player knows the password
                if(!players.contains(playerID)){//the player isnt already in the game
                    players.add(playerID);
//                    playerNames.put(playerID, playerName);
                    Player player = new Player();
                    player.name = playerName;
                    this.playerData.put(playerID, player);
                    return true;
                }
        }
        return false;
    }

    /**
     * Remove the player iff the player is in the game
     * @param player
     * @return 
     */
    public boolean RemovePlayer(String player){
        if(players.contains(player)){
            players.remove(player);
            playerData.remove(player);
            return true;
        }
        return false;
    }
    
    /**
     * Updates the Lobby state to represent a player name change
     * @param playerID the session ID of a player in the lobby
     * @param newName that player's new name
     */
    public void updatePlayerName(String playerID, String newName){
            Player player = (Player)playerData.get(playerID);
            if(player != null){
                player.name = newName;
            }
    } 
    
    /**
     * 
     * @return the new round number 
     */
    public int AdvanceRound(){
        if(roundNumber < 0){//set up the game for playing
            roundNumber = 0;
            isInProgress = true;
            for (String playerID: players) {
                Player p = playerData.get(playerID);
                p.currentBooklet = new Booklet(p.name, maxRounds);
            }
        }else if(roundNumber < maxRounds){//in play state
            roundNumber++;
            //TODO swap booklets
        }else{//in the review state
            isInProgress = false;
            
        }
        return roundNumber;
    }
    
    public boolean isFull(){
        return players.size() >= maxPlayers;
    }
    
    /**
     * @return the isReleased
     */
    public boolean isIsReleased() {
        return isReleased;
    }

    /**
     * @param isReleased the isReleased to set
     */
    public void setIsReleased(boolean isReleased) {
        this.isReleased = isReleased;
    }

    /**
     * @return the adminUuid
     */
    public String getAdminUuid() {
        return adminUuid;
    }

    /**
     * @param adminUuid the adminUuid to set
     */
    public void setAdminUuid(String adminUuid) {
        this.adminUuid = adminUuid;
    }

    /**
     * @return the isPrivate
     */
    public boolean isIsPrivate() {
        return getPassword() != null;
    }

    /**
     * @param password the isPrivate to set
     */
    public void setIsPrivate(String password) {
        this.setPassword(password);
    }

    /**
     * @return the isInProgress
     */
    public boolean isIsInProgress() {
        return isInProgress;
    }

    /**
     * @param isInProgress the isInProgress to set
     */
    public void setIsInProgress(boolean isInProgress) {
        this.isInProgress = isInProgress;
    }

    /**
     * @return the roundNumber
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * @param roundNumber the roundNumber to set
     */
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    /**
     * @return the maxRounds
     */
    public int getMaxRounds() {
        return maxRounds;
    }

    /**
     * @param maxRounds the maxRounds to set
     */
    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    /**
     * @return the maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @param maxPlayers the maxPlayers to set
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * @return the players
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * @param players the players to set
     */
    public void setPlayers(ArrayList<String> players) {
        this.players.clear();
        this.players.addAll(players);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
     
}
