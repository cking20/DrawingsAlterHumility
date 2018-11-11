/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import static com.kinglogic.dah.ResourceManager.tempDir;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import spark.Filter;
import spark.Session;
import spark.Spark;
import static spark.Spark.*;
/**
 *
 * @author chris
 */
public class ServerApp {
    private static final String host = "http://drawings-alter-humility.herokuapp.com";
//    private static final String host = "http://localhost:8080";
//    private static final String host = "*";
    private static Gson gson;
    
    public static void main(String[] args){
        gson = new Gson();
        staticFileLocation("public");
        GameManager.getInstance();
        ResourceManager.getInstance();
        ResourceManager.getInstance().LoadResources();
        Spark.externalStaticFileLocation("temp");
        port(getHerokuAssignedPort());
        configureRoutes();
        
        
        
        
}
    public static void configureTwitterUpload(){     
        post("/share", (request,response) ->{
       
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try{
            InputStream input = request.raw().getPart("uploaded_file").getInputStream();
            String ret =  ResourceManager.getInstance().persistImage(input);
            return ExternalAPIInterface.getInstance().UploadImageToTwitterUsing4J(ret);

            
            //return host+"/images/"+ret;
        }
        catch(IOException | ServletException e){
            e.printStackTrace();
            halt(500, "failed to share");
        }
       
        return "";
        });
    }
    
    public static void configureRoutes(){
        path("/game", () -> {
            //return JSON
            before("/*", (request, response) -> {
                response.type("application/json");
//                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Allow-Origin", host);
//                response.header("Access-Control-Allow-Origin", "http://localhost:8080");
//                response.header("Access-Control-Allow-Origin", "http://drawings-alter-humility.herokuapp.com");
                
                response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
                response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
                response.header("Access-Control-Allow-Credentials", "true");
                //set up their session 
                request.session(true);
            });

            configurePlayerStatusEndpoints();
            configureLobbiesEndpoints();
            configureImagesEndpoints();
            configurePromptsEndpoints();
            
            
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
                post("", (request,response) ->{
                    //Gson gson = new Gson();
                    HashMap<String,Object> map = new HashMap();
                    if(request.body() != null){
                        try{
                            Lobby updated = (Lobby) gson.fromJson(request.body(), Lobby.class);
                            if(updated != null){
                                if(request.session().attribute("CURRENT_GAME") != null){    
                                    return GameManager.getInstance().UpdateLobbySettings(
                                            request.session(),
                                            updated.getName(),
                                            updated.getMaxRounds(), 
                                            updated.getMaxPlayers(), 
                                            updated.getPassword());
                                } else halt();
                                
                            } else halt();
                        }catch(JsonSyntaxException js){
                            System.err.println(js.getMessage());
                            halt(400, js.getMessage());
                        }
                    }
                    halt();
                    return "";
                   
                });
                
                /**
                 * Join this lobby
                 */
                before("/join", (request, response) -> {
                    if(request.session().attribute("CURRENT_GAME") != null)
                        halt(403, "you already have a game in your session");
//                    System.out.println(request.body());
//                    System.out.println(request.body());
                });
                post("/join", (request,response) ->{
                    if(request.body() != null){
                        try{
                            HashMap<String,Object> map = new HashMap();
                            map = (HashMap<String,Object>) gson.fromJson(request.body(), map.getClass());
                            if(map != null && map.containsKey("password")){
                                String password = (String) map.get("password");
                                if( GameManager.getInstance().JoinLobby(
                                    request.session(), 
                                    Integer.parseInt(request.params(":lobby_id")),
                                    password
                                )){
                                    return GameManager.getInstance().getLobby(Integer.parseInt(request.params(":lobby_id")));
                                }
                                halt(403, "password not correct");
                            }
                            halt(403, "password field not present");
                            
                        }catch(JsonSyntaxException js){
                            System.err.println(js.getMessage());
                            halt(400, js.getMessage());
                        }
                    }
                    
                    response.status(403);
                    return "{\"message\": \"nope, stars. can't do it\"}";
                });
                
                /**
                 * Start the game, must be admin
                 */
                post("/begin", (request,response) ->{
                    return GameManager.getInstance().StartGame(request.session());
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
                
                /**
                * Restart the lobby
                */
                post("/restart", (request,response) ->{
                    String data = GameManager.getInstance().RestartLobby(request.session());
                    if(data == null)
                        halt(400);
                    return data;
                });
                
                
                configureSubmitEndpoints();
                
            });
            
                    
        });
    }
    
    /**
     * Set up routes for submitting plays
     */
    public static void configureSubmitEndpoints(){
        before("/submitdrawing", (request, response) -> {
            if(request.session().attribute("CURRENT_GAME") == null)
                halt(403, "{\"message\": \"you dont have a game in your session\"");
        });
        post("/submitdrawing", (request,response) ->{
//            int lobby, booklet, page;
            try{
//                lobby = request.session().attribute("CURRENT_GAME");
//                booklet = Integer.parseInt(request.params(":booklet"));
//                page = Integer.parseInt(request.params(":page"));

                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                try{
                    InputStream input = request.raw().getPart("uploaded_file").getInputStream();
                    String fileName = GameManager.getInstance().SubmitDrawing(
                            request.session().id(),
                            (request.session().attribute("CURRENT_GAME")),
                            input);
                            
                            
                            
//                    String fileName = ResourceManager.getInstance().saveImage(
//                        lobby, booklet, page, input);
//                    System.out.println("saved to"+ fileName);
                    return "{\"url\": \""+fileName+"\"}";
                }catch(IOException e){
                    System.err.println(e);
                }
            }catch(NumberFormatException e){
                System.err.println(e);
                halt(406, "invalid id format");
            }
            return ""; 
////            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
//            if(request.body() != null){
//                if(request.session().attribute("CURRENT_GAME") != null){
//                    try{
////                        if(request.raw().getPart("uploaded_file") == null)
////                            halt(400);
//                        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
//                        InputStream pictureStream = request.raw().getPart("uploaded_file").getInputStream();
//                        if( GameManager.getInstance().SubmitDrawing(
//                                request.session().id(),
//                                (request.session().attribute("CURRENT_GAME")),
//                                pictureStream))
//                            return "good stuff";
//                    }catch(IOException e){
//                        System.err.println(e);
//                        halt(500);
//                    }
//                } else halt(403);   
//            }
//            return "";
        });



        before("/submitguess", (request, response) -> {
            if(request.session().attribute("CURRENT_GAME") == null)
                halt(403, "{\"message\": \"you dont have a game in your session\"");
        });
        post("/submitguess", (request,response) ->{
            
            if(request.body() != null){
                if(request.session().attribute("CURRENT_GAME") != null){
                    try{
                        //Gson gson = new Gson();
                        HashMap<String,Object> map = new HashMap();
                        map = (HashMap<String,Object>) gson.fromJson(request.body(), map.getClass());
                        int currentGame = request.session().attribute("CURRENT_GAME");
                        String playerID = request.session().id();
                        if(map != null && map.containsKey("guess")){
                            String guess = (String) map.get("guess");
//                            System.out.println("submitting" + guess);
                            if( GameManager.getInstance().SubmitGuess(
                                    playerID,
                                    currentGame,
                                    guess))
                            return "good stuff";
                        }
                    }catch(JsonSyntaxException js){
                        System.err.println(js);
                        halt(400, js.getMessage());

                    }

                } else halt(403);   
            }
            return "";
        });
    }
    
//    /**
//     * Set all current Game Endpoints, mainly to be used for accessing the sessions game data without knowing the ID
//     */
//    public static void configureCurrentLobbyEndpoints(){
//        path("/current", () -> {
//            //make sure the user is in a Lobby
//            before("", (request, response) -> {
//                if(request.session().attribute("CURRENT_GAME") == null)
//                    halt(403, "{\"message\": \"you're not in a game\"}");
//            });
//            /**
//             * Retrun the data of the game the session is currently in
//             */
//            get("", (request,response) ->{
//                    String data = GameManager.getInstance().getLobby(request.session().attribute("CURRENT_GAME"));
//                    if(data == null)
//                        halt(400);
//                    return data;
//                });
//            
//        });
//    }
    
    /**
     * Set all routes for image manipulation
     */
    public static void configureImagesEndpoints(){
        path("/images", () ->{
            configureTwitterUpload();
//            //testing stuff
//            get("/testimage", (request,response) ->{
//                response.type("image/png");
//                if(tempDir.listFiles().length > 0){
//                    byte[] data = null;
//                    try {
//                        data = Files.readAllBytes(tempDir.listFiles()[0].toPath());
//                    } catch (Exception e) {e.printStackTrace();}
//                    HttpServletResponse raw = response.raw();
//    //                response.header("Content-Disposition", "attachment; filename=image.jpg");
//                    response.type("image/png");
//                    if(data != null)
//                    try {
//                        raw.getOutputStream().write(data);
//                        raw.getOutputStream().flush();
//                        raw.getOutputStream().close();
//                    } catch (Exception e) {e.printStackTrace();}
//                    return raw;
//                }
//                return null;
//            });
//
//            post("/testimage", (request,response) ->{
//                Path tempFile = new File(tempDir.getPath()+"/temp.png").toPath();//Files.createTempFile(tempDir.toPath(), "temp", ".png");
//    //            Path tempFile = Files.createTempFile(tempDir.toPath(), "temp", ".png");
//                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
//                try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) {
//                    Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
//                }
//                return "<h1>You uploaded this image:<h1><img src='" + tempFile.getFileName()+ "'>";
//            });
            
            get("/:lobby/:booklet/:page", (request,response) ->{
                System.out.println("image hit");
                response.type("image/png");
                byte[] data = null;
                int lobby, booklet, page;
                try{
                    lobby = Integer.parseInt(request.params(":lobby"));
                    booklet = Integer.parseInt(request.params(":booklet"));
                    page = Integer.parseInt(request.params(":page"));
                    
                    data = ResourceManager.getInstance().getImage(
                        lobby, booklet, page);
                    if(data != null){
                        HttpServletResponse raw = response.raw();
                        response.type("image/png");
                        try {
                            raw.getOutputStream().write(data);
                            raw.getOutputStream().flush();
                            raw.getOutputStream().close();
                            return raw;
                        } catch (IOException e) {
                            e.printStackTrace();
                            halt(500, "error writing image data to response");
                        }
                    }else{
                        halt(404, "Image does not exist");
                    }
                    
                }catch(NumberFormatException e){
                    halt(406, "invalid id format");
                }
                return "";   
            });
            
            post("/:lobby/:booklet/:page", (request,response) ->{
                int lobby, booklet, page;
                try{
                    lobby = Integer.parseInt(request.params(":lobby"));
                    booklet = Integer.parseInt(request.params(":booklet"));
                    page = Integer.parseInt(request.params(":page"));
                    
                    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                    try{
                        InputStream input = request.raw().getPart("uploaded_file").getInputStream();
                        String fileName = ResourceManager.getInstance().saveImage(
                            lobby, booklet, page, input);
                        return "{\"url\": \""+fileName+"\"}";
                    }catch(IOException e){
                        System.err.println(e);
                    }
                }catch(NumberFormatException e){
                    System.err.println(e);
                    halt(406, "invalid id format");
                }
                return "";   
            });
 
        });
        
    }
    
    /**
     * Set all routes for checking a players status
     */
    public static void configurePlayerStatusEndpoints(){
        path("/me", () ->{ 
            /**
             * Returns high level information about the player's state
             */
            get("", (request,response) ->{
                return JSONifyPlayerStatus(request.session());                
            });
            
            /**
             * Sets the name of the player
             */
            post("/name", (request,response) ->{
                //Gson gson = new Gson();
                HashMap<String,Object> map = new HashMap();
                
                
                if(request.body() != null){
                    try{
                        map = (HashMap<String,Object>) gson.fromJson(request.body(), map.getClass());
                        if(map != null && map.containsKey("name")){
                            String name = (String) map.get("name");
                            name = name.trim();
                            if(name.compareTo("") == 0)
                                halt(400, name + " is not a valid name");
                            request.session().attribute("NAME", name);//TBD true in session
//                            System.out.println("name set to session");
                            if(request.session().attribute("CURRENT_GAME") != null){
                                GameManager.getInstance().UpdatePlayerName(request.session());
                            }
                        }
                    }catch(JsonSyntaxException js){
                        halt(400, js.getMessage());
                    
                    }
                }
                
                return JSONifyPlayerStatus(request.session());
                
            });
            
            /**
             * Return the player's lobby data
             * @EQ  1: the player has a lobby
             *      2: the player is not in a lobby
             */
            before("/lobby", (request, response) -> {
                if(request.session().attribute("CURRENT_GAME") == null)
                    halt(403, "{\"message\": \"you're not in a game\"}");
            });
            /**
             * Retrun the data of the game the session is currently in
             */
            get("/lobby", (request,response) ->{
                String data = GameManager.getInstance().getLobby(request.session().attribute("CURRENT_GAME"));
                if(data == null)
                    halt(400);
                return data;
            });
            
            post("/vote", (request, response) -> {
                
                if(request.session().attribute("CURRENT_GAME") == null)
                    halt(403);
                String lobby = GameManager.getInstance().getLobby(request.session().attribute("CURRENT_GAME"));
                if(lobby == null)
                    halt(400);
                if(request.body() != null){
                    //Gson gson = new Gson();
                    HashMap<String,Object> map = new HashMap();
                    try{
                        map = (HashMap<String,Object>) gson.fromJson(request.body(), map.getClass());
                        if(map != null && map.containsKey("booklet") && map.containsKey("round")){
                            int currGame = request.session().attribute("CURRENT_GAME");
                            String booklet = (String)map.get("booklet");
                            int round = (int)Math.round((Double)map.get("round"));
                            return GameManager.getInstance().voteOn(request.session().id(), currGame, booklet, round );
                        }
                    }catch(JsonSyntaxException js){
                        halt(400, js.getMessage());
                    
                    }
                }
                return null;
            });
            
            configureSubmitEndpoints();
                
            
            
            
        });
    }
    
    /**
     * Set all routes for getting prompts
     */
    public static void configurePromptsEndpoints(){
        get("/prompts", (request,response) ->{
            return ResourceManager.getInstance().getPrompts(10);
        });
    }
    
    /**
     * Gets the port that the server should be running on
     * @return 
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
    
    private static String JSONifyPlayerStatus(Session playerSession){
        String name;
        if(playerSession.attribute("NAME") != null)
            name = "\""+playerSession.attribute("NAME")+"\"";
        else{
            System.err.println("playerSession.attribute(\"NAME\") == null");
            name = null;
        }
        
        String data = "{"
                + "\"id\": \""+playerSession.id()+"\","
                + "\"name\": "+name+","
                + "\"signed_in\": false,"//todo 
                + "\"current_game\": "+playerSession.attribute("CURRENT_GAME")
                +"}";
        return data;
    }
}
