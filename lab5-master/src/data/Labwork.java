package data;

import java.time.LocalDateTime;

/**
 * Класс Labwork - основной класс для коллекции
 */
public class Labwork implements Comparable<Labwork> {
    private long id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private double minimalPoint;
    private Difficulty difficulty;
    private Person author;

    public Labwork(long id, String name, Coordinates coordinates, LocalDateTime creationDate, double minimalPoint, Difficulty difficulty, Person author){
        this.id=id;
        this.name=name;
        this.coordinates=coordinates;
        this.creationDate=creationDate;
        this.minimalPoint=minimalPoint;
        this.difficulty = difficulty;
        this.author=author;
    }

    /**
     * Возвращает id
     * @return id
     */
    public long getId () {return id;}

    /**
     * Возвоащает имя
     * @return name
     */
    public String getName () {return name;}

    /**
     * Возвращает класс Coordinates
     * @return coordinates
     */
    public Coordinates getCoordinates () {return coordinates;}

    /**
     * Возвращает дату создания
     * @return creation date
     */
    public LocalDateTime getCreationDate () {return creationDate;}

    /**
     * Возвращает минимальный балл
     * @return minimal point
     */
    public double getMinimalPoint () {return minimalPoint;}

    /**
     * Возвращает сложность
     * @return difficulty
     */
    public Difficulty getDifficulty () {return difficulty;}

    /**
     * Возвращает сложность
     * @return person
     */
    public Person getAuthor () {return author;}

    /**
     * Установить id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Установить имя
     * @param name
     */
    public void setName (String name){
        this.name=name;
    }

    /**
     * Установить класс Coordinates
     * @param coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Установить дату создания
     * @param creationDate
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Установить минимальный балл
     * @param minimalPoint
     */
    public void setMinimalPoint(double minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    /**
     * Установить сложность
     * @param difficulty
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Установить автора
     * @param author
     */
    public void setAuthor(Person author) {
        this.author = author;
    }


    @Override
    public int compareTo(Labwork lbObj) {
        if (this.id < lbObj.id) return -1;
        else if(this.id > lbObj.id) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        String info = "";
        info += "\nПодопытный " + id;
        info += "\n Имя: " + name;
        info += "\nСоздан: " + creationDate.toLocalDate() + " " + creationDate.toLocalTime();
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
