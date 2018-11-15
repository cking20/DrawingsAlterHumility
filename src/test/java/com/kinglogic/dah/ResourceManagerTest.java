///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.kinglogic.dah;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.junit.After;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
///**
// *
// * @author chris
// */
//public class ResourceManagerTest {
//    
//    public ResourceManagerTest() {
//    }
//
//    /**
//     * Test of getInstance method, of class ResourceManager.
//     */
//    @Test
//    public void testGetInstance() {
//        System.out.println("getInstance");
//        ResourceManager expResult = ResourceManager.getInstance();
//        ResourceManager result = ResourceManager.getInstance();
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of saveImage method, of class ResourceManager.
//     */
//    @Test
//    public void testSaveImageTC1() {
//        InputStream ValidStream = GetImageInputStream();
//        String expected = "/0/0/0";
//        String actual = ResourceManager.getInstance().saveImage(0, 0, 0, ValidStream);
//        assertEquals(expected, actual);
//    }
//    /**
//     * Test of saveImage method, of class ResourceManager.
//     */
//    @Test
//    public void testSaveImageTC2() {
//        InputStream stream = GetTextInputStream();
//        try {
//            stream.close();
//        } catch (IOException ex) {
//            Logger.getLogger(ResourceManagerTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        String expected = null;
//        String actual = null;
//        try{
//            actual = 
//                ResourceManager.getInstance().saveImage(0, 0, 0, stream);
//            fail("No exception thrown");
//        }catch(Exception e){
//            //meant to fail
//        }
//        
//    }
//    @Test
//    public void testSaveImageTC3() {
//        InputStream stream = GetTextInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = 
//            ResourceManager.getInstance().saveImage(0, 0, 0, stream);
//        }catch(Exception e){
//            //meant to fail
//        }
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC4() {
//        String expected = null;
//        String actual = null;
//        try{
//            actual = 
//                ResourceManager.getInstance().saveImage(0, 0, 0, null);
//            fail();
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    
//    @Test
//    public void testSaveImageTC5() {
//        String expected = null;
//        String actual = null;
//        try{
//            //imposible to test for this
//            //actual = ResourceManager.getInstance().saveImage(0, 0, 0, (InputStream)0);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC6() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().saveImage(0, 0, -1, stream);
//            
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC7() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().saveImage(0, 0, 3, stream);
//            
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC8() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().saveImage(0, 0, Integer.MAX_VALUE, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC9() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            //imposible to test for
//            //actual = ResourceManager.getInstance().saveImage(0, null, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC10() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            //imposible to test for
//            //actual = ResourceManager.getInstance().saveImage(0, (Integer)"test", 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC11() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().saveImage(0, -1, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC12() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().saveImage(0, 3, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC13() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().saveImage(0,Integer.MAX_VALUE, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC14() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            //imposible to test for
//            //actual = ResourceManager.getInstance().saveImage(0, null, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC15() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            //imposible to test for
//            //actual = ResourceManager.getInstance().saveImage(0, "test", 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC16() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().saveImage(-1,0, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC17() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().saveImage(3,0, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC18() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().saveImage(Integer.MAX_VALUE,0, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC19() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            //imposible to test for
//            //actual = ResourceManager.getInstance().saveImage(null,0, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    @Test
//    public void testSaveImageTC20() {
//        InputStream stream = GetImageInputStream();
//        String expected = null;
//        String actual = null;
//        try{
//            //imposible to test for
//            //actual = ResourceManager.getInstance().saveImage("test",0, 0, stream);
//        }catch(Exception e){/*meant to fail*/}
//        assertEquals(expected, actual);
//    }
//    
//    
//    
//    
//    
//    @Test
//    public void testPersistImageTC21(){
//        InputStream stream = GetImageInputStream();
//        assertNotEquals(null, ResourceManager.getInstance().persistImage(stream));
//    }
//    @Test
//    public void testPersistImageTC22(){
//        InputStream stream = GetImageInputStream();
//        try {
//            stream.close();
//        } catch (Exception e) {
//        }
//        try {
//            ResourceManager.getInstance().persistImage(stream);
//            fail();
//        } catch (Exception e) {
//        }        
//    }
//    @Test
//    public void testPersistImageTC23(){
//        InputStream stream = GetTextInputStream();
//        try {
//            ResourceManager.getInstance().persistImage(stream);
//            fail();
//        } catch (Exception e) {
//        }        
//    }
//    @Test
//    public void testPersistImageTC24(){
//        InputStream stream = null;
//        try {
//            ResourceManager.getInstance().persistImage(stream);
//            fail();
//        } catch (Exception e) {
//        }        
//    }
//    @Test
//    public void testPersistImageTC25(){
//        try {
//            //imposible to execute
//            //ResourceManager.getInstance().persistImage(0);   
//        } catch (Exception e) {
//        }        
//    }
//    
//    
//    
//    
//    @Test
//    public void testGetImageTC26(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(0, 0, 0);
//        assertNotEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC27(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(0, 0, -1);
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC28(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(0, 0, 3);
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC29(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(0, 0, Integer.MAX_VALUE);
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC30(){
//        byte[] actual = null;
//        try{
//        actual = ResourceManager.getInstance().getImage(0, 0, (Integer)null);
//            fail();
//        }catch(Exception e){
//        }
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC31(){
//        byte[] actual = null;
//        //imposible to test
//        //actual = ResourceManager.getInstance().getImage(0, 0, "test");
//        assertEquals(null, actual);
//    }
//    
//    @Test
//    public void testGetImageTC32(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(0, -1, 0);
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC33(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(0, 3, 0);
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC34(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(0, Integer.MAX_VALUE, 0);
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC35(){
//        byte[] actual = null;
//        try{
//        actual = ResourceManager.getInstance().getImage(0, (Integer)null, 0);
//            fail();
//        }catch(Exception e){
//        }
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC36(){
//        byte[] actual = null;
//        //imposible to test
//        //actual = ResourceManager.getInstance().getImage(0, "test", 0);
//        assertEquals(null, actual);
//    }
//    
//    @Test
//    public void testGetImageTC37(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(-1,0,0);
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC38(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(0,3, 0);
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC39(){
//        byte[] actual = ResourceManager.getInstance()
//                .getImage(Integer.MAX_VALUE,0, 0);
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC40(){
//        byte[] actual = null;
//        try{
//        actual = ResourceManager.getInstance().getImage((Integer)null,0, 0);
//            fail();
//        }catch(Exception e){
//        }
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetImageTC41(){
//        byte[] actual = null;
//        //imposible to test
//        //actual = ResourceManager.getInstance().getImage("test",0, 0);
//        assertEquals(null, actual);
//    }
//    
//    @Test
//    public void testGetPromptsTC42(){
//        String actual = null;
//        actual = ResourceManager.getInstance().getPrompts(10);
//        assertNotEquals(null, actual);
//    }
//    @Test
//    public void testGetPromptsTC43(){
//        String actual = null;
//        actual = ResourceManager.getInstance().getPrompts(0);
//        assertNotEquals(null, actual);
//    }
//    @Test
//    public void testGetPromptsTC44(){
//        String actual = null;
//        actual = ResourceManager.getInstance().getPrompts(-1);
//        assertNotEquals(null, actual);
//    }
//    @Test
//    public void testGetPromptsTC45(){
//        String actual = null;
//        try{
//            //imposible to run
//            //actual = ResourceManager.getInstance().getPrompts("test"); 
//        }catch(Exception e){   
//        }
//        assertEquals(null, actual);
//    }
//    @Test
//    public void testGetPromptsTC46(){
//        String actual = null;
//        try{
//            actual = ResourceManager.getInstance().getPrompts((Integer)null);
//            fail();
//        }catch(Exception e){   
//        }
//        assertEquals(null, actual);
//    }
//    
//    
//    
//    
//    
//    
//
//    public InputStream GetImageInputStream(){
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        return classLoader.getResourceAsStream("test.png");
//    }
//    public InputStream GetTextInputStream(){
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        return classLoader.getResourceAsStream("prompts.txt");
//    }
//    
//    
//    @After
//    public void TearDownResourceManager(){
//        Method method;
//        try {
//            method = ResourceManager.class.getDeclaredMethod("NUKE");
//            method.setAccessible(true);
//            method.invoke(ResourceManager.getInstance());
//        } catch (NoSuchMethodException ex) {
//            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SecurityException ex) {
//            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvocationTargetException ex) {
//            Logger.getLogger(GameManagerTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
