package prr.terminals;

import java.io.Serializable;

public class Off extends State implements Serializable {
	public Off () {
		super.setReceiveInteractiveComs(false);
		super.setReceiveTextComs(false);
		super.setSendInteractiveComs(false);
		super.setSendTextComs(false);
		super.setType("OFF");
	}
}
