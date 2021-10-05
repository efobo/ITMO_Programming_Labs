package Communication;

import java.io.Serializable;

public class Request implements Serializable {
    private String commandName;
    private String commandStrArg;
    private Serializable commandObjArg;

    public Request (String commandName, String commandStrArg, Serializable commandObjArg) {
        this.commandName = commandName;
        this.commandStrArg = commandStrArg;
        this.commandObjArg = commandObjArg;
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
}
