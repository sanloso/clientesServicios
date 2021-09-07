package co.edu.escuelaing.networking.httpserver;

import co.edu.escuelaing.springplus.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HttpServer {
    private static final HttpServer _instance = new HttpServer();

    public static HttpServer get_instance(){
        return _instance;
    }

    private HttpServer(){}

    public void start (String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
//        searchForComponents();
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            serveConnection(clientSocket);
        }
        serverSocket.close();
    }

    public void serveConnection (Socket clientSocket) throws IOException, URISyntaxException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        ArrayList<String> request = new ArrayList<String>();
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            request.add(inputLine);
            if (!in.ready()) {
                break;
            }
        }
        String uriStr = request.get(0).split(" ") [1];
        URI resourceUri = new URI (uriStr);
        System.out.println("URI PATH: " + resourceUri.getPath());
        System.out.println("URI QUERY: " + resourceUri.getQuery());

        if(resourceUri.toString().startsWith("/appuser")){
            outputLine = getComponentResourse(resourceUri);
            out.println(outputLine);
        }else{
            outputLine = getHTMLResourse(resourceUri);
            out.println(outputLine);
        }

        out.close();
        in.close();
        clientSocket.close();
    }

    private String getComponentResourse(URI resourceUri) {
        String response = default404HTMLResponse();
        try {
            String classPath = resourceUri.getPath().toString().replace("/appuser/", "");
            Class component = Class.forName(classPath);
            for (Method m : component.getDeclaredMethods()){
                if (m.isAnnotationPresent(Service.class)){
                    response = m.invoke(null).toString();
                    response = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/html\r\n"
                            + "\r\n" + response;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return response;
    }

//    private void searchForComponents(){
//
//    }
//
//    private void loadServices (Class c) {
//        for (Method m : c.getDeclaredMethods()){
//            if (m.isAnnotationPresent(Service.class)){
//                String uri = m.getAnnotation(Service.class).uri();
//                services.put(uri, m);
//            }
//        }
//    }

    public String getHTMLResourse(URI resourceURI){
        Path file = Paths.get("public_html" + resourceURI.getPath());
        String response = "";
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            response = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n";
            String line = null;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
                response = response + line;
            }
        }catch(IOException x){
            System.err.format("IOException: %s%n", x);
            response = default404HTMLResponse();
        }
        return response;
    }

    public String default404HTMLResponse(){
        String outputLine =  "HTTP/1.1 400 Not Found\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                +   "<head>"
                +       "<title>404 Not Found</title>"
                +       "<meta charset=\"UTF-8\">"
                +       "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                +   "</head>"
                +   "<body>"
                +       "<div><h1>Error 404</h1></div>"
                +   "</body>"
                + "</html>";
        return outputLine;
    }
    public String computeDefaultResponse(){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                +   "<head>"
                +       "<title>200 ok</title>"
                +       "<meta charset=\"UTF-8\">"
                +       "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                +   "</head>"
                +   "<body>"
                +       "<div><h1>My Web Site</h1></div>"
                +       "<img src = \"https://images.unsplash.com/photo-1614026480209-cd9934144671?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2850&q=80\">"
                +   "</body>"
                + "</html>";
        return outputLine;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer.get_instance().start(args);
    }
}

