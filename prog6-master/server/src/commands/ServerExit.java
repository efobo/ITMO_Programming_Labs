package commands;

import Exceptions.WrongAmountOfElementsException;
import managers.CollectionFileRecorder;
import managers.CollectionManager;
import managers.ResponseOutput;

public class ServerExit extends ACommand{
    private CollectionManager collectionManager;
    private CollectionFileRecorder fileRecorder;

    public ServerExit (CollectionManager collectionManager, CollectionFileRecorder fileRecorder) {
        super ("server_exit", "Завершить программу");
        this.collectionManager = collectionManager;
        this.fileRecorder = fileRecorder;
    }
    @Override
    public boolean execute(String strArg, Object objArg) {
        try {
            if (!strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            collectionManager.saveCollection(fileRecorder);
            ResponseOutput.appendln("Работа сервера успешно завершена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент не нужен.");
        }
        return false;
    }
}
