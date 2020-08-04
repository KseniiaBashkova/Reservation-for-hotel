package Server;

import Help.Entity;
import Help.IntegrationResponse;
import Help.Operation;
import Server.Model.Reservation;
import Server.Model.Room;
import Server.Service.ReservationService;
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

public class ReservationServiceTest {

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
    public void create_CompleteCreate_CreateReservationWasSuccess() throws SQLException, IOException {

        // Arrange
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeUpdate(Mockito.any())).thenReturn(1);
        ReservationService reservationService = new ReservationService();
        Reservation reservation = new Reservation();
        Date checkIn = new Date(2);
        Date checkOut = new Date(3);
        reservation.setCheck_in(checkIn);
        reservation.setCheck_out(checkOut);
        reservation.setCustomer_id(1);
        reservation.setId(1);
        reservation.setRoom_id(4);

        Integer exceptedCode = 4;
        String exceptedDescription = "Reservation was created.";

        // Act
        String result = reservationService.create(reservation, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Mockito.verify(mockConnection, times(1)).createStatement();
        Mockito.verify(mockStatement).close();
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.CREATE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.RESERVATION, integrationResponse.getEntity());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void create_CompleteCreate_ReservationNoCreated() throws IOException {

        // Arrange
        ReservationService reservationService = new ReservationService();
        Reservation reservation = new Reservation();
        Date checkIn = new Date(2);
        Date checkOut = new Date(3);
        reservation.setCheck_in(checkIn);
        reservation.setCheck_out(checkOut);
        reservation.setCustomer_id(1);
        reservation.setId(1);
        reservation.setRoom_id(4);
        Integer exceptedCode = 2;
        String exceptedDescription = "Error during create the reservation null.";

        // Act
        String result = reservationService.create(reservation, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.CREATE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.RESERVATION, integrationResponse.getEntity());
    }

    @Test
    public void updateBySpecificAttribute_UpdateReservation_ReservationWasUpdated() throws SQLException, IOException {

        // Arrange
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeUpdate(Mockito.any())).thenReturn(1);
        ReservationService reservationService = new ReservationService();
        Reservation reservation = new Reservation();
        Date checkIn = new Date(2);
        Date checkOut = new Date(3);
        reservation.setCheck_in(checkIn);
        reservation.setCheck_out(checkOut);
        reservation.setCustomer_id(1);
        reservation.setId(1);
        reservation.setRoom_id(4);

        Integer exceptedCode = 4;
        String exceptedDescription = "Reservation was update.";

        // Act
        String result = reservationService.updateBySpecificAttribute(reservation, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Mockito.verify(mockConnection, times(1)).createStatement();
        Mockito.verify(mockStatement).close();
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.UPDATE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.RESERVATION, integrationResponse.getEntity());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void updateBySpecificAttribute_UpdateReservation_ReservationNoUpdated() throws IOException {

        // Arrange
        ReservationService reservationService = new ReservationService();
        Reservation reservation = new Reservation();
        Date checkIn = new Date(2);
        Date checkOut = new Date(3);
        reservation.setCheck_in(checkIn);
        reservation.setCheck_out(checkOut);
        reservation.setCustomer_id(1);
        reservation.setId(1);
        reservation.setRoom_id(4);

        Integer exceptedCode = 2;
        String exceptedDescription = "Error during update the reservation null.";

        // Act
        String result = reservationService.updateBySpecificAttribute(reservation, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.UPDATE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.RESERVATION, integrationResponse.getEntity());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void getCorrectConditionToUpdateOrFindEntity_GetCorrectConditionToSqlRequest_PartOfSqlRequestWithCondition() {

        // Arrange
        ReservationService reservationService = new ReservationService();
        Reservation reservation = new Reservation();
        Date checkIn = new Date(2);
        Date checkOut = new Date(3);
        reservation.setCheck_in(checkIn);
        reservation.setCheck_out(checkOut);
        reservation.setCustomer_id(1);
        reservation.setId(1);
        reservation.setRoom_id(4);
        String exceptedSqlConditionPart = " WHERE customer_id = 1";

        // Act
        String result = reservationService.getCorrectConditionToUpdateOrFindEntity(reservation);

        // Arrange
        Assertions.assertEquals(exceptedSqlConditionPart, result);
    }

    @Test
    public void getFreeRooms_GetCorrectFreeRooms_FreeRooms() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        String expectedFirstName = "Bob";
        String expectedSecondName = "Test";
        String expectedPassport = "1234567";
        String expectedBirthDay = "1999-01-01";
        String expectedPhoneNumber = "123456789";
        String expectedEmail = "test@mail.ru";
        String expectedCheckIn = "2020-05-23";
        String expectedCheckOut = "2020-05-28";
        Integer expectedCustomerId = 1;
        Integer exceptedCapacity = 4;
        Float exceptedPrice = (float) 6;
        String exceptedIntegrationDescription = "Rooms has been got";
        String exceptedRoomDescription = "Test Description";
        Boolean exceptedVipStatus = true;


        String SQL = "SELECT reservation.id, check_in, check_out, customer_id, room_id, passport as customerpassport, room_number as roomnumber\n" +
                " FROM public.reservation\n" +
                " INNER JOIN customer ON reservation.customer_id = customer.id\n" +
                " INNER JOIN room ON reservation.room_id = room.id;";

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute(SQL)).thenReturn(true);
        Mockito.when(mockStatement.execute("SELECT * FROM public.Room")).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(5).thenReturn(6);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("check_in").thenReturn("room_number");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("check_out").thenReturn("price");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("customer_id").thenReturn("description");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("room_id").thenReturn("vip");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("capacity");

        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1).thenReturn(2).thenReturn(1).thenReturn(2);
        Mockito.when(mockResultSet.getObject("check_in")).thenReturn(expectedCheckIn);
        Mockito.when(mockResultSet.getObject("check_out")).thenReturn(expectedCheckOut);
        Mockito.when(mockResultSet.getObject("customer_id")).thenReturn(expectedCustomerId);
        Mockito.when(mockResultSet.getObject("room_id")).thenReturn(1).thenReturn(2);
        Mockito.when(mockResultSet.getObject("first_name")).thenReturn(expectedFirstName);
        Mockito.when(mockResultSet.getObject("second_name")).thenReturn(expectedSecondName);
        Mockito.when(mockResultSet.getObject("passport")).thenReturn(expectedPassport);
        Mockito.when(mockResultSet.getObject("day_of_birth")).thenReturn(expectedBirthDay);
        Mockito.when(mockResultSet.getObject("phone_number")).thenReturn(expectedPhoneNumber);
        Mockito.when(mockResultSet.getObject("email")).thenReturn(expectedEmail);
        Mockito.when(mockResultSet.getObject("room_number")).thenReturn(1).thenReturn(2);
        Mockito.when(mockResultSet.getObject("description")).thenReturn(exceptedRoomDescription);
        Mockito.when(mockResultSet.getObject("vip")).thenReturn(exceptedVipStatus);
        Mockito.when(mockResultSet.getObject("capacity")).thenReturn(exceptedCapacity);
        Mockito.when(mockResultSet.getObject("price")).thenReturn(exceptedPrice);
        ReservationService reservationService = new ReservationService();
        Reservation reservation = new Reservation();
        Date checkIn = Date.valueOf("2020-06-10");
        Date checkOut = Date.valueOf("2020-06-20");
        reservation.setCheck_in(checkIn);
        reservation.setCheck_out(checkOut);
        reservation.setCustomer_id(1);
        reservation.setId(1);
        reservation.setRoom_id(4);

        // Act
        String result = reservationService.getFreeRooms(reservation, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedIntegrationDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.GET_BY_SPECIFIC_ATTRIBUTE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Room[] rooms = this.objectMapper.readValue(integrationResponse.getData(), Room[].class);
        Assertions.assertEquals(2, rooms.length);
        int roomIdAndRoomNumber = 1;
        for (Room room : rooms) {
            Assertions.assertEquals(exceptedCapacity, room.getCapacity());
            Assertions.assertEquals(exceptedPrice, room.getPrice());
            Assertions.assertEquals(exceptedVipStatus, room.isVip());
            Assertions.assertEquals(exceptedRoomDescription, room.getDescription());
            Assertions.assertEquals(roomIdAndRoomNumber, room.getRoom_number());
            Assertions.assertEquals(roomIdAndRoomNumber++, room.getId());
        }
    }

    @Test
    public void getFreeRooms_GetCorrectFreeRooms_NoFreeRooms() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 3;
        String expectedFirstName = "Bob";
        String expectedSecondName = "Test";
        String expectedPassport = "1234567";
        String expectedBirthDay = "1999-01-01";
        String expectedPhoneNumber = "123456789";
        String expectedEmail = "test@mail.ru";
        String expectedCheckIn = "2020-05-23";
        String expectedCheckOut = "2020-08-28";
        Integer expectedCustomerId = 1;
        Integer exceptedCapacity = 4;
        Float exceptedPrice = (float) 6;
        String exceptedIntegrationDescription = "Does not exist any rooms.";
        String exceptedRoomDescription = "Test Description";
        Boolean exceptedVipStatus = true;


        String SQL = "SELECT reservation.id, check_in, check_out, customer_id, room_id, passport as customerpassport, room_number as roomnumber\n" +
                " FROM public.reservation\n" +
                " INNER JOIN customer ON reservation.customer_id = customer.id\n" +
                " INNER JOIN room ON reservation.room_id = room.id;";
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute(SQL)).thenReturn(true);
        Mockito.when(mockStatement.execute("SELECT * FROM public.Room")).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(5).thenReturn(6);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("check_in").thenReturn("room_number");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("check_out").thenReturn("price");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("customer_id").thenReturn("description");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("room_id").thenReturn("vip");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("capacity");

        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1).thenReturn(2).thenReturn(1).thenReturn(2);
        Mockito.when(mockResultSet.getObject("check_in")).thenReturn(expectedCheckIn);
        Mockito.when(mockResultSet.getObject("check_out")).thenReturn(expectedCheckOut);
        Mockito.when(mockResultSet.getObject("customer_id")).thenReturn(expectedCustomerId);
        Mockito.when(mockResultSet.getObject("room_id")).thenReturn(1).thenReturn(2);
        Mockito.when(mockResultSet.getObject("first_name")).thenReturn(expectedFirstName);
        Mockito.when(mockResultSet.getObject("second_name")).thenReturn(expectedSecondName);
        Mockito.when(mockResultSet.getObject("passport")).thenReturn(expectedPassport);
        Mockito.when(mockResultSet.getObject("day_of_birth")).thenReturn(expectedBirthDay);
        Mockito.when(mockResultSet.getObject("phone_number")).thenReturn(expectedPhoneNumber);
        Mockito.when(mockResultSet.getObject("email")).thenReturn(expectedEmail);
        Mockito.when(mockResultSet.getObject("room_number")).thenReturn(1).thenReturn(2);
        Mockito.when(mockResultSet.getObject("description")).thenReturn(exceptedRoomDescription);
        Mockito.when(mockResultSet.getObject("vip")).thenReturn(exceptedVipStatus);
        Mockito.when(mockResultSet.getObject("capacity")).thenReturn(exceptedCapacity);
        Mockito.when(mockResultSet.getObject("price")).thenReturn(exceptedPrice);
        ReservationService reservationService = new ReservationService();
        Reservation reservation = new Reservation();
        Date checkIn = Date.valueOf("2020-06-10");
        Date checkOut = Date.valueOf("2020-06-20");
        reservation.setCheck_in(checkIn);
        reservation.setCheck_out(checkOut);
        reservation.setCustomer_id(1);
        reservation.setId(1);
        reservation.setRoom_id(4);

        // Act
        String result = reservationService.getFreeRooms(reservation, mockConnection);
        IntegrationResponse integrationResponse = this.objectMapper.readValue(result, IntegrationResponse.class);

        // Arrange
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(exceptedIntegrationDescription, integrationResponse.getDescription());
        Assertions.assertEquals(Operation.GET_BY_SPECIFIC_ATTRIBUTE, integrationResponse.getOperation());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Room[] rooms = this.objectMapper.readValue(integrationResponse.getData(), Room[].class);
        Assertions.assertEquals(0, rooms.length);
    }
}
