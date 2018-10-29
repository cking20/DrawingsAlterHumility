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
    private static long idCounter = 0;
    private final long id;
    private final String creator;
    
    /**
     * used for the front end, but its kind of redundant
     */
    private final boolean isImage;
    private final String content;
    private ArrayList<String> votes;
    
    public static Page BuildDrawing(String creator, String drawingPath){
        return new Page(creator, true, drawingPath);
    }
    
    public static Page BuildGuess(String creator, String guess){
        return new Page(creator, false, guess);
    }
    
    private Page(String creator, boolean isDrawing, String content){
        this.isImage = isDrawing;
        this.creator = creator;
        id = idCounter++;
        this.content = content;
        this.votes = new ArrayList<>();
    }
    
    private Page(){
        id = -1;
        creator = null;
        content = null;
        votes = new ArrayList<>();
        isImage = false;
    }
    
    /**
     * Vote on the current page. Can only be done iff that session has 
     * not voted on this page
     * @param session that is attempting to vote on the page.
     * @return true iff the session has just voted on the page
     */
    public boolean Vote(String session){
        if(votes == null)
            votes = new ArrayList<>();
        if(votes.contains(session)){
            return false;
        }
        votes.add(session);
        System.out.println("added");
        return true;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
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

    /**
     * @return the isImage
     */
    public boolean isIsImage() {
        return isImage;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
