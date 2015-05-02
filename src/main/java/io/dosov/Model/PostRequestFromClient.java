package io.dosov.Model;


import io.dosov.Controller.DataController;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by antondosov on 01.05.15.
 */
public class PostRequestFromClient {

    public enum RequestType{
        add(0),edit(1),delete(2),username(3);

        private int value;

        private RequestType(int value) {
            this.value = value;
        }

    }


    String username;
    RequestType requestType;
    int messageID;
    String messageText;

    String dateString;

    public PostRequestFromClient(Map<String, String[]> request){

        username = request.get("username")[0];
        requestType = RequestType.valueOf(request.get("requestType")[0]);
        messageID = Integer.parseInt(request.get("messageID")[0]);
        messageText = request.get("messageText")[0];


        if(requestType == RequestType.add) {
            if (messageID == -1) {
                messageID = DataController.SharedInstance.getMessageID();
            }
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
                     "\nusername: " + this.username +
                     "\nrequestType: " + this.requestType.name() +
                     "\nmessageID: " + this.messageID +
                     "\nmessageText: " + this.messageText +
                     "\ndate: " + this.dateString;
        return str;
    }

    public JSONObject getPostJSON(){
        JSONObject obj = new JSONObject();
        obj.put("username",username);
        obj.put("requestType", requestType.name());
        obj.put("messageID",(new Integer(messageID)).toString());
        obj.put("messageText",messageText);
        obj.put("time",dateString);

        return  obj;
    }




    public String getUsername(){
        return username;
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
    public String getMessageID(){
        return (new Integer(messageID)).toString();
    }

}
