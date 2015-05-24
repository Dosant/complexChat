package io.dosov.db;

import io.dosov.Model.RequestFromClient;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


/**
 * Created by antondosov on 24.05.15.
 */
public class DBConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/chat";
    private static final String USERNAME = "java";
    private static final String PASSWORD = "java";
    private static Logger logger = Logger.getLogger(DBConnector.class.getName());
    private Connection connection;
    private Statement statement;

    public DBConnector() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            logger.info("DB is connected");
        } catch (Exception e) {
            logger.error("DB is not connected");
            e.printStackTrace();
        }
    }

    public List<RequestFromClient> getActions(int actionID) {
        ArrayList<RequestFromClient> res = new ArrayList<>();

        try {


            statement = connection.createStatement();

            String query = "SELECT * FROM Actions WHERE actionID > " + actionID;
            ResultSet rs = statement.executeQuery(query);


            while (rs.next()) {

                TreeMap<String, String[]> requestMap = new TreeMap<String, String[]>();
                requestMap.put("username", new String[]{rs.getString("username")});
                requestMap.put("requestType", new String[]{rs.getString("requestType")});
                requestMap.put("messageID", new String[]{(new Integer(rs.getInt("messageID")).toString())});
                requestMap.put("messageText", new String[]{rs.getString("messageText")});
                requestMap.put("dateString", new String[]{rs.getString("messageDate")});
                requestMap.put("userID", new String[]{(new Integer(rs.getInt("userID")).toString())});
                requestMap.put("actionID", new String[]{(new Integer(rs.getInt("actionID")).toString())});


                RequestFromClient request = new RequestFromClient(requestMap);
                res.add(request);
            }


        } catch (Exception e) {
            logger.error("DB get Actions Exception");
            e.printStackTrace();
        }

        return res;

    }


    public void addAction(RequestFromClient req) {
        try {


            statement = connection.createStatement();

            String query = "INSERT INTO Actions" +
                    "(actionID,userID,username,requestType,messageID,messageText,messageDate) VALUES" +
                    "(?,?,?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, req.getActionID());
            preparedStatement.setInt(2, req.getUserIDint());
            preparedStatement.setString(3, req.getUsername());
            preparedStatement.setString(4, req.getRequestType());
            preparedStatement.setInt(5, req.getMessageIDint());
            preparedStatement.setString(6, req.getMessageText());
            preparedStatement.setString(7, req.getDateString());

            preparedStatement.execute();

            logger.info("DB add req id " + req.getActionID());


        } catch (Exception e) {
            logger.error("DB add Action Exception");
            e.printStackTrace();
        }
    }

    public int getNewActionID() {

        try {

            statement = connection.createStatement();

            String query = "SELECT MAX(actionID) FROM Actions";
            ResultSet rs = statement.executeQuery(query);

            rs.next();
            int actionID = rs.getInt("MAX(actionID)");

            logger.info("DB get new actionId success");
            return actionID + 1;


        } catch (Exception e) {
            logger.error("DB get new actionId Exception");
            e.printStackTrace();
        }

        return -1;
    }

    public int getNewUserID() {

        try {

            statement = connection.createStatement();

            String query = "SELECT MAX(userID) FROM Actions";
            ResultSet rs = statement.executeQuery(query);

            rs.next();
            int userID = rs.getInt("MAX(userID)");

            logger.info("DB get new userID success");
            return userID + 1;


        } catch (Exception e) {
            logger.error("DB get new userID Exception");
            e.printStackTrace();
        }

        return -1;
    }

    public int getNewMessageID() {

        try {

            statement = connection.createStatement();

            String query = "SELECT MAX(messageID) FROM Actions";
            ResultSet rs = statement.executeQuery(query);

            rs.next();
            int messageID = rs.getInt("MAX(messageID)");

            logger.info("DB get new messageID success");
            return messageID + 1;


        } catch (Exception e) {
            logger.error("DB get new messageID Exception");
            e.printStackTrace();
        }

        return -1;
    }

    public int getActionID() {

        try {

            statement = connection.createStatement();

            String query = "SELECT MAX(actionID) FROM Actions";
            ResultSet rs = statement.executeQuery(query);

            rs.next();
            int actionID = rs.getInt("MAX(actionID)");

            logger.info("DB get new actionId success");
            return actionID;


        } catch (Exception e) {
            logger.error("DB get new actionId Exception");
            e.printStackTrace();
        }

        return -1;
    }


}






