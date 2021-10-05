package commands;

import Exceptions.IncorrectScriptException;
import Exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import managers.ResponseOutput;
import data.DemoLabwork;
import data.Labwork;

import java.time.LocalDateTime;

/**
 * Класс команды Add
 */
public class Add extends ACommand{
    private CollectionManager collectionManager;

    public Add (CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
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
            Labwork lb = new Labwork(collectionManager.generateID(),
                    demoLabwork.getName(),
                    demoLabwork.getCoordinates(),
                    LocalDateTime.now(),
                    demoLabwork.getMinimalPoint(),
                    demoLabwork.getDifficulty(),
                    demoLabwork.getAuthor());
        collectionManager.addToCollection(lb);
        ResponseOutput.appendln("Новый элемент добавлен в коллекцию!");
        return true;
        } catch (WrongAmountOfElementsException e) {
            ResponseOutput.appendln("Использование: '" + getName() + "'");
            ResponseOutput.appendln("Неверно введена команда: String аргумент не нужен, Object аргумент нужен");
        }
        return false;
    }
}
