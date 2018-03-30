/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.scheduleTab.ScheduleData;
import csg.scheduleTab.ScheduleWorkspace;
import java.time.LocalDate;
import jtps.jTPS_Transaction;

public class ChangeMonday implements jTPS_Transaction{
    
    ScheduleWorkspace space;
    ScheduleData data; 
    LocalDate oldMonday, monday;
    
    public ChangeMonday(ScheduleWorkspace space, ScheduleData data, LocalDate oldMonday, LocalDate monday){
        this.space = space;
        this.data = data;
        this.monday = monday;
        this.oldMonday = oldMonday;
    }

    @Override
    public void doTransaction() {
        space.setUserAction(false);
        space.getMondayDate().setValue(monday);
        data.setStartDate(space.getMondayDate().getEditor().getText());
        data.setStartingMondayDay(Integer.toString(monday.getDayOfMonth()));
        data.setStartingMondayMonth(Integer.toString(monday.getMonthValue()));
        space.setUserAction(true);
    }

    @Override
    public void undoTransaction() {
        space.setUserAction(false);
        space.getMondayDate().setValue(oldMonday);
        data.setStartDate(space.getMondayDate().getEditor().getText());
        
        if(oldMonday != null){
            data.setStartingMondayDay(Integer.toString(oldMonday.getDayOfMonth()));
            data.setStartingMondayMonth(Integer.toString(oldMonday.getMonthValue()));
        }
        space.setUserAction(true);
    }
}
