/**
 * @author Samson Leung 110490519
 */
package csg.courseDetails;

import csg.CourseSiteGenerator;
import djf.components.AppDataComponent;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class CourseDetailsData implements AppDataComponent{
    
    CourseSiteGenerator csg;
    
    private String subject, semester, title, instructName, instructHome, 
            exportDir, templateDir, banner, leftFoot, rightFoot, styleSheet,
            number, year;
    
    ObservableList<SitePage> pages;
    
    public CourseDetailsData(CourseSiteGenerator csg) {
        this.csg = csg;
        this.pages = FXCollections.observableArrayList();
    }

    @Override
    public void resetData() {
        subject =  semester =  title =  instructName =  instructHome =  
        exportDir =  templateDir =  banner =  leftFoot =  rightFoot =  styleSheet = 
        number = year = "";
    }
    
    public void saveData()  {
        CourseWorkspace space = csg.getWorkspaceComponent().getCourseSpace();
        setSubject((String)space.subjectCombo.getValue());
        setSemester((String)space.semesterCombo.getValue());
        setTitle(space.titleText.getText());
        setInstructName(space.nameText.getText());
        setInstructHome(space.homeText.getText());
        setExportDir(space.exportPath.getText());
        setTemplateDir(space.tempDir.getText());
        
        setBanner(space.bannerPicture);
        setLeftFoot(space.leftPicture);
        setRightFoot(space.rightPicture);
        
        setNumber((String)space.numCombo.getValue());
        setYear((String)space.yearCombo.getValue());
    }
    
    public void loadDataGUI(){
        CourseWorkspace space = csg.getWorkspaceComponent().getCourseSpace();
        space.subjectCombo.setValue(this.subject);
        space.numCombo.setValue(this.number);
        space.semesterCombo.setValue(this.semester);
        space.yearCombo.setValue(this.year);
        space.titleText.setText(this.title);
        space.nameText.setText(this.instructName);
        space.homeText.setText(this.instructHome);
        space.exportPath.setText(this.exportDir);
        
        if(!exportDir.isEmpty()){
            csg.getGUI().getFileController().setExportPath(exportDir);
            csg.getGUI().getExportButton().setDisable(false);
        }
        
        space.tempDir.setText(this.templateDir);
        space.loadSites(new File(templateDir));
        
        try{
            space.bannerImg.setImage(new Image(banner, 100, 100, true, true));
            space.leftFooterImg.setImage(new Image(leftFoot, 100, 100, true, true));
            space.rightFooterImg.setImage(new Image(rightFoot, 100, 100, true, true));
        }catch(NullPointerException e){
            //e.printStackTrace();
        }catch(IllegalArgumentException f){
            //f.printStackTrace();
        }

        space.cssCombo.setValue(this.styleSheet);
    }

    public void setSubject(String subject) {
        if(subject == null)
            return;
        this.subject = subject;
    }

    public void setSemester(String semester) {
        if(semester == null)
            return;
        this.semester = semester;
    }

    public void setTitle(String title) {
        if(title == null)
            return;
        this.title = title;
    }

    public void setInstructName(String instructName) {
        if(instructName == null)
            return;
        this.instructName = instructName;
    }

    public void setInstructHome(String instructHome) {
        if(instructHome == null)
            return;
        this.instructHome = instructHome;
    }

    public void setExportDir(String exportDir) {
        if(exportDir == null)
            return;
        this.exportDir = exportDir;
    }

    public void setTemplateDir(String templateDir) {
        if(templateDir == null)
            return;
        this.templateDir = templateDir;
    }

    public void setBanner(String banner) {
        if(banner == null)
            return;
        this.banner = banner;
    }

    public void setLeftFoot(String leftFoot) {
        if(leftFoot == null)
            return;
        this.leftFoot = leftFoot;
    }

    public void setRightFoot(String rightFoot) {
        if(rightFoot == null)
            return;
        this.rightFoot = rightFoot;
    }

    public void setStyleSheet(String styleSheet) {
        if(styleSheet == null)
            return;
        this.styleSheet = styleSheet;
    }

    public void setNumber(String number) {
        if(number == null)
            return;
        this.number = number;
    }

    public void setYear(String year) {
        if(year == null)
            return;
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public String getSemester() {
        return semester;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructName() {
        return instructName;
    }

    public String getInstructHome() {
        return instructHome;
    }

    public String getExportDir() {
        return exportDir;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public String getBanner() {
        return banner;
    }

    public String getLeftFoot() {
        return leftFoot;
    }

    public String getRightFoot() {
        return rightFoot;
    }

    public String getStyleSheet() {
        return styleSheet;
    }

    public String getNumber() {
        return number;
    }

    public String getYear() {
        return year;
    }

    public ObservableList<SitePage> getPages() {
        return pages;
    }

    public boolean isHome() {
        for(SitePage p : pages){
            if(p.getNavBar() == "Home"){
                return p.isUse();
            }
        }
        return false;
    }

    public boolean isSyllabus() {
        for(SitePage p : pages){
            if(p.getNavBar() == "Syllabus"){
                return p.isUse();
            }
        }
        return false;
    }

    public boolean isSchedule() {
        for(SitePage p : pages){
            if(p.getNavBar() == "Schedule"){
                return p.isUse();
            }
        }
        return false;
    }

    public boolean isHws() {
        for(SitePage p : pages){
            if(p.getNavBar() == "HWs"){
                return p.isUse();
            }
        }
        return false;
    }

    public boolean isProjects() {
        for(SitePage p : pages){
            if(p.getNavBar() == "Projects"){
                return p.isUse();
            }
        }
        return false;
    }
    
    public String navBar(){
        // Home, Syllabus, Schedule, HWs, Projects
        String navBar = "<a href=\"http://www.stonybrook.edu\"><img alt=\"Banner\" id=\"bannerImg\" class=\"sbu_navbar\" src=\"\"></a>";
        
        if(isHome()){
            navBar+= "<a class=\"open_nav\" href=\"index.html\" id=\"home_link\">Home</a>";
        }
        if(isSyllabus()){
            navBar+= "<a class=\"nav\" href=\"syllabus.html\" id=\"syllabus_link\">Syllabus</a>";
        }
        if(isSchedule()){
            navBar+= "<a class=\"nav\" href=\"schedule.html\" id=\"schedule_link\">Schedule</a>";
        }
        if(isHws()){
            navBar+= "<a class=\"nav\" href=\"hws.html\" id=\"hws_link\">HWs</a>";
        }
        if(isProjects()){
            navBar+= "<a class=\"nav\" href=\"projects.html\" id=\"projects_link\">Projects</a>";
        }
        
        return navBar;
    }
}
