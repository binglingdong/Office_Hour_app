/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.workspace.OfficeHoursWorkspace;

/**
 *
 * @author bingling.dong
 */
public class EditTA_Transaction implements jTPS_Transaction {
    String newName;
    String newEmail;
    String newType;
    OfficeHoursWorkspace ohws;
    TeachingAssistantPrototype selectedTA;
    TableView<TeachingAssistantPrototype> taTable;
    OfficeHoursData data;
    String oriName;
    String oriEmail;
    String oriType;
    
    public EditTA_Transaction(String newName, String newEmail, String newType, 
                              TeachingAssistantPrototype selectedTA,TableView<TeachingAssistantPrototype> taTable,
                              OfficeHoursWorkspace ohws, OfficeHoursData data){
        this.newEmail= newEmail;
        this.newName= newName;
        this.newType= newType;
        this.selectedTA= selectedTA;
        this.taTable=taTable;
        this.ohws=ohws;
        this.data= data;
        oriName= selectedTA.getName();
        oriEmail= selectedTA.getEmail();
        oriType= selectedTA.getType();
        
    }
    
    @Override
    public void doTransaction() {
        selectedTA.setName(newName);
        selectedTA.setEmail(newEmail);
        selectedTA.setType(newType);
        ohws.updateTaTableForRadio(taTable.getItems());
        ohws.resetOHToMatchTA(data,data.getOfficeHours());
        ohws.removeOHToMatchTA(data, taTable.getItems(), data.getOfficeHours());
    }

    @Override
    public void undoTransaction() {
        selectedTA.setType(oriType);
        selectedTA.setEmail(oriEmail);
        selectedTA.setName(oriName);
        ohws.updateTaTableForRadio(taTable.getItems());
        ohws.resetOHToMatchTA(data,data.getOfficeHours());
        ohws.removeOHToMatchTA(data, taTable.getItems(), data.getOfficeHours());
    }
    
}
