package Database;

import Communication.User;
import Exceptions.DatabaseHandlingException;
import app.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseUserHandler {
    private final String SELECT_USER_BY_ID = "SELECT * FROM " +
            DatabaseManager.userTableName +
            " WHERE " + DatabaseManager.userTable_userID + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " +
            DatabaseManager.userTableName +
            " WHERE " + DatabaseManager.userTable_Login + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME +
            " AND " + DatabaseManager.userTable_Password + " = ?";
    private final String INSERT_USER = "INSERT INTO " + DatabaseManager.userTableName +
            "(" + DatabaseManager.userTable_Login + ", " +
            DatabaseManager.userTable_Password + ") VALUES (?, ?)";

    private DatabaseManager databaseManager;

    public DatabaseUserHandler(DatabaseManager databaseManager) {this.databaseManager = databaseManager;}

    public User getUserById(long userId) throws SQLException {
        User user;
        PreparedStatement preparedSelectUserByIdStatement = null;
        try {
            preparedSelectUserByIdStatement =
                    databaseManager.getPreparedStatement(SELECT_USER_BY_ID, false);
            preparedSelectUserByIdStatement.setLong(1, userId);
            ResultSet resultSet = preparedSelectUserByIdStatement.executeQuery();
            Main.logger.log(Level.INFO, "Выполнен запрос SELECT_USER_BY_ID.");
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString(DatabaseManager.userTable_Login),
                        resultSet.getString(DatabaseManager.userTable_Password)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            Main.logger.log(Level.SEVERE, "Произошла ошибка при выполнении запроса SELECT_USER_BY_ID!");
            throw new SQLException(exception);
        } finally {
            databaseManager.closePreparedStatement(preparedSelectUserByIdStatement);
        }
        return user;
    }

    public boolean checkUserByUsernameAndPassword(User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectUserByUsernameAndPasswordStatement = null;
        try {
            preparedSelectUserByUsernameAndPasswordStatement =
                    databaseManager.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserByUsernameAndPasswordStatement.setString(1, user.getUsername());
            preparedSelectUserByUsernameAndPasswordStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedSelectUserByUsernameAndPasswordStatement.executeQuery();
            Main.logger.log(Level.INFO, "Выполнен запрос SELECT_USER_BY_USERNAME_AND_PASSWORD.");
            return resultSet.next();
        } catch (SQLException exception) {
            Main.logger.log(Level.SEVERE, "Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD!");
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedSelectUserByUsernameAndPasswordStatement);
        }
    }

    public long getUserIdByUsername(User user) throws DatabaseHandlingException {
        long userId;
        PreparedStatement preparedSelectUserByUsernameStatement = null;
        try {
            preparedSelectUserByUsernameStatement =
                    databaseManager.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedSelectUserByUsernameStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedSelectUserByUsernameStatement.executeQuery();
            Main.logger.log(Level.INFO, "Выполнен запрос SELECT_USER_BY_USERNAME.");
            if (resultSet.next()) {
                userId = resultSet.getLong(DatabaseManager.userTable_userID);
            } else userId = -1;
            return userId;
        } catch (SQLException exception) {
            Main.logger.log(Level.SEVERE, "Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME!");
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedSelectUserByUsernameStatement);
        }
    }

    public boolean insertUser(User user) throws DatabaseHandlingException {
        PreparedStatement preparedInsertUserStatement = null;
        try {
            if (getUserIdByUsername(user) != -1) return false;
            preparedInsertUserStatement =
                    databaseManager.getPreparedStatement(INSERT_USER, false);
            preparedInsertUserStatement.setString(1, user.getUsername());
            preparedInsertUserStatement.setString(2, user.getPassword());
            if (preparedInsertUserStatement.executeUpdate() == 0) throw new SQLException();
            Main.logger.log(Level.INFO, "Выполнен запрос INSERT_USER.");
            return true;
        } catch (SQLException exception) {
            Main.logger.log(Level.SEVERE, "Произошла ошибка при выполнении запроса INSERT_USER!");
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedInsertUserStatement);
        }
    }
}
