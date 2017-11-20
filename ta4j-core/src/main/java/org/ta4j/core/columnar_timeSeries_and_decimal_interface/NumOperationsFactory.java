package org.ta4j.core.columnar_timeSeries_and_decimal_interface;

/**
 * Factory to access the valueOf functions
 * Every class that implements the {@link NumOperations NumOperations Interface} has also to
 * implement its {@link NumOperationsFactory static Factory} by <b>convention</d>
 * @param <I> The {@link NumOperations NumOperations} class
 */
public interface NumOperationsFactory<I> {

    I ZERO();
    I ONE();
    I TWO();
    I THREE();
    I TEN();
    I HUNDRED();
    I THOUSAND();
    NumOperations NaN();

    default I createInstance(Class<I> clazz)throws Exception {
        return clazz.newInstance();
    }

    I valueOf(double val);
    I valueOf(int val);
    I valueOf(long val);
    I valueOf(String val);
}
