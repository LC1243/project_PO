package prr.terminals;
import prr.Network;
import prr.clients.Client;
import prr.clients.Status;
import prr.clients.PricingPlan;
import prr.communications.Communication;
import prr.communications.Text;
import prr.communications.Voice;
import prr.communications.Video;
import prr.notifications.Notification;
import prr.notifications.RealNotification;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import prr.exceptions.NoOnGoingCommunicationException;
import prr.exceptions.TerminalUnsupportedAtOriginException;
import prr.exceptions.TerminalUnsupportedAtDestinationException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.exceptions.TerminalAlreadyIdleException;
import prr.exceptions.TerminalAlreadyBusyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.InvalidCommunicationException;
// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable /* FIXME maybe addd more interfaces */{
	private String terminalType;
	private int number_communications=0;
	private String _id;
	private double _payments=0;
	private double _saldo;
	private double _debts=0;
	private String _idClient;
	private State _state= new Idle();
	private State _pastState= _state;
	private Map<String,Terminal> _friends= new TreeMap<String,Terminal>();
	private Map<String,Notification> _clientsToNotify= new TreeMap<String
		,Notification>();
	private Map <String,Notification> _clientsToNotify2= new TreeMap<String
		,Notification>();
	private Map<Integer,Communication> _communications= new TreeMap<Integer,Communication>();
	private Communication onGoingCom;
	private long _lastCommunicationPrice = 0;
	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

        // FIXME define attributes
        // FIXME define contructor(s)
        // FIXME define methods

        /**
         * Checks if this terminal can end the current interactive communication.
         *
         * @return true if this terminal is busy (i.e., it has an active interactive communication) and
         *          it was the originator of this communication.
         **/
        public boolean canEndCurrentCommunication() {
          	if(this.isAlreadyBusy() &&
			onGoingCom.getSender().equals(this.getIDTerminal())) {
				return true;
			}
			return false; 
		 // FIXME add implementation code
        }

        /**
         * Checks if this terminal can start a new communication.
         *
         * @return true if this terminal is neither off neither busy, false otherwise.
         **/
        public boolean canStartCommunication() {
            if(this.isAlreadyBusy() || this.isAlreadyOff())
				return false;
			return true; 
		 // FIXME add implementation code
        }

	/**
	* Getters and Setters
	*
	**/

	public void setTerminalType(String type){
		terminalType=type;
	}

	public String getTerminalType(){
		return terminalType;
	}

	public void setIDTerminal(String name) {
		_id=name;
	}
	public String getIDTerminal() {
		return _id;
	}
	public void setIDClient(String name) {
		_idClient=name;
	}
	public String getIDClient() {
		return _idClient;
	}
	public void setPayments(double value) {
		_payments=value;
	}
	public double getPayments(){
		return _payments;
	}
	public long getSaldo() {
		_saldo = (long)getPayments() - (long)getDebts();
		return (long)_saldo;
	}
	public void setDebts(double value){
		_debts=value;
	}
	public double getDebts() {
		return _debts;
	}

	public State getState(){
		return _state;
	}

	public void setState(State state) {
		_state = state;
	}

	public void setlastCommunicationPrice(long price) {
		_lastCommunicationPrice = price;

	} 

	public long getLastCommunicationPrice() {
		return _lastCommunicationPrice;
	}

	public int getNumberCommunications(){
		return number_communications;
	}

	public Communication getOnGoingCommunication(){
		return onGoingCom;	
	}

	public void setOnGoingCommunication(Communication communication){
		onGoingCom = communication;
	}

	public void setPastState(State state){
		_pastState=state;
	}

	public State getPastState(){
		return _pastState;
	}


	/** 
	* Simple Operations, like increase or decrease atributtes
	*
	**/

	public void increaseNumberCommunications() {
		number_communications += 1;
	}

	public void increasePayments(double value) {
		_payments += value;
	}

	public void decreasePayments(double value) {
		_payments -= value;
	}
	
	public void increaseDebt(double value) {
		_debts += value;
	}	
	
	public void decreaseDebt(double value) {
		_debts -= value;
	}

	/**
	*
	* Operations related to Communications
	*
	**/

	public void addCommunication(Communication communication){
		_communications.put(communication.getCommunicationID(),
				communication);
	}

	public void removeCommunication(Communication communicationToRemove){
		_communications.remove(communicationToRemove.getCommunicationID());
	}

	public Communication getCommunication(int commKey) {
		return _communications.get(commKey);
	} 

	public ArrayList<Communication> getCommunicationsFromTerminal() {
		ArrayList<Communication> communications = 
			new ArrayList<Communication>(_communications.values());
		ArrayList<Communication> communicationsFrom = 
			new ArrayList<Communication>();

		for(Communication c : communications) {
			if((c.getSender().equals(this.getIDTerminal())))
				communicationsFrom.add(c);
		}
		return communicationsFrom;
	}

	public ArrayList<Communication> getCommunicationsToTerminal() {
		ArrayList<Communication> communications = 
			new ArrayList<Communication>(_communications.values());
		ArrayList<Communication> communicationsTo = 
			new ArrayList<Communication>();

		for(Communication c : communications) {
			if((c.getReciever().equals(this.getIDTerminal())))
				communicationsTo.add(c);
		}
		return communicationsTo;
	}

	public String showOnGoingCommunication()
			throws NoOnGoingCommunicationException{
		if(onGoingCom==null)
			throw new NoOnGoingCommunicationException();
		else 
			return onGoingCom.toString();

	}

	/**
	*
	* Operations related to Friends
	*
	**/

	public List<Terminal> getFriends(){
		ArrayList<Terminal> lista= new ArrayList<Terminal>();
		lista.addAll(_friends.values());
		return lista;
	}
	

	public boolean isFriend(String terminalKey) {
		if(_friends.get(terminalKey) == null)
			return false;
		return true;
	} 


	public void addFriend(Terminal terminalToAdd){
		_friends.put(terminalToAdd.getIDTerminal(),terminalToAdd);
	}

	public void addFriend(String terminalID_friend, Network _network) throws 
		UnknownTerminalKeyException, InvalidTerminalKeyException{

			if(_network.getTerminal(terminalID_friend)==null)
				throw new UnknownTerminalKeyException 
					(terminalID_friend);

			if(terminalID_friend.length() != 6 || 
					terminalID_friend.matches("[0-9]+") 
					!= true ) 
				throw new InvalidTerminalKeyException
					(terminalID_friend);
			if(this.getIDTerminal().equals(terminalID_friend))
				return ;
			if(_friends.get(terminalID_friend)==null || 
			!terminalID_friend.equals(this.getIDTerminal())) {
				_friends.put(terminalID_friend,
				_network.getTerminal(terminalID_friend));
		}
	}
	public void removeFriend(String terminal_unfriend, Network _network) throws
	 	UnknownTerminalKeyException, InvalidTerminalKeyException {
			
			if(_network.getTerminal(terminal_unfriend)==null)
				throw new UnknownTerminalKeyException 
					(terminal_unfriend);

			if(terminal_unfriend.length() != 6 
					|| terminal_unfriend.matches("[0-9]+")
				       	!= true ) 
				throw new InvalidTerminalKeyException
					(terminal_unfriend);
				
			if(_friends.get(terminal_unfriend)!=null) {
				_friends.remove(terminal_unfriend);
			}
	}

	/**
	*
	* Operations related to Terminal's States
	*
	*/
													
	public void changetoIdle(Network network)throws TerminalAlreadyIdleException {
		if(this.isAlreadyIdle())
			throw new TerminalAlreadyIdleException();
		if(_state.canTurnIdle()){
			_pastState=_state;
			_state= new Idle();
		}
		if(shouldSendNotifications()){
			Notification notification=decideNotification();
			sendNotificationsToClients(notification,network);
		}
	}
	
	public boolean isAlreadyIdle(){
		if(_state.getType().equals("IDLE"))
			return true;
		return false;
	}

	public void changetoSilence(Network network) throws TerminalAlreadySilentException {
		if(this.isAlreadySilence())
			throw new TerminalAlreadySilentException();
		if(_state.canTurnSilence()==true) {
			_pastState= _state;
			_state= new Silence();
		}
		if(shouldSendNotifications()){
			Notification notification=decideNotification();
			sendNotificationsToClients(notification,network);				    }
	}
	
	public boolean isAlreadySilence(){
		if(_state.getType().equals("SILENCE"))
			return true;
		return false;
	}

	public void changetoOff(Network network) throws TerminalAlreadyOffException {
		if(this.isAlreadyOff())
			throw new TerminalAlreadyOffException();
		if(_state.canTurnOff()==true) {
			_pastState=_state;
			_state= new Off();
		} 
		 if(shouldSendNotifications()){			                    
			Notification notification=decideNotification();
			sendNotificationsToClients(notification,network);
		 }
	}
	
	public boolean isAlreadyOff(){
		if(_state.getType().equals("OFF"))
			return true;
		return false;
	}

	public void changetoBusy(Network network) throws TerminalAlreadyBusyException{
		if(this.isAlreadyBusy())
			throw new TerminalAlreadyBusyException();
		if(_state.canTurnBusy()==true){
			_pastState=_state;
			_state= new Busy();
		}
		if(shouldSendNotifications()){
			Notification notification= decideNotification();
			sendNotificationsToClients(notification,network);
		}
	}

	public boolean isAlreadyBusy(){
		if(_state.getType().equals("BUSY"))
			return true;
		return false;
	}

	/**
	*
	* Operations related to Notifications
	*
	**/

	public Notification decideNotification(){
		String pastName=_pastState.getType();
		String presentName= _state.getType();
		RealNotification notification;
		if(pastName.equals("OFF")){
			if(presentName.equals("IDLE"))
				notification= new RealNotification("O2I",
						this.getIDTerminal());
			else
				notification= new RealNotification("O2S",
						this.getIDTerminal());
			return notification;
		}
		if(pastName.equals("SILENCE")){
			notification= new RealNotification("S2I",
					this.getIDTerminal());
			return notification;
		}
		notification= new RealNotification("B2I",
				this.getIDTerminal());
		return notification;
	}

	public boolean shouldSendNotifications(){
		String pastName= _pastState.getType();
		String presentName= _state.getType();
		if(pastName.equals("OFF") && (presentName.equals("IDLE") ||
					presentName.equals("SILENCE")))
			return true;
		if(pastName.equals("BUSY") && presentName.equals("IDLE"))
			return true;
		if(pastName.equals("SILENCE") && presentName.equals("IDLE"))
			return true;
		return false;
	}

	public void sendNotificationsToClients(Notification notification
		,Network network) {
		Map<String,Notification> novo = new TreeMap<String,Notification>();
		for(Map.Entry<String,Notification>
			entry: _clientsToNotify.entrySet()){
			if(entry.getValue().getMessage().equals("INTERACTIVE") &&
				       	notification.getMessage().equals("O2S")){
				novo.put(entry.getKey(),entry.getValue());
				continue;
			}
			network.getClient(entry.getKey()).addNotification(notification);
		}
		Map<String,Notification> novo2= new TreeMap<String,Notification>();
		for(Map.Entry<String,Notification>
			entry: _clientsToNotify2.entrySet()){
			if(entry.getValue().getMessage().equals("INTERACTIVE") &&
					notification.getMessage().equals("O2S")){
				novo2.put(entry.getKey(),entry.getValue());
				continue;
			}
			network.getClient(entry.getKey()).addNotification(notification);
		}
		_clientsToNotify=novo;
		_clientsToNotify2=novo2;
	}

	public void addClientToNotify(String idClient,Notification notification){
		if(_clientsToNotify.get(idClient)==null)
			_clientsToNotify.put(idClient,
					notification);
		if(_clientsToNotify2.get(idClient)==null && notification
				.getMessage().equals(_clientsToNotify.
					get(idClient).getMessage())==false)
			_clientsToNotify2.put(idClient,notification);
	}

	/**
	*
	* Operations for Text, Video and Voice Communications
	*
	**/

	public boolean canSendText(Terminal receiverTerminal) {
	    if(receiverTerminal.isAlreadyOff())
			return false;
		return true;
	}

	public boolean canVideo(){
		if(terminalType.equals("BASIC"))
			return false;
		else
			return true;
	}

	public void sendText(String receiverTerminalID, 
			String message, Network network) 
		throws TerminalAlreadyOffException, UnknownTerminalKeyException {
		
		if(network.getTerminal(receiverTerminalID) == null)
			throw new UnknownTerminalKeyException(receiverTerminalID);
		
		if(this.canStartCommunication()==false)
			return;
		
		if(canSendText(network.getTerminal(receiverTerminalID)) == false){
			
			if(network.getClient(_idClient).getNotificationsState()==true){
				Notification notification = new 
					RealNotification("TEXT",getIDTerminal());
				network.getTerminal(receiverTerminalID).
					addClientToNotify(_idClient,notification);
			}
			throw new TerminalAlreadyOffException();
		}
		
		if(this.isAlreadyOff())
			return;
		
		if(this.getIDTerminal().equals(receiverTerminalID))
			return;
		
		//after verifying all cases, creates a text
		Text textCom = new Text(receiverTerminalID,this.getIDTerminal()
				,network.getCommunicationId(),message);
		
		//calculates the communication cost using his tarif plan
		Client consumer = network.getClient(getIDClient());
		PricingPlan consumerPlan = consumer.getStatus().getPlan();
		double communicationPrice = consumerPlan.textCommunicationPrice(textCom.countCharacters());
		
		//if the communication was with friend, the client only pays half price
		if(this.isFriend(receiverTerminalID))
			communicationPrice = communicationPrice * (0.5);
		
		//updates the client, and terminal balance and the communication price
		textCom.setPrice((long)communicationPrice);
		long terminalDebts = (long) this.getDebts() + (long)communicationPrice;  
		this.setDebts(terminalDebts);
		consumer.incrementDebt((long)communicationPrice);
		//increments text consecutives
		consumer.setTextConsecutives(consumer.getTextConsecutives()+1);
		//sets videoConsecutives to zero, because it was made a text communication
		consumer.setVideoConsecutives(0);

		//increases the communication id for the next communication to be created
		network.increaseCommunicationId();
		//adds the communication to the terminals
		addCommunication(textCom);
		network.getTerminal(receiverTerminalID).addCommunication(textCom);
		this.increaseNumberCommunications();
		network.getTerminal(receiverTerminalID).increaseNumberCommunications();

		//verifys if a client of type GOLD shoul be demoted
		if(consumer.getStatus().getStatus().equals("GOLD") &&
		consumer.getStatus().shouldDowngrade()) {
				consumer.getStatus().downgrade();
		}

		//verifies if a client of type Platinum should be demoted
		if(consumer.getStatus().getStatus().equals("PLATINUM") &&
		consumer.getStatus().shouldDowngrade()) {
				consumer.getStatus().downgrade();
		}
	}

	public void startInteractiveCommunication(String type, String terminalReceiverID,Network network)
		throws TerminalUnsupportedAtDestinationException, TerminalUnsupportedAtOriginException,
		TerminalAlreadyOffException, TerminalAlreadyBusyException,
		TerminalAlreadySilentException,	UnknownTerminalKeyException {

		if(network.getTerminal(terminalReceiverID)==null)
			throw new UnknownTerminalKeyException
					 (terminalReceiverID);

		if(canStartCommunication()==false)
			return;

		Terminal terminalReceiver = network.getTerminal(terminalReceiverID);

		if(type.equals("VIDEO")  && this.canVideo() == false)
			throw new TerminalUnsupportedAtOriginException();

		if(type.equals("VIDEO") && terminalReceiver.canVideo() == false)

			throw new TerminalUnsupportedAtDestinationException();

		if(terminalReceiver.isAlreadyOff()){
			if(network.getClient(_idClient).getNotificationsState()==true){
				Notification notification= new RealNotification
					("INTERACTIVE",getIDTerminal());
				network.getTerminal(terminalReceiverID).
					addClientToNotify(_idClient,notification);
			}
			throw new TerminalAlreadyOffException();
		}
		if(terminalReceiver.isAlreadyBusy()){
			if(network.getClient(_idClient).getNotificationsState()==true){
				 Notification notification= new RealNotification
					 ("INTERACTIVE",getIDTerminal());
				network.getTerminal(terminalReceiverID).
					addClientToNotify(_idClient,notification);
		 	}		
			throw new TerminalAlreadyBusyException();
			
		}	
		if(this.getIDTerminal().equals(terminalReceiverID))

			throw new TerminalAlreadyBusyException();

		if(terminalReceiver.isAlreadySilence()){
			if(network.getClient(_idClient).getNotificationsState()==true){
				 Notification notification= new RealNotification
					 ("INTERACTIVE",getIDTerminal());
				network.getTerminal(terminalReceiverID).
					addClientToNotify(_idClient,notification);
			}
			throw new TerminalAlreadySilentException();
		}
		Communication intCom;

		if(type.equals("VIDEO"))
			intCom= new Video( terminalReceiverID,this.getIDTerminal()
					,network.getCommunicationId());
		else
			intCom= new Voice( terminalReceiverID,this.getIDTerminal()		
			,network.getCommunicationId());	

		//puts the terminals of the ongoing communication on busy state
		this.changetoBusy(network);
		terminalReceiver.changetoBusy(network);
		//increases the communication id for the next communication to be created
		network.increaseCommunicationId();	
		Client consumer = network.getClient(getIDClient());

		consumer.setTextConsecutives(0);
		if(type.equals("VIDEO"))
			consumer.setVideoConsecutives(consumer.getVideoConsecutives()+1);

		//adds the ongoing communication to the terminals and increases
		// their number of communications			
		this.addCommunication(intCom);
		this.setOnGoingCommunication(intCom);	 		
		terminalReceiver.addCommunication(intCom);
		terminalReceiver.setOnGoingCommunication(intCom);
		this.increaseNumberCommunications();
		terminalReceiver.increaseNumberCommunications();

		
	}

	public void endInteractiveCommunication(int time,Network network){
		if(canEndCurrentCommunication() == false)
			return;
		Communication com=null;

		//set communication state to false because it has finished		
		onGoingCom.setCommunicationState(false);
		onGoingCom.setDuration((double)time);
		//set terminals to their previous states
		this.setState(_pastState);
		_pastState=new Busy();
		Terminal terminalReceiver = network.getTerminal(onGoingCom.getReciever());
		terminalReceiver.setState(terminalReceiver.getPastState());
		terminalReceiver.setPastState(new Busy());
		Client consumer = network.getClient(this.getIDClient());
		PricingPlan consumerPlan = consumer.getStatus().getPlan();
		
		//calculates comunication price, according if it's a video or voice communication
		double communicationPrice;
		if(onGoingCom.getName().equals("VOICE"))
			communicationPrice = consumerPlan.voiceCommunicationPrice((double)time);
		else
			communicationPrice = consumerPlan.videoCommunicationPrice((double)time);

		if(this.isFriend(onGoingCom.getReciever()))
			communicationPrice = communicationPrice * (0.5);
		
		//updates clients, and terminals debts
		onGoingCom.setPrice((long)communicationPrice);
		long terminalDebts = (long) this.getDebts() + (long)communicationPrice;  
		this.setDebts(terminalDebts);
		consumer.incrementDebt((long)communicationPrice);
		this.setlastCommunicationPrice((long)communicationPrice);

		network.getTerminal(onGoingCom.getReciever())
		.setOnGoingCommunication(com);
		this.setOnGoingCommunication(com);

		//verifys if a client of type Gold should me demoted
		if(consumer.getStatus().getStatus().equals("GOLD") &&
		consumer.getStatus().shouldDowngrade()) {
				consumer.getStatus().downgrade();
		}

		//verifies if a client of type Gold shoul be promoted
		if(consumer.getStatus().getStatus().equals("GOLD") &&
		consumer.getStatus().shouldUpgrade()) {
				consumer.getStatus().upgrade();
		}

		//verifies if a client of type Platinum should be demoted
		if(consumer.getStatus().getStatus().equals("PLATINUM") &&
		consumer.getStatus().shouldDowngrade()) {
				consumer.getStatus().downgrade();
		}
		
	}


	public void performPayment(int commKey, Network network) throws InvalidCommunicationException{

		Communication commToPay = getCommunication(commKey);
		if(commToPay == null || commToPay.checkIfPaid() == true 
		|| !commToPay.getSender().equals(this.getIDTerminal()))
			throw new InvalidCommunicationException();

		if(commToPay.checkIfPaid() == false && 
		commToPay.getCommunicationState() == false &&
		commToPay.getSender().equals(this.getIDTerminal())) {

			//updates client's and terminals balance after payment
			Client client = network.getClient(this.getIDClient());
			client.makePayment(commToPay.getPrice());
		
			this.decreaseDebt((double)commToPay.getPrice());
			this.increasePayments((double)commToPay.getPrice());  

			Status clientStatus = client.getStatus();

			//checks if a client of type Normal should be promoted
			if(clientStatus.getStatus().equals("NORMAL") && 
			clientStatus.shouldUpgrade()) {

					client.getStatus().upgrade();
			}

			commToPay.setCommPaymentState(true);
		}
			
	}

}
