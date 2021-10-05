package managers;

import app.Main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class PasswordHash {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger integers = new BigInteger(1, messageDigest);
            String newPassword = integers.toString(16);
            while (newPassword.length() < 32) {
                newPassword = "0" + newPassword;
            }
            return newPassword;
        } catch (NoSuchAlgorithmException e) {
            Main.logger.log(Level.SEVERE, "Не найден алгоритм хэширования пароля");
            throw new IllegalStateException(e);
        }
    }
}
