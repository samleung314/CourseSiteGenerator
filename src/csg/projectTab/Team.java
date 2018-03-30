/**
 * @author Samson Leung 110490519
 */
package csg.projectTab;

public class Team {
    
    private String name, color, textColor, link;
    private String red, green ,blue;
    
    public Team(String name, String color, String textColor, String link){
        this.name = name;
        this.color = color;
        this.textColor = textColor;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRed() {
        int red = Integer.valueOf(color.substring( 1, 3 ), 16 );
        return Integer.toString(red);
    }

    public String getGreen() {
        int green = Integer.valueOf(color.substring( 3, 5 ), 16 );
        return Integer.toString(green);
    }

    public String getBlue() {
        int blue = Integer.valueOf(color.substring( 5, 7 ), 16 );
        return Integer.toString(blue);
    }
    
}
