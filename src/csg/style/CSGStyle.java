/**
 * @author Samson Leung 110490519
 */
package csg.style;

import csg.courseDetails.CourseWorkspace;
import csg.projectTab.ProjectWorkspace;
import csg.recitationTab.RecitationWorkspace;
import csg.scheduleTab.ScheduleWorkspace;
import csg.taTab.TAWorkspace;
import csg.CSGWorkspace;
import djf.AppTemplate;
import djf.components.AppStyleComponent;
import java.util.HashMap;
import javafx.scene.Node;

public class CSGStyle extends AppStyleComponent {
    
    public static String CLASS_HEADER_LABEL = "header_label";
    public static String CLASS_COURSE_PANES = "course_panes";
    
    
    // WE'LL USE THIS FOR ORGANIZING LEFT AND RIGHT CONTROLS
    public static String CLASS_PLAIN_PANE = "plain_pane";
    
    // THESE ARE THE HEADERS FOR EACH SIDE
    public static String CLASS_HEADER_PANE = "header_pane";

    // ON THE LEFT WE HAVE THE TA ENTRY
    public static String CLASS_TA_TABLE = "ta_table";
    public static String CLASS_TA_TABLE_COLUMN_HEADER = "ta_table_column_header";
    public static String CLASS_ADD_TA_PANE = "add_ta_pane";
    public static String CLASS_ADD_TA_TEXT_FIELD = "add_ta_text_field";
    public static String CLASS_ADD_TA_BUTTON = "add_ta_button";

    // ON THE RIGHT WE HAVE THE OFFICE HOURS GRID
    public static String CLASS_OFFICE_HOURS_GRID = "office_hours_grid";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE = "office_hours_grid_time_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL = "office_hours_grid_time_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE = "office_hours_grid_day_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL = "office_hours_grid_day_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE = "office_hours_grid_time_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL = "office_hours_grid_time_cell_label";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE = "office_hours_grid_ta_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL = "office_hours_grid_ta_cell_label";

    // FOR HIGHLIGHTING CELLS, COLUMNS, AND ROWS
    public static String CLASS_HIGHLIGHTED_GRID_CELL = "highlighted_grid_cell";
    public static String CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN = "highlighted_grid_row_or_column";

    // THIS PROVIDES ACCESS TO OTHER COMPONENTS
    private AppTemplate app;
    
    /**
     * This constructor initializes all style for the application.
     * 
     * @param initApp The application to be stylized.
     */
    public CSGStyle(AppTemplate initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // LET'S USE THE DEFAULT STYLESHEET SETUP
        super.initStylesheet(app);

        // INIT THE STYLE FOR THE FILE TOOLBAR
        app.getGUI().initFileToolbarStyle();

        // AND NOW OUR WORKSPACE STYLE
        initCSGWorkspaceStyle();
    }
    
    /**
     * This function specifies all the style classes for
     * all user interface controls in the workspace.
     */
    private void initCSGWorkspaceStyle() {
        CSGWorkspace work = (CSGWorkspace)app.getWorkspaceComponent();
        CourseWorkspace courseWork = work.getCourseSpace();
        RecitationWorkspace recitationSpace = work.getRecitaitonSpace();
        ScheduleWorkspace scheduleSpace = work.getScheduleSpace();
        ProjectWorkspace projectSpace = work.getProjectSpace();
        
        courseWork.getInfo().getStyleClass().add(CLASS_HEADER_LABEL);
        courseWork.getSite().getStyleClass().add(CLASS_HEADER_LABEL);
        courseWork.getPage().getStyleClass().add(CLASS_HEADER_LABEL);
        
        courseWork.getCourseInfo().getStyleClass().add(CLASS_COURSE_PANES);
        courseWork.getSiteTemplate().getStyleClass().add(CLASS_COURSE_PANES);
        courseWork.getPageStyle().getStyleClass().add(CLASS_COURSE_PANES);
        
        recitationSpace.getRecitaitonLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        recitationSpace.getAddEditLabel().getStyleClass().add(CLASS_HEADER_LABEL);
    
        scheduleSpace.getAddEditLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        scheduleSpace.getScheduleLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        scheduleSpace.getBoundaries().getStyleClass().add(CLASS_COURSE_PANES);
        scheduleSpace.getItemsBox().getStyleClass().add(CLASS_COURSE_PANES);
    
        projectSpace.getProjectsLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        projectSpace.getTeamBox().getStyleClass().add(CLASS_COURSE_PANES);
        projectSpace.getStudentBox().getStyleClass().add(CLASS_COURSE_PANES);
    }
    
    /**
     * This method initializes the style for all UI components in
     * the office hours grid. Note that this should be called every
     * time a new TA Office Hours Grid is created or loaded.
     */
    public void initOfficeHoursGridStyle() {
        // RIGHT SIDE - THE OFFICE HOURS GRID TIME HEADERS
        CSGWorkspace work = (CSGWorkspace) app.getWorkspaceComponent();
        TAWorkspace workspaceComponent = work.getTAWorkspace();
        workspaceComponent.getOfficeHoursGridPane().getStyleClass().add(CLASS_OFFICE_HOURS_GRID);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderPanes(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeHeaderLabels(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderPanes(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridDayHeaderLabels(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellPanes(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTimeCellLabels(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellPanes(), CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        setStyleClassOnAll(workspaceComponent.getOfficeHoursGridTACellLabels(), CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
    }
    
    /**
     * This helper method initializes the style of all the nodes in the nodes
     * map to a common style, styleClass.
     */
    private void setStyleClassOnAll(HashMap nodes, String styleClass) {
        for (Object nodeObject : nodes.values()) {
            Node n = (Node)nodeObject;
            n.getStyleClass().add(styleClass);
        }
    }
}
