package commands;

import Communication.User;
import Database.DatabaseCollectionManager;
import Exceptions.*;
import data.*;
import managers.CollectionManager;
import managers.ResponseOutput;

import java.time.LocalDateTime;

/**
 * Класс команды update
 */
public class Update extends ACommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public Update(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update <ID> {element}", "<ID> {element}", "обновить значение элемента коллекции по ID");
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
            if (strArg.isEmpty() || objArg == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();

            long id = Long.parseLong(strArg);
            if (id <= 0) throw new NumberFormatException();
            Labwork oldLabwork = collectionManager.getById(id);
            if (oldLabwork == null) throw new LabworkNotFoundException();
            if (!oldLabwork.getUser().equals(user)) throw new PermissionDeniedException();
            if (databaseCollectionManager.checkLabworkUserId(oldLabwork.getId(), user)) throw new ManualDatabaseEditException();
            DemoLabwork demoLabwork = (DemoLabwork) objArg;

            databaseCollectionManager.updateLabworkById(id, demoLabwork);

            String name = demoLabwork.getName() == null ? oldLabwork.getName() : demoLabwork.getName();
            Coordinates coordinates = demoLabwork.getCoordinates() == null ? oldLabwork.getCoordinates() : demoLabwork.getCoordinates();
            LocalDateTime creationDate = oldLabwork.getCreationDate();
            double minimalPoint = demoLabwork.getMinimalPoint() == -1 ? oldLabwork.getMinimalPoint() : demoLabwork.getMinimalPoint();
            Difficulty difficulty = demoLabwork.getDifficulty() == null ? oldLabwork.getDifficulty() : demoLabwork.getDifficulty();
            Person person = demoLabwork.getAuthor() == null ? oldLabwork.getAuthor() : demoLabwork.getAuthor();

            collectionManager.remove(oldLabwork);
            collectionManager.addToCollection(new Labwork(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    minimalPoint,
                    difficulty,
                    person,
                    user
            ));
            ResponseOutput.appendln("LabworkChange");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        } catch (EmptyCollectionException e) {
            ResponseOutput.appenderror("CollectionIsEmptyException");
        } catch (LabworkNotFoundException e) {
            ResponseOutput.appenderror("LabworkException");
        } catch (NumberFormatException e) {
            ResponseOutput.appenderror("NoughRightsException");
        } catch (ClassCastException e) {
            ResponseOutput.appenderror("ClientObjectException");
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

