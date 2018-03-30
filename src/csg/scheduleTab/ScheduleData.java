/**
 * @author Samson Leung 110490519
 */
package csg.scheduleTab;

import csg.CourseSiteGenerator;
import djf.components.AppDataComponent;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScheduleData implements AppDataComponent {
    
    String startDate, endDate;
    String startingMondayMonth, startingMondayDay, endingFridayMonth, endingFridayDay;
    ObservableList<ScheduleItem> scheduleItems;
    CourseSiteGenerator csg;
    
    
    public ScheduleData(CourseSiteGenerator csg){
        this.scheduleItems = FXCollections.observableArrayList();
        this.csg = csg;
    }
    
    public void addScheduleItem(ScheduleItem schedule){
        scheduleItems.add(schedule);
        
        //Sort items
        Collections.sort(scheduleItems);
    }
    
    public void addScheduleItem(String type, String date, String title){
        ScheduleItem schedule = new ScheduleItem(type, date, title);
        scheduleItems.add(schedule);
        
        //Sort items
        Collections.sort(scheduleItems);
    }
    
    public void removeScheduleItem(ScheduleItem schedule){
        scheduleItems.remove(schedule);
    }

    @Override
    public void resetData() {
        scheduleItems.clear();
        startDate = endDate = startingMondayMonth = startingMondayDay =
                endingFridayMonth = endingFridayDay = "";
        csg.getWorkspaceComponent().getScheduleSpace().getMondayDate().getEditor().clear();
        csg.getWorkspaceComponent().getScheduleSpace().getFridayDate().getEditor().clear();
    }

    public ObservableList<ScheduleItem> getScheduleItems() {
        return scheduleItems;
    }

    public String getStartingMondayMonth() {
        return startingMondayMonth;
    }

    public String getStartingMondayDay() {
        return startingMondayDay;
    }

    public String getEndingFridayMonth() {
        return endingFridayMonth;
    }

    public String getEndingFridayDay() {
        return endingFridayDay;
    }

    public void setStartingMondayMonth(String startingMondayMonth) {
        this.startingMondayMonth = startingMondayMonth;
    }

    public void setStartingMondayDay(String startingMondayDay) {
        this.startingMondayDay = startingMondayDay;
    }

    public void setEndingFridayMonth(String endingFridayMonth) {
        this.endingFridayMonth = endingFridayMonth;
    }

    public void setEndingFridayDay(String endingFridayDay) {
        this.endingFridayDay = endingFridayDay;
    }

    public void setScheduleItems(ObservableList<ScheduleItem> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }
    
    public void setDates(String start, String end){
        csg.getWorkspaceComponent().getScheduleSpace().setDates(start, end);
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
}
