package Help.Exception;

/**
 * Represents exception during creation connection with db.
 */
public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String message) {
        super(message);
    }
}