package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.LabworkNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import app.CollectionManager;
import data.Labwork;


/**
 * Класс команды remove_by_id
 */
public class RemoveByID extends ACommand{
    private CollectionManager collectionManager;

    public RemoveByID(CollectionManager collectionManager) {
        super("remove_by_id id", "удалить элемент из коллекции по его id");
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
            long id = Long.parseLong(argument);
            Labwork removeLB = collectionManager.getById(id);
            if (removeLB == null) throw new LabworkNotFoundException();
            collectionManager.remove(removeLB);
            System.out.println("Удалено!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, необходим аргумент - id.");
        } catch (EmptyCollectionException e) {
            System.out.println("Пустая коллекция");
        }catch (LabworkNotFoundException e) {
            System.out.println("Элемента с таким id нет");
        } catch (NumberFormatException e) {
            System.out.println("Аргумент должен быть числом");

        }
        return false;
    }
}
