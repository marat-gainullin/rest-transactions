package com.bearsoft.transactions.web;

/**
 * This class is sum response, "sum" property holder. The response should be
 * {sum: value} and so, this class exists.
 */
public class SumResult {

    /**
     * Sum property value.
     */
    private final double sum;

    /**
     * A valued constructor.
     *
     * @param aValue A value of sum property.
     */
    public SumResult(final double aValue) {
        super();
        sum = aValue;
    }

    /**
     * "sum" property getter.
     *
     * @return "sum" property value.
     */
    public final double getSum() {
        return sum;
    }

}
