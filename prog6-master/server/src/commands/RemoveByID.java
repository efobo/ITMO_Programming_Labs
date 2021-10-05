package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.LabworkNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;
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
            long id = Long.parseLong(strArg);
            Labwork removeLB = collectionManager.getById(id);
            if (removeLB == null) throw new LabworkNotFoundException();
            collectionManager.remove(removeLB);
            ResponseOutput.appendln("Удалено!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: необходим String аргумент - id, Object аргумент не нужен.");
        } catch (EmptyCollectionException e) {
            ResponseOutput.appendln("Пустая коллекция");
        }catch (LabworkNotFoundException e) {
            ResponseOutput.appendln("Элемента с таким id нет");
        } catch (NumberFormatException e) {
            ResponseOutput.appendln("Аргумент должен быть числом");

        }
        return false;
    }
}
