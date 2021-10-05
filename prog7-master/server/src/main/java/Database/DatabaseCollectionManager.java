package Database;

import Communication.User;
import Exceptions.DatabaseHandlingException;
import app.Main;
import data.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.logging.Level;

public class DatabaseCollectionManager {
    private DatabaseUserHandler databaseUserHandler;
    private DatabaseManager databaseManager;

    public DatabaseCollectionManager(DatabaseManager databaseManager, DatabaseUserHandler databaseUserHandler) {
        this.databaseManager = databaseManager;
        this.databaseUserHandler = databaseUserHandler;
    }

    private Labwork createLabwork (ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(DatabaseManager.labworkTable_id);
        String name = resultSet.getString(DatabaseManager.labworkTable_name);
        Coordinates coordinates = getCoordinatesByLabworkID(id);
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseManager.labworkTable_CreationDate).toLocalDateTime();
        double minimalPoint = resultSet.getDouble(DatabaseManager.labworkTable_MinimalPoint);
        Difficulty difficulty = null;
        if (resultSet.getString(DatabaseManager.labworksTable_Difficulty) != null) {
            difficulty = Difficulty.valueOf(resultSet.getString(DatabaseManager.labworksTable_Difficulty));
        }
        Person person = null;
        if (resultSet.getString(DatabaseManager.labworksTable_PersonID) != null) {
            person = getPersonByID(resultSet.getLong(DatabaseManager.labworksTable_PersonID));
        }
        User user = databaseUserHandler.getUserById(resultSet.getLong(DatabaseManager.labworksTable_userID));

        return new Labwork(id, name, coordinates, creationDate, minimalPoint, difficulty, person, user);
    }

    public TreeSet<Labwork> getCollection () throws DatabaseHandlingException {
        TreeSet<Labwork> labworks = new TreeSet<>();
        PreparedStatement preparedSelectAllStatement = null;
        try {
            preparedSelectAllStatement = databaseManager.getPreparedStatement(SQLRequests.select_all_labworks, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();

            while (resultSet.next()) {
                labworks.add(createLabwork(resultSet));
            }
        } catch (SQLException throwables) {
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedSelectAllStatement);
        }
        return labworks;
    }

    public Labwork insertLabwork (DemoLabwork demoLabwork, User user) throws DatabaseHandlingException {
        Labwork labwork;
        Long personId = 1L; //
        Long locationId = 1L; //Пока так
        PreparedStatement preparedInsertLabworkStatement = null;
        PreparedStatement preparedInsertCoordinateStatement = null;
        PreparedStatement preparedInsertPersonStatement = null;
        PreparedStatement preparedInsertLocationStatement = null;

        try {
            databaseManager.setCommitMode();
            databaseManager.setSavepoint();

            LocalDateTime creationDate = LocalDateTime.now();


            if (demoLabwork.getAuthor() != null) {
                String personSQLStatement = SQLRequests.insert_person_start;
                int countPersonFields = 2;

                // INSERT LOCATION
                if (demoLabwork.getAuthor().getLocation() != null) {
                    int countLocationFields = 1;
                    countPersonFields +=1;
                    personSQLStatement += ", " + DatabaseManager.personTable_LocationID;
                    String locationSQLStatement;
                    if (demoLabwork.getAuthor().getLocation().getName() != null) {
                        locationSQLStatement = SQLRequests.insert_location;
                        preparedInsertLocationStatement = databaseManager.getPreparedStatement(locationSQLStatement, true);
                        preparedInsertLocationStatement.setString(countLocationFields, demoLabwork.getAuthor().getLocation().getName());
                        countLocationFields += 1;
                    } else {
                        locationSQLStatement = SQLRequests.insert_location_without_name;
                        preparedInsertLocationStatement = databaseManager.getPreparedStatement(locationSQLStatement, true);
                    }

                    preparedInsertLocationStatement.setFloat(countLocationFields, demoLabwork.getAuthor().getLocation().getX());
                    countLocationFields +=1;
                    preparedInsertLocationStatement.setDouble(countLocationFields, demoLabwork.getAuthor().getLocation().getY());
                    if (preparedInsertLocationStatement.executeUpdate() == 0) throw new SQLException();
                    ResultSet generatedLocationKeys = preparedInsertLocationStatement.getGeneratedKeys();
                    if (generatedLocationKeys.next()) {
                        locationId = generatedLocationKeys.getLong(1);
                    } else throw new SQLException();
                    Main.logger.log(Level.INFO, "Выполнен запрос insert_location");
                }

                // INSERT PERSON
                if (demoLabwork.getAuthor().getBirthday() != null) {
                    personSQLStatement += ", " + DatabaseManager.personTable_Birthday;
                    countPersonFields +=1;
                }
                if (demoLabwork.getAuthor().getHairColor() != null) {
                    personSQLStatement += ", " + DatabaseManager.personTable_HairColor;
                    countPersonFields +=1;
                }
                if (countPersonFields == 2) {
                    personSQLStatement += SQLRequests.insert_person_end_2values;
                } else {
                    if (countPersonFields == 3) {
                        personSQLStatement += SQLRequests.insert_person_end_3values;
                    } else {
                        if (countPersonFields == 4) {
                            personSQLStatement += SQLRequests.insert_person_end_4values;
                        } else {
                            personSQLStatement += SQLRequests.insert_person_end_5values;
                        }
                    }
                }
                preparedInsertPersonStatement = databaseManager.getPreparedStatement(personSQLStatement, true);
                countPersonFields = 3;
                preparedInsertPersonStatement.setString(1, demoLabwork.getAuthor().getName());
                preparedInsertPersonStatement.setString(2, demoLabwork.getAuthor().getEyeColor().toString());
                if (demoLabwork.getAuthor().getLocation() != null) {
                    preparedInsertPersonStatement.setLong(countPersonFields, locationId);
                    countPersonFields += 1;
                }
                if (demoLabwork.getAuthor().getBirthday() != null) {
                    preparedInsertPersonStatement.setDate(countPersonFields, java.sql.Date.valueOf(demoLabwork.getAuthor().getBirthday()));
                    countPersonFields += 1;
                }
                if (demoLabwork.getAuthor().getHairColor() != null) {
                    preparedInsertPersonStatement.setString(countPersonFields, demoLabwork.getAuthor().getHairColor().toString());
                }

                if (preparedInsertPersonStatement.executeUpdate() == 0) throw new SQLException();
                ResultSet generatedPersonKeys = preparedInsertPersonStatement.getGeneratedKeys();
                if (generatedPersonKeys.next()) {
                    personId = generatedPersonKeys.getLong(1);
                } else throw new SQLException();
                Main.logger.log(Level.INFO, "Выполнен запрос insert_person");
            }

            String labworkSQLStatement = SQLRequests.insert_labwork_start;
            int countLabworkFields = 4;

            // INSERT LABWORK
            if (demoLabwork.getDifficulty() != null) {
                labworkSQLStatement += ", " + DatabaseManager.labworksTable_Difficulty;
                countLabworkFields += 1;
            }
            if (demoLabwork.getAuthor() != null) {
                labworkSQLStatement += ", " + DatabaseManager.labworksTable_PersonID;
                countLabworkFields += 1;
            }
            if (countLabworkFields == 4) {
                labworkSQLStatement += SQLRequests.insert_labwork_end_4values;
            } else {
                if (countLabworkFields == 5) {
                    labworkSQLStatement += SQLRequests.insert_labwork_end_5values;
                } else {
                        labworkSQLStatement += SQLRequests.insert_labwork_end_6values;
                }
            }

            preparedInsertLabworkStatement = databaseManager.getPreparedStatement(labworkSQLStatement, true);
            countLabworkFields = 5;

            preparedInsertLabworkStatement.setString(1, demoLabwork.getName());
            preparedInsertLabworkStatement.setTimestamp(2, Timestamp.valueOf(creationDate));
            preparedInsertLabworkStatement.setDouble(3, demoLabwork.getMinimalPoint());
            preparedInsertLabworkStatement.setLong(4, databaseUserHandler.getUserIdByUsername(user));

            if (demoLabwork.getDifficulty() != null) {
                preparedInsertLabworkStatement.setString(countLabworkFields, demoLabwork.getDifficulty().toString());
                countLabworkFields += 1;
            }
            if (demoLabwork.getAuthor() != null) {
                preparedInsertLabworkStatement.setLong(countLabworkFields, personId);
            }

            if (preparedInsertLabworkStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedLabworkKeys = preparedInsertLabworkStatement.getGeneratedKeys();
            long labworkId;
            if (generatedLabworkKeys.next()) {
                labworkId = generatedLabworkKeys.getLong(1);
            } else throw new SQLException();
            Main.logger.log(Level.INFO, "Выполнен запрос insert_labwork");

            // INSERT COORDINATES
            preparedInsertCoordinateStatement = databaseManager.getPreparedStatement(SQLRequests.insert_coordinates, true);

            preparedInsertCoordinateStatement.setLong(1, labworkId);
            preparedInsertCoordinateStatement.setLong(2, demoLabwork.getCoordinates().getX());
            preparedInsertCoordinateStatement.setFloat(3, demoLabwork.getCoordinates().getY());
            if (preparedInsertCoordinateStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedCoordinateKeys = preparedInsertCoordinateStatement.getGeneratedKeys();
            long coordinatesId;
            if (generatedCoordinateKeys.next()) {
                coordinatesId = generatedCoordinateKeys.getLong(1);
            } else throw new SQLException();
            Main.logger.log(Level.INFO, "Выполнен запрос insert_coordinates");


            labwork = new Labwork(labworkId,
                    demoLabwork.getName(),
                    demoLabwork.getCoordinates(),
                    creationDate,
                    demoLabwork.getMinimalPoint(),
                    demoLabwork.getDifficulty(),
                    demoLabwork.getAuthor(),
                    user
            );
            databaseManager.commit();
            return labwork;
        } catch (SQLException e) {
            Main.logger.log(Level.SEVERE, "Ошибка при выполнении группы запросов на добавление нового объекта");
            databaseManager.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedInsertLocationStatement);
            databaseManager.closePreparedStatement(preparedInsertPersonStatement);
            databaseManager.closePreparedStatement(preparedInsertLabworkStatement);
            databaseManager.closePreparedStatement(preparedInsertCoordinateStatement);
            databaseManager.setNormalMode();
        }
    }

    public void updateLabworkById(long labworkId, DemoLabwork demoLabwork) throws DatabaseHandlingException {
        PreparedStatement preparedUpdateLabworkNameByIdStatement = null;
        PreparedStatement preparedUpdateLabworkMinimalPointByIdStatement = null;
        PreparedStatement preparedUpdateLabworkDifficultyByIdStatement = null;
        PreparedStatement preparedUpdateCoordinatesByLabworkIdStatement = null;
        PreparedStatement preparedUpdatePersonByIdStatement = null;
        PreparedStatement preparedUpdateLocationByIdStatement = null;

        try {
            databaseManager.setCommitMode();
            databaseManager.setSavepoint();

            preparedUpdateLabworkNameByIdStatement = databaseManager.getPreparedStatement(SQLRequests.update_labwork_name_by_ID, false);
            preparedUpdateLabworkMinimalPointByIdStatement = databaseManager.getPreparedStatement(SQLRequests.update_labwork_MinimalPoint_by_ID, false);
            preparedUpdateLabworkDifficultyByIdStatement = databaseManager.getPreparedStatement(SQLRequests.update_labwork_Difficulty_by_ID, false);
            preparedUpdateCoordinatesByLabworkIdStatement = databaseManager.getPreparedStatement(SQLRequests.update_coordinates_by_labwork_id, false);



            if (demoLabwork.getName() != null) {
                preparedUpdateLabworkNameByIdStatement.setString(1, demoLabwork.getName());
                preparedUpdateLabworkNameByIdStatement.setLong(2, labworkId);
                if (preparedUpdateLabworkNameByIdStatement.executeUpdate() == 0) throw new SQLException();
                Main.logger.log(Level.INFO, "Выполнен запрос update_labwork_name_by_ID");
            }

            if (demoLabwork.getCoordinates() != null) {
                preparedUpdateCoordinatesByLabworkIdStatement.setLong(1, demoLabwork.getCoordinates().getX());
                preparedUpdateCoordinatesByLabworkIdStatement.setFloat(2, demoLabwork.getCoordinates().getY());
                preparedUpdateCoordinatesByLabworkIdStatement.setLong(3, labworkId);
                if (preparedUpdateCoordinatesByLabworkIdStatement.executeUpdate() == 0) throw new SQLException();
                Main.logger.log(Level.INFO, "Выполнен запрос update_coordinates_by_labwork_id");
            }

            if (demoLabwork.getMinimalPoint() != -1) {
                preparedUpdateLabworkMinimalPointByIdStatement.setDouble(1, demoLabwork.getMinimalPoint());
                preparedUpdateLabworkMinimalPointByIdStatement.setLong(2, labworkId);
                if (preparedUpdateLabworkMinimalPointByIdStatement.executeUpdate() == 0) throw new SQLException();
                Main.logger.log(Level.INFO, "Выполнен запрос update_labwork_MinimalPoint_by_ID");
            }

            if (demoLabwork.getDifficulty() != null) {
                preparedUpdateLabworkDifficultyByIdStatement.setString(1, demoLabwork.getDifficulty().toString());
                preparedUpdateLabworkDifficultyByIdStatement.setLong(2, labworkId);
                if (preparedUpdateLabworkDifficultyByIdStatement.executeUpdate() == 0) throw new SQLException();
                Main.logger.log(Level.INFO, "Выполнен запрос update_labwork_Difficulty_by_ID");
            }

            if (demoLabwork.getAuthor() != null) {
                String personSQLStatement = SQLRequests.update_person_by_id_start;
                if (demoLabwork.getAuthor().getLocation() != null) {
                    personSQLStatement += ", " + DatabaseManager.personTable_LocationID + " = ?";
                }
                if (demoLabwork.getAuthor().getBirthday() != null) {
                    personSQLStatement += ", " + DatabaseManager.personTable_Birthday + " = ?";
                }
                if (demoLabwork.getAuthor().getHairColor() != null) {
                    personSQLStatement += ", " + DatabaseManager.personTable_HairColor + " = ?";
                }
                personSQLStatement += SQLRequests.update_person_by_id_end;
                preparedUpdatePersonByIdStatement = databaseManager.getPreparedStatement(personSQLStatement, false);

                long personId = getPersonIDByLabworkID(labworkId);
                preparedUpdatePersonByIdStatement.setString(1, demoLabwork.getAuthor().getName());
                preparedUpdatePersonByIdStatement.setString(2, demoLabwork.getAuthor().getEyeColor().toString());
                int countPersonFields = 3;

                if (demoLabwork.getAuthor().getLocation() != null) {
                    int countLocationFields = 1;
                    if (demoLabwork.getAuthor().getLocation().getName() != null) {
                        preparedUpdateLocationByIdStatement = databaseManager.getPreparedStatement(SQLRequests.update_location_by_id, false);
                        preparedUpdateLocationByIdStatement.setString(countLocationFields, demoLabwork.getAuthor().getLocation().getName());
                        countLocationFields += 1;
                    } else {
                        preparedUpdateLocationByIdStatement = databaseManager.getPreparedStatement(SQLRequests.update_location_by_id_without_name, false);
                    }
                    preparedUpdateLocationByIdStatement.setFloat(countLocationFields, demoLabwork.getAuthor().getLocation().getX());
                    countLocationFields += 1;
                    preparedUpdateLocationByIdStatement.setDouble(countLocationFields, demoLabwork.getAuthor().getLocation().getY());
                    countLocationFields += 1;
                    preparedUpdateLocationByIdStatement.setLong(countLocationFields, getLocationIDByPersonID(personId));
                    if (preparedUpdateLocationByIdStatement.executeUpdate() == 0) throw new SQLException();
                    Main.logger.log(Level.INFO, "Выполнен запрос update_location_by_id");
                    preparedUpdatePersonByIdStatement.setLong(countPersonFields, getLocationIDByPersonID(personId));
                    countPersonFields += 1;
                }
                if (demoLabwork.getAuthor().getBirthday() != null) {
                    preparedUpdatePersonByIdStatement.setDate(countPersonFields, Date.valueOf(demoLabwork.getAuthor().getBirthday()));
                    countPersonFields += 1;
                }
                if (demoLabwork.getAuthor().getHairColor() != null) {
                    preparedUpdatePersonByIdStatement.setString(countPersonFields, demoLabwork.getAuthor().getHairColor().toString());
                    countPersonFields += 1;
                }

                preparedUpdatePersonByIdStatement.setLong(countPersonFields, personId);
                if (preparedUpdatePersonByIdStatement.executeUpdate() == 0) throw new SQLException();
                Main.logger.log(Level.INFO, "Выполнен запрос update_person_by_id");
            }

            databaseManager.commit();

        } catch (SQLException e) {
            Main.logger.log(Level.SEVERE, "Ошибка при выполнении группы запросов на обновление объекта");
            databaseManager.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedUpdateLabworkNameByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateLabworkMinimalPointByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateLabworkDifficultyByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateCoordinatesByLabworkIdStatement);
            databaseManager.closePreparedStatement(preparedUpdatePersonByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateLocationByIdStatement);
            databaseManager.setNormalMode();
        }
    }


    public void deleteLabworkById(long labworkId) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteCoordinatesByLabworkId = null;
        PreparedStatement preparedDeleteLabworkByIdStatement = null;
        PreparedStatement preparedDeletePersonByIDStatement = null;
        PreparedStatement preparedDeleteLocationByIdStatement = null;
        try {
            Long personID = getPersonIDByLabworkID(labworkId);
            Long locationID = 0L;
            if (personID != 0) {
                locationID = getLocationIDByPersonID(personID);
            }

            preparedDeleteCoordinatesByLabworkId = databaseManager.getPreparedStatement(SQLRequests.delete_coordinates_by_labwork_id, false);
            preparedDeleteCoordinatesByLabworkId.setLong(1, labworkId);
            if (preparedDeleteCoordinatesByLabworkId.executeUpdate() == 0) throw new SQLException();
            Main.logger.log(Level.INFO, "Выполнен запрос delete_coordinates_by_labwork_id");

            preparedDeleteLabworkByIdStatement = databaseManager.getPreparedStatement(SQLRequests.delete_labwork_by_ID, false);
            preparedDeleteLabworkByIdStatement.setLong(1, labworkId);
            if (preparedDeleteLabworkByIdStatement.executeUpdate() == 0) throw new SQLException();
            Main.logger.log(Level.INFO, "Выполнен запрос delete_labwork_by_ID");

            if (personID != 0) {
                preparedDeletePersonByIDStatement = databaseManager.getPreparedStatement(SQLRequests.delete_person_by_id, false);
                preparedDeletePersonByIDStatement.setLong(1, personID);
                if (preparedDeletePersonByIDStatement.executeUpdate() == 0) throw new SQLException();
                Main.logger.log(Level.INFO, "Выполнен запрос delete_person_by_id");
            }

            if (locationID != 0) {
                preparedDeleteLocationByIdStatement = databaseManager.getPreparedStatement(SQLRequests.delete_location_by_id, false);
                preparedDeleteLocationByIdStatement.setLong(1, locationID);
                if (preparedDeleteLocationByIdStatement.executeUpdate() == 0) throw new SQLException();
                Main.logger.log(Level.INFO, "Выполнен запрос delete_location_by_id");
            }
        } catch (SQLException throwables) {
            Main.logger.log(Level.SEVERE, "Произошла ошибка при выполнении запроса delete");
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedDeleteCoordinatesByLabworkId);
            databaseManager.closePreparedStatement(preparedDeleteLabworkByIdStatement);
            databaseManager.closePreparedStatement(preparedDeletePersonByIDStatement);
            databaseManager.closePreparedStatement(preparedDeleteLocationByIdStatement);
        }
    }


    public boolean checkLabworkUserId(long labworkId, User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectLabworkByIdAndUserIdStatement = null;
        try {
            preparedSelectLabworkByIdAndUserIdStatement = databaseManager.getPreparedStatement(SQLRequests.select_labwork_by_id_and_user_id, false);
            preparedSelectLabworkByIdAndUserIdStatement.setLong(1, labworkId);
            preparedSelectLabworkByIdAndUserIdStatement.setLong(2, databaseUserHandler.getUserIdByUsername(user));
            ResultSet resultSet = preparedSelectLabworkByIdAndUserIdStatement.executeQuery();
            Main.logger.log(Level.INFO, "Выполнен запрос select_labwork_by_id_and_user_id");
            return !resultSet.next();
        } catch (SQLException exception) {
            Main.logger.log( Level.SEVERE, "Произошла ошибка при выполнении запроса select_labwork_by_id_and_user_id");
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedSelectLabworkByIdAndUserIdStatement);
        }
    }

    public void clearCollection() throws DatabaseHandlingException {
        TreeSet<Labwork> labworks = getCollection();
        for (Labwork lb : labworks) {
            deleteLabworkById(lb.getId());
        }
    }

    private Long getPersonIDByLabworkID (long labworkID) throws SQLException {
        Long personID;
        PreparedStatement preparedSelectLabworkByIdStatement = null;

        try {
            preparedSelectLabworkByIdStatement = databaseManager.getPreparedStatement(SQLRequests.select_labwork_by_id, false);
            preparedSelectLabworkByIdStatement.setLong(1, labworkID);
            ResultSet resultSet = preparedSelectLabworkByIdStatement.executeQuery();
            Main.logger.log(Level.INFO, "Выполнен запрос select_labwork_by_id");
            if (resultSet.next()) {
                personID = Long.valueOf(resultSet.getLong(DatabaseManager.labworksTable_PersonID));
            } else throw new SQLException();
        } catch (SQLException throwables) {
            Main.logger.log(Level.SEVERE, "Ошибка при выполнении запроса select_labwork_by_id");
            throw new SQLException(throwables);
        }  finally {
            databaseManager.closePreparedStatement(preparedSelectLabworkByIdStatement);
        }
        return personID;
    }

    private Person getPersonByID (long personID) throws SQLException {
        Person person;
        PreparedStatement preparedSelectPersonByIdStatement = null;

        try {
            preparedSelectPersonByIdStatement =
                    databaseManager.getPreparedStatement(SQLRequests.select_person_by_id, false);
            preparedSelectPersonByIdStatement.setLong(1, personID);
            ResultSet resultSet = preparedSelectPersonByIdStatement.executeQuery();
            Main.logger.log(Level.INFO, "Выполнен запрос select_person_by_id");
            if (resultSet.next()) {
                String personName = resultSet.getString(DatabaseManager.personTable_PersonName);
                LocalDate birthday = null;
                if (resultSet.getDate(DatabaseManager.personTable_Birthday) != null) {
                    birthday = resultSet.getDate(DatabaseManager.personTable_Birthday).toLocalDate();
                }
                Color eyeColor = Color.valueOf(resultSet.getString(DatabaseManager.personTable_EyeColor));
                data.hair.Color hairColor = null;
                if (resultSet.getString(DatabaseManager.personTable_HairColor) != null) {
                    hairColor = data.hair.Color.valueOf(resultSet.getString(DatabaseManager.personTable_HairColor));
                }
                Location location = null;
                if (resultSet.getString(DatabaseManager.personTable_LocationID) != null) {
                    location = getLocationByID(resultSet.getLong(DatabaseManager.personTable_LocationID));
                }
                person = new Person(personName, birthday, eyeColor, hairColor, location);
            } else throw new SQLException();
        } catch (SQLException e) {
            Main.logger.log(Level.SEVERE, "Ошибка при выполнении запроса select_person_by_id");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedSelectPersonByIdStatement);
        }
        return person;
    }

    private Long getLocationIDByPersonID (long personID) throws SQLException {
        Long locationID;
        PreparedStatement preparedSelectPersonByIdStatement = null;

        try {
            preparedSelectPersonByIdStatement = databaseManager.getPreparedStatement(SQLRequests.select_person_by_id, false);
            preparedSelectPersonByIdStatement.setLong(1, personID);
            ResultSet resultSet = preparedSelectPersonByIdStatement.executeQuery();
            Main.logger.log(Level.INFO, "Выполнен запрос select_person_by_id");
            if (resultSet.next()) {
                locationID = resultSet.getLong(DatabaseManager.personTable_LocationID);
            } else throw new SQLException();
        } catch (SQLException e) {
            Main.logger.log(Level.SEVERE, "Ошибка при выполнении запроса select_labwork_by_id");
            throw new SQLException(e);
        }  finally {
            databaseManager.closePreparedStatement(preparedSelectPersonByIdStatement);
        }
        return locationID;
    }

    private Location getLocationByID (long locationID) throws SQLException {
        Location location;
        PreparedStatement preparedSelectLocationByIdStatement = null;

        try {
            preparedSelectLocationByIdStatement = databaseManager.getPreparedStatement(SQLRequests.select_location_by_id, false);
            preparedSelectLocationByIdStatement.setLong(1, locationID);
            ResultSet resultSet = preparedSelectLocationByIdStatement.executeQuery();
            Main.logger.log(Level.INFO, "Выполнен запрос select_location_by_id");
            if (resultSet.next()) {
                String locationName = null;
                if (resultSet.getString(DatabaseManager.locationTable_LocationName) != null) {
                    locationName = resultSet.getString(DatabaseManager.locationTable_LocationName);
                }
                location = new Location(resultSet.getFloat(DatabaseManager.locationTable_X_LocationCoordinate),
                        resultSet.getDouble(DatabaseManager.locationTable_Y_LocationCoordinate),
                        locationName);
            } else throw new SQLException();
        } catch (SQLException e) {
            Main.logger.log(Level.SEVERE, "Ошибка при выполнении запроса select_location_by_id");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedSelectLocationByIdStatement);
        }
        return location;
    }

    private Coordinates getCoordinatesByLabworkID(long labworkID) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedSelectCoordinatesByLabworkIdStatement = null;
        try {
            preparedSelectCoordinatesByLabworkIdStatement =
                    databaseManager.getPreparedStatement(SQLRequests.select_coordinates_by_labwork_id, false);
            preparedSelectCoordinatesByLabworkIdStatement.setLong(1, labworkID);
            ResultSet resultSet = preparedSelectCoordinatesByLabworkIdStatement.executeQuery();
            Main.logger.log(Level.INFO, "Выполнен запрос select_coordinates_by_labwork_id");
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getLong(DatabaseManager.coordinatesTable_X_Coordinate),
                        resultSet.getFloat(DatabaseManager.coordinatesTable_Y_Coordinate)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            Main.logger.log(Level.SEVERE, "Ошибка при выполнении запроса select_coordinates_by_labwork_id");
            throw new SQLException(exception);
        } finally {
            databaseManager.closePreparedStatement(preparedSelectCoordinatesByLabworkIdStatement);
        }
        return coordinates;
    }
}
