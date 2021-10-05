package commands;

import java.time.LocalDateTime;

import Exceptions.WrongAmountOfElementsException;
import app.CollectionManager;

/**
 * Класс команды info
 */
public class Info extends ACommand{
    private CollectionManager collectionManager;


    public Info(CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * @param argument
     * @return command exit status
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            LocalDateTime lastInitTime = collectionManager.getInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                    lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

            LocalDateTime lastSaveTime = collectionManager.getSaveTime();
            String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
                    lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

            System.out.println("Сведения о коллекции:");
            System.out.println(" Тип: " + collectionManager.collectionType());
            System.out.println(" Количество элементов: " + collectionManager.collectionSize());
            System.out.println(" Дата последнего сохранения: " + lastSaveTimeString);
            System.out.println(" Дата последней инициализации: " + lastInitTimeString);
            return true;
        } catch (WrongAmountOfElementsException e) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        }
        return false;
    }
}
