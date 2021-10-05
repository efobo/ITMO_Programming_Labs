package commands;

import Exceptions.IncorrectScriptException;
import Exceptions.WrongAmountOfElementsException;
import app.Asker;
import app.CollectionManager;
import data.Coordinates;
import data.Labwork;


import java.time.LocalDateTime;

/**
 * Класс команды add_if_min
 */
public class AddIfMin extends ACommand{
    private CollectionManager collectionManager;
    private Asker asker;

    public AddIfMin (CollectionManager collectionManager, Asker asker) {
        super ("add_if_min {element}", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
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
            Labwork lbToCompare = new Labwork(collectionManager.generateID(),
                    asker.askName(),
                    new Coordinates(asker.askXc(), asker.askYc()),
                    LocalDateTime.now(),
                    asker.askMinimalPoint(),
                    asker.askDifficulty(),
                    asker.askPerson());

            if (collectionManager.addToCollectionIfMin(lbToCompare)) {
                System.out.println("Элемент успешно добавлен!" + lbToCompare.compareTo(collectionManager.getFirstElement()));
            } else {
                System.out.println("Элемент не является минимальным для этой коллекции, поэтому добавлен не был.");
            }
            return true;
        } catch (WrongAmountOfElementsException e) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        }catch (IncorrectScriptException e) {
            System.out.println("Неправильно написан скрипт");
        }
        return false;
    }
}


