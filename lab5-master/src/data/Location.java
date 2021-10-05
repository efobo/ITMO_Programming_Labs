package data;
/**
 * Класс Location
 */
public class Location {
    private Float x;
    private Double y;
    private String name;


    public Location (Float x, Double y, String name) {
        this.x=x;
        this.y=y;
        this.name=name;
    }

    /**
     * Получить координату х локации
     * @return x
     */
    public Float getX () {return x;}

    /**
     * Получить коорлинату y локации
     * @return y
     */
    public Double getY () {return y;}

    /**
     * Получить название локации
     * @return name
     */
    public String getName () {return name;}

    /**
     * Установить координату х
     * @param x
     */
    public void setX(Float x) {
        this.x = x;
    }

    /**
     * Установить координату y
     * @param y
     */
    public void setY(Double y) {
        this.y = y;
    }

    /**
     * Установить название локации
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Location) {
            Location locationObj = (Location) obj;
            return name.equals(locationObj.getName()) && x.equals(locationObj.getX()) && y.equals(locationObj.getY());
        }
        return false;
    }
    @Override
    public String toString() {
        String info = "x: " + x + " y: " + y;
        if (name != null) {
            info += " Место" + name;
        }
        return info;
    }
}

