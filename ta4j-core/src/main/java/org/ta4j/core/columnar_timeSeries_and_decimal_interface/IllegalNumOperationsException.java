package org.ta4j.core.columnar_timeSeries_and_decimal_interface;

public class IllegalNumOperationsException extends IllegalArgumentException {

    public IllegalNumOperationsException(Value a, Value b){
        super(String.format("Incompatible implementations of the Value interface: %s with %s",a.getName(), b.getName()));
    }


}
