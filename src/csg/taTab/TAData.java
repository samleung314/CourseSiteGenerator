/**
 * @author Samson Leung 110490519
 */
package csg.taTab;

import csg.CourseSiteGenerator;
import csg.CsgProp;
import djf.components.AppDataComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;

public class TAData implements AppDataComponent {
    
    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CourseSiteGenerator app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistant> teachingAssistants;

    // THIS WILL STORE ALL THE OFFICE HOURS GRID DATA, WHICH YOU
    // SHOULD NOTE ARE StringProperty OBJECTS THAT ARE CONNECTED
    // TO UI LABELS, WHICH MEANS IF WE CHANGE VALUES IN THESE
    // PROPERTIES IT CHANGES WHAT APPEARS IN THOSE LABELS
    HashMap<String, StringProperty> officeHours;
    ObservableList<TimeSlot> officeHoursTest;
    
    // THESE ARE THE LANGUAGE-DEPENDENT VALUES FOR
    // THE OFFICE HOURS GRID HEADERS. NOTE THAT WE
    // LOAD THESE ONCE AND THEN HANG ON TO THEM TO
    // INITIALIZE OUR OFFICE HOURS GRID
    ArrayList<String> gridHeaders;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 0;
    public static final int MAX_END_HOUR = 23;
    
    public TAData(CourseSiteGenerator csg) {
        this.app = csg;
        // CONSTRUCT THE LIST OF TAs FOR THE TABLE
        teachingAssistants = FXCollections.observableArrayList();

        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        
        //THIS WILL STORE OUR OFFICE HOURS
        officeHours = new HashMap();
        officeHoursTest = FXCollections.observableArrayList();
        
        // THESE ARE THE LANGUAGE-DEPENDENT OFFICE HOURS GRID HEADERS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> timeHeaders = props.getPropertyOptionsList(CsgProp.OFFICE_HOURS_TABLE_HEADERS);
        ArrayList<String> dowHeaders = props.getPropertyOptionsList(CsgProp.DAYS_OF_WEEK);
        gridHeaders = new ArrayList();
        gridHeaders.addAll(timeHeaders);
        gridHeaders.addAll(dowHeaders);
    }
    
    public boolean containsTA(String testName, String testEmail) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return true;
            }
            if (ta.getEmail().equals(testEmail)) {
                return true;
            }
        }
        return false;
    }
    
    public void addTA(boolean underGrad, String initName, String initEmail) {
        // MAKE THE TA
        TeachingAssistant ta = new TeachingAssistant(underGrad, initName, initEmail);

        // ADD THE TA
        if (!containsTA(initName, initEmail)) {
            teachingAssistants.add(ta);
        }

        // SORT THE TAS
        Collections.sort(teachingAssistants);
    }
    
    public void addTA(TeachingAssistant ta) {

        // ADD THE TA
        if (!containsTA(ta.getName(), ta.getEmail())) {
            teachingAssistants.add(ta);
        }

        // SORT THE TAS
        Collections.sort(teachingAssistants);
    }
    
    public void removeTA(String name) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (name.equals(ta.getName())) {
                teachingAssistants.remove(ta);
                return;
            }
        }
    }
    
    public void replaceTAName(String name, String newName){
        TAWorkspace workspace = app.getWorkspaceComponent().getTAWorkspace();
        for(Pane p : workspace.getOfficeHoursGridTACellPanes().values()){
            String cellKey = p.getId();
            StringProperty cellProp = officeHours.get(cellKey);
            String cellText = cellProp.getValue();
            if (cellText.contains(name)) {
                toggleTAOfficeHours(cellKey, name);
                toggleTAOfficeHours(cellKey, newName);
            }
        }
    }
    
    public void addOfficeHoursReservation(String day, String time, String taName) {
        String cellKey = getCellKey(day, time);
        toggleTAOfficeHours(cellKey, taName);
    }
    
    /**
     * This function toggles the taName in the cell represented
     * by cellKey. Toggle means if it's there it removes it, if
     * it's not there it adds it.
     */
    public void toggleTAOfficeHours(String cellKey, String taName) {
        StringProperty cellProp = officeHours.get(cellKey);
        String cellText = cellProp.getValue();

        // IF IT ALREADY HAS THE TA, REMOVE IT
        if (cellText.contains(taName)) {
            removeTAFromCell(cellProp, taName);
        } // OTHERWISE ADD IT
        else if (cellText.length() == 0) {
            cellProp.setValue(taName);
        } else {
            cellProp.setValue(cellText + "\n" + taName);
        }
    }
    
    public void removeTAFromCell(StringProperty cellProp, String taName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(taName)) {
            cellProp.setValue("");
        }
        // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (cellText.indexOf(taName) == 0) {
            int startIndex = cellText.indexOf("\n") + 1;
            cellText = cellText.substring(startIndex);
            cellProp.setValue(cellText);
        }
        // IS IT IN THE MIDDLE OF A LIST OF TAs
        else if (cellText.indexOf(taName) < cellText.indexOf("\n", cellText.indexOf(taName))) {
            int startIndex = cellText.indexOf("\n" + taName);
            int endIndex = startIndex + taName.length() + 1;
            cellText = cellText.substring(0, startIndex) + cellText.substring(endIndex);
            cellProp.setValue(cellText);
        }
        // IT MUST BE THE LAST TA
        else {
            int startIndex = cellText.indexOf("\n" + taName);
            cellText = cellText.substring(0, startIndex);
            cellProp.setValue(cellText);
        }
    }
    
     public HashMap<String, StringProperty> getOfficeHours() {
        return officeHours;
    }
     
     public int getNumRows() {
        return ((endHour - startHour) * 2) + 1;
    }
    
    public ObservableList<TeachingAssistant> getTeachingAssistants() {
        return teachingAssistants;
    }
    
    public ObservableList getUndergradTANames() {
        ObservableList<String> names = FXCollections.observableArrayList();
        for(TeachingAssistant ta: teachingAssistants){
            if(ta.isUndergrad())
                names.add(ta.getName());
        }
        return names;
    }
    
    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }
    
    public TeachingAssistant getTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (testName.equals(ta.getName())) {
                return ta;
            }
        }
        return null;
    }
    
    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if ((initStartHour >= MIN_START_HOUR)
                && (initEndHour <= MAX_END_HOUR)
                && (initStartHour <= initEndHour)) {
            // THESE ARE VALID HOURS SO KEEP THEM
            initOfficeHours(initStartHour, initEndHour);
        }
    }
    
    private void initOfficeHours(int initStartHour, int initEndHour) {
        // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
        startHour = initStartHour;
        endHour = initEndHour;
        
        // EMPTY THE CURRENT OFFICE HOURS VALUES
        officeHours.clear();
            
        // WE'LL BUILD THE USER INTERFACE COMPONENT FOR THE
        // OFFICE HOURS GRID AND FEED THEM TO OUR DATA
        // STRUCTURE AS WE GO
        TAWorkspace workspaceComponent = app.getWorkspaceComponent().getTAWorkspace();
        workspaceComponent.reloadOfficeHoursGrid(this);
        
        workspaceComponent.getOfficeHour(true).getSelectionModel().select(startHour);
        workspaceComponent.getOfficeHour(false).getSelectionModel().select(endHour);
    }
    
    public ArrayList<String> getGridHeaders() {
        return gridHeaders;
    }
    
    public StringProperty getCellTextProperty(int col, int row) {
        String cellKey = getCellKey(col, row);
        return officeHours.get(cellKey);
    }
    
    public String getCellKey(String day, String time) {
        int col = gridHeaders.indexOf(day);
        int row = 1;
        int hour = Integer.parseInt(time.substring(0, time.indexOf("_")));
        int milHour = hour;
//        if (hour < startHour)
        if(time.contains("pm"))
            milHour += 12;
        if(time.contains("12"))
            milHour -= 12;
        row += (milHour - startHour) * 2;
        if (time.contains("_30"))
            row += 1;
        return getCellKey(col, row);
    }
    
    public String getCellKey(int col, int row) {
        return col + "_" + row;
    }
    
    /**
     * This method is for giving this data manager the string property
     * for a given cell.
     */
    public void setCellProperty(int col, int row, StringProperty prop) {
        String cellKey = getCellKey(col, row);
        officeHours.put(cellKey, prop);
    }
    
    public void changeTime(int startTime, int endTime, ArrayList<TimeSlot> officeHours){
        initHours("" + startTime, "" + endTime);
        for(TimeSlot ts : officeHours){
            String temp = ts.getTime();
            int tempint = Integer.parseInt(temp.substring(0, temp.indexOf('_')));
            if(temp.contains("pm"))
                tempint += 12;
            if(temp.contains("12"))
                tempint -= 12;
            if(tempint >= startTime && tempint <= endTime)
                try{
                    addOfficeHoursReservation(ts.getDay(), ts.getTime(), ts.getName());
                }catch(NullPointerException e){
                    
                }
        }
    }

    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void resetData() {
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        teachingAssistants.clear();
        officeHours.clear();
        
        TAWorkspace workspaceComponent = app.getWorkspaceComponent().getTAWorkspace();
        
        workspaceComponent.getOfficeHour(true).getSelectionModel().select(null);
        workspaceComponent.getOfficeHour(true).getSelectionModel().select(startHour);
        workspaceComponent.getOfficeHour(false).getSelectionModel().select(null);
        workspaceComponent.getOfficeHour(false).getSelectionModel().select(endHour);
    }
}
