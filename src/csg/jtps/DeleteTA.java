/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.CourseSiteGenerator;
import csg.taTab.TAData;
import csg.taTab.TAWorkspace;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import jtps.jTPS_Transaction;

public class DeleteTA implements jTPS_Transaction{
    
    private CourseSiteGenerator csg;
    private TAData data;
    private ArrayList<StringProperty> cellProps = new ArrayList<StringProperty>();
    private String TAname;
    private String TAemail;
    private boolean undergrad;
    
    public DeleteTA(CourseSiteGenerator csg, String name){
        this.csg = csg;
        this.data = csg.getDataComponent().getTaData();
        this.TAname = name;
        this.TAemail = data.getTA(TAname).getEmail();
        this.undergrad = data.getTA(TAname).isUndergrad();
        TAWorkspace workspace = csg.getWorkspaceComponent().getTAWorkspace();
        HashMap<String, Label> labels = workspace.getOfficeHoursGridTACellLabels();
        for (Label label : labels.values()) {
            if (label.getText().equals(TAname)
            || (label.getText().contains(TAname + "\n"))
            || (label.getText().contains("\n" + TAname))) {
                cellProps.add(label.textProperty());
            }
        }
    }

    @Override
    public void doTransaction() {
        csg.getDataComponent().getRecitationData().removeTA(data.getTA(TAname));
        data.removeTA(TAname);
        for(StringProperty cellProp : cellProps){
            data.removeTAFromCell(cellProp, TAname);
        }
        csg.getDataComponent().getRecitationData().resetCombos();
        csg.getWorkspaceComponent().getRecitaitonSpace().refreshTable();
    }

    @Override
    public void undoTransaction() {
        data.addTA(undergrad, TAname, TAemail);
        for(StringProperty cellProp : cellProps){
            String cellText = cellProp.getValue();
            if (cellText.length() == 0){
                cellProp.setValue(TAname);
            } else {
                cellProp.setValue(cellText + "\n" + TAname);}
        }
        csg.getDataComponent().getRecitationData().resetCombos();
        csg.getWorkspaceComponent().getRecitaitonSpace().refreshTable();
    }

}
