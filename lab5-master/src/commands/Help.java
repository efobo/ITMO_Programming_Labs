package commands;

import Exceptions.WrongAmountOfElementsException;

/**
 * Класс команды help
 */
public class Help extends ACommand{
    public Help() {
        super("help", "вывести справку по доступным командам");
    }

    /**
     * @param argument
     * @return command exit status
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException e) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        }
        return false;
    }
}

