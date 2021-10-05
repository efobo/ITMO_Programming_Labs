package commands;

import Exceptions.WrongAmountOfElementsException;
import managers.ResponseOutput;

/**
 * класс команды execute_script
 */
public class ExecuteScript extends ACommand{

    public ExecuteScript () {super("execute_script file_name", "исполнить скрипт из указанного файла");}

    /**
     *
     * @param strArg
     * @param objArg
     * @return
     */
    @Override
    public boolean execute(String strArg, Object objArg) {
        try {
            if (strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: необходим String аргумент - имя файла, Object аргумент не нужен.");
        }
        return false;
    }
}
