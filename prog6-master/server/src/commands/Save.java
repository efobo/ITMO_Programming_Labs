package commands;

import Exceptions.WrongAmountOfElementsException;
import app.Main;
import managers.CollectionFileRecorder;
import managers.CollectionManager;
import managers.ResponseOutput;

import java.util.logging.Level;

/**
 * Класс команды save
 */
public class Save extends ACommand{
private CollectionManager collectionManager;
private CollectionFileRecorder fileRecorder;

    public Save (CollectionManager collectionManager, CollectionFileRecorder fileRecorder) {
        super("save", "сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
        this.fileRecorder = fileRecorder;
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
            collectionManager.saveCollection(fileRecorder);
            Main.logger.log(Level.INFO, "Коллекция успешно сохранена");
            System.out.println("Коллекция успешно сохранена");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда: String аргумент не нужен, Object аргумент не нужен.");
        }
        return false;
    }
}
