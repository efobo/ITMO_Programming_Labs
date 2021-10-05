package managers;

import Communication.User;
import commands.ICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    //private ICommand help;
    private ICommand info;
    //private ICommand minByID;
    //private ICommand printDescending;
    private ICommand removeByID;
    private ICommand removeGreater;
    private ICommand removeLower;
    //private ICommand show;
    private ICommand update;
    private ICommand login;
    private ICommand register;
    private ICommand refresh;
    private ICommand exit;

    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();

    public CommandManager (ICommand add, ICommand addIfMin, ICommand clear, ICommand executeScript,
                           ICommand info,
                           ICommand removeByID, ICommand removeGreater,
                           ICommand removeLower,
                           ICommand login, ICommand register, ICommand update, ICommand refresh, ICommand exit) {

        this.add = add;
        this.addIfMin = addIfMin;
        this.clear = clear;
        this.executeScript = executeScript;
        //this.help = help;
        this.info = info;
        //this.minByID = minByID;
        //this.printDescending = printDescending;
        this.removeByID = removeByID;
        this.removeGreater = removeGreater;
        this.removeLower = removeLower;
        //this.show = show;
        this.update = update;
        this.login = login;
        this.register = register;
        this.refresh = refresh;
        this.exit = exit;

        commands.add(add);
        commands.add(addIfMin);
        commands.add(clear);
        commands.add(executeScript);
        //commands.add(help);
        commands.add(info);
        //commands.add(minByID);
        //commands.add(printDescending);
        commands.add(removeByID);
        commands.add(removeGreater);
        commands.add(removeLower);
        //commands.add(show);
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
        ResponseOutput.appenderror("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
        return false;
    }


    /**
     * Команда add
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean add(String strArg, Object objArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return add.execute(strArg, objArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Команда add_if_min
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean addIfMin (String strArg, Object objArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return addIfMin.execute(strArg, objArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Команда clear
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean clear (String strArg, Object objArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return clear.execute(strArg, objArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Команда execute_script
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean executeScript (String strArg, Object objArg, User user) {
        return executeScript.execute(strArg, objArg, user);
    }


    /**
     * Комнада help
     * @param strArg
     * @param objArg
     * @return
     */
    //public boolean help (String strArg, Object objArg, User user) {
      //  if(help.execute(strArg, objArg, user)) {
        //    for (ICommand comm : commands) {
          //      ResponseOutput.appendln(comm.getName() + " : " + comm.getDescription());
            //}
            //return true;

    //    } else return false;
    //}

    /**
     * Команда info
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean info (String strArg, Object objArg, User user) {
        collectionLocker.readLock().lock();
        try {
            return info.execute(strArg, objArg, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    /**
     * Команда min_by_id
     * @param strArg
     * @param objArg
     * @return
     */
    //public boolean minByID (String strArg, Object objArg, User user) {
      //  collectionLocker.readLock().lock();
        //try {
          //  return minByID.execute(strArg, objArg, user);
        //} finally {
          //  collectionLocker.readLock().unlock();
        //}
    //}

    /**
     * Команда print_descending
     * @param strArg
     * @param objArg
     * @return
     */
    //public boolean printDescending (String strArg, Object objArg, User user) {
      //  collectionLocker.readLock().lock();
        //try {
          //  return printDescending.execute(strArg, objArg, user);
        //} finally {
          //  collectionLocker.readLock().unlock();
        //}
    //}

    /**
     * Команда remove_by_id
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean removeByID (String strArg, Object objArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return removeByID.execute(strArg, objArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Команда remove_greater
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean removeGreater (String strArg, Object objArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return removeGreater.execute(strArg, objArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Команда remove_lower
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean removeLower (String strArg, Object objArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return removeLower.execute(strArg, objArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }


    /**
     * Команда show
     * @param strArg
     * @param objArg
     * @return
     */
    //public boolean show (String strArg, Object objArg, User user) {
      //  collectionLocker.readLock().lock();
        //try {
          //  return show.execute(strArg, objArg, user);
        //} finally {
          //  collectionLocker.readLock().unlock();
        //}
    //}

    /**
     * Команда update
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean update (String strArg, Object objArg, User user) {
        collectionLocker.writeLock().lock();
        try {
            return update.execute(strArg, objArg, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Команда login
     * @param strArg
     * @param objArg
     * @param user
     * @return
     */
    public boolean login (String strArg, Object objArg, User user) {return login.execute(strArg, objArg, user);}

    /**
     * Команда register
     * @param strArg
     * @param objArg
     * @param user
     * @return
     */
    public boolean register (String strArg, Object objArg, User user) {return register.execute(strArg, objArg, user);}

    /**
     * Команда exit
     * @param strArg
     * @param objArg
     * @return
     */
    public boolean exit (String strArg, Object objArg, User user) {
        return exit.execute(strArg, objArg, user);
    }

   // public boolean serverExit (String strArg, Object objArg, User user) {return serverExit.execute(strArg, objArg, user);}

    public boolean refresh(String stringArgument, Object objectArgument, User user) {
        return refresh.execute(stringArgument, objectArgument, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandManager)) return false;
        CommandManager comm = (CommandManager) o;
        return Objects.equals(collectionManager, comm.collectionManager);
    }


}
