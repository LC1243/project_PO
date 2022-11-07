package prr.communications;

import prr.communications.Communication;
import java.io.Serializable;

public class Text extends Communication implements Serializable {

    private String _message;

    private double _length;
	
    public Text (String idReceiver,String idSender,int id,String message){
	    super(idReceiver,idSender,id);
	    _message = message;
        //a text is instantly finished
        super.setCommunicationState(false);
        super.setName("TEXT");
        super.setDuration(0);
    }

    public double countCharacters() {
    	_length= (double) _message.length();
        return _length;
    }
	
    public boolean communicationFailed() {
        return true;
    }

    @Override
    public String toString() {
        return "TEXT" + super.toString() + (int)_length + "|" +
        (int)this.getPrice() + "|" + "FINISHED";
    }
}
