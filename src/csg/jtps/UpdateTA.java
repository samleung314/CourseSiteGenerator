/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.CourseSiteGenerator;
import csg.taTab.TAController;
import csg.taTab.TAData;
import csg.taTab.TeachingAssistant;
import jtps.jTPS_Transaction;

public class UpdateTA implements jTPS_Transaction{
    
    boolean oldUndergrad, newUndergrad;
    private String oldName, newName, oldEmail, newEmail;
    private TeachingAssistant oldTA, newTA;
    
    private CourseSiteGenerator csg;
    private TAData data;
    private TAController control;
    
    public UpdateTA(CourseSiteGenerator csg, TeachingAssistant oldTA, boolean newUndergrad, String newName, String newEmail){
        this.csg = csg;
        data = csg.getDataComponent().getTaData();
        control = csg.getWorkspaceComponent().getTAWorkspace().getController();
        
        this.oldTA = oldTA;
        this.oldUndergrad = oldTA.isUndergrad();
        this.oldName = oldTA.getName();
        this.oldEmail = oldTA.getEmail();
        
        this.newUndergrad = newUndergrad;
        this.newName = newName;
        this.newEmail = newEmail;
    }

    @Override
    public void doTransaction() {
        data.replaceTAName(oldName, newName);
        data.removeTA(oldName);
        newTA = new TeachingAssistant(newUndergrad, newName, newEmail);
        data.addTA(newTA);
        csg.getDataComponent().getRecitationData().updateRecitations(oldTA, newTA);
        csg.getDataComponent().getRecitationData().resetCombos();
        csg.getWorkspaceComponent().getRecitaitonSpace().refreshTable();
    }

    @Override
    public void undoTransaction() {
        data.replaceTAName(newName, oldName);
        data.removeTA(newName);
        data.addTA(oldUndergrad, oldName, oldEmail);
        csg.getDataComponent().getRecitationData().updateRecitations(newTA, oldTA);
        csg.getDataComponent().getRecitationData().resetCombos();
        csg.getWorkspaceComponent().getRecitaitonSpace().refreshTable();
    }

}
