package commands;

import Communication.User;
import Exceptions.WrongAmountOfElementsException;
import managers.ResponseOutput;

/**
 * класс команды execute_script
 */
public class ExecuteScript extends ACommand{

    public ExecuteScript () {super("execute_script", "<file_name>", "исполнить скрипт из указанного файла");}

    /**
     *
     * @param strArg
     * @param objArg
     * @return
     */
    @Override
    public boolean execute(String strArg, Object objArg, User user) {
        try {
            if (strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
