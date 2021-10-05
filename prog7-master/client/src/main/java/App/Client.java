package App;

import Communication.Request;
import Communication.Response;
import Communication.ResponseCode;
import Communication.User;
import Exceptions.ConnectionException;
import Exceptions.NotInDeclaredLimitsException;
import GUI.OutputUI;
import Managers.AuthorizationHandler;
import Managers.ScriptHandler;
import Managers.UserHandler;
import data.Labwork;
import serialize.Deserialize;
import serialize.Serialize;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.TreeSet;

public class Client {
    private String host;
    private int port;
    private SocketChannel clientSocket;
    private InputStream serverReader;
    private OutputStream serverWriter;
    private boolean isConnected;
    private User user;

    public Client (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run () {
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        String[] columnNames = {
                "Name",
                "Last modified",
                "Type",
                "Size"
        };
        String[][] data = {
                {"addins", "02.11.2006 19:15", "Folder", ""},
                {"AppPatch", "03.10.2006 14:10", "Folder", ""},
                {"assembly", "02.11.2006 14:20", "Folder", ""},
                {"Boot", "13.10.2007 10:46", "Folder", ""},
                {"Branding", "13.10.2007 12:10", "Folder", ""},
                {"Cursors", "23.09.2006 16:34", "Folder", ""},
                {"Debug", "07.12.2006 17:45", "Folder", ""},
                {"Fonts", "03.10.2006 14:08", "Folder", ""},
                {"Help", "08.11.2006 18:23", "Folder", ""},
                {"explorer.exe", "18.10.2006 14:13", "File", "2,93MB"},
                {"helppane.exe", "22.08.2006 11:39", "File", "4,58MB"},
                {"twunk.exe", "19.08.2007 10:37", "File", "1,08MB"},
                {"nsreg.exe", "07.08.2007 11:14", "File", "2,10MB"},
                {"avisp.exe", "17.12.2007 16:58", "File", "12,67MB"},
        };
        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.getContentPane().add(table);
        frame.setVisible(true);
        try {
            connection();
        } catch (NotInDeclaredLimitsException exception) {
            System.out.println("ClientException");
            System.exit(0);
        } catch (ConnectionException exception) {
            System.out.println("Ошибка соединения");
        }
    }

    public boolean connection () throws ConnectionException, NotInDeclaredLimitsException {
        try {
            clientSocket = SocketChannel.open(new InetSocketAddress(host, port));
            System.out.println("Соединение с сервером успешно установлено");
            System.out.println("Ожидание разрешения начала обмена данными");
            serverReader = clientSocket.socket().getInputStream();
            serverWriter = clientSocket.socket().getOutputStream();
            isConnected = true;
            System.out.println("Разрешение на обмен данными получено");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректно введен адрес сервера");
            isConnected = false;
            throw new NotInDeclaredLimitsException();
        } catch (IOException e) {
            System.out.println("Ошибка при соединении с сервером");
            isConnected = false;
            throw new ConnectionException();
        }
    }

    public void stop() {
        try {
            requestToServer("exit", "", null);
            clientSocket.close();
            System.out.println("EndWorkOfClient");
        } catch (IOException | NullPointerException exception) {
            System.out.println("EndWorkOfClientException");
            if (clientSocket == null) System.out.println("EndRunningWorkOfClientException");
        }
    }

    public TreeSet<Labwork> authentication(String login, String password, boolean register) {
        Request requestToServer = null;
        Response serverResponse = null;
        byte [] inputBuf;
        String command;
        Integer length = 0;

        try {
            command = register ? "register" : "login";
            requestToServer = new Request(command, "", null, new User(login, password));
            if (serverWriter == null) throw new IOException();
            serverWriter.write(Serialize.serializeRequest(requestToServer));
            serverWriter.flush();
            inputBuf = new byte[2048];
            serverReader.read(inputBuf);
            length = Deserialize.deserializeInteger(inputBuf);
            inputBuf = new byte[length];
            serverReader.read(inputBuf);
            serverResponse = Deserialize.deserializeResponse(inputBuf);
            OutputUI.tryError(serverResponse.getResponseBody());
        } catch (InvalidClassException | NotSerializableException exception) {
            OutputUI.error("DataSendingException");
        } catch (ClassNotFoundException exception) {
            OutputUI.error("DataReadingException");
        } catch (IOException exception) {
            OutputUI.error("End Connection To Server Exception");
            try {
                connection();
                OutputUI.info("ConnectionToServerComplete");
            } catch (ConnectionException | NotInDeclaredLimitsException reconnectionException) {
                OutputUI.info("TryAuthLater");
            }
        }
        if (serverResponse != null && serverResponse.getResponseCode().equals(ResponseCode.OK)) {
            user = requestToServer.getUser();
            return serverResponse.getLabworks();
        }
        return null;
    }

    public TreeSet<Labwork> requestToServer(String commandName, String strArg, Serializable objArg) {
        Request requestToServer = null;
        Response serverResponse = null;
        byte [] inputBuf;
        Integer length = 0;

        try {
            requestToServer = new Request(commandName, strArg, objArg, user);
            serverWriter.write(Serialize.serializeRequest(requestToServer));
            inputBuf = new byte[2048];
            serverReader.read(inputBuf);
            length = Deserialize.deserializeInteger(inputBuf);
            inputBuf = new byte[length];
            serverReader.read(inputBuf);
            serverResponse = Deserialize.deserializeResponse(inputBuf);
            if (!serverResponse.getResponseBody().isEmpty())
                OutputUI.tryError(serverResponse.getResponseBody());
        } catch (InvalidClassException | NotSerializableException e) {
            OutputUI.error("DataSendingException");
        } catch (IOException e) {
            if (requestToServer.getCommandName().equals("exit")) return null;
            OutputUI.error("EndConnectionToServerException");
            try {
                connection();
                OutputUI.info("ConnectionToServerComplete");
            } catch (ConnectionException | NotInDeclaredLimitsException reconnectionException) {
                OutputUI.info("TryCommandLater");
            }
        } catch (ClassNotFoundException e) {
            OutputUI.error("DataReadingException");
        }
        return serverResponse == null ? null : serverResponse.getLabworks();
    }

    public boolean scriptToServer(File scriptFile) {
        Request requestToServer = null;
        Response serverResponse = null;
        ScriptHandler scriptHandler = new ScriptHandler(scriptFile);
        byte [] inputBuf;
        Integer length = 0;
        do {
            try {
                requestToServer = serverResponse != null ? scriptHandler.handle(serverResponse.getResponseCode(), user) :
                        scriptHandler.handle(null, user);
                if (requestToServer == null) return false;
                if (requestToServer.isEmpty()) continue;
                serverWriter.write(Serialize.serializeRequest(requestToServer));
                inputBuf = new byte[2048];
                serverReader.read(inputBuf);
                length = Deserialize.deserializeInteger(inputBuf);
                inputBuf = new byte[length];
                serverReader.read(inputBuf);
                serverResponse = Deserialize.deserializeResponse(inputBuf);
                if (!serverResponse.getResponseBody().isEmpty())
                    OutputUI.tryError(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                OutputUI.error("DataSendingException");
            } catch (ClassNotFoundException exception) {
                OutputUI.error("DataReadingException");
            } catch (IOException exception) {
                OutputUI.error("EndConnectionToServerException");
                try {
                    connection();
                    OutputUI.info("ConnectionToServerComplete");
                } catch (ConnectionException | NotInDeclaredLimitsException reconnectionException) {
                    OutputUI.info("TryCommandLater");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return true;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
