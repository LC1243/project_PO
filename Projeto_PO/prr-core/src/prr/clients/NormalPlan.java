package prr.clients;

import java.io.Serializable;
import prr.clients.PricingPlan;

public class NormalPlan extends PricingPlan implements Serializable {

    private final double shortText = 10;

    private final double mediumText = 16;

    private final double doubleLongText = 2;

    private final double voiceCommunication = 20;

    private final double videoCommunication = 30;

    public double textCommunicationPrice(double characters) {
        if(characters < 50)
            return shortText;
        else if(50 <= characters && characters < 100)
            return mediumText;
        else
            return (doubleLongText * characters);
    }

    public double voiceCommunicationPrice(double minutes) {
        return (minutes * voiceCommunication);
    }

    public double videoCommunicationPrice(double minutes) {
        return (minutes * videoCommunication);
    }
}