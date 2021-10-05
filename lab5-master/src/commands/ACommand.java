package commands;

/**
 * Абстрактный класс для команд
 */
public abstract class ACommand implements ICommand{
    private String name;
    private String description;

    public ACommand(String name, String description) {
        this.name = name;
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
}


