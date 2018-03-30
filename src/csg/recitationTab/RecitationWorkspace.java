/**
 * @author Samson Leung 110490519
 */
package csg.recitationTab;

import csg.CsgProp;
import csg.CSGWorkspace;
import csg.jtps.AddRecitation;
import csg.jtps.DeleteRecitation;
import csg.jtps.EditRecitation;
import csg.taTab.TAData;
import csg.taTab.TeachingAssistant;
import djf.ui.AppGUI;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;

public class RecitationWorkspace {
    
    VBox recitationBox;
    HBox recitationHeader;
    GridPane addEditGrid;
    
    TextField sectionField, instructorField, dayTimeField, locationField;
    ComboBox firstTACombo, secondTACombo;
    
    // For recitaiton table
    TableView<Recitation> recitationTable;
    TableColumn<Recitation, String> sectionColumn;
    TableColumn<Recitation, String> instructorColumn;
    TableColumn<Recitation, String> dayTimeColumn;
    TableColumn<Recitation, String> locationColumn;
    TableColumn<Recitation, String> firstTAColumn;
    TableColumn<Recitation, String> secondTAColumn;
    
    Label recitaitonLabel, addEditLabel, sectionLabel, instructorLabel, dayTimeLabel,
            locationLabel, firstTALabel, secondTALabel;
    
    Button removeButton, addUpdateButton, clearButton;
    CSGWorkspace space;
    RecitationData data;
    TAData taData;
    
    public RecitationWorkspace(CSGWorkspace space){
        this.space = space;
        this.taData = space.getCourseSiteGenerator().getDataComponent().getTaData();
        this.data = space.getCourseSiteGenerator().getDataComponent().getRecitationData();
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        recitaitonLabel = new Label(props.getProperty(CsgProp.RECITATIONS_LABEL.toString()));
        removeButton = new Button("-");
        removeButton.setOnAction(e -> {
            deleteRecitation();
        });
        recitationHeader = new HBox(recitaitonLabel, removeButton);
        
        recitationTable = new TableView();
        recitationTable.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.DELETE){
                deleteRecitation();
            }
        });
        recitationTable.setOnMouseClicked(e ->{
            loadRecitationToText();
        });
        recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recitationTable.setItems(data.getRecitations());
        recitationTable.setEditable(true);
        
        sectionColumn = new TableColumn(props.getProperty(CsgProp.SECTION_COL.toString()));
        instructorColumn = new TableColumn(props.getProperty(CsgProp.INSTRUCTOR_COL.toString()));
        dayTimeColumn = new TableColumn(props.getProperty(CsgProp.DAYTIME_COL.toString()));
        locationColumn = new TableColumn(props.getProperty(CsgProp.LOCATION_COL.toString()));
        firstTAColumn = new TableColumn(props.getProperty(CsgProp.TA_COL.toString()));
        secondTAColumn = new TableColumn(props.getProperty(CsgProp.TA_COL.toString()));
        
        sectionColumn.setCellValueFactory(new PropertyValueFactory<>("section"));
        instructorColumn.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        dayTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        firstTAColumn.setCellValueFactory(new PropertyValueFactory<>("firstTA"));
        secondTAColumn.setCellValueFactory(new PropertyValueFactory<>("secondTA"));
        
        recitationTable.getColumns().addAll(sectionColumn, instructorColumn, 
                dayTimeColumn, locationColumn, firstTAColumn, secondTAColumn);
        
        addEditGrid = new GridPane();
        addEditGrid.setStyle("-fx-hgap: 10; -fx-vgap: 10; -fx-padding: 15;-fx-border-color: #7777dd;");
        
        addEditLabel = new Label(props.getProperty(CsgProp.ADD_LABEL.toString()));
        addEditGrid.add(addEditLabel, 0, 0);
        
        sectionLabel = new Label(props.getProperty(CsgProp.SECTION_LABEL.toString()));
        addEditGrid.add(sectionLabel, 0, 1);
        
        sectionField = new TextField();
        addEditGrid.add(sectionField, 1, 1);
        
        instructorLabel = new Label(props.getProperty(CsgProp.INSTRUCTOR_LABEL.toString())); 
        addEditGrid.add(instructorLabel, 0, 2);
        
        instructorField = new TextField();
        addEditGrid.add(instructorField, 1, 2);
        
        dayTimeLabel = new Label(props.getProperty(CsgProp.DAYTIME_LABEL.toString()));
        addEditGrid.add(dayTimeLabel, 0, 3);
        
        dayTimeField = new TextField();
        addEditGrid.add(dayTimeField, 1, 3);
        
        locationLabel = new Label(props.getProperty(CsgProp.LOCATION_LABEL.toString()));
        addEditGrid.add(locationLabel, 0, 4);
        
        locationField = new TextField();
        addEditGrid.add(locationField, 1, 4);
        
        firstTALabel = new Label(props.getProperty(CsgProp.TA_LABEL.toString()));
        addEditGrid.add(firstTALabel, 0, 5);
        
        firstTACombo = new ComboBox();
        addEditGrid.add(firstTACombo, 1, 5);
        
        secondTALabel = new Label(props.getProperty(CsgProp.TA_LABEL.toString()));
        addEditGrid.add(secondTALabel, 0, 6);
        
        secondTACombo = new ComboBox(taData.getUndergradTANames());
        addEditGrid.add(secondTACombo, 1, 6);
        
        addUpdateButton = new Button(props.getProperty(CsgProp.ADD_UPDATE_BUTTON.toString()));
        addEditGrid.add(addUpdateButton, 0, 7);
        addUpdateButton.setOnAction(e -> {
            
            TAData taData = space.getCourseSiteGenerator().getDataComponent().getTaData();
            
            if(recitationTable.getSelectionModel().getSelectedItem() != null){
                editRecitation();
            } else {
                String section = sectionField.getText();
                String instructor = instructorField.getText();
                String time = dayTimeField.getText();
                String location = locationField.getText();
                TeachingAssistant firstTA = taData.getTA((String)firstTACombo.getValue());
                TeachingAssistant secondTA = taData.getTA((String)secondTACombo.getValue());
                
                boolean empty = section.isEmpty() || time.isEmpty() || location.isEmpty();
                
                if (!empty) {
                    Recitation recitation = new Recitation(section, instructor, time, location, firstTA, secondTA);
                    jTPS_Transaction addRecitation = new AddRecitation(data, recitation);
                    CSGWorkspace.getjTPS().addTransaction(addRecitation);
                    markWorkAsEdited();
                }
            }
        });
        
        clearButton = new Button(props.getProperty(CsgProp.CLEAR_BUTTON.toString()));
        clearButton.setOnAction(e ->{
            resetWorkspace();
        });
        addEditGrid.add(clearButton, 1, 7);
        
        recitationBox = new VBox();
        recitationBox.setStyle("-fx-spacing: 10; -fx-padding: 15;");
        recitationBox.getChildren().addAll(recitationHeader, recitationTable, addEditGrid);
        
        space.getRecitationTab().setContent(recitationBox);
    }
    
    public void deleteRecitation() {
        Object selectedItem = recitationTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // GET THE TA AND REMOVE IT
            Recitation rec = (Recitation) selectedItem;

            jTPS_Transaction deleteRecitation = new DeleteRecitation(data, rec);
            CSGWorkspace.getjTPS().addTransaction(deleteRecitation);

            markWorkAsEdited();
        }
    }
    
    public void editRecitation(){
        Object selectedItem = (Recitation) recitationTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            Recitation oldRec = (Recitation) selectedItem;
            String newSection = sectionField.getText();
            String newInstruct = instructorField.getText();
            String newTime = dayTimeField.getText();
            String newLocation = locationField.getText();
            String newFirstTA = (String) firstTACombo.getValue();
            String newSecondTA = (String) secondTACombo.getValue();
            
            TAData taData = space.getCourseSiteGenerator().getDataComponent().getTaData();
            TeachingAssistant a = taData.getTA(newFirstTA);
            TeachingAssistant b = taData.getTA(newSecondTA);

            jTPS_Transaction edit = new EditRecitation(this, data, oldRec, newSection, 
                    newInstruct, newTime, newLocation, a, b);
            CSGWorkspace.getjTPS().addTransaction(edit);

            markWorkAsEdited();
        }
    }
    
    private void loadRecitationToText(){
        Object selectedItem = recitationTable.getSelectionModel().getSelectedItem();
        Recitation rec = (Recitation) selectedItem;
        sectionField.setText(rec.getSection());
        instructorField.setText(rec.getInstructor());
        dayTimeField.setText(rec.getTime());
        locationField.setText(rec.getLocation());
        firstTACombo.getSelectionModel().select(rec.getFirstTA());
        secondTACombo.getSelectionModel().select(rec.getSecondTA());
    }
    
    public void updateTable(){
        for(TableColumn c : recitationTable.getColumns()){
            c.setVisible(false);
            c.setVisible(true);
        }
    }
    
    private void markWorkAsEdited() {
        // MARK WORK AS EDITED
        AppGUI gui = space.getCourseSiteGenerator().getGUI();
        gui.getFileController().markAsEdited(gui);
    }
    
    public void resetCombos(){
        firstTACombo.setItems(taData.getUndergradTANames());
        secondTACombo.setItems(taData.getUndergradTANames());
    }
    
    public void resetWorkspace(){
        sectionField.clear();
        instructorField.clear();
        dayTimeField.clear();
        locationField.clear();
        firstTACombo.getSelectionModel().clearSelection();
        secondTACombo.getSelectionModel().clearSelection();
        recitationTable.getSelectionModel().clearSelection();
    }

    public Label getRecitaitonLabel() {
        return recitaitonLabel;
    }

    public Label getAddEditLabel() {
        return addEditLabel;
    }
    
    public void refreshTable(){
        
        recitationTable.refresh();
        
        for(TableColumn c : recitationTable.getColumns()){
            c.setVisible(false);
            c.setVisible(true);
        }
    }
}
