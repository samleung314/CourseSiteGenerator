/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.projectTab.ProjectData;
import csg.projectTab.Student;
import csg.projectTab.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jtps.jTPS_Transaction;

public class DeleteTeam implements jTPS_Transaction{
    ProjectData data;
    Team team;
    ObservableList<Student> students;
    
    public DeleteTeam(ProjectData data, Team team){
        this.data = data;
        this.team = team;
        this.students = FXCollections.observableArrayList();
    }
    
    @Override
    public void doTransaction() {
        for(Student s : data.getStudentList()){
            if(s.getTeam().equals(team)){
                students.add(s);
            }
        }
        data.removeTeam(team);
    }

    @Override
    public void undoTransaction() {
        data.addTeam(team);
        data.getStudentList().addAll(students);
        students.clear();
    }

}
