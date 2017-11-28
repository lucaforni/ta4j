package org.ta4j.core.prototype.num;


import org.decimal4j.immutable.Decimal5f;

public class Decimal10fNum implements Num<Decimal5f> {

    private final Decimal5f delegate;

    public static final NumFactory<Decimal10fNum> NUM_OPERATIONS_FACTORY = new NumFactory<Decimal10fNum>() {
        @Override
        public Decimal10fNum ZERO() {
            return new Decimal10fNum(0);
        }

        @Override
        public Decimal10fNum ONE() {
            return new Decimal10fNum(1);
        }

        @Override
        public Decimal10fNum TWO() {
            return new Decimal10fNum(2);
        }

        @Override
        public Decimal10fNum THREE() {
            return new Decimal10fNum(3);
        }

        @Override
        public Decimal10fNum TEN() {
            return new Decimal10fNum(10);
        }

        @Override
        public Decimal10fNum HUNDRED() {
            return new Decimal10fNum(100);
        }

        @Override
        public Decimal10fNum THOUSAND() {
            return new Decimal10fNum(1000);
        }

        @Override
        public Num NaN() {
            return Num.NaN;
        }

        @Override
        public Decimal10fNum valueOf(double val) {
            return new Decimal10fNum(val);
        }

        @Override
        public Decimal10fNum valueOf(int val) {
            return new Decimal10fNum(val);
        }

        @Override
        public Decimal10fNum valueOf(long val) {
            return new Decimal10fNum(val);
        }

        @Override
        public Decimal10fNum valueOf(String val) {
            return new Decimal10fNum(val);
        }
    };

    private Decimal10fNum(double val){
        this(Decimal5f.valueOf(val));
    }

    private Decimal10fNum(Decimal5f val){
        delegate = val;
    }

    private Decimal10fNum(String val){
        delegate = Decimal5f.valueOf(val);
    }

    private Decimal10fNum(int val){
        delegate = Decimal5f.valueOf(val);
    }


    @Override
    public NumFactory getFactory() {
        return NUM_OPERATIONS_FACTORY;
    }

    @Override
    public Decimal5f getDelegate() {
        return delegate;
    }

    @Override
    public String getName() {
        return "Decimal10fNum (Decimal5f)";
    }

    @Override
    public Num plus(Num augend) {
        if(!(augend instanceof Decimal10fNum)){
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN) || (augend == NaN)) {
            return NaN;
        }
        final Decimal10fNum longAugend = (Decimal10fNum) augend;
        return new Decimal10fNum(longAugend.delegate.add(delegate));
    }

    @Override
    public Num minus(Num subtrahend) {
        if(!(subtrahend instanceof Decimal10fNum)){
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN) || (subtrahend == NaN)) {
            return NaN;
        }
        final Decimal10fNum longSubtrahend = (Decimal10fNum) subtrahend;
        return new Decimal10fNum(longSubtrahend.delegate.subtract(delegate));
    }

    @Override
    public Num multipliedBy(Num multiplicand) {
        if(!(multiplicand instanceof Decimal10fNum)){
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN) || (multiplicand == NaN)) {
            return NaN;
        }
        final Decimal10fNum val = (Decimal10fNum) multiplicand;
        return new Decimal10fNum(val.delegate.multiply(delegate));
    }

    @Override
    public Num dividedBy(Num divisor) {
        if(!(divisor instanceof Decimal10fNum)){
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN) || (divisor == NaN)) {
            return NaN;
        }
        final Decimal10fNum val = (Decimal10fNum) divisor;
        return new Decimal10fNum(delegate.divide(val.delegate));
    }

    @Override
    public Num remainder(Num divisor) {
        if(!(divisor instanceof Decimal10fNum)){
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN) || (divisor == NaN)) {
            return NaN;
        }
        final Decimal10fNum val = (Decimal10fNum) divisor;
        return new Decimal10fNum(val.delegate.remainder(delegate));
    }

    @Override
    public Num pow(int n) {
        if ((this == NaN)) {
            return NaN;
        }
        return new Decimal10fNum(delegate.pow(n));
    }

    @Override
    public Num log() {
        if ((this == NaN)) {
            return NaN;
        }
        return new Decimal10fNum(StrictMath.log(delegate.doubleValue()));
    }

    @Override
    public Num abs() {
        if ((this == NaN)) {
            return NaN;
        }
        return new Decimal10fNum(delegate.abs());
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
    public boolean isEqual(Num other) {
        if (!(other instanceof Decimal10fNum)) {
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN)) {
            return false;
        }
        return delegate.isEqualTo(((Decimal10fNum) other).delegate);
    }

    @Override
    public boolean isGreaterThan(Num other) {
        if (!(other instanceof Decimal10fNum)) {
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN)) {
            return false;
        }
        return delegate.isGreaterThan(((Decimal10fNum) other).delegate);
    }

    @Override
    public boolean isGreaterThanOrEqual(Num other) {
        if (!(other instanceof Decimal10fNum)) {
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN)) {
            return false;
        }
        return delegate.isGreaterThanOrEqualTo(((Decimal10fNum) other).delegate);
    }

    @Override
    public boolean isLessThan(Num other) {
        if (!(other instanceof Decimal10fNum)) {
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN)) {
            return false;
        }
        return delegate.isLessThan(((Decimal10fNum) other).delegate);
    }

    @Override
    public boolean isLessThanOrEqual(Num other) {
        if (!(other instanceof Decimal10fNum)) {
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        if ((this == NaN)) {
            return false;
        }
        return delegate.isLessThanOrEqualTo(((Decimal10fNum) other).delegate);
    }

    @Override
    public Num min(Num other) {
        if (!(other instanceof Decimal10fNum)) {
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }

        Decimal10fNum val = (Decimal10fNum) other;
        if ((this == NaN) || val == NaN) {
            return NaN;
        }
        return (compareTo(other) <= 0 ? this : other);
    }

    @Override
    public Num max(Num other) {
        if (!(other instanceof Decimal10fNum)) {
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        return null;
    }

    @Override
    public double toDouble() {
        return delegate.doubleValue();
    }

    @Override
    public int compareTo(Num other) {
        if ((this == NaN) || (other == NaN)) {
            return 0;
        }
        if (!(other instanceof Decimal10fNum)){
            throw new IllegalArgumentException("Instance of Decimal10fNum is needed!");
        }
        return delegate.compareTo(((Decimal10fNum)other).delegate);
    }
}
