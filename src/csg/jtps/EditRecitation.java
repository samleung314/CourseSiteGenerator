/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.recitationTab.Recitation;
import csg.recitationTab.RecitationData;
import csg.recitationTab.RecitationWorkspace;
import csg.taTab.TeachingAssistant;
import jtps.jTPS_Transaction;

public class EditRecitation implements jTPS_Transaction {

    RecitationData data;
    Recitation oldRec;
    String newSection, newInstruct, newTime, newLocation;
    String oldSection, oldInstruct, oldTime, oldLocation; 
    
    TeachingAssistant newFirstTA, newSecondTA, oldFirstTA, oldSecondTA;
    
    RecitationWorkspace space;

    public EditRecitation(RecitationWorkspace space, RecitationData data, Recitation oldRec, String newSection, String newInstruct, String newTime, String newLocation, TeachingAssistant newFirstTA, TeachingAssistant newSecondTA) {
        this.data = data;
        this.oldRec = oldRec;
        this.space = space;
        
        this.newSection = newSection;
        this.newInstruct = newInstruct;
        this.newTime = newTime;
        this.newLocation = newLocation;
        this.newFirstTA = newFirstTA;
        this.newSecondTA = newSecondTA;
        
        this.oldSection = oldRec.getSection();
        this.oldInstruct = oldRec.getInstructor();
        this.oldTime = oldRec.getTime();
        this.oldLocation = oldRec.getLocation();
        this.oldFirstTA = oldRec.getA();
        this.oldSecondTA = oldRec.getB();
    }

    @Override
    public void doTransaction() {
        oldRec.setSection(newSection);
        oldRec.setInstructor(newInstruct);
        oldRec.setDayTime(newTime);
        oldRec.setLocation(newLocation);
        oldRec.setA(newFirstTA);
        oldRec.setB(newSecondTA);
        space.updateTable();
    }

    @Override
    public void undoTransaction() {
        oldRec.setSection(oldSection);
        oldRec.setInstructor(oldInstruct);
        oldRec.setDayTime(oldTime);
        oldRec.setLocation(oldLocation);
        oldRec.setA(oldFirstTA);
        oldRec.setB(oldSecondTA);
        space.updateTable();
    }

}
