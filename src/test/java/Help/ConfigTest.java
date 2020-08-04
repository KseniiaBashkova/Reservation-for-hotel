/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Help;

import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author d
 */
public class ConfigTest {


    @Mock
    Properties mockProperties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @org.junit.jupiter.api.Test
    public void getConfig_GetGonfig_GetCorrectGonfig() {

        // Arrange
        String exceptedUrl = "testUrl";
        String exceptedPassword = "password";
        String exceptedUser = "user";
        Integer exceptedPort = 122;
        Mockito.when(mockProperties.getProperty("url")).thenReturn(exceptedUrl);
        Mockito.when(mockProperties.getProperty("user")).thenReturn(exceptedUser);
        Mockito.when(mockProperties.getProperty("password")).thenReturn(exceptedPassword);
        Mockito.when(mockProperties.getProperty("port")).thenReturn(exceptedPort.toString());

        // Act
        Config config = Config.getConfig(mockProperties);

        // Arrange
        Assertions.assertEquals(exceptedUrl, config.url);
        Assertions.assertEquals(exceptedPassword, config.password);
        Assertions.assertEquals(exceptedPort, config.port);
        Assertions.assertEquals(exceptedUser, config.user);
    }

    @Test
    public void toString_CreateStringOfConfig_GetCorrectString() {

        // Arrange
        String exceptedUrl = "testUrl";
        String exceptedPassword = "password";
        String exceptedUser = "user";
        int exceptedPort = 122;
        String exceptedResult = "Password: password\n" +
                "Url: testUrl\n" +
                "User: user\n" +
                "Port: 122\n";
        Mockito.when(mockProperties.getProperty("url")).thenReturn(exceptedUrl);
        Mockito.when(mockProperties.getProperty("user")).thenReturn(exceptedUser);
        Mockito.when(mockProperties.getProperty("password")).thenReturn(exceptedPassword);
        Mockito.when(mockProperties.getProperty("port")).thenReturn(Integer.toString(exceptedPort));

        // Act
        Config config = Config.getConfig(mockProperties);
        String result = config.toString();

        // Arrange
        Assertions.assertEquals(exceptedResult, result);
    }
}
