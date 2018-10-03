/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import java.util.ArrayList;
import jtps.jTPS_Transaction;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;
import oh.workspace.OfficeHoursWorkspace;

/**
 *
 * @author bingling.dong
 */
public class CutTA_Transaction implements jTPS_Transaction{
    OfficeHoursWorkspace ohws;
    TeachingAssistantPrototype selectedTA;
    OfficeHoursData data;
    ArrayList<TimeSlot> removedTime= new ArrayList<>();
    ArrayList<DayOfWeek> removedDay= new ArrayList<>();
    
    
    
    public CutTA_Transaction(OfficeHoursWorkspace ohws, TeachingAssistantPrototype selectedTA,OfficeHoursData data){
        this.ohws=ohws;
        this.selectedTA= selectedTA;
        this.data=data;
    }

    @Override
    public void doTransaction() {
        removedTime.clear();
        removedDay.clear();
        //removed the ta and update the table
        ohws.getCopyTAs().remove(selectedTA);
        selectedTA.setSlots(0);
        ohws.updateTaTableForRadio(data.getTeachingAssistants());
        //remove the ta's office hours, and store them in the removedtime and removedday arraylists. 
        ArrayList <TimeSlot> copyOH= ohws.getCopyOH();
        for(TimeSlot s: copyOH){
            for(DayOfWeek d: DayOfWeek.values()){
                ArrayList<TeachingAssistantPrototype> listOfTaForThatDay= s.getTas().get(d);
                ArrayList<TeachingAssistantPrototype> listClone= (ArrayList)listOfTaForThatDay.clone();
                for(TeachingAssistantPrototype ta:listClone){
                    if(ta==selectedTA){
                        listOfTaForThatDay.remove(selectedTA);
                        removedTime.add(s);
                        removedDay.add(d);
                    }
                }
            }
        }
        ohws.resetOHToMatchTA(data, data.getOfficeHours());
        ohws.removeOHToMatchTA(data, data.getTeachingAssistants(), data.getOfficeHours());
    }

    @Override
    public void undoTransaction() {
        ohws.getCopyTAs().add(selectedTA);      //add the ta back to the copy list. 
        ohws.updateTaTableForRadio(data.getTeachingAssistants()); // and update it
        
        for(int i=0 ; i<removedTime.size(); i++){
            data.addOH(removedTime.get(i), selectedTA, removedDay.get(i));
        }
        removedTime.clear();
        removedDay.clear();
        ohws.resetOHToMatchTA(data, data.getOfficeHours());
        ohws.removeOHToMatchTA(data, data.getTeachingAssistants(), data.getOfficeHours());
    }
    
}
