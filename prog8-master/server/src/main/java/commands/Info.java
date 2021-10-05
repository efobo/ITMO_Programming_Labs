package commands;

import Communication.User;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;

import java.time.LocalDateTime;

/**
 * Класс команды info
 */
public class  Info extends ACommand{
    private CollectionManager collectionManager;


    public Info(CollectionManager collectionManager) {
        super("info", "", "вывести информацию о коллекции");
        this.collectionManager = collectionManager;
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
            LocalDateTime lastInitTime = collectionManager.getInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                    lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

            ResponseOutput.appendln("Collection Info\n" + "Collection Type: "
                    + collectionManager.collectionType() +
                    "\nSize: " +
                    String.valueOf(collectionManager.collectionSize()) + "\nLast Init Time: " + lastInitTimeString);
            ResponseOutput.appendargs(
                    collectionManager.collectionType(),
                    String.valueOf(collectionManager.collectionSize()),
                    lastInitTimeString);
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Using");
            ResponseOutput.appendargs(getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
