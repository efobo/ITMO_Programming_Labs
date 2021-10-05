package App;

import Communication.*;
import Managers.UserHandler;
import Exceptions.ConnectionException;
import Exceptions.NotInDeclaredLimitsException;
import serialize.Deserialize;
import serialize.Serialize;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class Client {
    private String host;
    private int port;
    private UserHandler userHandler;
    private SocketChannel clientSocket;
    private InputStream serverReader;
    private OutputStream serverWriter;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private boolean fileStatus;

    public Client (String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler) {
        this.host = host;
        this.port = port;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.reconnectionTimeout = reconnectionTimeout;
        this.userHandler = userHandler;
        fileStatus = true;
    }


    public void run () {
        try {
            boolean processStatus = true;
            while (processStatus) {
                try {
                    boolean processFileStatus = fileStatus;
                    if (!processFileStatus) {connection();}
                    while (processFileStatus) {
                        connection();
                        fileStatus = false;
                        processFileStatus = sendFileToServer();
                    }
                    processStatus = RequestToServer();
                } catch (ConnectionException e) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        System.out.println("Превышено количество попыток подключения");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException ilArgEx) {
                        System.out.println("Время ожидания подключения '" + reconnectionTimeout
                                + "' находится за пределами возможных значений!");
                        System.out.println("Повторное подключение будет произведено немедленно.");
                    } catch (Exception timeoutException) {
                        System.out.println("Ошибка при попытке ожидания подключения");
                        System.out.println("Повторное подключение будет произведено немедленно.");
                    }
                }
                reconnectionAttempts++;
            }
            if (clientSocket != null) clientSocket.close();
            System.out.println("Работа клиента успешно завершена");
        } catch (IOException e) {
            System.out.println("Ошибка при попытке завершить соединения с сервером");
        } catch (NotInDeclaredLimitsException e) {
            System.out.println("Клиент не может быть запущен");
        }
    }

    private void connection () throws ConnectionException, NotInDeclaredLimitsException {
        try {
            fileStatus = true;
            if (reconnectionAttempts >= 1) System.out.println("Повторное соединение с сервером...");
            clientSocket = SocketChannel.open(new InetSocketAddress(host, port));
            System.out.println("Соединение с сервером успешно установлено");
            System.out.println("Ожидание разрешения начала обмена данными");
            serverReader = clientSocket.socket().getInputStream();
            serverWriter = clientSocket.socket().getOutputStream();
            System.out.println("Разрешение на обмен данными получено");
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректно введен адрес сервера");
            throw new NotInDeclaredLimitsException();
        } catch (IOException e) {
            System.out.println("Ошибка при соединении с сервером");
            throw new ConnectionException();
        }
    }


    private boolean sendFileToServer () {
        RequestFile requestFile = null;
        ResponseFile responseFile = null;
        byte[] inputBuf = new byte[2048];
        do {
            try {
                requestFile = userHandler.fileHandle();
                serverWriter.write(Serialize.serializeLong(requestFile.getFileLength()));
                Thread.sleep(100);
                serverWriter.write(Serialize.serializeRequestFile(requestFile));
                serverWriter.flush();
                serverReader.read(inputBuf);
                responseFile = Deserialize.deserializeResponseFile(inputBuf);
                System.out.println(responseFile.getResponseBody());
            } catch (IOException e) {
                System.out.println("Соединение с сервером потеряно");
                try {
                    reconnectionAttempts++;
                    connection();
                } catch (ConnectionException | NotInDeclaredLimitsException exception) {
                    System.out.println("Попробуйте повторить ввод позднее");
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Ошибка при чтении полученных данных");
            } catch (InterruptedException e) {
                System.out.println("Ошибка! Поток прерван");
            }
        } while (!Objects.requireNonNull(responseFile).getFileStatus());
        return false;
    }

    private boolean RequestToServer () {
        Request requestToServer = null;
        Response serverResponse = null;
        byte [] inputBuf = new byte[2048];
        do {
            try {

                requestToServer = serverResponse != null? userHandler.handle(serverResponse.getResponseCode()) :
                        userHandler.handle(null);
                if (requestToServer.isEmpty()) continue;
                serverWriter.write(Serialize.serializeRequest(requestToServer));
                serverWriter.flush();
                serverReader.read(inputBuf);
                serverResponse = Deserialize.deserializeResponse(inputBuf);
                System.out.println(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException e) {
                System.out.println("Ошибка при отправке данных на сервер");
            } catch (ClassNotFoundException e) {
                System.out.println("Ошибка при чтении полученных данных");
            } catch (IOException e){
                System.out.println("Соединение с сервером потеряно");
                if (requestToServer.getCommandName().equals("exit")) {
                    System.out.println("Команда не будет зарегистрирована на сервере");
                    break;
                }
                    fileStatus = true;
                    reconnectionAttempts++;
                    return true;
            }
        } while(!requestToServer.getCommandName().equals("exit"));
        return false;
    }
}
