package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.LabworkNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import data.DemoLabwork;
import data.Labwork;
import managers.ResponseOutput;

import java.time.LocalDateTime;

/**
 * Класс команды remove_greater
 */
public class RemoveGreater extends ACommand {
    private CollectionManager collectionManager;


    public RemoveGreater (CollectionManager collectionManager) {
        super ("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
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
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            DemoLabwork demoLabwork = (DemoLabwork) objArg;
            Labwork lbToCompare = new Labwork(collectionManager.generateID(),
                    demoLabwork.getName(),
                    demoLabwork.getCoordinates(),
                    LocalDateTime.now(),
                    demoLabwork.getMinimalPoint(),
                    demoLabwork.getDifficulty(),
                    demoLabwork.getAuthor());
            Labwork lbFromCollection = collectionManager.getByValue(lbToCompare);
            if (lbFromCollection == null ) throw new LabworkNotFoundException();
            collectionManager.removeGreater(lbFromCollection);
            ResponseOutput.appendln("Элементы успешно удалены");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент нужен.");
        } catch (EmptyCollectionException e) {
            ResponseOutput.appendln("Пустая коллекция");
        } catch (LabworkNotFoundException e) {
            ResponseOutput.appendln("Такого элемента в коллекции нет");
        }
        return false;
    }
}
