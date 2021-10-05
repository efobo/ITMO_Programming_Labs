package app;

import java.util.logging.Logger;

public class Main {
    public static final int port = 1708;
    public static Logger logger = Logger.getLogger("ServerLogger");

    public static void main (String[] args) {
        Server server = new Server(port);
        Thread threadServer = new Thread(server);
        threadServer.start();
        InputThreadHandler inputThreadHandler = new InputThreadHandler(server);
        Thread iohThread = new Thread(inputThreadHandler);
        iohThread.start();
    }
}
