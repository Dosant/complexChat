package io.dosov.Controller;

import io.dosov.API.ServerApi;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by antondosov on 02.05.15.
 */
@WebServlet(name = "ServletGetUserID")
public class ServletGetUserID extends HttpServlet {

    private static Logger logger = Logger.getLogger(ServletGetUserID.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        int userID = ServerApi.SharedInstance.getUserID();
        logger.info("give new user ID " + userID);
        out.print(userID);

    }
}
