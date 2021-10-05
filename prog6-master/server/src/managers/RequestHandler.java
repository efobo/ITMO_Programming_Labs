package managers;

import Communication.*;
import commands.*;
import data.Labwork;

import java.util.TreeSet;

public class RequestHandler {
    private final CommandManager commandManager;

    public RequestHandler (CommandManager commandManager) {
        this.commandManager = commandManager;
    }


    public Response handle (Request request) {
        ResponseCode responseCode = executeCommand(request.getCommandName(),
                request.getCommandStrArg(), request.getCommandObjArg());
        return new Response(responseCode, ResponseOutput.getAndClear());
    }

    private ResponseCode executeCommand(String command, String commandStrArg, Object commandObjArg) {
        switch (command) {
            case "":
                break;
            case "next":
                break;
            case "help":
                if (!commandManager.help(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "add_if_min":
                if (!commandManager.addIfMin(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "add":
                if (!commandManager.add(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeByID(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "save":
                if (!commandManager.save(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "remove_greater":
                if (!commandManager.removeGreater(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "remove_lower":
                if (!commandManager.removeLower(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "min_by_id":
                if (!commandManager.minByID(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "print_descending":
                if (!commandManager.printDescending(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "filter_contains_name":
                if (!commandManager.filterContainsName(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                else return ResponseCode.CLIENT_EXIT;
            //case "server_exit":
              //  if (!commandManager.serverExit(commandStrArg, commandObjArg)) return ResponseCode.ERROR;
                //else return ResponseCode.SERVER_EXIT;
            default:
                ResponseOutput.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
