package prr.communications;

import java.io.Serializable;
import prr.clients.Client;
import prr.terminals.Terminal;

public abstract class Communication implements Serializable {

    private int _id;
	
    private boolean _paid=false;

    private String _terminalIDReceiver;

    private String  _terminalIDSender;

    private boolean _communicationState;

    private long _price;

    private String _name;

    private double _time;

    public Communication(String idReceiver,String idSender,int id){
	    _id = id;
	    _terminalIDReceiver = idReceiver;
	    _terminalIDSender = idSender;
	    _communicationState = true;
    }

    public String getReciever() {
        return _terminalIDReceiver;
    }

    public String getSender() {
        return _terminalIDSender;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getCommunicationID(){
	    return _id;
    }

    public double getDuration() {
        return _time;
    }

    public void setDuration(double time) {
        _time = time;
    }

    public boolean getCommunicationState() {
        return _communicationState;
    }

    public void setCommunicationState(boolean state) {
        _communicationState = state;
    }
    

    public long getPrice() {
        return _price;
    }

    public void setPrice(long price) {
        _price = price;
    }

    public boolean checkIfPaid() {
        return _paid;
    } 

    public void setCommPaymentState(boolean state) {
        _paid = state;
    }

    public void changeCommState() {}

    public boolean canMakeCommunication() {
        return false;
    }

    public void warnRecieverTerminal() {}

    @Override
    public String toString() {
        return "|" + _id + "|" + _terminalIDSender + "|" +
        _terminalIDReceiver + "|";
    }
}
