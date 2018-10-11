/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.workspace.OfficeHoursWorkspace;

/**
 *
 * @author bingling.dong
 */
public class CutTA_Transaction implements jTPS_Transaction{
    OfficeHoursWorkspace ohws;
    TeachingAssistantPrototype selectedTA;
    OfficeHoursData data;
    
    public CutTA_Transaction(OfficeHoursWorkspace ohws, TeachingAssistantPrototype selectedTA,OfficeHoursData data){
        this.ohws=ohws;
        this.selectedTA= selectedTA;
        this.data=data;
    }

    @Override
    public void doTransaction() {
        //Since when you remove a TA from the backup copy TA list, you are not removing the actual oh from the backup oh
        // list, you are just simply updating the list that's showing to the user, you can just remove the ta and add it back
        // anytime you want. Their oh will always be in the backup oh sheet. 
        ohws.getCopyTAs().remove(selectedTA);   
        ohws.updateTaTableForRadio(data.getTeachingAssistants());
        ohws.resetOHToMatchTA(data, data.getOfficeHours());
        ohws.removeOHToMatchTA(data, data.getTeachingAssistants(), data.getOfficeHours());
        ohws.updateBgColorForCell();
    }

    @Override
    public void undoTransaction() {
        ohws.getCopyTAs().add(selectedTA);      //add the ta back to the copy list. 
        ohws.updateTaTableForRadio(data.getTeachingAssistants()); // and update it
        ohws.resetOHToMatchTA(data, data.getOfficeHours());
        ohws.removeOHToMatchTA(data, data.getTeachingAssistants(), data.getOfficeHours());
        ohws.updateBgColorForCell();
    }
    
}
