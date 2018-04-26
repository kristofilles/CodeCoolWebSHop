package com.codecool.shop.dao.implementation.JDBC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class ConnectionHandler implements AutoCloseable {

    /**
     * Fields for setting up and handling a connection with a database.
     */
    private static final Logger logger = LoggerFactory.getLogger( ConnectionHandler.class );
    private Properties prop = new Properties();
    private InputStream input = null;
    private Connection connection = null;

    /**
     * <p>This function is read the properties from the connection.properties
     * and settle a connection to the database.</p>
     *
     * @return a connection to the database
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        String Database = null;
        String User = null;
        String Password = null;
        try {

            input = new FileInputStream( "src/main/resources/public/connection.properties" );

            // load a properties file
            prop.load( input );
            Database = "jdbc:postgresql://" + prop.getProperty( "url" ) + "/" + prop.getProperty( "database" );
            User = prop.getProperty( "user" );
            Password = prop.getProperty( "password" );
            logger.debug( "Properties read from the file: {}, {}, {}", Database, User, Password );
        } catch (IOException ex) {
            logger.warn( "File not found. StackTrace: ", ex.getMessage() );
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        logger.debug( "Driver manager connection established." );
        return DriverManager.getConnection( Database, User, Password );
    }

    /**
     * <p>It takes a query and a list of parameters(if it needed)
     * , establish a connection to the database and execute the sql
     * statement. Its also parsing the parameters for the desired
     * data type. </p>
     *
     * @param query      its a statement
     * @param parameters list of parameters
     * @return the result of the statement
     */
    ResultSet executeQuery(String query, List<String> parameters) {
        logger.debug( "The query is: {}, and the parameters is: {}.", query, parameters );
        try {
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement( query );
            if (parameters != null) {
                for (int i = 0; i < parameters.size(); i++) {
                    try {
                        statement.setInt( i + 1, Integer.parseInt( parameters.get( i ) ) );
                    } catch (NumberFormatException a) {
                        try {
                            statement.setFloat( i + 1, Float.parseFloat( parameters.get( i ) ) );
                        } catch (NumberFormatException b) {
                            statement.setString( i + 1, parameters.get( i ) );
                        }
                    }
                }
            }
            logger.debug( "Statement execute successfull." );
            return statement.executeQuery();
        } catch (SQLException e) {
            logger.warn( "Connection to the database failed. StackTrace: {}", e.getSQLState() );
        }
        return null;
    }

    /**
     * Its just the implementation of autoclosable.
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        connection.close();
    }
}
