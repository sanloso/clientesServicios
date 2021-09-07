package co.edu.escuelaing.webapp;

import co.edu.escuelaing.networking.httpserver.HttpServer;

import java.io.IOException;
import java.net.URISyntaxException;

public class AppStarter {

    public static void main(String [] args) throws IOException, URISyntaxException {
        HttpServer.get_instance().start(args);
    }
}
