/**
 * @author Samson Leung 110490519
 */
package csg.recitationTab;

import csg.CourseSiteGenerator;
import csg.taTab.TeachingAssistant;
import djf.components.AppDataComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecitationData implements AppDataComponent {

    ObservableList<Recitation> recitations;
    CourseSiteGenerator csg;
    
    public RecitationData(CourseSiteGenerator csg){
        this.recitations = FXCollections.observableArrayList();
        this.csg = csg;
    }
    
    public void addRecitation(Recitation r){
        recitations.add(r);
    }
    
    public void addRecitation(String section, String instructor, String time, String location, TeachingAssistant firstTa, TeachingAssistant secondTa){
        recitations.add(new Recitation(section, instructor, time, location, firstTa, secondTa));
    }
    
    public void removeRecitation(Recitation r){
        for(Recitation rec : recitations){
            if(rec.getId() == r.getId()){
                recitations.remove(rec);
                return;
            }
        }
    }

    public ObservableList<Recitation> getRecitations() {
        return recitations;
    }
    
    public void updateRecitations(TeachingAssistant oldTA, TeachingAssistant newTA){
        for(Recitation r : recitations){
            r.replaceTA(oldTA, newTA);
        }
    }
    
    public void removeTA(TeachingAssistant ta){
        for(Recitation r : recitations){
            r.removeTA(ta);
        }
    }
    
    @Override
    public void resetData() {
        recitations.clear();
        csg.getWorkspaceComponent().getRecitaitonSpace().resetCombos();
        resetCombos();
    }
    
    public void resetCombos(){
        csg.getWorkspaceComponent().getRecitaitonSpace().resetCombos();
    }

}
