/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;


import java.io.File;

/**
 *
 * @author chris
 */
public class ResourceManager {
    static File tempDir;
    private static ResourceManager instance;
    
    private ResourceManager(){
        tempDir = new File("temp");
        tempDir.mkdir();
        for(File f: tempDir.listFiles()){
            f.delete();
        }
        tempDir.deleteOnExit();
        
    }
    
    /**
     * There should only ever be one instance of the ResourceManager ever available
     * @return 
     */
    public static ResourceManager getInstance(){
        if(instance == null)
           instance = new ResourceManager(); 
        return instance;
    }
    
}
