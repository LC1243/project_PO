package prr.clients;

import java.io.Serializable;
import prr.clients.Client;
import prr.clients.PricingPlan;

public abstract class Status implements Serializable{
    
    private String _status;

    private Client _client;

    private PricingPlan _plan;
    
    public Status(String status, Client client, PricingPlan plan) {
	    _status = status;
        _client = client;
        _plan = plan;
    }

    public String getStatus() {
        return _status;
    }

    public Client getClient() {
        return _client;
    }

    public PricingPlan getPlan() {
        return _plan;
    }

    public abstract boolean shouldUpgrade();

    public abstract boolean shouldDowngrade();

    public abstract void upgrade();

    public abstract void downgrade();

}
