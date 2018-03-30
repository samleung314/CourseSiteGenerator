/**
 * @author Samson Leung 110490519
 */
package csg.jtps;

import csg.recitationTab.Recitation;
import csg.recitationTab.RecitationData;
import jtps.jTPS_Transaction;

public class DeleteRecitation implements jTPS_Transaction{

    RecitationData data;
    Recitation recitation;
    int id;
    
    public DeleteRecitation(RecitationData data, Recitation recitation){
        this.data = data;
        this.recitation = recitation;
    }
    
    @Override
    public void doTransaction() {
        data.removeRecitation(recitation);
    }

    @Override
    public void undoTransaction() {
        data.addRecitation(recitation);
    }

}
