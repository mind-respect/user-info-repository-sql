package org.triple_brain.module.repository_sql;

import java.sql.*;

/**
 * Copyright Mozilla Public License 1.1
 */
public class SQLConnection {
    static Connection connection;

    public static PreparedStatement preparedStatement(String query){
        try{
            return connection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        }catch (SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }

    public static void closeConnection() throws SQLException{
        connection().close();
    }

    public static void clearDatabases()throws SQLException{
        String query = "DROP TABLE IF EXISTS por_user;";
        preparedStatement(query).executeUpdate();
    }

    public static void createTables() throws SQLException{
        String query = "CREATE TABLE por_user (\n" +
                "    id           BIGINT    PRIMARY KEY AUTO_INCREMENT,\n" +
                "    creationTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "    updateTime   TIMESTAMP NOT NULL,\n" +
                "\n" +
                "    uuid   VARCHAR(36)   UNIQUE NOT NULL,\n" +
                "    username  VARCHAR(50)   UNIQUE NOT NULL,\n" +
                "    email  VARCHAR(50)   UNIQUE NOT NULL,\n" +
                "\n" +
                "    salt                 VARCHAR(36),\n" +
                "    passwordHash         VARCHAR(100)\n" +
                ");";
        preparedStatement(query).executeUpdate();
    }


    private static Connection connection(){
        try{
            if(connection == null || connection.isClosed()){
                connection = createConnection();
            }
            return connection;
        }catch(SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }
    
    private static Connection createConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1/triple_brain",
                    "triple_brain", "boraptop34");
            return connection;
        }catch(ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex){
            throw new SQLConnectionException(ex);
        }
    }
  
}
