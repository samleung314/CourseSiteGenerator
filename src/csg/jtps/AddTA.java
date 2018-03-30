/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.CourseSiteGenerator;
import csg.taTab.TAController;
import csg.taTab.TAData;
import jtps.jTPS_Transaction;

public class AddTA implements jTPS_Transaction{
    
    boolean undergrad;
    private String name, email;
    private CourseSiteGenerator csg;
    private TAData data;
    private TAController control;

    public AddTA(CourseSiteGenerator csg, boolean undergrad, String name, String email){
        this.undergrad = undergrad;
        this.name = name;
        this.email = email;
        this.csg = csg;
        data = csg.getDataComponent().getTaData();
        control = csg.getWorkspaceComponent().getTAWorkspace().getController();
    }
    
    @Override
    public void doTransaction() {
        data.addTA(undergrad, name, email);
        csg.getDataComponent().getRecitationData().resetCombos();
        csg.getWorkspaceComponent().getRecitaitonSpace().refreshTable();
    }

    @Override
    public void undoTransaction() {
        data.removeTA(name);
        csg.getDataComponent().getRecitationData().resetCombos();
        csg.getWorkspaceComponent().getRecitaitonSpace().refreshTable();
    }

}
