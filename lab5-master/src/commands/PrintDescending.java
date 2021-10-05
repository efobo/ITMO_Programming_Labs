package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.WrongAmountOfElementsException;
import app.CollectionManager;

/**
 * Класс для команды print_descending
 */
public class PrintDescending extends ACommand{
    private CollectionManager collectionManager;


    public PrintDescending (CollectionManager collectionManager) {
        super ("print_descending", "вывести элементы коллекции в порядке убывания");
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
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            collectionManager.showDescending();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        } catch (EmptyCollectionException e) {
            System.out.println("Пустая коллекция");
        }
        return false;
    }
}
