package commands;

import Communication.User;
import Database.DatabaseCollectionManager;
import Exceptions.*;
import managers.CollectionManager;
import data.DemoLabwork;
import data.Labwork;
import managers.ResponseOutput;

import java.time.LocalDateTime;

/**
 * Класс команды remove_greater
 */
public class RemoveGreater extends ACommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;


    public RemoveGreater (CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super ("remove_greater", "{element}","удалить из коллекции все элементы, превышающие заданный");
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
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            DemoLabwork demoLabwork = (DemoLabwork) objArg;
            Labwork lbToCompare = new Labwork(0L,
                    demoLabwork.getName(),
                    demoLabwork.getCoordinates(),
                    LocalDateTime.now(),
                    demoLabwork.getMinimalPoint(),
                    demoLabwork.getDifficulty(),
                    demoLabwork.getAuthor(),
                    user);
            Labwork lbFromCollection = collectionManager.getByValue(lbToCompare);
            if (lbFromCollection == null ) throw new LabworkNotFoundException();
            for (Labwork lb : collectionManager.getGreater(lbFromCollection)) {
                if (!lb.getUser().equals(user)) throw new PermissionDeniedException();
                if (databaseCollectionManager.checkLabworkUserId(lb.getId(), user)) throw new ManualDatabaseEditException();
            }
            for (Labwork lb : collectionManager.getGreater(lbFromCollection)) {
                databaseCollectionManager.deleteLabworkById(lb.getId());
                collectionManager.remove(lb);
            }
            ResponseOutput.appendln("LabworkWasDeleted");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        } catch (EmptyCollectionException e) {
            ResponseOutput.appenderror("CollectionIsEmptyException");
        } catch (LabworkNotFoundException e) {
            ResponseOutput.appenderror("LabworkException");
        } catch (PermissionDeniedException e) {
            ResponseOutput.appenderror("NoughRightsException");
        } catch (ClassCastException e) {
            ResponseOutput.appenderror("ClientObjectException");
        } catch (DatabaseHandlingException e) {
            ResponseOutput.appenderror("DatabaseHandlingException");
        } catch (ManualDatabaseEditException e) {
            ResponseOutput.appenderror("ManualDatabaseException");
        } catch (NumberFormatException e) {
            ResponseOutput.appenderror("IdMustBeNumberException");
        }
        return false;
    }
}
