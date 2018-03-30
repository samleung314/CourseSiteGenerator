/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.projectTab.ProjectData;
import csg.projectTab.ProjectWorkspace;
import csg.projectTab.Student;
import csg.projectTab.Team;
import jtps.jTPS_Transaction;

public class UpdateStudent implements jTPS_Transaction{

    ProjectWorkspace space;
    ProjectData data;
    
    Student student;
    String newFirstName, newLastName, newRole;
    Team newTeam;
    
    String firstName, lastName, role;
    Team team;
    
    public UpdateStudent(ProjectWorkspace aThis, ProjectData data, Student s, String first, String last, Team team, String role) {
        this.space = aThis;
        this.data = data;
        
        this.student = s;
        
        this.newFirstName = first;
        this.newLastName = last;
        this.newRole = role;
        this.newTeam = team;
        
        this.firstName = s.getFirstName();
        this.lastName = s.getLastName();
        this.role = s.getRole();
        this.team = s.getTeam();
    }
    
    @Override
    public void doTransaction() {
        student.setFirstName(newFirstName);
        student.setLastName(newLastName);
        student.setRole(newRole);
        student.setTeam(newTeam);
        space.updateTables();
    }

    @Override
    public void undoTransaction() {
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setRole(role);
        student.setTeam(team);
        space.updateTables();
    }
}
