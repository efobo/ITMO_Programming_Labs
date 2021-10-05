package managers;

import Communication.Request;
import Communication.Response;
import Communication.ResponseCode;
import Communication.User;

import java.util.concurrent.RecursiveTask;

public class HandleRequestTask extends RecursiveTask<Response> {
    private Request request;
    private CommandManager commandManager;
    private CollectionManager collectionManager;

    public HandleRequestTask(Request request, CommandManager commandManager, CollectionManager collectionManager) {
        this.request = request;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    @Override
    protected Response compute() {
        User hashedUser = new User(
                request.getUser().getUsername(),
                PasswordHash.hashPassword(request.getUser().getPassword())
        );
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStrArg(),
                request.getCommandObjArg(), hashedUser);
        return new Response(responseCode, ResponseOutput.getAndClear(), ResponseOutput.getArgsAndClear(), collectionManager.getCollection());
    }

    private synchronized ResponseCode executeCommand(String command, String commandStrArg, Object commandObjArg, User user) {
        switch (command) {
            case "":
                break;
            case "next":
                break;
            //case "help":
              //  if (!commandManager.help(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                //break;
            case "info":
                if (!commandManager.info(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                break;
            //case "show":
              //  if (!commandManager.show(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                //break;
            case "add_if_min":
                if (!commandManager.addIfMin(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                break;
            case "add":
                if (!commandManager.add(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeByID(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                break;
            case "remove_greater":
                if (!commandManager.removeGreater(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                break;
            case "remove_lower":
                if (!commandManager.removeLower(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                break;
            case "login":
                if (!commandManager.login(commandStrArg, commandObjArg, user))
                    return ResponseCode.ERROR;
                break;
            case "register":
                if (!commandManager.register(commandStrArg, commandObjArg, user))
                    return ResponseCode.ERROR;
                break;
            case "refresh":
                if (!commandManager.refresh(commandStrArg, commandObjArg, user))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStrArg, commandObjArg, user)) return ResponseCode.ERROR;
                else return ResponseCode.CLIENT_EXIT;
            default:
                ResponseOutput.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }

}
