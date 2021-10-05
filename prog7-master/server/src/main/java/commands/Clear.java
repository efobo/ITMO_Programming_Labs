package commands;

import Communication.User;
import Database.DatabaseCollectionManager;
import Exceptions.DatabaseHandlingException;
import Exceptions.ManualDatabaseEditException;
import Exceptions.PermissionDeniedException;
import Exceptions.WrongAmountOfElementsException;
import data.Labwork;
import managers.CollectionManager;
import managers.ResponseOutput;

/**
 * Класс команды clear
 */
public class Clear extends ACommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public Clear (CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("clear","","очистить коллекцию");
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
            if (!strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            for (Labwork lb : collectionManager.getCollection()) {
                if (!lb.getUser().equals(user)) throw new PermissionDeniedException();
                if (databaseCollectionManager.checkLabworkUserId(lb.getId(), user)) throw new ManualDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clearCollection();
            ResponseOutput.appendln("ClearCollection");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        } catch (PermissionDeniedException e) {
            ResponseOutput.appenderror("NoughRightsException");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appenderror("DatabaseHandlingException");
        } catch (ManualDatabaseEditException e) {
            ResponseOutput.appenderror("ManualDatabaseException");
        }
        return false;
    }
}

