package Client;

import Client.View.Verifier.EmailVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

public class EmailVerifierTest {


    @Test
    public void verify_CorrectEmail_EmailWasValidForm(){

        // Arrange
        javax.swing.JTextField email = new JTextField(20);
        email.setText("kseba@mail.com");
        boolean expectedResult = true;

        // Act
        EmailVerifier emailVerifier = new EmailVerifier("Email");
        boolean isEmail = emailVerifier.verify(email);

        // Assert
        Assertions.assertEquals(expectedResult, isEmail);
    }

    @Test
    public void verify_EmailIsNotInCorrectForm_EmailWasNotCorrect(){

        // Arrange
        javax.swing.JTextField e1 = new JTextField(20);
        javax.swing.JTextField e2 = new JTextField(20);
        javax.swing.JTextField e3 = new JTextField(20);
        javax.swing.JTextField e4 = new JTextField(20);
        e1.setText("kse");
        e2.setText("kse@");
        e3.setText("Kse@mail");
        e4.setText("Kse@mail.");
        boolean expectedResult = false;
        EmailVerifier.isTest = true;


        // Act
        EmailVerifier emailVerifier = new EmailVerifier("Email");
        boolean isEmail1 = emailVerifier.verify(e1);
        boolean isEmail2 = emailVerifier.verify(e1);
        boolean isEmail3 = emailVerifier.verify(e1);
        boolean isEmail4 = emailVerifier.verify(e1);
        // Assert
        Assertions.assertEquals(expectedResult, isEmail1);
        Assertions.assertEquals(expectedResult, isEmail2);
        Assertions.assertEquals(expectedResult, isEmail3);
        Assertions.assertEquals(expectedResult, isEmail4);

    }
    @Test
    public void verify_EmptyEmail_EmailWasNotCorrect(){

        // Arrange
        javax.swing.JTextField email = new JTextField(20);
        email.setText("");
        boolean expectedResult = false;
        EmailVerifier.isTest = true;

        // Act
        EmailVerifier emailVerifier = new EmailVerifier("Email");
        boolean isEmail = emailVerifier.verify(email);

        // Assert
        Assertions.assertEquals(expectedResult, isEmail);
    }
}
