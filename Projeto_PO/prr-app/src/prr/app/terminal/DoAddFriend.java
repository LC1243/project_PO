package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
//FIXME add more imports if needed

/**
 * Add a friend.
 */
class DoAddFriend extends TerminalCommand {

	DoAddFriend(Network context, Terminal terminal) {
		super(Label.ADD_FRIEND, context, terminal);
		addStringField("terminalFriendID",Prompt.terminalKey());
		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException{
		try{
                	_receiver.addFriend(stringField("terminalFriendID")
					,_network);
	} catch (prr.exceptions.UnknownTerminalKeyException e){
		throw new UnknownTerminalKeyException(e.getKey());
	} catch (prr.exceptions.InvalidTerminalKeyException e) {
		throw new InvalidTerminalKeyException(e.getKey());
	}
		//FIXME implement command
	}
}
