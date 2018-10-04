/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.workspace.dialogs;


import djf.AppTemplate;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static oh.OfficeHoursPropertyType.*;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.transactions.EditTA_Transaction;
import oh.workspace.OfficeHoursWorkspace;
import oh.workspace.foolproof.EditingTAFoolproofDesign;
import properties_manager.PropertiesManager;

/**
 *
 * @author bingling.dong
 */
public class OfficeHoursDialogs {
    
    
    //THIS METHOD IS GOING TO CREATE A DIALOG WITH THE SELECTED TA'S INFORMATION. AND ASK FOR THE USER TO TAKE ACTION.
    public static void editTADialog(Stage parent, Object titleProperty, Object headerProperty, 
                                    TableView<TeachingAssistantPrototype> taTable, OfficeHoursWorkspace ohws,AppTemplate app){
        
        TeachingAssistantPrototype selectedTA= taTable.getSelectionModel().getSelectedItem();
        PropertiesManager props= PropertiesManager.getPropertiesManager();
        String title= props.getProperty(titleProperty);
        String header= props.getProperty(headerProperty);
        Dialog dialog= new Dialog<>();
        dialog.setTitle(title);
        dialog.initOwner(parent);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setHeaderText(header);
        dialog.getDialogPane().setMinSize(480, 300);
        
        ButtonType okButton= new ButtonType(props.getProperty(EDIT_OK), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton= new ButtonType(props.getProperty(EDIT_CANCEL),ButtonBar.ButtonData.CANCEL_CLOSE);
        VBox vbox= new VBox();
        Label nameLabel= new Label(props.getProperty(EDIT_NAME));
        Label emailLabel= new Label(props.getProperty(EDIT_EMAIL));
        TextField nameTf= new TextField();
        TextField emailTf= new TextField();
        nameTf.setText(selectedTA.getName());
        emailTf.setText(selectedTA.getEmail());
       
        RadioButton gra= new RadioButton(props.getProperty(EDIT_TYPE_GRA));
        RadioButton under= new RadioButton(props.getProperty(EDIT_TYPE_UNDER));
        ToggleGroup tg= new ToggleGroup();
        gra.setToggleGroup(tg);
        under.setToggleGroup(tg);
        Label typeLabel= new Label(props.getProperty(EDIT_TYPE));
        
        if(selectedTA.getType().equals("Graduate"))gra.setSelected(true);
        else under.setSelected(true);
        
        HBox type= new HBox();
        HBox name= new HBox();
        HBox email= new HBox();
        
        name.getChildren().addAll(nameLabel,nameTf);
        email.getChildren().addAll(emailLabel, emailTf);
        type.getChildren().addAll(typeLabel,gra,under);
              
        name.setPadding(new Insets(20,10,10,10));
        email.setPadding(new Insets(10,10,10,10));
        type.setPadding(new Insets(10,10,10,10));
        name.setSpacing(20);
        email.setSpacing(20);
        type.setSpacing(20);
        
        vbox.getChildren().addAll(name,email,type);
        vbox.setPadding(new Insets(10,10,10,10));
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().setStyle("-fx-font-weight: bold;"
                                      + "-fx-font-size: 17pt;"
                                      + "-fx-background-color:SWATCH_NEUTRAL;");
        dialog.getDialogPane().getButtonTypes().addAll(cancelButton, okButton);
        dialog.getDialogPane().lookupButton(okButton).setDisable(true);
        
        app.getFoolproofModule().registerModeSettings(EDIT_TA_FOOLPROOF_SETTINGS, 
        new EditingTAFoolproofDesign(ohws,nameTf, emailTf, dialog, selectedTA, okButton, tg, under, gra));

        nameTf.textProperty().addListener(e->{ 
            app.getFoolproofModule().updateControls(EDIT_TA_FOOLPROOF_SETTINGS);
        });
        emailTf.textProperty().addListener(e->{
            app.getFoolproofModule().updateControls(EDIT_TA_FOOLPROOF_SETTINGS);
        });
        tg.selectedToggleProperty().addListener(e->{
            app.getFoolproofModule().updateControls(EDIT_TA_FOOLPROOF_SETTINGS);
        });
       
        OfficeHoursData data=(OfficeHoursData)app.getDataComponent();
        ////////////////////////////////INITALIZED THE DIALOG (TOOK FOREVER =-=)//////////////////////////////////
        Optional<ButtonType> result= dialog.showAndWait();
        if(result.isPresent()){
            if(result.get().equals(okButton)){
                String newName = nameTf.getText();
                String newEmail = emailTf.getText();
                String newType;
                RadioButton rb= (RadioButton)tg.getSelectedToggle();
                if(rb==gra){
                    newType= "Graduate";
                }
                else newType= "Undergraduate";
                
                EditTA_Transaction editTATransaction = new EditTA_Transaction(newName, newEmail, newType,selectedTA, taTable, ohws,data);
                app.processTransaction(editTATransaction);
                
            }
        }
    }
}
