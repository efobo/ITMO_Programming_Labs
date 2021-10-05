package managers;

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
        ResponseOutput.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
        return false;
    }


    /**
     * Команда add
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean add(String strArg, Object objArg) {
        return add.execute(strArg, objArg);
    }

    /**
     * Команда add_if_min
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean addIfMin (String strArg, Object objArg) {
        return addIfMin.execute(strArg, objArg);
    }

    /**
     * Команда clear
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean clear (String strArg, Object objArg) {
        return clear.execute(strArg, objArg);
    }

    /**
     * Команда execute_script
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean executeScript (String strArg, Object objArg) {
        return executeScript.execute(strArg, objArg);
    }

    /**
     * Команда filter_contains_name
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean filterContainsName (String strArg, Object objArg) {
        return filterContainsName.execute(strArg, objArg);
    }

    /**
     * Комнада help
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean help (String strArg, Object objArg) {
        if(help.execute(strArg, objArg)) {
            for (ICommand comm : commands) {
                ResponseOutput.appendln(comm.getName() + " : " + comm.getDescription());
            }
            return true;

        } else return false;
    }

    /**
     * Команда info
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean info (String strArg, Object objArg) {
        return info.execute(strArg, objArg);
    }

    /**
     * Команда min_by_id
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean minByID (String strArg, Object objArg) {
        return minByID.execute(strArg, objArg);
    }

    /**
     * Команда print_descending
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean printDescending (String strArg, Object objArg) {
        return printDescending.execute(strArg, objArg);
    }

    /**
     * Команда remove_by_id
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean removeByID (String strArg, Object objArg) {
        return  removeByID.execute(strArg, objArg);
    }

    /**
     * Команда remove_greater
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean removeGreater (String strArg, Object objArg) {
        return removeGreater.execute(strArg, objArg);
    }

    /**
     * Команда remove_lower
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean removeLower (String strArg, Object objArg) {
        return removeLower.execute(strArg, objArg);
    }

    /**
     * Команда save
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean save (String strArg, Object objArg) {
        return save.execute(strArg, objArg);
    }

    /**
     * Команда show
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean show (String strArg, Object objArg) {
        return show.execute(strArg, objArg);
    }

    /**
     * Команда update
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean update (String strArg, Object objArg) {
        return update.execute(strArg, objArg);
    }

    /**
     * Команда exit
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean exit (String strArg, Object objArg) {
        return exit.execute(strArg, objArg);
    }

   // public boolean serverExit (String strArg, Object objArg) {return serverExit.execute(strArg, objArg);}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandManager)) return false;
        CommandManager comm = (CommandManager) o;
        return Objects.equals(collectionManager, comm.collectionManager);
    }


}
