package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.WrongAmountOfElementsException;
import app.CollectionManager;
import data.Labwork;

/**
 * Класс команды filter_contains_name
 */
public class FilterContainsName extends ACommand{
    private CollectionManager collectionManager;

    public FilterContainsName(CollectionManager collectionManager) {
        super ("filter_contains_name name ","вывести элементы, значение поля name которых содержит заданную подстроку");
        this.collectionManager = collectionManager;
    }

    /**
     * @param argument
     * @return command exit status
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            String name = argument;
            boolean b = true;
            for (Labwork lb : collectionManager.getCollection()) {
                if (lb.getName().equals(name)) {
                    System.out.println(lb.toString());
                    b = false;
                }
            }
            if (b) {
                System.out.println("Элемента с таким именем не найдено");
            }
            return true;
        } catch (WrongAmountOfElementsException exception) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, необходим аргумент - имя.");
        } catch (EmptyCollectionException e) {
            System.out.println("Пустая коллекция");
        }
        return false;
    }
}
