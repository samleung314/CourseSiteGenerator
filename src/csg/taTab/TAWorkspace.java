/**
 * @author Samson Leung 110490519
 */
package csg.taTab;

import csg.CsgProp;
import csg.style.CSGStyle;
import csg.CSGWorkspace;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import properties_manager.PropertiesManager;

public class TAWorkspace extends AppWorkspaceComponent {
    
    CSGWorkspace space;
    TAController controller;
    
    // FOR THE HEADER ON THE LEFT
    HBox addBox;
    Label tasHeaderLabel;
    
    // TA TABLE
    TableView taTable;
    TableColumn<TeachingAssistant, CheckBox> undergradColumn;
    TableColumn<TeachingAssistant, String> nameColumn;
    TableColumn<TeachingAssistant, String> emailColumn;
    
    // ENTRY CONTROL
    TextField nameTextField, emailTextField;
    Button addButton, clearButton, changeButton;
    
    // THE HEADER ON THE RIGHT
    HBox officeHoursHeaderBox;
    Label officeHoursHeaderLabel;
    
    // OFFICE HOURS GRID
    GridPane officeHoursGridPane;
    HashMap<String, Pane> officeHoursGridTimeHeaderPanes;
    HashMap<String, Label> officeHoursGridTimeHeaderLabels;
    HashMap<String, Pane> officeHoursGridDayHeaderPanes;
    HashMap<String, Label> officeHoursGridDayHeaderLabels;
    HashMap<String, Pane> officeHoursGridTimeCellPanes;
    HashMap<String, Label> officeHoursGridTimeCellLabels;
    HashMap<String, Pane> officeHoursGridTACellPanes;
    HashMap<String, Label> officeHoursGridTACellLabels;
    
    ObservableList<String> time_options;
    ComboBox startTime;
    ComboBox endTime;
    
    CheckBox undergradCheck;
    
    PropertiesManager props;
    
    boolean addChange;
    
    public TAWorkspace(CSGWorkspace space){
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        props = PropertiesManager.getPropertiesManager();
        controller = new TAController(space.getCsg());
        this.space = space;
        
        // INIT THE HEADER ON THE LEFT
        tasHeaderLabel = new Label(props.getProperty(CsgProp.TAS_HEADER_TEXT.toString()));
        tasHeaderLabel.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold; -fx-padding: 10;");
        
        // TA TABLE
        taTable = new TableView();
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TAData data = space.getCsg().getDataComponent().getTaData();
        ObservableList<TeachingAssistant> tableData = data.getTeachingAssistants();
        taTable.setItems(tableData);
        taTable.setEditable(true);
        String nameColumnText = props.getProperty(CsgProp.NAME_COLUMN_TEXT.toString());
        String emailColumnText = props.getProperty(CsgProp.EMAIL_COLUMN_TEXT.toString());
        nameColumn = new TableColumn(nameColumnText);
        emailColumn = new TableColumn(emailColumnText);
        undergradColumn = new TableColumn("Undergrad");
        undergradColumn.setEditable(true);
        
        nameColumn.setCellValueFactory(new PropertyValueFactory<TeachingAssistant, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<TeachingAssistant, String>("email"));
        undergradColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TeachingAssistant, CheckBox>, ObservableValue<CheckBox>>(){
            @Override
            public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<TeachingAssistant, CheckBox> c) {
                TeachingAssistant ta = c.getValue();
                CheckBox checkBox = new CheckBox();
                checkBox.setOnAction(e -> {
                    ta.setUndergrad(checkBox.selectedProperty().get());
                    controller.markWorkAsEdited();
                    space.getCourseSiteGenerator().getWorkspaceComponent().getRecitaitonSpace().resetCombos();
                });

                checkBox.selectedProperty().setValue(ta.isUndergrad());
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> a, Boolean oldVal, Boolean newVal) {
                        ta.setUndergrad(newVal);
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        }); 
        
        taTable.getColumns().addAll(undergradColumn, nameColumn, emailColumn);
        
        // TABLE BUTTONS
        addBox = new HBox();
        nameTextField = new TextField();
        emailTextField = new TextField();
        nameTextField.setPromptText(props.getProperty(CsgProp.NAME_PROMPT_TEXT.toString()));
        emailTextField.setPromptText(props.getProperty(CsgProp.EMAIL_PROMPT_TEXT.toString()));
        addButton = new Button(props.getProperty(CsgProp.ADD_BUTTON_TEXT.toString()));
        clearButton = new Button(props.getProperty(CsgProp.CLEAR_BUTTON_TEXT.toString()));
        
        nameTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.4));
        emailTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.4));
        addButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.1));
        clearButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.1));
        addBox.getChildren().add(nameTextField);
        addBox.getChildren().add(emailTextField);
        addBox.getChildren().add(addButton);
        addBox.getChildren().add(clearButton);
        
        addChange = true;
         // CONTROLS FOR ADDING TAs
        nameTextField.setOnAction(e -> {
            if(!addChange)
                controller.changeExistTA();
            else
                controller.handleAddTA();
        });
        emailTextField.setOnAction(e -> {
            if(!addChange)
                controller.changeExistTA();
            else
                controller.handleAddTA();
        });
        addButton.setOnAction(e -> {
            if(!addChange)
                controller.changeExistTA();
            else
                controller.handleAddTA();
        });
        clearButton.setOnAction(e -> {
            clear();
        });
        
        taTable.setFocusTraversable(true);
        taTable.setOnKeyPressed(e -> {
            controller.handleKeyPress(e.getCode());
        });
        taTable.setOnMouseClicked(e -> {
            if(taTable.getSelectionModel().getSelectedItem() != null){
                addChange = false;
                addButton.setText(props.getProperty(CsgProp.UPDATE_BUTTON_TEXT.toString()));
            }
            controller.loadTAtotext();
        });
        
        undergradCheck = new CheckBox(props.getProperty(CsgProp.CHECK_TEXT.toString()));
        
        // LEFT PANE
        VBox leftPane = new VBox();
        leftPane.getChildren().add(tasHeaderLabel);
        leftPane.getChildren().add(taTable);  
        taTable.setStyle("-fx-padding: 10;");
        leftPane.getChildren().add(addBox);
        leftPane.getChildren().add(undergradCheck);
        
        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox();
        officeHoursHeaderLabel = new Label(props.getProperty(CsgProp.OFFICE_HOURS_SUBHEADER.toString()));
        officeHoursHeaderLabel.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold;-fx-padding: 10;");
        officeHoursHeaderBox.getChildren().add(officeHoursHeaderLabel);
        
        // COMBO BOX BUTTONS
        time_options = FXCollections.observableArrayList("12AM");
        startTime = new ComboBox(time_options);
        endTime = new ComboBox(time_options);
        
        time_options = FXCollections.observableArrayList(
        props.getProperty(CsgProp.TIME_12AM.toString()),
        props.getProperty(CsgProp.TIME_1AM.toString()),
        props.getProperty(CsgProp.TIME_2AM.toString()),
        props.getProperty(CsgProp.TIME_3AM.toString()),
        props.getProperty(CsgProp.TIME_4AM.toString()),
        props.getProperty(CsgProp.TIME_5AM.toString()),
        props.getProperty(CsgProp.TIME_6AM.toString()),
        props.getProperty(CsgProp.TIME_7AM.toString()),
        props.getProperty(CsgProp.TIME_8AM.toString()),
        props.getProperty(CsgProp.TIME_9AM.toString()),
        props.getProperty(CsgProp.TIME_10AM.toString()),
        props.getProperty(CsgProp.TIME_11AM.toString()),
        props.getProperty(CsgProp.TIME_12PM.toString()),
        props.getProperty(CsgProp.TIME_1PM.toString()),
        props.getProperty(CsgProp.TIME_2PM.toString()),
        props.getProperty(CsgProp.TIME_3PM.toString()),
        props.getProperty(CsgProp.TIME_4PM.toString()),
        props.getProperty(CsgProp.TIME_5PM.toString()),
        props.getProperty(CsgProp.TIME_6PM.toString()),
        props.getProperty(CsgProp.TIME_7PM.toString()),
        props.getProperty(CsgProp.TIME_8PM.toString()),
        props.getProperty(CsgProp.TIME_9PM.toString()),
        props.getProperty(CsgProp.TIME_10PM.toString()),
        props.getProperty(CsgProp.TIME_11PM.toString())
        );
        startTime = new ComboBox(time_options);
        endTime = new ComboBox(time_options);
        
        officeHoursHeaderBox.getChildren().add(startTime);
        startTime.setPrefHeight(42);
        startTime.setPrefWidth(150);
        startTime.getSelectionModel().select(data.getStartHour());
        
        officeHoursHeaderBox.getChildren().add(endTime);
        endTime.setPrefHeight(42);
        endTime.setPrefWidth(150);
        endTime.getSelectionModel().select(data.getEndHour());
        
        changeButton = new Button("Change");
        officeHoursHeaderBox.setStyle("-fx-spacing: 10;");
        officeHoursHeaderBox.getChildren().add(changeButton);
        officeHoursHeaderBox.setAlignment(Pos.CENTER_LEFT);
        
        changeButton.setOnAction(e -> {
            boolean diffStart = startTime.getSelectionModel().getSelectedIndex() != data.getStartHour();
            boolean diffEnd = endTime.getSelectionModel().getSelectedIndex() != data.getEndHour();
            
            if(diffStart || diffEnd){
                controller.changeTime();
            }
        });
        
        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridTimeHeaderPanes = new HashMap();
        officeHoursGridTimeHeaderLabels = new HashMap();
        officeHoursGridDayHeaderPanes = new HashMap();
        officeHoursGridDayHeaderLabels = new HashMap();
        officeHoursGridTimeCellPanes = new HashMap();
        officeHoursGridTimeCellLabels = new HashMap();
        officeHoursGridTACellPanes = new HashMap();
        officeHoursGridTACellLabels = new HashMap();
        
        // RIGHT PANE
        VBox rightPane = new VBox();
        rightPane.getChildren().addAll(officeHoursHeaderBox, officeHoursGridPane);
        //rightPane.setScaleX(0.5);
        //rightPane.setScaleY(0.5);
        
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, new ScrollPane(rightPane));
        
        space.getTaTab().setContent(sPane);
    }

    public CheckBox getUndergradCheck() {
        return undergradCheck;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }
    
    public TableView getTATable() {
        return taTable;
    }
    
    public ComboBox getOfficeHour(boolean start){
        if(start)
            return startTime;
        return endTime;
    }
    
    public String getCellKey(Pane testPane) {
        for (String key : officeHoursGridTACellLabels.keySet()) {
            if (officeHoursGridTACellPanes.get(key) == testPane) {
                return key;
            }
        }
        return null;
    }

    public Label getTACellLabel(String cellKey) {
        return officeHoursGridTACellLabels.get(cellKey);
    }

    public Pane getTACellPane(String cellPane) {
        return officeHoursGridTACellPanes.get(cellPane);
    }
    
    public HashMap<String, Pane> getOfficeHoursGridTimeHeaderPanes() {
        return officeHoursGridTimeHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeHeaderLabels() {
        return officeHoursGridTimeHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridDayHeaderPanes() {
        return officeHoursGridDayHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridDayHeaderLabels() {
        return officeHoursGridDayHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeCellPanes() {
        return officeHoursGridTimeCellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeCellLabels() {
        return officeHoursGridTimeCellLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTACellPanes() {
        return officeHoursGridTACellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTACellLabels() {
        return officeHoursGridTACellLabels;
    }
    
    public GridPane getOfficeHoursGridPane() {
        return officeHoursGridPane;
    }

    public TAController getController() {
        return controller;
    }
    
    public void addCellToGrid(TAData dataComponent, HashMap<String, Pane> panes, HashMap<String, Label> labels, int col, int row) {       
        // MAKE THE LABEL IN A PANE
        Label cellLabel = new Label("");
        HBox cellPane = new HBox();
        cellPane.setAlignment(Pos.CENTER);
        cellPane.getChildren().add(cellLabel);

        // BUILD A KEY TO EASILY UNIQUELY IDENTIFY THE CELL
        String cellKey = dataComponent.getCellKey(col, row);
        cellPane.setId(cellKey);
        cellLabel.setId(cellKey);
        
        // NOW PUT THE CELL IN THE WORKSPACE GRID
        officeHoursGridPane.add(cellPane, col, row);
        
        // AND ALSO KEEP IN IN CASE WE NEED TO STYLIZE IT
        panes.put(cellKey, cellPane);
        labels.put(cellKey, cellLabel);
        
        // AND FINALLY, GIVE THE TEXT PROPERTY TO THE DATA MANAGER
        // SO IT CAN MANAGE ALL CHANGES
        dataComponent.setCellProperty(col, row, cellLabel.textProperty());        
    }
    
    public String buildCellText(int militaryHour, String minutes) {
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutes;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    @Override
    public void resetWorkspace() {
        // CLEAR OUT THE GRID PANE
        officeHoursGridPane.getChildren().clear();
        
        // AND THEN ALL THE GRID PANES AND LABELS
        officeHoursGridTimeHeaderPanes.clear();
        officeHoursGridTimeHeaderLabels.clear();
        officeHoursGridDayHeaderPanes.clear();
        officeHoursGridDayHeaderLabels.clear();
        officeHoursGridTimeCellPanes.clear();
        officeHoursGridTimeCellLabels.clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();
    }

    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        TAData taData = space.getCsg().getDataComponent().getTaData();
        reloadOfficeHoursGrid(taData);
        
        addButton.setText(props.getProperty(CsgProp.ADD_BUTTON_TEXT.toString()));
        addChange = true;
        nameTextField.clear();
        emailTextField.clear();
        undergradCheck.setSelected(false);
        taTable.getSelectionModel().select(null);
    }
    
    public void reloadOfficeHoursGrid(TAData dataComponent) {        
        ArrayList<String> gridHeaders = dataComponent.getGridHeaders();

        // ADD THE TIME HEADERS
        for (int i = 0; i < 2; i++) {
            addCellToGrid(dataComponent, officeHoursGridTimeHeaderPanes, officeHoursGridTimeHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }
        
        // THEN THE DAY OF WEEK HEADERS
        for (int i = 2; i < 7; i++) {
            addCellToGrid(dataComponent, officeHoursGridDayHeaderPanes, officeHoursGridDayHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));            
        }
        
        // THEN THE TIME AND TA CELLS
        int row = 1;
        for (int i = dataComponent.getStartHour(); i < dataComponent.getEndHour(); i++) {
            // START TIME COLUMN
            int col = 0;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(i, "00"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(i, "30"));

            // END TIME COLUMN
            col++;
            int endHour = i;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(endHour, "30"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row+1);
            dataComponent.getCellTextProperty(col, row+1).set(buildCellText(endHour+1, "00"));
            col++;

            // AND NOW ALL THE TA TOGGLE CELLS
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row+1);
                col++;
            }
            row += 2;
        }

        // CONTROLS FOR TOGGLING TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setFocusTraversable(true);
            p.setOnKeyPressed(e -> {
                controller.handleKeyPress(e.getCode());
            });
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
            });
            p.setOnMouseExited(e -> {
                controller.handleGridCellMouseExited((Pane) e.getSource());
            });
            p.setOnMouseEntered(e -> {
                controller.handleGridCellMouseEntered((Pane) e.getSource());
            });
        }
        
        // AND MAKE SURE ALL THE COMPONENTS HAVE THE PROPER STYLE
        CSGStyle taStyle = (CSGStyle) space.getCsg().getStyleComponent();
        taStyle.initOfficeHoursGridStyle();
    }

    public void clear() {
        addButton.setText(props.getProperty(CsgProp.ADD_BUTTON_TEXT.toString()));
        addChange = true;
        nameTextField.clear();
        emailTextField.clear();
        undergradCheck.setSelected(false);
        taTable.getSelectionModel().select(null);
    }
}
