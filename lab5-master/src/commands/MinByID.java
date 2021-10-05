package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.LabworkNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import app.CollectionManager;
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
     * @param argument
     * @return command exit status
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            Labwork lb = collectionManager.chooseElementWithLowestID();
            if (lb == null) throw new LabworkNotFoundException();
            System.out.println(lb.toString());
            return true;
        } catch (WrongAmountOfElementsException e) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, аргумент не нужен.");
        } catch (EmptyCollectionException e) {
            System.out.println("Пустая коллекция");
        } catch (LabworkNotFoundException e) {
            System.out.println("Элемент с минимальным id не найден");
        }
        return false;
    }
}
