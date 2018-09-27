package oh.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import oh.OfficeHoursApp;
import static oh.OfficeHoursPropertyType.OH_ADD_TA_BUTTON;
import static oh.OfficeHoursPropertyType.OH_EMAIL_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_NAME_TEXT_FIELD;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursFoolproofDesign implements FoolproofDesign {
    OfficeHoursApp app;
    
    public OfficeHoursFoolproofDesign(OfficeHoursApp initApp) {
        app = initApp;
    }

    @Override
    public void updateControls() {
        AppGUIModule gui = app.getGUIModule();
        enableIfInUse(OH_NAME_TEXT_FIELD,OH_EMAIL_TEXT_FIELD,OH_ADD_TA_BUTTON,true);
    }
    private void enableIfInUse(Object NameField,Object EmailField,Object button, boolean setToDisable) {
        AppGUIModule gui = app.getGUIModule();
        boolean noName=((TextField)gui.getGUINode(NameField)).getText().equals("");
        boolean noEmail=((TextField)gui.getGUINode(EmailField)).getText().equals("");
        
        Button addButton=(Button) gui.getGUINode(button);
        if(noName||noEmail){
            addButton.setDisable(setToDisable);
            
        }
        else{
            addButton.setDisable(!setToDisable);
        }
    }
}