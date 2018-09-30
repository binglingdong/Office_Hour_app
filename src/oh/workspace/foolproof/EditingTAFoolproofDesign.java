package oh.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import oh.OfficeHoursApp;
import static oh.OfficeHoursPropertyType.OH_ADD_TA_BUTTON;
import static oh.OfficeHoursPropertyType.OH_EMAIL_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_NAME_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_TYPE_ALL;
import oh.data.TeachingAssistantPrototype;
import oh.workspace.OfficeHoursWorkspace;

/**
 *
 * @author McKillaGorilla
 */
public class EditingTAFoolproofDesign implements FoolproofDesign {
    OfficeHoursWorkspace ohws;
    TextField nameTf;
    TextField emailTf; 
    Dialog dialog;
    TeachingAssistantPrototype selectedTA;
    ButtonType okButton;
    
    public EditingTAFoolproofDesign(OfficeHoursWorkspace ohws, TextField nameTf,TextField emailTf, 
                                    Dialog dialog, TeachingAssistantPrototype selectedTA, ButtonType okButton) {
        this.ohws=ohws;
        this.nameTf= nameTf;
        this.emailTf= emailTf;
        this.dialog= dialog;
        this.selectedTA= selectedTA;
        this.okButton=okButton;
    }

    @Override
    public void updateControls() {
        enableIfInUse();
    }
    
    private void enableIfInUse() {
        String newName= nameTf.getText();
        String newEmail= emailTf.getText();
        boolean validName;
        boolean validEmail;
        
        if(!newName.equals(selectedTA.getName())){
            validName= checkForRepeatedName(newName);
            if(!validName) nameTf.setStyle("-fx-text-fill: red;");
            else nameTf.setStyle("-fx-text-fill: black;");
        }
        else {//if the name didnt change. 
            nameTf.setStyle("-fx-text-fill: black;");
            validName= true;
        }                   
        
        
        if(!newEmail.equals(selectedTA.getEmail())){
            validEmail= checkForRepeatedAndValidEmail(newEmail);
            if(!validEmail) emailTf.setStyle("-fx-text-fill: red;");
            else  emailTf.setStyle("-fx-text-fill: black;"); 
        }
        else {
            emailTf.setStyle("-fx-text-fill: black;");
            validEmail= true;
        }
        
        if(newName.equals(selectedTA.getName())&& newEmail.equals(selectedTA.getEmail())){
            dialog.getDialogPane().lookupButton(okButton).setDisable(true);
        }
        else{
            if(!validName||!validEmail){
                dialog.getDialogPane().lookupButton(okButton).setDisable(true);
            }
            else dialog.getDialogPane().lookupButton(okButton).setDisable(false);
        }
    }
    
    public boolean checkForRepeatedName(String newName){
        if(newName.isEmpty())return false;
        for(TeachingAssistantPrototype ta:ohws.getCopyTAs()){
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
        for(TeachingAssistantPrototype ta:ohws.getCopyTAs()){
            if(ta.getEmail().equalsIgnoreCase(newEmail)) return false;
        }
        
        return true;
    }
}