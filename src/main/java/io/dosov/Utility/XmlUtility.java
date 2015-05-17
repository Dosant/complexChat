package io.dosov.Utility;

import io.dosov.Model.RequestFromClient;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by antondosov on 01.05.15.
 */
public class XmlUtility {

    public synchronized void saveHistory(LinkedList<RequestFromClient> posts, String lastMessageID, String lastUserID, String lastActionID) {

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("history.xml");
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder =
                    dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            // root element
            Element rootElement = doc.createElement("history");
            doc.appendChild(rootElement);


            Element lastMessageIDElement = doc.createElement("lastMessageID");
            lastMessageIDElement.appendChild(doc.createTextNode(lastMessageID));
            rootElement.appendChild(lastMessageIDElement);

            Element lastUserIDElement = doc.createElement("lastUserID");
            lastUserIDElement.appendChild(doc.createTextNode(lastUserID));
            rootElement.appendChild(lastUserIDElement);

            Element lastActionIDElement = doc.createElement("lastActionID");
            lastActionIDElement.appendChild(doc.createTextNode(lastActionID));
            rootElement.appendChild(lastActionIDElement);


            Element postsElement = doc.createElement("posts");
            rootElement.appendChild(postsElement);

            ListIterator<RequestFromClient> it = posts.listIterator();

            for (RequestFromClient request : posts) {

                Element post = doc.createElement("post");


                Element username = doc.createElement("username");
                username.appendChild(doc.createTextNode(request.getUsername()));
                post.appendChild(username);

                Element requestType = doc.createElement("requestType");
                requestType.appendChild(doc.createTextNode(request.getRequestType()));
                post.appendChild(requestType);

                Element dateString = doc.createElement("dateString");
                dateString.appendChild(doc.createTextNode(request.getDateString()));
                post.appendChild(dateString);

                Element messageID = doc.createElement("messageID");
                messageID.appendChild(doc.createTextNode(request.getMessageID()));
                post.appendChild(messageID);

                Element messageText = doc.createElement("messageText");
                messageText.appendChild(doc.createTextNode(request.getMessageText()));
                post.appendChild(messageText);

                Element userID = doc.createElement("userID");
                userID.appendChild(doc.createTextNode(request.getUserID()));
                post.appendChild(userID);

                Element actionID = doc.createElement("actionID");
                actionID.appendChild(doc.createTextNode((new Integer(request.getActionID())).toString()));
                post.appendChild(actionID);

                postsElement.appendChild(post);
            }
            // write the content into xml file
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);


            StreamResult result =
                    new StreamResult(writer);



            transformer.transform(source, result);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }



    }


    public synchronized int[] loadHistory(LinkedList<RequestFromClient> posts) {

        try {




            File inputFile = new File("history.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            org.jdom2.Document document = saxBuilder.build(inputFile);

            System.out.println("Root element :"
                    + document.getRootElement().getName());

            org.jdom2.Element rootElement = document.getRootElement();

            String lastMessageIDstring = rootElement.getChild("lastMessageID").getText();
            int lastMessageID = Integer.parseInt(lastMessageIDstring);

            String lastUserIDstring = rootElement.getChild("lastUserID").getText();
            int lastUserID = Integer.parseInt(lastUserIDstring);

            String lastActionIDstring = rootElement.getChild("lastActionID").getText();
            int lastActionID = Integer.parseInt(lastActionIDstring);


            org.jdom2.Element postsElement = rootElement.getChild("posts");
            List<org.jdom2.Element> postsList = postsElement.getChildren();

            Iterator<org.jdom2.Element> it = postsList.iterator();
            while(it.hasNext()){
                org.jdom2.Element postElement = it.next();
                TreeMap<String, String[]> requestMap = new TreeMap<String, String[]>();
                requestMap.put("username",new String[]{postElement.getChildText("username")});
                requestMap.put("requestType",new String[]{postElement.getChildText("requestType")});
                requestMap.put("messageID",new String[]{postElement.getChildText("messageID")});
                requestMap.put("messageText",new String[]{postElement.getChildText("messageText")});
                requestMap.put("dateString",new String[]{postElement.getChildText("dateString")});
                requestMap.put("userID",new String[]{postElement.getChildText("userID")});
                requestMap.put("actionID", new String[]{postElement.getChildText("actionID")});


                RequestFromClient request = new RequestFromClient(requestMap);

                posts.addLast(request);



            }


            return new int[]{lastMessageID, lastUserID, lastActionID};

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }


        return new int[]{0, 0, 0};


    }


}
