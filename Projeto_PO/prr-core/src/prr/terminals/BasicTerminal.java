package prr.terminals;

import prr.Network;
import prr.communications.Communication;
import prr.communications.Voice;
import java.io.Serializable;
import prr.terminals.Terminal;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadyBusyException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.exceptions.TerminalUnsupportedAtOriginException;
import prr.exceptions.TerminalUnsupportedAtDestinationException;
import prr.exceptions.UnknownTerminalKeyException;

public class BasicTerminal extends Terminal implements Serializable {
			
	public BasicTerminal(String id_terminal, String id_Client,String name){
		setIDTerminal(id_terminal);
		setIDClient(id_Client);
		setTerminalType("BASIC");
		State state= super.getState();
		switch(name) {
			case "ON": super.setState(new Idle());break;
			case "OFF": super.setState(new Off());break;
			case "SILENCE": super.setState(new Silence());break;
		}
	}


	public String toString() {
		int payments=(int) getPayments();
		int debts=(int) getDebts();
		String rendered="|";
		if(super.getFriends().size()==0) {
			return "BASIC|"+this.getIDTerminal()+"|"+this.getIDClient()
				+"|"+this.getState().getType()+"|"+payments+"|"+debts;
		}
		for(Terminal t:this.getFriends()) {
			rendered+= t.getIDTerminal()+",";
		}
		return "BASIC|"+this.getIDTerminal()+"|"+this.getIDClient()
			+"|"+this.getState().getType()+"|"+payments+"|"+
			debts+rendered.substring(0, rendered.length() - 1);
		}
}
