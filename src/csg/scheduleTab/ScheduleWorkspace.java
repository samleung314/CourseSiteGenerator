/**
 * @author Samson Leung 110490519
 */
package csg.scheduleTab;

import csg.CsgProp;
import csg.CSGWorkspace;
import csg.jtps.AddScheduleItem;
import csg.jtps.ChangeFriday;
import csg.jtps.ChangeMonday;
import csg.jtps.DeleteScheduleItem;
import csg.jtps.EditScheduleItem;
import djf.ui.AppGUI;
import djf.ui.AppMessageDialogSingleton;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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

public class ScheduleWorkspace {

    Label scheduleLabel,
            calendarLabel, mondayLabel, fridayLabel,
            itemsLabel, 
            addEditLabel, typeLabel, dateLabel, timeLabel, titleLabel, topicLabel,
            linkLabel, criteriaLabel;
    
    DatePicker mondayDate, fridayDate, addDate;
    
    TableView<ScheduleItem> scheduleTable;
    TableColumn<ScheduleItem, String> typeCol, dateCol, titleCol, topicCol;
    
    TextField titleField, timeField, topicField, linkField, criteriaField;
    
    Button removeButton, addUpdateButton, clearButton;

    HBox schedueItemsHeader;
    
    VBox scheduleSections, itemsBox;
    GridPane boundariesGrid, scheduleGrid;
    
    ComboBox typeCombo;
    
    LocalDate oldMonday, monday, oldFriday, friday, date;
    
    ObservableList<String> typeOptions;
    
    ScheduleData data;
    
    CSGWorkspace space;
    
    boolean userAction;
    
    public ScheduleWorkspace(CSGWorkspace space){
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        data = space.getCourseSiteGenerator().getDataComponent().getScheduleData();
        this.space = space;
        
        // Create all UI elements
        scheduleLabel = new Label(props.getProperty(CsgProp.SCHEDULE_LABEL.toString()));
        calendarLabel = new Label(props.getProperty(CsgProp.CALENDAR_LABEL.toString()));
        mondayLabel = new Label(props.getProperty(CsgProp.MONDAY_LABEL.toString()));
        fridayLabel = new Label(props.getProperty(CsgProp.FRIDAY_LABEL.toString()));
        itemsLabel = new Label(props.getProperty(CsgProp.ITEMS_LABEL.toString()));
        addEditLabel = new Label(props.getProperty(CsgProp.ADD_EDIT_LABEL.toString()));
        typeLabel = new Label(props.getProperty(CsgProp.TYPE_LABEL.toString()));
        dateLabel = new Label(props.getProperty(CsgProp.DATE_LABEL.toString()));
        timeLabel = new Label(props.getProperty(CsgProp.TIME_LABEL.toString()));
        titleLabel = new Label(props.getProperty(CsgProp.TITLE_LABEL.toString()));
        topicLabel = new Label(props.getProperty(CsgProp.TOPIC_LABEL.toString()));
        linkLabel = new Label(props.getProperty(CsgProp.LINK_LABEL.toString()));
        criteriaLabel = new Label(props.getProperty(CsgProp.CRITERIA_LABEL.toString()));
        
        mondayDate = new DatePicker();
        mondayDate.setOnMouseClicked(e->{
            oldMonday = mondayDate.getValue();
        });
        userAction = true;
        mondayDate.setOnAction(e -> {
            if (userAction) {
                if (checkMonday()) {
                    monday = mondayDate.getValue();
                    jTPS_Transaction change = new ChangeMonday(this, data, oldMonday, monday);
                    CSGWorkspace.getjTPS().addTransaction(change);
                    markWorkAsEdited();
                }
            }
        });
        
        fridayDate = new DatePicker();
        fridayDate.setOnMouseClicked(e->{
            oldFriday = fridayDate.getValue();
        });
        fridayDate.setOnAction(e->{
            if (userAction) {
                if (checkFriday()) {
                    friday = fridayDate.getValue();
                    jTPS_Transaction change = new ChangeFriday(this, data, oldFriday, friday);
                    CSGWorkspace.getjTPS().addTransaction(change);
                    markWorkAsEdited();
                }
            }
        });
        
        addDate = new DatePicker();
        addDate.setOnAction(e->{
            //try{ checkDate(); }catch(NullPointerException o){return;}
            date = addDate.getValue();
        });
        
        scheduleTable = new TableView();
        scheduleTable.setOnMouseClicked(e -> {
            Object selected = (ScheduleItem) scheduleTable.getSelectionModel().getSelectedItem();
            ScheduleItem item = (ScheduleItem) selected;
            
            typeCombo.setValue(item.getType());
            addDate.getEditor().setText(item.getDate());
            timeField.setText(item.getTime());
            titleField.setText(item.getTitle());
            topicField.setText(item.getTopic());
            linkField.setText(item.getLink());
            criteriaField.setText(item.getCriteria());
        });
        ObservableList<ScheduleItem> tableData = data.getScheduleItems();
        scheduleTable.setItems(tableData);
        
        scheduleTable.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.DELETE){
                deleteSchedule();
            }
        });
        
        typeCol = new TableColumn(props.getProperty(CsgProp.TYPE_COL.toString())); 
        dateCol = new TableColumn(props.getProperty(CsgProp.DATE_COL.toString()));
        titleCol = new TableColumn(props.getProperty(CsgProp.TITLE_COL.toString()));
        topicCol = new TableColumn(props.getProperty(CsgProp.TOPIC_COL.toString()));
        
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        topicCol.setCellValueFactory(new PropertyValueFactory<>("topic"));
        
        scheduleTable.getColumns().addAll(typeCol, dateCol, titleCol, topicCol);
        
        titleField = new TextField(); 
        timeField = new TextField(); 
        topicField = new TextField(); 
        linkField = new TextField(); 
        criteriaField = new TextField();
        
        typeOptions = FXCollections.observableArrayList("Holiday", "Lecture", "Reference", "Recitation", "HW");
        typeCombo = new ComboBox(typeOptions);
        
        removeButton = new Button("-"); 
        removeButton.setOnAction(e -> {
            deleteSchedule();
            markWorkAsEdited();
        });
        addUpdateButton = new Button(props.getProperty(CsgProp.ADD_UPDATE_BUTTON.toString())); 
        clearButton = new Button(props.getProperty(CsgProp.CLEAR_BUTTON.toString())); 
        
        // Start organizing all the UI elements onto the GUI
        boundariesGrid = new GridPane();
        boundariesGrid.setStyle("-fx-hgap: 15; -fx-vgap:15; -fx-padding: 15;");
        boundariesGrid.add(calendarLabel, 0, 0);
        boundariesGrid.add(mondayLabel, 0, 1);
        boundariesGrid.add(mondayDate, 1, 1);
        boundariesGrid.add(fridayLabel, 2, 1);
        boundariesGrid.add(fridayDate, 3, 1);
        
        schedueItemsHeader = new HBox();
        schedueItemsHeader.setStyle("-fx-spacing: 10");
        schedueItemsHeader.getChildren().addAll(itemsLabel, removeButton);
        
        scheduleGrid = new GridPane();
        scheduleGrid.setStyle("-fx-hgap: 10; -fx-vgap:10; -fx-padding: 15;");
        scheduleGrid.add(addEditLabel, 0, 0);
        scheduleGrid.add(typeLabel, 0, 1);
        scheduleGrid.add(typeCombo, 1, 1);
        scheduleGrid.add(dateLabel, 0, 2);
        scheduleGrid.add(addDate, 1, 2);
        scheduleGrid.add(timeLabel, 0, 3);
        scheduleGrid.add(timeField, 1, 3);
        scheduleGrid.add(titleLabel, 0, 4);
        scheduleGrid.add(titleField, 1, 4);
        scheduleGrid.add(topicLabel, 0, 5);
        scheduleGrid.add(topicField, 1, 5);
        scheduleGrid.add(linkLabel, 0, 6);
        scheduleGrid.add(linkField, 1, 6);
        scheduleGrid.add(criteriaLabel, 0, 7);
        scheduleGrid.add(criteriaField, 1, 7);
        
        scheduleGrid.add(addUpdateButton, 0, 8);
        addUpdateButton.setOnAction(e -> {
            if(scheduleTable.getSelectionModel().getSelectedItem() != null){
                //Update item
                editItem();
                markWorkAsEdited();
            }else{
                //Add item
                String type = (String) typeCombo.getValue();
                String date = addDate.getEditor().getText();
                String title = titleField.getText();
                boolean isEmpty = type == null && date.isEmpty() && title.isEmpty();
                
                if(!isEmpty){
                    ScheduleItem item = new ScheduleItem(type, date, title);
                    item.setCriteria(criteriaField.getText());
                    item.setLink(linkField.getText());
                    item.setTime(timeField.getText());
                    item.setTopic(topicField.getText());
                    
                    jTPS_Transaction add = new AddScheduleItem(data, item);
                    CSGWorkspace.getjTPS().addTransaction(add);
                    markWorkAsEdited();
                }else{
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show("REQUIRED FIELDS", "MUST HAVE TYPE, DATE, TITLE");
                }
            }
        });
        scheduleGrid.add(clearButton, 1, 8);
        clearButton.setOnAction(e -> {
            clearFields();
        });
        
        itemsBox = new VBox();
        itemsBox.setStyle("-fx-spacing: 10; -fx-padding: 15;");
        itemsBox.getChildren().addAll(schedueItemsHeader, scheduleTable, scheduleGrid);
        
        scheduleSections = new VBox();
        scheduleSections.getChildren().addAll(scheduleLabel, boundariesGrid, itemsBox);
        scheduleSections.setStyle("-fx-spacing: 10; -fx-padding: 15;");
        
        space.getScheduleTab().setContent(scheduleSections);
    }
    
    boolean checkMonday(){
        // 1 (Monday) to 7 (Sunday)
        if(mondayDate.getValue().getDayOfWeek().getValue() != 1){
            mondayDate.setValue(monday);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show("INVALID DATE", "DATE MUST BE A MONDAY");
            return false;
        }
        
        if(fridayDate.getValue() == null){
            return true;
        }else if(mondayDate.getValue().isAfter(fridayDate.getValue())){
            mondayDate.setValue(monday);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show("INVALID DATE", "START DATE MUST BE BEFORE END DATE");
            return false;
        }
        return true;
    }
    
    boolean checkFriday(){
        // 1 (Monday) to 7 (Sunday)
        if(fridayDate.getValue().getDayOfWeek().getValue() != 5){
            fridayDate.setValue(friday);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show("INVALID DATE", "DATE MUST BE A FRIDAY");
            return false;
        }
        
        if(mondayDate.getValue() == null){
            return true;
        }else if(mondayDate.getValue().isAfter(fridayDate.getValue())){
            fridayDate.setValue(friday);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show("INVALID DATE", "START DATE MUST BE BEFORE END DATE");
            return false;
        }
        return true;
    }
    
    void editItem(){
        Object selectedItem = (ScheduleItem) scheduleTable.getSelectionModel().getSelectedItem();
        
        if(selectedItem != null){
            ScheduleItem item = (ScheduleItem) selectedItem;
            String newType = (String) typeCombo.getValue();
            String newDate = addDate.getEditor().getText();
            String newTime = timeField.getText();
            String newTitle = titleField.getText();
            String newTopic = topicField.getText();
            String newLink = linkField.getText();
            String newCriteria = criteriaField.getText();
            
            jTPS_Transaction edit = new EditScheduleItem(this, data, item, newType, 
                    newDate, newTime, newTitle, newTopic, newLink, newCriteria);
            CSGWorkspace.getjTPS().addTransaction(edit);
        }
    }
    
    public void updateTable(){
        for(TableColumn c : scheduleTable.getColumns()){
            c.setVisible(false);
            c.setVisible(true);
        }
    }
   
    void deleteSchedule(){
        Object selectedItem = (ScheduleItem) scheduleTable.getSelectionModel().getSelectedItem();
        
        if(selectedItem != null){
            ScheduleItem item = (ScheduleItem) selectedItem;
            jTPS_Transaction delete = new DeleteScheduleItem(data, item);
            CSGWorkspace.getjTPS().addTransaction(delete);
        }
    }
    
    public Label getScheduleLabel() {
        return scheduleLabel;
    }

    public Label getAddEditLabel() {
        return addEditLabel;
    }

    public GridPane getBoundaries() {
        return boundariesGrid;
    }

    public VBox getItemsBox() {
        return itemsBox;
    }

    public DatePicker getMondayDate() {
        return mondayDate;
    }

    public DatePicker getFridayDate() {
        return fridayDate;
    }
    
    private void markWorkAsEdited() {
        // MARK WORK AS EDITED
        AppGUI gui = space.getCourseSiteGenerator().getGUI();
        gui.getFileController().markAsEdited(gui);
    }
    
    public void setDates(String start, String end){
        mondayDate.getEditor().setText(start);
        fridayDate.getEditor().setText(end);
    }

    public void setUserAction(boolean userAction) {
        this.userAction = userAction;
    }
    
    public void clearFields(){
        scheduleTable.getSelectionModel().clearSelection();
        typeCombo.getSelectionModel().clearSelection();
        addDate.getEditor().clear();
        addDate.setValue(null);
        timeField.clear();
        titleField.clear();
        topicField.clear();
        linkField.clear();
        criteriaField.clear();
    }
}
