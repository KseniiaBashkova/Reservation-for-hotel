package Client.View.Verifier;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Represents class to verify double input.
 */
public class DoubleVerifier extends InputVerifier {

    private String nameOfField;

    public DoubleVerifier(String nameOfField) {
        this.nameOfField = nameOfField;
    }

    /**
     * Verify value is double.
     *
     * @return boolean
     */
    public boolean verify(JComponent input) {
        String s = ((JTextField) input).getText();
        // Check if its not empty
        if (s.isEmpty()) {
            // If value is empty show Error message.
            JOptionPane.showMessageDialog(input, this.nameOfField + " required field ", "Error Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            // If value is not double, show error message.
            JOptionPane.showMessageDialog(input, this.nameOfField + "You have to write the number ", "Error Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}