package io.dosov.Utility;

import io.dosov.Model.PostRequestFromClient;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import java.io.*;

import org.jdom2.*;

/**
 * Created by antondosov on 01.05.15.
 */
public class XmlUtility {

    public synchronized  void saveHistory(LinkedList<PostRequestFromClient> posts, String lastMessageID){


        try {

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


            Element postsElement = doc.createElement("posts");
            rootElement.appendChild(postsElement);




            ListIterator<PostRequestFromClient> it = posts.listIterator();

            while(it.hasNext()){
                PostRequestFromClient request = it.next();

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


                postsElement.appendChild(post);


            }



            // write the content into xml file
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            PrintWriter writer = new PrintWriter("history.xml");

            StreamResult result =
                    new StreamResult(writer);



            transformer.transform(source, result);
            // Output to console for testing
            StreamResult consoleResult =
                    new StreamResult(System.out);
            transformer.transform(source, consoleResult);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public synchronized int loadHistory(LinkedList<PostRequestFromClient> posts) {
        try {




            File inputFile = new File("history.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            org.jdom2.Document document = saxBuilder.build(inputFile);

            System.out.println("Root element :"
                    + document.getRootElement().getName());

            org.jdom2.Element rootElement = document.getRootElement();

            String lastMessageIDstring = rootElement.getChild("lastMessageID").getText();
            int lastMessageID = Integer.parseInt(lastMessageIDstring);


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


                PostRequestFromClient request = new PostRequestFromClient(requestMap);

                posts.addLast(request);



            }


            return lastMessageID;

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }


}
