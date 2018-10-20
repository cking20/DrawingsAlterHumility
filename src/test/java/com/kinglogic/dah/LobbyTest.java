/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

import com.kinglogic.dah.Lobby;
import com.kinglogic.dah.Player;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
/**
 *
 * @author chris
 */
public class LobbyTest {
    @Test
    public void TestLobby(){
        Lobby test1 = new Lobby(0);
        assertEquals(test1.getId(), 0);
        assertEquals(test1.getPassword(), null);   
        assertEquals(test1.getRoundNumber(), -1);
    }
    
    @Test
    public void TestReset(){
        //EQ:0 - new Lobby
        Lobby test = new Lobby(0);
        assertEquals(test.Reset(), true);
        //EQ:1 - lobby checkedout
        test.Checkout("adminID", "adminName");
        assertEquals(test.Reset(), false);
        //EQ:2 - lobby released
        test.Release();
        assertEquals(test.Reset(), true);
    }
    
    @Test
    public void TestCheckout(){
        //EQ:0 - Lobby not checked out
        Lobby test0 = new Lobby(0);
        assertEquals(test0.Checkout("", ""), true);
        
        //EQ:1 - Lobby already checked out
        Lobby test1 = new Lobby(0);
        assertEquals(test1.Checkout("adminID", "adminName"), true);
        assertEquals(test1.Checkout("",""), false);
    }
    
    @Test
    public void TestRestart(){
        Lobby test;
        //EQ:0 - Admin and player in lobby not started
        test = new Lobby(0);
        test.Checkout("admin", "admin");
        test.JoinPlayer("player", "playerName", null);
        //Only the admin can checkout the lobby
        assertEquals(test.Restart("player"), false);
        assertEquals(test.Restart("random"), false);
        assertEquals(test.Restart("admin"), true);
        //The game is in the inlobby state
        assertEquals(test.getRoundNumber(), -1);
        assertEquals(test.isIsInProgress(), false);
        //No players were kicked out
        assertEquals(test.getPlayers().size(), 2);
        
        //EQ:1 Many players in lobby started
        test = new Lobby(0);
        test.Checkout("admin", "admin");
        test.JoinPlayer("player", "playerName", null);
        test.JoinPlayer("player2", "playerName", null);
        test.JoinPlayer("player3", "playerName", null);
        test.JoinPlayer("player4", "playerName", null);
        test.JoinPlayer("player5", "playerName", null);
        //Only the admin can checkout the lobby
        assertEquals(test.Restart("player"), false);
        assertEquals(test.Restart("random"), false);
        assertEquals(test.Restart("admin"), true);
        //The game is in the inlobby state
        assertEquals(test.getRoundNumber(), -1);
        assertEquals(test.isIsInProgress(), false);
        //No players were kicked out
        assertEquals(test.getPlayers().size(), 6);
    }
    
    @Test
    public void TestJoinPlayer(){
        Lobby test;
        
        //EQ:0 - A player attempts to join a lobby not checkedout
        test = new Lobby(0);
        assertEquals(test.JoinPlayer(null, null, null), false);
        assertEquals(test.JoinPlayer("", "", ""), false);
        assertEquals(test.JoinPlayer("1", "", null), false);
        
        //EQ:0 - A player attempts to join a lobby that is private
        test = new Lobby(0);
        test.Checkout("admin", "admin");
        test.setPassword("password");
        assertEquals(test.JoinPlayer(null, null, null), false);
        assertEquals(test.JoinPlayer("", "", ""), false);
        assertEquals(test.JoinPlayer("1", "", null), false);
        assertEquals(test.getPlayers().size(), 1);
        assertEquals(test.JoinPlayer("1", "", "password"), true);
        assertEquals(test.getPlayers().size(), 2);
        
        //EQ:2 - A player trys to join when already in the lobby
        assertEquals(test.JoinPlayer("1", "", "password"), false);
        assertEquals(test.getPlayers().size(), 2);
         
        //EQ:3 - A player trys to join when it is full 
        
        //TBD
        
        
    }
    
    @Test
    public void TestRemovePlayer(){
        Lobby test = new Lobby(0);
        test.Checkout("admin", "admin");
        test.JoinPlayer("player1", "1", null);
        test.JoinPlayer("player2", "2", null);
        //EQ:0 - A player attempts to leave
        assertEquals(test.RemovePlayer("player1"),true);
        assertEquals(test.getPlayers().size(), 2);
        //EQ:1 - The last player attempts to leave
        assertEquals(test.RemovePlayer("player2"),true);
        assertEquals(test.getPlayers().size(), 1);
        //EQ:3 - A player not in the game attempts to leave
        assertEquals(test.RemovePlayer("player76"), false);
        
        
    }
    
    
    @Test
    public void TestGetPlayerData(){
        Lobby test;
        
        test = new Lobby(0);
        test.Checkout("admin", "name");
        Player admin = test.getPlayerData("admin");
        assert admin != null;
        assertEquals(admin.name, "name");
        
        test = new Lobby(0);
        test.Checkout("admin", null);
        Player admin2 = test.getPlayerData("admin");
        assert admin2 != null;
        assertEquals(admin2.name, null); 
    }
    
    
    @Test
    public void TestUpdatePlayerName(){
        Lobby test;
        
        //EQ:0 - Attempting to update a player not in the lobby
        test = new Lobby(0);
        test.updatePlayerName("notHere", null);
        test.updatePlayerName("notHere", "something");
        test.updatePlayerName(null, "something");
        test.updatePlayerName(null, null);
        
        //EQ:1 - Attemptin to update a player in the lobby
        test = new Lobby(0);
        test.Checkout("admin", "name");
        test.updatePlayerName("admin", null);
        assertEquals(test.getPlayerData("admin").name, null);
        test.updatePlayerName("admin", "cool name");
        assertEquals(test.getPlayerData("admin").name, "cool name");
        
    }

    @Test
    public void TestStartGame(){
        Lobby test;
        
        //EQ:0 - The lobby is not checked out
        test = new Lobby(0);
        test.StartGame();
        assertEquals(test.getRoundNumber(), -1);
        //subsequent calls still do nothing
        test.StartGame();
        assertEquals(test.getRoundNumber(), -1);
        
        //EQ:1 - The lobby is checked out
        test = new Lobby(0);
        test.Checkout("admin", "admin");
        test.StartGame();
        assertEquals(test.getRoundNumber(), 0);
        //subsequent calls should still do nothing
        test.StartGame();
        assertEquals(test.getRoundNumber(), 0);
    }
    
    @Test
    public void TestAdvanceRound(){
        try {
            Lobby test;
            Method method = Lobby.class.getDeclaredMethod("AdvanceRound");
            method.setAccessible(true);
            
            //EQ:0 - the lobby is not started
            test = new Lobby(0);
            test.Checkout("admin", "admin");
            method.invoke(test);
            assertEquals(0, test.getRoundNumber());
            
            //EQ:1 - the lobby is started, not the last turn of the game
            test = new Lobby(0);
            test.Checkout("admin", "admin");
            method.invoke(test);
            assertEquals(0, test.getRoundNumber());
            method.invoke(test);
            assertEquals(1, test.getRoundNumber());
            assertEquals(test.getPlayerData("admin").submitted, false);
            //EQ:2 - the lobby is in progress and the last turn of the game
            test = new Lobby(0);
            test.Checkout("admin", "admin");
            test.setMaxRounds(1);
            method.invoke(test);//start
            assertEquals(0, test.getRoundNumber());
            assertEquals(true, test.isIsInProgress());
            assertEquals(test.getPlayerData("admin").submitted, false);
            
            method.invoke(test);//all submit -> advance
            assertEquals(1, test.getRoundNumber());
            assertEquals(true, test.isIsInProgress());
            assertEquals(test.getPlayerData("admin").submitted, false);
            
            method.invoke(test);//all submit -> advance
            assertEquals(2, test.getRoundNumber());
            assertEquals(true, test.isIsInProgress());
            assertEquals(test.getPlayerData("admin").submitted, false);
            
            method.invoke(test);//all submit -> over
            assertEquals(2, test.getRoundNumber());
            assertEquals(false, test.isIsInProgress());
            assertEquals(test.getPlayerData("admin").submitted, false);
            
            method.invoke(test);//it should still be over
            assertEquals(2, test.getRoundNumber());
            assertEquals(false, test.isIsInProgress());
            assertEquals(test.getPlayerData("admin").submitted, false);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (SecurityException ex) {
            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (InvocationTargetException ex) {
            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
    
    @Test
    public void TestCheckRoundAdvance(){
        Lobby test = new Lobby(0);
        
        //EQ:0: The lobby is not checked out
        assertEquals(-1, test.CheckRoundAdvance());
        test.Checkout("admin", "admin");
        assertEquals(-1, test.CheckRoundAdvance());
        test.StartGame();
        assertEquals(0, test.CheckRoundAdvance());
        test.getPlayerData("admin").submitted = true;
        assertEquals(1, test.CheckRoundAdvance());
        
    }
    
    @Test
    public void TestSwapBooklets(){
        try {
            Lobby test;// = new Lobby(0);
            Method method = Lobby.class.getDeclaredMethod("SwapBooklets");
//            method.setAccessible(true);
            
            test = new Lobby(0);
            test.Checkout("admin", "admin");
            test.JoinPlayer("p1", "n1", null);
            test.StartGame();
            test.SwapBooklets();
//            method.invoke(test);
            
            assertEquals("p1", test.getPlayerData("p1").currentBooklet.getUser());
            assertEquals("admin", test.getPlayerData("p1").currentBooklet.getOwner());
            
            assertEquals("admin", test.getPlayerData("admin").currentBooklet.getUser());
            assertEquals("p1", test.getPlayerData("admin").currentBooklet.getOwner());
            
            
            
            
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (SecurityException ex) {
            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
//            fail();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
//        } catch (InvocationTargetException ex) {
//            Logger.getLogger(LobbyTest.class.getName()).log(Level.SEVERE, null, ex);
//            fail();
//        }
    }
}
