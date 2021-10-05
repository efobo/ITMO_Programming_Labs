package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.IncorrectScriptException;
import Exceptions.LabworkNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import app.Asker;
import app.CollectionManager;
import data.Coordinates;
import data.Labwork;

import java.time.LocalDateTime;

/**
 * Класс команды remove_greater
 */
public class RemoveGreater extends ACommand {
    private CollectionManager collectionManager;
    private Asker asker;


    public RemoveGreater (CollectionManager collectionManager, Asker asker) {
        super ("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
        this.asker = asker;
    }

    /**
     * @param argument
     * @return command exit status
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            Labwork lbToCompare = new Labwork(collectionManager.generateID(),
                    asker.askName(),
                    new Coordinates(asker.askXc(), asker.askYc()),
                    LocalDateTime.now(),
                    asker.askMinimalPoint(),
                    asker.askDifficulty(),
                    asker.askPerson());
            Labwork lbFromCollection = collectionManager.getByValue(lbToCompare);
            if (lbFromCollection == null ) throw new LabworkNotFoundException();
            collectionManager.removeGreater(lbFromCollection);
            System.out.println("Элементы успешно удалены");
            return true;
        } catch (WrongAmountOfElementsException e) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        } catch (EmptyCollectionException e) {
            System.out.println("Пустая коллекция");
        } catch (LabworkNotFoundException e) {
            System.out.println("Такого элемента в коллекции нет");
        } catch (IncorrectScriptException e) {
            System.out.println("Неправильно написан скрипт");
        }
        return false;
    }
}
