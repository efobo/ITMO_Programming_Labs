package App;

import Exceptions.NotInDeclaredLimitsException;
import Exceptions.WrongAmountOfElementsException;
import GUI.FrameKeeper;
import Managers.AuthorizationHandler;
import Managers.UserHandler;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static String host;
    public static int port;
    public static int reconnectionTimeout = 5000;
    public static int maxReconnectionAttempts = 5;


    public static boolean initializeConnectionAddress(String[] hostAndPort) {
        try {
            if (hostAndPort.length != 2) throw new WrongAmountOfElementsException();
            host = hostAndPort[0];
            port = Integer.parseInt(hostAndPort[1]);
            if (port < 1 || port > 65535) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException e) {
            String jarName = new File (Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            System.out.println("Использование: 'java -jar " + jarName + " <host> <port>'");
        } catch (NotInDeclaredLimitsException e) {
            System.out.println("Номер порта должен быт числом от 1 до 65 535");
        } catch (NumberFormatException exception) {
            System.out.println("Номер порта должен быть представлен числом!");
        }
        return false;
    }

    public static void main (String[] args) {
        if (!initializeConnectionAddress(args)) return;

        JFrame frame = FrameKeeper.initFrame();
        JPanel startWindow = FrameKeeper.startWindow(new Client(host, port));
        frame.setContentPane(startWindow);
        frame.setVisible(true);
        RefreshThreadHandler refreshThreadHandler = new RefreshThreadHandler();
        Thread refreshThread = new Thread(refreshThreadHandler);
        refreshThread.start();
    }


}
