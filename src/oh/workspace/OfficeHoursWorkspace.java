package oh.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import oh.OfficeHoursApp;
import oh.OfficeHoursPropertyType;
import static oh.OfficeHoursPropertyType.*;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;
import oh.workspace.controllers.OfficeHoursController;
import oh.workspace.foolproof.OfficeHoursFoolproofDesign;
import static oh.workspace.style.OHStyle.*;
import djf.ui.dialogs.AppDialogsFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ToggleGroup;
import oh.transactions.AddOH_Transaction;
import oh.workspace.dialogs.OfficeHoursDialogs;

/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursWorkspace extends AppWorkspaceComponent {
    private ArrayList <TeachingAssistantPrototype> copyTAs= new ArrayList<>();
    private ArrayList <TimeSlot> copyOH= new ArrayList<>();
    private ToggleGroup taTypes= new ToggleGroup();

    public OfficeHoursWorkspace(OfficeHoursApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        // SETUP FOOLPROOF DESIGN FOR THIS APP
        initFoolproofDesign();
        
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();

        // INIT THE HEADER ON THE LEFT
        VBox leftPane = ohBuilder.buildVBox(OH_LEFT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox tasHeaderBox = ohBuilder.buildHBox(OH_TAS_HEADER_PANE, leftPane, CLASS_OH_BOX, ENABLED);
        ohBuilder.buildLabel(OfficeHoursPropertyType.OH_TAS_HEADER_LABEL, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        
        //CREATE THE RADIO BUTTONS
        ohBuilder.buildRadioButton(OfficeHoursPropertyType.OH_TYPE_ALL, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED,taTypes,true);
        ohBuilder.buildRadioButton(OfficeHoursPropertyType.OH_TYPE_UNDERGRADUATE, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED,taTypes,false);
        ohBuilder.buildRadioButton(OfficeHoursPropertyType.OH_TYPE_GRADUATE, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED,taTypes,false);
        
        
        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = ohBuilder.buildTableView(OH_TAS_TABLE_VIEW, leftPane, CLASS_OH_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn nameColumn = ohBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/4.0));
        
        TableColumn emailColumn= ohBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/3.0));
        
        //COUNT THE TIME SLOT OF A TA
        TableColumn timeSlotColumn = ohBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        timeSlotColumn.setCellValueFactory(new PropertyValueFactory<String, Integer>("slots"));
        timeSlotColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/5.0));
        
        TableColumn typeColumn = ohBuilder.buildTableColumn(OH_TYPE_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/4.0));
        
        // ADD BOX FOR ADDING A TA
        HBox taBox = ohBuilder.buildHBox(OH_ADD_TA_PANE, leftPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_OH_BUTTON, ENABLED);
        
     
        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(taTable, Priority.ALWAYS);
        for (int i = 0; i < taTable.getColumns().size(); i++) {
            ((TableColumn)taTable.getColumns().get(i)).setSortable(false);
        }
        
        taTable.setOnMouseClicked(e ->{
            OfficeHoursData data=(OfficeHoursData)app.getDataComponent();
            if(e.getClickCount()==2){
                if(data.isTASelected()){
                    OfficeHoursDialogs.editTADialog(app.getGUIModule().getWindow(), EDIT_TITLE, EDIT_HEADER, taTable, this, app.getFoolproofModule());
                    
                }
            }
        });
        
        
        
        
        
        
        
        
        
        
        ////////-------------------------------RIGHT TABLE------------------------------------////////
        VBox rightPane = ohBuilder.buildVBox(OH_RIGHT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox officeHoursHeaderBox = ohBuilder.buildHBox(OH_OFFICE_HOURS_HEADER_PANE, rightPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);

        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = ohBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, rightPane, CLASS_OH_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        TableColumn startTimeColumn = ohBuilder.buildTableColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN);
        TableColumn endTimeColumn = ohBuilder.buildTableColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN);
        TableColumn mondayColumn = ohBuilder.buildTableColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn tuesdayColumn = ohBuilder.buildTableColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn wednesdayColumn = ohBuilder.buildTableColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn thursdayColumn = ohBuilder.buildTableColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn fridayColumn = ohBuilder.buildTableColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("endTime"));
        mondayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("monday"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("tuesday"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("wednesday"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("thursday"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("friday"));
        for (int i = 0; i < officeHoursTable.getColumns().size(); i++) {
            ((TableColumn)officeHoursTable.getColumns().get(i)).prefWidthProperty().bind(officeHoursTable.widthProperty().multiply(1.0/7.0));
        }
       
        
        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(officeHoursTable, Priority.ALWAYS);

        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, rightPane);
        sPane.setDividerPositions(.4);
        workspace = new BorderPane();

        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane)workspace).setCenter(sPane);
        
          
        
        ///////////////////////////////////////// GENERATES DATA FOR THE OH/////////////////////////////////////
        TableView<TimeSlot> OHTableView = (TableView) app.getGUIModule().getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
       // OHTableView
        OHTableView.setOnMouseClicked(e->{     
            ObservableList list= officeHoursTable.getSelectionModel().getSelectedCells();
            if(!list.isEmpty()){
                TablePosition tp = (TablePosition) list.get(0);
                int cellCol= tp.getColumn();
                int cellRow= tp.getRow();
                OfficeHoursData data=(OfficeHoursData)app.getDataComponent();
                boolean validCol= data.isDayOfWeekColumn(cellCol);

                // SETUP THE COPYOH IF IS EMPTY (FIRST TIME)
                if(copyOH.isEmpty()){
                     initCopyOH(data.getStartHour(),data.getEndHour());
                }
                //If the ta is selected
                if(validCol&& data.isTASelected()){
                    TeachingAssistantPrototype selectedTA=taTable.getSelectionModel().getSelectedItem();
                    DayOfWeek day= data.getColumnDayOfWeek(cellCol);
                    TimeSlot timeSlot = OHTableView.getSelectionModel().getSelectedItem();
                    TimeSlot copy_timeSlot= getTimeSlotInCopyOH(timeSlot);
                    //Create a transaction for adding timeslots, and process the transaction
                    AddOH_Transaction addOH_transaction = new AddOH_Transaction(data,timeSlot,selectedTA ,day, copy_timeSlot,copyOH, OHTableView.getItems(), this);
                    app.processTransaction(addOH_transaction);
                }
                else{
                    //no TA is selected
                    AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(),INVALID_COMMAND_TITLE, DIDNT_CHOOSE_TA_INVALID_CLICK_CONTENT);
                }
                ((TableView)(app.getGUIModule().getGUINode(OH_OFFICE_HOURS_TABLE_VIEW))).refresh();
            }
            
        }); 
        

        
        /////////////////AFTER SETTING UP THE TABLE, WE NEED TO LISTEN FOR THE CHANGE IN THE RADIO BUTTON TO CHANGE////////////
        ////////////////////////////////////////////THE TABLEVIEWS ACCORDINGLY//////////////////////////////////////////
        ObservableList<TeachingAssistantPrototype>  allTAs= taTable.getItems();
        ObservableList<TimeSlot> originalOH= OHTableView.getItems();
        

        //add a listener for toggle group      
        taTypes.selectedToggleProperty().addListener(e->{
            OfficeHoursData data=(OfficeHoursData)app.getDataComponent();
            app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
            updateTaTableForRadio(allTAs);
            
            //After they updated the taTable on the left
            //first reset the originalOH to copyOH
            resetOHToMatchTA(data,originalOH);
            
            //remove the ones that are not in the ta list. 
            removeOHToMatchTA(data,allTAs,originalOH);
            
            OHTableView.refresh();
        });
        
    }

    
    
    
    
    //////////////////////-------------------------------------------------------------------/////////////////////
    private void initControllers() {
        OfficeHoursController controller = new OfficeHoursController((OfficeHoursApp) app);
        AppGUIModule gui = app.getGUIModule();
        
        TextField nameTextField= (TextField)gui.getGUINode(OH_NAME_TEXT_FIELD);
        TextField emailTextField= (TextField)gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        Button addTAButton= (Button) gui.getGUINode(OH_ADD_TA_BUTTON);
        
        //Listen to the change made in the text fields. 
        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)){
                
                app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
            }           
        });
       
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            
            if(!newValue.equals(oldValue)){
                
                app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
            }           
        });
        
        //update the button after every action. 
        //update the copylist also
        (nameTextField).setOnAction(e -> {
            app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
            if(!addTAButton.isDisabled()){
                controller.processAddTA(copyTAs, this);
            }
        });
        
        (emailTextField).setOnAction(e -> {
            app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
            if(!addTAButton.isDisabled()){
                controller.processAddTA(copyTAs,this);
            }
        });

        (addTAButton).setOnAction(e -> {
            app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
            controller.processAddTA(copyTAs, this);
        });
     
        
        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        // DON'T LET ANYONE SORT THE TABLES
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn)officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        officeHoursTableView.refresh();
        
    }
    
    
//This initiazes the fool proof for the workplace pane.
    private void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule(); //has a hashmap of all the settings
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new OfficeHoursFoolproofDesign((OfficeHoursApp) app,copyTAs));
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
    
    //UPDATE THE TA TABLE ACCORDING TO RADIO BUTTON
    public void updateTaTableForRadio(ObservableList<TeachingAssistantPrototype> allTAs){
        allTAs.clear();
        if(taTypes.getSelectedToggle()== app.getGUIModule().getGUINode(OH_TYPE_UNDERGRADUATE)){
            for(TeachingAssistantPrototype a: copyTAs){
                allTAs.add(a);
            }
            for(TeachingAssistantPrototype a: copyTAs){
                if(a.getType().equals("Graduate")){
                    allTAs.remove(a);
                }
            }
        }
        else if(taTypes.getSelectedToggle()== app.getGUIModule().getGUINode(OH_TYPE_GRADUATE)){
            for(TeachingAssistantPrototype a: copyTAs){
                allTAs.add(a);
            }
            for(TeachingAssistantPrototype a: copyTAs ){
                if(a.getType().equals("Undergraduate")){
                    allTAs.remove(a);
                }
            }
        }
        else{
            for(TeachingAssistantPrototype a: copyTAs){
                allTAs.add(a);
            }
        }
    }
    
    //INITIALING THE 24 TIMESLOTS FOR COPYOH SO THERE'S NO NULL POINTER
    public void initCopyOH(int startHour, int endHour){

        for (int i = startHour; i <= endHour; i++) {
            TimeSlot timeSlot = new TimeSlot(   this.getTimeString(i, true),
                                                this.getTimeString(i, false));
            copyOH.add(timeSlot);
            
            TimeSlot halfTimeSlot = new TimeSlot(   this.getTimeString(i, false),
                                                    this.getTimeString(i+1, true));
            copyOH.add(halfTimeSlot);
        }

    }
    
    private String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    public TimeSlot getTimeSlotInCopyOH(TimeSlot originalTime){
        TimeSlot result= null;
        for(TimeSlot ts: copyOH){
            if(ts.getStartTime().equals(originalTime.getStartTime())){
                result= ts;
                break;
            }
        }
        return result;
    }
    
    //CLEAR THE OH TABLE, AND REEST IT TO "ALL" MODE
    public void resetOHToMatchTA(OfficeHoursData data, ObservableList<TimeSlot> originalOH){
        data.resetOfficeHours();
        ObservableList<TeachingAssistantPrototype> allTheTA= data.getTeachingAssistants();
        for(TeachingAssistantPrototype t: allTheTA){
            t.setSlots(0);
        }
        for(int i= 0; i<copyOH.size(); i++){   
            TimeSlot copyTime= copyOH.get(i);
            TimeSlot originalTime= originalOH.get(i);
            HashMap<DayOfWeek, ArrayList<TeachingAssistantPrototype>> copyTas= copyTime.getTas();

            for(DayOfWeek d: DayOfWeek.values()){
                ArrayList<TeachingAssistantPrototype> copyList= copyTas.get(d);
                for(TeachingAssistantPrototype ta: copyList){
                    data.addOH(originalTime, ta, d);
                }
            }
        }
    }
    
    //REMOVE THE ITEMS ON THE OH TABLE ACCORDINGLY
    public void removeOHToMatchTA(OfficeHoursData data, ObservableList<TeachingAssistantPrototype>  allTAs
                                  ,ObservableList<TimeSlot> originalOH){
        
        for(int i= 0; i<copyOH.size(); i++){
            TimeSlot originalTime= originalOH.get(i);
            TimeSlot copyTime= copyOH.get(i);

            HashMap<DayOfWeek, ArrayList<TeachingAssistantPrototype>> copyTas= copyTime.getTas();
            for(DayOfWeek d: DayOfWeek.values()){
                ArrayList<TeachingAssistantPrototype> copyList= copyTas.get(d);
                for(TeachingAssistantPrototype ta: copyList){
                    if(!allTAs.contains(ta)){
                        data.removeOH(originalTime, ta, d);
                    }
                }
            }
        }
    }
    
    /**
     * @return the copyOH
     */
    public ArrayList <TimeSlot> getCopyOH() {
        return copyOH;
    }

    /**
     * @return the copyTAs
     */
    public ArrayList <TeachingAssistantPrototype> getCopyTAs() {
        return copyTAs;
    }
}
