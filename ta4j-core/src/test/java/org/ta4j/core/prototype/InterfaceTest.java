package org.ta4j.core.prototype;
import org.junit.Test;
import org.ta4j.core.Decimal;
import org.ta4j.core.Tick;
import org.ta4j.core.prototype.indicator.ClosePriceIndicator;
import org.ta4j.core.prototype.indicator.SMAIndicator;
import org.ta4j.core.prototype.num.*;
import org.ta4j.core.mocks.MockTick;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.ta4j.core.prototype.TimeSeriesFactory.delegate.BigDecimal;
import static org.ta4j.core.prototype.TimeSeriesFactory.delegate.Decimal10f;


public class InterfaceTest {


    @Test
    public void canDeclareAndInit() {

        // declare and initialize empty time series of two different types
        ColumnarTimeSeries decimal10fBase = TimeSeriesFactory.create(Decimal10f);
        ColumnarTimeSeries bigDecimalBase = TimeSeriesFactory.create(BigDecimal);

        // declare and initialize indicators on the time series
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(bigDecimalBase);
        SMAIndicator smaIndicator = new SMAIndicator(closePriceIndicator, 10);

        ClosePriceIndicator closePriceIndicatorLong = new ClosePriceIndicator(decimal10fBase);
        SMAIndicator smaIndicatorLong = new SMAIndicator(closePriceIndicator, 10);
    }

    @Test
    public void compareStructureAndDecimalImplementations(){
        int capacity = 54 * 5 * 24 * 60 * 3;
        int upto = 30; // timeFrame SMA from 2 to 30

        // Standard BigDecimal, POJO structure
        List<Tick> inputOld = getTickInput(capacity);
        org.ta4j.core.TimeSeries oldTimeSeries = new org.ta4j.core.BaseTimeSeries(inputOld);

        // Decimal10fNum -> works with Decimal5f
        List<Bar<Decimal10fNum>> input = getBarInput(capacity, Decimal10fNum.NUM_OPERATIONS_FACTORY);
        ColumnarTimeSeries<Decimal10fNum> longDecimalBase = TimeSeriesFactory.create(capacity,Decimal10f);
        longDecimalBase.addBars(input);

        // BigDecimalNum -> works with BigDecimal, column based structure
        List<Bar<BigDecimalNum>> input2 = getBarInput(capacity, BigDecimalNum.NUM_OPERATIONS_FACTORY);
        ColumnarTimeSeries<BigDecimalNum> baseDecimalBase = TimeSeriesFactory.create(capacity,BigDecimal);
        baseDecimalBase.addBars(input2);

        // DoubleNum -> works with Double, column based structure
        List<Bar<DoubleNum>> input3 = getBarInput(capacity, DoubleNum.NUM_OPERATIONS_FACTORY);
        ColumnarTimeSeries<DoubleNum> baseDoubleBase = TimeSeriesFactory.create(capacity,TimeSeriesFactory.delegate.Double);
        baseDoubleBase.addBars(input3);

        for (int i= 3; i <= upto; i++){ // for each SMA(2)-SMA(i) calculate every value and print time
            System.out.println("--------------Test (time frame 2-"+i+")--------------");
            long time = testNewStructure("Decimal10fNum",longDecimalBase, i);
            long time2 = testNewStructure("BigDecimalNum", baseDecimalBase, i);
            long time3 = testNewStructure("DoubleNum", baseDoubleBase, i);
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
        Num average = smaCalculations(series,upto);
        long time = System.currentTimeMillis()-start;
        System.out.println(String.format("[Column based %s     ] time: %s lastValue: %s", dataType,time, average.toDouble()));
        return time;
    }


    public long testOldStructure(org.ta4j.core.TimeSeries series, int upto){
        long start = System.currentTimeMillis();
        Decimal average = smaCalculations(series, upto);
        long time = System.currentTimeMillis()-start;
        System.out.println(String.format("[Classic POJO               ] time: %s lastValue: %s", time, average.toDouble()));
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
    private Num smaCalculations(TimeSeries series, int upTo){
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(series);
        Num average = Num.NaN;
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

    private <D extends Num> List<Bar<D>> getBarInput(int capacity, NumFactory<D> numFac) {
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
/*
    @Test
    public void floatingPointVsFixPoint(){
        NumFactory dFac = DoubleNum.NUM_OPERATIONS_FACTORY;
        NumFactory bFac = BigDecimalNum.NUM_OPERATIONS_FACTORY;

        TimeSeries<DoubleNum> tsDouble = new BaseTimeSeries<DoubleNum>(dFac);
        TimeSeries<BigDecimalNum> tsBigDecimal = new BaseTimeSeries<BigDecimalNum>(bFac);

        tsDouble.addBar(new BaseBar<DoubleNum>(ZonedDateTime.now(),.9999,.9999,.9999,.9999,.9999,dFac));
        tsDouble.addBar(new BaseBar<DoubleNum>(ZonedDateTime.now(),.9999,.9999,.9999,.9999,.9999,dFac));
        tsDouble.addBar(new BaseBar<DoubleNum>(ZonedDateTime.now(),.9999,.9999,.9999,.9999,.9999,dFac));
        tsDouble.addBar(new BaseBar<DoubleNum>(ZonedDateTime.now(),.9999,.9999,.9999,.9999,.9999,dFac));
        tsDouble.addBar(new BaseBar<DoubleNum>(ZonedDateTime.now(),.9999,.9999,.9999,.9999,.9999,dFac));
        tsDouble.addBar(new BaseBar<DoubleNum>(ZonedDateTime.now(),.9999,.9999,.9999,.9999,.9999,dFac));
        tsDouble.addBar(new BaseBar<DoubleNum>(ZonedDateTime.now(),.9999,.9999,.9999,.9999,.9999,dFac));
        tsDouble.addBar(new BaseBar<DoubleNum>(ZonedDateTime.now(),.9999,.9999,.9999,.9999,.9999,dFac));

    }
    */
}
