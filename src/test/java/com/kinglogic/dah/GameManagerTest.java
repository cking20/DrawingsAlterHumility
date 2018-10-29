/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinglogic.dah;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mock.web.MockHttpServletRequest;
import spark.Request;
import spark.RequestResponseFactory;


/**
 *
 * @author chris
 */
public class GameManagerTest {
    
    public GameManagerTest() {
    }

    /**
     * Test of getInstance method, of class GameManager.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        GameManager expResult = GameManager.getInstance();
        GameManager result = GameManager.getInstance();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLobby method, of class GameManager.
     */
    @Test
    public void testGetLobby() {
        System.out.println("getLobby");
        //EQ:0 - Get negative lobby 
        String t1 = GameManager.getInstance().getLobby(-1);
        assertEquals(null, t1);
        //EQ:0 - Get positive lobby in range
        String t2 = GameManager.getInstance().getLobby(0);
        assertNotEquals(null, t2);
        String t3 = GameManager.getInstance().getLobby(1);
        assertNotEquals(null, t3);
        //EQ:0 - Get positive lobby out of range
        String t4 = GameManager.getInstance().getLobby(GameManager.MAX_LOBBIES);
        assertEquals(null, t4);
        String t5 = GameManager.getInstance().getLobby(GameManager.MAX_LOBBIES+1);
        assertEquals(null, t5);
    }

    /**
     * Test of getJoinableLobbies method, of class GameManager.
     */
    @Test
    public void testGetJoinableLobbies() {
        System.out.println("getJoinableLobbies");
        //EQ:0 - Not Acceptable Ranges
        assertEquals(null, GameManager.getInstance().getJoinableLobbies(-1, -1));
        assertEquals(null, GameManager.getInstance().getJoinableLobbies(-1, 0));
        assertEquals(null, GameManager.getInstance().getJoinableLobbies(0, -1));
        assertEquals(null, GameManager.getInstance().getJoinableLobbies(GameManager.MAX_LOBBIES+1, GameManager.MAX_LOBBIES+1));
        assertEquals(null, GameManager.getInstance().getJoinableLobbies(0, GameManager.MAX_LOBBIES+1));
        assertEquals(null, GameManager.getInstance().getJoinableLobbies(GameManager.MAX_LOBBIES+1, 0));
        
        //EQ:1 - Acceptable Ranges
        assertNotEquals(null, GameManager.getInstance().getJoinableLobbies(0, 0));
        assertNotEquals(null, GameManager.getInstance().getJoinableLobbies(1, 1));
        assertNotEquals(null, GameManager.getInstance().getJoinableLobbies(GameManager.MAX_LOBBIES-1, GameManager.MAX_LOBBIES-1));
        assertNotEquals(null, GameManager.getInstance().getJoinableLobbies(1, GameManager.MAX_LOBBIES-1));
        assertNotEquals(null, GameManager.getInstance().getJoinableLobbies(GameManager.MAX_LOBBIES-1, 1));
    }

    /**
     * Test of CheckoutLobby method, of class GameManager.
     */
    @Test
    public void testCheckoutLobby() {
        TearDownGameManager();
        System.out.println("CheckoutLobby");
        
        MockHttpServletRequest mock = new MockHttpServletRequest();
        Request scaffold = RequestResponseFactory.create(mock);
        //EQ:0 - No lobbies have been checked out
        assertNotEquals(null, GameManager.getInstance().CheckoutLobby(scaffold.session()));
        assertEquals((Integer)0, scaffold.session().attribute("CURRENT_GAME"));
        assertEquals(true, GameManager.getInstance().getLobby(0).contains("\"isReleased\":false"));
        //EQ:1 - One lobby has been checked out
        assertNotEquals(null, GameManager.getInstance().CheckoutLobby(scaffold.session()));
        assertEquals((Integer)1, scaffold.session().attribute("CURRENT_GAME"));
        assertEquals(true, GameManager.getInstance().getLobby(1).contains("\"isReleased\":false"));
        //EQ:1 - All lobbies have been checkedout
        for (int i = 2; i < GameManager.MAX_LOBBIES; i++) {
            assertEquals(true, GameManager.getInstance().CheckoutLobby(scaffold.session()).contains("\"isReleased\":false"));
            assertEquals((Integer)i, scaffold.session().attribute("CURRENT_GAME"));        
        }
        assertEquals(null, GameManager.getInstance().CheckoutLobby(scaffold.session()));
        assertEquals((Integer)(GameManager.MAX_LOBBIES-1), scaffold.session().attribute("CURRENT_GAME"));
        TearDownGameManager();
        
        
    }

    /**
     * Test of ReleaseLobby method, of class GameManager.
     */
    @Test
    public void testReleaseLobby() {
        System.out.println("ReleaseLobby");
        TearDownGameManager();
        //scaffold
        MockHttpServletRequest mock = new MockHttpServletRequest();
        Request scaffold = RequestResponseFactory.create(mock);
        //EQ:0 - The lobby is checked out by the player
        GameManager.getInstance().CheckoutLobby(scaffold.session());
        assertEquals(true, GameManager.getInstance().getLobby(0).contains("\"isReleased\":false"));
        GameManager.getInstance().ReleaseLobby(scaffold.session());
        assertEquals(true, GameManager.getInstance().getLobby(0).contains("\"isReleased\":true"));
        //EQ:1 - The lobby is checked out, and a non admin tries to release it
        mock = new MockHttpServletRequest();
        scaffold = RequestResponseFactory.create(mock);
        MockHttpServletRequest mock2 = new MockHttpServletRequest();
        Request scaffold2 = RequestResponseFactory.create(mock2);
        GameManager.getInstance().CheckoutLobby(scaffold.session());
        assertEquals(true, GameManager.getInstance().getLobby(0).contains("\"isReleased\":false"));
        assertEquals(null,GameManager.getInstance().ReleaseLobby(scaffold2.session()));
        assertEquals(true, GameManager.getInstance().getLobby(0).contains("\"isReleased\":false"));
        GameManager.getInstance().ReleaseLobby(scaffold.session());
        
    }
    

    /**
     * Test of JoinLobby method, of class GameManager.
     */
    @Test
    public void testJoinLobby() {
        //EQ:0 - Player tries to join checkedout lobby with no password
        TearDownGameManager();
        MockHttpServletRequest mock = new MockHttpServletRequest();
        Request scaffold_admin = RequestResponseFactory.create(mock);
        MockHttpServletRequest mock2 = new MockHttpServletRequest();
        Request scaffold_player = RequestResponseFactory.create(mock2);
        GameManager.getInstance().CheckoutLobby(scaffold_admin.session());
        
        assertEquals(true, GameManager.getInstance().JoinLobby(scaffold_player.session(), 0, null));
        assertEquals((Integer)0, scaffold_player.session().attribute("CURRENT_GAME"));
        
        GameManager.getInstance().ReleaseLobby(scaffold_admin.session());
        
        //EQ:0 - Player tries to join checkedout lobby with password
        mock = new MockHttpServletRequest();
        scaffold_admin = RequestResponseFactory.create(mock);
        mock2 = new MockHttpServletRequest();
        scaffold_player = RequestResponseFactory.create(mock2);
        GameManager.getInstance().CheckoutLobby(scaffold_admin.session());
        assertNotEquals(null, GameManager.getInstance().UpdateLobbySettings(scaffold_admin.session(), "", 10, 10, "thisisthepassword"));
        assertEquals(true, GameManager.getInstance().getLobby(0).contains("thisisthepassword"));
        
        assertEquals(false, GameManager.getInstance().JoinLobby(scaffold_player.session(), 0, null));
        assertEquals(null, (Integer)scaffold_player.session().attribute("CURRENT_GAME"));
        
        assertEquals(false, GameManager.getInstance().JoinLobby(scaffold_player.session(), 0, "not the password"));
        assertEquals(null, (Integer)scaffold_player.session().attribute("CURRENT_GAME"));
        
        assertEquals(true, GameManager.getInstance().JoinLobby(scaffold_player.session(), 0, "thisisthepassword"));
        assertEquals((Integer)0, scaffold_player.session().attribute("CURRENT_GAME"));
        
        GameManager.getInstance().ReleaseLobby(scaffold_admin.session());
        
        //EQ:0 - Player tries to join released lobby
        mock = new MockHttpServletRequest();
        scaffold_admin = RequestResponseFactory.create(mock);
        mock2 = new MockHttpServletRequest();
        scaffold_player = RequestResponseFactory.create(mock2);
        
        assertEquals(false, GameManager.getInstance().JoinLobby(scaffold_player.session(), 0, null));
        assertEquals(null, (Integer)scaffold_player.session().attribute("CURRENT_GAME"));
        
        assertEquals(false, GameManager.getInstance().JoinLobby(scaffold_player.session(), 0, "not the password"));
        assertEquals(null, (Integer)scaffold_player.session().attribute("CURRENT_GAME"));
                
        //EQ:3 - Player in one session tries to join another   
        mock = new MockHttpServletRequest();
        scaffold_admin = RequestResponseFactory.create(mock);
        mock2 = new MockHttpServletRequest();
        scaffold_player = RequestResponseFactory.create(mock2);
        MockHttpServletRequest mock3 = new MockHttpServletRequest();
        Request scaffold_admin2 = RequestResponseFactory.create(mock3);
        
        GameManager.getInstance().CheckoutLobby(scaffold_admin.session());
        GameManager.getInstance().CheckoutLobby(scaffold_admin2.session());
        assertEquals(true, GameManager.getInstance().JoinLobby(scaffold_player.session(), 0, null));
        assertEquals((Integer)0, scaffold_player.session().attribute("CURRENT_GAME"));
        
        assertEquals(false, GameManager.getInstance().JoinLobby(scaffold_player.session(), 1, null));
        assertEquals((Integer)0, scaffold_player.session().attribute("CURRENT_GAME"));
        
        GameManager.getInstance().ReleaseLobby(scaffold_admin.session());
        GameManager.getInstance().ReleaseLobby(scaffold_admin2.session());
        
        TearDownGameManager();
        
    }

    /**
     * Test of LeaveLobby method, of class GameManager.
     */
    @Test
    public void testLeaveLobby() {
        MockHttpServletRequest mock = new MockHttpServletRequest();
        Request scaffold_admin = RequestResponseFactory.create(mock);
        MockHttpServletRequest mock2 = new MockHttpServletRequest();
        Request scaffold_player1 = RequestResponseFactory.create(mock2);
        MockHttpServletRequest mock3 = new MockHttpServletRequest();
        Request scaffold_player2 = RequestResponseFactory.create(mock3);
        GameManager.getInstance().CheckoutLobby(scaffold_admin.session());
        //EQ:0 - Player leaves a lobby when theyre not in one
        assertEquals(false, GameManager.getInstance().LeaveLobby(scaffold_player1.session()));
        TearDownGameManager();
        //EQ:1 - Player leaves a lobby when they are in one, and they are the only one
        //in the lobby
        mock = new MockHttpServletRequest();
        scaffold_admin = RequestResponseFactory.create(mock);
        mock2 = new MockHttpServletRequest();
        scaffold_player1 = RequestResponseFactory.create(mock2);
        mock3 = new MockHttpServletRequest();
        scaffold_player2 = RequestResponseFactory.create(mock3);
        
        GameManager.getInstance().CheckoutLobby(scaffold_admin.session());
        assertEquals(true, GameManager.getInstance().LeaveLobby(scaffold_admin.session()));
        assertEquals(null, (Integer)scaffold_admin.session().attribute("CURRENT_GAME"));
        TearDownGameManager();
        //EQ:2 - Player leaves a lobby when they are in one, and there are more 
        //players in the lobby
        mock = new MockHttpServletRequest();
        scaffold_admin = RequestResponseFactory.create(mock);
        mock2 = new MockHttpServletRequest();
        scaffold_player1 = RequestResponseFactory.create(mock2);
        mock3 = new MockHttpServletRequest();
        scaffold_player2 = RequestResponseFactory.create(mock3);
        
        GameManager.getInstance().CheckoutLobby(scaffold_admin.session());
        GameManager.getInstance().JoinLobby(scaffold_player1.session(), 0, null);
        GameManager.getInstance().JoinLobby(scaffold_player2.session(), 0, null);
        
        assertEquals(true, GameManager.getInstance().LeaveLobby(scaffold_admin.session()));
        assertEquals(null, (Integer)scaffold_admin.session().attribute("CURRENT_GAME"));
        assertEquals((Integer)0, scaffold_player1.session().attribute("CURRENT_GAME"));
        assertEquals((Integer)0, scaffold_player2.session().attribute("CURRENT_GAME"));
        
        TearDownGameManager();
        
    }

    /**
     * Test of UpdatePlayerName method, of class GameManager.
     */
    @Test
    public void testUpdatePlayerName() {
        TearDownGameManager();
        MockHttpServletRequest mock = new MockHttpServletRequest();
        Request scaffold_player = RequestResponseFactory.create(mock);
        GameManager.getInstance().CheckoutLobby(scaffold_player.session());
        //EQ:0 - name not set
        GameManager.getInstance().UpdatePlayerName(scaffold_player.session());
        System.out.println(GameManager.getInstance().getLobby(0));
        assertEquals(false, GameManager.getInstance().getLobby(0).contains("{\"name\""));
        //EQ:1 - name is not valid
        scaffold_player.session().attribute("NAME", " ");
        GameManager.getInstance().UpdatePlayerName(scaffold_player.session());
        System.out.println(GameManager.getInstance().getLobby(0));
        assertEquals(false, GameManager.getInstance().getLobby(0).contains("\"name\":\" \""));
        //EQ:2- name is null
        scaffold_player.session().attribute("NAME", null);
        GameManager.getInstance().UpdatePlayerName(scaffold_player.session());
        System.out.println(GameManager.getInstance().getLobby(0));
        assertEquals(false, GameManager.getInstance().getLobby(0).contains("\"name\":\"\""));
        //EQ:3 - name is set
        scaffold_player.session().attribute("NAME", "Tester");
        GameManager.getInstance().UpdatePlayerName(scaffold_player.session());
        System.out.println(GameManager.getInstance().getLobby(0));
        assertEquals(true, GameManager.getInstance().getLobby(0).contains("\"name\":\"Tester\""));
        
        
    }

    /**
     * Test of UpdateLobbySettings method, of class GameManager.
     */
    @Test
    public void testUpdateLobbySettings() {
        TearDownGameManager();
        MockHttpServletRequest mock = new MockHttpServletRequest();
        Request admin = RequestResponseFactory.create(mock);
        String name = "cool name";
        int maxRounds = 72;
        int maxPlayers = 16;
        String password = "banana";
        
        GameManager.getInstance().CheckoutLobby(admin.session());
        
        String result = GameManager.getInstance().UpdateLobbySettings(admin.session(), name, maxRounds, maxPlayers, password);
        assertNotEquals(null, result);
        assertEquals(true, result.contains("cool name"));
        assertEquals(true, result.contains("72"));
        assertEquals(true, result.contains("16"));
        assertEquals(true, result.contains("banana"));
        
        TearDownGameManager();
    }

    /**
     * Test of StartGame method, of class GameManager.
     */
    @Test
    public void testStartGame() {
        TearDownGameManager();
        MockHttpServletRequest mock = new MockHttpServletRequest();
        Request scaffold_admin = RequestResponseFactory.create(mock);
        MockHttpServletRequest mock2 = new MockHttpServletRequest();
        GameManager.getInstance().CheckoutLobby(scaffold_admin.session());
        //EQ:0 - Game is not started
        assertEquals(true, GameManager.getInstance().StartGame(scaffold_admin.session()).contains("\"isInProgress\":true"));
        assertEquals(true, GameManager.getInstance().StartGame(scaffold_admin.session()).contains("\"roundNumber\":0"));
        
        //EQ:1 - Game has started
        assertEquals(true, GameManager.getInstance().StartGame(scaffold_admin.session()).contains("\"isInProgress\":true"));
        assertEquals(true, GameManager.getInstance().StartGame(scaffold_admin.session()).contains("\"roundNumber\":0"));
        
    }

    /**
     * Test of SubmitGuess method, of class GameManager.
     */
    @Test
    public void testSubmitGuess() {
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of SubmitDrawing method, of class GameManager.
     */
    @Test
    public void testSubmitDrawing() {
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    
    public void TearDownGameManager(){
        Method method;
        try {
            method = GameManager.class.getDeclaredMethod("NUKE");
            method.setAccessible(true);
            method.invoke(GameManager.getInstance());
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
