/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.projectTab.ProjectData;
import csg.projectTab.Student;
import jtps.jTPS_Transaction;

public class AddStudent implements jTPS_Transaction{

    ProjectData data;
    Student student;
    
    public AddStudent(ProjectData data, Student s){
        this.data = data;
        this.student = s;
    }
    
    @Override
    public void doTransaction() {
        data.addStudent(student);
    }

    @Override
    public void undoTransaction() {
        data.removeStudent(student);
    }

}
