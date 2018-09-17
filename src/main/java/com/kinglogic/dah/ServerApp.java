/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletResponse;
import spark.Filter;
import spark.Spark;
import static spark.Spark.*;
/**
 *
 * @author chris
 */
public class ServerApp {
    static File tempDir;
    
    public static void main(String[] args){
        staticFileLocation("public");
        tempDir = new File("temp");
        tempDir.mkdir();
        for(File f: tempDir.listFiles()){
            f.delete();
        }
        tempDir.deleteOnExit();
        Spark.externalStaticFileLocation("temp");
        port(getHerokuAssignedPort());
        configureRoutes();
        
        
        
}
    
    public static void configureRoutes(){
        path("/game", () -> {
            //return JSON
            before("/*", (request, response) -> {
                response.type("application/json");
                response.header("Access-Control-Allow-Origin", "http://localhost:8080");
                response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
//                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
                response.header("Access-Control-Allow-Credentials", "true");
                //set up their session 
                request.session(true);
            });

            configureLobbiesEndpoints();
            configureCurrentLobbyEndpoints();
            configureImagesEndpoints();
            
            
            get("/hello", (request,response) ->{
                return "{\"message\": \"hi there\"}";
            });  
        });      
    }
    
    /**
     * Set all the routes for the /game/lobbies/{lobby_id} endpoints
     */
    public static void configureLobbiesEndpoints(){
        //set the endpoint GET:/game/lobbies
        path("/lobbies", () -> {
            /**
             * Returns a list of all the Lobbies
             */
            get("", (request,response) ->{
                String data = GameManager.getInstance().getJoinableLobbies(10, 0);
                if(data == null)
                    halt(400);
                return data;
            });
           
            /**
             * Check out a new lobby
             */
            post("", (request,response) ->{
                if(request.session().attribute("CURRENT_GAME") != null)
                        halt(403, "you already have a game in your session");
                String data = GameManager.getInstance().CheckoutLobby(request.session());
                if(data == null)
                    halt(400);
                return data;
            });
            
            
            //Specific lobby functions
            path("/:lobby_id", () ->{
                before("/*", (request, response) -> {
//                    System.out.println("api called");
                    try{
//                        System.out.println(request.params(":lobby_id"));
                        Integer.parseInt(request.params(":lobby_id"));
                    }catch(NumberFormatException exception){
                        halt(406, "invalid lobby id");
                    }
                });
                /**
                * Returns all data about a specific lobby
                */
                get("", (request,response) ->{
                    String data = GameManager.getInstance().getLobby(Integer.parseInt(request.params(":lobby_id")));
                    System.err.println(data);
                    if(data == null)
                        halt(400);
                    return data;
                });
                
                /**
                 * Release this lobby back to the pool
                 */
                delete("", (request,response) ->{
                    return GameManager.getInstance().ReleaseLobby(request.session());
                });
                
                /**
                 * Update the settings of this lobby
                 */
                put("", (request,response) ->{
                    return "{\"message\": not yet implemented}";
                });
                
                /**
                 * Join this lobby
                 */
                before("/join", (request, response) -> {
                    if(request.session().attribute("CURRENT_GAME") != null)
                        halt(403, "you already have a game in your session");
                    System.out.println(request.body());
                    System.out.println(request.body());
                });
                post("/join", (request,response) ->{
                    if( GameManager.getInstance().JoinLobby(
                            request.session(), 
                            Integer.parseInt(request.params(":lobby_id")),
                            request.body()
                    )){
                        return GameManager.getInstance().getLobby(Integer.parseInt(request.params(":lobby_id")));
                    }
                    response.status(403);
                    return "{\"message\": \"nope, stars. can't join\"}";
                });
                
                /**
                 * Leave this lobby
                 */
                before("/leave", (request, response) -> {
                    if(request.session().attribute("CURRENT_GAME") == null)
                        halt(403, "you dont have a game in your session");
                });
                post("/leave", (request,response) ->{
                    if(GameManager.getInstance().LeaveLobby(request.session())){
                        response.status(200);
                        return "{\"message\": \"successfully left\"}";
                    }
                    response.status(403);
                    return "{\"message\": \"nope, stars. can't leave\"}";
                });
                
            });
            
                    
        });
    }
    
    /**
     * Set all current Game Endpoints, mainly to be used for accessing the sessions game data without knowing the ID
     */
    public static void configureCurrentLobbyEndpoints(){
        path("/current", () -> {
            //make sure the user is in a Lobby
            before("", (request, response) -> {
                if(request.session().attribute("CURRENT_GAME") == null)
                    halt(403, "{\"message\": \"you're not in a game\"}");
            });
            /**
             * Retrun the data of the game the session is currently in
             */
            get("", (request,response) ->{
                    String data = GameManager.getInstance().getLobby(request.session().attribute("CURRENT_GAME"));
                    if(data == null)
                        halt(400);
                    return data;
                });
        });
    }
    
    public static void configureImagesEndpoints(){
        path("/images", () ->{
            //testing stuff
            get("/testimage", (request,response) ->{
                response.type("image/png");
                if(tempDir.listFiles().length > 0){
                    byte[] data = null;
                    try {
                        data = Files.readAllBytes(tempDir.listFiles()[0].toPath());
                    } catch (Exception e) {e.printStackTrace();}
                    HttpServletResponse raw = response.raw();
    //                response.header("Content-Disposition", "attachment; filename=image.jpg");
                    response.type("image/png");
                    try {
                        raw.getOutputStream().write(data);
                        raw.getOutputStream().flush();
                        raw.getOutputStream().close();
                    } catch (Exception e) {e.printStackTrace();}
                    return raw;
                }
                return null;
            });

            post("/testimage", (request,response) ->{
                Path tempFile = new File(tempDir.getPath()+"/temp.png").toPath();//Files.createTempFile(tempDir.toPath(), "temp", ".png");
    //            Path tempFile = Files.createTempFile(tempDir.toPath(), "temp", ".png");
                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) {
                    Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
                }
                return "<h1>You uploaded this image:<h1><img src='" + tempFile.getFileName()+ "'>";
            });
        });
        
    }
    
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
