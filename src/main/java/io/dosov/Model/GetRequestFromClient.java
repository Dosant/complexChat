package io.dosov.Model;

import javax.ws.rs.GET;
import java.util.Map;

/**
 * Created by antondosov on 01.05.15.
 */
public class GetRequestFromClient {

    String username;
    int actionID;

    public GetRequestFromClient(Map<String, String[]> request){

        username = request.get("username")[0];
        actionID = Integer.parseInt(request.get("actionID")[0]);

    }

    public String toString() {
        String str = "***GETReguest" +
                "\nusername: " + this.username +
                "\nactionID: " + this.actionID;
        return str;
    }

    public int getActionID(){
        return actionID;
    }


}
