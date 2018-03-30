/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.taTab.TAData;
import jtps.jTPS_Transaction;

public class ToggleTA implements jTPS_Transaction{

    private String TAname;
    private String cellKey;
    private TAData data;
    
    public ToggleTA(String taName, String cellKey, TAData data) {
        this.TAname = taName;
        this.cellKey = cellKey;
        this.data = data;
    }

    @Override
    public void doTransaction() {
        data.toggleTAOfficeHours(cellKey, TAname);
    }

    @Override
    public void undoTransaction() {
        data.toggleTAOfficeHours(cellKey, TAname);
    }

}
