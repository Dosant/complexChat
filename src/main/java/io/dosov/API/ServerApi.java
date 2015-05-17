package io.dosov.API;

import io.dosov.Model.GetRequestFromClient;
import io.dosov.Model.RequestFromClient;
import io.dosov.Utility.XmlUtility;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;

/**
 * Created by antondosov on 01.05.15.
 */
public class ServerApi {

    public static final ServerApi SharedInstance = new ServerApi();
    private static Logger logger = Logger.getLogger(ServerApi.class.getName());
    private int messageID = 0;
    private int userID = 0;
    private int actionID = 0;


    private LinkedList<RequestFromClient> data = new LinkedList<RequestFromClient>();


    private XmlUtility xmlUtility = new XmlUtility();

    private ServerApi() {

        logger.info("Api init");

        int[] history = xmlUtility.loadHistory(data);

        messageID = history[0];
        userID = history[1];
        actionID = history[2];
    }


    public synchronized void addPostRequest(RequestFromClient pr) {

        logger.info("addPostRequest: id = " + pr.getActionID());

        if (pr.getRequestTypeEnum() == RequestFromClient.RequestType.add) {
            addMessage(pr);
        } else if (pr.getRequestTypeEnum() == RequestFromClient.RequestType.edit) {

            editMessage(pr);

        } else if (pr.getRequestTypeEnum() == RequestFromClient.RequestType.delete) {

            deleteMessage(pr);

        } else if (pr.getRequestTypeEnum() == RequestFromClient.RequestType.username) {

            changeUsername(pr);

        }

        validateUserId(pr.getUserIDint());
        saveHistory();

    }


    private void addMessage(RequestFromClient pr) {
        data.addLast(pr);

    }

    private void editMessage(RequestFromClient pr) {
        for (RequestFromClient req : data) {
            if (pr.getMessageIDint() == req.getMessageIDint() && req.getRequestTypeEnum() == RequestFromClient.RequestType.edit) {
                data.remove(req);
            }
        }
        data.addLast(pr);

    }

    private void changeUsername(RequestFromClient pr) {
        for (RequestFromClient req : data) {
            if (req.getRequestTypeEnum() == RequestFromClient.RequestType.username) {
                data.remove(req);
            }
        }
        data.addLast(pr);

    }

    private void deleteMessage(RequestFromClient pr) {

        for (RequestFromClient req : data) {
            if (pr.getMessageIDint() == req.getMessageIDint()) {
                data.remove(req);
            }
        }
        data.addLast(pr);


    }


    public synchronized JSONObject getPosts(GetRequestFromClient getRequestFromClient) {

        logger.info("getPostsFrom id = " + getRequestFromClient.getActionID() + " to id = " + data.peekLast().getActionID());

        int actionID = getRequestFromClient.getActionID();

        JSONObject response = new JSONObject();
        JSONArray posts = new JSONArray();

        if (data.isEmpty()) {
            response.put("posts", posts);
            return response;
        }

        if (actionID == data.peekLast().getActionID()) {
            response.put("posts", posts);
            return response;
        } else if (actionID < data.peekLast().getActionID()) {
            // form get response

            for (RequestFromClient req : data) {

                if (req.getActionID() > actionID) {
                    JSONObject postJSON = req.getPostJSON();
                    posts.add(postJSON);
                }
            }

            response.put("posts",posts);
            return response;





        } else {

            return null;

        }

    }


    public synchronized int getMessageID(){
        messageID++;
        return messageID;
    }

    public synchronized int getUserID(){
        userID++;
        return userID;
    }

    public synchronized int getActionID() {
        actionID++;
        return actionID;
    }

    public synchronized boolean isNewActions(int actionID) {

        return actionID < data.peekLast().getActionID();

    }

    private void saveHistory() {
        xmlUtility.saveHistory(data, (new Integer(messageID)).toString(), (new Integer(userID)).toString(), (new Integer(actionID)).toString());
    }


    private void validateUserId(int userID) {
        if (this.userID <= userID) {
            this.userID = userID++;
        }
    }



}
