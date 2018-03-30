/**
 * @author Samson Leung 110490519
 */
package csg.projectTab;

public class Student {
    
    private String firstName, lastName, role;
    private Team team;
    
    public Student(String firstName, String lastName, Team team, String role){
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeamName() {
        return team.getName();
    }
    
}
