package oh.transactions;

import java.util.ArrayList;
import javafx.scene.control.ToggleGroup;
import jtps.jTPS_Transaction;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.workspace.OfficeHoursWorkspace;

/**
 *
 * @author McKillaGorilla
 */
public class AddTA_Transaction implements jTPS_Transaction {
    OfficeHoursData data;
    TeachingAssistantPrototype ta;
    ArrayList<TeachingAssistantPrototype> copyTAs;
    OfficeHoursWorkspace ohws;
    
    public AddTA_Transaction(OfficeHoursData initData, TeachingAssistantPrototype initTA, ArrayList<TeachingAssistantPrototype> copyTAs,
                            OfficeHoursWorkspace ws) {
        data = initData;
        ta = initTA;
        this.copyTAs= copyTAs;
        ohws= ws;
    }

    @Override
    public void doTransaction() {
        data.addTA(ta);
        if (!copyTAs.contains(ta))copyTAs.add(ta);
        ohws.updateTaTableForRadio(data.getTeachingAssistants());
    }

    @Override
    public void undoTransaction() {
        data.removeTA(ta);
        copyTAs.remove(ta);
        ohws.updateTaTableForRadio(data.getTeachingAssistants());
    }
}
