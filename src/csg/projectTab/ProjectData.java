/**
 * @author Samson Leung 110490519
 */
package csg.projectTab;

import djf.components.AppDataComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProjectData implements AppDataComponent {

    ObservableList<Team> teamList;
    ObservableList<Student> studentList;
    
    public ProjectData(){
        teamList = FXCollections.observableArrayList();
        studentList = FXCollections.observableArrayList();
    }

    public ObservableList<Team> getTeamList() {
        return teamList;
    }
    
    public ObservableList<String> getTeamNames() {
        ObservableList<String> teamNames = FXCollections.observableArrayList();
        for(Team t : teamList){
            teamNames.add(t.getName());
        }
        return teamNames;
    }

    public ObservableList<Student> getStudentList() {
        return studentList;
    }
    
    public void addTeam(Team team){
        teamList.add(team);
    }
    
    public void addStudent(Student student){
        studentList.add(student);
    }
    
    public void removeTeam(Team team){
        for(int i = studentList.size(); i>0; i--){
            if(studentList.get(i-1).getTeam().equals(team)){
                removeStudent(studentList.get(i-1));
            }
        }
        teamList.remove(team);
    }
    
    public void removeStudent(Student student){
        studentList.remove(student);
    }
    
    public Team getTeam(String name){
        for(Team t : teamList){
            if(t.getName().equals(name)){
                return t;
            }
        }
        return null;
    }
    
    @Override
    public void resetData() {
        teamList.clear();
        studentList.clear();
    }

}
