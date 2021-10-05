package commands;

import Communication.User;
import Database.DatabaseUserHandler;
import Exceptions.DatabaseHandlingException;
import Exceptions.UserIsAlreadyExists;
import Exceptions.WrongAmountOfElementsException;
import managers.ResponseOutput;

public class Register extends ACommand{
    private DatabaseUserHandler databaseUserHandler;

    public Register(DatabaseUserHandler databaseUserHandler) {
        super("register", "", "зарегистрировать пользователя (внутренняя команда)");
        this.databaseUserHandler = databaseUserHandler;
    }

    @Override
    public boolean execute(String strArg, Object objArg, User user) {
        try {
            if (!strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            if (!databaseUserHandler.insertUser(user)) throw new UserIsAlreadyExists();
            ResponseOutput.appendln("UserRegistered");
            ResponseOutput.appendargs(user.getUsername());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutput.appenderror("ClientObjectException");
        } catch (DatabaseHandlingException exception) {
            ResponseOutput.appenderror("DatabaseHandlingException");
        } catch (UserIsAlreadyExists exception) {
            ResponseOutput.appendln("UserExistsException");
            ResponseOutput.appendargs(user.getUsername());
        }
        return false;
    }
}
