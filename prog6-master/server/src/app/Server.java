package app;

import Communication.*;
import Exceptions.ClosingSocketException;
import Exceptions.ConnectionException;
import Exceptions.OpeningSocketException;
import commands.*;
import data.Labwork;
import managers.*;
import serialize.Deserialize;
import serialize.Serialize;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.TreeSet;
import java.util.logging.Level;

public class Server implements Runnable{
    private final int port;
    private ServerSocketChannel socketChannel = null;
    private boolean closeFlag = false;
    public boolean getFileFlag;
    public boolean connectionFlag = false;
    public SocketChannel clientSocket = null;

    public TreeSet<Labwork> labworks;
    public CollectionFileRecorder cfr;
    public CollectionManager collectionManager;
    public CommandManager commandManager;

    public Server(int port) {
        this.port = port;
        getFileFlag = true;
    }

    private void setClientSocket (SocketChannel clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void setCloseFlag (boolean closeFlag) { this.closeFlag = closeFlag; }

    public void run () {
        try {
            openServerSocketChannel();
            boolean status = true;
            getFileFlag = true;

            while (status) {
                try {
                if (!getFileFlag) {
                    SocketChannel clientSocket = connection();
                    setClientSocket(clientSocket);
                }
                    while (getFileFlag) {
                        SocketChannel clientSocket = connection();
                        setClientSocket(clientSocket);
                        getFileFlag = getRequestFileFromClient(clientSocket);
                    }
                    collectionManager = new CollectionManager(labworks);
                    commandManager = new CommandManager(new Add(collectionManager),
                            new AddIfMin(collectionManager),
                            new Clear(collectionManager),
                            new ExecuteScript(),
                            new FilterContainsName(collectionManager),
                            new Help(),
                            new Info(collectionManager),
                            new MinByID(collectionManager),
                            new PrintDescending(collectionManager),
                            new RemoveByID(collectionManager),
                            new RemoveGreater(collectionManager),
                            new RemoveLower(collectionManager),
                            new Save(collectionManager, cfr),
                            new Show(collectionManager),
                            new Update(collectionManager),
                            new Exit(collectionManager, cfr));
                    RequestHandler requestHandler = new RequestHandler(commandManager);
                    status = getRequestFromClient(clientSocket, requestHandler);
                } catch (ConnectionException e) {
                    connectionFlag = false;
                    break;
                }
            }
           // stop();
        } catch (OpeningSocketException e) {
            System.out.println("Произошла ошибка при запуске сервера");
            Main.logger.log(Level.SEVERE, "Произошла ошибка при запуске сервера");
        }
    }

    public void stop () {
        try {
            if (socketChannel == null) throw new ClosingSocketException();
            socketChannel.socket().close();
            socketChannel.close();
            System.out.println("Работа сервера завершена");
            Main.logger.log(Level.INFO, "Работа сервера завершена");
        } catch (IOException e) {
            System.out.println("Ошибка завершения работы сервера");
            Main.logger.log(Level.SEVERE, "Ошибка завершения работы сервера");
        } catch (ClosingSocketException e) {
            System.out.println("Сервер еще не запущен. Невозможно завершить работу.");
            Main.logger.log(Level.SEVERE, "Сервер еще не запущен. Невозможно завершить работу.");
        }
    }

    public void openServerSocketChannel () throws OpeningSocketException {
        try {
            Main.logger.log(Level.INFO, "Запуск сервера...");
            SocketAddress socketAddress = new InetSocketAddress(port);
            socketChannel = ServerSocketChannel.open();
            socketChannel.bind(socketAddress);
            socketChannel.configureBlocking(false);
            Main.logger.log(Level.INFO, "Сервер успешно запущен");
        } catch (IllegalArgumentException e) {
            System.out.println("Порт '" + port + "' находится за пределами возможных значений!");
            Main.logger.log(Level.SEVERE, "Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningSocketException();
        } catch (IOException e) {
            System.out.println("Ошибка при использованиии порта " + port + " при подключении");
            Main.logger.log(Level.SEVERE, "Ошибка при использованиии порта " + port + " при подключении");
            throw new OpeningSocketException();
        }
    }

    private SocketChannel connection () throws ConnectionException {
        try {
            System.out.println("Прослушивание порта '" + port + "'...");
            Main.logger.log(Level.INFO, "Прослушивание порта '" + port + "'...");
            SocketChannel clientSocket = null;
            while (clientSocket == null) {
                clientSocket = socketChannel.accept();
                if (clientSocket != null) {
                    clientSocket.configureBlocking(false);
                    System.out.println("Соединение с клиентом установлено!");
                    Main.logger.log(Level.INFO, "Соединение с клиентом установлено!");
                    connectionFlag = true;
                }
            }
            return clientSocket;
        } catch (IOException e) {
            if (!closeFlag) {
                System.out.println("Ошибка при соединении с клиентом");
                Main.logger.log(Level.SEVERE, "Ошибка при соединении с клиентом");
            }
            throw new ConnectionException();
        }
    }


    private boolean getRequestFileFromClient (SocketChannel clientSocket) {
        RequestFile requestFile;
        ResponseFile responseFile = null;
        boolean fileStatus = false;
        ByteBuffer inputBuf = ByteBuffer.allocate(2048);
        byte[] outputBuf;
        boolean flag = true;
        boolean filebytes = true;
        Long length = Long.valueOf(0);
        try {
            do {
                if (length > 2048) {
                    inputBuf = ByteBuffer.allocate(length.intValue());
                }
                clientSocket.read(inputBuf);
                if (inputBuf.position() == 0) continue;
                if (filebytes) {
                    length = Deserialize.deserializeLong(inputBuf.array());
                    inputBuf.clear();
                    filebytes = false;
                    continue;
                }
                Main.logger.log(Level.INFO, "Сервер десериализует файл");
                requestFile = Deserialize.deserializeRequestFile(inputBuf.array());
                inputBuf.clear();
                File file = requestFile.getFile();
                    cfr = new CollectionFileRecorder(file);
                    labworks = cfr.readCollection();
                    if (cfr.check) {
                        fileStatus = true;
                        Main.logger.log(Level.INFO, "Файл '" + file.getName() + "' успешно обработан.");
                    } else {
                        Main.logger.log(Level.INFO, "Файл '" + file.getName() + "' некорректный.");
                    }
                    responseFile = new ResponseFile(fileStatus, ResponseOutput.getAndClear());
                    Main.logger.log(Level.INFO, "Сервер сериализует ответ на отправку файла");
                    outputBuf = Serialize.serializeResponseFile(responseFile);
                    clientSocket.write(ByteBuffer.wrap(outputBuf));
                    flag = !responseFile.getFileStatus();
            } while (flag);
            return false;
        } catch (IOException e) {
            connectionFlag = false;
            System.out.println("Непредвиденный разрыв соединения с клиентом");
            Main.logger.log(Level.WARNING, "Непредвиденный разрыв соединения с клиентом");
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка чтения полученных от клиента данных");
            Main.logger.log(Level.SEVERE, "Ошибка чтения полученных от клиента данных");
        }
        return true;
    }

    private boolean getRequestFromClient (SocketChannel clientSocket, RequestHandler requestHandler) {
        Request userRequest = null;
        Response responseToUser = null;
        ByteBuffer inputBuf = ByteBuffer.allocate(2048);
        byte [] outputBuf;
        boolean flag = true;
        try {

            do {
                while (true) {
                    clientSocket.read(inputBuf);
                    if (inputBuf.position() == 0) continue;
                    Main.logger.log(Level.INFO, "Сервер десериализует запрос");
                    userRequest = Deserialize.deserializeRequest(inputBuf.array());
                    inputBuf.clear();
                    break;
                }
                responseToUser = requestHandler.handle(userRequest);
                Main.logger.log(Level.INFO, "Запрос '" + userRequest.getCommandName() + "' успешно обработан.");
                Main.logger.log(Level.INFO, "Сервер сериализует ответ");
                outputBuf = Serialize.serializeResponse(responseToUser);
                clientSocket.write(ByteBuffer.wrap(outputBuf));
                if (responseToUser.getResponseCode() == ResponseCode.CLIENT_EXIT) throw new IOException();
                //flag = responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT;
               // flag = !closeFlag;
            } while (flag);
            getFileFlag = true;
            return false;
        } catch (IOException e) {
            getFileFlag =true;
            connectionFlag = false;
            if (userRequest == null) {
                System.out.println("Непредвиденный разрыв соединения с клиентом");
                Main.logger.log(Level.WARNING, "Непредвиденный разрыв соединения с клиентом");
            } else {
                System.out.println("Клиент успешно отсоединен от сервера");
                Main.logger.log(Level.INFO, "Клиент успешно отсоединен от сервера");

            }
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка чтения полученных от клиента данных");
            Main.logger.log(Level.SEVERE, "Ошибка чтения полученных от клиента данных");
        }

        return true;
    }


}
