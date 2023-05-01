package com.donny.dendroecc.points;

import com.donny.dendroecc.curves.Montgomery;
import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public class MECPoint extends ECPoint<MECPoint, Montgomery> {
    public MECPoint(BigInteger x, BigInteger y, Montgomery curve) {
        super(x, y, curve);
    }

    @Override
    public MECPoint add(MECPoint b) {
        BigInteger dydx;
        if (b.X.compareTo(X) == 0) {
            if (b.Y.compareTo(Y) == 0) {
                dydx = Functions.modDivide(
                        X.pow(2).multiply(BigInteger.valueOf(3)).add(CURVE.A.multiply(X)).add(BigInteger.ONE).mod(CURVE.P),
                        Y.multiply(BigInteger.TWO).multiply(CURVE.B).mod(CURVE.P),
                        CURVE.P
                );
            } else {
                return null;
            }
        } else {
            dydx = Functions.modDivide(
                    Y.subtract(b.Y).mod(CURVE.P),
                    X.subtract(b.X).mod(CURVE.P),
                    CURVE.P
            );
        }
        return new MECPoint(
                CURVE.B.multiply(dydx.pow(2)).subtract(CURVE.A).subtract(X).subtract(b.X).mod(CURVE.P),
                dydx.multiply(X.multiply(BigInteger.TWO).add(b.X).add(CURVE.A)).subtract(CURVE.B.multiply(dydx.pow(3))).subtract(Y).mod(CURVE.P),
                CURVE
        );
    }
}
