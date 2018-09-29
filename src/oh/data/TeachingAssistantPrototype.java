package oh.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Richard McKenna
 */
public class TeachingAssistantPrototype {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty name;
    private final StringProperty email;
    private final IntegerProperty slots;
    private final StringProperty type;

    /**
     * Constructor initializes both the TA name and email.
     * @param initName
     * @param initEmail
     * @param initSlots
     */
    public TeachingAssistantPrototype(String initName, String initEmail, Integer initSlots, String initType) {
        name = new SimpleStringProperty(initName);
        email= new SimpleStringProperty(initEmail);
        slots= new SimpleIntegerProperty(initSlots);
        type= new SimpleStringProperty(initType);
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    public String getName() {
        return name.get();
    }

    public void setName(String initName) {
        name.set(initName);
    }
    
    public StringProperty nameProperty() {
        return name;
    }
    
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String initEmail) {
        email.set(initEmail);
    }
    
    public StringProperty emailProperty() {
        return email;
    }
    
    public int getSlots() {
        return slots.get();
    }

    public void setSlots(int initSlots) {
        slots.set(initSlots);
    }
    
    public IntegerProperty slotsProperty() {
        return slots;
    }
    
    public String getType() {
        return type.get();
    }

    public void setType(int initType) {
        slots.set(initType);
    }
    
    public StringProperty typeProperty() {
        return type;
    }
    
    @Override
    public String toString() {
        return name.getValue();
    }
}