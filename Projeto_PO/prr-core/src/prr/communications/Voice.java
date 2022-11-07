package prr.communications;

import java.io.Serializable;
import prr.communications.Communication;

public class Voice extends Communication implements Serializable {
    
    public Voice(String IdReceiver,String IdSender,int _id){
	    super(IdReceiver,IdSender,_id);
        setName("VOICE");
    }

    public boolean communicationEnded() {
        return false;
    }

    public boolean communicationFailed() {
        return true;
    }

    @Override
    public String toString() {
	boolean state = getCommunicationState();
	if(state == false)
    		return "VOICE" + super.toString() + (int)super.getDuration() + "|" +
        	(int)this.getPrice() + "|FINISHED";
	else
		return "VOICE" + super.toString() + (int)super.getDuration() + "|" +
		(int)this.getPrice() + "|ONGOING";
    }
 
}
