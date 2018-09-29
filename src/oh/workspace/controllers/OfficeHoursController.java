package oh.workspace.controllers;

import djf.modules.AppGUIModule;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import oh.OfficeHoursApp;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.transactions.AddTA_Transaction;
import djf.ui.dialogs.AppDialogsFacade;
import static djf.AppPropertyType.INVALID_EMAIL_TITLE;
import static djf.AppPropertyType.INVALID_EMAIL_CONTENT;
import java.util.ArrayList;
import javafx.scene.control.RadioButton;
import static oh.OfficeHoursPropertyType.*;
import oh.workspace.OfficeHoursWorkspace;


/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursController {

    OfficeHoursApp app;

    public OfficeHoursController(OfficeHoursApp initApp) {
        app = initApp;
    }

    public void processAddTA(ArrayList<TeachingAssistantPrototype> copyTAs, OfficeHoursWorkspace ohws) {
        
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String email= emailTF.getText();
        
        OfficeHoursData data=(OfficeHoursData)app.getDataComponent();
   
        //testing for repeating email
        
        boolean repeatingEmail= false;
        boolean repeatingName =false;
        for(TeachingAssistantPrototype ta:copyTAs){
            if(ta.getEmail().equalsIgnoreCase(email)) repeatingEmail=true; 
            if(ta.getName().equalsIgnoreCase(name)) repeatingName=true;
        }
        
        if(repeatingName){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(),REPEATING_NAME_FOR_ADDING_TA_TITLE,REPEATING_NAME_FOR_ADDING_TA_MESSAGE);
        }
        else if (repeatingEmail){
            AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(),REPEATING_EMAIL_FOR_ADDING_TA_TITLE,REPEATING_EMAIL_FOR_ADDING_TA_MESSAGE);
        }
        else{
            Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            
            if(VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()){
                RadioButton graduate= ((RadioButton)gui.getGUINode(OH_TYPE_GRADUATE));
                TeachingAssistantPrototype ta;
                if(graduate.isSelected()){
                    ta = new TeachingAssistantPrototype(name,email,0,"Graduate");
                }
                else{
                    ta = new TeachingAssistantPrototype(name,email,0,"Undergraduate");
                }

                AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta, copyTAs,ohws);
                app.processTransaction(addTATransaction);
                
            }
            else{
                AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(),INVALID_EMAIL_TITLE, INVALID_EMAIL_CONTENT);
            }
        }
        
        // NOW CLEAR THE TEXT FIELDS
        nameTF.setText("");
        emailTF.setText("");
        nameTF.requestFocus();
        emailTF.requestFocus();
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
}
