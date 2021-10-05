package app;

import Communication.Response;
import Communication.ResponseCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.logging.Level;

public class InputThreadHandler implements Runnable{

    Scanner scanner = new Scanner(System.in);

    private Server server;

    public InputThreadHandler(Server server){
        this.server = server;
    }

    public void run() {

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("server_exit")) {
                System.out.println("Исполльзуется команда 'server_exit'");
                if (server.connectionFlag) {
                    System.out.println("Невозможно завершить работу сервера пока к серверу подключен клиент");
                } else {
                    server.setCloseFlag(true);
                    server.stop();
                    break;
                }
            } else {
                if (input.equals("save")) {
                    System.out.println("Исполльзуется команда 'save'");
                    if (!server.getFileFlag) {
                        server.commandManager.save("", null);
                    } else {
                        System.out.println("От клиента еще не получена коллекция. Ее нельзя сохранить.");
                    }

                } else {
                    System.out.println("Неизвестная команда");
                }
            }
           // if (!server.getFileFlag) {
                //String input = scanner.nextLine();
             //   if (input.equals("save")) {
               //     System.out.println("Исполльзуется команда 'save'");
                 //   server.commandManager.save("", null);
                //} else {
                  //  System.out.println("Неизвестная команда");
                //}
           // }
        }
    }
}
