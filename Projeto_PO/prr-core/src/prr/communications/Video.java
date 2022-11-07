package prr.communications;

import prr.communications.Communication;
import java.io.Serializable;

public class Video extends Communication implements Serializable {
	
    public Video(String ReceiverId,String SenderId, int id){
	    super(ReceiverId,SenderId,id);
        setName("VIDEO");
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
	if(state==false)
        	return "VIDEO" + super.toString() + (int)super.getDuration() + "|" +
        	(int)this.getPrice() + "|FINISHED";
	else 
		return "VIDEO" + super.toString() + (int)super.getDuration() + "|" +
		(int)this.getPrice() + "|ONGOING";
    }
}
