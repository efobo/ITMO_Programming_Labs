package app;

import Exceptions.ScriptRecursionException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Класс для вывода результата команд
 */
public class Output {
    private CommandManager commandManager;
    private BufferedReader inputReader;
    private Asker asker;
    private List<String> scriptStack = new ArrayList<>();



    public Output (CommandManager commandManager, BufferedReader inputReader, Asker asker) {
        this.commandManager = commandManager;
        this.inputReader = inputReader;
        this.asker = asker;
    }

    /**
     * Метод, реализующий интерактивный режим
     */
    public void interactiveMode() {
        int status;
        String [] userCommand = {"",""};
        boolean flag = true;
        while (flag) {
            try {
                System.out.print("$ ");
                userCommand = (inputReader.readLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                status = launchCommand(userCommand);
                if (status == 2) {
                    flag = false;
                }
            } catch (NoSuchElementException e) {
                System.out.println("Ввод не обнаружен");
            } catch (IllegalStateException e) {
                System.out.println("Непредвиденная ошибка");
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
            } catch (NullPointerException e) {
                break;
            }
        }
    }

    /**
     * Метод, запускающий команды
     * @param userCommand
     * @return
     */
    private int launchCommand(String[] userCommand) {
        switch (userCommand[0]) {
            case "":
                break;
            case "help":
                if (!commandManager.help(userCommand[1])) return 1;
                break;
            case "info":
                if (!commandManager.info(userCommand[1])) return 1;
                break;
            case "show":
                if (!commandManager.show(userCommand[1])) return 1;
                break;
            case "add_if_min":
                if (!commandManager.addIfMin(userCommand[1])) return 1;
                break;
            case "add":
                if (!commandManager.add(userCommand[1])) return 1;
                break;
            case "update":
                if (!commandManager.update(userCommand[1])) return 1;
                break;
            case "remove_by_id":
                if (!commandManager.removeByID(userCommand[1])) return 1;
                break;
            case "clear":
                if (!commandManager.clear(userCommand[1])) return 1;
                break;
            case "save":
                if (!commandManager.save(userCommand[1])) return 1;
                break;
            case "execute_script":
                if (!commandManager.executeScript(userCommand[1])) return 1;
                return scriptMode(userCommand[1]);
            case "remove_greater":
                if (!commandManager.removeGreater(userCommand[1])) return 1;
                break;
            case "remove_lower":
                if (!commandManager.removeLower(userCommand[1])) return 1;
                break;
            case "min_by_id":
                if (!commandManager.minByID(userCommand[1])) return 1;
                break;
            case "print_descending":
                if (!commandManager.printDescending(userCommand[1])) return 1;
                break;
            case "filter_contains_name":
                if (!commandManager.filterContainsName(userCommand[1])) return 1;
                break;
            case "exit":
                if (!commandManager.exit(userCommand[1])) return 1;
                else return 2;
            default:
                if (!commandManager.noSuchCommand(userCommand[0])) return 1;
        }
        return 0;
    }

    /**
     * Метод, реализующий работу со скриптом
     * @param argument
     * @return
     */
    public int scriptMode (String argument) {
        String[] userCommand = {"", ""};
        int status;
        scriptStack.add(argument);

        try (BufferedReader scriptReader = new BufferedReader(new InputStreamReader(new FileInputStream(argument)));) {
            String line = scriptReader.readLine();
            if (line == null) throw new NoSuchElementException();
            BufferedReader tmpReader = asker.getInputReader();
            asker.setReader(scriptReader);
            asker.setFileMode();
            do {
                userCommand = (line.trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (line != null && userCommand[0].isEmpty()) {
                    line = scriptReader.readLine();
                    userCommand = (line.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                System.out.println("$ " + String.join(" ", userCommand));
                if (userCommand[0].equals("execute_script")) {
                    for (String script : scriptStack) {
                        if (userCommand[1].equals(script)) throw new ScriptRecursionException();
                    }
                }
                status = launchCommand(userCommand);
                line = scriptReader.readLine();

            } while (status == 0 && line != null);
            asker.setReader(tmpReader);
            asker.setUserMode();
            if (status == 1 && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty()))
                System.out.println("Проверьте скрипт на корректность введенных данных!");
            return status;

        } catch (FileNotFoundException exception) {
            System.out.println("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            System.out.println("Файл со скриптом пуст!");
        } catch (ScriptRecursionException exception) {
            System.out.println("Скрипты не могут вызываться рекурсивно!");
        } catch (IllegalStateException exception) {
            System.out.println("Непредвиденная ошибка!");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Ошибка ввода");
        } catch (NullPointerException e) {

        } finally {
            scriptStack.remove(scriptStack.size()-1);
        }
        return 1;
    }

}


