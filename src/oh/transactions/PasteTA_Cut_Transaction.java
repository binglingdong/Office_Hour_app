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
public class PasteTA_Cut_Transaction implements jTPS_Transaction{
    TeachingAssistantPrototype selectedTA;
    OfficeHoursWorkspace ohws;
    OfficeHoursData data;
    
    public PasteTA_Cut_Transaction(TeachingAssistantPrototype selectedTA,OfficeHoursWorkspace ohws,OfficeHoursData data){
        this.selectedTA= selectedTA;
        this.ohws= ohws;
        this.data= data;
    }
    
    @Override
    public void doTransaction() {
        ohws.getCopyTAs().add(selectedTA);
        ohws.updateTaTableForRadio(data.getTeachingAssistants());
        ohws.resetOHToMatchTA(data, data.getOfficeHours());
        ohws.removeOHToMatchTA(data, data.getTeachingAssistants(), data.getOfficeHours());
        ohws.updateBgColorForCell();
    }

    @Override
    public void undoTransaction() {
        ohws.getCopyTAs().remove(selectedTA);
        ohws.updateTaTableForRadio(data.getTeachingAssistants());
        ohws.resetOHToMatchTA(data, data.getOfficeHours());
        ohws.removeOHToMatchTA(data, data.getTeachingAssistants(), data.getOfficeHours());
        ohws.updateBgColorForCell();
    }
}
