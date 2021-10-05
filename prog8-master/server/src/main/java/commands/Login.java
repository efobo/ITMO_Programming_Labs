package commands;

import Communication.User;
import Database.DatabaseUserHandler;
import Exceptions.DatabaseHandlingException;
import Exceptions.UserIsNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import managers.ResponseOutput;

public class Login extends ACommand {

        private DatabaseUserHandler databaseUserHandler;

        public Login(DatabaseUserHandler databaseUserHandler) {
            super("login",  "", "вход пользователя (внутренняя команда)");
            this.databaseUserHandler = databaseUserHandler;
        }

        /**
         * Executes the command.
         *
         * @return Command exit status.
         */
        @Override
        public boolean execute(String strArg, Object objArg, User user) {
            try {
                if (!strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
                if (!databaseUserHandler.checkUserByUsernameAndPassword(user)) throw new UserIsNotFoundException();
                ResponseOutput.appendln("UserAuthorized");
                ResponseOutput.appendargs(user.getUsername());
                return true;
            } catch (WrongAmountOfElementsException e) {
                ResponseOutput.appendln("Using");
                ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
            } catch (ClassCastException e) {
                ResponseOutput.appenderror("ClientObjectException");
            } catch (DatabaseHandlingException e) {
                ResponseOutput.appenderror("DatabaseHandlingException");
            } catch (UserIsNotFoundException e) {
                ResponseOutput.appenderror("InvalidUserException");
            }
            return false;
        }
    }
