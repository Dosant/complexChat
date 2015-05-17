package io.dosov.Controller;

import io.dosov.API.ServerApi;
import io.dosov.Model.GetRequestFromClient;
import io.dosov.Model.RequestFromClient;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by antondosov on 28.04.15.
 */
@WebServlet(name = "ServletPostRequests", asyncSupported = true)
public class ServletPostRequests extends HttpServlet {

    private static Logger logger = Logger.getLogger(ServletPostRequests.class.getName());
    private final Queue<AsyncContext> queue = new ConcurrentLinkedQueue<AsyncContext>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("doPost");
        //PrintWriter out = response.getWriter();
        Map<String,String[]> requestMap = request.getParameterMap();
        RequestFromClient postRequestFromClient = new RequestFromClient(requestMap);

        logger.info(postRequestFromClient);

        ServerApi.SharedInstance.addPostRequest(postRequestFromClient);

        respond();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("doGet");

        AsyncContext actx = request.startAsync();
        actx.setTimeout(40000);
        queue.offer(actx);

        registerAsyncContextForListenters(actx);

        respond();

    }


    private void registerAsyncContextForListenters(AsyncContext actx) {

        actx.addListener(new AsyncListener() {
            public void onTimeout(AsyncEvent arg0) throws IOException {


                logger.info("Timeout");

                AsyncContext ctx = arg0.getAsyncContext();

                HttpServletResponse res = (HttpServletResponse) ctx.getResponse();

                res.setStatus(304);
                res.setContentType("application/json");


                PrintWriter out = res.getWriter();
                out.print("{}");

                ctx.complete();


            }

            public void onStartAsync(AsyncEvent arg0) throws IOException {
            }

            public void onError(AsyncEvent arg0) throws IOException {
                queue.remove(arg0.getAsyncContext());
            }

            public void onComplete(AsyncEvent arg0) throws IOException {
                queue.remove(arg0.getAsyncContext());
            }

        });
    }


    private synchronized void respond() throws IOException {

        logger.info("Respond");

        for (AsyncContext ctx : queue) {

            process(ctx);

        }


    }

    private void process(AsyncContext ctx) throws IOException {
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        HttpServletResponse response = (HttpServletResponse) ctx.getResponse();


        response.setCharacterEncoding("UTF-8");
        Map<String, String[]> requestMap = request.getParameterMap();
        GetRequestFromClient getRequestFromClient = new GetRequestFromClient(requestMap);

        logger.info("Proccess");
        logger.info(getRequestFromClient);

        if (ServerApi.SharedInstance.isNewActions(getRequestFromClient.getActionID())) {

            JSONObject responseJSON = ServerApi.SharedInstance.getPosts(getRequestFromClient);
            PrintWriter out = response.getWriter();
            out.print(responseJSON.toJSONString());

            logger.info("responseJSON");
            ctx.complete();

        }

    }


}
