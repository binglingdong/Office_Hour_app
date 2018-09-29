package oh.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import oh.OfficeHoursApp;
import static oh.OfficeHoursPropertyType.OH_ADD_TA_BUTTON;
import static oh.OfficeHoursPropertyType.OH_EMAIL_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_NAME_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_TYPE_ALL;
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
            Name.setDisable(false);
            Email.setDisable(false);
            if(Name.getText().equals("")||Email.getText().equals("")){
                addButton.setDisable(true);

            }
            else{
                addButton.setDisable(false);
            }
        }
    }
}