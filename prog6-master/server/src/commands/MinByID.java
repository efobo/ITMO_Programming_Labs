package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.LabworkNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;
import data.Labwork;

/**
 * Класс команды min_by_id
 */
public class MinByID extends ACommand{
    private CollectionManager collectionManager;

    public MinByID (CollectionManager collectionManager) {
        super ("min_by_id","вывести любой объект из коллекции, значение поля id которого является минимальным");
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
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            Labwork lb = collectionManager.chooseElementWithLowestID();
            if (lb == null) throw new LabworkNotFoundException();
            ResponseOutput.appendln(lb.toString());
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент не нужен.");
        } catch (EmptyCollectionException e) {
            ResponseOutput.appendln("Пустая коллекция");
        } catch (LabworkNotFoundException e) {
            ResponseOutput.appendln("Элемент с минимальным id не найден");
        }
        return false;
    }
}
