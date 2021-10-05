package commands;

import Communication.User;
import Database.DatabaseCollectionManager;
import Exceptions.DatabaseHandlingException;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;
import data.DemoLabwork;

/**
 * Класс команды Add
 */
public class Add extends ACommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public Add (CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add", "{element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     *
     * @param strArg
     * @param objArg
     * @return
     */
    @Override
    public boolean execute(String strArg, Object objArg, User user) {
        try {
            if (!strArg.isEmpty() || objArg == null) throw new WrongAmountOfElementsException();
            DemoLabwork demoLabwork = (DemoLabwork) objArg;
            collectionManager.addToCollection(databaseCollectionManager.insertLabwork(demoLabwork, user));
            ResponseOutput.appendln("LabworkWasAdded");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appendln("DatabaseHandlingException");
        } catch (ClassCastException e) {
            ResponseOutput.appenderror("ClientObjectException");
        }
        return false;
    }
}
