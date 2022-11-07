package prr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import prr.exceptions.UnrecognizedEntryException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.DuplicateTerminalKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DuplicateClientKeyException;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.InvalidEntryException;
import prr.exceptions.ImportFileException;
import prr.exceptions.NotificationsAlreadyEnabledException;
import prr.exceptions.NotificationsAlreadyDisabledException;

import prr.terminals.Terminal;
import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;
import prr.clients.Client;
import prr.clients.Status;
import prr.clients.Normal;
import prr.clients.Gold;
import prr.clients.Platinum;
import prr.communications.Communication;

// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

	private long _globalPayments;

	private long _globalDebts;

	private long _globalBalance;

	private int _communicationId = 1;

	/** Clients. */
  	private Map<String, Client> _clients = new TreeMap<String, Client>();

  	/** Terminals. */
  	private Map<String, Terminal> _terminals = new TreeMap<String, Terminal>();

 	/** Network object has been changed. */
  	private boolean _changed = false;


	public class sortById implements Comparator<Client> {

        @Override
        public int compare(Client client1, Client client2) {
            return client1.getId().compareToIgnoreCase(client2.getId());
        }
    }

    public class sortByDebt implements Comparator<Client> {

        @Override
        public int compare(Client client1, Client client2) {
            return ((int)client1.getDebts()) - ((int)client2.getDebts());       
        }
    }

	public class sortCommunications implements Comparator<Communication> {

		@Override
		public int compare(Communication comm1, Communication comm2) {
			return comm1.getCommunicationID() -comm2.getCommunicationID();
		}
	}

	/**
	* Set changed.
	*/
	public void changed() {
		setChanged(true);
	}

	/**
	* @return changed
	*/
	public boolean hasChanged() {
		return _changed;
	}

	/**
	* @param changed
	*/
	public void setChanged(boolean changed) {
		_changed = changed;
	}

	/**
	 * Read text input file and create corresponding domain entities.
	 * 
	 * @param filename name of the text input file
     * @throws UnrecognizedEntryException if some entry is not correct
	 * @throws IOException if there is an IO erro while processing the text file
	 */
	void importFile(String filename) throws IOException, ImportFileException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      		String line;
      		while ((line = reader.readLine()) != null) {
        		String[] fields = line.split("\\|");
				registerEntry(fields);
        		
     		 }
   	 	} 
		catch (UnrecognizedEntryException | IOException | InvalidEntryException e) {
          	e.printStackTrace();
		}
  	}
	
	/**
	 * Import an entry from a plain text file
	 *
	 * @param fields the fields of the entry to the method below that were split
	 * by the separator  
	 * @throws UnrecognizedEntryException if some entry is not correct 
	 * @throws IOException If there is an IO erro while processing the text file 
	 * @throws InvalidEntryException if an entry isn't valid
	 */
	public void registerEntry(String... fields) 
			throws UnrecognizedEntryException, InvalidEntryException {
    	switch (fields[0]) {
    	case "CLIENT" -> this.importClient(fields);
 		case "BASIC", "FANCY" -> this.importTerminal(fields);
    	case "FRIENDS" -> this.importFriends(fields);
    	default -> throw new UnrecognizedEntryException(String.join("|", fields));
    	}
  	}
	

	/**
	 * Import a Client form a plain text file.
	 *
	 * @param fields the fields of the entry to the method below that were split
	 * by the separator
	 * @throws InvalidEntryException if some clientId is already taken 
	 * or is not correct 
	/**
	 * Import a Client form a plain text file.
	 *
	 * @param fields the fields of the entry to the method below that were split
	 * by the separator
	 * @throws InvalidEntryException if some clientId is already taken 
	 * or is not correct 
	 * @throws Unrecognized EntryException If ome entry is not correct
	 */
	public void importClient(String... fields) 
			throws InvalidEntryException, UnrecognizedEntryException {
		if (fields.length != 4) {
			throw new InvalidEntryException(fields);
		}
		try {
			this.registerClient(fields[1], fields[2], fields[3]);
		} catch (DuplicateClientKeyException | UnknownClientKeyException e) {
			throw new InvalidEntryException(fields);
    	}
	}
	
	/**
	 * Register a new Client in the Network, which will be created  from the
	 * given parameters.
	 *
	 * @param key client's  key
	 * @param name client's name 
	 * @param taxId client's nif
	 * @return The Client that was just created
	 * @throws DuplicateClientKeyException if clients key is already taken
	 * @throws UnknownClientKeyException if clients key doesnt respect certain rules
	 */
	
	public Client registerClient(String key, String name, String taxId) 
			throws DuplicateClientKeyException, UnknownClientKeyException {
		
		if (_clients.containsKey(key)) {
      		throw new DuplicateClientKeyException(key);
   		} 
		int client_taxId = Integer.parseInt(taxId);
		Client client = new Client(key, name, client_taxId);
		addClient(key, client);
		this.changed();
		return client;
  	}
	
	/**
	 * Import a Terminal from a plain text file.
	 *
	 * @param fields the fields of the entry to the method below that were split
	 * by the separator
	 * @throws UnrecognizedEntryException if some entry is not correct 
	 * @throws InvalidEntryException if the number of fields isnt equal to 4 (terminalKey ,clientKey,terminalType,state)
	 */
	public void importTerminal(String... fields) 
			throws UnrecognizedEntryException, InvalidEntryException {
		if (fields.length != 4) {
			throw new InvalidEntryException(fields);
		}
		try {   
			this.registerTerminal(fields[1], fields[2], fields[0], fields[3]);
		} catch (DuplicateTerminalKeyException | InvalidTerminalKeyException | UnknownClientKeyException e) {
			throw new InvalidEntryException(fields);
    	}	
	}
	

	/**
	 * Register a Terminal, redirecting it, in case if it's basic or fancy
	 *
	 * @param terminalKey  terminal identifier 
	 * @param clientKey  clientId to which the terminal is associated 
	 * @param terminalType terminal type, either BASIC or FANCY
	 * @param state Each terminal has a state that can be one of four types with
	 * different caracteristics : Idle, Busy, Off and Silenced 
	 * @throws DuplicateTerminalKeyException If terminalId already take
	 * @throws InvalidTerminalKeyException if key isnt correct. Different
	 * type from expected , incorrect length...
	 * @throws UnknownClientKeyException if client identifier refers to a 
	 * client that doesnt exist 
	 */
	public void registerTerminal(String terminalKey, String clientKey, String terminalType, String state) throws
	  DuplicateTerminalKeyException, InvalidTerminalKeyException, UnknownClientKeyException {

		if(_terminals.containsKey(terminalKey)) {
			throw new DuplicateTerminalKeyException(terminalKey);
		}
		if(getClient(clientKey) == null) {
			throw new UnknownClientKeyException(clientKey);
		}
		if(terminalKey.length() != 6 || terminalKey.matches("[0-9]+") != true ) {
			throw new InvalidTerminalKeyException(terminalKey);
	  	}
		if(terminalType.equals("BASIC")) {
			this.registerBasicTerminal(terminalKey, clientKey, state);
		}
		if(terminalType.equals("FANCY")) {
			this.registerFancyTerminal(terminalKey, clientKey, state);
		}

	}

	/**
	 * Register a BasicTerminal in the Network, which will be created
	 * with the given parameters.
	 *
	 * @param terminalKey terminal identifier 
	 * @param clientKey clientId to which the terminal is associated
	 * @param state Each terminal has a state that can be one of four types with
	 * different caracteristics : Idle, Busy, Off and Silenced
	 * @return The Terminal that was just created
	 */
	public Terminal registerBasicTerminal(String terminalKey, String clientKey, String state) {
		Terminal terminal = new BasicTerminal(terminalKey, clientKey, state);
		Client client = getClient(clientKey);

		if(client != null)
			client.addTerminal(terminal);

		addTerminal(terminalKey, terminal);
		this.changed();
		return terminal;
	}

	/**
	 * Register a FancyTerminal in the Network, which will be created
	 * with the given parameters.
	 *
	 * @param terminalKey terminal identifier
	 * @param clientKey clientId to which the terminal is associated
	 * @param state Each terminal has a state that can be one of four types with
	 * different caracteristics : Idle, Busy, Off and Silenced
	 * @return The Terminal that was just created
	 */
	public Terminal registerFancyTerminal(String terminalKey, String clientKey, String state) {
		Terminal terminal = new FancyTerminal(terminalKey, clientKey, state);
		Client client = getClient(clientKey);
		
		if(client != null)
			client.addTerminal(terminal);
		
		addTerminal(terminalKey, terminal);
		this.changed();
		return terminal;
	}
	
	/**
	 * Imports a list of terminal keys to be added as friends to a 
	 * specific terminal, with a given key
	 *
	 * @param fields the fields of the entry to the method below that were split
	 * by the separator
	 * @throws UnrecognizedEntryException if some entry is not correct 
	 * @throws InvalidEntryException if the number of fields isnt equal to 3 (FRIENDS ,terminalKey, terminalskeys
	 *  to add as friends)
	 */
	public void importFriends(String... fields) throws UnrecognizedEntryException, 
			InvalidEntryException {
		if (fields.length != 3) {
			throw new InvalidEntryException(fields);
		}
		try {   
			this.addFriends(fields[1], fields[2]);
		} catch (InvalidTerminalKeyException e) {
			throw new InvalidEntryException(fields);
		}	
	}

	/**
	 * Adds friend(s) to a Terminal wich is given the key
	 *
	 * @param terminalKey terminal identifier
	 * @param terminalsToAdd a string with terminals indentifiers to be separated
	 * and to be added as friends to thew terminal with key terminalKey
	 * @throws InvalidTerminalKeyException  if the terminal to be added friends has an invalid key
	 */
	public void addFriends(String terminalKey, String terminalsToAdd) 
			throws InvalidTerminalKeyException{
		if(_terminals.containsKey(terminalKey) == false)
			throw new InvalidTerminalKeyException(terminalKey);
		String[] friends = terminalsToAdd.split(",");
		for(String friend : friends) {
			if(getTerminal(friend) == null)
				throw new InvalidTerminalKeyException(friend);
			getTerminal(terminalKey).addFriend(getTerminal(friend));
		}
		this.changed();
	}
	
	/**
	 * Adds a Client to a TreeMap called _clients, where are being
	 * stored all Clients
	 *
	 * @param key represents the key that is going to be used to store the
	 * client in the Map of clients
	 * @param client client that is going to be added to the Map
	 */
	public void addClient(String key, Client client) {
    	_clients.put(key, client);
    	changed();
  	}

	/**
	 * Adds a Temrinal to a TreeMap called _terminals, where are being 
	 * stored all Terminals
	 *
	 * @param key represents the key that is going to be used to store
	 * terminal in the Map of terminals
	 * @param terminal terminal that is going to be added to the Map 
	 */
	public void addTerminal(String key, Terminal terminal) {
    	_terminals.put(key, terminal);
	changed(); 
	}
	/**
	 * Fetch the Terminal, from which it will open his Console
	 *
	 * @param terminal_key is going to be used in order to open a Menu
	 * of an specified terminal. Said Menu has operations that focus on
	 * the terminal with the terminal_key
	 * @return The Terminal to be opened the console with terminal operations
	 * @throws UnknownTerminalKeyException If terminal_key isnt the terminalId of
	 * a terminal within the Network
	 */
	public Terminal getTerminalForConsole(String terminal_key) 
			throws UnknownTerminalKeyException {
		if(getTerminal(terminal_key) == null)
			throw new UnknownTerminalKeyException(terminal_key);
		return getTerminal(terminal_key);
	}
	/**
	 * Fetch the Client, to be showm in DoShowClient
	 *
	 * @param client_key used to know which client this method is going to operate upon 
	 * @return The Client that is to be shown in DoShowClient 
	 * @throws UnknownClientKeyException if client_key isnt the clientId of
	 * a client within the Network
	 */
	public Client getClientToShow(String client_key) 
			throws UnknownClientKeyException {
		if(getClient(client_key) == null) 
			throw new UnknownClientKeyException(client_key);
		getClient(client_key).setShowOneClient(false);
		return getClient(client_key);
	}
	
	/**
	 * Calculates the network global balance
	 *
	 * @return The global balance of the network
	 * @throws UnknownClientKeyException if client_key isnt the clientId of
	 * a client within the Network
	 */
	public long showGlobalBalance() {
		_globalBalance = _globalPayments - _globalDebts;
		return _globalBalance;
	}
	
	/**
	 * @return this network
	 */
	public Network getNetwork(){
		return this;
	}	
	/**
	 * Gets the value of global payments
	 *
	 * @return the total value of payments made in the network
	 */
	public long getTotalPayments() {
		return _globalPayments;
	}

	/**
	 * Gets the value of global debts
	 *
	 * @return the total value of debts made in the network
	 */
	public long getTotalDebts() {
		return _globalDebts;
	}

	/**
	 * Creates a list of all the clients in the network
	 *
	 * @return an ArrayList with all the Clients to be shown in DoShowAllClients
	 */
	public ArrayList<Client> showAllClients() {
		ArrayList<Client> clientsToShow = new ArrayList<Client>(_clients.values());
		Collections.sort(clientsToShow, new sortById());
		for(Client c:clientsToShow){
			c.setShowOneClient(true);
		}
		return  clientsToShow;
	}

	/**
	 * Creates a list of all the terminals in the network
	 * @return an ArrayList with all the Termianls to be shown in DoShowAllTerminals
	 */
	public ArrayList<Terminal> showAllTerminals() {
		ArrayList<Terminal> terminalsToShow = new ArrayList<Terminal>(_terminals.values());
		return terminalsToShow;
	}

	/**
	 * Creates a list of all terminals that have 0 communications (unuses terminals)
	 * @return an ArrayList with all the Terminals that haven't made communications
	 */
	public ArrayList<Terminal> showUnusedTerminals() {
		ArrayList<Terminal> terminals = showAllTerminals();
		ArrayList<Terminal> unusedTerminals = new ArrayList<Terminal>();
		for(Terminal t : terminals)
			if(t.getNumberCommunications() == 0)
				unusedTerminals.add(t);
		return unusedTerminals;
	}

	/**
	 * Searches and returns a terminal with a specified terminalId
	 * @param terminalKey to be used to fetch Terminal
	 * @return the Terminal whith the key terminalKey
	 */
	public Terminal getTerminal(String terminalKey) {
		return _terminals.get(terminalKey);
	}

	/**
	 * Searches and returns a client with a specified clientId
	 * @param clientKey to be used to fetch Client
	 * @return the Client with the key clientKey
	 */
	public Client getClient(String clientKey){
		return _clients.get(clientKey);
	}
	

	/**
	 * Show the debts and payments from a Client with a specified clientId
	 * @param clientKey used to get the client to be shown his payments 
	 * and debts
	 */
	public String showClientPaymentsAndDebts(String clientKey) throws UnknownClientKeyException{
		Client client = getClient(clientKey);
		if(client == null)
			throw new UnknownClientKeyException(clientKey);
		return clientKey;
	}

	/**
	 * Shows all clients without debts
	 * @return an arraylist of clients to be displayed in app
	 */
	public ArrayList<Client> showClientsWithoutDebts() {
		ArrayList<Client> clients = showAllClients();
		ArrayList<Client> clientsWithoutDebts = new ArrayList<Client>();
		for(Client c : clients)
			if(c.getDebts() == 0)
				clientsWithoutDebts.add(c);
		Collections.sort(clientsWithoutDebts, new sortById());
		return clientsWithoutDebts;		
	}

	/**
	 * Shows all clients with debts sorted by their debts in reverse order
	 * @return an arraylist of clients to be displayed in app
	 */
	public ArrayList<Client> showClientsWithDebts(){
		ArrayList<Client> clients = showAllClients();
		ArrayList<Client> clientsWithDebts = new ArrayList<Client>();
		for(Client c : clients)
			if(c.getDebts() > 0)
				clientsWithDebts.add(c);
		Collections.sort(clientsWithDebts, new sortById());
		Collections.sort(clientsWithDebts, new sortByDebt());
		Collections.reverse(clientsWithDebts);
		return clientsWithDebts;
	}

	/**
	 * Enables a client notifications
	 * @param recieves a Client, from which his notifications
	 * will be enabled
	 * @throws UnknownClientKeyEXception in case the clientKey doesn't belong to network
	 * @throws NotificationsAlreadyEnabled in case of client's notifications are already enabled
	 */
	public void enableClientsNotifications(String clientKey) throws UnknownClientKeyException, 
	NotificationsAlreadyEnabledException {
		Client client = getClient(clientKey);
		
		if(client == null) {
			throw new UnknownClientKeyException(clientKey);
		}
		if(client.getNotificationsState() == true) {
			throw new NotificationsAlreadyEnabledException();
		}
		else 
			client.setNotificationsState(true);		
	}

	/**
	 * Disables a client notifications
	 * @param recieves a Client, from which his notifications
	 * will be disabled
	 */
	public void disableClientsNotifications(String clientKey) throws UnknownClientKeyException,
	NotificationsAlreadyDisabledException {
		Client client = getClient(clientKey);
		
		if(client == null) {
			throw new UnknownClientKeyException(clientKey);
		}
		if(client.getNotificationsState() == false) {
			throw new NotificationsAlreadyDisabledException();
		}
		else 
			client.setNotificationsState(false);		
	}


	public int getCommunicationId() {
		return _communicationId;
	}

	public void increaseCommunicationId() {
		_communicationId += 1;
	}
	/**
	 * Shows All Communications made
	 */
	public ArrayList<Communication> showAllCommunications() {
		ArrayList<Terminal> terminals = showAllTerminals();
		ArrayList<Communication> communications = new ArrayList<Communication> ();
		for(Terminal t : terminals) {
			communications.addAll(t.getCommunicationsFromTerminal());
		}
		Collections.sort(communications, new sortCommunications());
		return communications;
	}

	/**
	 * Shows terminals with a positive Balance
	 */
	public ArrayList<Terminal> showTerminalsWithPositiveBalance() {
		ArrayList<Terminal> terminals = showAllTerminals();
		ArrayList<Terminal> terminalsWithPositiveBalance = new ArrayList<Terminal>();
		for(Terminal t : terminals) {
			if(t.getSaldo() > 0)
				terminalsWithPositiveBalance.add(t);
		}
		return terminalsWithPositiveBalance;		
	}

	/**
	 * Shows communications to client in DoShowCommunicationsToClient
	 * @param recieves a clientId to fetch a client and show communications
	 *  to client
	 */
	public ArrayList<Communication> showCommunicationsToClient(String clientKey) {
		ArrayList<Terminal> terminals = showAllTerminals();
		ArrayList<Communication> communications = new ArrayList<Communication>();
		for(Terminal t : terminals) {
			if((t.getIDClient().equals(clientKey)))
				communications.addAll(t.getCommunicationsToTerminal());		
		}
		Collections.sort(communications, new sortCommunications());
		return communications;
	}

	/**
	 * Shows communications from client in showCommunicationsFromClient
	 * @param recieves a cleintId to fetch a Client and sheo communications
	 * from client
	 */
	public ArrayList<Communication> showCommunicationsFromClient(String clientKey) {
		ArrayList<Terminal> terminals = showAllTerminals();
		ArrayList<Communication> communications = new ArrayList<Communication>();
		for(Terminal t : terminals) {
			if((t.getIDClient().equals(clientKey)))
				communications.addAll(t.getCommunicationsFromTerminal());		
		}
		Collections.sort(communications, new sortCommunications());
		return communications;
	}

}

