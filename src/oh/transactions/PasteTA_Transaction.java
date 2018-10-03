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
import oh.workspace.OfficeHoursWorkspace;

/**
 *
 * @author bingling.dong
 */
public class PasteTA_Transaction implements jTPS_Transaction {
    TeachingAssistantPrototype TAToBePasted;
    OfficeHoursWorkspace ohws;
    OfficeHoursData data;
    ArrayList<Integer> numberOfPastesForName;
    ArrayList<Integer> numberOfPastesForEmail;
    TeachingAssistantPrototype newTA;
    int copyIforName;
    int copyJforEmail;

    public PasteTA_Transaction(TeachingAssistantPrototype selectedTA,OfficeHoursWorkspace ohws,OfficeHoursData data
                               , ArrayList<Integer> numberOfPasteForName, ArrayList<Integer> numberOfPasteForEmail){
        this.TAToBePasted= selectedTA;
        this.ohws= ohws;
        this.data= data;
        this.numberOfPastesForName= numberOfPasteForName;
        this.numberOfPastesForEmail= numberOfPasteForEmail;
    }
    
    @Override
    public void doTransaction() {
        String name= TAToBePasted.getName();
        String email= TAToBePasted.getEmail();
        String type= TAToBePasted.getType();
        ArrayList<TeachingAssistantPrototype> copyTAs= ohws.getCopyTAs();
        ArrayList<String> allNames= new ArrayList<>();
        ArrayList<String> allEmails= new ArrayList<>();
        
        for(TeachingAssistantPrototype ta: copyTAs){
            allNames.add(ta.getName());
            allEmails.add(ta.getEmail());
        }
        
        copyIforName=numberOfPastesForName.get(numberOfPastesForName.size()-1);       //keeps a copy for undo
        int i=numberOfPastesForName.get(numberOfPastesForName.size()-1);          //get the number of pastes of the last ta.
        while(allNames.contains(name)){
            name= TAToBePasted.getName()+i;
            i=i+1;
        }
         
        numberOfPastesForName.remove(numberOfPastesForName.size()-1);       //add the number of pastes for the next paste. 
        numberOfPastesForName.add(i);
        
        
        copyJforEmail=numberOfPastesForEmail.get(numberOfPastesForEmail.size()-1);       //keeps a copy for undo
        int j=numberOfPastesForEmail.get(numberOfPastesForEmail.size()-1);          //get the number of pastes of the last ta.
        while(allEmails.contains(email)){
            String e= TAToBePasted.getEmail();
            String pre= e.substring(0,e.indexOf('@'));
            String post= e.substring(e.indexOf('@'));
            email= pre+j+post;
            j=j+1;
        }
         
        numberOfPastesForEmail.remove(numberOfPastesForEmail.size()-1);       //add the number of pastes for the next paste. 
        numberOfPastesForEmail.add(j);

        newTA= new TeachingAssistantPrototype(name,email,0,type);
        ohws.getCopyTAs().add(newTA);
        ohws.updateTaTableForRadio(data.getTeachingAssistants());  
    }

    @Override
    public void undoTransaction() {
        ohws.getCopyTAs().remove(newTA);
        numberOfPastesForName.remove(numberOfPastesForName.size()-1);       //subtract the number of pastes for the next paste. 
        numberOfPastesForName.add(copyIforName);
        
        numberOfPastesForEmail.remove(numberOfPastesForEmail.size()-1);
        numberOfPastesForEmail.add(copyJforEmail);

        ohws.updateTaTableForRadio(data.getTeachingAssistants());
    }
}
