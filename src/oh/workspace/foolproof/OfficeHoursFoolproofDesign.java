package oh.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import oh.OfficeHoursApp;
import static oh.OfficeHoursPropertyType.OH_ADD_TA_BUTTON;
import static oh.OfficeHoursPropertyType.OH_EMAIL_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_NAME_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_TYPE_ALL;
import oh.data.TeachingAssistantPrototype;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursFoolproofDesign implements FoolproofDesign {
    OfficeHoursApp app;
    ArrayList <TeachingAssistantPrototype> copyTAs;
    
    public OfficeHoursFoolproofDesign(OfficeHoursApp initApp, ArrayList <TeachingAssistantPrototype> copyTAs) {
        app = initApp;
        this.copyTAs= copyTAs;
        
    }

    @Override
    public void updateControls() {
        enableIfInUse();
    }
    private void enableIfInUse() {
        AppGUIModule gui = app.getGUIModule();
        TextField Name=(TextField)gui.getGUINode(OH_NAME_TEXT_FIELD);
        TextField Email=(TextField)gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        boolean allIsSelected= ((RadioButton)gui.getGUINode(OH_TYPE_ALL)).isSelected();
        Button addButton=(Button) gui.getGUINode(OH_ADD_TA_BUTTON);
        
        if(allIsSelected){
            addButton.setDisable(true);
            Name.setDisable(true);
            Email.setDisable(true);
        }
        else{
            //re-active the textfield
            Name.setDisable(false);
            Email.setDisable(false);
            
            //check to see if the text in the textfield is valid
            boolean validEmail= checkForRepeatedAndValidEmail(Email.getText());
            if(!validEmail){
                Email.setStyle("-fx-text-fill: red;");
            }
            else Email.setStyle("-fx-text-fill: black;");

            boolean validName= checkForRepeatedName(Name.getText());
            if(!validName){
                Name.setStyle("-fx-text-fill: red;");
            }
            else Name.setStyle("-fx-text-fill: black;");

            if(!validEmail||!validName){
                addButton.setDisable(true);     // disable the button if not valid
            }
            else addButton.setDisable(false);   // if both are valid, then able the button
        }
    }
    
    public boolean checkForRepeatedName(String newName){
        if(newName.isEmpty())return false;
        for(TeachingAssistantPrototype ta:copyTAs){
            if(ta.getName().equalsIgnoreCase(newName)) return false;
        }
        return true;
    }
    
    public boolean checkForRepeatedAndValidEmail(String newEmail){
        if(newEmail.isEmpty())return false;
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if(!VALID_EMAIL_ADDRESS_REGEX.matcher(newEmail).matches()){
            return false;
        }
        for(TeachingAssistantPrototype ta:copyTAs){
            if(ta.getEmail().equalsIgnoreCase(newEmail)) return false;
        }
        
        return true;
    }
}