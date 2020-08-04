package Server;

import Help.Entity;
import Help.IntegrationResponse;
import Help.Operation;
import Server.Model.Room;
import Server.Service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.*;

import static org.mockito.Mockito.times;

public class RoomServiceTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private Statement mockStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private ResultSetMetaData mockResultSetMetaData;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void create_ValidRoom_CreateRoomWasSuccess() throws SQLException, IOException {

        // Arrange
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeUpdate(Mockito.any())).thenReturn(1);
        RoomService roomService = new RoomService();
        Room room = new Room();

        Integer roomNumber = 1;
        Integer price = 1000;
        Integer capacity = 2;
        String description = "Test Description";

        room.setId(1);
        room.setCapacity(capacity);
        room.setDescription(description);
        room.setPrice((float) price);
        room.setRoom_number(roomNumber);
        room.setVip(true);

        Integer exceptedCode = 4;
        String exceptedDescription = "Room was created.";

        // Act
        String result = roomService.create(room, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Mockito.verify(mockConnection, times(1)).createStatement();
        Mockito.verify(mockStatement).close();
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.CREATE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void updateBySpecificAttribute_UpdateRoom_RoomWasUpdated() throws SQLException, IOException {

        // Arrange
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeUpdate(Mockito.any())).thenReturn(1);
        RoomService roomService = new RoomService();
        Room room = new Room();

        Integer roomNumber = 1;
        Integer price = 1000;
        Integer capacity = 2;
        String description = "Test Description";

        room.setId(1);
        room.setCapacity(capacity);
        room.setDescription(description);
        room.setPrice((float) price);
        room.setRoom_number(roomNumber);
        room.setVip(true);

        Integer exceptedCode = 4;
        String exceptedDescription = "Room was update.";

        // Act
        String result = roomService.updateBySpecificAttribute(room, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Mockito.verify(mockConnection, times(1)).createStatement();
        Mockito.verify(mockStatement).close();
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.UPDATE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }


    @Test
    public void deleteById_RoomExist_DeleteRoomWasSuccess() throws SQLException, IOException {

        // Arrange
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("DELETE FROM public.Reservation WHERE id = 1")).thenReturn(true);
        RoomService roomService = new RoomService();
        Room room = new Room();

        Integer exceptedCode = 4;
        String exceptedDescription = "Room has been deleted";

        // Act
        String result = roomService.deleteById(room, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Mockito.verify(mockConnection, times(1)).createStatement();
        Mockito.verify(mockStatement).close();
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.DELETE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void deleteById_RoomNotExist_RoomHasNotBeenFound() throws SQLException, IOException {

        // Arrange
        RoomService roomService = new RoomService();
        Room room = new Room();

        Integer exceptedCode = 3;
        String exceptedDescription = "Room has not been found";

        // Act
        String result = roomService.deleteById(room, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Mockito.verify(mockConnection, times(1)).createStatement();
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.DELETE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void getAll_RoomsAreInDB_RoomsHaveBeenGot() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        Integer numberOfRoom = 5;
        Integer exceptedCapacity = 4;
        Float exceptedPrice = (float) 6;
        String exceptedRoomDescription = "Test Description";
        Boolean exceptedVipStatus = true;

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("SELECT * FROM public.Room")).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(6);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("room_number");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("price");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("description");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("vip");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("capacity");
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1);
        Mockito.when(mockResultSet.getObject("room_number")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("description")).thenReturn(exceptedRoomDescription);
        Mockito.when(mockResultSet.getObject("vip")).thenReturn(exceptedVipStatus);
        Mockito.when(mockResultSet.getObject("capacity")).thenReturn(exceptedCapacity);
        Mockito.when(mockResultSet.getObject("price")).thenReturn(exceptedPrice);
        ObjectMapper objectMapper = new ObjectMapper();

        RoomService roomService = new RoomService();
        String exceptedDescription = "Rooms has been got";

        // Act
        String result = roomService.getAll(new Room(), mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Mockito.verify(mockConnection, times(1)).createStatement();
        Mockito.verify(mockStatement).close();
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.GET_ALL, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Room[] rooms = objectMapper.readValue(integrationResponse.getData(), Room[].class);
        Assertions.assertEquals(numberOfRoom, rooms.length);

        int exceptedRoomNumber = 1;
        for (Room room : rooms) {
            Assertions.assertEquals(exceptedRoomNumber, room.getRoom_number());
            Assertions.assertEquals(exceptedPrice, room.getPrice());
            Assertions.assertEquals(exceptedRoomDescription, room.getDescription());
            Assertions.assertEquals(exceptedCapacity, room.getCapacity());
            Assertions.assertEquals(exceptedVipStatus, room.isVip());
            exceptedRoomNumber += 1;
        }
    }

    @Test
    public void getAll_EmptyRoomTable_DoesNotExistAnyRoom() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 3;
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("SELECT * FROM public.Room")).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(6);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("room_number");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("price");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("description");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("vip");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("capacity");

        RoomService roomService = new RoomService();
        String exceptedDescription = "Does not exist any rooms.";

        // Act
        String result = roomService.getAll(new Room(), mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Mockito.verify(mockConnection, times(1)).createStatement();
        Mockito.verify(mockStatement).close();
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.GET_ALL, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertTrue(integrationResponse.getData().isBlank());

    }

    @Test
    public void getCorrectConditionToUpdateOrFindEntity_GetCorrectConditionToSqlRequest_PartOfSqlRequestWithCondition() {

        // Arrange
        RoomService roomService = new RoomService();
        Room room = new Room();
        room.setRoom_number(1);
        String exceptedSqlConditionPart = " WHERE room_number = 1";

        // Act
        String result = roomService.getCorrectConditionToUpdateOrFindEntity(room);

        // Arrange
        Assertions.assertEquals(exceptedSqlConditionPart, result);
    }
}
