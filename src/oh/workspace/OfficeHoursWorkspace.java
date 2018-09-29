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
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleGroup;
import oh.transactions.AddOH_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursWorkspace extends AppWorkspaceComponent {
    private ArrayList <TeachingAssistantPrototype> copyTAs= new ArrayList<>();
    private ArrayList <TimeSlot> copyOH= new ArrayList<>();

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
        ToggleGroup taTypes= new ToggleGroup();  
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

        
        ////////-------------------------------INIT THE HEADER ON THE RIGHT------------------------------------////////
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
               
        OHTableView.setOnMouseClicked(e->{
            TablePosition tp=(TablePosition) officeHoursTable.getSelectionModel().getSelectedCells().get(0);
            int cellCol= tp.getColumn();
            int cellRow= tp.getRow();
            OfficeHoursData data=(OfficeHoursData)app.getDataComponent();
            boolean validCol= data.isDayOfWeekColumn(cellCol);
            TimeSlot timeSlot;

            //If the ta is selected
            if(validCol&& data.isTASelected()){
                TeachingAssistantPrototype selectedTA=taTable.getSelectionModel().getSelectedItem();
                DayOfWeek day= data.getColumnDayOfWeek(cellCol);
                timeSlot = OHTableView.getSelectionModel().getSelectedItem();
                //Create a transaction for adding timeslots, and process the transaction
                AddOH_Transaction addOH_transaction = new AddOH_Transaction(data,timeSlot,selectedTA ,day);
                app.processTransaction(addOH_transaction);
                
            }
            else{
                //no TA is selected
                AppDialogsFacade.showMessageDialog(app.getGUIModule().getWindow(),INVALID_COMMAND_TITLE, DIDNT_CHOOSE_TA_INVALID_CLICK_CONTENT);
            }
            ((TableView)(app.getGUIModule().getGUINode(OH_OFFICE_HOURS_TABLE_VIEW))).refresh();

            
            copyOH.clear();
            
            for(TimeSlot original: OHTableView.getItems()){
                TimeSlot copyTimeSlot= new TimeSlot(original.getStartTime(),original.getEndTime());
                
                HashMap<DayOfWeek, ArrayList<TeachingAssistantPrototype>> tas_all = original.getTas();
                for(DayOfWeek d: DayOfWeek.values()){
                    ArrayList<TeachingAssistantPrototype> tasOfThatDay= tas_all.get(d);
                    ArrayList<TeachingAssistantPrototype> tasOfThatDay_copy= (ArrayList<TeachingAssistantPrototype>)tasOfThatDay.clone();
                    copyTimeSlot.getTas().remove(d);
                    copyTimeSlot.getTas().put(d, tasOfThatDay_copy);
                }
                
              copyOH.add(copyTimeSlot);  
            }
        }); 
        
        /////////////////AFTER SETTING UP THE TABLE, WE NEED TO LISTEN FOR THE CHANGE IN THE RADIO BUTTON TO CHANGE////////////
        ////////////////////////////////////////////THE TABLEVIEWS ACCORDINGLY//////////////////////////////////////////
        
        ObservableList<TeachingAssistantPrototype>  allTAs= taTable.getItems();
        ObservableList<TimeSlot> originalOH= OHTableView.getItems();
        
        //add a listener for toggle group
        
        
        taTypes.selectedToggleProperty().addListener(e->{
            app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
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
            
            //ObservableList<TimeSlot> originalOH= OHTableView.getItems();
       
            //After they updated the taTable on the left
            //first reset the originalOH to all
           
            
//            for(int i= 0; i<copyOH.size(); i++){   
//                TimeSlot originalTime= originalOH.get(i);
//                TimeSlot copyTime= copyOH.get(i);
//                HashMap<DayOfWeek, ArrayList<TeachingAssistantPrototype>> copyTas= copyTime.getTas();
//                HashMap<DayOfWeek, ArrayList<TeachingAssistantPrototype>> originalTas= originalTime.getTas();
//                
//                for(DayOfWeek d: DayOfWeek.values()){
//                    ArrayList<TeachingAssistantPrototype> copyList= copyTas.get(d);
//                    ArrayList<TeachingAssistantPrototype> originalList=(ArrayList<TeachingAssistantPrototype>)copyList.clone();
//                    originalTas.remove(d);
//                    originalTas.put(d, originalList);
//                }
//            }
            
            //remove the ones that are not in the ta list. 
//            for(int i= 0; i<copyOH.size(); i++){
//                TimeSlot originalTime= originalOH.get(i);
//                TimeSlot copyTime= copyOH.get(i);
//                HashMap<DayOfWeek, ArrayList<TeachingAssistantPrototype>> copyTas= copyTime.getTas();
//                HashMap<DayOfWeek, ArrayList<TeachingAssistantPrototype>> originalTas= originalTime.getTas();
//                
//                for(DayOfWeek d: DayOfWeek.values()){
//                    
//                    ArrayList<TeachingAssistantPrototype> copyList= copyTas.get(d);
//                    ArrayList<TeachingAssistantPrototype> originalList= originalTas.get(d);
//                     
//                    for(TeachingAssistantPrototype ta: copyList){
//                        if(!allTAs.contains(ta)){
//                            originalList.remove(ta);
//                        }
//                    }
//                }
//            }
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
        
  
        TableView taTable= (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        ObservableList<TeachingAssistantPrototype> taList=  taTable.getItems();
        
        //update the button after every action. 
        //update the copylist also
        (nameTextField).setOnAction(e -> {
            app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
            if(!addTAButton.isDisabled()){
                controller.processAddTA(copyTAs);
            }
        });
        
        (emailTextField).setOnAction(e -> {
            app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
            if(!addTAButton.isDisabled()){
                controller.processAddTA(copyTAs);
            }
        });

        (addTAButton).setOnAction(e -> {
            app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
            controller.processAddTA(copyTAs);
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
                new OfficeHoursFoolproofDesign((OfficeHoursApp) app));
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
}
