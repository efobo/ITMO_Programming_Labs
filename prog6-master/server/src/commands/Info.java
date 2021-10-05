package commands;

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
        super("info", "вывести информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     *
     * @param strArg
     * @param objArg
     * @return
     */
    @Override
    public boolean execute(String strArg, Object objArg) {
        try {
            if (!strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            LocalDateTime lastInitTime = collectionManager.getInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                    lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

            LocalDateTime lastSaveTime = collectionManager.getSaveTime();
            String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
                    lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

            ResponseOutput.appendln("Сведения о коллекции:");
            ResponseOutput.appendln(" Тип: " + collectionManager.collectionType());
            ResponseOutput.appendln(" Количество элементов: " + collectionManager.collectionSize());
            ResponseOutput.appendln(" Дата последнего сохранения: " + lastSaveTimeString);
            ResponseOutput.appendln(" Дата последней инициализации: " + lastInitTimeString);
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент не нужен.");
        }
        return false;
    }
}
