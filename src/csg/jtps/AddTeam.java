/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.projectTab.ProjectData;
import csg.projectTab.Team;
import jtps.jTPS_Transaction;

public class AddTeam implements jTPS_Transaction{
    
    ProjectData data;
    Team team;

    public AddTeam(ProjectData data, Team team){
        this.data = data;
        this.team = team;
    }
    
    @Override
    public void doTransaction() {
        data.addTeam(team);
    }

    @Override
    public void undoTransaction() {
        data.removeTeam(team);
    }

}
