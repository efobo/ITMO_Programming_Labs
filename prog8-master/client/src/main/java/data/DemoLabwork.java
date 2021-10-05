package data;

import java.io.Serializable;

/**
 * Класс для получения объекта Labwork
 */
public class DemoLabwork implements Serializable {
    private String name;
    private Coordinates coordinates;
    private double minimalPoint;
    private Difficulty difficulty;
    private Person author;

    public DemoLabwork (String name, Coordinates coordinates, double minimalPoint, Difficulty difficulty, Person author) {
        this.name = name;
        this.coordinates = coordinates;
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.author = author;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public double getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(double minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    @Override
    public String toString() {
        String info = "";
        info += "\nНеготовый Подопытный ";
        info += "\nимя: " + name;
        info += "\nКоординаты: " + coordinates;
        info += "\nМинимальные очки: " + minimalPoint;
        if (difficulty != null) {
            info += "\nСложность: " + difficulty;
        }
        if (author != null) {
            info += "\nСоздатель: " + author.getName() + "\n";
        }
        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + difficulty.hashCode() + author.hashCode()+ (int) minimalPoint;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Labwork) {
            Labwork labObj = (Labwork) obj;
            return name.equals(labObj.getName()) && coordinates.equals(labObj.getCoordinates()) && (minimalPoint == labObj.getMinimalPoint()) &&
                    (difficulty == labObj.getDifficulty()) && author.equals(labObj.getAuthor());
        }
        return false;
    }
}
