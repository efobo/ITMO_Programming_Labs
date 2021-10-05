package commands;

import Exceptions.WrongAmountOfElementsException;
import app.CollectionManager;

/**
 * Класс команды clear
 */
public class Clear extends ACommand {
    private CollectionManager collectionManager;
    public Clear (CollectionManager collectionManager){
        super("clear","очистить коллекцию");
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
            collectionManager.clearCollection();
            System.out.println("Коллекция очищена!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        }
        return false;
    }
}

