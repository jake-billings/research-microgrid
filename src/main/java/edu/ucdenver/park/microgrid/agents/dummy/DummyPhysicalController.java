package edu.ucdenver.park.microgrid.agents.dummy;

/**
 * DummyPhysicalController
 * <p>
 * class
 * <p>
 * Dummy implementations of functions such as getVoltage()
 * <p>
 * this separates out the dummy measurement code from real, re-usable measurement sending code
 */
public class DummyPhysicalController {
    private DummyPhysicalController() {}

    public float getVoltage() {
        return 500L;
    }

    public float getAmperage() {
        return (float) Math.sin(System.currentTimeMillis() * 6.283185 / 10000) * 10L;
    }

    public float getWattage() {
        return getVoltage() * getAmperage();
    }

    public boolean isFault() {
        return getWattage() < 0;
    }

    public boolean isWarning() {
        return getWattage() > 0;
    }

    public boolean isCircuitBreakerOpen() {
        return isFault();
    }

    private static DummyPhysicalController instance = new DummyPhysicalController();

    public static DummyPhysicalController getInstance() {
        return instance;
    }
}
