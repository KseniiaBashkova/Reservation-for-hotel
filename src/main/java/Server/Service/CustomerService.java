package Server.Service;

import Help.Operation;
import Server.Model.Customer;
import Server.Model.IEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Represent reservations service to connection with database.
 */
public class CustomerService extends BaseService {

    static CustomerService singleInstance;

    public static BaseService getService() {

        if (singleInstance == null) {
            singleInstance = new CustomerService();
        }

        return singleInstance;
    }
    
    /**
     * Get all row from database
     *
     * @return String
     */
    @Override
    public String getAll(IEntity entity, Connection connection) {

        String SQL = "SELECT customer.id, first_name, second_name, passport, day_of_birth, phone_number, email, COUNT(reservation.id) AS total\n" +
                     " FROM public.customer\n" +
                     " LEFT JOIN reservation ON customer.id = reservation.customer_id GROUP BY customer.id ORDER BY total DESC;";

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
     * Get correct attribute
     *
     * @param customer
     * @return
     */
    @Override
    public String getCorrectConditionToUpdateOrFindEntity(IEntity customer) {
        return " WHERE passport = '" + ((Customer) customer).getPassport() + '\'';
    }
}
