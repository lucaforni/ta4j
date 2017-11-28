package org.ta4j.core.prototype.num;

public class DoubleNum implements Num<Double> {

    public static final NumFactory<DoubleNum> NUM_OPERATIONS_FACTORY = new NumFactory<DoubleNum>() {
        @Override
        public DoubleNum ZERO() {
            return new DoubleNum(0);
        }

        @Override
        public DoubleNum ONE() {
            return new DoubleNum(1);
        }

        @Override
        public DoubleNum TWO() {
            return new DoubleNum(2);
        }

        @Override
        public DoubleNum THREE() {
            return new DoubleNum(3);
        }

        @Override
        public DoubleNum TEN() {
            return new DoubleNum(10);
        }

        @Override
        public DoubleNum HUNDRED() {
            return new DoubleNum(100);
        }

        @Override
        public DoubleNum THOUSAND() {
            return new DoubleNum(1000);
        }

        @Override
        public Num NaN() {
            return Num.NaN;
        }

        @Override
        public DoubleNum valueOf(double val) {
           return new DoubleNum(val);
        }

        @Override
        public DoubleNum valueOf(int val) {
            return new DoubleNum(val);
        }

        @Override
        public DoubleNum valueOf(long val) {
            return new DoubleNum(val);
        }

        @Override
        public DoubleNum valueOf(String val) {
            return new DoubleNum(val);
        }
    } ;


    private final double delegate;


    DoubleNum(double val){
        delegate = val;
    }
    DoubleNum(int val){
        delegate = Double.valueOf(val);
    }
    DoubleNum(long val){
        delegate = Double.valueOf(val);
    }
    DoubleNum(String val){
        delegate = Double.valueOf(val);
    }

    @Override
    public NumFactory getFactory() {
        return NUM_OPERATIONS_FACTORY;
    }

    @Override
    public Double getDelegate() {
        return delegate;
    }

    @Override
    public String getName() {
        return "DoubleNum";
    }

    @Override
    public Num plus(Num augend) {
        if (this == NaN || augend == NaN){
            return Num.NaN;
        }
        if (!(augend instanceof DoubleNum)){
            throw new IllegalNumException(this, augend);
        }

        return new DoubleNum(delegate+((DoubleNum) augend).delegate);
    }

    @Override
    public Num minus(Num subtrahend) {
        if (this == NaN || subtrahend == NaN){
            return Num.NaN;
        }
        if (!(subtrahend instanceof DoubleNum)){
            throw new IllegalNumException(this, subtrahend);
        }

        return new DoubleNum(delegate-((DoubleNum) subtrahend).delegate);
    }

    @Override
    public Num multipliedBy(Num multiplicand) {
        if (this == NaN || multiplicand == NaN){
            return Num.NaN;
        }
        if (!(multiplicand instanceof DoubleNum)){
            throw new IllegalNumException(this, multiplicand);
        }

        return new DoubleNum(delegate*((DoubleNum) multiplicand).delegate);
    }

    @Override
    public Num dividedBy(Num divisor) {
        if (this == NaN || divisor == NaN){
            return Num.NaN;
        }
        if (!(divisor instanceof DoubleNum)){
            throw new IllegalNumException(this, divisor);
        }

        return new DoubleNum(delegate/((DoubleNum) divisor).delegate);
    }

    @Override
    public Num remainder(Num divisor) {
        if (this == NaN || divisor == NaN){
            return Num.NaN;
        }
        if (!(divisor instanceof DoubleNum)){
            throw new IllegalNumException(this, divisor);
        }

        return new DoubleNum(delegate%((DoubleNum) divisor).delegate);
    }

    @Override
    public Num pow(int n) {
        return new DoubleNum(Math.pow(delegate,n));
    }

    @Override
    public Num log() {
        return new DoubleNum(Math.log(delegate));
    }

    @Override
    public Num abs() {
        return new DoubleNum(Math.abs(delegate));
    }

    @Override
    public boolean isZero() {
        return delegate == 0;
    }

    @Override
    public boolean isPositive() {
        return delegate > 0;
    }

    @Override
    public boolean isPositiveOrZero() {
        return delegate >= 0;
    }

    @Override
    public boolean isNegative() {
        return delegate < 0;
    }

    @Override
    public boolean isNegativeOrZero() {
        return delegate <= 0;
    }

    @Override
    public boolean isEqual(Num other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleNum)){
            throw new IllegalNumException(this, other);
        }
        return delegate == ((DoubleNum) other).delegate;
    }

    @Override
    public boolean isGreaterThan(Num other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleNum)){
            throw new IllegalNumException(this, other);
        }
        return delegate > ((DoubleNum) other).delegate;
    }

    @Override
    public boolean isGreaterThanOrEqual(Num other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleNum)){
            throw new IllegalNumException(this, other);
        }
        return delegate >= ((DoubleNum) other).delegate;
    }

    @Override
    public boolean isLessThan(Num other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleNum)){
            throw new IllegalNumException(this, other);
        }
        return delegate < ((DoubleNum) other).delegate;
    }

    @Override
    public boolean isLessThanOrEqual(Num other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleNum)){
            throw new IllegalNumException(this, other);
        }
        return delegate <= ((DoubleNum) other).delegate;
    }

    @Override
    public Num min(Num other) {
        if (this == NaN || other == NaN){
            return Num.NaN;
        }
        if (!(other instanceof DoubleNum)){
            throw new IllegalNumException(this, other);
        }
        return new DoubleNum(Math.min(delegate,((DoubleNum) other).delegate));
    }

    @Override
    public Num max(Num other) {
        return null;
    }

    @Override
    public double toDouble() {
        return delegate;
    }

    @Override
    public int hashCode() {
        return ((Double) (delegate)).hashCode();
    }

    @Override
    public String toString() {
        return ((Double) (delegate)).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == NaN || obj == NaN){
            return true;
        }
        if(!(obj instanceof Num)) {
            throw new IllegalArgumentException("Must extend from num");
        }
        if (!((Num)obj instanceof DoubleNum)){
            throw new IllegalNumException(this, (Num)obj);
        }
        return delegate == ((DoubleNum)obj).delegate;
    }

    @Override
    public int compareTo(Num o) {
        if (this == NaN || o == NaN){
            return 0;
        }
        if (!(o instanceof DoubleNum)){
            throw new IllegalNumException(this, o);
        }
        return ((Double)delegate).compareTo(((DoubleNum) o).delegate);
    }
}
