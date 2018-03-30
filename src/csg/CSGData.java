/**
 * @author Samson Leung 110490519
 */
package csg;

import csg.courseDetails.CourseDetailsData;
import csg.projectTab.ProjectData;
import csg.recitationTab.RecitationData;
import csg.scheduleTab.ScheduleData;
import csg.taTab.TAData;
import djf.components.AppDataComponent;

public class CSGData implements AppDataComponent {
    
    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CourseSiteGenerator csg;
    
    TAData taData;
    CourseDetailsData courseDetailsData;
    RecitationData recitationData;
    ScheduleData scheduleData;
    ProjectData projectData;
    
    public CSGData(CourseSiteGenerator csg) {
        this.csg = csg;
        this.taData = new TAData(csg);
        this.courseDetailsData = new CourseDetailsData(csg);
        this.recitationData = new RecitationData(csg);
        this.scheduleData = new ScheduleData(csg);
        this.projectData = new ProjectData();
    }

    @Override
    public void resetData() {
        CSGWorkspace.getjTPS().reset();
        taData.resetData();
        courseDetailsData.resetData();
        recitationData.resetData();
        scheduleData.resetData();
        csg.getWorkspaceComponent().getScheduleSpace().clearFields();
        projectData.resetData();
    }
    
    public void saveData(){
        courseDetailsData.saveData();
    }
    
    public void loadDataToGUI(){
        courseDetailsData.loadDataGUI();
    }

    public TAData getTaData() {
        return taData;
    }
    
    public CourseDetailsData getCourseDetailsData(){
        return courseDetailsData;
    }

    public RecitationData getRecitationData() {
        return recitationData;
    }

    public ScheduleData getScheduleData() {
        return scheduleData;
    }

    public ProjectData getProjectData() {
        return projectData;
    }
    
}
