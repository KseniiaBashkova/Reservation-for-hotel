package Server.Controller;

import Help.IntegrationResponse;
import Help.Entity;
import Help.Operation;
import Server.Model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.sql.*;

public class ServerControllerTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private Statement mockStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private ResultSetMetaData mockResultSetMetaData;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCorrectEntity_CorrectRoomEntity_RoomEntityHasBeenGot() throws JsonProcessingException {

        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        String entityName = "Room";
        Room room = new Room();
        room.setCapacity(20);
        room.setDescription("Description");
        room.setId(1);
        room.setPrice((float) 100);
        room.setRoom_number(20);
        room.setVip(true);
        String roomInJson = objectMapper.writeValueAsString(room);

        try {
            // Act
            Room currentRoom = (Room) ServerController.getCorrectEntity(entityName, roomInJson);

            // Assertion
            Assertions.assertNotNull(currentRoom);
            Assertions.assertEquals(room.getCapacity(), currentRoom.getCapacity());
            Assertions.assertEquals(room.getDescription(), currentRoom.getDescription());
            Assertions.assertEquals(room.getId(), currentRoom.getId());
            Assertions.assertEquals(room.getPrice(), currentRoom.getPrice());
            Assertions.assertEquals(room.getRoom_number(), currentRoom.getRoom_number());
            Assertions.assertEquals(room.isVip(), currentRoom.isVip());

        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void getCorrectEntity_CorrectReceptionEntity_ReceptionEntityHasBeenGot() throws JsonProcessingException {

        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        String entityName = "Reception";
        Reception reception = new Reception();
        reception.setAddress("Test");
        reception.setEmail("test@test.ru");
        reception.setFirst_name("Nikita");
        reception.setPassport("211");
        reception.setId(1);
        reception.setSecond_name("Second Name");
        reception.setPhone_number("899");

        String receptionInJson = objectMapper.writeValueAsString(reception);

        try {
            // Act
            Reception currentReception = (Reception) ServerController.getCorrectEntity(entityName, receptionInJson);

            // Assertion
            Assertions.assertNotNull(currentReception);
            Assertions.assertEquals(reception.getAddress(), currentReception.getAddress());
            Assertions.assertEquals(reception.getEmail(), currentReception.getEmail());
            Assertions.assertEquals(reception.getFirst_name(), currentReception.getFirst_name());
            Assertions.assertEquals(reception.getSecond_name(), currentReception.getSecond_name());
            Assertions.assertEquals(reception.getId(), currentReception.getId());
            Assertions.assertEquals(reception.getPhone_number(), currentReception.getPhone_number());
            Assertions.assertEquals(reception.getPassport(), currentReception.getPassport());

        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void getCorrectEntity_CorrectCustomerEntity_CustomerEntityHasBeenGot() throws JsonProcessingException {

        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        String entityName = "Customer";
        Customer customer = new Customer();
        customer.setEmail("test@test.com");
        customer.setFirst_name("Nikita");
        customer.setSecond_name("Test");
        customer.setId(1);
        customer.setPassport("11212");
        customer.setPhone_number("121212");
        String customerInJson = objectMapper.writeValueAsString(customer);

        try {
            // Act
            Customer currentCustomer = (Customer) ServerController.getCorrectEntity(entityName, customerInJson);

            // Assertion
            Assertions.assertNotNull(currentCustomer);
            Assertions.assertEquals(customer.getEmail(), currentCustomer.getEmail());
            Assertions.assertEquals(customer.getFirst_name(), currentCustomer.getFirst_name());
            Assertions.assertEquals(customer.getId(), currentCustomer.getId());
            Assertions.assertEquals(customer.getPassport(), currentCustomer.getPassport());
            Assertions.assertEquals(customer.getPhone_number(), currentCustomer.getPhone_number());
            Assertions.assertEquals(customer.getSecond_name(), currentCustomer.getSecond_name());
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void getCorrectEntity_CorrectReservationEntity_ReservationEntityHasBeenGot() throws JsonProcessingException {

        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        String entityName = "Reservation";
        Date checkIn = new Date(2);
        Date checkOut = new Date(3);
        Reservation reservation = new Reservation();
        reservation.setCheck_in(checkIn);
        reservation.setCheck_out(checkOut);
        reservation.setCustomer_id(1);
        reservation.setId(1);
        reservation.setRoom_id(4);
        String reservationInJson = objectMapper.writeValueAsString(reservation);

        try {
            // Act
            Reservation currentReservation = (Reservation) ServerController.getCorrectEntity(entityName, reservationInJson);

            // Assertion
            Assertions.assertNotNull(currentReservation);
            Assertions.assertEquals(reservation.getCheck_in(), currentReservation.getCheck_in());
            Assertions.assertEquals(reservation.getCheck_out(), currentReservation.getCheck_out());
            Assertions.assertEquals(reservation.getCustomer_id(), currentReservation.getCustomer_id());
            Assertions.assertEquals(reservation.getId(), currentReservation.getId());
            Assertions.assertEquals(reservation.getRoom_id(), currentReservation.getRoom_id());
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void getCorrectEntity_NoEntity_EntityDoesNotExist() {

        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        String entityName = "NoExistEntity";

        try {
            // Act
            IEntity entity = ServerController.getCorrectEntity(entityName, "");

            // Assertion
            Assertions.assertNull(entity);
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    public void processMessage_CorrectRequestCreateRoom_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockConnection.createStatement().executeUpdate(Mockito.any())).thenReturn(1);
        ObjectMapper objectMapper = new ObjectMapper();

        Room room = new Room();
        room.setRoom_number(4);
        room.setDescription("fd");
        room.setVip(true);
        room.setCapacity(4);
        room.setPrice((float) 6);
        IntegrationRequest integrationRequest = new IntegrationRequest("Room", "create", room);
        String jsonRequest = objectMapper.writeValueAsString(integrationRequest);
        Integer exceptedCode = 4;
        String exceptedDescription = "Room was created.";
        ServerController serverController = new ServerController();

        // Act
        String answerFromServer = serverController.processMessage(jsonRequest, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.CREATE, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void processMessage_CorrectRequestCreateReservation_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockConnection.createStatement().executeUpdate(Mockito.any())).thenReturn(1);
        ObjectMapper objectMapper = new ObjectMapper();

        Reservation reservation = new Reservation();
        reservation.setRoom_id(20);
        reservation.setCheck_in(Date.valueOf("2020-05-12"));
        reservation.setCheck_out(Date.valueOf("2020-05-28"));
        reservation.setCustomer_id(1);
        IntegrationRequest integrationRequest = new IntegrationRequest("Reservation", "create", reservation);
        String jsonRequest = objectMapper.writeValueAsString(integrationRequest);
        Integer exceptedCode = 4;
        String exceptedDescription = "Reservation was created.";
        ServerController serverController = new ServerController();

        // Act
        String answerFromServer = serverController.processMessage(jsonRequest, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.RESERVATION, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.CREATE, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void processMessage_CorrectRequestCreateCustomer_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockConnection.createStatement().executeUpdate(Mockito.any())).thenReturn(1);
        ObjectMapper objectMapper = new ObjectMapper();

        Customer customer = new Customer();
        customer.setDay_of_birth(Date.valueOf("2020-02-12"));
        customer.setPassport("32323");
        customer.setSecond_name("Grigoryev");
        customer.setFirst_name("Nikita");
        customer.setPhone_number("32323");
        customer.setEmail("test@test.com");
        IntegrationRequest integrationRequest = new IntegrationRequest("Customer", "create", customer);
        String json = objectMapper.writeValueAsString(integrationRequest);
        Integer exceptedCode = 4;
        String exceptedDescription = "Customer was created.";
        ServerController serverController = new ServerController();

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.CUSTOMER, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.CREATE, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void processMessage_CorrectRequestCreateReception_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockConnection.createStatement().executeUpdate(Mockito.any())).thenReturn(1);
        ObjectMapper objectMapper = new ObjectMapper();
        Reception reception = new Reception();
        reception.setDay_of_birth(Date.valueOf("2020-02-14"));
        reception.setPassport("32323");
        reception.setAddress("Rus");
        reception.setSecond_name("Bashkova");
        reception.setFirst_name("Kseniia");
        reception.setEmail("test@test.com");
        reception.setPhone_number("323");
        IntegrationRequest integrationRequest = new IntegrationRequest("Reception", "create", reception);
        String json = objectMapper.writeValueAsString(integrationRequest);
        Integer exceptedCode = 4;
        String exceptedDescription = "Reception was created.";
        ServerController serverController = new ServerController();

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.RECEPTION, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.CREATE, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescription, integrationResponse.getDescription());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void processMessage_CorrectRequestGetAllRooms_ResponseWasCorrect() throws SQLException, IOException {

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

        IntegrationRequest integrationRequest = new IntegrationRequest("Room", "getAll", new Room());
        String jsonGetAllRoom = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Rooms has been got";

        // Act
        String answerFromServer = serverController.processMessage(jsonGetAllRoom, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.GET_ALL, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
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
    public void processMessage_CorrectRequestGetAllReception_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        Integer numberOfReceptions = 5;
        String expectedFirstName = "Bob";
        String expectedSecondName = "Test";
        String expectedPassport = "1234567";
        String expectedBirthDay = "1999-01-01";
        String expectedPhoneNumber = "123456789";
        String expectedEmail = "test@mail.ru";
        String expectedAddress = "Taborska, 5";

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("SELECT * FROM public.Reception")).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(8);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("first_name");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("second_name");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("passport");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("day_of_birth");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("phone_number");
        Mockito.when(mockResultSetMetaData.getColumnName(7)).thenReturn("email");
        Mockito.when(mockResultSetMetaData.getColumnName(8)).thenReturn("address");

        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("first_name")).thenReturn(expectedFirstName);
        Mockito.when(mockResultSet.getObject("second_name")).thenReturn(expectedSecondName);
        Mockito.when(mockResultSet.getObject("passport")).thenReturn(expectedPassport);
        Mockito.when(mockResultSet.getObject("day_of_birth")).thenReturn(expectedBirthDay);
        Mockito.when(mockResultSet.getObject("phone_number")).thenReturn(expectedPhoneNumber);
        Mockito.when(mockResultSet.getObject("email")).thenReturn(expectedEmail);
        Mockito.when(mockResultSet.getObject("address")).thenReturn(expectedAddress);
        ObjectMapper objectMapper = new ObjectMapper();

        IntegrationRequest integrationRequest = new IntegrationRequest("Reception", "getAll", new Reception());
        String json = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Receptions has been got";

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.RECEPTION, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.GET_ALL, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Reception[] receptions = objectMapper.readValue(integrationResponse.getData(), Reception[].class);
        Assertions.assertEquals(numberOfReceptions, receptions.length);

        int expectedId = 1;
        for (Reception reception : receptions) {
            Assertions.assertEquals(expectedId, reception.getId());
            Assertions.assertEquals(expectedFirstName, reception.getFirst_name());
            Assertions.assertEquals(expectedSecondName, reception.getSecond_name());
            Assertions.assertEquals(expectedPassport, reception.getPassport());
            Assertions.assertEquals(expectedBirthDay, reception.getDay_of_birth().toString());
            Assertions.assertEquals(expectedPhoneNumber, reception.getPhone_number());
            Assertions.assertEquals(expectedEmail, reception.getEmail());
            Assertions.assertEquals(expectedAddress, reception.getAddress());

            expectedId += 1;
        }
    }

    @Test
    public void processMessage_CorrectRequestGetAllCustomers_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        Integer numberOfCustomers = 5;
        String expectedFirstName = "Bob";
        String expectedSecondName = "Test";
        String expectedPassport = "1234567";
        String expectedBirthDay = "1999-01-01";
        String expectedPhoneNumber = "123456789";
        String expectedEmail = "test@mail.ru";

        String SQL = "SELECT customer.id, first_name, second_name, passport, day_of_birth, phone_number, email, COUNT(reservation.id) AS total\n" +
                " FROM public.customer\n" +
                " LEFT JOIN reservation ON customer.id = reservation.customer_id GROUP BY customer.id ORDER BY total DESC;";

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute(SQL)).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(7);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("first_name");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("second_name");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("passport");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("day_of_birth");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("phone_number");
        Mockito.when(mockResultSetMetaData.getColumnName(7)).thenReturn("email");

        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("first_name")).thenReturn(expectedFirstName);
        Mockito.when(mockResultSet.getObject("second_name")).thenReturn(expectedSecondName);
        Mockito.when(mockResultSet.getObject("passport")).thenReturn(expectedPassport);
        Mockito.when(mockResultSet.getObject("day_of_birth")).thenReturn(expectedBirthDay);
        Mockito.when(mockResultSet.getObject("phone_number")).thenReturn(expectedPhoneNumber);
        Mockito.when(mockResultSet.getObject("email")).thenReturn(expectedEmail);
        ObjectMapper objectMapper = new ObjectMapper();

        IntegrationRequest integrationRequest = new IntegrationRequest("Customer", "getAll", new Customer());
        String json = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Customers has been got";

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.CUSTOMER, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.GET_ALL, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Reception[] customers = objectMapper.readValue(integrationResponse.getData(), Reception[].class);
        Assertions.assertEquals(numberOfCustomers, customers.length);

        int expectedId = 1;
        for (Reception customer : customers) {
            Assertions.assertEquals(expectedId, customer.getId());
            Assertions.assertEquals(expectedFirstName, customer.getFirst_name());
            Assertions.assertEquals(expectedSecondName, customer.getSecond_name());
            Assertions.assertEquals(expectedPassport, customer.getPassport());
            Assertions.assertEquals(expectedBirthDay, customer.getDay_of_birth().toString());
            Assertions.assertEquals(expectedPhoneNumber, customer.getPhone_number());
            Assertions.assertEquals(expectedEmail, customer.getEmail());

            expectedId += 1;
        }
    }

    @Test
    public void processMessage_CorrectRequestGetAllReservations_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        Integer numberOfReservations = 5;
        String expectedCheckIn = "2020-05-23";
        String expectedCheckOut = "2020-05-25";
        Integer expectedRoomId = 1;
        Integer expectedCustomerId = 1;

        String SQL = "SELECT reservation.id, check_in, check_out, customer_id, room_id, passport as customerpassport, room_number as roomnumber\n" +
                " FROM public.reservation\n" +
                " INNER JOIN customer ON reservation.customer_id = customer.id\n" +
                " INNER JOIN room ON reservation.room_id = room.id;";

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute(SQL)).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(5);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("check_in");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("check_out");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("customer_id");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("room_id");

        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("check_in")).thenReturn(expectedCheckIn);
        Mockito.when(mockResultSet.getObject("check_out")).thenReturn(expectedCheckOut);
        Mockito.when(mockResultSet.getObject("customer_id")).thenReturn(expectedCustomerId);
        Mockito.when(mockResultSet.getObject("room_id")).thenReturn(expectedRoomId);
        ObjectMapper objectMapper = new ObjectMapper();

        IntegrationRequest integrationRequest = new IntegrationRequest("Reservation", "getAll", new Reservation());
        String json = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Reservations has been got";

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.RESERVATION, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.GET_ALL, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Reservation[] reservations = objectMapper.readValue(integrationResponse.getData(), Reservation[].class);
        Assertions.assertEquals(numberOfReservations, reservations.length);

        int expectedId = 1;
        for (Reservation reservation : reservations) {
            Assertions.assertEquals(expectedId, reservation.getId());
            Assertions.assertEquals(expectedCheckIn, reservation.getCheck_in().toString());
            Assertions.assertEquals(expectedCheckOut, reservation.getCheck_out().toString());
            Assertions.assertEquals(expectedCustomerId, reservation.getCustomer_id());
            Assertions.assertEquals(expectedRoomId, reservation.getRoom_id());

            expectedId += 1;
        }
    }

    @Test
    public void processMessage_CorrectRequestGetByIdRoom_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        Integer numberOfRoom = 1;
        Integer exceptedCapacity = 4;
        Float exceptedPrice = (float) 6;
        String exceptedRoomDescription = "Test Description";
        Boolean exceptedVipStatus = true;
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("SELECT * FROM public.Room WHERE id = 1")).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(6);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("room_number");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("price");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("description");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("vip");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("capacity");
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1);
        Mockito.when(mockResultSet.getObject("room_number")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("description")).thenReturn(exceptedRoomDescription);
        Mockito.when(mockResultSet.getObject("vip")).thenReturn(exceptedVipStatus);
        Mockito.when(mockResultSet.getObject("capacity")).thenReturn(exceptedCapacity);
        Mockito.when(mockResultSet.getObject("price")).thenReturn(exceptedPrice);
        ObjectMapper objectMapper = new ObjectMapper();

        Room roomT = new Room();
        roomT.setId(1);
        IntegrationRequest integrationRequest = new IntegrationRequest("Room", "getById", roomT);
        ServerController serverController = new ServerController();
        String json = objectMapper.writeValueAsString(integrationRequest);
        String exceptedDescriptionOfResponse = "Room has been got";

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.GET_BY_ID, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
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
    public void processMessage_CorrectRequestGetByIdCustomer_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        Integer numberOfCustomer = 1;
        String expectedFirstName = "Bob";
        String expectedSecondName = "Test";
        String expectedPassport = "1234567";
        String expectedBirthDay = "1999-01-01";
        String expectedPhoneNumber = "123456789";
        String expectedEmail = "test@mail.ru";

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("SELECT * FROM public.Customer WHERE id = 1")).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(7);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("first_name");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("second_name");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("passport");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("day_of_birth");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("phone_number");
        Mockito.when(mockResultSetMetaData.getColumnName(7)).thenReturn("email");

        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("first_name")).thenReturn(expectedFirstName);
        Mockito.when(mockResultSet.getObject("second_name")).thenReturn(expectedSecondName);
        Mockito.when(mockResultSet.getObject("passport")).thenReturn(expectedPassport);
        Mockito.when(mockResultSet.getObject("day_of_birth")).thenReturn(expectedBirthDay);
        Mockito.when(mockResultSet.getObject("phone_number")).thenReturn(expectedPhoneNumber);
        Mockito.when(mockResultSet.getObject("email")).thenReturn(expectedEmail);
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customerT = new Customer();
        customerT.setId(1);
        IntegrationRequest integrationRequest = new IntegrationRequest("Customer", "getById", customerT);
        String json = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Customer has been got";

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.CUSTOMER, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.GET_BY_ID, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Reception[] customers = objectMapper.readValue(integrationResponse.getData(), Reception[].class);
        Assertions.assertEquals(numberOfCustomer, customers.length);

        int expectedId = 1;
        for (Reception customer : customers) {
            Assertions.assertEquals(expectedId, customer.getId());
            Assertions.assertEquals(expectedFirstName, customer.getFirst_name());
            Assertions.assertEquals(expectedSecondName, customer.getSecond_name());
            Assertions.assertEquals(expectedPassport, customer.getPassport());
            Assertions.assertEquals(expectedBirthDay, customer.getDay_of_birth().toString());
            Assertions.assertEquals(expectedPhoneNumber, customer.getPhone_number());
            Assertions.assertEquals(expectedEmail, customer.getEmail());

            expectedId += 1;
        }
    }

    @Test
    public void processMessage_CorrectRequestGetByIdReception_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        Integer numberOfReception = 1;
        String expectedFirstName = "Bob";
        String expectedSecondName = "Test";
        String expectedPassport = "1234567";
        String expectedBirthDay = "1999-01-01";
        String expectedPhoneNumber = "123456789";
        String expectedEmail = "test@mail.ru";
        String expectedAddress = "Taborska, 8";


        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("SELECT * FROM public.Reception WHERE id = 1")).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(8);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("first_name");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("second_name");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("passport");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("day_of_birth");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("phone_number");
        Mockito.when(mockResultSetMetaData.getColumnName(7)).thenReturn("email");
        Mockito.when(mockResultSetMetaData.getColumnName(8)).thenReturn("address");

        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("first_name")).thenReturn(expectedFirstName);
        Mockito.when(mockResultSet.getObject("second_name")).thenReturn(expectedSecondName);
        Mockito.when(mockResultSet.getObject("passport")).thenReturn(expectedPassport);
        Mockito.when(mockResultSet.getObject("day_of_birth")).thenReturn(expectedBirthDay);
        Mockito.when(mockResultSet.getObject("phone_number")).thenReturn(expectedPhoneNumber);
        Mockito.when(mockResultSet.getObject("email")).thenReturn(expectedEmail);
        Mockito.when(mockResultSet.getObject("address")).thenReturn(expectedAddress);
        ObjectMapper objectMapper = new ObjectMapper();

        Reception receptionT = new Reception();
        receptionT.setId(1);
        IntegrationRequest integrationRequest = new IntegrationRequest("Reception", "getById", receptionT);
        String json = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Reception has been got";

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.RECEPTION, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.GET_BY_ID, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Reception[] receptions = objectMapper.readValue(integrationResponse.getData(), Reception[].class);
        Assertions.assertEquals(numberOfReception, receptions.length);

        int expectedId = 1;
        for (Reception reception : receptions) {
            Assertions.assertEquals(expectedId, reception.getId());
            Assertions.assertEquals(expectedFirstName, reception.getFirst_name());
            Assertions.assertEquals(expectedSecondName, reception.getSecond_name());
            Assertions.assertEquals(expectedPassport, reception.getPassport());
            Assertions.assertEquals(expectedBirthDay, reception.getDay_of_birth().toString());
            Assertions.assertEquals(expectedPhoneNumber, reception.getPhone_number());
            Assertions.assertEquals(expectedEmail, reception.getEmail());
            Assertions.assertEquals(expectedAddress, reception.getAddress());

            expectedId += 1;
        }
    }

    @Test
    public void processMessage_CorrectRequestGetByIdReservation_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        Integer numberOfReservation = 1;
        String expectedCheckIn = "2020-05-23";
        String expectedCheckOut = "2020-05-25";
        Integer expectedRoomId = 1;
        Integer expectedCustomerId = 1;
        String exceptedCustomerPassword = "test";
        String exceptedRoomNumber = "32323";

        String sql = "SELECT reservation.id, check_in, check_out, customer_id, room_id, passport as customerpassport, room_number as roomnumber\n" +
                " FROM public.reservation\n" +
                " INNER JOIN customer ON reservation.customer_id = customer.id\n" +
                " INNER JOIN room ON reservation.room_id = room.id WHERE reservation.id = 1;";
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute(sql)).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(7);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("check_in");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("check_out");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("customer_id");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("room_id");
        Mockito.when(mockResultSetMetaData.getColumnName(6)).thenReturn("customerpassport");
        Mockito.when(mockResultSetMetaData.getColumnName(7)).thenReturn("roomnumber");

        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("check_in")).thenReturn(expectedCheckIn);
        Mockito.when(mockResultSet.getObject("check_out")).thenReturn(expectedCheckOut);
        Mockito.when(mockResultSet.getObject("customer_id")).thenReturn(expectedCustomerId);
        Mockito.when(mockResultSet.getObject("room_id")).thenReturn(expectedRoomId);
        Mockito.when(mockResultSet.getObject("customerpassport")).thenReturn(exceptedCustomerPassword);
        Mockito.when(mockResultSet.getObject("roomnumber")).thenReturn(exceptedRoomNumber);
        ObjectMapper objectMapper = new ObjectMapper();
        Reservation reservationRequest = new Reservation();
        reservationRequest.setId(1);
        String jsonGetReservation = objectMapper.writeValueAsString(new IntegrationRequest("Reservation", "getById", reservationRequest));

        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Reservation has been got";

        // Act
        String answerFromServer = serverController.processMessage(jsonGetReservation, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.RESERVATION, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.GET_BY_ID, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Reservation[] reservations = objectMapper.readValue(integrationResponse.getData(), Reservation[].class);
        Assertions.assertEquals(numberOfReservation, reservations.length);

        int expectedId = 1;
        for (Reservation reservation : reservations) {
            Assertions.assertEquals(expectedId, reservation.getId());
            Assertions.assertEquals(expectedCheckIn, reservation.getCheck_in().toString());
            Assertions.assertEquals(expectedCheckOut, reservation.getCheck_out().toString());
            Assertions.assertEquals(expectedCustomerId, reservation.getCustomer_id());
            Assertions.assertEquals(expectedRoomId, reservation.getRoom_id());
            Assertions.assertEquals(exceptedRoomNumber, reservation.getRoomnumber());
            Assertions.assertEquals(exceptedCustomerPassword, reservation.getCustomerpassport());
            expectedId += 1;
        }
    }


    @Test
    public void processMessage_CorrectRequestDeleteByIdRoom_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 4;
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("DELETE FROM public.Room WHERE id = 1")).thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();

        Room room = new Room();
        room.setId(1);
        IntegrationRequest integrationRequest = new IntegrationRequest("Room", "deleteById", room);
        String json = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Room has been deleted";

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.ROOM, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.DELETE, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertTrue(integrationResponse.getData().isBlank());

    }

    @Test
    public void processMessage_CorrectRequestDeleteByIdReception_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 4;

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("DELETE FROM public.Reception WHERE id = 1")).thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();
        Reception reception = new Reception();
        reception.setId(1);
        IntegrationRequest integrationRequest = new IntegrationRequest("Reception", "deleteById", reception);
        String json = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Reception has been deleted";


        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.RECEPTION, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.DELETE, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void processMessage_CorrectRequestDeleteByIdCustomer_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 4;

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("DELETE FROM public.Customer WHERE id = 1")).thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();

        Customer customer = new Customer();
        customer.setId(1);
        IntegrationRequest integrationRequest = new IntegrationRequest("Customer", "deleteById", customer);
        String json = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Customer has been deleted";

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.CUSTOMER, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.DELETE, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertTrue(integrationResponse.getData().isBlank());
    }

    @Test
    public void processMessage_CorrectRequestDeleteByIdReservation_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 4;
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("DELETE FROM public.Reservation WHERE id = 1")).thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();

        Reservation reservation = new Reservation();
        reservation.setId(1);
        IntegrationRequest integrationRequest = new IntegrationRequest("Reservation", "deleteById", reservation);
        String json = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Reservation has been deleted";

        // Act
        String answerFromServer = serverController.processMessage(json, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.RESERVATION, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.DELETE, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertTrue(integrationResponse.getData().isBlank());

    }

    @Test
    public void processMessage_CorrectRequestGetBySpecificAttributeReservation_ResponseWasCorrect() throws SQLException, IOException {

        // Arrange
        Integer exceptedCode = 1;
        Integer numberOfReservation = 4;
        String expectedCheckIn = "2020-05-23";
        String expectedCheckOut = "2020-05-25";
        Integer expectedRoomId = 1;

        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute("SELECT * FROM public.Reservation WHERE customer_id = 1")).thenReturn(true);
        Mockito.when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        Mockito.when(mockResultSetMetaData.getColumnCount()).thenReturn(5);
        Mockito.when(mockResultSetMetaData.getColumnName(1)).thenReturn("id");
        Mockito.when(mockResultSetMetaData.getColumnName(2)).thenReturn("check_in");
        Mockito.when(mockResultSetMetaData.getColumnName(3)).thenReturn("check_out");
        Mockito.when(mockResultSetMetaData.getColumnName(4)).thenReturn("customer_id");
        Mockito.when(mockResultSetMetaData.getColumnName(5)).thenReturn("room_id");

        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getObject("id")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("check_in")).thenReturn(expectedCheckIn);
        Mockito.when(mockResultSet.getObject("check_out")).thenReturn(expectedCheckOut);
        Mockito.when(mockResultSet.getObject("customer_id")).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        Mockito.when(mockResultSet.getObject("room_id")).thenReturn(expectedRoomId);
        ObjectMapper objectMapper = new ObjectMapper();

        Reservation reservationT = new Reservation();
        reservationT.setCustomer_id(1);
        IntegrationRequest integrationRequest = new IntegrationRequest("Reservation", "getBySpecificAttribute", reservationT);
        String jsonGetReservation = objectMapper.writeValueAsString(integrationRequest);
        ServerController serverController = new ServerController();
        String exceptedDescriptionOfResponse = "Reservations has been got";

        // Act
        String answerFromServer = serverController.processMessage(jsonGetReservation, mockConnection);
        IntegrationResponse integrationResponse = objectMapper.readValue(answerFromServer, IntegrationResponse.class);

        // Assertion
        Assertions.assertNotNull(integrationResponse);
        Assertions.assertEquals(exceptedCode, integrationResponse.getCode());
        Assertions.assertEquals(Entity.RESERVATION, integrationResponse.getEntity());
        Assertions.assertEquals(Operation.GET_BY_SPECIFIC_ATTRIBUTE, integrationResponse.getOperation());
        Assertions.assertEquals(exceptedDescriptionOfResponse, integrationResponse.getDescription());
        Assertions.assertFalse(integrationResponse.getData().isBlank());

        Reservation[] reservations = objectMapper.readValue(integrationResponse.getData(), Reservation[].class);
        Assertions.assertEquals(numberOfReservation, reservations.length);

        int expectedId = 1;
        int expectedCustomerId = 1;
        for (Reservation reservation : reservations) {
            Assertions.assertEquals(expectedId, reservation.getId());
            Assertions.assertEquals(expectedCheckIn, reservation.getCheck_in().toString());
            Assertions.assertEquals(expectedCheckOut, reservation.getCheck_out().toString());
            Assertions.assertEquals(expectedCustomerId, reservation.getCustomer_id());
            Assertions.assertEquals(expectedRoomId, reservation.getRoom_id());

            expectedCustomerId += 1;
            expectedId += 1;
        }
    }
}
