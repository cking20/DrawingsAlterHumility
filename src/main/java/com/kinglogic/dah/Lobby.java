/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

import java.util.ArrayList;

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
    

    public Lobby(int id) {
        this.id = id;
        name = id+"";
        password = null;
        isReleased = true;
        password = null;
        isInProgress = false;
        roundNumber = -1;
        maxRounds = 10;
        maxPlayers = 10;
        players = new ArrayList<>();
    }

    /**
     * Resets the lobby. Assigns newAdmin as the admin of this lobby
     * @param newAdmin
     * @return 
     */
    public boolean Checkout(String newAdmin){
        if(Reset()){
            this.isReleased = false;
            this.adminUuid = newAdmin;
            this.players.add(this.adminUuid);
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
            password = null;
            isInProgress = false;
            roundNumber = -1;
            maxRounds = 10;
            maxPlayers = 10;
            players.clear();
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
    public boolean JoinPlayer(String playerID, String password){
        if(!isReleased && !isInProgress){//the lobby is in the waiting for players state
            if(this.password == null//the lobby is public
                    || (this.password != null && this.password.compareTo(password) == 0))//the lobby is private and the player knows the password
                if(!players.contains(playerID)){//the player isnt already in the game
                    players.add(playerID);
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
            return true;
        }
        return false;
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
        return password != null;
    }

    /**
     * @param password the isPrivate to set
     */
    public void setIsPrivate(String password) {
        this.password = password;
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
     
}
