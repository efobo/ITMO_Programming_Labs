package commands;

import Exceptions.IncorrectScriptException;
import Exceptions.WrongAmountOfElementsException;
import app.*;
import data.*;

import java.time.LocalDateTime;

/**
 * Класс команды Add
 */
public class Add extends ACommand{
    private CollectionManager collectionManager;
    private Asker asker;

    public Add (CollectionManager collectionManager, Asker asker) {
        super("add {element}", "добавить новый элемент в коллекцию");
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

            Labwork lb = new Labwork(collectionManager.generateID(),
                    asker.askName(),
                    new Coordinates(asker.askXc(), asker.askYc()),
                    LocalDateTime.now(),
                    asker.askMinimalPoint(),
                    asker.askDifficulty(),
                    asker.askPerson());
        collectionManager.addToCollection(lb);
        System.out.println("Новый элемент добавлен в коллекцию!");
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
