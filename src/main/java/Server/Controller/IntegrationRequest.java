package Server.Controller;

import Server.Model.IEntity;

/**
 * Represent request from client.
 */
public class IntegrationRequest {

    // Correct classname.
    private String className;
    // Correct method name.
    private String methodName;
    // Correct IEntity object.
    private IEntity entity;

    // We need this empty constructor to serialization by ObjectMapper.
    public IntegrationRequest() {
        super();
    }

    public IntegrationRequest(String className, String methodName, IEntity entity) {
        this.className = className;
        this.methodName = methodName;
        this.entity = entity;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public IEntity getEntity() {
        return entity;
    }
}
