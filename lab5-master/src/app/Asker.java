package app;

import Exceptions.IncorrectScriptException;
import Exceptions.WrongAmountOfElementsException;
import data.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * Класс для создания новых элементов коллекции
 */
public class Asker {
    private TreeSet<Labwork> labworks;
    private Labwork labwork;
    private boolean fileMode;
    private BufferedReader inputReader;

    public Asker(TreeSet<Labwork> labworks, BufferedReader inputReader) {
        this.labworks = labworks;
        fileMode = false;
        this.inputReader = inputReader;
    }

    /**
     * Установить buffered reader
     * @param inputReader
     */
    public void setReader (BufferedReader inputReader) {this.inputReader = inputReader;}

    /**
     * Получить buffered reader
     * @return input reader
     */
    public BufferedReader getInputReader() {return inputReader;}

    /**
     * Включить режим пользователя
     */
    public void setUserMode () {fileMode = false;}

    /**
     * Включить режим чтения из файла
     */
    public void setFileMode () {fileMode = true;}

    /**
     * Генерирует новый id
     * @return id
     */
    public Long generateId() {
        if (labworks.isEmpty()) return 1L;
        return labworks.last().getId() + 1;
    }

    /**
     * Спрашивает имя подопытного
     * @return name
     * @throws IncorrectScriptException
     */
    public String askName() throws IncorrectScriptException {
        String name = null;
        boolean flag = true;
        while (flag) {
            try {
                System.out.println("Введите имя подопытного ");
                System.out.print("> ");
                name = inputReader.readLine().trim();
                if (fileMode) System.out.println(name);
                if (name.equals("")) throw new NullPointerException();
                flag = false;
            } catch (NullPointerException e) {
                System.out.println("Имя не должно быть пустым");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return name;
    }

    /**
     * Спрашивает координату x
     * @return xc
     * @throws IncorrectScriptException
     */
    public Long askXc() throws IncorrectScriptException {
        Long xc = null;
        String strXc;
        Float xcCheck;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Введите координату X");
                System.out.println("(число должно быть целым)");
                System.out.print("> ");
                strXc = inputReader.readLine().trim();
                if (fileMode) System.out.println(strXc);
                xcCheck = Float.parseFloat(strXc);
                if (xcCheck > 547 || xcCheck % 1 != 0) throw new WrongAmountOfElementsException();
                if (xcCheck == null) throw new NullPointerException();
                xc = Long.parseLong(strXc);
                flag = false;
            } catch (NullPointerException e) {
                System.out.println("Поле не должно быть пустым!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                System.out.println("Координата X должна быть представлена числом");
                if (fileMode) throw new IncorrectScriptException();
            } catch (WrongAmountOfElementsException e) {
                System.out.println("X должно быть целым числом, меньшим 547");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return xc;
    }

    /**
     * Спрашивает координату y
     * @return yc
     * @throws IncorrectScriptException
     */
    public float askYc() throws IncorrectScriptException {
        float yc = 0;
        String strYc;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Введите координату y");
                System.out.println("(число может быть не целым)");
                System.out.print("> ");
                strYc = inputReader.readLine().trim();
                if (fileMode) System.out.println(strYc);
                if (strYc == "") throw new NullPointerException();
                yc = Float.parseFloat(strYc);
                flag = false;
            } catch (NullPointerException e) {
                System.out.println("Поле не может быть пустым");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                System.out.println("Координата Y должна быть представлена числом");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return yc;
    }

    /**
     * Спрашивает минимальные баллы
     * @return minimal point
     * @throws IncorrectScriptException
     */
    public double askMinimalPoint() throws IncorrectScriptException {
        double minimalPoint = -1;
        String strMinimalPoint;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Введите минимальные очки ");
                System.out.println("(число может быть не целым)");
                System.out.print("> ");
                strMinimalPoint = inputReader.readLine().trim();
                if (fileMode) System.out.println(strMinimalPoint);
                if (strMinimalPoint == "") throw new NullPointerException();
                minimalPoint = Double.parseDouble(strMinimalPoint);
                if (minimalPoint <= 0) throw new WrongAmountOfElementsException();
                flag = false;
            } catch (NullPointerException e) {
                System.out.println("Поле не может быть пустым");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                System.out.println("Поле должно быть представлено числом");
                if (fileMode) throw new IncorrectScriptException();
            } catch (WrongAmountOfElementsException e) {
                System.out.println("Поле должно быть больше 0");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return minimalPoint;
    }

    /**
     * Спрашивает сложность
     * @return difficulty
     * @throws IncorrectScriptException
     */
    public Difficulty askDifficulty() throws IncorrectScriptException {
        Difficulty difficulty = null;
        String strDifficulty;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Выберите сложность: " + Difficulty.nameList());
                System.out.print("> ");
                strDifficulty = inputReader.readLine().trim();
                if (fileMode) System.out.println(strDifficulty);
                if (!strDifficulty.equals("")) {
                    difficulty = Difficulty.valueOf(strDifficulty);
                }
                flag = false;
            } catch (NoSuchElementException | IllegalArgumentException e) {
                System.out.println("Неверно введена сложность");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return difficulty;
    }

    /**
     * Спраишивает локацию
     * @return location
     * @throws IncorrectScriptException
     */
    public Location askLocation () throws IncorrectScriptException {
        Location location = null;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Вы хотите указать местоположение создателя? Ответьте yes/no");
                System.out.print("> ");
                String answer = inputReader.readLine().trim();
                if (fileMode) System.out.println(answer);
                if (answer.equalsIgnoreCase("no")) {
                    break;
                } else {
                    if (answer.equalsIgnoreCase("yes")) {
                        location = new Location(askXl(), askYl(), askLocationName());
                        flag = false;
                    } else {
                        System.out.println("\"Вы можете ввести только \"yes\" или \"no\"");
                        continue;
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return location;
    }

    /**
     * Спрашивает создателя
     * @return person
     * @throws IncorrectScriptException
     */
    public Person askPerson () throws IncorrectScriptException {
        Person person = null;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Вы хотите указать создателя? Ответьте yes/no");
                System.out.print("> ");
                String answer = inputReader.readLine().trim();
                if (fileMode) System.out.println(answer);
                if (answer.equalsIgnoreCase("no")) {
                    break;
                } else {
                    if (answer.equalsIgnoreCase("yes")) {
                        person = new Person(askPersonName(), askBirthday(), askEyeColor(), askHairColor(), askLocation());
                        flag = false;
                    } else {
                        System.out.println("Вы можете ввести только \"yes\" или \"no\"");
                        continue;
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return person;
    }

    /**
     * Спрашивает имя создателя
     * @return name
     * @throws IncorrectScriptException
     */
    public String askPersonName() throws IncorrectScriptException {
        String personName = null;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Введите имя создателя");
                System.out.print("> ");
                personName = inputReader.readLine().trim();
                if (fileMode) System.out.println(personName);
                if (personName.equals("")) throw new NullPointerException();
                flag = false;
            } catch (NullPointerException e) {
                System.out.println("Поле не должно быть пустым");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return personName;
    }

    /**
     * Спрашивает дату рождения создателя
     * @return birthday
     * @throws IncorrectScriptException
     */
    public java.time.LocalDate askBirthday() throws IncorrectScriptException {
        LocalDate birthday = null;
        String strBirthday;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Введите дату рождения");
                System.out.println("Пример ввода: 2000-12-11");
                System.out.print("> ");
                strBirthday = inputReader.readLine().trim();
                if (fileMode) System.out.println(strBirthday);
                if (!strBirthday.equals("")) {
                    birthday = LocalDate.parse(strBirthday);
                }
                flag = false;
            } catch (IllegalArgumentException | DateTimeParseException e) {
                System.out.println("Неверно введена дата");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }

        }
        return birthday;
    }

    /**
     * Спрашивает цвет глаз создателя
     * @return eye color
     * @throws IncorrectScriptException
     */
    public data.Color askEyeColor() throws IncorrectScriptException {
        data.Color eyeColor = null;
        String strEyeColor;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Выберите цвет глаз: " + data.Color.nameList());
                System.out.print("> ");
                strEyeColor = inputReader.readLine().trim();
                if (fileMode) System.out.println(strEyeColor);
                if (strEyeColor.equals("")) throw new NullPointerException();
                eyeColor = data.Color.valueOf(strEyeColor);
                flag = false;
            } catch (NoSuchElementException | IllegalArgumentException e) {
                System.out.println("Неверно введен цвет");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NullPointerException e) {
                System.out.println("Поле не должно быть пустым");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return eyeColor;
    }

    /**
     * Спрашивает цвет волос создателя
     * @return hair color
     * @throws IncorrectScriptException
     */
    public data.hair.Color askHairColor() throws IncorrectScriptException {
        data.hair.Color hairColor = null;
        String strHairColor;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Выберите цвет волос: " + data.hair.Color.nameList());
                System.out.print("> ");
                strHairColor = inputReader.readLine().trim();
                if (fileMode) System.out.println(strHairColor);
                if (!strHairColor.equals("")) {
                    hairColor = data.hair.Color.valueOf(strHairColor);
                }
                flag = false;
            } catch (NoSuchElementException | IllegalArgumentException e) {
                System.out.println("Неверно введен цвет");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return hairColor;
    }

    /**
     * Спрашивает координату х локации
     * @return xl
     * @throws IncorrectScriptException
     */
    public Float askXl() throws IncorrectScriptException {
        Float xl = null;
        String strXl;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Введите координату X местоположения ");
                System.out.print("> ");
                strXl = inputReader.readLine().trim();
                if (fileMode) System.out.println(strXl);
                if (strXl.equals("")) throw new NullPointerException();
                xl = Float.parseFloat(strXl);
                flag = false;
            } catch (NumberFormatException e) {
                System.out.println("Координата должна быть представлена числом");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NullPointerException e) {
                System.out.println("Поле не может быть пустым");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return xl;
    }

    /**
     * Спрашивает координату y локации
     * @return yl
     * @throws IncorrectScriptException
     */
    public Double askYl() throws IncorrectScriptException {
        Double yl = null;
        String strYl;
        boolean flag = true;
        while (flag) {
            try{
                System.out.println("Введите координату Y местоположения");
                System.out.print("> ");
                strYl = inputReader.readLine().trim();
                if (fileMode) System.out.println(strYl);
                if (strYl.equals("")) throw new NullPointerException();
                yl = Double.parseDouble(strYl);
                flag = false;
            } catch (NumberFormatException e) {
                System.out.println("Координата должна быть представлена числом");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NullPointerException e) {
                System.out.println("Поле не может быть пустым");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return yl;
    }

    /**
     * Спрашивает название локации
     * @return location name
     * @throws IncorrectScriptException
     */
    public String askLocationName () throws IncorrectScriptException {
        String locationName = null;
        boolean flag = true;

        while (flag) {
            try {
                System.out.println("Введите название местоположения");
                System.out.print("> ");
                locationName = inputReader.readLine().trim();
                if (fileMode) System.out.println(locationName);
                if (locationName.equals("")) {
                    locationName = null;
                }
                flag = false;
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
                if (fileMode) throw new IncorrectScriptException();
            }
        }
        return locationName;
    }

}

