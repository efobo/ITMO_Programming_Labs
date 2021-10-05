package data;

/**
 * Enum класс Color для выбора цвета глаз
 */
public enum Color {
    GREEN,
    YELLOW,
    WHITE;

    /**
     * Получение всех возможных цветов глаз
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
