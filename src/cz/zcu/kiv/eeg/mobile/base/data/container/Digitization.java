package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * Digitization data container.
 *
 * @author Petr Miko
 */
public class Digitization implements Serializable {

    private int digitizationId;
    private String filter;
    private float gain, samplingRate;

    /**
     * Digitization identifier getter.
     *
     * @return digitization identifier value
     */
    public int getDigitizationId() {
        return digitizationId;
    }

    /**
     * Digitization identifier setter.
     *
     * @param digitizationId digitization identifier
     */
    public void setDigitizationId(int digitizationId) {
        this.digitizationId = digitizationId;
    }

    /**
     * Filter getter.
     *
     * @return filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Filter setter.
     *
     * @param filter used filter
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Digitization gain getter.
     *
     * @return used gain
     */
    public float getGain() {
        return gain;
    }

    /**
     * Digitization gain setter.
     *
     * @param gain used gain
     */
    public void setGain(float gain) {
        this.gain = gain;
    }

    /**
     * Sampling rate getter.
     *
     * @return set sampling rate
     */
    public float getSamplingRate() {
        return samplingRate;
    }

    /**
     * Setter of sampling rate.
     *
     * @param samplingRate set sampling rate
     */
    public void setSamplingRate(float samplingRate) {
        this.samplingRate = samplingRate;
    }
}
