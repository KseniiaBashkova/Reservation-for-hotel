package Server;

import Help.Entity;
import Help.IntegrationResponse;
import Help.Operation;
import Server.Controller.IntegrationRequest;
import Server.Model.IEntity;
import Server.Model.Room;
import Server.Service.DecisionHelper;
import Server.Service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

public class DecisionHelperTest {



    @Test
    public void createMethod_GetCorrectMethod_GetAllMethod() {

        // Arrange
        IEntity room = new Room();
        RoomService decisionHelper = new RoomService();
        String exceptedMethodName = "getAll";
        String exceptedClassName = "RoomService";
        IntegrationRequest integrationRequest = new IntegrationRequest("Room", exceptedMethodName, room);

        // Act
        Method method = decisionHelper.selectMethod(integrationRequest);

        // Assertions
        Assertions.assertEquals(exceptedMethodName, method.getName());
        Assertions.assertEquals(exceptedClassName, decisionHelper.getClass().getSimpleName());
    }

    @Test
    public void createMethod_GetCorrectMethod_GetCreateMethod() {

        // Arrange
        IEntity room = new Room();
        RoomService decisionHelper = new RoomService();
        String exceptedMethodName = "create";
        String exceptedClassName = "RoomService";
        IntegrationRequest integrationRequest = new IntegrationRequest("Room", exceptedMethodName, room);

        // Act
        Method method = decisionHelper.selectMethod(integrationRequest);

        // Assertions
        Assertions.assertEquals(exceptedMethodName, method.getName());
        Assertions.assertEquals(exceptedClassName, decisionHelper.getClass().getSimpleName());
    }

    @Test
    public void generateFieldMap_GetCorrectMapWithObjectFields_GetRoomFields() {


        // Arrange
        Room room = new Room();
        room.setCapacity(20);
        room.setDescription("Description");
        room.setId(1);
        room.setPrice((float) 100);
        room.setRoom_number(20);
        room.setVip(true);

        // Act
        Map<String, Object> resultMap = DecisionHelper.generateFieldMap(room);

        // Assertions
        Assertions.assertEquals(resultMap.get("id"), room.getId());
        Assertions.assertEquals(resultMap.get("description"), room.getDescription());
        Assertions.assertEquals(resultMap.get("price"), room.getPrice());
        Assertions.assertEquals(resultMap.get("room_number"), room.getRoom_number());
        Assertions.assertEquals(resultMap.get("capacity"), room.getCapacity());
        Assertions.assertEquals(resultMap.get("vip"), room.isVip());
    }

    @Test
    public void deleteCommaFromEnd_DeleteCommaOnEnt_GetStringWithoutCommaOnEnd() {


        // Arrange
        StringBuilder stringWithComma = new StringBuilder();
        String strWithComma = "Test Test,";
        stringWithComma.append(strWithComma);

        // Act
        DecisionHelper.deleteCommaFromEnd(stringWithComma);

        // Assertions
        Assertions.assertNotEquals(strWithComma, stringWithComma.toString());
        Assertions.assertEquals(strWithComma.length() - 1, stringWithComma.toString().length());
    }

    @Test
    public void createAnswer_CreateCorrectAnswer_GetCorrectResponse() throws IOException {

        // Arrange
        DecisionHelper decisionHelper = new DecisionHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        int exceptedCode = 1;
        String exceptedDescription = "Test";
        String exceptedData = "DATA";
        Operation exceptedOperation = Operation.CREATE;
        Entity exceptedEntity = Entity.ROOM;

        // Act
        String result = decisionHelper.createAnswer(exceptedCode, exceptedDescription, exceptedData, exceptedOperation, exceptedEntity);

        // Assertions
        Assertions.assertFalse(result.isBlank());
        IntegrationResponse integrationResponse = objectMapper.readValue(result, IntegrationResponse.class);
        Assertions.assertNotNull(integrationResponse);

        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(exceptedData, integrationResponse.getData());
        Assertions.assertEquals(exceptedEntity, integrationResponse.getEntity());
        Assertions.assertEquals(exceptedOperation, integrationResponse.getOperation());
    }
}
