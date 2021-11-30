import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URL;

public class RealiWebApp {
    private static final String WEBAPP_RESOURCES_LOCATION = "webapp";

    public static void main(String[] args) throws Exception {
        DBHandler.prepareDb();
        startWebServer();
    }

    private static void startWebServer() throws Exception {
        Server server = new Server(8080);
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        URL webAppDir = Thread.currentThread().getContextClassLoader().getResource(WEBAPP_RESOURCES_LOCATION);
        webAppContext.setResourceBase(webAppDir.toURI().toString());
        server.setHandler(webAppContext);
        server.start();
        server.join();
    }
}
