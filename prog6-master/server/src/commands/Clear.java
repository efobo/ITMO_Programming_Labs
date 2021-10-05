package commands;

import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;

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
     *
     * @param strArg
     * @param objArg
     * @return
     */
    @Override
    public boolean execute(String strArg, Object objArg) {
        try {
            if (!strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            collectionManager.clearCollection();
            ResponseOutput.appendln("Коллекция очищена!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент не нужен.");
        }
        return false;
    }
}

