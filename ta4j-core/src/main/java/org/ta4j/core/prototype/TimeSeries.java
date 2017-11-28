package org.ta4j.core.prototype;

import org.ta4j.core.Tick;
import org.ta4j.core.prototype.num.Num;
import org.ta4j.core.prototype.num.NumFactory;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Time series interface
 */
public interface TimeSeries<D extends Num> {


    NumFactory<D> getNumFactory();
    public D getOpenPrice(int index);
    public D getMinPrice(int index);
    public D getMaxPrice(int index);
    public D getClosePrice(int index);
    public D getAmount(int index);
    public D getVolume(int index);
    public int getTrades(int index);
    public Duration getDuration(int index);
    public ZonedDateTime getBeginTime(int index);
    public ZonedDateTime getEndTime(int index);
    //TODO: extract further functions from AbstractTimeSeries

    /**
     * @return the name of the series
     */
    String getName();

    /**
     * @param i an index
     * @return the tick at the i-th position
     */
    Bar<D> getBar(int i);

    /**
     * @return the first tick of the series
     */
    default Bar<D> getFirstBar() {
        return getBar(getBeginIndex());
    }

    /**
     * @return the last tick of the series
     */
    default Bar<D> getLastBar() {
        return getBar(getEndIndex());
    }

    /**
     * @return the number of ticks in the series
     */
    int getBarCount();

    boolean isWindow();
    /**
     * @return true if the series is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Warning: should be used carefully!
     * <p>
     * Returns the raw tick data.
     * It means that it returns the current List object used internally to store the {@link Tick ticks}.
     * It may be:
     *   - a shortened tick list if a maximum tick count has been set
     *   - a extended tick list if it is a constrained time series
     * @return the raw tick data
     * @deprecated use streams if possible
     */
    List<Bar<D>> getBarData();

    /**
     * @return the begin index of the series
     */
    int getBeginIndex();

    /**
     * @return the end index of the series
     */
    int getEndIndex();

    /**
     * @return the description of the series period (e.g. "from 12:00 21/01/2014 to 12:15 21/01/2014")
     */
    default String getSeriesPeriodDescription() {
        StringBuilder sb = new StringBuilder();
        if (!getBarData().isEmpty()) {
            Bar firstTick = getFirstBar();
            Bar lastTick = getLastBar();
            sb.append(firstTick.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME))
                    .append(" - ")
                    .append(lastTick.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        return sb.toString();
    }

    /**
     * @return the number of removed ticks
     */
    int getRemovedBarCount();

    /**
     * Adds a tick at the end of the series.
     * <p>
     * Begin index set to 0 if if wasn't initialized.<br>
     * End index set to 0 if if wasn't initialized, or incremented if it matches the end of the series.<br>
     * Exceeding ticks are removed.
     * @param bar the tick to be added
     * @see org.ta4j.core.TimeSeries#setMaximumTickCount(int)
     * @deprecated use the addBar(..., ..., ...) function to add direct tick data to the time series
     */
    void addBar(Bar<D> bar);

    /**
     *
     */
    default int getMaximumBarCount(){
        return Integer.MAX_VALUE;
    }
}
