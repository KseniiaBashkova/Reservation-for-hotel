package Help;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Represent structure to communicate with db.
 */
public class IntegrationDBHelper {

    // Set with data from db.
    private ResultSet resultSet;
    // Array of columns of table.
    private ArrayList<String> columns;
    // Statement to execute sql query.
    private Statement stm;

    public IntegrationDBHelper(ResultSet resultSet, ArrayList<String> columns, Statement stm) {
        this.columns = columns;
        this.resultSet = resultSet;
        this.stm = stm;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public Statement getStm() {
        return stm;
    }
}
