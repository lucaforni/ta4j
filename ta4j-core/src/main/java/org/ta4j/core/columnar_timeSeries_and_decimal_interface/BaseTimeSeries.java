package org.ta4j.core.columnar_timeSeries_and_decimal_interface;

import java.util.ArrayList;
import java.util.List;

public class BaseTimeSeries<D extends Value> extends AbstractTimeSeries<D> {

    public BaseTimeSeries(int capacity, NumOperationsFactory<D> numOperationsFactory) {
        super(capacity, numOperationsFactory);
    }

    public BaseTimeSeries(NumOperationsFactory<D> numOperationsFactory) {
        super(numOperationsFactory);
    }

    public BaseTimeSeries(List<Bar<D>> bars, NumOperationsFactory<D> numOperationsFactory){
        super(bars.size(), numOperationsFactory);
        for(Bar b: bars){
            addBar(b);
        }
    }

    @Override
    public Bar<D> getBar(int i) {
        return get(i);
    }

    @Override
    public int getBarCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return position < 0;
    }

    @Override
    public List<Bar<D>> getBarData() {
        List<Bar<D>> barList = new ArrayList<>();
        for (int i = 0; i<capacity; i++){
            barList.add(getBar(i));
        }
        return barList;
    }

    @Override
    public int getBeginIndex() {
        return 0;
    }

    @Override
    public int getEndIndex() {
        return capacity-1;
    }

    @Override
    public void setMaximumBarCount(int maximumTickCount) {

    }

    @Override
    public int getMaximumBarCount() {
        return getWindowSize();
    }

    @Override
    public int getRemovedBarCount() {
        return removed;
    }

    @Override
    public void addBar(Bar bar) {
        addBar(bar.getEndTime(),
                (D) bar.getOpenPrice(),
                (D) bar.getMinPrice(),
                (D) bar.getMinPrice(),
                (D) bar.getClosePrice(),
                (D) bar.getVolume());
    }
}
