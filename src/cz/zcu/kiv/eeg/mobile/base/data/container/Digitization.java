package cz.zcu.kiv.eeg.mobile.base.data.container;

/**
 * @author Petr Miko
 *         Date: 10.3.13
 */
public class Digitization {

    private int digitizationId;
    private String filter;
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
