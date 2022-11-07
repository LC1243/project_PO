package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for ending communication.
 */
class DoEndInteractiveCommunication extends TerminalCommand {

	DoEndInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.END_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canEndCurrentCommunication());
	addIntegerField("time",Prompt.duration());
	}

	@Override
	protected final void execute() throws CommandException {
                 _receiver.endInteractiveCommunication(integerField("time"),
					 _network);
				_display.popup(Message.communicationCost
				(_receiver.getLastCommunicationPrice()));
		//FIXME implement command
	}
}
