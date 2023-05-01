package com.donny.dendroecc.points;

import com.donny.dendroecc.curves.WeierstrassBinary;
import com.donny.dendroecc.util.Galois;

import java.math.BigInteger;

public class WbECPoint extends ECPoint<WbECPoint, WeierstrassBinary> {
    public WbECPoint(BigInteger x, BigInteger y, WeierstrassBinary curve) {
        super(x, y, curve);
    }

    @Override
    public WbECPoint add(WbECPoint b) {
        BigInteger gamma;
        if (X.compareTo(b.X) == 0 && Y.compareTo(b.Y) == 0) {
            gamma = Galois.modulus(Galois.modDivide(Y.xor(Galois.multiply(X, X)), X, CURVE.F), CURVE.F);
        } else {
            gamma = Galois.modDivide(Y.xor(b.Y), X.xor(b.X), CURVE.F);
        }
        BigInteger xx = Galois.modulus(Galois.multiply(gamma, gamma).xor(gamma).xor(X).xor(b.X).xor(CURVE.A), CURVE.F);
        return new WbECPoint(
                xx,
                Galois.modulus(Galois.multiply(gamma, X.xor(xx)).xor(xx).xor(Y), CURVE.F),
                CURVE
        );
    }
}
