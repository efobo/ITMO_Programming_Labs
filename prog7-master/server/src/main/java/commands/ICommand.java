package commands;

import Communication.User;

/**
 * Интерфейс для всех команд
 */
public interface ICommand {
    String getDescription();
    String getName();
    boolean execute(String strArg, Object objArg, User user);
}
