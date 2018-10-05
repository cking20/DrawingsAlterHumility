/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;


import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author chris
 */
public class ResourceManager {
    final static Logger logger = LoggerFactory.getLogger(ResourceManager.class);
    static File tempDir;
    private static ResourceManager instance;
    private ArrayList<String> promptList;
    private Random random;
    private Gson gson;
    
    private ResourceManager(){
        
        gson = new Gson();
        tempDir = new File("temp");               
        
        tempDir.mkdir();
        for(File f: tempDir.listFiles()){
            f.delete();
        }
        tempDir.deleteOnExit();
        
        promptList = new ArrayList<>();
        
        random = new Random();
        
    }
    
    public void LoadResources(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream promptStream = classLoader.getResourceAsStream("prompts.txt");
        try (Scanner inFileScanner = new Scanner(promptStream)) {
            while(inFileScanner.hasNext()){
                getInstance().promptList.add(inFileScanner.nextLine());
            }
        }
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
    
  
    /**
     * 
     * @param LobbyNum
     * @param BookletNum
     * @param PageNum
     * @param inputStream
     * @return fileName
     */
    public String saveImage(int LobbyNum, int BookletNum, int PageNum, InputStream inputStream){
        File dir = new File(tempDir.getPath()+"/"+LobbyNum+"/"+BookletNum);
        dir.mkdirs();
        Path tempFile = new File(dir.getPath()+"/"+PageNum+".png").toPath();//Files.createTempFile(tempDir.toPath(), "temp", ".png");
//            Path tempFile = Files.createTempFile(tempDir.toPath(), "temp", ".png");
        try{
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return "/"+LobbyNum+"/"+BookletNum+"/"+PageNum;
        }catch(IOException e){
            System.err.println(e);
            return null;
        }
    }
    
    /**
     * 
     * @param LobbyNum
     * @param BookletNum
     * @param PageNum
     * @return 
     */
    public byte[] getImage(int LobbyNum, int BookletNum, int PageNum){
        File img = new File(tempDir+"/"+LobbyNum+"/"+BookletNum+"/"+PageNum+".png");
        if(img.exists()){
            try {
                return Files.readAllBytes(img.toPath());
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return null;
    }
    
    /**
     * Get an amount of random prompts 
     * @param num the number of prompts to return
     * @return a list of random prompts
     */
    public String getPrompts(int num){
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            results.add(promptList.get(random.nextInt(promptList.size())));
        }
        return gson.toJson(results);
    }
    
    
    
}
