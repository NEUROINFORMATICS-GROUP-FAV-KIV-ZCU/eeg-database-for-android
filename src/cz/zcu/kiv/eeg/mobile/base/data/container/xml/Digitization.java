package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for digitization information.
 *
 * @author Petr Miko
 */
@Root(name = "digitization")
public class Digitization {

    @Element
    private int digitizationId;
    @Element
    private String filter;
    @Element
    private float gain, samplingRate;

    public int getDigitizationId() {
        return digitizationId;
    }

    public void setDigitizationId(int digitizationId) {
        this.digitizationId = digitizationId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public float getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(float samplingRate) {
        this.samplingRate = samplingRate;
    }
}
