package Managers;

import Exceptions.MustBeNotEmptyException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class UserAsker {
    private BufferedReader inputReader;

    public UserAsker (BufferedReader inputReader) {
        this.inputReader = inputReader;
    }

    public String askLogin() {
        String login;
        while (true) {
            try {
                System.out.println("Введите логин:");
                System.out.print("> ");
                login = inputReader.readLine().trim();
                if (login.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException e) {
                System.out.println("Такого логина не существует");
            } catch (MustBeNotEmptyException e) {
                System.out.println("Имя не может быть пустым");
            } catch (IllegalStateException e) {
                System.out.println("Непредвиденная ошибка");
                System.exit(0);
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
            }
        }
        return login;
    }

    public String askPassword() {
        String password;
        while (true) {
            try {
                System.out.println("Введите пароль:");
                System.out.print("> ");

                password = inputReader.readLine().trim();
                break;
            } catch (NoSuchElementException exception) {
                System.out.println("Неверный логин или пароль!");
            } catch (IllegalStateException exception) {
                System.out.println("Непредвиденная ошибка");
                System.exit(0);
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
            }
        }
        return password;
    }

    public boolean askAuthorization () {
        boolean flag = true;
        boolean ret = false;
        while (flag) {
            try {
                System.out.println("У вас уже есть учетная запись? Ответьте yes/no");
                System.out.print("> ");
                String answer = inputReader.readLine().trim();
                if (answer.equalsIgnoreCase("no")) {
                    ret = false;
                    break;
                } else {
                    if (answer.equalsIgnoreCase("yes")) {
                        ret = true;
                        break;
                    } else {
                        System.out.println("Вы можете ввести только \"yes\" или \"no\"");
                        continue;
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка ввода");
            }
        }
        return ret;
    }
}
