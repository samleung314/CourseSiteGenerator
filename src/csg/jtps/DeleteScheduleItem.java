/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.scheduleTab.ScheduleData;
import csg.scheduleTab.ScheduleItem;
import jtps.jTPS_Transaction;

public class DeleteScheduleItem implements jTPS_Transaction{
    
    ScheduleData data;
    ScheduleItem item;
    
    public DeleteScheduleItem(ScheduleData data, ScheduleItem item){
        this.data = data;
        this.item = item;
    }

    @Override
    public void doTransaction() {
        data.removeScheduleItem(item);
    }

    @Override
    public void undoTransaction() {
        data.addScheduleItem(item);
    }

}
