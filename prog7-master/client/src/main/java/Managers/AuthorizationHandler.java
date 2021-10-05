package Managers;

import Communication.Request;
import Communication.User;

import java.io.BufferedReader;

public class AuthorizationHandler {
    private final String loginCommand = "login";
    private final String registerCommand = "register";

    private BufferedReader inputReader;

    public AuthorizationHandler (BufferedReader inputReader) {
        this.inputReader = inputReader;
    }

    public Request handle() {
        UserAsker authAsker = new UserAsker(inputReader);
        String command = authAsker.askAuthorization() ? loginCommand : registerCommand;
        User user = new User(authAsker.askLogin(), authAsker.askPassword());
        return new Request(command, "", null, user);
    }
}
