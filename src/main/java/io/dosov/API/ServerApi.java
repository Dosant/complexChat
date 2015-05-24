package io.dosov.API;

import io.dosov.Model.GetRequestFromClient;
import io.dosov.Model.RequestFromClient;
import io.dosov.db.DBConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by antondosov on 01.05.15.
 */
public class ServerApi {

    public static final ServerApi SharedInstance = new ServerApi();
    private DBConnector dbConnector = new DBConnector();




    public synchronized void addPostRequest(RequestFromClient pr) {
        dbConnector.addAction(pr);
    }

    public synchronized JSONObject getPosts(GetRequestFromClient getRequestFromClient) {




        JSONObject response = new JSONObject();
        JSONArray posts = new JSONArray();

        ArrayList<RequestFromClient> data = new ArrayList<>(dbConnector.getActions(getRequestFromClient.getActionID()));

            for (RequestFromClient req : data) {

                    JSONObject postJSON = req.getPostJSON();
                    posts.add(postJSON);

            }

        response.put("posts", posts);
            return response;

    }




    public synchronized int getMessageID(){
        return dbConnector.getNewMessageID();
    }

    public synchronized int getUserID(){
        return dbConnector.getNewUserID();
    }

    public synchronized int getActionID() {
        return dbConnector.getNewActionID();
    }

    public synchronized boolean isNewActions(int actionID) {

        return actionID < dbConnector.getActionID();

    }


}
