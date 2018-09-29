package oh.transactions;

import java.util.ArrayList;
import jtps.jTPS_Transaction;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class AddTA_Transaction implements jTPS_Transaction {
    OfficeHoursData data;
    TeachingAssistantPrototype ta;
    ArrayList<TeachingAssistantPrototype> copyTAs;
    
    public AddTA_Transaction(OfficeHoursData initData, TeachingAssistantPrototype initTA, ArrayList<TeachingAssistantPrototype> copyTAs) {
        data = initData;
        ta = initTA;
        this.copyTAs= copyTAs;
    }

    @Override
    public void doTransaction() {
        data.addTA(ta);
        copyTAs.add(ta);
    }

    @Override
    public void undoTransaction() {
        data.removeTA(ta);
        copyTAs.remove(ta);
    }
}
