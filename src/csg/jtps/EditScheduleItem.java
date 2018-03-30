/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.scheduleTab.ScheduleData;
import csg.scheduleTab.ScheduleItem;
import csg.scheduleTab.ScheduleWorkspace;
import jtps.jTPS_Transaction;

public class EditScheduleItem implements jTPS_Transaction{

    String newType, newDate, newTime, newTitle, newTopic, newLink, newCriteria;
    String oldType, oldDate, oldTime, oldTitle, oldTopic, oldLink, oldCriteria;
    
    ScheduleItem oldItem;
    ScheduleData data;
    ScheduleWorkspace space;
    
    public EditScheduleItem(ScheduleWorkspace space, ScheduleData data, ScheduleItem oldItem, String newType, 
            String newDate, String newTime, String newTitle, String newTopic, 
            String newLink, String newCriteria){
        this.space = space;
        this.oldItem = oldItem;
        this.data = data;
        
        this.newType = newType;
        this.newDate = newDate;
        this.newTime = newTime;
        this.newTitle = newTitle;
        this.newTopic = newTopic;
        this.newLink = newLink;
        this.newCriteria = newCriteria;
        
        this.oldType = oldItem.getType();
        this.oldDate = oldItem.getDate();
        this.oldTime = oldItem.getTime();
        this.oldTitle = oldItem.getTitle();
        this.oldTopic = oldItem.getTopic();
        this.oldLink = oldItem.getLink();
        this.oldCriteria = oldItem.getCriteria();
    }
    
    @Override
    public void doTransaction() {
        oldItem.setType(newType);
        oldItem.setDate(newDate);
        oldItem.setTime(newTime);
        oldItem.setTitle(newTitle);
        oldItem.setTopic(newTopic);
        oldItem.setLink(newLink);
        oldItem.setCriteria(newCriteria);
        space.updateTable();
    }

    @Override
    public void undoTransaction() {
        oldItem.setType(oldType);
        oldItem.setDate(oldDate);
        oldItem.setTime(oldTime);
        oldItem.setTitle(oldTitle);
        oldItem.setTopic(oldTopic);
        oldItem.setLink(oldLink);
        oldItem.setCriteria(oldCriteria);
        space.updateTable();
    }

}
