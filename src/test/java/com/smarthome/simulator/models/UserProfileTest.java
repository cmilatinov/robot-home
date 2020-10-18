package com.smarthome.simulator.models;

import org.junit.Assert;
import org.junit.*;

/**
 * 2. Manage user profiles
 */
public class UserProfileTest {

    /**
     * Testing if adding a user profile returns an exception when the name is null
     */
    @Test(expected=Exception.class)
    public void addUserProfileNameNullShouldReturnException() throws Exception
    {
        new UserProfile(null);
    }

    /**
     * Testing if adding a user profile returns an exception when the name is ""
     */
    @Test(expected=Exception.class)
    public void addUserProfileNameEmptyStringShouldReturnException() throws Exception
    {
        new UserProfile("");
    }

    /**
     * Testing if adding a user profile returns an exception when the name is " "
     */
    @Test(expected=Exception.class)
    public void addUserProfileNameSpaceShouldReturnException() throws Exception
    {
        new UserProfile(" ");
    }

    /**
     * Testing if adding a user profile returns an exception when the name is greater than 16 character
     */
    @Test(expected=Exception.class)
    public void addUserProfileNameGreaterThan16ShouldReturnException() throws Exception
    {
        new UserProfile("abcdefghijklmnopq");
    }

    /**
     * Testing if adding a user profile returns the name when the name is a character
     */
    @Test
    public void addUserProfileNameWithACharacterShouldCreateObjectWithName()
    {
        UserProfile tester = null;
        try{
            tester = new UserProfile("a");
        }catch(Exception e){}

        Assert.assertEquals("a", tester.getName());
    }

    /**
     * Testing if adding a user profile returns the name when the name is a number
     */
    @Test
    public void addUserProfileNameNumberOnlyShouldShouldCreateObjectWithName()
    {
        UserProfile tester = null;
        try{
            tester = new UserProfile("1234");
        }catch(Exception e){}

        Assert.assertEquals("1234", tester.getName());
    }

    /**
     * Testing if adding a user profile returns the name when the name is alphanumerical
     */
    @Test
    public void addUserProfileNameAlphaNumericalShouldShouldCreateObjectWithName()
    {
        UserProfile tester = null;
        try{
            tester = new UserProfile("abc1234");
        }catch(Exception e){}

        Assert.assertEquals("abc1234", tester.getName());
    }

    /**
     * Testing if editing a user profile returns an exception when the name is null
     */
    @Test(expected=Exception.class)
    public void editUserProfileNameNullShouldReturnException() throws Exception
    {
        UserProfile tester = new UserProfile("test");

        tester.setName(null);
    }

    /**
     * Testing if editing a user profile returns an exception when the name is ""
     */
    @Test(expected=Exception.class)
    public void editUserProfileNameEmptyStringShouldReturnException() throws Exception
    {
        UserProfile tester = new UserProfile("test");

        tester.setName("");
    }

    /**
     * Testing if editing a user profile returns an exception when the name is " "
     */
    @Test(expected=Exception.class)
    public void editUserProfileNameSpaceShouldReturnException() throws Exception
    {
        UserProfile tester = new UserProfile("test");

        tester.setName(" ");
    }

    /**
     * Testing if editing a user profile returns an exception when the name is greater than 16 characters
     */
    @Test(expected=Exception.class)
    public void editUserProfileNameGreaterThan16ShouldReturnException() throws Exception
    {
        UserProfile tester = new UserProfile("test");

        tester.setName("abcdefghijklmnopq");
    }

    /**
     * Testing if editing a user profile returns the name when the name is a character
     */
    @Test
    public void editUserProfileNameWithACharacterShouldEditName()
    {
        UserProfile tester = null;
        try{
            tester = new UserProfile("test");
            tester.setName("a");
        }catch(Exception e){}

        Assert.assertEquals("a", tester.getName());
    }

    /**
     * Testing if editing a user profile returns the name when the name is a number
     */
    @Test
    public void editUserProfileNameNumberOnlyShouldShouldEditName()
    {
        UserProfile tester = null;
        try{
            tester = new UserProfile("test");
            tester.setName("1234");
        }catch(Exception e){}

        Assert.assertEquals("1234", tester.getName());
    }

    /**
     * Testing if editing a user profile returns the when the name is alphanumerical
     */
    @Test
    public void editUserProfileNameAlphaNumericalShouldShouldEditName()
    {
        UserProfile tester = null;
        try{
            tester = new UserProfile("test");
            tester.setName("abc1234");
        }catch(Exception e){}

        Assert.assertEquals("abc1234", tester.getName());
    }

}
