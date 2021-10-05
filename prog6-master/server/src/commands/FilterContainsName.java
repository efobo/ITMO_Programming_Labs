package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;
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
     *
     * @param strArg
     * @param objArg
     * @return
     */
    @Override
    public boolean execute(String strArg, Object objArg) {
        try {
            if (strArg.isEmpty() || objArg != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new EmptyCollectionException();
            String name = strArg;
            boolean b = true;
            for (Labwork lb : collectionManager.getCollection()) {
                if (lb.getName().equals(name)) {
                    ResponseOutput.appendln(lb.toString());
                    b = false;
                }
            }
            if (b) {
                ResponseOutput.appendln("Элемента с таким именем не найдено");
            }
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда, необходим String аргумент - имя, Object аргумент не нужен.");
        } catch (EmptyCollectionException e) {
            ResponseOutput.appendln("Пустая коллекция");
        }
        return false;
    }
}
