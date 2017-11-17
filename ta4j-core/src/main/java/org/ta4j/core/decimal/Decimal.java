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
package org.ta4j.core.decimal;

import java.io.Serializable;
import java.math.BigDecimal;

public interface Decimal extends Serializable, Comparable<Decimal> {

    Decimal NaN = null;


    Decimal get(double val);

    /**
     * Returns a {@code Decimal} whose value is {@code (this + augend)},
     * @param augend value to be added to this {@code Decimal}.
     * @return {@code this + augend}, rounded as necessary
     */
     Decimal plus(Decimal augend);

    /**
     * Returns a {@code Decimal} whose value is {@code (this - augend)},
     * @param subtrahend value to be subtracted from this {@code Decimal}.
     * @return {@code this - subtrahend}, rounded as necessary
     */
    Decimal minus(Decimal subtrahend);

    /**
     * Returns a {@code Decimal} whose value is {@code this * multiplicand},
     * @param multiplicand value to be multiplied by this {@code Decimal}.
     * @return {@code this * multiplicand}, rounded as necessary
     */
    Decimal multipliedBy(Decimal multiplicand);

    /**
     * Returns a {@code Decimal} whose value is {@code (this / divisor)},
     * @param divisor value by which this {@code Decimal} is to be divided.
     * @return {@code this / divisor}, rounded as necessary
     */
    public abstract Decimal dividedBy(Decimal divisor);

    /**
     * Returns a {@code Decimal} whose value is {@code (this % divisor)},
     * @param divisor value by which this {@code Decimal} is to be divided.
     * @return {@code this % divisor}, rounded as necessary.
     */
    public abstract Decimal remainder(Decimal divisor);

    /**
     * Returns a {@code Decimal} whose value is <tt>(this<sup>n</sup>)</tt>.
     * @param n power to raise this {@code Decimal} to.
     * @return <tt>this<sup>n</sup></tt>
     */
    public abstract Decimal pow(int n);

    /**
     * Returns the correctly rounded natural logarithm (base e) of the <code>double</code> value of this {@code Decimal}.
     * @return the natural logarithm (base e) of {@code this}
     * @see StrictMath#log(double)
     */
    public abstract Decimal log();

    /**
     * Returns a {@code Decimal} whose value is the absolute value
     * of this {@code Decimal}.
     * @return {@code abs(this)}
     */
    public abstract Decimal abs();

    /**
     * Checks if the value is zero.
     * @return true if the value is zero, false otherwise
     */
    public abstract boolean isZero();

    /**
     * Checks if the value is greater than zero.
     * @return true if the value is greater than zero, false otherwise
     */
    public abstract boolean isPositive();

    /**
     * Checks if the value is zero or greater.
     * @return true if the value is zero or greater, false otherwise
     */
    public abstract boolean isPositiveOrZero();

    /**
     * Checks if the value is less than zero.
     * @return true if the value is less than zero, false otherwise
     */
    public abstract boolean isNegative();

    /**
     * Checks if the value is zero or less.
     * @return true if the value is zero or less, false otherwise
     */
    public abstract boolean isNegativeOrZero();

    /**
     * Checks if this value is equal to another.
     * @param other the other value, not null
     * @return true is this is greater than the specified value, false otherwise
     */
    public abstract boolean isEqual(Decimal other);

    /**
     * Checks if this value is greater than another.
     * @param other the other value, not null
     * @return true is this is greater than the specified value, false otherwise
     */
    public abstract boolean isGreaterThan(Decimal other);

    /**
     * Checks if this value is greater than or equal to another.
     * @param other the other value, not null
     * @return true is this is greater than or equal to the specified value, false otherwise
     */
    public abstract boolean isGreaterThanOrEqual(Decimal other);

    /**
     * Checks if this value is less than another.
     * @param other the other value, not null
     * @return true is this is less than the specified value, false otherwise
     */
    public abstract boolean isLessThan(Decimal other);

    /**
     * Returns the minimum of this {@code Decimal} and {@code other}.
     * @param other value with which the minimum is to be computed
     * @return the {@code Decimal} whose value is the lesser of this
     *         {@code Decimal} and {@code other}.  If they are equal,
     *         method, {@code this} is returned.
     */
    public abstract Decimal min(Decimal other);

    /**
     * Returns the maximum of this {@code Decimal} and {@code other}.
     * @param  other value with which the maximum is to be computed
     * @return the {@code Decimal} whose value is the greater of this
     *         {@code Decimal} and {@code other}.  If they are equal,
     *         method, {@code this} is returned.
     */
    public abstract Decimal max(Decimal other);

    /**
     * Converts this {@code Decimal} to a {@code double}.
     * @return this {@code Decimal} converted to a {@code double}
     * @see BigDecimal#doubleValue()
     */
    public abstract double toDouble();

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    /**
     * {@inheritDoc}
     * @apiNote: This method should return true if `this` and `obj` are both NaN.
     */
    @Override
    public abstract boolean equals(Object obj);
}
