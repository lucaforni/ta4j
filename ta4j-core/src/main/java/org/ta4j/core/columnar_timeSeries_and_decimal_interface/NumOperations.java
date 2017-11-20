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
package org.ta4j.core.columnar_timeSeries_and_decimal_interface;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @param <D> the delegate of the class that implements NumOperations
 *              See {@link LongDecimal} -> delegate {@link org.decimal4j.immutable.Decimal10f}
 *              See {@link BaseDecimal} -> delegate {@link BigDecimal}
 *
 * @See {@link NumOperationsFactory<D>}
 */
public interface NumOperations<D extends Number> extends Serializable, Comparable<NumOperations> {


    static final NumOperations NaN = null;

    D getValue();

    /**
     * Returns a {@code NumOperations} whose value is {@code (this + augend)},
     * @param augend value to be added to this {@code NumOperations}.
     * @return {@code this + augend}, rounded as necessary
     */
     NumOperations plus(NumOperations augend);

    /**
     * Returns a {@code NumOperations} whose value is {@code (this - augend)},
     * @param subtrahend value to be subtracted from this {@code NumOperations}.
     * @return {@code this - subtrahend}, rounded as necessary
     */
    NumOperations minus(NumOperations subtrahend);

    /**
     * Returns a {@code NumOperations} whose value is {@code this * multiplicand},
     * @param multiplicand value to be multiplied by this {@code NumOperations}.
     * @return {@code this * multiplicand}, rounded as necessary
     */
    NumOperations multipliedBy(NumOperations multiplicand);

    /**
     * Returns a {@code NumOperations} whose value is {@code (this / divisor)},
     * @param divisor value by which this {@code NumOperations} is to be divided.
     * @return {@code this / divisor}, rounded as necessary
     */
    NumOperations dividedBy(NumOperations divisor);

    /**
     * Returns a {@code NumOperations} whose value is {@code (this % divisor)},
     * @param divisor value by which this {@code NumOperations} is to be divided.
     * @return {@code this % divisor}, rounded as necessary.
     */
    NumOperations remainder(NumOperations divisor);

    /**
     * Returns a {@code NumOperations} whose value is <tt>(this<sup>n</sup>)</tt>.
     * @param n power to raise this {@code NumOperations} to.
     * @return <tt>this<sup>n</sup></tt>
     */
    NumOperations pow(int n);

    /**
     * Returns the correctly rounded natural logarithm (base e) of the <code>double</code> value of this {@code NumOperations}.
     * @return the natural logarithm (base e) of {@code this}
     * @see StrictMath#log(double)
     */
    NumOperations log();

    /**
     * Returns a {@code NumOperations} whose value is the absolute value
     * of this {@code NumOperations}.
     * @return {@code abs(this)}
     */
    NumOperations abs();

    /**
     * Checks if the value is zero.
     * @return true if the value is zero, false otherwise
     */
    boolean isZero();

    /**
     * Checks if the value is greater than zero.
     * @return true if the value is greater than zero, false otherwise
     */
    boolean isPositive();

    /**
     * Checks if the value is zero or greater.
     * @return true if the value is zero or greater, false otherwise
     */
    boolean isPositiveOrZero();

    /**
     * Checks if the value is less than zero.
     * @return true if the value is less than zero, false otherwise
     */
    boolean isNegative();

    /**
     * Checks if the value is zero or less.
     * @return true if the value is zero or less, false otherwise
     */
    boolean isNegativeOrZero();

    /**
     * Checks if this value is equal to another.
     * @param other the other value, not null
     * @return true is this is greater than the specified value, false otherwise
     */
    boolean isEqual(NumOperations other);

    /**
     * Checks if this value is greater than another.
     * @param other the other value, not null
     * @return true is this is greater than the specified value, false otherwise
     */
    boolean isGreaterThan(NumOperations other);

    /**
     * Checks if this value is greater than or equal to another.
     * @param other the other value, not null
     * @return true is this is greater than or equal to the specified value, false otherwise
     */
    boolean isGreaterThanOrEqual(NumOperations other);

    /**
     * Checks if this value is less than another.
     * @param other the other value, not null
     * @return true is this is less than the specified value, false otherwise
     */
    boolean isLessThan(NumOperations other);

    /**
     * Returns the minimum of this {@code NumOperations} and {@code other}.
     * @param other value with which the minimum is to be computed
     * @return the {@code NumOperations} whose value is the lesser of this
     *         {@code NumOperations} and {@code other}.  If they are equal,
     *         method, {@code this} is returned.
     */
    NumOperations min(NumOperations other);

    /**
     * Returns the maximum of this {@code NumOperations} and {@code other}.
     * @param  other value with which the maximum is to be computed
     * @return the {@code NumOperations} whose value is the greater of this
     *         {@code NumOperations} and {@code other}.  If they are equal,
     *         method, {@code this} is returned.
     */
    NumOperations max(NumOperations other);

    /**
     * Converts this {@code NumOperations} to a {@code double}.
     * @return this {@code NumOperations} converted to a {@code double}
     * @see BigDecimal#doubleValue()
     */
    double toDouble();

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * {@inheritDoc}
     * @apiNote: This method should return true if `this` and `obj` are both NaN.
     */
    @Override
    boolean equals(Object obj);
}
