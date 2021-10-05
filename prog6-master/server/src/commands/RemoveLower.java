package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.LabworkNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import data.DemoLabwork;
import data.Labwork;
import managers.CollectionManager;
import managers.ResponseOutput;

import java.time.LocalDateTime;

/**
 * Класс команды remove_lower
 */
public class RemoveLower extends ACommand {
    private CollectionManager collectionManager;

    public RemoveLower (CollectionManager collectionManager) {
        super ("remove_lower {element}", "удалить из коллекции все элементы, меньшие, чем заданный");
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
            collectionManager.removeLower(lbFromCollection);
            ResponseOutput.appendln("Элементы успешно удалены");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент нужен.");
        } catch (EmptyCollectionException e) {
            System.out.println("Пустая коллекция");
        } catch (LabworkNotFoundException e) {
            System.out.println("Такого элемента в коллекции нет");
        }
        return false;
    }
}
