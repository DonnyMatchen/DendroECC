package com.donny.dendroecc.points;

import com.donny.dendroecc.curves.TwistedEdwards;
import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public class TeECPoint extends ECPoint<TeECPoint, TwistedEdwards> {
    public TeECPoint(BigInteger x, BigInteger y, TwistedEdwards curve) {
        super(x, y, curve);
    }

    @Override
    public TeECPoint add(TeECPoint b) {
        BigInteger common = CURVE.D.multiply(X).multiply(Y).multiply(b.X).multiply(b.Y);
        return new TeECPoint(
                Functions.modDivide(
                        X.multiply(b.Y).add(Y.multiply(b.X)).mod(CURVE.P),
                        BigInteger.ONE.add(common).mod(CURVE.P),
                        CURVE.P
                ),
                Functions.modDivide(
                        Y.multiply(b.Y).subtract(CURVE.A.multiply(X).multiply(b.X)).mod(CURVE.P),
                        BigInteger.ONE.subtract(common).mod(CURVE.P),
                        CURVE.P
                ),
                CURVE
        );
    }
}
