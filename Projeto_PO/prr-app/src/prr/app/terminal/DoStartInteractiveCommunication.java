package prr.app.terminal;

import prr.Network;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadyBusyException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.exceptions.TerminalUnsupportedAtOriginException;
import prr.exceptions.TerminalUnsupportedAtDestinationException;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

	DoStartInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
		addStringField("terminalReceiverKey",Prompt.terminalKey());
		addOptionField("type",Prompt.commType(),"VOICE", "VIDEO");
	}

	@Override
	protected final void execute() throws CommandException {
	       try{ _receiver.startInteractiveCommunication(
			       optionField("type"),stringField(
				"terminalReceiverKey"),_network);
	        }catch( prr.exceptions.UnknownTerminalKeyException e){
		       throw new UnknownTerminalKeyException(e.getKey());
	        }catch (TerminalUnsupportedAtOriginException e){
		       _display.popup(Message.unsupportedAtOrigin(
					       _receiver.getIDTerminal()
					       ,optionField("type")));
		}catch(TerminalUnsupportedAtDestinationException e){
			_display.popup(Message.unsupportedAtDestination(
						stringField("terminalReceiverKey")
						,optionField("type")));
		}catch(TerminalAlreadyOffException e){
			_display.popup(Message.destinationIsOff(
					stringField("terminalReceiverKey")));
		}catch(TerminalAlreadyBusyException e){
			_display.popup(Message.destinationIsBusy(
					stringField("terminalReceiverKey")));
		}catch(TerminalAlreadySilentException e){
			_display.popup(Message.destinationIsSilent(
					stringField("terminalReceiverKey")));
		}	
	       	//FIXME implement command
	}
}
