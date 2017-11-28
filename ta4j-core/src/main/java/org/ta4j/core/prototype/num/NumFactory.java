package org.ta4j.core.prototype.num;

/**
 * Factory to access the valueOf functions
 * Every class that implements the {@link num num Interface} has also to
 * implement its {@link NumFactory static Factory} by <b>convention</d>
 * @param <I> The {@link num num} class
 */
public interface NumFactory<I> {

    I ZERO();
    I ONE();
    I TWO();
    I THREE();
    I TEN();
    I HUNDRED();
    I THOUSAND();
    Num NaN();

    default I createInstance(Class<I> clazz)throws Exception {
        return clazz.newInstance();
    }

    I valueOf(double val);
    I valueOf(int val);
    I valueOf(long val);
    I valueOf(String val);
}
