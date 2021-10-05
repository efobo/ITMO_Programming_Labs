package Communication;

import java.io.Serializable;

public class Request implements Serializable {
    private String commandName;
    private String commandStrArg;
    private Serializable commandObjArg;
    private User user;

    public Request (String commandName, String commandStrArg, Serializable commandObjArg, User user) {
        this.commandName = commandName;
        this.commandStrArg = commandStrArg;
        this.commandObjArg = commandObjArg;
        this.user = user;
    }

    public Request (String commandName, String commandStrArg) {
        this.commandName = commandName;
        this.commandStrArg = commandStrArg;
    }

    public Request () {
        this ("", "");
    }

    /**
     * @return commandName
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @param commandName
     */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    /**
     * @return commandStrArg
     */
    public String getCommandStrArg() {
        return commandStrArg;
    }

    /**
     * @param commandStrArg
     */
    public void setCommandStrArg(String commandStrArg) {
        this.commandStrArg = commandStrArg;
    }

    /**
     * @return commandObjArg
     */
    public Object getCommandObjArg() {
        return commandObjArg;
    }

    /**
     * @param commandObjArg
     */
    public void setCommandObjArg(Serializable commandObjArg) {
        this.commandObjArg = commandObjArg;
    }

    public boolean isEmpty () {
        return commandName.isEmpty() && commandStrArg.isEmpty() && commandObjArg == null;
    }

    @Override
    public String toString () {
        return "Request [ " + commandName + ", " + commandStrArg + ", " + commandObjArg + "]";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
