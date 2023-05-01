package com.donny.dendroecc.points;

import com.donny.dendroecc.curves.Edwards;
import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public class EdECPoint extends ECPoint<EdECPoint, Edwards> {
    public EdECPoint(BigInteger x, BigInteger y, Edwards curve) {
        super(x, y, curve);
    }

    @Override
    public EdECPoint add(EdECPoint b) {
        BigInteger common = CURVE.D.multiply(X).multiply(Y).multiply(b.X).multiply(b.Y);
        return new EdECPoint(
                Functions.modDivide(
                        X.multiply(b.Y).add(Y.multiply(b.X)).mod(CURVE.P),
                        BigInteger.ONE.add(common).mod(CURVE.P),
                        CURVE.P
                ),
                Functions.modDivide(
                        Y.multiply(b.Y).subtract(X.multiply(b.X)).mod(CURVE.P),
                        BigInteger.ONE.subtract(common).mod(CURVE.P),
                        CURVE.P
                ),
                CURVE
        );
    }
}
