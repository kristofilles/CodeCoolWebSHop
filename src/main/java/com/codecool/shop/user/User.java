package com.codecool.shop.user;

import com.codecool.shop.Globals;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This class contains user related informations
 */
public class User {
    /**
     * The unique id of the user
     */
    private final int id;
    /**
     * The unique username of the user
     */
    private String username;
    /**
     * The hashed version of the password
     */
    private String password;

    /**
     * The constructor for this class for first building objects
     *
     * <p>It differs from the other constructor in generating its own unique id and hashing the password</p>
     * @param username
     * @param nonHashedPassword
     */
    public User(String username, String nonHashedPassword){
        id = Globals.userDao.getAll().size();
        this.username = username;
        password = BCrypt.hashpw(nonHashedPassword, BCrypt.gensalt());
    }

    /**
     * The second constructor for rebuilding the object based on database data
     * @param id
     * @param username
     * @param password
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the id of the user
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the username of the user
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the hashed password of the user
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Compares the typed in password with the hashed one stored on the object
     * @param candidatePassword
     * @return
     */
    public boolean checkPassword(String candidatePassword){
        return BCrypt.checkpw(candidatePassword, password);
    }
}
