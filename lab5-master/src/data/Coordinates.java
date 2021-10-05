package data;

/**
 * Класс Coordinates
 */
public class Coordinates {
    private Long x;
    private float y;

    public Coordinates (Long x, float y) {
        this.x=x;
        this.y=y;
    }

    /**
     * Получение координаты х
     * @return xc
     */
    public Long getX () {return x;}

    /**
     * Получение координаты y
     * @return yc
     */
    public float getY () {return y;}

    /**
     * Установить координату х
     * @param x
     */
    public void setX(Long x) {
        this.x = x;
    }

    /**
     * Установить координату y
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }


    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }

    @Override
    public int hashCode() {
        return x.hashCode() + (int) y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Coordinates) {
            Coordinates coordinatesObj = (Coordinates) obj;
            return (y == coordinatesObj.getY()) && x.equals(coordinatesObj.getX());
        }
        return false;
    }
}
