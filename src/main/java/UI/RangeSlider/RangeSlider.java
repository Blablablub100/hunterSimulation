package UI.RangeSlider;

import UI.RangeSlider.multirange.MultiRange;

public class RangeSlider extends MultiRange {

    public RangeSlider(int min, int max) {
        super(min,max);
    }

    @Override
    public void createNewRange(double low, double high) {
        return;
    }
}
