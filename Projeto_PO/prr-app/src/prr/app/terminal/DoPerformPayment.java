package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
// Add more imports if needed

/**
 * Perform payment.
 */
class DoPerformPayment extends TerminalCommand {

	DoPerformPayment(Network context, Terminal terminal) {
		super(Label.PERFORM_PAYMENT, context, terminal);
		addIntegerField("commKey", Prompt.commKey());

		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
                //FIXME implement command
		try {
			_receiver.performPayment(integerField("commKey"), _network);
		} catch(prr.exceptions.InvalidCommunicationException e) {
			_display.popup(Message.invalidCommunication());
		}
	}
}
