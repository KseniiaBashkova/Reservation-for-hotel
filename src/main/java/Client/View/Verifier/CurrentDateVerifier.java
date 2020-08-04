package Client.View.Verifier;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CurrentDateVerifier extends InputVerifier {

    private String nameOfField;
    public static boolean isTest = false;
    public static String errorMessage = "";

    public CurrentDateVerifier(String nameOfField) {
        this.nameOfField = nameOfField;
    }

    /**
     * Verify selected Date is after current date and it is not null
     *
     * @return boolean
     */
    public boolean verify(JComponent input) {
        String s = ((JTextField) input).getText();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // Check if its empty
        if (s.isEmpty()) {
            // If value is empty show Error message.
            if(!isTest){
                JOptionPane.showMessageDialog(input, this.nameOfField + " required field ", "Error Dialog",
                        JOptionPane.ERROR_MESSAGE);
            }else{
                errorMessage = this.nameOfField + " required field ";
            }

            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            LocalDate date = LocalDate.now();
            Date currDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            try {
                Date sdate = formatter.parse(s);
                // Check if selected date is after current Date
                if (!this.nameOfField.equals("Date Of Birthday: ")) {
                    if (sdate.before(currDate)) {
                        if(!isTest){
                            JOptionPane.showMessageDialog(input, this.nameOfField + " Selected date is to be at least today ", "Error Dialog",
                                    JOptionPane.ERROR_MESSAGE);
                        }else{
                            errorMessage = this.nameOfField + " Selected date is to be at least today ";
                        }

                        return false;
                    }
                }
                
            } catch (ParseException e) {
                if(!isTest){
                    JOptionPane.showMessageDialog(input, this.nameOfField + " Selected date is has wrong format, please use yyyy-mm-dd format! ", "Error Dialog",
                            JOptionPane.ERROR_MESSAGE);
                }else{
                    errorMessage = this.nameOfField + " Selected date is has wrong format, please use yyyy-mm-dd format! ";
                }

                return false;
            }


        }
        return true;
    }
}
