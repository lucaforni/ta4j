package org.ta4j.core.columnar_timeSeries_and_decimal_interface.value_types;

import org.ta4j.core.columnar_timeSeries_and_decimal_interface.Value;
import org.ta4j.core.columnar_timeSeries_and_decimal_interface.NumOperationsFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable, arbitrary-precision signed columnar_timeSeries_and_decimal_interface numbers designed for technical analysis.
 * <p></p>
 * A {@code Value} consists of a {@code BigDecimal} with arbitrary {@link MathContext} (precision and rounding mode).
 *
 * @see BigDecimal
 * @see MathContext
 * @see RoundingMode
 */
public final class BaseDecimal implements Value<BigDecimal> {

    private static final long serialVersionUID = 2225130444465033658L;

    public static final MathContext MATH_CONTEXT = new MathContext(32, RoundingMode.HALF_UP);

    private final BigDecimal delegate;

    public static final NumOperationsFactory<BaseDecimal> NUM_OPERATIONS_FACTORY = new NumOperationsFactory<BaseDecimal>() {
        @Override
        public BaseDecimal ZERO() {
            return new BaseDecimal(0);
        }

        @Override
        public BaseDecimal ONE() {
            return new BaseDecimal(1);
        }

        @Override
        public BaseDecimal TWO() {
            return new BaseDecimal(2);
        }

        @Override
        public BaseDecimal THREE() {
            return new BaseDecimal(3);
        }

        @Override
        public BaseDecimal TEN() { return new BaseDecimal(10); }

        @Override
        public BaseDecimal HUNDRED() {
            return new BaseDecimal(100);
        }

        @Override
        public BaseDecimal THOUSAND() {
            return new BaseDecimal(1000);
        }

        @Override
        public Value NaN() {
            return Value.NaN;
        }

        @Override
        public BaseDecimal valueOf(double val) {
            return new BaseDecimal(val);
        }

        @Override
        public BaseDecimal valueOf(int val) {
            return new BaseDecimal(val);
        }

        @Override
        public BaseDecimal valueOf(long val) {
            return new BaseDecimal(val);
        }

        @Override
        public BaseDecimal valueOf(String val) {
            return new BaseDecimal(val);
        }
    };

    /**
     * Constructor.
     * @param val the string representation of the columnar_timeSeries_and_decimal_interface value
     */
    private BaseDecimal(String val) {
        delegate = new BigDecimal(val, MATH_CONTEXT);
    }

    /**
     * Constructor.
     * @param val the double value
     */
    private BaseDecimal(double val) {
        delegate = new BigDecimal(val, MATH_CONTEXT);
    }

    private BaseDecimal(int val) {
        delegate = new BigDecimal(val, MATH_CONTEXT);
    }

    private BaseDecimal(long val) {
        delegate = new BigDecimal(val, MATH_CONTEXT);
    }

    private BaseDecimal(BigDecimal val) {
        delegate = val;
    }


    @Override
    public BigDecimal getValue() {
        return delegate;
    }

    @Override
    public String getName() {
        return "BaseDecimal";
    }

    /**
     * Returns a {@code Value} whose value is {@code (this + augend)},
     * with rounding according to the context settings.
     * @param augend value to be added to this {@code Value}.
     * @return {@code this + augend}, rounded as necessary
     * @see BigDecimal#add(java.math.BigDecimal, java.math.MathContext)
     */
    public Value plus(Value augend) {
        if(!(augend instanceof BaseDecimal)){
            throw new IllegalArgumentException("Instance of BaseDecimal is needed!");
        }
        if ((this == Value.NaN) || (augend == NaN)) {
            return NaN;
        }
        final BaseDecimal baseReplicant = (BaseDecimal) augend;
        return new BaseDecimal(delegate.add(baseReplicant.delegate, MATH_CONTEXT));
    }
    /**
     * Returns a {@code Value} whose value is {@code (this - augend)},
     * with rounding according to the context settings.
     * @param subtrahend value to be subtracted from this {@code Value}.
     * @return {@code this - subtrahend}, rounded as necessary
     * @see BigDecimal#subtract(java.math.BigDecimal, java.math.MathContext)
     */
    public Value minus(Value subtrahend) {
        if(!(subtrahend instanceof BaseDecimal)){
            throw new IllegalArgumentException("Instance of BaseDecimal is needed!");
        }
        if ((this == NaN) || (subtrahend == NaN)) {
            return NaN;
        }
        final BaseDecimal baseReplicant = (BaseDecimal) subtrahend;
        return new BaseDecimal(delegate.subtract(baseReplicant.delegate, MATH_CONTEXT));
    }

    /**
     * Returns a {@code Value} whose value is {@code this * multiplicand},
     * with rounding according to the context settings.
     * @param multiplicand value to be multiplied by this {@code Value}.
     * @return {@code this * multiplicand}, rounded as necessary
     * @see BigDecimal#multiply(java.math.BigDecimal, java.math.MathContext)
     */
    public Value multipliedBy(Value multiplicand) {
        if(!(multiplicand instanceof BaseDecimal)){
            throw new IllegalArgumentException("Instance of BaseDecimal is needed!");
        }
        if ((this == NaN) || (multiplicand == NaN)) {
            return NaN;
        }
        final BaseDecimal baseReplicant = (BaseDecimal) multiplicand;
        return new BaseDecimal(delegate.multiply(baseReplicant.delegate, MATH_CONTEXT));
    }

    /**
     * Returns a {@code Value} whose value is {@code (this / divisor)},
     * with rounding according to the context settings.
     * @param divisor value by which this {@code Value} is to be divided.
     * @return {@code this / divisor}, rounded as necessary
     * @see BigDecimal#divide(java.math.BigDecimal, java.math.MathContext)
     */
    public Value dividedBy(Value divisor) {
        if(!(divisor instanceof BaseDecimal)){
            throw new IllegalArgumentException("Instance of BaseDecimal is needed!");
        }
        if ((this == NaN) || (divisor == NaN) || divisor.isZero()) {
            return NaN;
        }

        final BaseDecimal baseDivisor = (BaseDecimal) divisor;
        return new BaseDecimal(delegate.divide(baseDivisor.delegate, MATH_CONTEXT));
    }

    /**
     * Returns a {@code Value} whose value is {@code (this % divisor)},
     * with rounding according to the context settings.
     * @param divisor value by which this {@code Value} is to be divided.
     * @return {@code this % divisor}, rounded as necessary.
     * @see BigDecimal#remainder(java.math.BigDecimal, java.math.MathContext)
     */
    public Value remainder(Value divisor) {
        if(!(divisor instanceof BaseDecimal)){
            throw new IllegalArgumentException("Instance of BaseDecimal is needed!");
        }
        if ((this == NaN) || (divisor == NaN) || divisor.isZero()) {
            return NaN;
        }

        BaseDecimal baseDivisor = (BaseDecimal) divisor;
        return new BaseDecimal(delegate.remainder(baseDivisor.delegate, MATH_CONTEXT));
    }


    /**
     * Returns a {@code Value} whose value is <tt>(this<sup>n</sup>)</tt>.
     * @param n power to raise this {@code Value} to.
     * @return <tt>this<sup>n</sup></tt>
     * @see BigDecimal#pow(int, java.math.MathContext)
     */
    public Value pow(int n) {
        if (this == NaN) {
            return NaN;
        }
        return new BaseDecimal(delegate.pow(n, MATH_CONTEXT));
    }

    /**
     * Returns the correctly rounded natural logarithm (base e) of the <code>double</code> value of this {@code Value}.
     * /!\ Warning! Uses the {@code StrictMath#log(double)} method under the hood.
     * @return the natural logarithm (base e) of {@code this}
     * @see StrictMath#log(double)
     */
    public Value log() {
        if (this == NaN) {
            return NaN;
        }
        return new BaseDecimal(StrictMath.log(delegate.doubleValue()));
    }

    /**
     * Returns the correctly rounded positive square root of the <code>double</code> value of this {@code Value}.
     * /!\ Warning! Uses the {@code StrictMath#sqrt(double)} method under the hood.
     * @return the positive square root of {@code this}
     * @see StrictMath#sqrt(double)
     */
    public Value sqrt() {
        if (this == NaN) {
            return NaN;
        }
        return new BaseDecimal(StrictMath.sqrt(delegate.doubleValue()));
    }

    /**
     * Returns a {@code Value} whose value is the absolute value
     * of this {@code Value}.
     * @return {@code abs(this)}
     */
    public Value abs() {
        if (this == NaN) {
            return NaN;
        }
        return new BaseDecimal(delegate.abs());
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
    public boolean isEqual(Value other) {
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
    public boolean isGreaterThan(Value other) {
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
    public boolean isGreaterThanOrEqual(Value other) {
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
    public boolean isLessThan(Value other) {
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
    public boolean isLessThanOrEqual(Value other) {
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
     *         as defined by the {@link #compareTo(Value) compareTo}
     *         method, {@code this} is returned.
     */
    public Value min(Value other) {
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
     *         as defined by the {@link #compareTo(Value) compareTo}
     *         method, {@code this} is returned.
     */
    public Value max(Value other) {
        if ((this == NaN) || (other == NaN)) {
            return NaN;
        }
        return (compareTo(other) >= 0 ? this : other);
    }

    /**
     * Converts this {@code Value} to a {@code double}.
     * @return this {@code Value} converted to a {@code double}
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
        if (!(obj instanceof Value)) {
            return false;
        }
        if(!(obj instanceof BaseDecimal)){
            return false;
        }
        final BaseDecimal other = (BaseDecimal) obj;
        if (this.delegate != other.delegate
                && (this.delegate == null || (this.delegate.compareTo(other.delegate) != 0))) {
            return false;
        }
        return true;
    }


    @Override
    public int compareTo(Value other) {
        if(!(other instanceof BaseDecimal)) {
            throw new IllegalArgumentException("Must be an BaseDecimal!");
        }
        final BaseDecimal baseOther = (BaseDecimal) other;
        if ((this == NaN) || (baseOther == NaN)) {
            return 0;
        }
        return delegate.compareTo(baseOther.delegate);
    }

    public static BaseDecimal valueOf(double val){
        return new BaseDecimal(val);
    }
}