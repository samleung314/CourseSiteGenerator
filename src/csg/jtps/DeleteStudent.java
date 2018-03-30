/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.projectTab.ProjectData;
import csg.projectTab.Student;
import jtps.jTPS_Transaction;

public class DeleteStudent implements jTPS_Transaction{

    ProjectData data;
    Student student;
    
    public DeleteStudent(ProjectData data, Student student){
        this.data = data;
        this.student = student;
    }
    
    @Override
    public void doTransaction() {
        data.removeStudent(student);
    }

    @Override
    public void undoTransaction() {
        data.addStudent(student);
    }
}
