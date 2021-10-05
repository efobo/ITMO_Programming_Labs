package app;

import Database.DatabaseCollectionManager;
import Database.DatabaseUserHandler;
import Exceptions.NotInDeclaredLimitsException;
import Exceptions.WrongAmountOfElementsException;
import Database.DatabaseManager;
import commands.*;
import managers.CollectionManager;
import managers.CommandManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final int max_clients = 1000;
    public static int port;
    public static Logger logger = Logger.getLogger("ServerLogger");
    private static String databaseUsername = "s312486";
    private static String databaseHost;
    private static String databasePassword;
    private static String databaseAddress;

    /**
     * 1 - порт
     * 2 - хост
     * 3 - пароль
     * @param args
     * @return
     */
    private static boolean initialize(String[] args) {
        try {
            if (args.length != 3) throw new WrongAmountOfElementsException();
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            databaseHost = args[1];
            databasePassword = args[2];
            //databaseAddress = "jdbc:postgresql://pg/studs";
            //databaseAddress = "jdbc:postgresql://" + databaseHost + ":" + 5432 + "/studs";
            databaseAddress = "jdbc:postgresql://" + databaseHost + ":" + 1610 + "/studs";
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            System.out.println("Использование: 'java -jar " + jarName + " <port> <db_host> <db_password>'");
        } catch (NumberFormatException exception) {
            System.out.println("Порт должен быть представлен числом!");
            Main.logger.log(Level.SEVERE, "Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            System.out.println("Порт не может быть отрицательным!");
            Main.logger.log(Level.SEVERE, "Порт не может быть отрицательным!");
        }
        Main.logger.log(Level.SEVERE, "Ошибка инициализации порта запуска!");
        return false;
    }

    public static void main (String[] args) {
        if (!initialize(args)) return;
        DatabaseManager databaseManager = new DatabaseManager(databaseAddress, databaseUsername, databasePassword);
        DatabaseUserHandler databaseUserHandler = new DatabaseUserHandler(databaseManager);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseManager, databaseUserHandler);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(new Add(collectionManager, databaseCollectionManager),
                new AddIfMin(collectionManager, databaseCollectionManager),
                new Clear(collectionManager, databaseCollectionManager),
                new ExecuteScript(),
                new Info(collectionManager),
                new RemoveByID(collectionManager, databaseCollectionManager),
                new RemoveGreater(collectionManager, databaseCollectionManager),
                new RemoveLower(collectionManager, databaseCollectionManager),
                new Login(databaseUserHandler),
                new Register(databaseUserHandler),
                new Update(collectionManager, databaseCollectionManager),
                new Refresh(),
                new Exit(collectionManager));
        Server server = new Server(1212, max_clients, commandManager, collectionManager);
        Thread threadServer = new Thread(server);
        threadServer.start();
        InputThreadHandler inputThreadHandler = new InputThreadHandler(server, databaseManager);
        Thread iohThread = new Thread(inputThreadHandler);
        iohThread.start();
    }
}
