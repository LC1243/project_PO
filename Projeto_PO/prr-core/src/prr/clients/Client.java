package prr.clients;


import prr.terminals.Terminal;
import prr.exceptions.UnrecognizedEntryException;
import prr.clients.Status;
import prr.notifications.Notification;
import prr.notifications.RealNotification;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;


public class Client implements Serializable {

    private String _id;

    private long _saldo = 0;

    private long _payments = 0;

    private String _name;

    private int _nif;

    private long _debts = 0;

    private boolean _notificationsState = true;

    private Status _status = new Normal(this);

    private int _tconsecutivas = 0;

    private int _vconsecutivas = 0;

    private ArrayList<Notification> _notifications = new ArrayList<
	    						Notification>();

    private Map<String, Terminal> _terminals = new TreeMap<String, Terminal>();

    private int _nTerminals = 0;

    private boolean showOneClient;


    public Client(String id, String name, int nif) {
        _id = id;
        _name = name;
        _nif = nif;
    }

    public void setShowOneClient(boolean value){
	    showOneClient=value;
    }

    public boolean getShowOneClient(){
	    return showOneClient;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public long getSaldo() {
        return _payments - _debts;
    }

    public void setSaldo(long saldo) {
        _saldo = saldo;
    }

    public long getPayments() {
        return _payments;
    }

    public void setPayments(long payments) {
        _payments = payments;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getNif() {
        return _nif;
    }

    public void setNif(int nif) {
        _nif = nif;
    }

    public long getDebts() {
        return _debts; 
    }

    public void setDebts(long debts) {
        _debts = debts;
    }

    public boolean getNotificationsState() {
        return _notificationsState;
    }

    public void setNotificationsState(boolean state) {
        _notificationsState = state;
    }

    public String getNotificationsMode() {
        if (getNotificationsState() == true)
            return "YES";
        return "NO";
    }

    public int getTextConsecutives() {
        return _tconsecutivas;
    }

    public void setTextConsecutives(int tconsecutivas) {
        _tconsecutivas = tconsecutivas;
    }

    public int getVideoConsecutives() {
        return _vconsecutivas;
    }

    public void setVideoConsecutives(int vconsecutivas) {
        _vconsecutivas = vconsecutivas;
    }

    public void addTerminal(Terminal terminal) {
        _terminals.put(terminal.getIDTerminal(), terminal);
        _nTerminals++;
    }

    public boolean inDebt(){
        return _debts > 0;
    }

    public void activateNotifications(Terminal terminal){
        _notificationsState = true;
    }

    public void deactivateNotifications(Terminal terminal){
        _notificationsState = false;
    }

    public void incrementDebt(long debt){
        _debts += debt;
    }

    public Status getStatus() {
        return _status;
    }

    public void setStatus(Status status) {
        _status = status;
    }

    public void makePayment(long price) {
        _debts -= price;
        _payments += price;
        _saldo = _payments - _debts;
    }

    public boolean shouldAddNotificationInClient(Notification notification){
	for(int i=0;i<_notifications.size();i++){
		if(_notifications.get(i).getMessage().equals(
				notification.getMessage()))
			return false;
	}
	return true;
    }   

    public void clearNotifications(){
	    _notifications.removeAll(_notifications);
    }

    public void addNotification(Notification notification){
	for(Notification notification2: _notifications){
		if (notification2.equals(notification))
			return;
	}
    		_notifications.add(notification);
    }
    public String toStringNotifications(){
    	String result="";
	for(int i=0;i<_notifications.size();i++){
		result+="\n"+ _notifications.get(i).toString();
	}
	return result;
    			    
    }
    @Override
    public String toString() {
        int payments = (int) _payments;
        int debts = (int) _debts;
	String string;
	if(toStringNotifications().equals("") || showOneClient == true)
        	return "CLIENT|" + _id + "|" + _name + "|" + _nif + "|" +
        	_status.getStatus() + "|" + getNotificationsMode() + "|"
	       	+ _nTerminals + "|" + payments + "|" + debts ;
	else{
		string= "CLIENT|" + _id + "|" +_name + "|" + 
		       	_nif + "|" + _status.getStatus()+ "|" + 
			getNotificationsMode() + "|" + _nTerminals +
			"|" + payments + "|" + debts  + 
			toStringNotifications();
		clearNotifications();
		return string;
	}
    }
    
}
