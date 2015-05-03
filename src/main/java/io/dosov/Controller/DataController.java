package io.dosov.Controller;

import io.dosov.Model.GetRequestFromClient;
import io.dosov.Model.PostRequestFromClient;
import io.dosov.Utility.XmlUtility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.util.LinkedList;

/**
 * Created by antondosov on 01.05.15.
 */
public class DataController {

    public static final DataController SharedInstance = new DataController();
    int messageID = 0;
    int userID = 0;


    LinkedList <PostRequestFromClient> data = new LinkedList<PostRequestFromClient>();


    XmlUtility xmlUtility = new XmlUtility();

    DataController(){

        messageID = xmlUtility.loadHistory(data)[0];
        userID = xmlUtility.loadHistory(data)[1];
        System.out.println("messageID" + messageID);
        System.out.println("userID" + userID);
    }


    public synchronized  void addPostRequest(PostRequestFromClient pr){

        data.addLast(pr);
        xmlUtility.saveHistory(data, (new Integer(messageID)).toString(), (new Integer(userID)).toString());

    }

    public synchronized JSONObject getPosts(GetRequestFromClient getRequestFromClient){
        int actionID = getRequestFromClient.getActionID();


        JSONObject response = new JSONObject();
        JSONArray  posts = new JSONArray();





        if(actionID == data.size()){

            response.put("posts",posts);
            return response;


        } else if (actionID < data.size()){
            // form get response

            int postsSize = data.size() - actionID;

            for(int i = actionID; i < data.size(); i++){

                PostRequestFromClient post = data.get(i);
                JSONObject postJSON = post.getPostJSON();
                postJSON.put("actionID",(i+1));

                posts.add(postJSON);

            }

            response.put("posts",posts);
            return response;





        } else {

            System.err.println("ActionID from the client > data contained on server");
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



}
