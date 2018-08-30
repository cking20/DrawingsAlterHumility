/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

import org.json.JSONException;
import org.json.JSONObject;
import spark.Spark;
import static spark.Spark.*;
/**
 *
 * @author chris
 */
public class ServerApp {
    public static void main(String[] args){
        staticFileLocation("public");
        port(8080);
        configureRoutes();
    }
    
    public static void configureRoutes(){
        
        
        path("/api", () -> {
            //return JSON
            before("/*", (request, response) -> {
                response.type("application/json");
            });
            
            
            get("/hello", (request,response) ->{
                return "{\"hello\": Ow0}";
            });
        });      
    }
}
