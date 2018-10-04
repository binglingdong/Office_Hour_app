package oh.clipboard;

import djf.components.AppClipboardComponent;
import java.util.ArrayList;
import javafx.scene.control.TableView;
import oh.OfficeHoursApp;
import static oh.OfficeHoursPropertyType.OH_TAS_TABLE_VIEW;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.transactions.CutTA_Transaction;
import oh.transactions.PasteTA_Transaction;
import oh.workspace.OfficeHoursWorkspace;

/**
 *  
 * @author McKillaGorilla
 */
public class OfficeHoursClipboard implements AppClipboardComponent {
    OfficeHoursApp app;
    ArrayList<TeachingAssistantPrototype> clipboardCutAndCopiedItems;
    ArrayList<Integer> numberOfPastesForName= new ArrayList<>();                       //assigns the number behind the name pastedName
    ArrayList<Integer> numberOfPastesForEmail= new ArrayList<>();
   
    
    public OfficeHoursClipboard(OfficeHoursApp initApp) {
        app = initApp;
        clipboardCutAndCopiedItems = new ArrayList<>() ;    
        //clipboardCopiedItems = new ArrayList<>();
    }
    
    @Override
    public void cut() {
        OfficeHoursWorkspace ohws= (OfficeHoursWorkspace)app.getWorkspaceComponent();
        OfficeHoursData data= (OfficeHoursData)app.getDataComponent();
        TableView<TeachingAssistantPrototype> taTable= (TableView<TeachingAssistantPrototype>)(app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW));
        TeachingAssistantPrototype selectedTA= taTable.getSelectionModel().getSelectedItem();
        TeachingAssistantPrototype selectedTA_clone= new TeachingAssistantPrototype(selectedTA.getName(),selectedTA.getEmail(),0,selectedTA.getType());
        clipboardCutAndCopiedItems.add(selectedTA_clone);      //Add the removed TA to the clipboard
        numberOfPastesForName.add(1);                           //new TA added to the clipboard, it has not yet been pasted. 
        numberOfPastesForEmail.add(1);                          //if it's been pasted, the first one will be 1. 
        CutTA_Transaction cutTATransaction = new CutTA_Transaction(ohws, selectedTA,data);
        app.processTransaction(cutTATransaction);
    }

    @Override
    public void copy() {
        TableView<TeachingAssistantPrototype> taTable= (TableView<TeachingAssistantPrototype>)(app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW));
        TeachingAssistantPrototype selectedTA= taTable.getSelectionModel().getSelectedItem();
        TeachingAssistantPrototype selectedTA_clone= new TeachingAssistantPrototype(selectedTA.getName(),selectedTA.getEmail(),0,selectedTA.getType());
        clipboardCutAndCopiedItems.add(selectedTA_clone);      //Add the selected TA to the clipboard
        numberOfPastesForName.add(1);
        numberOfPastesForEmail.add(1);
    }
    
    @Override
    public void paste() {
        OfficeHoursWorkspace ohws= (OfficeHoursWorkspace)app.getWorkspaceComponent();
        OfficeHoursData data= (OfficeHoursData)app.getDataComponent();
        //get the last ta in the clipboard
        int index= clipboardCutAndCopiedItems.size()-1;
        TeachingAssistantPrototype taInClipboard= clipboardCutAndCopiedItems.get(index);
        PasteTA_Transaction pasteTATransaction= new PasteTA_Transaction(taInClipboard,ohws,data,numberOfPastesForName,numberOfPastesForEmail,index);
        app.processTransaction(pasteTATransaction);
    }    

    @Override
    public boolean hasSomethingToCut() {
        return ((OfficeHoursData)app.getDataComponent()).isTASelected();
    }

    @Override
    public boolean hasSomethingToCopy() {
        return ((OfficeHoursData)app.getDataComponent()).isTASelected();
    }

    @Override
    public boolean hasSomethingToPaste() {
        if ((clipboardCutAndCopiedItems != null) && (!clipboardCutAndCopiedItems.isEmpty()))
            return true;
        //else if ((clipboardCopiedItems != null) && (!clipboardCopiedItems.isEmpty()))
            //return true;
        else
            return false;
    }
}