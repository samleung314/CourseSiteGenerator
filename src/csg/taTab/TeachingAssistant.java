/**
 * @author Samson Leung 110490519
 */
package csg.taTab;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Richard McKenna
 */
public class TeachingAssistant<E extends Comparable<E>> implements Comparable<E>  {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final BooleanProperty undergrad;
    private final StringProperty name;
    private final StringProperty email;

    /**
     * Constructor initializes both the TA name and email.
     */
    public TeachingAssistant(boolean initUndergrad, String initName, String initEmail) {
        undergrad = new SimpleBooleanProperty(initUndergrad);
        name = new SimpleStringProperty(initName);
        email = new SimpleStringProperty(initEmail);
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    public String getName() {
        return name.get();
    }

    public void setName(String initName) {
        name.set(initName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String initEmail) {
        email.set(initEmail);
    }

    public boolean isUndergrad() {
        return undergrad.get();
    }
    
    public void setUndergrad(boolean underGrad) {
        undergrad.set(underGrad);
    }

    @Override
    public int compareTo(E otherTA) {
        return getName().compareTo(((TeachingAssistant)otherTA).getName());
    }
    
    @Override
    public String toString() {
        return name.getValue();
    }
}