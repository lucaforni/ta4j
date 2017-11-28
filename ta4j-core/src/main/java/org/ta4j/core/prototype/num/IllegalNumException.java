package org.ta4j.core.prototype.num;

import org.ta4j.core.prototype.num.Num;

public class IllegalNumException extends IllegalArgumentException {

    public IllegalNumException(Num a, Num b){
        super(String.format("Incompatible implementations of the num interface: %s with %s",a.getName(), b.getName()));
    }


}
