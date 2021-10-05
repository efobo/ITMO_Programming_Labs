package data.hair;

/**
 * Enum класс Color для выбора цвета волос
 */
public enum Color {
    RED,
    BLACK,
    ORANGE;

    /**
     * Получение списка всех возможных цветов волос
     * @return name list
     */
    public static String nameList() {
        String nameList = "";
        for (Color color : values()) {
            nameList += color.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
