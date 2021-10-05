package data;

import java.time.LocalDate;

/**
 * Класс Person
 */
public class Person {
    private String name;
    private java.time.LocalDate birthday;
    private data.Color eyeColor;
    private data.hair.Color hairColor;
    private Location location;

    public Person (String name, java.time.LocalDate birthday, data.Color eyeColor, data.hair.Color hairColor, Location location) {
        this.name=name;
        this.birthday=birthday;
        this.eyeColor=eyeColor;
        this.hairColor=hairColor;
        this.location=location;
    }


    /**
     * Получить цвет глаз
     * @return eye color
     */
    public data.Color getEyeColor() {
        return eyeColor;
    }

    /**
     * Установить цвет глаз
     * @param eyeColor
     */
    public void setEyeColor(data.Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    /**
     * Получить имя создателя
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Установить имя создателя
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить дату рождения
     * @return birthday
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Установить дату рождения
     * @param birthday
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * Получить цвет волос
     * @return hair color
     */
    public data.hair.Color getHairColor() {
        return hairColor;
    }

    /**
     * Установить цвет волос
     * @param hairColor
     */
    public void setHairColor(data.hair.Color hairColor) {
        this.hairColor = hairColor;
    }

    /**
     * Получить класс Location
     * @return location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Установить локацию
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Person) {
            Person personObj = (Person) obj;
            return name.equals(personObj.getName()) && birthday.equals(personObj.getBirthday()) && (eyeColor == personObj.getEyeColor()) && (hairColor == personObj.getHairColor()) && location.equals(personObj.getLocation());
        //
        }
        return false;
    }
    @Override
    public String toString() {
        String info = "\nИмя: " + name ;
        if (birthday != null) {
            info += "\nДень рождение: " + birthday;
        }
        info += "\nЦвет глаз: " + eyeColor;
        if (hairColor != null) {
            info += "\nЦвет волос: " + hairColor;
        }
        if (location != null) {
            info += "\nМестоположение: x = " + location.getX() + ", y = " + location.getY();
        }
        return info;
    }
}

