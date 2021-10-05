package data;

/**
 * Enum класс Difficulty для выбора сложности
 */
public enum Difficulty {
    NORMAL,
    HOPELESS,
    TERRIBLE;

    /**
     * Возвращает список всех возможных сложностей
     * @return name list
     */
    public static String nameList() {
        String nameList = "";
        for (Difficulty difficulty : values()) {
            nameList += difficulty.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
