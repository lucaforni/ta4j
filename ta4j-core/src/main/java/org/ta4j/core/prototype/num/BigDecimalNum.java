package org.ta4j.core.prototype.num;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable, arbitrary-precision signed prototype numbers designed for technical analysis.
 * <p></p>
 * A {@code num} consists of a {@code BigDecimal} with arbitrary {@link MathContext} (precision and rounding mode).
 *
 * @see BigDecimal
 * @see MathContext
 * @see RoundingMode
 */
public final class BigDecimalNum implements Num<BigDecimal> {

    private static final long serialVersionUID = 2225130444465033658L;

    public static final MathContext MATH_CONTEXT = new MathContext(32, RoundingMode.HALF_UP);

    private final BigDecimal delegate;

    public static final NumFactory<BigDecimalNum> NUM_OPERATIONS_FACTORY = new NumFactory<BigDecimalNum>() {
        @Override
        public BigDecimalNum ZERO() {
            return new BigDecimalNum(0);
        }

        @Override
        public BigDecimalNum ONE() {
            return new BigDecimalNum(1);
        }

        @Override
        public BigDecimalNum TWO() {
            return new BigDecimalNum(2);
        }

        @Override
        public BigDecimalNum THREE() {
            return new BigDecimalNum(3);
        }

        @Override
        public BigDecimalNum TEN() { return new BigDecimalNum(10); }

        @Override
        public BigDecimalNum HUNDRED() {
            return new BigDecimalNum(100);
        }

        @Override
        public BigDecimalNum THOUSAND() {
            return new BigDecimalNum(1000);
        }

        @Override
        public Num NaN() {
            return Num.NaN;
        }

        @Override
        public BigDecimalNum valueOf(double val) {
            return new BigDecimalNum(val);
        }

        @Override
        public BigDecimalNum valueOf(int val) {
            return new BigDecimalNum(val);
        }

        @Override
        public BigDecimalNum valueOf(long val) {
            return new BigDecimalNum(val);
        }

        @Override
        public BigDecimalNum valueOf(String val) {
            return new BigDecimalNum(val);
        }
    };

    /**
     * Constructor.
     * @param val the string representation of the prototype value
     */
    private BigDecimalNum(String val) {
        delegate = new BigDecimal(val, MATH_CONTEXT);
    }

    /**
     * Constructor.
     * @param val the double value
     */
    private BigDecimalNum(double val) {
        delegate = new BigDecimal(val, MATH_CONTEXT);
    }

    private BigDecimalNum(int val) {
        delegate = new BigDecimal(val, MATH_CONTEXT);
    }

    private BigDecimalNum(long val) {
        delegate = new BigDecimal(val, MATH_CONTEXT);
    }

    private BigDecimalNum(BigDecimal val) {
        delegate = val;
    }


    @Override
    public NumFactory getFactory() {
        return NUM_OPERATIONS_FACTORY;
    }

    @Override
    public BigDecimal getDelegate() {
        return delegate;
    }

    @Override
    public String getName() {
        return "BigDecimalNum";
    }

    /**
     * Returns a {@code num} whose value is {@code (this + augend)},
     * with rounding according to the context settings.
     * @param augend value to be added to this {@code num}.
     * @return {@code this + augend}, rounded as necessary
     * @see BigDecimal#add(java.math.BigDecimal, java.math.MathContext)
     */
    public Num plus(Num augend) {
        if(!(augend instanceof BigDecimalNum)){
            throw new IllegalArgumentException("Instance of BigDecimalNum is needed!");
        }
        if ((this == Num.NaN) || (augend == NaN)) {
            return NaN;
        }
        final BigDecimalNum baseReplicant = (BigDecimalNum) augend;
        return new BigDecimalNum(delegate.add(baseReplicant.delegate, MATH_CONTEXT));
    }
    /**
     * Returns a {@code num} whose value is {@code (this - augend)},
     * with rounding according to the context settings.
     * @param subtrahend value to be subtracted from this {@code num}.
     * @return {@code this - subtrahend}, rounded as necessary
     * @see BigDecimal#subtract(java.math.BigDecimal, java.math.MathContext)
     */
    public Num minus(Num subtrahend) {
        if(!(subtrahend instanceof BigDecimalNum)){
            throw new IllegalArgumentException("Instance of BigDecimalNum is needed!");
        }
        if ((this == NaN) || (subtrahend == NaN)) {
            return NaN;
        }
        final BigDecimalNum baseReplicant = (BigDecimalNum) subtrahend;
        return new BigDecimalNum(delegate.subtract(baseReplicant.delegate, MATH_CONTEXT));
    }

    /**
     * Returns a {@code num} whose value is {@code this * multiplicand},
     * with rounding according to the context settings.
     * @param multiplicand value to be multiplied by this {@code num}.
     * @return {@code this * multiplicand}, rounded as necessary
     * @see BigDecimal#multiply(java.math.BigDecimal, java.math.MathContext)
     */
    public Num multipliedBy(Num multiplicand) {
        if(!(multiplicand instanceof BigDecimalNum)){
            throw new IllegalArgumentException("Instance of BigDecimalNum is needed!");
        }
        if ((this == NaN) || (multiplicand == NaN)) {
            return NaN;
        }
        final BigDecimalNum baseReplicant = (BigDecimalNum) multiplicand;
        return new BigDecimalNum(delegate.multiply(baseReplicant.delegate, MATH_CONTEXT));
    }

    /**
     * Returns a {@code num} whose value is {@code (this / divisor)},
     * with rounding according to the context settings.
     * @param divisor value by which this {@code num} is to be divided.
     * @return {@code this / divisor}, rounded as necessary
     * @see BigDecimal#divide(java.math.BigDecimal, java.math.MathContext)
     */
    public Num dividedBy(Num divisor) {
        if(!(divisor instanceof BigDecimalNum)){
            throw new IllegalArgumentException("Instance of BigDecimalNum is needed!");
        }
        if ((this == NaN) || (divisor == NaN) || divisor.isZero()) {
            return NaN;
        }

        final BigDecimalNum baseDivisor = (BigDecimalNum) divisor;
        return new BigDecimalNum(delegate.divide(baseDivisor.delegate, MATH_CONTEXT));
    }

    /**
     * Returns a {@code num} whose value is {@code (this % divisor)},
     * with rounding according to the context settings.
     * @param divisor value by which this {@code num} is to be divided.
     * @return {@code this % divisor}, rounded as necessary.
     * @see BigDecimal#remainder(java.math.BigDecimal, java.math.MathContext)
     */
    public Num remainder(Num divisor) {
        if(!(divisor instanceof BigDecimalNum)){
            throw new IllegalArgumentException("Instance of BigDecimalNum is needed!");
        }
        if ((this == NaN) || (divisor == NaN) || divisor.isZero()) {
            return NaN;
        }

        BigDecimalNum baseDivisor = (BigDecimalNum) divisor;
        return new BigDecimalNum(delegate.remainder(baseDivisor.delegate, MATH_CONTEXT));
    }


    /**
     * Returns a {@code num} whose value is <tt>(this<sup>n</sup>)</tt>.
     * @param n power to raise this {@code num} to.
     * @return <tt>this<sup>n</sup></tt>
     * @see BigDecimal#pow(int, java.math.MathContext)
     */
    public Num pow(int n) {
        if (this == NaN) {
            return NaN;
        }
        return new BigDecimalNum(delegate.pow(n, MATH_CONTEXT));
    }

    /**
     * Returns the correctly rounded natural logarithm (base e) of the <code>double</code> value of this {@code num}.
     * /!\ Warning! Uses the {@code StrictMath#log(double)} method under the hood.
     * @return the natural logarithm (base e) of {@code this}
     * @see StrictMath#log(double)
     */
    public Num log() {
        if (this == NaN) {
            return NaN;
        }
        return new BigDecimalNum(StrictMath.log(delegate.doubleValue()));
    }

    /**
     * Returns the correctly rounded positive square root of the <code>double</code> value of this {@code num}.
     * /!\ Warning! Uses the {@code StrictMath#sqrt(double)} method under the hood.
     * @return the positive square root of {@code this}
     * @see StrictMath#sqrt(double)
     */
    public Num sqrt() {
        if (this == NaN) {
            return NaN;
        }
        return new BigDecimalNum(StrictMath.sqrt(delegate.doubleValue()));
    }

    /**
     * Returns a {@code num} whose value is the absolute value
     * of this {@code num}.
     * @return {@code abs(this)}
     */
    public Num abs() {
        if (this == NaN) {
            return NaN;
        }
        return new BigDecimalNum(delegate.abs());
    }

    /**
     * Checks if the value is zero.
     * @return true if the value is zero, false otherwise
     */
    public boolean isZero() {
        if (this == NaN) {
            return false;
        }
        return compareTo(valueOf(0)) == 0;
    }

    /**
     * Checks if the value is greater than zero.
     * @return true if the value is greater than zero, false otherwise
     */
    public boolean isPositive() {
        if (this == NaN) {
            return false;
        }
        return compareTo(valueOf(0)) > 0;
    }

    /**
     * Checks if the value is zero or greater.
     * @return true if the value is zero or greater, false otherwise
     */
    public boolean isPositiveOrZero() {
        if (this == NaN) {
            return false;
        }
        return compareTo(valueOf(0)) >= 0;
    }

    /**
     * Checks if the value is Not-a-Number.
     * @return true if the value is Not-a-Number (NaN), false otherwise
     */
    public boolean isNaN() {
        return this == NaN;
    }

    /**
     * Checks if the value is less than zero.
     * @return true if the value is less than zero, false otherwise
     */
    public boolean isNegative() {
        if (this == NaN) {
            return false;
        }
        return compareTo(valueOf(0)) < 0;
    }

    /**
     * Checks if the value is zero or less.
     * @return true if the value is zero or less, false otherwise
     */
    public boolean isNegativeOrZero() {
        if (this == NaN) {
            return false;
        }
        return compareTo(valueOf(0)) <= 0;
    }

    /**
     * Checks if this value is equal to another.
     * @param other the other value, not null
     * @return true is this is greater than the specified value, false otherwise
     */
    public boolean isEqual(Num other) {
        if ((this == NaN) || (other == NaN)) {
            return false;
        }
        return compareTo(other) == 0;
    }

    /**
     * Checks if this value is greater than another.
     * @param other the other value, not null
     * @return true is this is greater than the specified value, false otherwise
     */
    public boolean isGreaterThan(Num other) {
        if ((this == NaN) || (other == NaN)) {
            return false;
        }
        return compareTo(other) > 0;
    }

    /**
     * Checks if this value is greater than or equal to another.
     * @param other the other value, not null
     * @return true is this is greater than or equal to the specified value, false otherwise
     */
    public boolean isGreaterThanOrEqual(Num other) {
        if ((this == NaN) || (other == NaN)) {
            return false;
        }
        return compareTo(other) > -1;
    }

    /**
     * Checks if this value is less than another.
     * @param other the other value, not null
     * @return true is this is less than the specified value, false otherwise
     */
    public boolean isLessThan(Num other) {
        if ((this == NaN) || (other == NaN)) {
            return false;
        }
        return compareTo(other) < 0;
    }

    /**
     * Checks if this value is less than or equal to another.
     * @param other the other value, not null
     * @return true is this is less than or equal to the specified value, false otherwise
     */
    public boolean isLessThanOrEqual(Num other) {
        if ((this == NaN) || (other == NaN)) {
            return false;
        }
        return compareTo(other) < 1;
    }


    /**
     * Returns the minimum of this {@code Decimal} and {@code other}.
     * @param other value with which the minimum is to be computed
     * @return the {@code Decimal} whose value is the lesser of this
     *         {@code Decimal} and {@code other}.  If they are equal,
     *         as defined by the {@link #compareTo(Num) compareTo}
     *         method, {@code this} is returned.
     */
    public Num min(Num other) {
        if ((this == NaN) || (other == NaN)) {
            return NaN;
        }
        return (compareTo(other) <= 0 ? this : other);
    }

    /**
     * Returns the maximum of this {@code Decimal} and {@code other}.
     * @param  other value with which the maximum is to be computed
     * @return the {@code Decimal} whose value is the greater of this
     *         {@code Decimal} and {@code other}.  If they are equal,
     *         as defined by the {@link #compareTo(Num) compareTo}
     *         method, {@code this} is returned.
     */
    public Num max(Num other) {
        if ((this == NaN) || (other == NaN)) {
            return NaN;
        }
        return (compareTo(other) >= 0 ? this : other);
    }

    /**
     * Converts this {@code num} to a {@code double}.
     * @return this {@code num} converted to a {@code double}
     * @see BigDecimal#doubleValue()
     */
    public double toDouble() {
        if (this == NaN) {
            return Double.NaN;
        }
        return delegate.doubleValue();
    }

    @Override
    public String toString() {
        if (this == NaN) {
            return "NaN";
        }
        return delegate.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate);
    }

    /**
     * {@inheritDoc}
     * Warning: This method returns true if `this` and `obj` are both NaN.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Num)) {
            return false;
        }
        if(!(obj instanceof BigDecimalNum)){
            return false;
        }
        final BigDecimalNum other = (BigDecimalNum) obj;
        if (this.delegate != other.delegate
                && (this.delegate == null || (this.delegate.compareTo(other.delegate) != 0))) {
            return false;
        }
        return true;
    }


    @Override
    public int compareTo(Num other) {
        if(!(other instanceof BigDecimalNum)) {
            throw new IllegalArgumentException("Must be an BigDecimalNum!");
        }
        final BigDecimalNum baseOther = (BigDecimalNum) other;
        if ((this == NaN) || (baseOther == NaN)) {
            return 0;
        }
        return delegate.compareTo(baseOther.delegate);
    }

    public static BigDecimalNum valueOf(double val){
        return new BigDecimalNum(val);
    }
}