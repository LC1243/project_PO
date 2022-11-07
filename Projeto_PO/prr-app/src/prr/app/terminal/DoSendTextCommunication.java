package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
import prr.exceptions.TerminalAlreadyOffException;
//FIXME add more imports if needed

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

        DoSendTextCommunication(Network context, Terminal terminal) {
               	super(Label.SEND_TEXT_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
        	addStringField("terminalToSendID",Prompt.terminalKey());
			addStringField("MessageToSend",Prompt.textMessage());

	}	

        @Override
        protected final void execute() throws CommandException {
		try{_receiver.sendText(stringField("terminalToSendID"),
					stringField("MessageToSend"),
					_network);
		}catch(TerminalAlreadyOffException e){
			_display.popup(Message.destinationIsOff(
					stringField("terminalToSendID")));
		}catch (prr.exceptions.UnknownTerminalKeyException e){
			throw new UnknownTerminalKeyException(e.getKey());
		}
		//FIXME implement command
        }
} 
