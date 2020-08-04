package Server.Service;

import Help.Entity;
import Help.IntegrationDBHelper;
import Help.Operation;
import Server.Controller.ServerController;
import Server.Model.IEntity;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Class to work with database and generate method for call from receive data
 */
public class BaseService extends DecisionHelper implements IService {

    protected String className;

    BaseService() {
        super();
        this.className = this.getClass().getSimpleName().replace("Service", "");
    }

    /**
     * Create record to Db.
     *
     * @param entity
     * @return String
     */
    public String create(IEntity entity, Connection connection) {

        // Get Map with Object's fields and values.
        Map<String, Object> fieldNameToFieldValueMap = generateFieldMap(entity);

        StringBuilder attribute = new StringBuilder();
        StringBuilder values = new StringBuilder();
        // Append attribute by maps keys and values by maps values
        fieldNameToFieldValueMap.keySet().stream().filter((key) -> (!key.equals("id") && !key.equals("roomnumber") && !key.equals("customerpassport") && !key.equals("total"))).peek((key) -> attribute.append(key).append(",")).forEachOrdered((key) -> {
            values.append("'").append(fieldNameToFieldValueMap.get(key)).append("',");
        });

        deleteCommaFromEnd(attribute);
        deleteCommaFromEnd(values);

        String sql = "INSERT INTO public." + this.className.toLowerCase() + "(" + attribute.toString() + ") " + "VALUES (" + values.toString() + ");";

        try {
            Statement stm = connection.createStatement();
            stm.execute(sql);
            stm.close();
            // create answer to Client
            return this.createAnswer(4, this.className + " was created.", "", Operation.CREATE, getTypeOfEntity());
        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(2, "Error during create the " + this.className.toLowerCase() + " " + ex.getMessage() + ".", "", Operation.CREATE, getTypeOfEntity());
        }
    }

    String getCorrectConditionToUpdateOrFindEntity(IEntity entity) {
        return " WHERE id = " + entity.getId() + ";";
    }

    /**
     * Create sql for update row in database
     *
     * @param entity
     * @return String
     */
    public String updateBySpecificAttribute(IEntity entity, Connection connection) {

        Map<String, Object> fieldNameToFieldValueMap = generateFieldMap(entity);
        StringBuilder valuesToUpdate = new StringBuilder();
        fieldNameToFieldValueMap.keySet().stream().filter((key) -> (!key.equals("id") && !key.equals("roomnumber") && !key.equals("customerpassport")  && !key.equals("total"))).forEachOrdered((key) -> {
            valuesToUpdate.append(key).append(" = " + "'").append(fieldNameToFieldValueMap.get(key)).append("',");
        });

        deleteCommaFromEnd(valuesToUpdate);

        String sql = "UPDATE public." + this.className.toLowerCase() + " SET " + valuesToUpdate.toString() + this.getCorrectConditionToUpdateOrFindEntity(entity);

        try {
            Statement stm = connection.createStatement();
            stm.execute(sql);
            stm.close();
            return this.createAnswer(4, this.className + " was update.", "", Operation.UPDATE, getTypeOfEntity());
        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(2, "Error during update the " + this.className.toLowerCase() + " " + ex.getLocalizedMessage() + ".", "", Operation.UPDATE, getTypeOfEntity());
        }
    }
    
     /**
     * Update record by Id.
     *
     * @param entity
     * @param connection
     * @return String
     */
    public String updateById(IEntity entity, Connection connection) {

        Map<String, Object> fieldNameToFieldValueMap = generateFieldMap(entity);
        StringBuilder valuesToUpdate = new StringBuilder();
        fieldNameToFieldValueMap.keySet().stream().filter((key) -> (!key.equals("id") && !key.equals("roomnumber") && !key.equals("customerpassport") && !key.equals("total"))).forEachOrdered((key) -> {
            valuesToUpdate.append(key).append(" = " + "'").append(fieldNameToFieldValueMap.get(key)).append("',");
        });

        deleteCommaFromEnd(valuesToUpdate);

        String sql = "UPDATE public." + this.className.toLowerCase() + " SET " + valuesToUpdate.toString() + " WHERE id = " + fieldNameToFieldValueMap.get("id");

        try {
            Statement stm = connection.createStatement();
            stm.execute(sql);
            stm.close();
            return this.createAnswer(4, this.className + " was update.", "", Operation.UPDATE, getTypeOfEntity());
        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(2, "Error during update the " + this.className.toLowerCase() + " " + ex.getLocalizedMessage() + ".", "", Operation.UPDATE, getTypeOfEntity());
        }
    }

    /**
     * Get all row from database
     *
     * @return String
     */
    @Override
    public String getAll(IEntity entity, Connection connection) {

        String SQL = "SELECT * FROM public." + this.className;

        try {
            ArrayList<IEntity> rows = getRows(SQL, connection);
            if (rows.isEmpty()) {
                return createAnswer(3, "Does not exist any " + this.className.toLowerCase() + "s.", "", Operation.GET_ALL, getTypeOfEntity());
            } else {
                return createAnswer(1, this.className + "s has been got", this.objectMapper.writeValueAsString(rows), Operation.GET_ALL, getTypeOfEntity());
            }
        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(2, "Error during getting all " + this.className, "", Operation.GET_ALL, getTypeOfEntity());
        }
    }

    /**
     * Get row by Specific attribute
     *
     * @param entity
     * @return String
     */
    public String getBySpecificAttribute(IEntity entity, Connection connection) {

        String SQL = "SELECT * FROM public." + this.className + this.getCorrectConditionToUpdateOrFindEntity(entity);
        try {
            ArrayList<IEntity> rows = getRows(SQL, connection);
            if (rows.isEmpty()) {
                return createAnswer(3, "Does not exist any " + this.className.toLowerCase() + "s.", "", Operation.GET_BY_SPECIFIC_ATTRIBUTE, getTypeOfEntity());
            } else {
                return createAnswer(1, this.className + "s has been got", this.objectMapper.writeValueAsString(rows), Operation.GET_BY_SPECIFIC_ATTRIBUTE, getTypeOfEntity());
            }
        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(2, "Error during getting by attribute", "", Operation.GET_BY_SPECIFIC_ATTRIBUTE, getTypeOfEntity());
        }
    }

    protected ArrayList<IEntity> getRows(String SQL, Connection connection) throws SQLException, IOException {
        ArrayList<IEntity> rows = new ArrayList<>();
        IntegrationDBHelper integrationDBHelper = this.getBasicData(SQL, connection);
        //fetch out rows
        assert integrationDBHelper != null;
        while (integrationDBHelper.getResultSet().next()) {
            rows.add(getRow(integrationDBHelper));
        }
        integrationDBHelper.getStm().close();

        return rows;
    }

    /**
     * Get row by Id
     *
     * @param entity
     * @return String
     */
    @Override
    public String getById(IEntity entity, Connection connection) {

        String SQL = "SELECT * FROM public." + this.className + " WHERE id = " + entity.getId();
        try {
            IntegrationDBHelper integrationDBHelper = this.getBasicData(SQL, connection);
            assert integrationDBHelper != null;
            if (integrationDBHelper.getResultSet().next()) {
                return createAnswer(1, this.className + " has been got", objectMapper.writeValueAsString(new ArrayList<>(Arrays.asList(getRow(integrationDBHelper)))), Operation.GET_BY_ID, getTypeOfEntity());
            }

            return createAnswer(3, this.className + " has not been found", "", Operation.GET_BY_ID, getTypeOfEntity());
        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(2, "Error during getting by id", "", Operation.GET_BY_ID, getTypeOfEntity());
        }
    }

    /**
     * Delete row by id
     *
     * @param entity
     * @return String
     */
    @Override
    public String deleteById(IEntity entity, Connection connection) {
        String SQL = "DELETE FROM public." + this.className + " WHERE id = " + entity.getId();

        try {
            Statement stm = connection.createStatement();
            stm.execute(SQL);
            stm.close();
            return createAnswer(4, this.className + " has been deleted", "", Operation.DELETE, getTypeOfEntity());
        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(3, this.className + " has not been found", "", Operation.DELETE, getTypeOfEntity());
        }
    }

    /**
     * Delete row by id
     *
     * @param entity
     * @return String
     */
    public String deleteBySpecificAtribute(IEntity entity, Connection connection) {
        String SQL = "DELETE FROM public." + this.className + this.getCorrectConditionToUpdateOrFindEntity(entity);

        try {
            Statement stm = connection.createStatement();
            stm.execute(SQL);
            stm.close();
            return createAnswer(4, this.className + " has been deleted", "", Operation.DELETE, getTypeOfEntity());
        } catch (Exception ex) {
            return createAnswer(3, this.className + " has not been found", "", Operation.DELETE, getTypeOfEntity());
        }
    }


    Entity getTypeOfEntity() {
        switch (this.className) {
            case "Room":
                return Entity.ROOM;
            case "Reservation":
                return Entity.RESERVATION;
            case "Reception":
                return Entity.RECEPTION;
            case "Customer":
                return Entity.CUSTOMER;
        }

        return null;
    }

    protected IntegrationDBHelper getBasicData(String SQL, Connection connection) {

        try {
            Statement stm = connection.createStatement();

            //query
            ResultSet resultSet;
            boolean isReturningRows = stm.execute(SQL);
            if (isReturningRows)
                resultSet = stm.getResultSet();
            else
                return null;

            //get metadata
            ResultSetMetaData meta;
            meta = resultSet.getMetaData();

            int colCount = meta.getColumnCount();
            ArrayList<String> cols = new ArrayList<>();
            for (int index = 1; index <= colCount; index++) {
                cols.add(meta.getColumnName(index));
            }
            return new IntegrationDBHelper(resultSet, cols, stm);
        } catch (Exception ex) {
            setLog(ex);
            return null;
        }
    }

    protected IEntity getRow(IntegrationDBHelper integrationDBHelper) throws SQLException, IOException {

        StringBuilder row = new StringBuilder();
        for (String colName : integrationDBHelper.getColumns()) {
            Object val = integrationDBHelper.getResultSet().getObject(colName);
            row.append("\"").append(colName).append("\":\"").append(val).append("\",");
        }
        if (row.length() > 0) {
            row.deleteCharAt(row.length() - 1);
        }

        return ServerController.getCorrectEntity(this.className, "{" + row.toString() + "}");
    }
}