package prr.notifications;

import prr.Network;
import prr.notifications.Notification;
import java.io.Serializable;


public class RealNotification extends Notification{
	
	private String _deliveryMethod;

	public RealNotification(String message,String terminalID,
			 String deliveryMethod){
		super(message,terminalID);
		_deliveryMethod=deliveryMethod;
	}

	public RealNotification(String message,String terminalID){
		super(message,terminalID);
		_deliveryMethod="DEFAULT";
	}

	@Override 
	public String toString(){
		if (_deliveryMethod.equals("DEFAULT"))
			return super.toString();
		return super.toString()+"|"+_deliveryMethod;
	}
}
