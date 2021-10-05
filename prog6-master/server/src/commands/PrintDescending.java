package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;

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
     *
     * @param strArg
     * @param objArg
     * @return
     */
    @Override
    public boolean execute(String strArg, Object objArg) {
        try {
            if (!strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            collectionManager.showDescending();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент не нужен.");
        } catch (EmptyCollectionException e) {
            ResponseOutput.appendln("Пустая коллекция");
        }
        return false;
    }
}
