package Help;

/**
 * Represent server response.
 */
public class IntegrationResponse {

    // Code of result.
    private int code;
    // Type of Entity.
    private Entity entity;
    // Type of operation.
    private Operation operation;
    // Description.
    private String description;
    // Json with data by sql execution.
    private String data;

    // We need this empty constructor to serialization by ObjectMapper.
    public IntegrationResponse() {
        super();
    }

    public IntegrationResponse(int code, String description, String data, Entity entity, Operation operation) {
        this.code = code;
        this.description = description;
        this.data = data;
        this.entity = entity;
        this.operation = operation;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}