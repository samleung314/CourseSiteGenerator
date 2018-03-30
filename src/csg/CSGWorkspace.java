/**
 * @author Samson Leung 110490519
 */
package csg;

import csg.courseDetails.CourseWorkspace;
import csg.projectTab.ProjectWorkspace;
import csg.recitationTab.RecitationWorkspace;
import csg.scheduleTab.ScheduleWorkspace;
import csg.taTab.TAWorkspace;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import djf.ui.AppGUI;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import jtps.jTPS;
import properties_manager.PropertiesManager;

public class CSGWorkspace extends AppWorkspaceComponent{
    
    static jTPS jTPS = new jTPS();
    
    TabPane tabs;
    Tab courseTab, taTab, recitationTab, scheduleTab, projectTab;
    
    CourseSiteGenerator csg;
    CourseWorkspace courseSpace;
    RecitationWorkspace recitaitonSpace;
    ScheduleWorkspace scheduleSpace;
    ProjectWorkspace projectSpace;
    TAWorkspace taSpace;
    
    public CSGWorkspace(CourseSiteGenerator csg){
        this.csg = csg;
        
        csg.getGUI().getUndoButton().setOnAction(e -> {
            jTPS.undoTransaction();
            markWorkAsEdited();
        });
        csg.getGUI().getRedoButton().setOnAction(e -> {
            jTPS.doTransaction();
            markWorkAsEdited();
        });
        
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // Create tabs and add them to a TabPane
        tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);     // users cannot close tabs
        
        courseTab = new Tab(props.getProperty(CsgProp.COURSE_TAB.toString()));
        taTab = new Tab(props.getProperty(CsgProp.TA_TAB.toString()));
        recitationTab = new Tab(props.getProperty(CsgProp.RECITATION_TAB.toString()));
        scheduleTab = new Tab(props.getProperty(CsgProp.SCHEDULE_TAB.toString()));
        projectTab = new Tab(props.getProperty(CsgProp.PROJECT_TAB.toString()));
        
        courseSpace = new CourseWorkspace(this);
        recitaitonSpace = new RecitationWorkspace(this);
        scheduleSpace = new ScheduleWorkspace(this);
        projectSpace = new ProjectWorkspace(this);
        taSpace = new TAWorkspace(this);
        
        tabs.getTabs().addAll(courseTab, taTab, recitationTab, scheduleTab, projectTab);
        
        workspace = new BorderPane();
        workspace.setStyle("-fx-background-color:#e1ffb2");
        
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(tabs);
        
        workspace.setOnKeyPressed(e -> {
            if (e.isControlDown()) {
                if (e.getCode() == KeyCode.Z) {
                    jTPS.undoTransaction();
                    markWorkAsEdited();
                } else if (e.getCode() == KeyCode.Y) {
                    jTPS.doTransaction();
                    markWorkAsEdited();
                }
            }
        });
    }

    @Override
    public void resetWorkspace() {
        taSpace.resetWorkspace();
        taSpace.reloadOfficeHoursGrid(csg.getDataComponent().getTaData());
        courseSpace.resetWorkspace();
    }

    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        taSpace.reloadWorkspace(dataComponent);
    }
    
    private void markWorkAsEdited() {
        // MARK WORK AS EDITED
        AppGUI gui = csg.getGUI();
        gui.getFileController().markAsEdited(gui);
    }

    public CourseSiteGenerator getCsg() {
        return csg;
    }
    
    public CourseWorkspace getCourseSpace() {
        return courseSpace;
    }

    public RecitationWorkspace getRecitaitonSpace() {
        return recitaitonSpace;
    }

    public ScheduleWorkspace getScheduleSpace() {
        return scheduleSpace;
    }

    public ProjectWorkspace getProjectSpace() {
        return projectSpace;
    }
    
    public Tab getCourseTab() {
        return courseTab;
    }

    public void setCourseTab(Tab courseTab) {
        this.courseTab = courseTab;
    }

    public Tab getTaTab() {
        return taTab;
    }

    public void setTaTab(Tab taTab) {
        this.taTab = taTab;
    }

    public Tab getRecitationTab() {
        return recitationTab;
    }

    public void setRecitationTab(Tab recitationTab) {
        this.recitationTab = recitationTab;
    }

    public Tab getScheduleTab() {
        return scheduleTab;
    }

    public void setScheduleTab(Tab scheduleTab) {
        this.scheduleTab = scheduleTab;
    }

    public Tab getProjectTab() {
        return projectTab;
    }

    public void setProjectTab(Tab projectTab) {
        this.projectTab = projectTab;
    }

    public TAWorkspace getTAWorkspace() {
        return taSpace;
    }
    
    public CourseSiteGenerator getCourseSiteGenerator() {
        return csg;
    }

    public static jTPS getjTPS() {
        return jTPS;
    }
    
}
