/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.CourseSiteGenerator;
import csg.taTab.TAData;
import csg.taTab.TAWorkspace;
import csg.taTab.TimeSlot;
import java.util.ArrayList;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;

public class HoursChange implements jTPS_Transaction{
    
    private CourseSiteGenerator csg;
    private TAData data;
    private TAWorkspace workspace;
    private int startTime;
    private int endTime;
    private int newStartTime;
    private int newEndTime;
    private ArrayList<TimeSlot> officeHours;
    
    public HoursChange(CourseSiteGenerator csg){
        this.csg = csg;
        this.data = csg.getDataComponent().getTaData();
        this.workspace = csg.getWorkspaceComponent().getTAWorkspace();

        ComboBox comboBox1 = workspace.getOfficeHour(true);
        ComboBox comboBox2 = workspace.getOfficeHour(false);
        startTime = data.getStartHour();
        endTime = data.getEndHour();
        newStartTime = comboBox1.getSelectionModel().getSelectedIndex();
        newEndTime = comboBox2.getSelectionModel().getSelectedIndex();
        officeHours = TimeSlot.buildOfficeHoursList(data);
    }

    @Override
    public void doTransaction() {
        workspace.getOfficeHoursGridPane().getChildren().clear();
        data.changeTime(newStartTime, newEndTime, officeHours);
    }

    @Override
    public void undoTransaction() {
        workspace.getOfficeHoursGridPane().getChildren().clear();
        data.changeTime(startTime, endTime, officeHours);
    }

}
