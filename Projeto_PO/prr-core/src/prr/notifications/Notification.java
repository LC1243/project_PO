package prr.notifications;
import prr.Network;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public abstract class Notification implements Serializable {
	private String _message;
	private String _terminalID;
	private boolean _completed;
	public Notification(String message,String terminalID){
		_message=message;
		_terminalID=terminalID;
	}
	public void Completed(){
		_completed=true;
	}

	public String getMessage(){
		return _message;
	}

	public void setMessage(String message){
		_message=message;
	}

	public void setTerminalID(String terminalID){
		_terminalID=terminalID;
	}
	
	public String getTerminalID(){
		return _terminalID;
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof Notification){
			Notification notification= (Notification) o;
		return _message.equals(notification.getMessage()) &&
			_terminalID.equals(notification.getTerminalID());
		}
		return false;
	}

	@Override
	public String toString(){
		return _message+"|"+_terminalID;
	}
}
