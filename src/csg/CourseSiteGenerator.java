/**
 * @author Samson Leung 110490519
 */
package csg;

import csg.style.CSGStyle;
import djf.AppTemplate;
import java.util.Locale;

public class CourseSiteGenerator extends AppTemplate{

    public void buildAppComponentsHook() {
        fileComponent = new CSGFiles(this);
        dataComponent = new CSGData(this);
        workspaceComponent = new CSGWorkspace(this);
        styleComponent = new CSGStyle(this);
    }
    
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }
    
    @Override
    public CSGData getDataComponent(){
        return (CSGData) dataComponent;
    }
    
    @Override
    public CSGWorkspace getWorkspaceComponent(){
        return (CSGWorkspace) workspaceComponent;
    }

}
