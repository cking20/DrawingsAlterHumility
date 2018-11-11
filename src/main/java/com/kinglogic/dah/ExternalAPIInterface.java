/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.client.HttpClient;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 *
 * @author chris
 */
public class ExternalAPIInterface {
    private static ExternalAPIInterface instance; 
    
    public static ExternalAPIInterface getInstance(){
        if(instance == null){
            instance = new ExternalAPIInterface();
        }
        return instance;
    }
    private ExternalAPIInterface(){
        try {
            Twitter twitter = TwitterFactory.getSingleton();
            twitter.setOAuthConsumer(
                    "gCboW23uWsEZYmgI91qP3bJf1",
                    "BEuDyz9BZdyXHezIWn2EVgDIOVb7099gfbgmo9gtUuji5P5PSq");
            AccessToken token = new AccessToken(
                    "1042432383474184192-jp0RQ75rFxx2K6Y817Hcx8GFYTAnnu",
                    "g45GLkTXgrxcxVoKwK58fHqvUaDpOnah1qxNM3BWwp7ks");
            twitter.setOAuthAccessToken(token);
            twitter.verifyCredentials().getId();
        } catch (TwitterException ex) {
            Logger.getLogger(ExternalAPIInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Connects to the Twitter API
     * @param image
     * @return the connection
     */
//    public static HttpURLConnection UploadImageToTwitter(String imagePath){
//        URL myURL; 
//        HttpURLConnection myConn;
//        
//        try{
//            myURL = new URL("https://upload.twitter.com/1.1/media/upload.json");
//            myConn = (HttpURLConnection)myURL.openConnection();
//            myConn.setRequestMethod("POST");
//            myConn.setRequestProperty("Content-Type","multipart/form-data"); 
//            myConn.setDoOutput(true);
//            
//            
//            Gson gson = new Gson();
//            HashMap<String,String> json = new HashMap();
//            //JSONObject json= new JSONObject();
//
//            OAuthHmacSigner oauth = new OAuthHmacSigner();
//            oauth.clientSharedSecret = "";
//            oauth.tokenSharedSecret = "";
//            //String signature = oauth.computeSignature("BASE????");
//            String nonce = UUID.randomUUID().toString();
//            
//            long time = System.currentTimeMillis() / 10000;
//            String authString = 
//                    "OAuth oauth_consumer_key=\"gCboW23uWsEZYmgI91qP3bJf1 \",\n" +
//                    "oauth_nonce=\""+nonce+"\",\n" +
//                    "oauth_signature=\"tnnArxj06cWHq44gCs1OSKk%2FjLY%3D\",\n" +
//                    "oauth_signature_method=\"HMAC-SHA1\",\n" +
//                    "oauth_timestamp=\""+time+"\",\n" +
//                    "oauth_token=\"1042432383474184192-znu6kC8kCWgidYnf7rU1DDmrIXGog4\",\n" +
//                    "oauth_version=\"1.0\"";
//
//            json.put("Authorization", authString);
//            json.put("user-id", "cking20");
//            json.put("api-key", "gCboW23uWsEZYmgI91qP3bJf1");
//            json.put("media_data", imagePath);
//            byte[] outputInBytes = gson.toJson(json).getBytes("UTF-8");
//            try (OutputStream os = myConn.getOutputStream()) {
//                os.write( outputInBytes );
//            }
//            myConn.connect();
//            return myConn;  
//        }catch(MalformedURLException e){
//            System.err.println("url");
//            e.printStackTrace();
//        }catch(IOException e){
//            
//            System.err.println("IOEXCEPTION");
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * Connects to the Twitter API, and uploads an image
     * @param fileName
     * @return the connection
     */
    public String UploadImageToTwitterUsing4J(String fileName){
        try {
            Twitter twitter = TwitterFactory.getSingleton();
//            twitter.updateStatus("hello world");
            File file = new File(ResourceManager.persistDir+"/"+fileName); 
            StatusUpdate status = new StatusUpdate("Hello Image");
            status.setMedia(file); // set the image to be uploaded here.
            Status updated = twitter.updateStatus(status);
//            twitter.uploadMedia("fileName", strm);
            return updated.getText();
        } catch (TwitterException ex) {
            Logger.getLogger(ExternalAPIInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "this is bad";
    }

}
