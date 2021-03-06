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
    private final ArrayList<Booklet> booklets;
    private final HashMap<String, Player> playerData;
    

    public Lobby(int id) {
        this.id = id;
        name = id+"";
        password = null;
        isReleased = true;
        isInProgress = false;
        roundNumber = -1;
        maxRounds = 2;
        maxPlayers = 10;
        players = new ArrayList<>();
        booklets = new ArrayList<>();
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
            for (String player : players) {
                playerData.get(player).votes = 0;
                playerData.get(player).submitted = false;
                playerData.get(player).currentBooklet = null;
            }
            booklets.clear();
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
            name = id+"";
            isInProgress = false;
            roundNumber = -1;
            maxRounds = 2;
            maxPlayers = 10;
            players.clear();
            playerData.clear();
            booklets.clear();
            return true;
        }
        return false;
    }
    
    /**
     * Allow a player to join the Lobby iff the game hasnt started, the lobby is checkedout, the player knows the password,
     * @param playerID
     * @param playerName
     * @param password
     * @return 
     */
    public boolean JoinPlayer(String playerID, String playerName, String password){
        if(isFull()) return false;
        if(password == null)
            password = "";
        if(!isReleased && !isInProgress){//the lobby is in the waiting for players state
            if(this.getPassword() == null//the lobby is public
                    || (this.getPassword() != null && this.getPassword().compareTo(password) == 0))//the lobby is private and the player knows the password
                if(!players.contains(playerID)){//the player isnt already in the game
                    players.add(playerID);
//                    playerNames.put(playerID, playerName);
                    Player player = new Player();
                    player.name = playerName;
                    this.playerData.put(playerID, player);
                    //recalculate round number
                    maxRounds = 2 * (this.players.size()-1);
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
            booklets.remove(playerData.get(player).currentBooklet);
            playerData.remove(player);
            //recalculate round number
            maxRounds = 2 * this.players.size();
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
     * Begin the game. Only allowable if the game is checkedout and not already started
     */
    public void StartGame(){
        if(isReleased || isInProgress)
            return;
        if(roundNumber < 0)
            AdvanceRound();
        
    }
    
    /**
     * 
     * @return the new round number 
     */
    private int AdvanceRound(){
        if(roundNumber < 0){//set up the game for playing
            roundNumber = 0;
            isInProgress = true;
            for (String playerID: players) {
                Player p = playerData.get(playerID);
                Booklet b = new Booklet(playerID, maxRounds+1);
                booklets.add(b);
                p.submitted = false;
                p.currentBooklet = b;
            }
        }else if(roundNumber <= maxRounds){//in play state
            roundNumber++;
            //dont swap on the first transition iff there are an even number of players
            if(roundNumber == 1 && players.size() % 2 == 0){          
                //Dont swap
            }
            else{//normal case
                SwapBooklets();
            }
            
            for (String playerID: players) {
                playerData.get(playerID).submitted = false;
            }
            
        }else{//in the review state
            isInProgress = false;
            
        }
        return roundNumber;
    }
    
    /**
     * Shifts all booklets by one player
     */
    public void SwapBooklets(){
        Booklet temp = playerData.get(players.get(0)).currentBooklet;
        for (int i = 0; i < players.size()-1; i++) {
            Booklet current = playerData.get(players.get(i)).currentBooklet = 
                    playerData.get(players.get(i+1)).currentBooklet;
            current.setUser(players.get(i));
        }
        playerData.get(players.get(players.size()-1)).currentBooklet = temp;
        playerData.get(players.get(players.size()-1)).currentBooklet.
                setUser(players.get(players.size()-1));
    }
    
    /**
     * Check if all players have submitted, if so then advance the round
     * @return the roundNumber
     */
    public int CheckRoundAdvance(){
        if(isReleased)
            return roundNumber;
        for (int i = 0; i < players.size(); i++) {
            if(!playerData.get(players.get(i)).submitted)
                return roundNumber;//do not advance
        }
        //all players have submitted
        AdvanceRound();
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
     * @deprecated the rounds should be calculated when a player joins or leaves
     * @param maxRounds the maxRounds to set
     */
    public void setMaxRounds(int maxRounds) {
        //this.maxRounds = maxRounds;
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
     * get a player's data
     * @param id
     * @return 
     */
    public Player getPlayerData(String id){
        return playerData.get(id);
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
     
    /**
     * Allow a player to vote on a page in a booklet
     * @param id the player voting
     * @param votingOn the booklet's owner ID
     * @param round the round to be voted on
     * @return 
     */
    public boolean vote(String id, String votingOn, int round){
        for (Booklet b : booklets) {
            if(b.getOwner().compareTo(votingOn) == 0){
                if(b.voteOn(id, round)){
                    String playerVotedOn = b.getPages()[round].getCreator();
                    playerData.get(playerVotedOn).votes++;
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    
    public int getBookletIndex(Booklet b){
        return booklets.indexOf(b);
    }
    
    public boolean hasPlayer(String playerID){
        return players.contains(playerID);
    }
}
