package prr.clients;

import java.io.Serializable;
import prr.clients.Status;
import prr.clients.Client;
import prr.clients.GoldPlan;

public class Gold extends Status implements Serializable {
    
    public Gold(Client client){
        super("GOLD", client, new GoldPlan());
    }

    public boolean shouldUpgrade(){
        if(getClient().getVideoConsecutives() == 5 
        && getClient().getSaldo() >= 0)
            return true;
        return false;
    }

    public boolean shouldDowngrade(){
        if(getClient().getSaldo() < 0)
            return true;
        return false;
    }

    public void upgrade(){
        if(shouldUpgrade()) {
            getClient().setStatus(new Platinum(getClient()));
            getClient().setTextConsecutives(0);
            getClient().setVideoConsecutives(0);
        }
    }

    public void downgrade(){
        if(shouldDowngrade()) {
            getClient().setStatus(new Normal(getClient()));
            getClient().setTextConsecutives(0);
            getClient().setVideoConsecutives(0);
        }
    }

}
