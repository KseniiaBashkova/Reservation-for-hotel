package Server.Service;

import Server.Model.IEntity;
import java.sql.Connection;

public interface IService {

    //Get all rows from database
    String getAll(IEntity entity, Connection connection);

    //Get row by id
    String getById(IEntity entity, Connection connection);

    // Delete data from database
    String deleteById(IEntity entity, Connection connection);
}
