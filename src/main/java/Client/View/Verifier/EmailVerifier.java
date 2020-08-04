package Client.View.Verifier;

import javax.swing.*;

public class EmailVerifier extends InputVerifier {

    private String nameOfField;
    public static boolean isTest = false;

    public EmailVerifier(String nameOfField) {
        this.nameOfField = nameOfField;
    }

    /**
     * Verify value is email.
     *
     * @return boolean
     */
    public boolean verify(JComponent input) {
        String s = ((JTextField) input).getText();
        if (s.isEmpty()) {
            // If value is empty show Error message.
            if(!isTest){
                JOptionPane.showMessageDialog(input, this.nameOfField + " required field ", "Error Dialog",
                        JOptionPane.ERROR_MESSAGE);
            }

            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

            // Check if its email
            if (!s.matches(regex)) {
                if(!isTest){
                    JOptionPane.showMessageDialog(input, this.nameOfField + "You have to write the correct email ", "Error Dialog",
                            JOptionPane.ERROR_MESSAGE);
                }

                return false;
            }
        }
        return true;
    }
}
