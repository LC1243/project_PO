package prr.terminals;

import java.io.Serializable;

public class Silence extends State implements Serializable{
	public Silence() {
		super.setReceiveInteractiveComs(false);
		super.setReceiveTextComs(true);
		super.setSendInteractiveComs(true);
		super.setSendTextComs(true);
		super.setType("SILENCE");
	}
}
