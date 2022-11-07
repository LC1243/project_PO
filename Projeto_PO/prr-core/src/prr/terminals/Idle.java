package prr.terminals;

import java.io.Serializable;

public class Idle extends State implements Serializable {
	public Idle() {
		super.setReceiveInteractiveComs(true);
		super.setReceiveTextComs(true);
		super.setSendInteractiveComs(true);
		super.setSendTextComs(true);
		super.setType("IDLE");
	}
}
