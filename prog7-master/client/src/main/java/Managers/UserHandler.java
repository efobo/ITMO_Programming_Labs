package Managers;

import Communication.Request;
import Communication.ResponseCode;
import Communication.User;
import Exceptions.IncorrectScriptException;
import Exceptions.ScriptRecursionException;
import Exceptions.UsingCommandException;
import data.Coordinates;
import data.DemoLabwork;
import data.Difficulty;
import data.Person;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Stack;

public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private BufferedReader userReader;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<BufferedReader> readerStack = new Stack<>();

    public UserHandler (BufferedReader userReader) {
        this.userReader = userReader;
    }


    public Request handle (ResponseCode serverResponseCode, User user) {
        String userInput;
        String[] userCommand = {"", ""};
        ProcessCode processCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.ERROR ||
                            serverResponseCode == ResponseCode.CLIENT_EXIT)) throw new IncorrectScriptException();
                    System.out.print("$ ");
                    userInput = userReader.readLine();
                    while (fileMode() & userInput == null) {
                        userReader.close();
                        userReader = readerStack.pop();
                        //System.out.println("Возвращаюсь к скрипту '" + scriptStack.pop().getName() + "'...");
                        scriptStack.pop();
                        System.out.print("$ ");
                        userInput = userReader.readLine();
                    }
                    if (fileMode()) {
                        if (!userInput.isEmpty()) {
                            System.out.println(userInput);
                        }
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (IOException e) {
                    System.out.println("Ошибка ввода");
                } catch (NoSuchElementException | IllegalStateException e) {
                    System.out.println("Ошибка при вводе команды");
                    userCommand = new String[] {"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        System.out.println("Превышено количество попыток ввода");
                        System.exit(0);
                    }
                }
                processCode = processCommands(userCommand[0], userCommand[1]);
            } while (processCode == ProcessCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                if (fileMode() && (processCode == ProcessCode.ERROR || serverResponseCode == ResponseCode.ERROR))
                    throw new IncorrectScriptException();
                switch (processCode) {
                    case OBJECT:
                        DemoLabwork demoLabworkToAdd = makeLabworkToAdd();
                        return new Request(userCommand[0], userCommand[1], demoLabworkToAdd, user);
                    case UPDATE_OBJECT:
                        DemoLabwork demoLabworkToUpdate = makeLabworkToUpdate();
                        return new Request(userCommand[0], userCommand[1], demoLabworkToUpdate, user);
                    case SCRIPT:
                        fileMode();
                        File scriptFile = new File (userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        readerStack.push(userReader);
                        scriptStack.push(scriptFile);
                        userReader = new BufferedReader(new InputStreamReader(new FileInputStream(scriptFile)));
                        System.out.println("Выполняю скрипт '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (FileNotFoundException e) {
                System.out.println("Ошибка! Файл со скриптом не найден.");
            } catch (ScriptRecursionException e) {
                System.out.println("Скрипты не могут вызываться рекурсивно!");
            }
        } catch (IncorrectScriptException e) {
            System.out.println("Выполнение скрипта прервано");
            try {
                while (!readerStack.isEmpty()) {
                    userReader.close();
                    userReader = readerStack.pop();
                }
                scriptStack.clear();
                return new Request();
            } catch (IOException ex) {
                System.out.println("Ошибка ввода");
            }

        }
        return new Request(userCommand[0], userCommand[1]);
    }

    private DemoLabwork makeLabworkToAdd () throws IncorrectScriptException {
        Asker asker = new Asker(userReader);
        if (fileMode()) asker.setFileMode();
        return new DemoLabwork(asker.askName(),
                new Coordinates(asker.askXc(), asker.askYc()),
                asker.askMinimalPoint(),
                asker.askDifficulty(),
                asker.askPerson());
    }

    private DemoLabwork makeLabworkToUpdate () throws IncorrectScriptException {
        Asker asker = new Asker(userReader);
        if (fileMode()) asker.setFileMode();
        String name = asker.askQuestion("Хотите изменить имя?") ? asker.askName() : null;
        Coordinates coordinates = asker.askQuestion("Хотите изменить координаты?") ? new Coordinates(asker.askXc(), asker.askYc()) : null;
        double minimalPoint = asker.askQuestion("Хотите изменить минимальные очки?") ? asker.askMinimalPoint() : -1;
        Difficulty difficulty = asker.askQuestion("Хотите изменить сложность?") ? asker.askDifficulty() : null;
        Person person = asker.askQuestion("Хотите изменить автора?") ? asker.askPerson() : null;

        return new DemoLabwork(name, coordinates, minimalPoint, difficulty, person);
    }

    private ProcessCode processCommands (String command, String argument) {
        try {
            switch (command) {
                case "":
                    return ProcessCode.ERROR;
                case "help":
                    if (!argument.isEmpty()) throw new UsingCommandException();
                    break;
                case "info":
                    if (!argument.isEmpty()) throw new UsingCommandException();
                    break;
                case "show":
                    if (!argument.isEmpty()) throw new UsingCommandException();
                    break;
                case "clear":
                    if (!argument.isEmpty()) throw new UsingCommandException();
                    break;
                //case "save":
                    //if (!argument.isEmpty()) throw new UsingCommandException();
                    //break;
                case "exit":
                    if (!argument.isEmpty()) throw new UsingCommandException();
                    break;
                case "add_if_min":
                    if (!argument.isEmpty()) throw new UsingCommandException("{element}");
                    return ProcessCode.OBJECT;
                case "add":
                    if (!argument.isEmpty()) throw new UsingCommandException("{element}");
                    return ProcessCode.OBJECT;
                case "remove_greater":
                    if (!argument.isEmpty()) throw new UsingCommandException("{element}");
                    return ProcessCode.OBJECT;
                case "remove_lower":
                    if (!argument.isEmpty()) throw new UsingCommandException("{element}");
                    return ProcessCode.OBJECT;
                case "min_by_id":
                    if (!argument.isEmpty()) throw new UsingCommandException();
                    break;
                case "print_descending":
                    if (!argument.isEmpty()) throw new UsingCommandException();
                    break;
                case "update":
                    if (argument.isEmpty()) throw new UsingCommandException("<ID> {element}");
                    return ProcessCode.UPDATE_OBJECT;
                case "remove_by_id":
                    if (argument.isEmpty()) throw new UsingCommandException("<ID>");
                    break;
                case "execute_script":
                    if (argument.isEmpty()) throw new UsingCommandException("<file_name>");
                    return ProcessCode.SCRIPT;
                case "filter_contains_name":
                    if (argument.isEmpty()) throw new UsingCommandException("<name>");
                    break;
                //case "server_exit":
                  //  if (!argument.isEmpty()) throw new UsingCommandException();
                    //break;
                default:
                    System.out.println("Команда '" + command + "' не найдена. Введите 'help' для справки.");
                    return ProcessCode.ERROR;
            }
        } catch (UsingCommandException e) {
            if (e.getMessage() != null) command += " " + e.getMessage();
            System.out.println("Использование: '" + command + "'");
            return ProcessCode.ERROR;
        }
        return ProcessCode.OK;
    }

    private boolean fileMode () {return !readerStack.isEmpty();}

}
