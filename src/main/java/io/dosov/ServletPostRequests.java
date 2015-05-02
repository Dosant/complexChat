package io.dosov;

import io.dosov.Controller.DataController;
import io.dosov.Model.GetRequestFromClient;
import io.dosov.Model.PostRequestFromClient;
import org.json.simple.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by antondosov on 28.04.15.
 */
@WebServlet(name = "ServletPostRequests")
public class ServletPostRequests extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //PrintWriter out = response.getWriter();
        Map<String,String[]> requestMap = request.getParameterMap();
        PostRequestFromClient postRequestFromClient = new PostRequestFromClient(requestMap);
        System.out.println(postRequestFromClient);

        DataController.SharedInstance.addPostRequest(postRequestFromClient);



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");


        Map<String,String[]> requestMap = request.getParameterMap();
        GetRequestFromClient getRequestFromClient = new GetRequestFromClient(requestMap);
        System.out.println(getRequestFromClient);

        JSONObject responseJSON = DataController.SharedInstance.getPosts(getRequestFromClient);


        PrintWriter out = response.getWriter();
        out.print(responseJSON.toJSONString());




    }
}
