package com.donny.dendroecc.points;

import com.donny.dendroecc.curves.WeierstrassPrime;
import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public class WpECPoint extends ECPoint<WpECPoint, WeierstrassPrime> {
    public WpECPoint(BigInteger x, BigInteger y, WeierstrassPrime curve) {
        super(x, y, curve);
    }

    @Override
    public WpECPoint add(WpECPoint b) {
        BigInteger dydx;
        if (b.X.compareTo(X) == 0) {
            if (b.Y.compareTo(Y) == 0) {
                dydx = Functions.modDivide(
                        X.pow(2).multiply(BigInteger.valueOf(3)).add(CURVE.A).mod(CURVE.P),
                        Y.multiply(BigInteger.TWO).mod(CURVE.P),
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
        BigInteger x = dydx.pow(2).subtract(X).subtract(b.X).mod(CURVE.P);
        return new WpECPoint(
                x,
                dydx.multiply(X.subtract(x)).subtract(Y).mod(CURVE.P),
                CURVE
        );
    }
}
