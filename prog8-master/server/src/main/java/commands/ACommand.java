package commands;

/**
 * Абстрактный класс для команд
 */
public abstract class ACommand implements ICommand{
    private String name;
    private String usage;
    private String description;

    public ACommand(String name, String usage, String description) {
        this.name = name;
        this.usage = usage;
        this.description = description;
    }

    /**
     * Возвращает имя команды
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание команды
     * @return description of command
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " (" + description + ")";
    };

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ACommand other = (ACommand) obj;
        return name.equals(other.name) && description.equals(other.description);
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}


