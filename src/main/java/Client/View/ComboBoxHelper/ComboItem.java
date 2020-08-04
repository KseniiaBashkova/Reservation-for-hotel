package Client.View.ComboBoxHelper;

/**
 * ComboItem class. Represents option in select. We need this, because we want show label but save id of object.
 *
 * @author Kseniia Bashkova
 */
public class ComboItem {

    // Represents id of object.
    private String value;
    // Represents label to show, for example Customer passport
    private String label;

    public ComboItem(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getId() {
        return value;
    }

    public void setId(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    // Need for correct show label in JSelection element.
    @Override
    public String toString() {
        return label;
    }
}
