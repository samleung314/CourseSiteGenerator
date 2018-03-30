/**
 * @author Samson Leung 110490519
 */
package csg;

import csg.courseDetails.CourseDetailsData;
import csg.courseDetails.SitePage;
import csg.projectTab.ProjectData;
import csg.projectTab.Student;
import csg.projectTab.Team;
import csg.recitationTab.Recitation;
import csg.recitationTab.RecitationData;
import csg.scheduleTab.ScheduleData;
import csg.scheduleTab.ScheduleItem;
import csg.taTab.TAData;
import csg.taTab.TeachingAssistant;
import csg.taTab.TimeSlot;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import org.apache.commons.io.FileUtils;

public class CSGFiles implements AppFileComponent {
    
    CourseSiteGenerator csg;
    CSGData csgData;
    
    public CSGFiles(CourseSiteGenerator csg){
        this.csg = csg;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        // THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add("course_details", courseJSON())
                .add("ta_data", taJSON())
                .add("recitations", recitationJSON())
                .add("schedule_data", scheduleJSON())
                .add("project_data", projectJSON())
		.build();
        
        makeJSON(dataManagerJSO, filePath);
    }

    public JsonObject courseJSON(){
        CSGData dataManager = (CSGData)csg.getDataComponent();
        CourseDetailsData courseData = dataManager.getCourseDetailsData();
        
        dataManager.saveData();
        
        // Build Course Details array
        JsonObject courseJson = Json.createObjectBuilder()
                .add("navBar", courseData.navBar())
                .add("subject", "" + courseData.getSubject())
                .add("number", "" + courseData.getNumber())
                .add("semester", "" + courseData.getSemester())
                .add("year", "" + courseData.getYear())
                .add("title", "" + courseData.getTitle())
                .add("instructor_name", "" + courseData.getInstructName())
                .add("instructor_home", "" + courseData.getInstructHome())
                .add("export_directory", "" + courseData.getExportDir())
                .add("template_directory", "" + courseData.getTemplateDir())
                .add("left_footer", "" + courseData.getLeftFoot())
                .add("right_footer", "" + courseData.getRightFoot())
                .add("banner", "" + courseData.getBanner())
                .add("stylesheet", "" + courseData.getStyleSheet())
                .build();
        
        return courseJson;
    }
    
    public JsonObject taJSON(){
        CSGData dataManager = (CSGData)csg.getDataComponent();
        TAData taData = dataManager.getTaData();
        
        // Build TA Data array
        JsonArrayBuilder taArrayBuild = Json.createArrayBuilder();
        JsonArrayBuilder hoursArrayBuild = Json.createArrayBuilder();
        ObservableList<TeachingAssistant> taList = taData.getTeachingAssistants();
        ArrayList<TimeSlot> taHours = TimeSlot.buildOfficeHoursList(taData);

        for (TeachingAssistant ta : taList) {
            JsonObject taJson = Json.createObjectBuilder()
                    .add("name", ta.getName())
                    .add("email", ta.getEmail())
                    .add("undergrad", ta.isUndergrad())
                    .build();
            taArrayBuild.add(taJson);
        }

        for(TimeSlot slot : taHours){
            JsonObject hoursJson = Json.createObjectBuilder()
                    .add("day", slot.getDay())
                    .add("time", slot.getTime())
                    .add("name", slot.getName())
                    .build();
            hoursArrayBuild.add(hoursJson);
        }
        JsonArray taArray = taArrayBuild.build();
        JsonArray hoursArray = hoursArrayBuild.build();
        
        JsonObject taJson = Json.createObjectBuilder()
                .add("startHour", Integer.toString(taData.getStartHour()))
                .add("endHour", Integer.toString(taData.getEndHour()))
                .add("officeHours", hoursArray)
                .add("undergrad_tas",taArray)
                .build();
        
        return taJson;
    }
    
    public JsonArray recitationJSON(){
        CSGData dataManager = (CSGData)csg.getDataComponent();
        RecitationData recitationData = dataManager.getRecitationData();
        
        // Build Recitation array
        JsonArrayBuilder recitArrayBuild = Json.createArrayBuilder();
        ObservableList<Recitation> recitList = recitationData.getRecitations();
        
        for(Recitation recit : recitList){
            JsonObject recitJson = Json.createObjectBuilder()
                    .add("section", recit.getSection())
                    .add("instructor", "" + recit.getInstructor())
                    .add("day_time", recit.getTime())
                    .add("location", recit.getLocation())
                    .add("ta_1", "" + recit.getFirstTA())
                    .add("ta_2", "" + recit.getSecondTA())
                    .build();
            recitArrayBuild.add(recitJson);
        }
        JsonArray recitArray = recitArrayBuild.build();
        
        return recitArray;
    }
    
    public JsonObject scheduleJSON(){
        CSGData dataManager = (CSGData)csg.getDataComponent();
        ScheduleData scheduleData = dataManager.getScheduleData();
        
        // Build Schedule array
        JsonArrayBuilder holidayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lectureBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recitationsBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsBuilder = Json.createArrayBuilder();

        ObservableList<ScheduleItem> scheduleList = scheduleData.getScheduleItems();

        for (ScheduleItem item : scheduleList) {
            if (item.getType().equals("Holiday")) {
                JsonObject holiday = Json.createObjectBuilder()
                        .add("date", item.getDate())
                        .add("month", item.getMonth())
                        .add("day", item.getDay())
                        .add("title", item.getTitle())
                        .add("topic", item.getTopic())
                        .add("link", item.getLink())
                        .add("time", item.getTime())
                        .add("criteria", item.getCriteria())
                        .build();
                holidayBuilder.add(holiday);
            } else if (item.getType().equals("Lecture")) {
                JsonObject lecture = Json.createObjectBuilder()
                        .add("date", item.getDate())
                        .add("month", item.getMonth())
                        .add("day", item.getDay())
                        .add("title", item.getTitle())
                        .add("topic", item.getTopic())
                        .add("link", item.getLink())
                        .add("time", item.getTime())
                        .add("criteria", item.getCriteria())
                        .build();
                lectureBuilder.add(lecture);
            } else if (item.getType().equals("HW")) {
                JsonObject hw = Json.createObjectBuilder()
                        .add("date", item.getDate())
                        .add("month", item.getMonth())
                        .add("day", item.getDay())
                        .add("title", item.getTitle())
                        .add("topic", item.getTopic())
                        .add("link", item.getLink())
                        .add("time", item.getTime())
                        .add("criteria", item.getCriteria())
                        .build();
                hwsBuilder.add(hw);
            } else if (item.getType().equals("Reference")) {
                JsonObject ref = Json.createObjectBuilder()
                        .add("date", item.getDate())
                        .add("month", item.getMonth())
                        .add("day", item.getDay())
                        .add("title", item.getTitle())
                        .add("topic", item.getTopic())
                        .add("link", item.getLink())
                        .add("time", item.getTime())
                        .add("criteria", item.getCriteria())
                        .build();
                referencesBuilder.add(ref);
            } else if (item.getType().equals("Recitation")) {
                JsonObject rec = Json.createObjectBuilder()
                        .add("date", item.getDate())
                        .add("month", item.getMonth())
                        .add("day", item.getDay())
                        .add("title", item.getTitle())
                        .add("topic", item.getTopic())
                        .add("link", item.getLink())
                        .add("time", item.getTime())
                        .add("criteria", item.getCriteria())
                        .build();
                recitationsBuilder.add(rec);
            }
        }

        JsonArray holidayArray = holidayBuilder.build();
        JsonArray lectureArray = lectureBuilder.build();
        JsonArray referenceArray = referencesBuilder.build();
        JsonArray recitationArray = recitationsBuilder.build();
        JsonArray hwsArray = hwsBuilder.build();

        JsonObject scheduleJson = Json.createObjectBuilder()
                .add("startDate", scheduleData.getStartDate())
                .add("endDate", scheduleData.getEndDate())
                .add("startingMondayMonth", scheduleData.getStartingMondayMonth())
                .add("startingMondayDay", scheduleData.getStartingMondayDay())
                .add("endingFridayMonth", scheduleData.getEndingFridayMonth())
                .add("endingFridayDay", scheduleData.getEndingFridayDay())
                .add("holidays", holidayArray)
                .add("lectures", lectureArray)
                .add("references", referenceArray)
                .add("recitations", recitationArray)
                .add("hws", hwsArray)
                .build();

        return scheduleJson;
    }
    
    public JsonObject projectJSON(){
        CSGData dataManager = (CSGData)csg.getDataComponent();
        ProjectData projectData = dataManager.getProjectData();
        
        // Build Project array
        JsonArrayBuilder studentArrayBuild = Json.createArrayBuilder();
        JsonArrayBuilder teamArrayBuild = Json.createArrayBuilder();
        ObservableList<Student> studentList = projectData.getStudentList();
        ObservableList<Team> teamList = projectData.getTeamList();
        
        for(Student s : studentList){
            JsonObject studentJson = Json.createObjectBuilder()
                    .add("firstName", s.getFirstName())
                    .add("lastName", s.getLastName())
                    .add("team", s.getTeam().getName())
                    .add("role", s.getRole())
                    .build();
            studentArrayBuild.add(studentJson);
        }
        for(Team t : teamList){
            JsonObject hoursJson = Json.createObjectBuilder()
                    .add("name", t.getName())
                    .add("color", t.getColor())
                    .add("text_color", t.getTextColor())
                    .add("link", t.getLink())
                    .add("red", t.getRed())
                    .add("green", t.getGreen())
                    .add("blue", t.getBlue())
                    .build();
            teamArrayBuild.add(hoursJson);
        }
        JsonArray studentArray = studentArrayBuild.build();
        JsonArray teamArray = teamArrayBuild.build();
        
        JsonObject projectJson = Json.createObjectBuilder()
                .add("students",studentArray)
                .add("teams", teamArray)
                .build();
        
        return projectJson;
    }
    
    public JsonObject actuallyProjectJSON(){
        CSGData dataManager = (CSGData)csg.getDataComponent();
        ProjectData projectData = dataManager.getProjectData();
        
        String semester = dataManager.getCourseDetailsData().getSemester() + " " 
                + dataManager.getCourseDetailsData().getYear();
        
        
        JsonArrayBuilder projectsArrayBuilder = Json.createArrayBuilder();
        
        for(Team t : projectData.getTeamList()){
            JsonArrayBuilder studentArrayBuilder = Json.createArrayBuilder();
            
            for(Student s : projectData.getStudentList()){
                if(s.getTeam().getName().equals(t.getName())){
                    studentArrayBuilder.add(s.getFirstName() + " " + s.getLastName());
                }
            }
            JsonArray studentArray = studentArrayBuilder.build();
            
            JsonObject project = Json.createObjectBuilder()
                    .add("name", t.getName())
                    .add("students", studentArray)
                    .add("link", t.getLink())
                    .build();
            projectsArrayBuilder.add(project);
        }
        
        JsonArray projectArray = projectsArrayBuilder.build();
        JsonObject innerWork = Json.createObjectBuilder()
                                .add("semester", semester)
                                .add("projects", projectArray)
                                .build();
        
        
        
        
        JsonArrayBuilder workArrayBuilder = Json.createArrayBuilder();
        workArrayBuilder.add(innerWork);
        
        JsonArray workArray = workArrayBuilder.build();
        
        JsonObject projectJson = Json.createObjectBuilder()
                .add("work", workArray)
                .build();
        
        return projectJson;
    }
    
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        this.csgData = (CSGData) data;
        
        // LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
        // Load Course Data
        loadCourseData(json);
        
        // Load TA Data
        loadTAData(json);
        
        // Load recitation Data
        loadRecitations(json);
        
        // Load schedule Data
        loadSchedule(json);
        
        // Load project Data
        loadProject(json);
    }

    // Helper Method for loading Course Data
    void loadCourseData(JsonObject json){
        JsonObject course = json.getJsonObject("course_details");
        
        CourseDetailsData courseDetails = csgData.courseDetailsData;
        courseDetails.setSubject(course.getString("subject"));
        courseDetails.setNumber(course.getString("number"));
        courseDetails.setSemester(course.getString("semester"));
        courseDetails.setYear(course.getString("year"));
        courseDetails.setTitle(course.getString("title"));
        courseDetails.setInstructName(course.getString("instructor_name"));
        courseDetails.setInstructHome(course.getString("instructor_home"));
        courseDetails.setExportDir(course.getString("export_directory"));
        courseDetails.setTemplateDir(course.getString("template_directory"));
        courseDetails.setBanner(course.getString("banner"));
        courseDetails.setLeftFoot(course.getString("left_footer"));
        courseDetails.setRightFoot(course.getString("right_footer"));
        courseDetails.setStyleSheet(course.getString("stylesheet"));
        csgData.loadDataToGUI();
    }
    
    // Helper Method for loading TAData
    void loadTAData(JsonObject mainJson){
        // CLEAR THE OLD DATA OUT
	TAData dataManager = csgData.getTaData();
        
        JsonObject json = mainJson.getJsonObject("ta_data");
        
        // LOAD THE START AND END HOURS
	String startHour = json.getString("startHour");
        String endHour = json.getString("endHour");
        csg.getWorkspaceComponent().getTAWorkspace().resetWorkspace();
        dataManager.initHours(startHour, endHour);
        
        // NOW RELOAD THE WORKSPACE WITH THE LOADED DATA
        csg.getWorkspaceComponent().getTAWorkspace().reloadWorkspace(dataManager);
        
        // NOW LOAD ALL THE UNDERGRAD TAs
        JsonArray jsonTAArray = json.getJsonArray("undergrad_tas");
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString("name");
            String email = jsonTA.getString("email");
            boolean undergrad = jsonTA.getBoolean("undergrad");
            dataManager.addTA(undergrad, name, email);
        }
        
        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray("officeHours");
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String day = jsonOfficeHours.getString("day");
            String time = jsonOfficeHours.getString("time");
            String name = jsonOfficeHours.getString("name");
            dataManager.addOfficeHoursReservation(day, time, name);
        }
    }
    
    // Helper method for loading recitations
    void loadRecitations(JsonObject json){
        JsonArray recArray = json.getJsonArray("recitations");
        RecitationData data = csgData.getRecitationData();
        TAData taData = csgData.getTaData();
        
        for(int i=0; i<recArray.size(); i++){
            JsonObject rec = recArray.getJsonObject(i);
            String section = rec.getString("section");
            String instructor = rec.getString("instructor");
            String time = rec.getString("day_time");
            String location = rec.getString("location");
            String firstTa = rec.getString("ta_1");
            String secondTa = rec.getString("ta_2");
            
            TeachingAssistant a = taData.getTA(firstTa);
            TeachingAssistant b = taData.getTA(secondTa);
            if(firstTa.equals("null"))
                firstTa = "";
            if(secondTa.equals("null"))
                secondTa = "";
            data.addRecitation(section, instructor, time, location, a, b);
        }
        
        data.resetCombos();
    }
    
    // Helper method for loading schedules
    void loadSchedule(JsonObject json){
        ScheduleData data = csgData.getScheduleData();
        
        JsonObject scheduleJson = json.getJsonObject("schedule_data");
        String startDate = scheduleJson.getString("startDate");
        String endDate = scheduleJson.getString("endDate");
        String startingMondayMonth = scheduleJson.getString("startingMondayMonth");
        String startingMondayDay = scheduleJson.getString("startingMondayDay");
        String endingFridayMonth = scheduleJson.getString("endingFridayMonth");
        String endingFridayDay = scheduleJson.getString("endingFridayDay");
        
        JsonArray holidays = scheduleJson.getJsonArray("holidays");
        JsonArray lectures = scheduleJson.getJsonArray("lectures");
        JsonArray references = scheduleJson.getJsonArray("references");
        JsonArray recitations = scheduleJson.getJsonArray("recitations");
        JsonArray hws = scheduleJson.getJsonArray("hws");
        
        data.setStartDate(startDate);
        data.setEndDate(endDate);
        data.setDates(startDate, endDate);
        data.setStartingMondayDay(startingMondayDay);
        data.setStartingMondayMonth(startingMondayMonth);
        data.setEndingFridayDay(endingFridayDay);
        data.setEndingFridayMonth(endingFridayMonth);
        
        for(int i=0; i<holidays.size(); i++){
            JsonObject item = holidays.getJsonObject(i);
            ScheduleItem sch = new ScheduleItem("Holiday", item.getString("date"), item.getString("title"));
            sch.setTime(item.getString("time"));
            sch.setTopic(item.getString("topic"));
            sch.setLink(item.getString("link"));
            sch.setCriteria(item.getString("criteria"));
            data.addScheduleItem(sch);
        }
        
        for(int i=0; i<lectures.size(); i++){
            JsonObject item = lectures.getJsonObject(i);
            ScheduleItem sch = new ScheduleItem("Lecture", item.getString("date"), item.getString("title"));
            sch.setTime(item.getString("time"));
            sch.setTopic(item.getString("topic"));
            sch.setLink(item.getString("link"));
            sch.setCriteria(item.getString("criteria"));
            data.addScheduleItem(sch);
        }
        
        for(int i=0; i<hws.size(); i++){
            JsonObject item = hws.getJsonObject(i);
            ScheduleItem sch = new ScheduleItem("HW", item.getString("date"), item.getString("title"));
            sch.setTime(item.getString("time"));
            sch.setTopic(item.getString("topic"));
            sch.setLink(item.getString("link"));
            sch.setCriteria(item.getString("criteria"));
            data.addScheduleItem(sch);
        }
        
        for(int i=0; i<references.size(); i++){
            JsonObject item = references.getJsonObject(i);
            ScheduleItem sch = new ScheduleItem("Reference", item.getString("date"), item.getString("title"));
            sch.setTime(item.getString("time"));
            sch.setTopic(item.getString("topic"));
            sch.setLink(item.getString("link"));
            sch.setCriteria(item.getString("criteria"));
            data.addScheduleItem(sch);
        }
        
        for(int i=0; i<recitations.size(); i++){
            JsonObject item = recitations.getJsonObject(i);
            ScheduleItem sch = new ScheduleItem("Recitation", item.getString("date"), item.getString("title"));
            sch.setTime(item.getString("time"));
            sch.setTopic(item.getString("topic"));
            sch.setLink(item.getString("link"));
            sch.setCriteria(item.getString("criteria"));
            data.addScheduleItem(sch);
        }
    }
    
    // Helper method for loading projects
    void loadProject(JsonObject json){
        ProjectData data = csgData.getProjectData();
        JsonObject project = json.getJsonObject("project_data");
        JsonArray students = project.getJsonArray("students");
        JsonArray teams = project.getJsonArray("teams");
        
        for(int i=0; i<teams.size(); i++){
            JsonObject team = teams.getJsonObject(i);
            String name = team.getString("name");
            String color = team.getString("color");
            String textColor = team.getString("text_color");
            String link = team.getString("link");
            
            data.addTeam(new Team(name, color, textColor, link));
        }
        
        for(int i=0; i<students.size(); i++){
            JsonObject student = students.getJsonObject(i);
            String first = student.getString("firstName");
            String last = student.getString("lastName");
            String team = student.getString("team");
            String role = student.getString("role");
            
            data.addStudent(new Student(first, last, data.getTeam(team), role));
        }
    }
    
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        File css = new File(csg.getDataComponent().getCourseDetailsData().getStyleSheet());
        File dest = new File(filePath);
        File brands = new File("./work/brands");
        File layout = new File("./work/css/course_homepage_layout.css");
        File js = new File("./work/js");
        
        // Make CSS folder
        File cssFolder = new File(filePath + "/css");
        cssFolder.mkdirs();
        
        // Make Images folder
        File images = new File(filePath + "/images");
        images.mkdirs();
        
        // Make JS folder
        File jsFolder = new File(filePath + "/js");
        jsFolder.mkdirs();
        
        // Add five JSON files to JS folder
        makeJSON(courseJSON(), filePath + "/js/Course.json");
        
        JsonObject recitationObj = Json.createObjectBuilder()
                .add("recitations", recitationJSON()).build();
        
        makeJSON(taJSON(), filePath + "/js/OfficeHoursGridData.json");
        makeJSON(recitationObj, filePath + "/js/RecitationsData.json");
        makeJSON(scheduleJSON(), filePath + "/js/ScheduleData.json");
        makeJSON(projectJSON(), filePath + "/js/TeamsAndStudents.json");
        makeJSON(actuallyProjectJSON(), filePath + "/js/ProjectsData.json");

        try {
            // Copy Templates
            for (SitePage s : csg.getDataComponent().getCourseDetailsData().getPages()) {
                if (s.isUse()) {
                    FileUtils.copyFileToDirectory(s.getFile(), dest);
                }
            }
            
            // Copy Images
            for (File f : brands.listFiles()) {
                FileUtils.copyFileToDirectory(f, images);
            }
            
            // Copy JS Files
            for (File g : js.listFiles()){
                FileUtils.copyFileToDirectory(g, jsFolder);
            }

            // Copy Stylesheets
            FileUtils.copyFileToDirectory(css, cssFolder);
            FileUtils.copyFileToDirectory(layout, cssFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void makeJSON(JsonObject dataManagerJSO, String filePath) throws FileNotFoundException{
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();
        
        // INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
