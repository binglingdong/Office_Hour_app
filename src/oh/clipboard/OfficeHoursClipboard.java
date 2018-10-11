package oh.clipboard;

import djf.components.AppClipboardComponent;
import java.util.ArrayList;
import javafx.scene.control.TableView;
import oh.OfficeHoursApp;
import static oh.OfficeHoursPropertyType.OH_TAS_TABLE_VIEW;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.transactions.CutTA_Transaction;
import oh.transactions.PasteTA_Copy_Transaction;
import oh.transactions.PasteTA_Cut_Transaction;
import oh.workspace.OfficeHoursWorkspace;

/**
 *  
 * @author McKillaGorilla
 */
public class OfficeHoursClipboard implements AppClipboardComponent {
    OfficeHoursApp app;
    private ArrayList<TeachingAssistantPrototype> clipboardCutAndCopiedItems;
    private ArrayList<Integer> numberOfPastesForName= new ArrayList<>();                       //assigns the number behind the name pastedName
    private ArrayList<Integer> numberOfPastesForEmail= new ArrayList<>();
    private ArrayList<Boolean> cutOrCopy = new ArrayList<>();         // if true= cut, if false= copy
   
    
    public OfficeHoursClipboard(OfficeHoursApp initApp) {
        app = initApp;
        clipboardCutAndCopiedItems = new ArrayList<>();
    }
    
    @Override
    public void cut() {
        OfficeHoursWorkspace ohws= (OfficeHoursWorkspace)app.getWorkspaceComponent();
        OfficeHoursData data= (OfficeHoursData)app.getDataComponent();
        TableView<TeachingAssistantPrototype> taTable= (TableView<TeachingAssistantPrototype>)(app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW));
        TeachingAssistantPrototype selectedTA= taTable.getSelectionModel().getSelectedItem();
        clipboardCutAndCopiedItems.add(selectedTA);      //Add the removed TA to the clipboard
        cutOrCopy.add(true);
        numberOfPastesForName.add(1);                           //new TA added to the clipboard, it has not yet been pasted. 
        numberOfPastesForEmail.add(1);                          //if it's been pasted, the first one will be 1. 
        CutTA_Transaction cutTATransaction = new CutTA_Transaction(ohws, selectedTA,data);
        app.processTransaction(cutTATransaction);
    }

    @Override
    public void copy() {
        TableView<TeachingAssistantPrototype> taTable= (TableView<TeachingAssistantPrototype>)(app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW));
        TeachingAssistantPrototype selectedTA= taTable.getSelectionModel().getSelectedItem();
        TeachingAssistantPrototype TA_clone= new TeachingAssistantPrototype(selectedTA.getName(),selectedTA.getEmail(),0,selectedTA.getType());
        clipboardCutAndCopiedItems.add(TA_clone);      //clone here is to ensure if you change the name of the original ta, the 
                                                       // one to be pasted will stay the same.
        cutOrCopy.add(false);
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
        TeachingAssistantPrototype TA_clone= new TeachingAssistantPrototype(taInClipboard.getName(),taInClipboard.getEmail(),0,taInClipboard.getType());
        
        boolean whetherCut= cutOrCopy.get(index);
        boolean alreadyExist=false;
        
        if(ohws.getCopyTAs().contains(taInClipboard)){      //if you added another ta with the same name or you undo
            alreadyExist=true;
        }
        else{
            for(TeachingAssistantPrototype ta: ohws.getCopyTAs()){
                if (ta.getName().equalsIgnoreCase(taInClipboard.getName())||
                        ta.getEmail().equalsIgnoreCase(taInClipboard.getEmail())){
                    alreadyExist=true;
                    break;
                }
            }
        }
        
        if(alreadyExist==false){    // if the ta didnt exist, add it like normal. either with or without oh
            if(whetherCut==false){  //meaning the item was copied
                PasteTA_Copy_Transaction pasteTATransaction= new PasteTA_Copy_Transaction(TA_clone,ohws,data,numberOfPastesForName,numberOfPastesForEmail,index);
                app.processTransaction(pasteTATransaction);
            }
            else{                   //meaning the item was cutted
                PasteTA_Cut_Transaction pasteTACutTransaction = new PasteTA_Cut_Transaction(taInClipboard,ohws,data);
                app.processTransaction(pasteTACutTransaction);
            }
        }
        else{       //if the ta already exists, add it all with no oh paste
            PasteTA_Copy_Transaction pasteTATransaction= new PasteTA_Copy_Transaction(TA_clone,ohws,data,numberOfPastesForName,numberOfPastesForEmail,index);
            app.processTransaction(pasteTATransaction);
        }
        
       
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
            {return true;}
        //else if ((clipboardCopiedItems != null) && (!clipboardCopiedItems.isEmpty()))
            //return true;
        else
            return false;
    }
}