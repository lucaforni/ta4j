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
package org.ta4j.core.prototype.num;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @param <D> the delegate of the class that implements num
 *              See {@link Decimal10fNum Decimal10fNum} -> delegate: {@link org.decimal4j.immutable.Decimal10f Decimal10f}
 *              See {@link BigDecimalNum BigDecimalNum} -> delegate: {@link BigDecimal BigDecimal}
 *              See {@link org.ta4j.core.prototype.num.DoubleNum DoubleNum}   -> delegate: Double
 *
 * @See {@link NumFactory <D>}
 */
public interface Num<D extends Number> extends Serializable, Comparable<Num> {


    static final Num NaN = null;

    /**
     * Get a 'Factory' for the underlying delegate type
     * @return NumFactory object (for using valueOf transformations)
     */
    NumFactory getFactory();

    /**
     * Get the delegate of the num implementation
     * @return the delegate value
     */
    D getDelegate();

    /**
     * Get the name of the num implementation
     * @return name of the num implementation
     */
    String getName();

    /**
     * Returns a {@code num} whose value is {@code (this + augend)},
     * @param augend value to be added to this {@code num}.
     * @return {@code this + augend}, rounded as necessary
     */
     Num plus(Num augend);

    /**
     * Returns a {@code num} whose value is {@code (this - augend)},
     * @param subtrahend value to be subtracted from this {@code num}.
     * @return {@code this - subtrahend}, rounded as necessary
     */
    Num minus(Num subtrahend);

    /**
     * Returns a {@code num} whose value is {@code this * multiplicand},
     * @param multiplicand value to be multiplied by this {@code num}.
     * @return {@code this * multiplicand}, rounded as necessary
     */
    Num multipliedBy(Num multiplicand);

    /**
     * Returns a {@code num} whose value is {@code (this / divisor)},
     * @param divisor value by which this {@code num} is to be divided.
     * @return {@code this / divisor}, rounded as necessary
     */
    Num dividedBy(Num divisor);

    /**
     * Returns a {@code num} whose value is {@code (this % divisor)},
     * @param divisor value by which this {@code num} is to be divided.
     * @return {@code this % divisor}, rounded as necessary.
     */
    Num remainder(Num divisor);

    /**
     * Returns a {@code num} whose value is <tt>(this<sup>n</sup>)</tt>.
     * @param n power to raise this {@code num} to.
     * @return <tt>this<sup>n</sup></tt>
     */
    Num pow(int n);

    /**
     * Returns the correctly rounded natural logarithm (base e) of the <code>double</code> value of this {@code num}.
     * @return the natural logarithm (base e) of {@code this}
     * @see StrictMath#log(double)
     */
    Num log();

    /**
     * Returns a {@code num} whose value is the absolute value
     * of this {@code num}.
     * @return {@code abs(this)}
     */
    Num abs();

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
    boolean isEqual(Num other);

    /**
     * Checks if this value is greater than another.
     * @param other the other value, not null
     * @return true is this is greater than the specified value, false otherwise
     */
    boolean isGreaterThan(Num other);

    /**
     * Checks if this value is greater than or equal to another.
     * @param other the other value, not null
     * @return true is this is greater than or equal to the specified value, false otherwise
     */
    boolean isGreaterThanOrEqual(Num other);

    /**
     * Checks if this value is less than another.
     * @param other the other value, not null
     * @return true is this is less than the specified value, false otherwise
     */
    boolean isLessThan(Num other);

    /**
     * Checks if this value is less than another.
     * @param other the other value, not null
     * @return true is this is less than or equal the specified value, false otherwise
     */
    boolean isLessThanOrEqual(Num other);

    /**
     * Returns the minimum of this {@code num} and {@code other}.
     * @param other value with which the minimum is to be computed
     * @return the {@code num} whose value is the lesser of this
     *         {@code num} and {@code other}.  If they are equal,
     *         method, {@code this} is returned.
     */
    Num min(Num other);

    /**
     * Returns the maximum of this {@code num} and {@code other}.
     * @param  other value with which the maximum is to be computed
     * @return the {@code num} whose value is the greater of this
     *         {@code num} and {@code other}.  If they are equal,
     *         method, {@code this} is returned.
     */
    Num max(Num other);

    /**
     * Converts this {@code num} to a {@code double}.
     * @return this {@code num} converted to a {@code double}
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
