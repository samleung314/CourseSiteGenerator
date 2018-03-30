/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.scheduleTab.ScheduleData;
import csg.scheduleTab.ScheduleItem;
import jtps.jTPS_Transaction;

public class AddScheduleItem implements jTPS_Transaction{

    ScheduleData data;
    ScheduleItem item;
    
    public AddScheduleItem(ScheduleData data, ScheduleItem item){
        this.data = data;
        this.item = item;
    }
    
    @Override
    public void doTransaction() {
        data.addScheduleItem(item);
    }

    @Override
    public void undoTransaction() {
        data.removeScheduleItem(item);
    }

}
