/*
  The MIT License (MIT)

  Copyright (c) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)

  Permission is hereby granted, free of charge, to any person obtaining a copy of
  this software and associated documentation files (the "Software"), to deal in
  the Software without restriction, including without limitation the rights to
  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
  the Software, and to permit persons to whom the Software is furnished to do so,
  subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.prototype;

import org.ta4j.core.prototype.num.Num;
import org.ta4j.core.prototype.num.NumFactory;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Base implementation of a {@link Bar}.
 * <p/>
 */
public class BaseBar<D extends Num> implements Bar<D> {

	private static final long serialVersionUID = 8038383777467488147L;

	private final NumFactory<D> factory;
	/** Time period (e.g. 1 day, 15 min, etc.) of the tick */
    private Duration timePeriod;
    /** End time of the tick */
    private ZonedDateTime endTime;
    /** Begin time of the tick */
    private ZonedDateTime beginTime;
    /** Open price of the period */
    private D openPrice;
    /** Close price of the period */
    private D closePrice;
    /** Max price of the period */
    private D maxPrice;
    /** Min price of the period */
    private D minPrice;
    /** Traded amount during the period */
    private D amount;
    /** Volume of the period */
    private D volume;
    /** Trade count */
    private int trades;

    /**
     * Constructor.
     * @param timePeriod the time period
     * @param endTime the end time of the tick period
     */
    public BaseBar(Duration timePeriod, ZonedDateTime endTime, NumFactory<D> factory) {
        checkTimeArguments(timePeriod, endTime);
        this.timePeriod = timePeriod;
        this.endTime = endTime;
        this.beginTime = endTime.minus(timePeriod);
        this.factory = factory;
        this.trades = 0;
        this.volume = factory.valueOf(0);
        this.amount = factory.valueOf(0);
    }

    /**
     * Constructor.
     * @param endTime the end time of the tick period
     * @param openPrice the open price of the tick period
     * @param highPrice the highest price of the tick period
     * @param lowPrice the lowest price of the tick period
     * @param closePrice the close price of the tick period
     * @param volume the volume of the tick period
     */
    public BaseBar(ZonedDateTime endTime, double openPrice, double highPrice, double lowPrice, double closePrice, double volume, NumFactory<D> factory) {
        this(endTime,
                factory.valueOf(openPrice),
                factory.valueOf(highPrice),
                factory.valueOf(lowPrice),
                factory.valueOf(closePrice),
                factory.valueOf(volume),
                factory);
    }

    /**
     * Constructor.num
     * @param endTime the end time of the tick period
     * @param openPrice the open price of the tick period
     * @param highPrice the highest price of the tick period
     * @param lowPrice the lowest price of the tick period
     * @param closePrice the close price of the tick period
     * @param volume the volume of the tick period
     */
    public BaseBar(ZonedDateTime endTime, String openPrice, String highPrice, String lowPrice, String closePrice, String volume, NumFactory<D> factory) {
        this(endTime, factory.valueOf(openPrice),
                factory.valueOf(highPrice),
                factory.valueOf(lowPrice),
                factory.valueOf(closePrice),
                factory.valueOf(volume), factory);
    }

    /**
     * Constructor.
     * @param endTime the end time of the tick period
     * @param openPrice the open price of the tick period
     * @param highPrice the highest price of the tick period
     * @param lowPrice the lowest price of the tick period
     * @param closePrice the close price of the tick period
     * @param volume the volume of the tick period
     */
    public BaseBar(ZonedDateTime endTime, D openPrice, D highPrice, D lowPrice, D closePrice, D volume, NumFactory<D> factory) {
        this(Duration.ofDays(1), endTime, openPrice, highPrice, lowPrice, closePrice, volume, factory);
    }

    /**
     * Constructor.
     * @param timePeriod the time period
     * @param endTime the end time of the tick period
     * @param openPrice the open price of the tick period
     * @param highPrice the highest price of the tick period
     * @param lowPrice the lowest price of the tick period
     * @param closePrice the close price of the tick period
     * @param volume the volume of the tick period
     */
    public BaseBar(Duration timePeriod, ZonedDateTime endTime, D openPrice, D highPrice, D lowPrice, D closePrice, D volume, NumFactory<D> factory) {
        this(timePeriod, endTime, openPrice, highPrice, lowPrice, closePrice, volume, factory.valueOf(0), factory);
    }

    /**
     * Constructor.
     * @param timePeriod the time period
     * @param endTime the end time of the tick period
     * @param openPrice the open price of the tick period
     * @param highPrice the highest price of the tick period
     * @param lowPrice the lowest price of the tick period
     * @param closePrice the close price of the tick period
     * @param volume the volume of the tick period
     * @param amount the amount of the tick period
     */
    public BaseBar(Duration timePeriod, ZonedDateTime endTime, D openPrice, D highPrice, D lowPrice, D closePrice, D volume, D amount, NumFactory<D> factory) {
        checkTimeArguments(timePeriod, endTime);
        this.timePeriod = timePeriod;
        this.endTime = endTime;
        this.beginTime = endTime.minus(timePeriod);
        this.openPrice = openPrice;
        this.maxPrice = highPrice;
        this.minPrice = lowPrice;
        this.closePrice = closePrice;
        this.volume = volume;
        this.amount = amount;
        this.factory = factory;
    }

    /**
     * @return the open price of the period
     */
    public D getOpenPrice() {
        return openPrice;
    }


    public D getMinPrice() {
        return minPrice;
    }

    /**
     * @return the max price of the period
     */
    public D getMaxPrice() {
        return maxPrice;
    }

    /**
     * @return the close price of the period
     */
    public D getClosePrice() {
        return closePrice;
    }

    /**
     * @return the whole traded volume in the period
     */
    public D getVolume() {
        return volume;
    }

    /**
     * @return the number of trades in the period
     */
    public int getTrades() {
        return trades;
    }

    /**
     * @return the whole traded amount of the period
     */
    public D getAmount() {
        return amount;
    }

    /**
     * @return the time period of the tick
     */
    public Duration getTimePeriod() {
        return timePeriod;
    }

    /**
     * @return the begin timestamp of the tick period
     */
    public ZonedDateTime getBeginTime() {
        return beginTime;
    }

    /**
     * @return the end timestamp of the tick period
     */
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    @Override
    public void addTrade(double tradeVolume, double tradePrice) {

    }

    @Override
    public void addTrade(String tradeVolume, String tradePrice) {

    }


    /**    @Override
    public void addTrade(num tradeVolume, num tradePrice) {

    }
     * Adds a trade at the end of tick period.
     * @param tradeVolume the traded volume
     * @param tradePrice the price
     */
    @Override
    public void addTrade(D tradeVolume, D tradePrice) {
        if (openPrice == null) {
            openPrice = tradePrice;
        }
        closePrice = tradePrice;

        if (maxPrice == null) {
            maxPrice = tradePrice;
        } else {
            maxPrice = maxPrice.isLessThan(tradePrice) ? tradePrice : maxPrice;
        }
        if (minPrice == null) {
            minPrice = tradePrice;
        } else {
            minPrice = minPrice.isGreaterThan(tradePrice) ? tradePrice : minPrice;
        }
        volume = (D) volume.plus(tradeVolume);
        amount = (D) amount.plus(tradeVolume.multipliedBy(tradePrice));
        trades++;
    }

    @Override
    public String toString() {
        return String.format("{end time: %1s, close price: %2$f, open price: %3$f, min price: %4$f, max price: %5$f, volume: %6$f}",
                endTime.withZoneSameInstant(ZoneId.systemDefault()), closePrice.toDouble(), openPrice.toDouble(), minPrice.toDouble(), maxPrice.toDouble(), volume.toDouble());
    }

    /**
     * @param timePeriod the time period
     * @param endTime the end time of the tick
     * @throws IllegalArgumentException if one of the arguments is null
     */
    private void checkTimeArguments(Duration timePeriod, ZonedDateTime endTime) {
        if (timePeriod == null) {
            throw new IllegalArgumentException("Time period cannot be null");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
    }
}
