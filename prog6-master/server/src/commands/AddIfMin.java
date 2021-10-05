package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import data.DemoLabwork;
import data.Labwork;
import managers.ResponseOutput;

import java.time.LocalDateTime;

/**
 * Класс команды add_if_min
 */
public class AddIfMin extends ACommand{
    private CollectionManager collectionManager;

    public AddIfMin (CollectionManager collectionManager) {
        super ("add_if_min {element}", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
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
            if (!strArg.isEmpty() || objArg == null) throw new WrongAmountOfElementsException();
            DemoLabwork demoLabwork = (DemoLabwork) objArg;
            Labwork lbToCompare = new Labwork(collectionManager.generateID(),
                    demoLabwork.getName(),
                    demoLabwork.getCoordinates(),
                    LocalDateTime.now(),
                    demoLabwork.getMinimalPoint(),
                    demoLabwork.getDifficulty(),
                    demoLabwork.getAuthor());

            if (collectionManager.collectionSize() == 0 || lbToCompare.compareTo(collectionManager.getFirstElement()) < 0) {
                collectionManager.addToCollection(lbToCompare);
                ResponseOutput.appendln("Элемент успешно добавлен!" + lbToCompare.compareTo(collectionManager.getFirstElement()));
                return true;
            } else ResponseOutput.appendln("Элемент не является минимальным для этой коллекции, поэтому добавлен не был.");
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент нужен");
        }
        return false;
    }
}


