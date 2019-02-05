package UI.RangeSlider;

import UI.RangeSlider.multirange.MultiRange;

/**
 * Class for creating RangeSliders in JavaFX
 */
public class RangeSlider extends MultiRange {

    /**
     * Constructor for creating new RangeSliders with a min and max value.
     * @param min min for the RangerSlider.
     * @param max max for the RangerSlider.
     */
    public RangeSlider(int min, int max) {
        super(min,max);
    }

    @Override
    public void createNewRange(double low, double high) {
        return;
    }
}
