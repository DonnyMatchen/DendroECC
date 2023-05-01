package com.donny.dendroecc.curves;

import java.math.BigInteger;

public abstract class Curve {
    /**
     * validates if a point given by a set of coordinates falls on the elliptic curve
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if (x, y) lies on the curve, false otherwise
     */
    public abstract boolean validate(BigInteger x, BigInteger y);

    /**
     * returns the two <code>y</code> values given <code>x</code> and the curve parameters
     *
     * @param x the <code>x</code> coordinate
     * @return the <code>y</code> coordinates as: [even, odd], or null if there are no solutions for y given x
     */
    public abstract BigInteger[] getY(BigInteger x);
}
