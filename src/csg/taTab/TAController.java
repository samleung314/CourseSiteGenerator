/**
 * @author Samson Leung 110490519
 */
package csg.taTab;

import csg.CSGWorkspace;
import csg.CourseSiteGenerator;
import csg.CsgProp;
import djf.ui.AppMessageDialogSingleton;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import properties_manager.PropertiesManager;
import static csg.CsgProp.*;
import csg.jtps.AddTA;
import csg.jtps.DeleteTA;
import csg.jtps.HoursChange;
import csg.jtps.ToggleTA;
import csg.jtps.UpdateTA;
import static csg.style.CSGStyle.CLASS_HIGHLIGHTED_GRID_CELL;
import static csg.style.CSGStyle.CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN;
import static csg.style.CSGStyle.CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE;
import djf.ui.AppGUI;
import djf.ui.AppYesNoCancelDialogSingleton;
import java.util.ArrayList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import jtps.jTPS_Transaction;

public class TAController {

    CourseSiteGenerator csg;
    
    public TAController(CourseSiteGenerator csg) {
        this.csg = csg;
    }
    
    /**
     * This method responds to when the user requests to add
     * a new TA via the UI. Note that it must first do some
     * validation to make sure a unique name and email address
     * has been provided.
     */
    public void handleAddTA() {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        TAWorkspace workspace = csg.getWorkspaceComponent().getTAWorkspace();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        CheckBox undergradCheck = workspace.getUndergradCheck();
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        boolean undergrad = undergradCheck.isSelected();
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = csg.getDataComponent().getTaData();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (email.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));                        
        }
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.containsTA(name, email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
        }
        else if (!Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches()){
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_EMAIL_INVALID_TITLE), props.getProperty(TA_EMAIL_INVALID_MESSAGE));
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            
            // ADD THE NEW TA TO THE DATA
            jTPS_Transaction addTA = new AddTA(csg, undergrad, name, email);
            CSGWorkspace.getjTPS().addTransaction(addTA);
            
            // CLEAR THE TEXT FIELDS
            nameTextField.setText("");
            emailTextField.setText("");
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            nameTextField.requestFocus();
            
            // WE'VE CHANGED STUFF
            markWorkAsEdited();
        }
    }
    
    public void changeExistTA(){
        TAWorkspace workspace = csg.getWorkspaceComponent().getTAWorkspace();
        TableView taTable = workspace.getTATable();
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        TAData data = csg.getDataComponent().getTaData();
        TeachingAssistant oldTA = (TeachingAssistant)selectedItem;
        String newName = workspace.getNameTextField().getText();
        String newEmail = workspace.getEmailTextField().getText();
        boolean undergrad = workspace.getUndergradCheck().isSelected();

        jTPS_Transaction UpdateTA = new UpdateTA(csg, oldTA, undergrad, newName, newEmail);
        CSGWorkspace.getjTPS().addTransaction(UpdateTA);
        
        taTable.getSelectionModel().select(data.getTA(newName));
        
        markWorkAsEdited();
    }
    
    public void loadTAtotext(){
        TAWorkspace workspace = csg.getWorkspaceComponent().getTAWorkspace();
        TableView taTable = workspace.getTATable();
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            TeachingAssistant ta = (TeachingAssistant)selectedItem;
            String name = ta.getName();
            String email = ta.getEmail();
            boolean undergrad = ta.isUndergrad();
            workspace.getNameTextField().setText(name);
            workspace.getEmailTextField().setText(email);
            workspace.getUndergradCheck().setSelected(undergrad);
        }
    }
    
    public void handleKeyPress(KeyCode code) {
        // DID THE USER PRESS THE DELETE KEY?
        if (code == KeyCode.DELETE) {
            // GET THE TABLE
            TAWorkspace workspace = csg.getWorkspaceComponent().getTAWorkspace();
            TableView taTable = workspace.getTATable();
            
            // IS A TA SELECTED IN THE TABLE?
            Object selectedItem = taTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // GET THE TA AND REMOVE IT
                TeachingAssistant ta = (TeachingAssistant)selectedItem;
                String taName = ta.getName();
                
                jTPS_Transaction deleteTA = new DeleteTA(csg, taName);
                CSGWorkspace.getjTPS().addTransaction(deleteTA);
                
                // AND BE SURE TO REMOVE ALL THE TA'S OFFICE HOURS
                // WE'VE CHANGED STUFF
                workspace.clear();
                markWorkAsEdited();
            }
        }
    }

    /**
     * This function provides a response for when the user clicks
     * on the office hours grid to add or remove a TA to a time slot.
     * 
     * @param pane The pane that was toggled.
     */
    public void handleCellToggle(Pane pane) {
        // GET THE TABLE
        TAWorkspace workspace = csg.getWorkspaceComponent().getTAWorkspace();
        TableView taTable = workspace.getTATable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // GET THE TA
            TeachingAssistant ta = (TeachingAssistant)selectedItem;
            String taName = ta.getName();
            TAData data = csg.getDataComponent().getTaData();
            String cellKey = pane.getId();
            
            // AND TOGGLE THE OFFICE HOURS IN THE CLICKED CELL
            jTPS_Transaction toggleTA = new ToggleTA(taName, cellKey, data);
            CSGWorkspace.getjTPS().addTransaction(toggleTA);
            
            // WE'VE CHANGED STUFF
            markWorkAsEdited();
        }
    }
    
    void handleGridCellMouseExited(Pane pane) {
        String cellKey = pane.getId();
        TAData data = csg.getDataComponent().getTaData();
        int column = Integer.parseInt(cellKey.substring(0, cellKey.indexOf("_")));
        int row = Integer.parseInt(cellKey.substring(cellKey.indexOf("_") + 1));
        TAWorkspace workspace = csg.getWorkspaceComponent().getTAWorkspace();

        Pane mousedOverPane = workspace.getTACellPane(data.getCellKey(column, row));
        mousedOverPane.getStyleClass().clear();
        mousedOverPane.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);

        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = workspace.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(column, 0));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // THE MOUSED OVER ROW HEADERS
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        
        // AND NOW UPDATE ALL THE CELLS IN THE SAME ROW TO THE LEFT
        for (int i = 2; i < column; i++) {
            cellKey = data.getCellKey(i, row);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
            cell.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        }

        // AND THE CELLS IN THE SAME COLUMN ABOVE
        for (int i = 1; i < row; i++) {
            cellKey = data.getCellKey(column, i);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
            cell.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        }
    }
    
    void handleGridCellMouseEntered(Pane pane) {
        String cellKey = pane.getId();
        TAData data = csg.getDataComponent().getTaData();
        int column = Integer.parseInt(cellKey.substring(0, cellKey.indexOf("_")));
        int row = Integer.parseInt(cellKey.substring(cellKey.indexOf("_") + 1));
        TAWorkspace workspace = csg.getWorkspaceComponent().getTAWorkspace();
        // THE MOUSED OVER PANE
        Pane mousedOverPane = workspace.getTACellPane(data.getCellKey(column, row));
        mousedOverPane.getStyleClass().clear();
        mousedOverPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_CELL);
        
        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = workspace.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(column, 0));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        
        // THE MOUSED OVER ROW HEADERS
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        
        // AND NOW UPDATE ALL THE CELLS IN THE SAME ROW TO THE LEFT
        for (int i = 2; i < column; i++) {
            cellKey = data.getCellKey(i, row);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        }

        // AND THE CELLS IN THE SAME COLUMN ABOVE
        for (int i = 1; i < row; i++) {
            cellKey = data.getCellKey(column, i);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        }
    }
    
    public void changeTime(){
        TAData data = csg.getDataComponent().getTaData();
        TAWorkspace workspace = csg.getWorkspaceComponent().getTAWorkspace();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ComboBox comboBox1 = workspace.getOfficeHour(true);
        ComboBox comboBox2 = workspace.getOfficeHour(false);
        int startTime = data.getStartHour();
        int endTime = data.getEndHour();
        int newStartTime = comboBox1.getSelectionModel().getSelectedIndex();
        int newEndTime = comboBox2.getSelectionModel().getSelectedIndex();
        
        // Check that we're not ending before starting and vice-versa
        if(newStartTime > endTime || newEndTime < startTime){
            comboBox1.getSelectionModel().select(startTime);
            comboBox2.getSelectionModel().select(endTime);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(CsgProp.START_OVER_END_TITLE.toString()), props.getProperty(CsgProp.START_OVER_END_MESSAGE.toString()));
            return;
        }
        ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(data);
        if(officeHours.isEmpty()){
            workspace.getOfficeHoursGridPane().getChildren().clear();
            //data.initHours("" + newStartTime, "" + newEndTime);
        }
        
        if(!officeHours.isEmpty()){
            // Get true number of first office hour
            String firsttime = officeHours.get(0).getTime();
            int firsthour = Integer.parseInt(firsttime.substring(0, firsttime.indexOf('_')));
            if(firsttime.contains("pm"))
                firsthour += 12;
            if(firsttime.contains("12"))
                firsthour -= 12;

            // Get true number of last office hour
            String lasttime = officeHours.get(officeHours.size() - 1).getTime();
            int lasthour = Integer.parseInt(lasttime.substring(0, lasttime.indexOf('_')));
            if(lasttime.contains("pm"))
                lasthour += 12;
            if(lasttime.contains("12"))
                lasthour -= 12;
            
            if(firsthour < newStartTime || lasthour + 1 > newEndTime){
                AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
                yesNoDialog.show(props.getProperty(CsgProp.OFFICE_HOURS_REMOVED_TITLE.toString()), props.getProperty(CsgProp.OFFICE_HOURS_REMOVED_MESSAGE).toString());
                String selection = yesNoDialog.getSelection();
                if (!selection.equals(AppYesNoCancelDialogSingleton.YES)){
                    comboBox1.getSelectionModel().select(startTime);
                    comboBox2.getSelectionModel().select(endTime);
                    return;
                }
            }
        }

        jTPS_Transaction hoursChange = new HoursChange(csg);
        CSGWorkspace.getjTPS().addTransaction(hoursChange);

        markWorkAsEdited();
    }
    
    public void markWorkAsEdited() {
        // MARK WORK AS EDITED
        AppGUI gui = csg.getGUI();
        gui.getFileController().markAsEdited(gui);
    }
}
