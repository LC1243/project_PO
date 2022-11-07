package prr.clients;

import java.io.Serializable;

public abstract class PricingPlan implements Serializable {

    public PricingPlan() {}

    public abstract double textCommunicationPrice(double characters);

    public abstract double voiceCommunicationPrice(double minutes);

    public abstract double videoCommunicationPrice(double minutes);

}