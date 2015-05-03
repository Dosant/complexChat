package io.dosov.Model;

import javax.ws.rs.GET;
import java.util.Map;

/**
 * Created by antondosov on 01.05.15.
 */
public class GetRequestFromClient {

    String username;
    int userID;
    int actionID;


    public GetRequestFromClient(Map<String, String[]> request){

        username = request.get("username")[0];
        userID = Integer.parseInt(request.get("userID")[0]);
        actionID = Integer.parseInt(request.get("actionID")[0]);

    }

    public String toString() {
        String str = "***GETReguest" +
                "\nusername: " + this.username +
                "\nuserID: " + this.userID +
                "\nactionID: " + this.actionID;
        return str;
    }

    public int getActionID(){
        return actionID;
    }


}
