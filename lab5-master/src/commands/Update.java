package commands;

import Exceptions.EmptyCollectionException;
import Exceptions.IncorrectScriptException;
import Exceptions.LabworkNotFoundException;
import Exceptions.WrongAmountOfElementsException;
import app.*;
import data.Coordinates;
import data.Labwork;

import java.time.LocalDateTime;

/**
 * Класс команды update
 */
public class Update extends ACommand {
    private CollectionManager collectionManager;
    private Asker asker;

    public Update(CollectionManager collectionManager, Asker asker) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
        this.asker = asker;
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
            Long id = Long.parseLong(argument);
            Labwork lb = collectionManager.getById(id);
            if (lb == null) throw new LabworkNotFoundException();
            lb.setName(asker.askName());
            lb.setCoordinates(new Coordinates(asker.askXc(), asker.askYc()));
            lb.setCreationDate(LocalDateTime.now());
            lb.setMinimalPoint(asker.askMinimalPoint());
            lb.setDifficulty(asker.askDifficulty());
            lb.setAuthor(asker.askPerson());
            System.out.println("Элемент обновлен!");
            return true;
        } catch (WrongAmountOfElementsException e) {
            System.out.println("Использование: '" + getName() + "'");
            System.out.println("Неверно введена команда, необходим аргумент - id.");
        } catch (EmptyCollectionException e) {
            System.out.println("Пустая коллекция");
        } catch (LabworkNotFoundException e) {
            System.out.println("Элемента с таким id нет");
        } catch (IncorrectScriptException e) {
            System.out.println("Неправильно написан скрипт");
        } catch (NumberFormatException e) {
            System.out.println("Аргумент должен быть числом");
        }
        return false;
    }
}

