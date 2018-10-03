/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

/**
 *
 * @author chris
 */
public class Booklet {
    private String owner;
    private String user;
    private Page[] pages;

    /**
     * Booklet holds page data, and information about who is using the booklet
     * @param owner
     * @param numPages number of pages to allocate
     */
    public Booklet(String owner, int numPages) {
        this.owner = owner;
        this.user = owner;
        pages = new Page[numPages];
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the pages
     */
    public Page[] getPages() {
        return pages;
    }
    
    /**
     * Attempt to submit a new page to the booklet
     * @param submitor the user attempting to submit to this booklet
     * @param currentRound the current game round
     * @param newPage 
     * @return true iff the page has been added
     */
    public boolean submit(String submitor, int currentRound, Page newPage){
        if(submitor.compareTo(this.user) != 0)//the user must be the one to submit
            return false;
        if(pages[currentRound] != null)//this method is not meant to overwrite a submission
            return false;
        
        //check health(the page must be a guess when the round is EVEN,
        //      and must be an image when round is ODD)
        if(currentRound % 2 == 0){
            //the page must be a guess
            if(newPage.isIsImage())
                return false;
        }else{
             //the page must be an image
            if(!newPage.isIsImage())
                return false;
        }
        pages[currentRound] = newPage;
        return true;
    }
    
    
    
}
