/**
 * @author Samson Leung 110490519
 */
package csg.courseDetails;

import java.io.File;

public class SitePage {
    
    private String navBar, fileName, script;
    private boolean use;
    private File file;
    
    public SitePage(boolean use, String navBar, String fileName, String script, File file){
        this.file = file;
        this.navBar = navBar;
        this.fileName = fileName;
        this.script = script;
        this.use = use;
    }

    public String getNavBar() {
        return navBar;
    }

    public String getFileName() {
        return fileName;
    }

    public String getScript() {
        return script;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public File getFile() {
        return file;
    }
    
}
