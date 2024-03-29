/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;

/**
 *
 * @author bingling.dong
 */
public class AddOH_Transaction implements jTPS_Transaction {
    OfficeHoursData data;
    TimeSlot timeSlot;
    DayOfWeek theDay;
    TeachingAssistantPrototype TA;
    
    public AddOH_Transaction(OfficeHoursData initData, TimeSlot initTime, TeachingAssistantPrototype TA,DayOfWeek theDay) {
        data = initData;
        timeSlot = initTime;
        this.theDay=theDay;
        this.TA= TA;
    }

    @Override
    public void doTransaction() {
        //getting everysingle ta on that timeslot, then get the array for the tas that are on a certain day. 
        if(!timeSlot.getTas().get(theDay).contains(TA)){//if the TA is not in the cell yet.
            data.addOH(timeSlot,TA,theDay);  //then add TA
        }
        else{
            data.removeOH(timeSlot, TA, theDay);//else, meaning the TA already exists in the cell, then remove it. 
        }

    }

    @Override
    public void undoTransaction() {
        //In terms of the UNDO, if the TA currently exists in the cell. Then remove it.
        if(timeSlot.getTas().get(theDay).contains(TA)){
            data.removeOH(timeSlot,TA,theDay);
        }
        
        //else, meaning that if the TA is not currently in the cell. Which means, we just removed it.
        else{
             data.addOH(timeSlot,TA,theDay);  //Now we add it back. 
        }
    }
}
