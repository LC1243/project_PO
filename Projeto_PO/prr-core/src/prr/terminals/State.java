package prr.terminals;

import java.io.Serializable;

public class State implements Serializable {
	private String _name="IDLE";
	private String notificationType;
	private boolean r_InteractiveComs=true;
	private boolean r_TextComs=true;
	private boolean s_InteractiveComs=true;
 	private boolean s_TextComs=true;
								
								
	public boolean canReceiveInteractiveComs() {
		return r_InteractiveComs;
	}
	public boolean canReceiveTextComs() {
		return r_TextComs;
	}
	public boolean canSendInteractiveComs() {
		return s_InteractiveComs;
	}
	public boolean canSendTextComs() {
		return s_TextComs;
	}
		
	public void setReceiveInteractiveComs(boolean logic) {
		r_InteractiveComs=logic;
	}
	public void setReceiveTextComs(boolean logic) {
		r_TextComs=logic;
	}
	public void setSendInteractiveComs(boolean logic) {
		s_InteractiveComs=logic;
	}
	public void setSendTextComs(boolean logic) {
		s_TextComs=logic;
	}
		
	public String getType() {
		return _name;
	}
	public void setType(String name) {
		_name=name;
	}
	public boolean canTurnIdle() {
		return true;
	}
		
	public boolean canTurnOff() {
		if(_name.equals("IDLE")==true|| _name.equals("SILENCE")==true) {
			return true;
		}
		return false;
									}
		
	public boolean canTurnSilence(){
		return true;
	}
	public boolean canTurnBusy() {
		//FIXME 
		return true;
	}
	public void activateIdleMode() {
	}
	public void activateSilenceMode() {
	}
	public void activateOffMode() {
	}
	public void activateBusyMode() {
	}
}

