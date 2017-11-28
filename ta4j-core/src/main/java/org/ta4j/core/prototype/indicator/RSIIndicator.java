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
package org.ta4j.core.prototype.indicator;


import org.ta4j.core.prototype.num.Num;
import org.ta4j.core.prototype.num.NumFactory;

/**
 * Relative strength index indicator.
 * <p></p>
 * This calculation of RSI uses traditional moving averages
 * as opposed to Wilder's accumulative moving average technique.
 *
 * @see <a href="https://www.barchart.com/education/technical-indicators#/studies/std_rsi_mod">
 * RSI calculation</a>.
 *
 */
public class RSIIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> averageGainIndicator;
    private final Indicator<Num> averageLossIndicator;
    private final NumFactory<Num> numFa = getNumFactory();
    
    public RSIIndicator(Indicator<Num> indicator, int timeFrame) {
        this(new AverageGainIndicator(indicator, timeFrame),
                new AverageLossIndicator(indicator, timeFrame));
    }

    public RSIIndicator(Indicator<Num> avgGainIndicator, Indicator<Num> avgLossIndicator) {
        super(avgGainIndicator);
        averageGainIndicator = avgGainIndicator;
        averageLossIndicator = avgLossIndicator;
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return numFa.valueOf(0);
        }

        // Relative strength
        Num averageLoss = averageLossIndicator.getValue(index);
        if (averageLoss.isZero()) {
            return numFa.valueOf(100);
        }
        Num averageGain = averageGainIndicator.getValue(index);
        Num relativeStrength = averageGain.dividedBy(averageLoss);

        // Nominal case
        Num ratio = numFa.valueOf(100).dividedBy(numFa.valueOf(1).plus(relativeStrength));
        return numFa.valueOf(100).minus(ratio);
    }

}
