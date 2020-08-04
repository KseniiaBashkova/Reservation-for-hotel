package Client.View.Verifier;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class StringVerifier extends InputVerifier {

    private String nameOfField;

    public StringVerifier(String nameOfField) {
        this.nameOfField = nameOfField;
    }

    /**
     * Verify value is not empty.
     *
     * @return boolean
     */
    public boolean verify(JComponent input) {
        String s = ((JTextField) input).getText();
        if (s.isEmpty()) {
            // If value is empty show Error message.
            JOptionPane.showMessageDialog(input, this.nameOfField + " required field ", "Error Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
