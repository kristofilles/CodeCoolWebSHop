package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Here you can find JDBC related queries for the user table.
 * <p>
 * <p>Queries what we are implemented for  the user table are: add,
 * find by id or name, remove by id and get all of the users.</p>
 */
public class UserDaoJDBC implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger( UserDao.class );
    private static UserDaoJDBC instance = null;
    /**
     * This is a ConnectionHandler type {@link ConnectionHandler} for establish connection.
     */
    private ConnectionHandler ch;

    private UserDaoJDBC() {

    }

    public static UserDaoJDBC getInstance() {
        if (instance == null) {
            instance = new UserDaoJDBC();
        }
        return instance;
    }

    /**
     * Its supposed to add a user to the users table.
     *
     * @param user
     */
    @Override
    public void add(User user) {
        logger.debug( "Inserting {} into users.", user.getUsername() );
        String query = "INSERT INTO users (id, username, password) VALUES (?,?,?)";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( user.getId() ), user.getUsername(), user.getPassword() ) );
    }

    /**
     * Its supposed to select a user from users table with the given id.
     *
     * @param id of user
     * @return a user with given id
     */
    @Override
    public User find(int id) {
        logger.debug( "Selecting the user with id: {}", id );
        String query = "SELECT * FROM users WHERE id = ?";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );) {
            if (resultSet.next()) {
                logger.debug( "Successfully selected." );
                return new User( id, resultSet.getString( "username" ), resultSet.getString( "password" ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. STackTrace: {}", e.getMessage() );
        }
        return null;
    }

    /**
     * Its supposed to return a user with the given username.
     *
     * @param username
     * @return a user with the given username
     */
    @Override
    public User find(String username) {
        logger.debug( "Selecting user with username: {}", username );
        String query = "SELECT * FROM users WHERE username = ?";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( username ) );) {
            if (resultSet.next()) {
                logger.debug( "Successfully selected." );
                return new User( resultSet.getInt( "id" ), username, resultSet.getString( "password" ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return null;
    }

    /**
     * Its supposed to remove a user with the given id.
     *
     * @param id of the user
     */
    @Override
    public void remove(int id) {
        logger.debug( "Removing the user with the id: {}", id );
        String query = "DELETE FROM users WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );
    }

    /**
     * Its supposed to select all of the users from the users table.
     *
     * @return all of the users
     */
    @Override
    public List<User> getAll() {
        logger.debug( "Selecting users from users table." );
        String query = "SELECT * FROM users";
        List<User> userResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, null );) {
            while (resultSet.next()) {
                userResult.add( new User( resultSet.getInt( "id" ), resultSet.getString( "username" ), resultSet.getString( "password" ) ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        logger.debug( "Successfully selected the users." );
        return userResult;
    }
}
