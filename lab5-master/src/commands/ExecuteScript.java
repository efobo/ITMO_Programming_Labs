package commands;

import Exceptions.WrongAmountOfElementsException;

/**
 * класс команды execute_script
 */
public class ExecuteScript extends ACommand{

    public ExecuteScript () {super("execute_script file_name", "исполнить скрипт из указанного файла");}

    /**
     * @param argument
     * @return command exit status
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongAmountOfElementsException();
            System.out.println("Выполняю скрипт '" + argument + "'...");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, необходим аргумент - имя файла.");
        }
        return false;
    }
}
