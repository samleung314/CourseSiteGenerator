/**
 * @author Samson Leung 110490519
 */
package csg.recitationTab;

import csg.taTab.TeachingAssistant;

public class Recitation {

    private TeachingAssistant a, b;
    private String section, instructor, time, location;
    private String firstTA, secondTA;
    private int id;
    private static int idCounter = -1;

    public Recitation(String section, String instructor, String time, 
            String location, TeachingAssistant a, TeachingAssistant b){
        
        this.section = section;
        this.instructor = instructor;
        this.time = time;
        this.location = location;
        this.a = a;
        this.b = b;
        this.id = ++idCounter;
        
        if(a != null)
            this.firstTA = a.getName();
        
        if(b != null)
            this.secondTA = b.getName();
    }
    
    public void replaceTA(TeachingAssistant oldTA, TeachingAssistant newTA){
        if(oldTA.equals(a)){
            this.a = newTA;
            this.firstTA = a.getName();
        }
        
        if(oldTA.equals(b)){
            this.b = newTA;
            this.secondTA = b.getName();
        }
    }
    
    public void removeTA(TeachingAssistant ta){
        if(ta.equals(a)){
            this.a = null;
            this.firstTA = "";
        }
        
        if(ta.equals(b)){
            this.b = null;
            this.secondTA = "";
        }
    }
    
    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getTime() {
        return time;
    }

    public void setDayTime(String dayTime) {
        this.time = dayTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFirstTA() {
        return firstTA;
    }

    public void setFirstTA(String firstTA) {
        this.firstTA = firstTA;
    }

    public String getSecondTA() {
        return secondTA;
    }

    public void setSecondTA(String secondTA) {
        this.secondTA = secondTA;
    }

    public int getId() {
        return id;
    }

    public TeachingAssistant getA() {
        return a;
    }

    public TeachingAssistant getB() {
        return b;
    }

    public void setA(TeachingAssistant a) {
        this.a = a;
        this.firstTA = a.getName();
    }

    public void setB(TeachingAssistant b) {
        this.b = b;
        this.secondTA = b.getName();
    }
    
}
