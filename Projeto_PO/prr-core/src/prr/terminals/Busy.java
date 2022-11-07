package prr.terminals;

import java.io.Serializable;

public class Busy extends State implements Serializable{
	public Busy() {
		super.setReceiveInteractiveComs(false);
		super.setReceiveTextComs(true);
		super.setSendInteractiveComs(false);
		super.setSendTextComs(false);
		super.setType("BUSY");
	}
}
