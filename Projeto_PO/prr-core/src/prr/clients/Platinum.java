package prr.clients;

import java.io.Serializable;
import prr.clients.Status;
import prr.clients.Client;
import prr.clients.PlatinumPlan;

public class Platinum extends Status implements Serializable {

    private boolean doubleDowngrade = false;
   
    public Platinum(Client client) {
        super("PLATINUM", client, new PlatinumPlan());
    }


    public boolean shouldUpgrade(){
        //can't upgrade from Platinum
        return false;
    }

    public boolean shouldDowngrade(){
        if(getClient().getTextConsecutives() == 2
        && getClient().getSaldo() >= 0) {
            return true;
        }
        if(getClient().getSaldo() < 0) {
            doubleDowngrade = true;
            return true;
        }
        return false;
    }

    public void upgrade(){
        //do nothing
    }

    public void downgrade(){
        if(shouldDowngrade() == true && doubleDowngrade == true) {
            getClient().setStatus(new Normal(getClient()));
            getClient().setTextConsecutives(0);
            getClient().setVideoConsecutives(0);
        }
        else if(shouldDowngrade() == true && doubleDowngrade == false) {
            getClient().setStatus(new Gold(getClient()));
            getClient().setTextConsecutives(0);
            getClient().setVideoConsecutives(0);
        }
    }

}
