package managers;

import Communication.Request;
import Communication.Response;
import Communication.ResponseCode;
import app.Main;
import app.Server;
import serialize.Deserialize;
import serialize.Serialize;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;
import java.util.logging.Level;

public class ConnectionHandler implements Runnable {
    private Server server;
    private SocketChannel clientSocket;
    private CollectionManager collectionManager;
    private CommandManager commandManager;
    private ForkJoinPool forkJoinPool = new ForkJoinPool(2);
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

    public ConnectionHandler (Server server, SocketChannel clientSocket, CommandManager commandManager, CollectionManager collectionManager) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        System.out.println("Подключен новый клиент");
        Main.logger.log(Level.INFO, "Подключен новый клиент");
    }


    @Override
    public void run() {
        Request userRequest = null;
        Response responseToUser = null;
        boolean stopFlag = false;
        byte [] inputBuf = new byte[2048];
        try {
            do {
                clientSocket.read(ByteBuffer.wrap(inputBuf));
                userRequest = Deserialize.deserializeRequest(inputBuf);
                inputBuf = new byte[2048];
                responseToUser = forkJoinPool.invoke(new HandleRequestTask(userRequest, commandManager, collectionManager));
                //responseToUser = forkJoinPool.submit(new HandleRequestTask(userRequest, commandManager));
                Main.logger.log(Level.INFO, "Запрос '" + userRequest.getCommandName() + "' обработан.");
                byte[] outputBuf = Serialize.serializeResponse(responseToUser);
                if (!fixedThreadPool.submit(() -> {
                    try {
                        clientSocket.write(ByteBuffer.wrap(Serialize.serializeInteger(outputBuf.length)));
                        Thread.sleep(100);
                        clientSocket.write(ByteBuffer.wrap(outputBuf));
                        System.out.println("Отправил");
                        return true;
                    } catch (IOException exception) {
                        System.out.println("Ошибка при отправке данных на клиент!");
                        Main.logger.log(Level.SEVERE, "Ошибка при отправке данных на клиент!");
                    }
                    return false;
                }).get()) break;
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT &&
                    responseToUser.getResponseCode() != ResponseCode.CLIENT_EXIT);
            if (responseToUser.getResponseCode() == ResponseCode.SERVER_EXIT)
                stopFlag = true;
        } catch (ClassNotFoundException exception) {
            System.out.println("Ошибка при чтении полученных данных!");
            Main.logger.log(Level.SEVERE, "Ошибка при чтении полученных данных!");
        } catch (CancellationException | ExecutionException | InterruptedException exception) {
            System.out.println("При обработке запроса произошла ошибка многопоточности!");
            Main.logger.log(Level.WARNING, "При обработке запроса произошла ошибка многопоточности!");
        } catch (IOException exception) {
            System.out.println("Непредвиденный разрыв соединения с клиентом!");
            Main.logger.log(Level.WARNING, "Непредвиденный разрыв соединения с клиентом!");
        } finally {
            try {
                fixedThreadPool.shutdown();
                clientSocket.close();
                System.out.println("Клиент отключен от сервера.");
                Main.logger.log(Level.INFO, "Клиент отключен от сервера.");
            } catch (IOException exception) {
                System.out.println("Ошибка при попытке завершить соединение с клиентом!");
                Main.logger.log(Level.SEVERE, "Ошибка при попытке завершить соединение с клиентом!");
            }
            if (stopFlag) server.stop();
            server.releaseConnection();
        }
    }



}
