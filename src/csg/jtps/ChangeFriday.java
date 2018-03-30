/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.scheduleTab.ScheduleData;
import csg.scheduleTab.ScheduleWorkspace;
import java.time.LocalDate;
import jtps.jTPS_Transaction;

public class ChangeFriday implements jTPS_Transaction{
    
    ScheduleWorkspace space;
    ScheduleData data; 
    LocalDate oldFriday, friday;
    
    public ChangeFriday(ScheduleWorkspace space, ScheduleData data, LocalDate oldFriday, LocalDate friday){
        this.space = space;
        this.data = data;
        this.friday = friday;
        this.oldFriday = oldFriday;
    }

    @Override
    public void doTransaction() {
        space.setUserAction(false);
        space.getFridayDate().setValue(friday);
        data.setEndDate(space.getFridayDate().getEditor().getText());
        data.setEndingFridayDay(Integer.toString(friday.getDayOfMonth()));
        data.setEndingFridayMonth(Integer.toString(friday.getMonthValue()));
        space.setUserAction(true);
    }

    @Override
    public void undoTransaction() {
        space.setUserAction(false);
        space.getFridayDate().setValue(oldFriday);
        data.setEndDate(space.getFridayDate().getEditor().getText());
        
        if(oldFriday != null){
            data.setEndingFridayDay(Integer.toString(oldFriday.getDayOfMonth()));
            data.setEndingFridayMonth(Integer.toString(oldFriday.getMonthValue()));
        }
        space.setUserAction(true);
    }
}
