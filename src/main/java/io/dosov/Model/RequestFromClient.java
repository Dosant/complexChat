package io.dosov.Model;


import io.dosov.API.ServerApi;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by antondosov on 01.05.15.
 */
public class RequestFromClient {

    String username;
    RequestType requestType;
    int messageID;
    int userID;
    int actionID;
    String messageText;
    String dateString;

    public RequestFromClient(Map<String, String[]> request) {

        username = request.get("username")[0];
        requestType = RequestType.valueOf(request.get("requestType")[0]);
        messageID = Integer.parseInt(request.get("messageID")[0]);
        messageText = request.get("messageText")[0];
        userID = Integer.parseInt(request.get("userID")[0]);


        if(requestType == RequestType.add) {
            if (messageID == -1) {
                messageID = ServerApi.SharedInstance.getMessageID();
            }
        }

        if (request.get("actionID") == null) {
            actionID = ServerApi.SharedInstance.getActionID();
        } else {
            actionID = Integer.parseInt(request.get("actionID")[0]);
        }

        dateString = request.getOrDefault("dateString", new String[]{nowDateString()})[0];
    }

    String nowDateString(){
        Date date = new Date();
        SimpleDateFormat dateFormatter =
                new SimpleDateFormat("HH:mm");
        return dateFormatter.format(date);

    }

    public String toString() {

        String str = "***POSTReguest" +
                "\nactionID: " + this.actionID +
                     "\nusername: " + this.username +
                     "\nuserID: " + this.userID +
                     "\nrequestType: " + this.requestType.name() +
                     "\nmessageID: " + this.messageID +
                     "\nmessageText: " + this.messageText +
                     "\ndate: " + this.dateString;
        return str;
    }

    public JSONObject getPostJSON(){
        JSONObject obj = new JSONObject();
        obj.put("username",username);
        obj.put("actionID", (new Integer(actionID)).toString());
        obj.put("requestType", requestType.name());
        obj.put("messageID",(new Integer(messageID)).toString());
        obj.put("userID",(new Integer(userID)).toString());
        obj.put("messageText",messageText);
        obj.put("time",dateString);

        return  obj;
    }

    public String getUsername(){
        return username;
    }

    public String getUserID(){
        return (new Integer(userID)).toString();
    }

    public String getMessageText(){
        return messageText;
    }

    public String getDateString(){
        return dateString;
    }

    public String getRequestType(){
        return requestType.name();
    }

    public RequestType getRequestTypeEnum() {
        return requestType;
    }

    public String getMessageID(){
        return (new Integer(messageID)).toString();
    }

    public int getMessageIDint() {
        return messageID;
    }

    public int getUserIDint() {
        return userID;
    }

    public int getActionID() {
        return actionID;
    }

    public enum RequestType {
        add(0), edit(1), delete(2), username(3);

        private int value;

        RequestType(int value) {
            this.value = value;
        }

    }

}
