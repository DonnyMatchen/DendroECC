package com.donny.dendroecc.curves;

import com.donny.dendroecc.util.Galois;

import java.math.BigInteger;

public class WeierstrassBinary extends Curve {
    public final BigInteger A, B, F;

    public WeierstrassBinary(BigInteger a, BigInteger b, BigInteger f) {
        A = a;
        B = b;
        F = f;
    }

    @Override
    public boolean validate(BigInteger x, BigInteger y) {
        return Galois.modulus(
                        Galois.multiply(y, y)
                                .xor(Galois.multiply(x, y)),
                        F
                )
                .compareTo(
                        Galois.modulus(
                                Galois.modExp(x, BigInteger.valueOf(3), F)
                                        .xor(Galois.multiply(Galois.multiply(x, x), A))
                                        .xor(B),
                                F
                        )
                ) == 0;
    }

    /**
     * returns the two <code>y</code> values given <code>x</code> and the curve parameters
     * the formula used can be found <a href="https://crypto.stackexchange.com/questions/72994/efficient-calculation-of-point-coordinates-with-elliptic-curves-over-binary-fiel">here</a>
     *
     * @param x the <code>x</code> coordinate
     * @return the <code>y</code> coordinates as: [even, odd], or null if there are no solutions for y given x
     */
    @Override
    public BigInteger[] getY(BigInteger x) {
        BigInteger y = Galois.modMult(
                x,
                Galois.halfTrace(
                        Galois.modulus(
                                x.xor(A).xor(
                                        Galois.modDivide(
                                                B,
                                                Galois.modMult(x, x, F),
                                                F
                                        )),
                                F
                        ),
                        F
                ),
                F
        );
        if (y.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
            return new BigInteger[]{y, x.xor(y)};
        } else {
            return new BigInteger[]{x.xor(y), y};
        }
    }

    @Override
    public String toString() {
        return "WeierstrassBinary{a=" + A + ", b=" + B + ", f=" + F + "}";
    }
}
