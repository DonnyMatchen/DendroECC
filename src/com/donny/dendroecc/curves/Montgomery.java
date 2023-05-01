package com.donny.dendroecc.curves;

import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public class Montgomery extends Curve {
    public final BigInteger A, B, P;

    public Montgomery(BigInteger a, BigInteger b, BigInteger p) {
        if (b.multiply(a.pow(2).subtract(BigInteger.valueOf(4))).compareTo(BigInteger.ZERO) == 0) {
            throw new IllegalArgumentException("Invalid values of A and B");
        }
        A = a;
        B = b;
        P = p;
    }

    @Override
    public boolean validate(BigInteger x, BigInteger y) {
        return B.multiply(y.pow(2)).mod(P)
                .compareTo(
                        x.pow(3).add(A.multiply(x.pow(2))).add(x).mod(P)
                ) == 0;
    }

    @Override
    public BigInteger[] getY(BigInteger x) {
        BigInteger solution = Functions.modSqrt(
                Functions.modDivide(
                        x.pow(3).add(A.multiply(x.pow(2))).add(x),
                        B,
                        P
                ),
                P
        );
        if (solution == null) {
            return null;
        } else {
            if (solution.mod(BigInteger.TWO).compareTo(BigInteger.ONE) == 0) {
                return new BigInteger[]{
                        P.subtract(solution),
                        solution
                };
            } else {
                return new BigInteger[]{
                        solution,
                        P.subtract(solution)
                };
            }
        }
    }

    @Override
    public String toString() {
        return "Montgomery{a=" + A + ", b=" + B + ", p=" + P + "}";
    }
}
