package org.ta4j.core.prototype;

import org.ta4j.core.prototype.num.BigDecimalNum;
import org.ta4j.core.prototype.num.Decimal10fNum;
import org.ta4j.core.prototype.num.DoubleNum;

/**
 * Should enable an easy creation of {@link ColumnarTimeSeries TimeSeries} with the corresponding {@link num class}
 */
public class TimeSeriesFactory {

    public enum delegate{
        BigDecimal,
        Double,
        Decimal10f
    }

    /**
     *
     * @param delegate
     * @return TimeSeries with initial capacity of 1000
     */
    public static ColumnarTimeSeries create(TimeSeriesFactory.delegate delegate){
        switch (delegate){
            case Double:
                return new ColumnarTimeSeries<>(DoubleNum.NUM_OPERATIONS_FACTORY);
            case Decimal10f:
                return new ColumnarTimeSeries<>(Decimal10fNum.NUM_OPERATIONS_FACTORY);
            default:
                return new ColumnarTimeSeries<>(BigDecimalNum.NUM_OPERATIONS_FACTORY);
        }
    }

    public static ColumnarTimeSeries create(int capacity, TimeSeriesFactory.delegate delegate){
        switch (delegate){
            case Double:
                return new ColumnarTimeSeries<>(capacity, DoubleNum.NUM_OPERATIONS_FACTORY);
            case Decimal10f:
                return new ColumnarTimeSeries<>(capacity, Decimal10fNum.NUM_OPERATIONS_FACTORY);
            default:
                return new ColumnarTimeSeries<>(capacity, BigDecimalNum.NUM_OPERATIONS_FACTORY);
        }
    }

    public static ColumnarTimeSeries create(int capacity, boolean isWindow, TimeSeriesFactory.delegate delegate){
        switch (delegate){
            case Double:
                return new ColumnarTimeSeries<>(capacity, isWindow, DoubleNum.NUM_OPERATIONS_FACTORY);
            case Decimal10f:
                return new ColumnarTimeSeries<>(capacity, isWindow, Decimal10fNum.NUM_OPERATIONS_FACTORY);
            default:
                return new ColumnarTimeSeries<>(capacity, isWindow, BigDecimalNum.NUM_OPERATIONS_FACTORY);
        }
    }
}
