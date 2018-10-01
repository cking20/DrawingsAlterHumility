/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author chris
 */
public class Page {
    private UUID id;
    private String creator;
    private String text;
    private String imagePath;
    private ArrayList<String> votes;
    
    /**
     * Vote on the current page. Can only be done iff that session has 
     * not voted on this page
     * @param session that is attempting to vote on the page.
     * @return true iff the session has just voted on the page
     */
    public boolean Vote(String session){
        if(getVotes().contains(session)){
            return false;
        }
        getVotes().add(session);
        return true;
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the votes
     */
    public ArrayList<String> getVotes() {
        return votes;
    }

    /**
     * @param votes the votes to set
     */
    public void setVotes(ArrayList<String> votes) {
        this.votes = votes;
    }
}
