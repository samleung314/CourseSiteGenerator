/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.projectTab.ProjectData;
import csg.projectTab.ProjectWorkspace;
import csg.projectTab.Team;
import jtps.jTPS_Transaction;

public class UpdateTeam implements jTPS_Transaction{

    ProjectWorkspace space;
    ProjectData data;
    Team team;
    String newName, newColor, newTextColor, newLink;
    String name, color, textColor, link;
    
    public UpdateTeam(ProjectWorkspace space, ProjectData data, Team team, String newName, String newColor, String newTextColor, String newLink){
        this.space = space;
        this.data = data;
        this.team = team;
        
        this.newName = newName;
        this.newColor = newColor;
        this.newTextColor = newTextColor;
        this.newLink = newLink;
        
        // Old Data
        this.name = team.getName();
        this.color = team.getColor();
        this.textColor = team.getTextColor();
        this.link = team.getLink();
    }
    
    @Override
    public void doTransaction() {
        team.setName(newName);
        team.setColor(newColor);
        team.setTextColor(newTextColor);
        team.setLink(newLink);
        space.updateTables();
    }

    @Override
    public void undoTransaction() {
        team.setName(name);
        team.setColor(color);
        team.setTextColor(textColor);
        team.setLink(link);
        space.updateTables();
    }

}
