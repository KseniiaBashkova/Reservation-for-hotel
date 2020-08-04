package Client.View.Verifier;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class NumberVerifier extends InputVerifier {

    private String nameOfField;

    public NumberVerifier(String nameOfField) {
        this.nameOfField = nameOfField;
    }

    /**
     * Verify value is integer.
     *
     * @return boolean
     */
    public boolean verify(JComponent input) {
        String s = ((JTextField) input).getText();
        if (s.isEmpty()) {
            // If value is empty show Error message.
            JOptionPane.showMessageDialog(input, this.nameOfField + "required field ", "Error Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), 10) < 0) {
                // If value is not integer, show error message.
                JOptionPane.showMessageDialog(input, this.nameOfField + "You have to write the number ", "Error Dialog",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}