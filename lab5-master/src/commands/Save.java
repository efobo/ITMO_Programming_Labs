package commands;

import Exceptions.WrongAmountOfElementsException;
import app.CollectionManager;

/**
 * Класс команды save
 */
public class Save extends ACommand{
private CollectionManager collectionManager;

    public Save (CollectionManager collectionManager) {super("save", "сохранить коллекцию в файл");
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
            collectionManager.saveCollection();
            System.out.println("Сохранено!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        }
        return false;
    }
}
