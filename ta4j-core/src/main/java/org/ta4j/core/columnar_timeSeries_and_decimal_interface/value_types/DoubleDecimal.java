package org.ta4j.core.columnar_timeSeries_and_decimal_interface.value_types;

import org.ta4j.core.columnar_timeSeries_and_decimal_interface.IllegalNumOperationsException;
import org.ta4j.core.columnar_timeSeries_and_decimal_interface.NumOperationsFactory;
import org.ta4j.core.columnar_timeSeries_and_decimal_interface.Value;

public class DoubleDecimal implements Value<Double> {

    public static final NumOperationsFactory<DoubleDecimal> NUM_OPERATIONS_FACTORY = new NumOperationsFactory<DoubleDecimal>() {
        @Override
        public DoubleDecimal ZERO() {
            return new DoubleDecimal(0);
        }

        @Override
        public DoubleDecimal ONE() {
            return new DoubleDecimal(1);
        }

        @Override
        public DoubleDecimal TWO() {
            return new DoubleDecimal(2);
        }

        @Override
        public DoubleDecimal THREE() {
            return new DoubleDecimal(3);
        }

        @Override
        public DoubleDecimal TEN() {
            return new DoubleDecimal(10);
        }

        @Override
        public DoubleDecimal HUNDRED() {
            return new DoubleDecimal(100);
        }

        @Override
        public DoubleDecimal THOUSAND() {
            return new DoubleDecimal(1000);
        }

        @Override
        public Value NaN() {
            return Value.NaN;
        }

        @Override
        public DoubleDecimal valueOf(double val) {
           return new DoubleDecimal(val);
        }

        @Override
        public DoubleDecimal valueOf(int val) {
            return new DoubleDecimal(val);
        }

        @Override
        public DoubleDecimal valueOf(long val) {
            return new DoubleDecimal(val);
        }

        @Override
        public DoubleDecimal valueOf(String val) {
            return new DoubleDecimal(val);
        }
    } ;


    private final double delegate;


    DoubleDecimal(double val){
        delegate = val;
    }
    DoubleDecimal(int val){
        delegate = Double.valueOf(val);
    }
    DoubleDecimal(long val){
        delegate = Double.valueOf(val);
    }
    DoubleDecimal(String val){
        delegate = Double.valueOf(val);
    }

    @Override
    public Double getValue() {
        return delegate;
    }

    @Override
    public String getName() {
        return "DoubleDecimal";
    }

    @Override
    public Value plus(Value augend) {
        if (this == NaN || augend == NaN){
            return Value.NaN;
        }
        if (!(augend instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, augend);
        }

        return new DoubleDecimal(delegate+((DoubleDecimal) augend).delegate);
    }

    @Override
    public Value minus(Value subtrahend) {
        if (this == NaN || subtrahend == NaN){
            return Value.NaN;
        }
        if (!(subtrahend instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, subtrahend);
        }

        return new DoubleDecimal(delegate-((DoubleDecimal) subtrahend).delegate);
    }

    @Override
    public Value multipliedBy(Value multiplicand) {
        if (this == NaN || multiplicand == NaN){
            return Value.NaN;
        }
        if (!(multiplicand instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, multiplicand);
        }

        return new DoubleDecimal(delegate*((DoubleDecimal) multiplicand).delegate);
    }

    @Override
    public Value dividedBy(Value divisor) {
        if (this == NaN || divisor == NaN){
            return Value.NaN;
        }
        if (!(divisor instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, divisor);
        }

        return new DoubleDecimal(delegate/((DoubleDecimal) divisor).delegate);
    }

    @Override
    public Value remainder(Value divisor) {
        if (this == NaN || divisor == NaN){
            return Value.NaN;
        }
        if (!(divisor instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, divisor);
        }

        return new DoubleDecimal(delegate%((DoubleDecimal) divisor).delegate);
    }

    @Override
    public Value pow(int n) {
        return new DoubleDecimal(Math.pow(delegate,n));
    }

    @Override
    public Value log() {
        return new DoubleDecimal(Math.log(delegate));
    }

    @Override
    public Value abs() {
        return new DoubleDecimal(Math.abs(delegate));
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
    public boolean isEqual(Value other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, other);
        }
        return delegate == ((DoubleDecimal) other).delegate;
    }

    @Override
    public boolean isGreaterThan(Value other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, other);
        }
        return delegate > ((DoubleDecimal) other).delegate;
    }

    @Override
    public boolean isGreaterThanOrEqual(Value other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, other);
        }
        return delegate >= ((DoubleDecimal) other).delegate;
    }

    @Override
    public boolean isLessThan(Value other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, other);
        }
        return delegate < ((DoubleDecimal) other).delegate;
    }

    @Override
    public boolean isLessThanOrEqual(Value other) {
        if (this == NaN || other == NaN){
            return true;
        }
        if (!(other instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, other);
        }
        return delegate <= ((DoubleDecimal) other).delegate;
    }

    @Override
    public Value min(Value other) {
        if (this == NaN || other == NaN){
            return Value.NaN;
        }
        if (!(other instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, other);
        }
        return new DoubleDecimal(Math.min(delegate,((DoubleDecimal) other).delegate));
    }

    @Override
    public Value max(Value other) {
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
        if(!(obj instanceof Value)) {
            throw new IllegalArgumentException("Must extend from Value");
        }
        if (!((Value)obj instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, (Value)obj);
        }
        return delegate == ((DoubleDecimal)obj).delegate;
    }

    @Override
    public int compareTo(Value o) {
        if (this == NaN || o == NaN){
            return 0;
        }
        if (!(o instanceof DoubleDecimal)){
            throw new IllegalNumOperationsException(this, o);
        }
        return ((Double)delegate).compareTo(((DoubleDecimal) o).delegate);
    }
}
