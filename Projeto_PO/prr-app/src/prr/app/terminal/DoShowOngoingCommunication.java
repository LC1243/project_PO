package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import prr.exceptions.NoOnGoingCommunicationException;
//FIXME add more imports if needed

/**
 * Command for showing the ongoing communication.
 */
class DoShowOngoingCommunication extends TerminalCommand {

	DoShowOngoingCommunication(Network context, Terminal terminal) {
		super(Label.SHOW_ONGOING_COMMUNICATION, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
                try{ 
			_display.popup(_receiver.showOnGoingCommunication());
		}catch (NoOnGoingCommunicationException e){
			_display.popup(Message.noOngoingCommunication());
		}
		//FIXME implement command
	}
}
