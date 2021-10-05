package commands;

import Communication.User;
import Database.DatabaseCollectionManager;
import Exceptions.DatabaseHandlingException;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import data.DemoLabwork;
import data.Labwork;
import managers.ResponseOutput;

/**
 * Класс команды add_if_min
 */
public class AddIfMin extends ACommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddIfMin (CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super ("add_if_min", "{element}", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
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
            Labwork lbToCompare = databaseCollectionManager.insertLabwork(demoLabwork, user);
            if (collectionManager.collectionSize() == 0 || lbToCompare.compareTo(collectionManager.getFirstElement()) < 0) {
                collectionManager.addToCollection(lbToCompare);
                ResponseOutput.appendln("Labwork Was Added");
                return true;
            } else ResponseOutput.appendln("Labwork Isn't min");
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appenderror("DatabaseHandlingException");
        } catch (ClassCastException exception) {
            ResponseOutput.appenderror("ClientObjectException");
        }
        return false;
    }
}


