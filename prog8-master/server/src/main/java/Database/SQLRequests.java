package Database;


class SQLRequests {

    // Таблица LABWORKS
    protected static final String select_all_labworks = "SELECT * FROM " + DatabaseManager.labworkTableName;

    protected static final String select_labwork_by_id = select_all_labworks + " WHERE " + DatabaseManager.labworkTable_id + " = ?";

    protected static final String select_labwork_by_id_and_user_id = select_labwork_by_id +
            " AND " + DatabaseManager.labworksTable_userID + " = ?";

    protected static final String insert_labwork_start = "INSERT INTO " +
            DatabaseManager.labworkTableName + " (" +
            DatabaseManager.labworkTable_name + ", " +
            DatabaseManager.labworkTable_CreationDate + ", " +
            DatabaseManager.labworkTable_MinimalPoint + ", " +
            //DatabaseManager.labworksTable_Difficulty + ", " +
            //DatabaseManager.labworksTable_PersonID + ", " +
            DatabaseManager.labworksTable_userID;
            //+ ") VALUES (?, ?, ?, ?, ?, ?)";

    protected static final String insert_labwork_end_4values = ") VALUES (?, ?, ?, ?)";

    protected static final String insert_labwork_end_5values = ") VALUES (?, ?, ?, ?, ?)";

    protected static final String insert_labwork_end_6values = ") VALUES (?, ?, ?, ?, ?, ?)";

    protected static final String delete_labwork_by_ID = "DELETE FROM " + DatabaseManager.labworkTableName +
            " WHERE " + DatabaseManager.labworkTable_id + " = ?";

    protected static final String update_labwork_name_by_ID = "UPDATE " + DatabaseManager.labworkTableName +
            " SET " + DatabaseManager.labworkTable_name + " = ? " + "WHERE " + DatabaseManager.labworkTable_id + " = ?";

    protected static final String update_labwork_MinimalPoint_by_ID = "UPDATE " + DatabaseManager.labworkTableName +
            " SET " + DatabaseManager.labworkTable_MinimalPoint + " = ? " + "WHERE " + DatabaseManager.labworkTable_id + " = ?";

    protected static final String update_labwork_Difficulty_by_ID = "UPDATE " + DatabaseManager.labworkTableName +
            " SET " + DatabaseManager.labworksTable_Difficulty + " = ? " + "WHERE " + DatabaseManager.labworkTable_id + " = ?";



    // Таблица COORDINATES
    protected static final String select_all_coordinates = "SELECT * FROM " + DatabaseManager.coordinatesTableName;

    protected static final String select_coordinates_by_labwork_id = select_all_coordinates +
            " WHERE " + DatabaseManager.coordinatesTable_LabworkID + " = ?";

    protected static final String insert_coordinates = "INSERT INTO " +
            DatabaseManager.coordinatesTableName + " (" +
            DatabaseManager.coordinatesTable_LabworkID + ", " +
            DatabaseManager.coordinatesTable_X_Coordinate + ", " +
            DatabaseManager.coordinatesTable_Y_Coordinate + ") VALUES (?, ?, ?)";

    protected static final String update_coordinates_by_labwork_id = "UPDATE " + DatabaseManager.coordinatesTableName +
            " SET " + DatabaseManager.coordinatesTable_X_Coordinate + " = ?, " +
            DatabaseManager.coordinatesTable_Y_Coordinate + " = ? " + "WHERE " +
            DatabaseManager.coordinatesTable_LabworkID + " = ?";

    protected static final String delete_coordinates_by_labwork_id = "DELETE FROM " + DatabaseManager.coordinatesTableName +
            " WHERE " + DatabaseManager.coordinatesTable_LabworkID + " = ?";



    // Таблица PERSON
    protected static final String select_all_persons = "SELECT * FROM " + DatabaseManager.personTableName;

    protected static final String select_person_by_id = select_all_persons + " WHERE " + DatabaseManager.personTable_PersonID + " = ?";

    // PERSON INSERT
    protected static final String insert_person_start = "INSERT INTO " + DatabaseManager.personTableName
            + " (" + DatabaseManager.personTable_PersonName + ", " +
            //DatabaseManager.personTable_Birthday + ", " +
            DatabaseManager.personTable_EyeColor;
            //DatabaseManager.personTable_HairColor + ", " +
            //DatabaseManager.personTable_LocationID + ") VALUES (?, ?, ?, ?, ?)";

    protected static final String insert_person_end_2values = ") VALUES (?, ?)";

    protected static final String insert_person_end_3values = ") VALUES (?, ?, ?)";

    protected static final String insert_person_end_4values = ") VALUES (?, ?, ?, ?)";

    protected static final String insert_person_end_5values = ") VALUES (?, ?, ?, ?, ?)";

    // PERSON update
    protected static final String update_person_by_id_start = "UPDATE " + DatabaseManager.personTableName +
            " SET " + DatabaseManager.personTable_PersonName + " = ?" +
            //DatabaseManager.personTable_Birthday + " = ?, " +
            ", " + DatabaseManager.personTable_EyeColor + " = ?";
            //DatabaseManager.personTable_HairColor + " = ?, " +
            //DatabaseManager.personTable_LocationID + " = ?" +
            //" WHERE " + DatabaseManager.personTable_PersonID + " = ?";

    protected static final String update_person_by_id_end = " WHERE " + DatabaseManager.personTable_PersonID + " = ?";

    protected static final String delete_person_by_id = "DELETE FROM " + DatabaseManager.personTableName +
            " WHERE " + DatabaseManager.personTable_PersonID + " = ?";


    // Таблица LOCATION
    protected static final String select_all_locations = "SELECT * FROM " + DatabaseManager.locationTableName;

    protected static final String select_location_by_id = select_all_locations + " WHERE "
            + DatabaseManager.locationTable_LocationID + " = ?";

    protected static final String insert_location = "INSERT INTO " + DatabaseManager.locationTableName +
            " (" + DatabaseManager.locationTable_LocationName + ", " +
            DatabaseManager.locationTable_X_LocationCoordinate + ", " +
            DatabaseManager.locationTable_Y_LocationCoordinate + ") VALUES (?, ?, ?)";

    protected static final String insert_location_without_name = "INSERT INTO " + DatabaseManager.locationTableName +
            " (" + DatabaseManager.locationTable_X_LocationCoordinate + ", " +
            DatabaseManager.locationTable_Y_LocationCoordinate + ") VALUES (?, ?)";

    protected static final String update_location_by_id = "UPDATE " + DatabaseManager.locationTableName +
            " SET " + DatabaseManager.locationTable_LocationName + " = ?, " +
            DatabaseManager.locationTable_X_LocationCoordinate + " = ?, " +
            DatabaseManager.locationTable_Y_LocationCoordinate + " = ?" +
            " WHERE " + DatabaseManager.locationTable_LocationID + " = ?";

    protected static final String update_location_by_id_without_name = "UPDATE " + DatabaseManager.locationTableName +
            " SET " + DatabaseManager.locationTable_X_LocationCoordinate + " = ?, " +
            DatabaseManager.locationTable_Y_LocationCoordinate + " = ?" +
            " WHERE " + DatabaseManager.locationTable_LocationID + " = ?";

    protected static final String delete_location_by_id = "DELETE FROM " + DatabaseManager.locationTableName +
            " WHERE " + DatabaseManager.locationTable_LocationID + " = ?";

}
