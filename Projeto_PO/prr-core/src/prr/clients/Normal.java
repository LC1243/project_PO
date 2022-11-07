package prr.clients;

import java.io.Serializable;
import prr.clients.Status;
import prr.clients.Client;
import prr.clients.NormalPlan;

public class Normal extends Status implements Serializable {
    
    public Normal(Client client) {
    	super("NORMAL", client, new NormalPlan());
    }


    public boolean shouldUpgrade(){
        if(getClient().getSaldo() > 500)
            return true; 
        return false;
    }

    public boolean shouldDowngrade(){
        // can't downgrade from normal
        return false;
    }

    public void upgrade(){
        if(shouldUpgrade()) {
            getClient().setStatus(new Gold(getClient()));
            getClient().setTextConsecutives(0);
            getClient().setVideoConsecutives(0);
        }
    }

    public void downgrade(){
        //do nothing
    }

}
