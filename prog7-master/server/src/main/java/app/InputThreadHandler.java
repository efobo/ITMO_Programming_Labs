package app;

import Communication.Response;
import Communication.ResponseCode;
import Database.DatabaseManager;

import java.util.Scanner;

public class InputThreadHandler implements Runnable{

    Scanner scanner = new Scanner(System.in);

    private Server server;
    private DatabaseManager databaseManager;

    public InputThreadHandler(Server server, DatabaseManager databaseManager){
        this.server = server;
        this.databaseManager = databaseManager;
    }

    public void run() {

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("server_exit")) {
                System.out.println("Исполльзуется команда 'server_exit'");
                if (server.connectionFlag) {
                    System.out.println("Невозможно завершить работу сервера пока к серверу подключен клиент");
                } else {
                    server.stop();
                    databaseManager.closeConnection();
                    break;
                }
            }
        }
    }
}
