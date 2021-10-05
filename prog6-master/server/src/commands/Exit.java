package commands;

import Exceptions.WrongAmountOfElementsException;
import managers.CollectionFileRecorder;
import managers.CollectionManager;
import managers.ResponseOutput;

/**
 * класс команды exit
 */
public class Exit extends ACommand{
    private CollectionManager collectionManager;
    private CollectionFileRecorder fileRecorder;

    public Exit (CollectionManager collectionManager, CollectionFileRecorder fileRecorder) {
        super("exit", "завершить работу клиента");
        this.collectionManager = collectionManager;
        this.fileRecorder = fileRecorder;
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
            collectionManager.saveCollection(fileRecorder);
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент не нужен.");
        }
        return false;
    }
}
