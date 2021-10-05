package commands;

import Communication.User;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;

public class ServerExit extends ACommand{
    private CollectionManager collectionManager;

    public ServerExit (CollectionManager collectionManager) {
        super ("server_exit", "","Завершить программу");
        this.collectionManager = collectionManager;
        //this.fileRecorder = fileRecorder;
    }
    @Override
    public boolean execute(String strArg, Object objArg, User user) {
        try {
            if (!strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            //collectionManager.saveCollection(fileRecorder);
            ResponseOutput.appendln("Работа сервера успешно завершена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент не нужен.");
        }
        return false;
    }
}
