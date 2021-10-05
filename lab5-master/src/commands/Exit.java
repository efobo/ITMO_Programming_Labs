package commands;

import Exceptions.WrongAmountOfElementsException;

/**
 * класс команды exit
 */
public class Exit extends ACommand{

    public Exit () {super("exit", "завершить программу (без сохранения в файл)");}

    /**
     * @param argument
     * @return command exit status
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        }
        return false;
    }
}
