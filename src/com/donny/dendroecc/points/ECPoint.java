package com.donny.dendroecc.points;

import com.donny.dendroecc.curves.Curve;
import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public abstract class ECPoint<P extends ECPoint<P, C>, C extends Curve> {
    public final BigInteger X, Y;
    public final C CURVE;

    public ECPoint(BigInteger x, BigInteger y, C curve) {
        X = x;
        Y = y;
        CURVE = curve;
    }

    /**
     * elliptic curve point addition
     *
     * @param b the point to be added
     * @return the result of point addition with this and <code>b</code>
     */
    public abstract P add(P b);

    /**
     * Elliptic curve point doubling
     *
     * @return the result of point doubling
     */
    public P doublePoint() {
        return add((P) this);
    }

    /**
     * elliptic curve point multiplication using double and add
     *
     * @param multiplicand positive integer
     * @return the result of adding this to itself <code>multiplicand</code> times
     */
    public P multiply(BigInteger multiplicand) {
        boolean[] map = Functions.getLsbVector(multiplicand);
        P out = null;
        P temp = (P) this;
        for (boolean bit : map) {
            if (bit) {
                if (out == null) {
                    out = temp;
                } else {
                    out = out.add(temp);
                }
                if (out == null) {
                    return null;
                }
            }
            temp = temp.doublePoint();
            if (temp == null) {
                return null;
            }
        }
        return out;
    }

    /**
     * tests if this point is actually on the elliptic curve <code>CURVE</code>
     *
     * @return <code>true</code> if the point is on the curve, <code>false</code> otherwise
     */
    public boolean validate() {
        return CURVE.validate(X, Y);
    }

    /**
     * tests <code>X</code> and <code>Y</code>
     * does not test <code>CURVE</code>
     *
     * @param b second elliptic curve point to test
     * @return true if <cdoe>X</cdoe> and <code>Y</code> match <code>b.X</code> and <code>b.Y</code>
     */
    public boolean equals(P b) {
        return X.compareTo(b.X) == 0 && Y.compareTo(b.Y) == 0;
    }

    @Override
    public String toString() {
        return "(" + X + ", " + Y + "): " + CURVE;
    }
}
