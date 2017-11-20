package org.ta4j.core.columnar_timeSeries_and_decimal_interface;


import org.decimal4j.immutable.Decimal5f;

public class LongDecimal implements NumOperations<Decimal5f>{

    private final Decimal5f delegate;

    public static final NumOperationsFactory<LongDecimal> NUM_OPERATIONS_FACTORY = new NumOperationsFactory<LongDecimal>() {
        @Override
        public LongDecimal ZERO() {
            return new LongDecimal(0);
        }

        @Override
        public LongDecimal ONE() {
            return new LongDecimal(1);
        }

        @Override
        public LongDecimal TWO() {
            return new LongDecimal(2);
        }

        @Override
        public LongDecimal THREE() {
            return new LongDecimal(3);
        }

        @Override
        public LongDecimal TEN() {
            return new LongDecimal(10);
        }

        @Override
        public LongDecimal HUNDRED() {
            return new LongDecimal(100);
        }

        @Override
        public LongDecimal THOUSAND() {
            return new LongDecimal(1000);
        }

        @Override
        public NumOperations NaN() {
            return NumOperations.NaN;
        }

        @Override
        public LongDecimal valueOf(double val) {
            return new LongDecimal(val);
        }

        @Override
        public LongDecimal valueOf(int val) {
            return new LongDecimal(val);
        }

        @Override
        public LongDecimal valueOf(long val) {
            return new LongDecimal(val);
        }

        @Override
        public LongDecimal valueOf(String val) {
            return new LongDecimal(val);
        }
    };

    private LongDecimal(double val){
        this(Decimal5f.valueOf(val));
    }

    private LongDecimal(Decimal5f val){
        delegate = val;
    }

    private LongDecimal(String val){
        delegate = Decimal5f.valueOf(val);
    }

    private LongDecimal(int val){
        delegate = Decimal5f.valueOf(val);
    }


    @Override
    public Decimal5f getValue() {
        return delegate;
    }

    @Override
    public NumOperations plus(NumOperations augend) {
        if(!(augend instanceof LongDecimal)){
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        if ((this == NaN) || (augend == NaN)) {
            return NaN;
        }
        final LongDecimal longAugend = (LongDecimal) augend;
        return new LongDecimal(longAugend.delegate.add(delegate));
    }

    @Override
    public NumOperations minus(NumOperations subtrahend) {
        if(!(subtrahend instanceof LongDecimal)){
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        if ((this == NaN) || (subtrahend == NaN)) {
            return NaN;
        }
        final LongDecimal longSubtrahend = (LongDecimal) subtrahend;
        return new LongDecimal(longSubtrahend.delegate.subtract(delegate));
    }

    @Override
    public NumOperations multipliedBy(NumOperations multiplicand) {
        if(!(multiplicand instanceof LongDecimal)){
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        if ((this == NaN) || (multiplicand == NaN)) {
            return NaN;
        }
        final LongDecimal val = (LongDecimal) multiplicand;
        return new LongDecimal(val.delegate.multiply(delegate));
    }

    @Override
    public NumOperations dividedBy(NumOperations divisor) {
        if(!(divisor instanceof LongDecimal)){
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        if ((this == NaN) || (divisor == NaN)) {
            return NaN;
        }
        final LongDecimal val = (LongDecimal) divisor;
        return new LongDecimal(delegate.divide(val.delegate));
    }

    @Override
    public NumOperations remainder(NumOperations divisor) {
        if(!(divisor instanceof LongDecimal)){
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        if ((this == NaN) || (divisor == NaN)) {
            return NaN;
        }
        final LongDecimal val = (LongDecimal) divisor;
        return new LongDecimal(val.delegate.remainder(delegate));
    }

    @Override
    public NumOperations pow(int n) {
        if ((this == NaN)) {
            return NaN;
        }
        return new LongDecimal(delegate.pow(n));
    }

    @Override
    public NumOperations log() {
        if ((this == NaN)) {
            return NaN;
        }
        return new LongDecimal(StrictMath.log(delegate.doubleValue()));
    }

    @Override
    public NumOperations abs() {
        if ((this == NaN)) {
            return NaN;
        }
        return new LongDecimal(delegate.abs());
    }

    @Override
    public boolean isZero() {
        if ((this == NaN)) {
            return false;
        }
        return delegate.isZero();
    }

    @Override
    public boolean isPositive() {
        if ((this == NaN)) {
            return false;
        }
        return delegate.isPositive();
    }

    @Override
    public boolean isPositiveOrZero() {
        if ((this == NaN)) {
            return false;
        }
        return delegate.isPositive() || delegate.isZero();
    }

    @Override
    public boolean isNegative() {
        if ((this == NaN)) {
            return false;
        }
        return delegate.isNegative();
    }

    @Override
    public boolean isNegativeOrZero() {
        if ((this == NaN)) {
            return false;
        }
        return delegate.isNegative() || delegate.isZero();
    }

    @Override
    public boolean isEqual(NumOperations other) {
        if (!(other instanceof LongDecimal)) {
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        if ((this == NaN)) {
            return false;
        }
        return delegate.isEqualTo(((LongDecimal) other).delegate);
    }

    @Override
    public boolean isGreaterThan(NumOperations other) {
        if (!(other instanceof LongDecimal)) {
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        if ((this == NaN)) {
            return false;
        }
        return delegate.isGreaterThan(((LongDecimal) other).delegate);
    }

    @Override
    public boolean isGreaterThanOrEqual(NumOperations other) {
        if (!(other instanceof LongDecimal)) {
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        if ((this == NaN)) {
            return false;
        }
        return delegate.isGreaterThanOrEqualTo(((LongDecimal) other).delegate);
    }

    @Override
    public boolean isLessThan(NumOperations other) {
        if (!(other instanceof LongDecimal)) {
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        if ((this == NaN)) {
            return false;
        }
        return delegate.isLessThan(((LongDecimal) other).delegate);
    }

    @Override
    public NumOperations min(NumOperations other) {
        if (!(other instanceof LongDecimal)) {
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }

        LongDecimal val = (LongDecimal) other;
        if ((this == NaN) || val == NaN) {
            return NaN;
        }
        return (compareTo(other) <= 0 ? this : other);
    }

    @Override
    public NumOperations max(NumOperations other) {
        if (!(other instanceof LongDecimal)) {
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        return null;
    }

    @Override
    public double toDouble() {
        return delegate.doubleValue();
    }

    @Override
    public int compareTo(NumOperations other) {
        if ((this == NaN) || (other == NaN)) {
            return 0;
        }
        if (!(other instanceof LongDecimal)){
            throw new IllegalArgumentException("Instance of LongDecimal is needed!");
        }
        return delegate.compareTo(((LongDecimal)other).delegate);
    }
}
