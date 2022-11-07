package prr.clients;

import java.io.Serializable;
import prr.clients.PricingPlan;

public class PlatinumPlan extends PricingPlan implements Serializable {

    private final double shortText = 0;

    private final double mediumText = 4;

    private final double longText = 4;

    private final double voiceCommunication = 10;

    private final double videoCommunication = 10;

    public double textCommunicationPrice(double characters) {
        if(characters < 50)
            return shortText;
        else if(50 <= characters && characters < 100)
            return mediumText;
        else
            return longText;
    }

    public double voiceCommunicationPrice(double minutes) {
        return (voiceCommunication * minutes);
    }

    public double videoCommunicationPrice(double minutes) {
        return (videoCommunication *  minutes);
    }

}