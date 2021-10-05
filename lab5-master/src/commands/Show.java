package commands;

import Exceptions.WrongAmountOfElementsException;
import app.CollectionManager;
/**
 * Класс команды show
 */
public class Show extends ACommand{

    private CollectionManager collectionManager;

    public Show (CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * @param argument
     * @return command exit status
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            System.out.println(collectionManager.getCollection());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        }
        return false;
    }
}
