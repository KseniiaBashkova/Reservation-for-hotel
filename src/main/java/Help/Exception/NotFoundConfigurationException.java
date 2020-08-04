package Help.Exception;

/**
 * Represents exception when file with configuration has not found.
 */
public class NotFoundConfigurationException extends RuntimeException {

    public NotFoundConfigurationException(String message) {
        super(message);
    }
}
