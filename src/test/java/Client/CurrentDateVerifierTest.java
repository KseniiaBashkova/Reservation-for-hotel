package Client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import Client.View.Verifier.CurrentDateVerifier;
import javax.swing.*;

public class CurrentDateVerifierTest {


    @Test
    public void verify_DateInFuture_CorrectDateDate(){

        // Arrange
        javax.swing.JTextField inputDate = new JTextField(20);
        inputDate.setText("2030-01-01");
        boolean expectedResult = true;

        // Act
        CurrentDateVerifier currentDateVerifier = new CurrentDateVerifier("Check in");
        boolean isValid = currentDateVerifier.verify(inputDate);

        // Assert
        Assertions.assertEquals(expectedResult, isValid);
    }

    @Test
    public void verify_DateInPast_WrongDate(){

        // Arrange
        javax.swing.JTextField inputDate = new JTextField(20);
        inputDate.setText("2000-01-01");
        boolean expectedResult = false;
        CurrentDateVerifier.isTest = true;
        String expectedMessage = "Date Selected date is to be at least today ";

        // Act
        CurrentDateVerifier currentDateVerifier = new CurrentDateVerifier("Date");
        boolean isValid = currentDateVerifier.verify(inputDate);

        // Assert
        Assertions.assertEquals(expectedResult, isValid);
        Assertions.assertEquals(expectedMessage, CurrentDateVerifier.errorMessage);
    }

    @Test
    public void verify_EmptyInputDate_WrongDate(){

        // Arrange
        javax.swing.JTextField inputDate = new JTextField(20);
        inputDate.setText("");
        boolean expectedResult = false;
        CurrentDateVerifier.isTest = true;
        String expectedMessage = "Date required field ";

        // Act
        CurrentDateVerifier currentDateVerifier = new CurrentDateVerifier("Date");
        boolean isValid = currentDateVerifier.verify(inputDate);

        // Assert
        Assertions.assertEquals(expectedResult, isValid);
        Assertions.assertEquals(expectedMessage, CurrentDateVerifier.errorMessage);
    }

    @Test
    public void verify_DateIsNotInCorrectForm_DateWasNotCorrect(){

        // Arrange
        javax.swing.JTextField inputDate = new JTextField(20);
        inputDate.setText("d4r");
        boolean expectedResult = false;
        CurrentDateVerifier.isTest = true;
        String expectedMessage = "Date Selected date is has wrong format, please use yyyy-mm-dd format! ";

        // Act
        CurrentDateVerifier currentDateVerifier = new CurrentDateVerifier("Date");
        boolean isValid = currentDateVerifier.verify(inputDate);

        // Assert
        Assertions.assertEquals(expectedResult, isValid);
        Assertions.assertEquals(expectedMessage, CurrentDateVerifier.errorMessage);
    }


}
