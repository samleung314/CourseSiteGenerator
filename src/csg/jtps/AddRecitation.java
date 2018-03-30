/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.recitationTab.Recitation;
import csg.recitationTab.RecitationData;
import jtps.jTPS_Transaction;

public class AddRecitation implements jTPS_Transaction{
    
    RecitationData data;
    Recitation recitation;
    int id;
    
    public AddRecitation(RecitationData data, Recitation recitation){ 
        this.data = data;
        this.recitation = recitation;
    }

    @Override
    public void doTransaction() {
        data.addRecitation(recitation);
    }

    @Override
    public void undoTransaction() {
        data.removeRecitation(recitation);
    }

}
