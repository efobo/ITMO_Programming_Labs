package commands;

import Communication.User;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;

/**
 * класс команды exit
 */
public class Exit extends ACommand{
    private CollectionManager collectionManager;
    //private CollectionFileRecorder fileRecorder;

    public Exit (CollectionManager collectionManager) {
        super("exit", "","завершить работу клиента");
        this.collectionManager = collectionManager;
        //this.fileRecorder = fileRecorder;
    }

    /**
     *
     * @param strArg
     * @param objArg
     * @return
     */
    @Override
    public boolean execute(String strArg, Object objArg, User user) {
        try {
            if (!strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            //collectionManager.saveCollection(fileRecorder);
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
