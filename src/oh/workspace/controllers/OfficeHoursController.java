package oh.workspace.controllers;

import djf.modules.AppGUIModule;
import javafx.scene.control.TextField;
import oh.OfficeHoursApp;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.transactions.AddTA_Transaction;
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
        OfficeHoursData data=(OfficeHoursData)app.getDataComponent();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String email= emailTF.getText();
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
        
        // NOW CLEAR THE TEXT FIELDS
        nameTF.setText("");
        emailTF.setText("");
        nameTF.requestFocus();
        emailTF.requestFocus();
        
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
}
