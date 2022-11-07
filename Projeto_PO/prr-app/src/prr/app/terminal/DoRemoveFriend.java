package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownTerminalKeyException;
//FIXME add more imports if needed

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {

	DoRemoveFriend(Network context, Terminal terminal) {
		super(Label.REMOVE_FRIEND, context, terminal);
		addStringField("terminalUnfriendID",Prompt.terminalKey());
		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_receiver.removeFriend(
					stringField("terminalUnfriendID"),
					_network);
		}catch (prr.exceptions.InvalidTerminalKeyException e){
			throw new InvalidTerminalKeyException(e.getKey());
		}catch (prr.exceptions.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(e.getKey());
		}
                //FIXME implement command
	}
}
