package org.ta4j.core.columnar_timeSeries_and_decimal_interface;
import org.junit.Test;
import org.ta4j.core.Decimal;
import org.ta4j.core.Tick;
import org.ta4j.core.columnar_timeSeries_and_decimal_interface.value_types.BaseDecimal;
import org.ta4j.core.columnar_timeSeries_and_decimal_interface.value_types.DoubleDecimal;
import org.ta4j.core.columnar_timeSeries_and_decimal_interface.value_types.LongDecimal;
import org.ta4j.core.mocks.MockTick;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class InterfaceTest {


    @Test
    public void canDeclareAndInit() {

        // declare and initialize empty time series of two different types
        BaseTimeSeries<LongDecimal> longDecimalBase = new BaseTimeSeries<>(LongDecimal.NUM_OPERATIONS_FACTORY);
        BaseTimeSeries<BaseDecimal> baseDecimalBase = new BaseTimeSeries<>(BaseDecimal.NUM_OPERATIONS_FACTORY);

        // declare and initialize indicators on the time series
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(baseDecimalBase);
        SMAIndicator smaIndicator = new SMAIndicator(closePriceIndicator, 10);

        ClosePriceIndicator closePriceIndicatorLong = new ClosePriceIndicator(longDecimalBase);
        SMAIndicator smaIndicatorLong = new SMAIndicator(closePriceIndicator, 10);
    }

    @Test
    public void compareStructureAndDecimalImplementations(){
        int capacity = 54 * 5 * 24 * 60 * 3;
        int upto = 30; // timeFrame SMA from 2 to 30

        // Standard BigDecimal, POJO structure
        List<Tick> inputOld = getTickInput(capacity);
        org.ta4j.core.TimeSeries oldTimeSeries = new org.ta4j.core.BaseTimeSeries(inputOld);

        // LongDecimal -> works with Decimal5f
        List<Bar<LongDecimal>> input = getBarInput(capacity, LongDecimal.NUM_OPERATIONS_FACTORY);
        BaseTimeSeries<LongDecimal> longDecimalBase = new BaseTimeSeries<>(input, LongDecimal.NUM_OPERATIONS_FACTORY);

        // BaseDecimal -> works with BigDecimal, column based structure
        List<Bar<BaseDecimal>> input2 = getBarInput(capacity, BaseDecimal.NUM_OPERATIONS_FACTORY);
        BaseTimeSeries<BaseDecimal> baseDecimalBase = new BaseTimeSeries<>(input2, BaseDecimal.NUM_OPERATIONS_FACTORY);

        // DoubleDecimal -> works with Double, column based structure
        List<Bar<DoubleDecimal>> input3 = getBarInput(capacity, DoubleDecimal.NUM_OPERATIONS_FACTORY);
        BaseTimeSeries<DoubleDecimal> baseDoubleBase = new BaseTimeSeries<>(input3, DoubleDecimal.NUM_OPERATIONS_FACTORY);

        for (int i= 3; i <= upto; i++){ // for each SMA(2)-SMA(i) calculate every value and print time
            System.out.println("--------------Test (time frame 2-"+i+")--------------");
            //long time = testNewStructure("LongDecimal",longDecimalBase, i);
            //long time2 = testNewStructure("BaseDecimal", baseDecimalBase, i);
            long time3 = testNewStructure("DoubleDecimal", baseDoubleBase, i);
            long time4 = testOldStructure(oldTimeSeries,i);
        }
    }



    /**
     * Test new structure of time series and decimal interface
     * @param series
     * @param upto
     * @return
     */
    public long testNewStructure(String dataType, TimeSeries series, int upto){
        long start = System.currentTimeMillis();
        Value average = smaCalculations(series,upto);
        long time = System.currentTimeMillis()-start;
        System.out.println(String.format("[Column based %s ] time: %s lastValue: %s", dataType,time, average.toDouble()));
        return time;
    }


    public long testOldStructure(org.ta4j.core.TimeSeries series, int upto){
        long start = System.currentTimeMillis();
        Decimal average = smaCalculations(series, upto);
        long time = System.currentTimeMillis()-start;
        System.out.println(String.format("[Classic POJO             ] time: %s lastValue: %s", time, average.toDouble()));
        return time;
    }

    /**
     * Run calculations for new TimeSeries structure
     * @param series
     * @return
     */
    private Decimal smaCalculations(org.ta4j.core.TimeSeries series, int upTo){
        org.ta4j.core.indicators.helpers.ClosePriceIndicator closePriceIndicator = new org.ta4j.core.indicators.helpers.ClosePriceIndicator(series);
        Decimal average = Decimal.NaN;
        for (int h = 2; h < upTo; h++) {
            org.ta4j.core.indicators.SMAIndicator sma = new org.ta4j.core.indicators.SMAIndicator(closePriceIndicator,h);
            for (int i = 0; i <= series.getEndIndex(); i++) {
                average = sma.getValue(i);
                //System.out.println(average);
            }
        }
        return average;
    }

    /**
     * Run calculations for old TimeSeries structure
     * @param series
     * @return
     */
    private Value smaCalculations(TimeSeries series, int upTo){
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(series);
        Value average = Value.NaN;
        for (int h = 2; h < upTo; h++) {
            SMAIndicator sma = new SMAIndicator(closePriceIndicator,h);
            for (int i = 0; i < series.getEndIndex(); i++) {
                average = sma.getValue(i);
                //System.out.println(average);
            }
        }
        return average;
    }

    private List<Tick> getTickInput(int capacity) {
        int initialCapacity = capacity;
        double[] input = new double[initialCapacity];
        List<Tick> ticks = new ArrayList<>();

        for (int i = 0; i < input.length; i++) {
            Tick b = new MockTick(i,i,i,i,i);
            ticks.add(b);
        }
        return ticks;
    }


    private double[] getInput(int capacity){
        int initialCapacity = capacity;
        double[] input = new double[initialCapacity];

        for (int i = 0; i < input.length; i++) {
            input[i] = i;
        }
        return input;
    }

    private <D extends Value> List<Bar<D>> getBarInput(int capacity, NumOperationsFactory<D> numFac){
        int initialCapacity = capacity;
        double[] input = new double[initialCapacity];
        List<Bar<D>> bars = new ArrayList<>();

        for (int i = 0; i < input.length; i++) {
            Bar b = new BaseBar(Duration.ZERO, ZonedDateTime.now(), numFac.valueOf(i), numFac.valueOf(i),
                    numFac.valueOf(i), numFac.valueOf(i), numFac.valueOf(i), numFac.valueOf(i), numFac);
            bars.add(b);
        }
        return bars;
    }
}
