package commands;

import Communication.User;
import Database.DatabaseCollectionManager;
import Exceptions.*;
import managers.CollectionManager;
import managers.ResponseOutput;
import data.Labwork;


/**
 * Класс команды remove_by_id
 */
public class RemoveByID extends ACommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveByID(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_by_id id", "<ID>", "удалить элемент из коллекции по его id");
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
            if (strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            long id = Long.parseLong(strArg);
            Labwork removeLB = collectionManager.getById(id);
            if (removeLB == null) throw new LabworkNotFoundException();
            if (!removeLB.getUser().equals(user)) throw new PermissionDeniedException();
            if (databaseCollectionManager.checkLabworkUserId(removeLB.getId(), user)) throw new ManualDatabaseEditException();
            databaseCollectionManager.deleteLabworkById(id);
            collectionManager.remove(removeLB);
            ResponseOutput.appendln("LabworkWasDeleted");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        } catch (EmptyCollectionException e) {
            ResponseOutput.appenderror("CollectionIsEmptyException");
        }catch (LabworkNotFoundException e) {
            ResponseOutput.appenderror("IdOfLabworkException");
        } catch (NumberFormatException e) {
            ResponseOutput.appenderror("IdMustBeNumberException");
        } catch (PermissionDeniedException e) {
            ResponseOutput.appenderror("NoughRightsException");
        } catch (ManualDatabaseEditException e) {
            ResponseOutput.appenderror("ManualDatabaseException");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appenderror("DatabaseHandlingException");
        }
        return false;
    }
}
