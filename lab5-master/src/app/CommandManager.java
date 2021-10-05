package app;

import commands.ICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс для обработки всех команд
 */
public class CommandManager {
    private CollectionManager collectionManager;
    private List<ICommand> commands = new ArrayList<>();
    private ICommand add;
    private ICommand addIfMin;
    private ICommand clear;
    private ICommand executeScript;
    private ICommand filterContainsName;
    private ICommand help;
    private ICommand info;
    private ICommand minByID;
    private ICommand printDescending;
    private ICommand removeByID;
    private ICommand removeGreater;
    private ICommand removeLower;
    private ICommand save;
    private ICommand show;
    private ICommand update;
    private ICommand exit;

    public CommandManager (ICommand add, ICommand addIfMin, ICommand clear, ICommand executeScript,
                           ICommand filterContainsName, ICommand help, ICommand info, ICommand minByID,
                           ICommand printDescending, ICommand removeByID, ICommand removeGreater,
                           ICommand removeLower, ICommand save, ICommand show, ICommand update, ICommand exit) {

        this.add = add;
        this.addIfMin = addIfMin;
        this.clear = clear;
        this.executeScript = executeScript;
        this.filterContainsName = filterContainsName;
        this.help = help;
        this.info = info;
        this.minByID = minByID;
        this.printDescending = printDescending;
        this.removeByID = removeByID;
        this.removeGreater = removeGreater;
        this.removeLower = removeLower;
        this.save = save;
        this.show = show;
        this.update = update;
        this.exit = exit;

        commands.add(add);
        commands.add(addIfMin);
        commands.add(clear);
        commands.add(executeScript);
        commands.add(filterContainsName);
        commands.add(help);
        commands.add(info);
        commands.add(minByID);
        commands.add(printDescending);
        commands.add(removeByID);
        commands.add(removeGreater);
        commands.add(removeLower);
        commands.add(save);
        commands.add(show);
        commands.add(update);
        commands.add(exit);

    }

    /**
     * Возвращает список всех команд
     * @return list of commands
     */
    public List<ICommand> getCommands () {return commands;}

    /**
     * Говорит, существует ли указанная команда
     * @param command
     * @return boolean
     */
    public boolean noSuchCommand(String command) {
        System.out.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
        return false;
    }


    /**
     * Команда add
     * @param argument
     * @return
     */
    public boolean add(String argument) {
        return add.execute(argument);
    }

    /**
     * Команда add_if_min
     * @param argument
     * @return
     */
    public boolean addIfMin (String argument) {
        return addIfMin.execute(argument);
    }

    /**
     * Команда clear
     * @param argument
     * @return
     */
    public boolean clear (String argument) {
        return clear.execute(argument);
    }

    /**
     * Команда execute_script
     * @param argument
     * @return
     */
    public boolean executeScript (String argument) {
        return executeScript.execute(argument);
    }

    /**
     * Команда filter_contains_name
     * @param argument
     * @return
     */
    public boolean filterContainsName (String argument) {
        return filterContainsName.execute(argument);
    }

    /**
     * Комнада help
     * @param argument
     * @return
     */
    public boolean help (String argument) {
        if(help.execute(argument)) {
            for (ICommand comm : commands) {
                System.out.println(comm.getName() + " : " + comm.getDescription());
            }
            return true;

        } else return false;
    }

    /**
     * Команда info
     * @param argument
     * @return
     */
    public boolean info (String argument) {
        return info.execute(argument);
    }

    /**
     * Команда min_by_id
     * @param argument
     * @return
     */
    public boolean minByID (String argument) {
        return minByID.execute(argument);
    }

    /**
     * Команда print_descending
     * @param argument
     * @return
     */
    public boolean printDescending (String argument) {
        return printDescending.execute(argument);
    }

    /**
     * Команда remove_by_id
     * @param argument
     * @return
     */
    public boolean removeByID (String argument) {
        return  removeByID.execute(argument);
    }

    /**
     * Команда remove_greater
     * @param argument
     * @return
     */
    public boolean removeGreater (String argument) {
        return removeGreater.execute(argument);
    }

    /**
     * Команда remove_lower
     * @param argument
     * @return
     */
    public boolean removeLower (String argument) {
        return removeLower.execute(argument);
    }

    /**
     * Команда save
     * @param argument
     * @return
     */
    public boolean save (String argument) {
        return save.execute(argument);
    }

    /**
     * Команда show
     * @param argument
     * @return
     */
    public boolean show (String argument) {
        return show.execute(argument);
    }

    /**
     * Команда update
     * @param argument
     * @return
     */
    public boolean update (String argument) {
        return update.execute(argument);
    }

    /**
     * Команда exit
     * @param argument
     * @return
     */
    public boolean exit (String argument) {
        return exit.execute(argument);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandManager)) return false;
        CommandManager comm = (CommandManager) o;
        return Objects.equals(collectionManager, comm.collectionManager);
    }


}
