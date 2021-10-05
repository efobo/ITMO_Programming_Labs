package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.LabworkNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import data.*;
import managers.CollectionManager;
import managers.ResponseOutput;

import java.time.LocalDateTime;

/**
 * Класс команды update
 */
public class Update extends ACommand {
    private CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
    }

    /**
     *
     * @param strArg
     * @param objArg
     * @return
     */
    @Override
    public boolean execute(String strArg, Object objArg) {
        try {
            if (strArg.isEmpty() || objArg == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            Long id = Long.parseLong(strArg);
            if (id <= 0) throw new NumberFormatException();
            Labwork lb = collectionManager.getById(id);
            if (lb == null) throw new LabworkNotFoundException();

            DemoLabwork demoLabwork = (DemoLabwork) objArg;
            String name = demoLabwork.getName() == null? lb.getName() : demoLabwork.getName();
            Coordinates coordinates = demoLabwork.getCoordinates() == null?
                    lb.getCoordinates() : demoLabwork.getCoordinates();
            LocalDateTime creationDate = lb.getCreationDate();
            double minimalPoint = demoLabwork.getMinimalPoint() == -1?
                    lb.getMinimalPoint() : demoLabwork.getMinimalPoint();
            Difficulty difficulty = demoLabwork.getDifficulty() == null? lb.getDifficulty() : demoLabwork.getDifficulty();
            Person author = demoLabwork.getAuthor() == null? lb.getAuthor() : demoLabwork.getAuthor();

            collectionManager.remove(lb);
            collectionManager.addToCollection(
                    new Labwork(id, name, coordinates, creationDate, minimalPoint, difficulty, author));

            ResponseOutput.appendln("Элемент обновлен!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: необходим String аргумент - id, необходим Object аргумент.");
        } catch (EmptyCollectionException e) {
            ResponseOutput.appendln("Пустая коллекция");
        } catch (LabworkNotFoundException e) {
            ResponseOutput.appendln("Элемента с таким id нет");
        } catch (NumberFormatException e) {
            ResponseOutput.appendln("Аргумент должен быть числом");
        }
        return false;
    }
}

