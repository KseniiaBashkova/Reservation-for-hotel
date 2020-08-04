package Server.Service;

import Help.Entity;
import Help.IntegrationResponse;
import Help.Operation;
import Server.Controller.IntegrationRequest;
import Server.Model.IEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DecisionHelper {

    private Logger logger = Logger.getLogger(this.getClass());
    ObjectMapper objectMapper;

    public DecisionHelper() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Select right method from class by method name
     *
     * @return String
     */
    public Method selectMethod(IntegrationRequest integrationRequest) {
        java.lang.reflect.Method method = null;
        try {

            Method[] methods = this.getClass().getMethods();
            for (Method tempMethod : methods) {
                if (tempMethod.getName().equals(integrationRequest.getMethodName())) {
                    method = tempMethod;
                }
            }

            assert method != null;
            return method;
        } catch (SecurityException ex) {
            setLog(ex);
            return null;
        }
    }

    /**
     * Invoke selected method
     *
     * @return String
     */
    public String methodInvoke(Method method, IEntity entity, Connection connection) {
        try {
            return (String) method.invoke(this.getClass().newInstance(), entity, connection);
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            setLog(ex);
        }
        return createAnswer(2, "Error during method invoke.", null, null, null);
    }

    /**
     * Generate Map with Object's fields and values.
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> generateFieldMap(IEntity entity) {
        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(entity.getClass().getDeclaredFields()));
        Map<String, Object> fieldNameToFieldValueMap = new HashMap<>();
        for (Field field : fields) {
            try {
                // Accessible default is false
                field.setAccessible(true);
                fieldNameToFieldValueMap.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return fieldNameToFieldValueMap;
    }

    /**
     * Delete comma from the end of StringBuilder
     *
     * @param processedString
     */
    public static void deleteCommaFromEnd(StringBuilder processedString) {
        if (processedString.length() > 0) {
            processedString.deleteCharAt(processedString.length() - 1);
        }
    }

    /**
     * Create response to client
     *
     * @return String
     */
    public String createAnswer(int code, String description, String data, Operation operation, Entity entity) {
        try {
            return objectMapper.writeValueAsString(new IntegrationResponse(code, description, data, entity, operation));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            setLog(e);
            return "";
        }
    }

    /**
     * Save logs
     *
     * @param ex
     */
    void setLog(Exception ex) {
        logger.error(ex.getMessage());
        System.out.println(ex.getMessage());
    }
}