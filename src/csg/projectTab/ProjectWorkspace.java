/**
 * @author Samson Leung 110490519
 */
package csg.projectTab;

import csg.CsgProp;
import csg.CSGWorkspace;
import csg.jtps.AddStudent;
import csg.jtps.AddTeam;
import csg.jtps.DeleteStudent;
import csg.jtps.DeleteTeam;
import csg.jtps.UpdateStudent;
import csg.jtps.UpdateTeam;
import djf.ui.AppGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
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
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;

public class ProjectWorkspace {

    VBox projectSections, teamBox, studentBox;
    
    HBox teamsHeaderBox, studentHeaderBox;
    
    Label projectsLabel, teamsLabel, studentsLabel, 
            addEditTeamLabel, nameLabel, colorLabel, textColorLabel, linkLabel,
            addEditStudentLabel, firstNameLabel, lastNameLabel, teamLabel, roleLabel;
    
    Button removeTeamsButton, removeStudentsButton,
            addUpdateTeamButton, clearTeamButton,
            addUpdateStudentButton, clearStudentButton;
    
    TextField nameField, linkField,
            firstNameField, lastNameField, roleField;
    
    ComboBox<String> teamCombo;
    ObservableList<String> teams;
    
    ColorPicker colorPick, textColorPick;
    
    TableView<Team> teamsTable;
    TableColumn<Team, String> name, color, textColor, link;
    
    TableView<Student> studentsTable;
    TableColumn<Student, String> firstName, lastName, team, role;
    
    GridPane teamGrid, studentGrid;
    
    ProjectData data;
    CSGWorkspace space;
    
    public ProjectWorkspace(CSGWorkspace space){
        this.space = space;
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // props.getProperty(CsgProp.SECTION_COL.toString())
        
        data = space.getCourseSiteGenerator().getDataComponent().getProjectData();
        
        // Main label
        projectsLabel = new Label(props.getProperty(CsgProp.PROJECTS_LABEL.toString()));
        
        // Create Team Section //
        teamBox = new VBox();
        
        // Team box header
        teamsLabel = new Label(props.getProperty(CsgProp.TEAMS_LABEL.toString()));
        removeTeamsButton = new Button("-");
        removeTeamsButton.setOnAction(e -> {
            deleteTeam();
        });
        teamsHeaderBox = new HBox();
        teamsHeaderBox.getChildren().addAll(teamsLabel, removeTeamsButton);
        
        // Team table
        teamsTable = new TableView();
        teamsTable.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.DELETE){
                deleteTeam();
            }
        });
        teamsTable.setEditable(true);
        teamsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList teamData = data.getTeamList();
        teamsTable.setItems(teamData);
        
        teamsTable.setOnMouseClicked(e -> {
            Object select = (Team) teamsTable.getSelectionModel().getSelectedItem();
            Team team = (Team) select;
            
            nameField.setText(team.getName());
            colorPick.setValue(Color.web(team.getColor()));
            textColorPick.setValue(Color.web(team.getTextColor()));
            linkField.setText(team.getLink());
        });
        
        name = new TableColumn(props.getProperty(CsgProp.NAME_COL.toString()));
        color = new TableColumn(props.getProperty(CsgProp.COLOR_COL.toString())); 
        textColor = new TableColumn(props.getProperty(CsgProp.TEXT_COLOR_COL.toString()));
        link = new TableColumn(props.getProperty(CsgProp.LINK_COL.toString()));
        
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        color.setCellValueFactory(new PropertyValueFactory<>("color"));
        textColor.setCellValueFactory(new PropertyValueFactory<>("textColor"));
        link.setCellValueFactory(new PropertyValueFactory<>("link"));
        teamsTable.getColumns().addAll(name, color, textColor, link);
        
        // Team fields
        teamGrid = new GridPane();
        teamGrid.setStyle("-fx-hgap: 5; -fx-vgap:5; -fx-padding: 5;");
        
        addEditTeamLabel = new Label(props.getProperty(CsgProp.ADD_EDIT_LABEL.toString())); 
        teamGrid.add(addEditTeamLabel, 0, 0);
        
        nameLabel = new Label(props.getProperty(CsgProp.NAME_LABEL.toString())); 
        teamGrid.add(nameLabel, 0, 1);
        
        nameField = new TextField();
        teamGrid.add(nameField, 1, 1);
        
        colorLabel = new Label(props.getProperty(CsgProp.COLOR_LABEL.toString()));
        colorLabel.setStyle("-fx-padding: 15 15 15 0;");
        teamGrid.add(colorLabel, 0, 2);
        
        colorPick = new ColorPicker();
        teamGrid.add(colorPick, 1, 2);
        
        textColorLabel = new Label(props.getProperty(CsgProp.TEXT_COLOR_LABEL.toString())); 
        teamGrid.add(textColorLabel, 2, 2);
        
        textColorPick = new ColorPicker();
        teamGrid.add(textColorPick, 3, 2);
        
        linkLabel = new Label(props.getProperty(CsgProp.LINK_LABEL.toString()));
        teamGrid.add(linkLabel, 0, 3);
        
        linkField = new TextField();
        teamGrid.add(linkField, 1, 3);
        
        addUpdateTeamButton = new Button(props.getProperty(CsgProp.ADD_UPDATE_BUTTON.toString()));
        addUpdateTeamButton.setOnAction(e -> {
            Object select = (Team) teamsTable.getSelectionModel().getSelectedItem();
            Team selected = (Team) select;
            
            if(selected != null){
                // Update
                String name = nameField.getText();
                String color = '#' + colorPick.getValue().toString().substring(2, 8);
                String textColor = '#' + textColorPick.getValue().toString().substring(2, 8);
                String link = linkField.getText();
                
                jTPS_Transaction update = new UpdateTeam(this , data, selected, name, color, textColor, link);
                CSGWorkspace.getjTPS().addTransaction(update);
                teamCombo.setItems(data.getTeamNames());
            }else{
                // Add
                String name = nameField.getText();
                String color = '#' + colorPick.getValue().toString().substring(2, 8);
                String textColor = '#' + textColorPick.getValue().toString().substring(2, 8);
                String link = linkField.getText();
                
                boolean isEmpty = name.isEmpty() || link.isEmpty();

                if(!isEmpty){
                    Team team = new Team(name, color, textColor, link);
                    jTPS_Transaction add = new AddTeam(data, team);
                    CSGWorkspace.getjTPS().addTransaction(add);
                    teamCombo.setItems(data.getTeamNames());
                    markWorkAsEdited();
                }
            }
        });
        teamGrid.add(addUpdateTeamButton, 0, 4);
        
        clearTeamButton = new Button(props.getProperty(CsgProp.CLEAR_BUTTON.toString()));
        clearTeamButton.setOnAction(e ->{
            nameField.clear();
            colorPick.setValue(Color.WHITE);
            textColorPick.setValue(Color.WHITE);
            linkField.clear();
            teamsTable.getSelectionModel().clearSelection();
        });
        teamGrid.add(clearTeamButton, 1, 4);
        
        // Add UI elements to Team Pane
        teamBox.setStyle("-fx-spacing: 10; -fx-padding: 15;");
        teamBox.getChildren().addAll(teamsHeaderBox, teamsTable, teamGrid);
        
        // Student box header
        studentsLabel = new Label(props.getProperty(CsgProp.STUDENTS_LABEL.toString()));
        removeStudentsButton = new Button("-");
        removeStudentsButton.setOnAction(e -> {
            deleteStudent();
        });
        studentHeaderBox = new HBox();
        studentHeaderBox.getChildren().addAll(studentsLabel, removeStudentsButton);
        
        // Student table
        studentsTable = new TableView();
        studentsTable.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.DELETE){
                deleteStudent();
            }
        });
        studentsTable.setEditable(true);
        studentsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        studentsTable.setItems(data.getStudentList());
        
        studentsTable.setOnMouseClicked(e -> {
            Object select = (Student) studentsTable.getSelectionModel().getSelectedItem();
            Student s = (Student) select;
            
            firstNameField.setText(s.getFirstName());
            lastNameField.setText(s.getLastName());
            teamCombo.setValue(s.getTeam().getName());
            roleField.setText(s.getRole());
        });
        
        firstName = new TableColumn(props.getProperty(CsgProp.FIRST_NAME_COL.toString()));
        lastName = new TableColumn(props.getProperty(CsgProp.LAST_NAME_COL.toString())); 
        team = new TableColumn(props.getProperty(CsgProp.TEAM_COL.toString())); 
        role = new TableColumn(props.getProperty(CsgProp.ROLE_COL.toString()));
        
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        team.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        studentsTable.getColumns().addAll(firstName, lastName, team, role);
        
        // Student fields
        studentGrid = new GridPane();
        studentGrid.setStyle("-fx-hgap: 5; -fx-vgap:5; -fx-padding: 5;");
        
        addEditStudentLabel = new Label(props.getProperty(CsgProp.ADD_EDIT_LABEL.toString()));
        studentGrid.add(addEditStudentLabel, 0, 0);
        
        firstNameLabel = new Label(props.getProperty(CsgProp.FIRST_NAME_COL.toString()));
        studentGrid.add(firstNameLabel, 0, 1);
        
        firstNameField = new TextField();
        studentGrid.add(firstNameField, 1, 1);
        
        lastNameLabel = new Label(props.getProperty(CsgProp.LAST_NAME_COL.toString()));
        studentGrid.add(lastNameLabel, 0, 2);
        
        lastNameField = new TextField();
        studentGrid.add(lastNameField, 1, 2);
        
        teamLabel = new Label(props.getProperty(CsgProp.TEAM_COL.toString()));
        studentGrid.add(teamLabel, 0, 3);
        
        teamCombo = new ComboBox(data.getTeamNames());
        studentGrid.add(teamCombo, 1, 3);
        
        roleLabel = new Label(props.getProperty(CsgProp.ROLE_COL.toString()));
        studentGrid.add(roleLabel, 0, 4);
        
        roleField = new TextField();
        studentGrid.add(roleField, 1, 4);
        
        addUpdateStudentButton = new Button(props.getProperty(CsgProp.ADD_UPDATE_BUTTON.toString()));
        addUpdateStudentButton.setOnAction(e ->{
            String first = firstNameField.getText();
            String last = lastNameField.getText();
            Team team = null;
            
            for(Team t: data.getTeamList()){
                if(teamCombo.getValue() == t.getName()){
                    team = t;
                    break;
                }
            }
            
            String role = roleField.getText();
            
            boolean isEmpty = first.isEmpty() || last.isEmpty() || team == null || role.isEmpty();
            
            Object select = (Student) studentsTable.getSelectionModel().getSelectedItem();
            Student s = (Student) select;
            
            if(s != null){
                // Update
                jTPS_Transaction update = new UpdateStudent(this, data, s, first, last, team, role);
                CSGWorkspace.getjTPS().addTransaction(update);
            }else if(!isEmpty){
                // Add
                Student student = new Student(first, last, team, role);
                jTPS_Transaction add = new AddStudent(data, student);
                CSGWorkspace.getjTPS().addTransaction(add);
                studentsTable.getSelectionModel().clearSelection();
                markWorkAsEdited();
            }  
        });
        studentGrid.add(addUpdateStudentButton, 0, 5);
        
        clearStudentButton = new Button(props.getProperty(CsgProp.CLEAR_BUTTON.toString()));
        clearStudentButton.setOnAction(e -> {
            firstNameField.clear();
            lastNameField.clear();
            teamCombo.getSelectionModel().clearSelection();
            roleField.clear();
            studentsTable.getSelectionModel().clearSelection();
        });
        studentGrid.add(clearStudentButton, 1, 5);
        
        // Add UI elements to students Pane
        studentBox = new VBox();
        studentBox.setStyle("-fx-spacing: 10; -fx-padding: 15;");
        studentBox.getChildren().addAll(studentHeaderBox, studentsTable, studentGrid);
        
        // Add everything to projects table
        projectSections = new VBox();
        projectSections.setStyle("-fx-spacing: 10; -fx-padding: 15;");
        projectSections.getChildren().addAll(projectsLabel, teamBox, studentBox);
        
        space.getProjectTab().setContent(projectSections);
    }

    public Label getProjectsLabel() {
        return projectsLabel;
    }

    public VBox getTeamBox() {
        return teamBox;
    }

    public VBox getStudentBox() {
        return studentBox;
    }
    
    public void updateTables(){
        for(TableColumn c : teamsTable.getColumns()){
            c.setVisible(false);
            c.setVisible(true);
        }
        
        for(TableColumn d : studentsTable.getColumns()){
            d.setVisible(false);
            d.setVisible(true);
        }
    }
    
    void deleteTeam() {
        Object select = (Team) teamsTable.getSelectionModel().getSelectedItem();
        Team team = (Team) select;

        if (select != null) {
            jTPS_Transaction del = new DeleteTeam(data, team);
            CSGWorkspace.getjTPS().addTransaction(del);
            teamCombo.setItems(data.getTeamNames());
            markWorkAsEdited();
        }
    }
    
    void deleteStudent(){
        Object select = (Student) studentsTable.getSelectionModel().getSelectedItem();
        Student s = (Student) select;
            
        if(select != null){
            jTPS_Transaction del = new DeleteStudent(data, s);
            CSGWorkspace.getjTPS().addTransaction(del);
            markWorkAsEdited();
        }
    }
    
    private void markWorkAsEdited() {
        // MARK WORK AS EDITED
        AppGUI gui = space.getCourseSiteGenerator().getGUI();
        gui.getFileController().markAsEdited(gui);
    }
}
