/**
 * @author Samson Leung 110490519
 */
package csg.scheduleTab;

public class ScheduleItem <E extends Comparable<E>> implements Comparable<E>{

    String type, date, time, title, topic, link, criteria;
    String month, day;
    
    public ScheduleItem(String type, String date, String title){
        this.type = type;
        this.date = date;
        this.title = title;
        
        String times[] = date.split("/");
        this.month = times[0];
        this.day = times[1];
    }
    
    @Override
    public int compareTo(E otherItem) {
        return getDate().compareTo(((ScheduleItem)otherItem).getDate());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }
    
    
}
