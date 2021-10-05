package app;

import Exceptions.ClosingSocketException;
import Exceptions.ConnectionException;
import Exceptions.OpeningSocketException;
import data.Labwork;
import managers.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.util.logging.Level;

public class Server implements Runnable{
    private final int port;
    private ServerSocketChannel socketChannel = null;
    private boolean closeFlag = false;
    private boolean isStopped;
    private int max_clients;
    public boolean getFileFlag;
    public boolean connectionFlag = false;
    public SocketChannel clientSocket = null;

    public TreeSet<Labwork> labworks;
    public CollectionManager collectionManager;
    public CommandManager commandManager;
    private Semaphore semaphore;
    //private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private ExecutorService forkJoinPool = new ForkJoinPool(2);

    public Server(int port, int max_clients, CommandManager commandManager, CollectionManager collectionManager) {
        this.port = port;
        this.max_clients = max_clients;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.semaphore = new Semaphore(max_clients);
    }

    public void run () {
        try {
            openServerSocketChannel();
            while (!isStopped()) {
                try {
                    acquireConnection();
                    if (isStopped()) throw new ConnectionException();
                    System.out.println("Прослушивание порта '" + port + "'...");
                    Main.logger.log(Level.INFO, "Прослушивание порта '" + port + "'...");
                    //clientSocket = connection();
                    //forkJoinPool.submit(new ConnectionHandler(this, connection(), commandManager));
                    while (true) {
                        forkJoinPool.execute(new ConnectionHandler(this, socketChannel.accept(), commandManager, collectionManager));
                    }
                } catch (IOException e) {
                    if (!closeFlag) {
                        System.out.println("Ошибка при соединении с клиентом");
                        Main.logger.log(Level.SEVERE, "Ошибка при соединении с клиентом");
                    }
                } catch (ConnectionException e) {
                    if (isStopped()) {
                        System.out.println("Ошибка при соединении с клиентом!");
                        Main.logger.log(Level.SEVERE, "Ошибка при соединении с клиентом!");
                    } else break;
                }
                forkJoinPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                System.out.println("Работа сервера завершена");
            }
        } catch (OpeningSocketException e) {
            System.out.println("Сервер не может быть запущен!");
            Main.logger.log(Level.SEVERE, "Сервер не может быть запущен!");
        } catch (InterruptedException e) {
            System.out.println("Ошибка при завершении работы с уже подключенными клиентами!");
        }
    }

    public synchronized void stop () {
        try {
            Main.logger.log(Level.INFO, "Завершение работы сервера");
            if (socketChannel == null) throw new ClosingSocketException();
            isStopped = true;
            forkJoinPool.shutdown();
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
            Main.logger.log(Level.INFO, "Сервер успешно запущен");
            isStopped = false;
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


    private synchronized boolean isStopped() {
        return isStopped;
    }

    public void releaseConnection() {
        semaphore.release();
        Main.logger.log(Level.INFO, "Разрыв соединения зарегистрирован.");
    }

    public void acquireConnection() {
        try {
            semaphore.acquire();
            Main.logger.log(Level.INFO, "Разрешение на новое соединение получено.");
        } catch (InterruptedException e) {
            System.out.println("Ошибка при получении разрешения на новое соединение!");
            Main.logger.log(Level.SEVERE, "Ошибка при получении разрешения на новое соединение!");
        }
    }
}
