package com.donny.dendroecc.curves;

import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public class WeierstrassPrime extends Curve {
    public final BigInteger A, B, P;

    public WeierstrassPrime(BigInteger a, BigInteger b, BigInteger p) {
        A = a;
        B = b;
        P = p;
    }

    @Override
    public boolean validate(BigInteger x, BigInteger y) {
        return y.pow(2).mod(P)
                .compareTo(
                        x.pow(3).add(A.multiply(x)).add(B).mod(P)
                ) == 0;
    }

    @Override
    public BigInteger[] getY(BigInteger x) {
        BigInteger solution = Functions.modSqrt(x.pow(3).add(A.multiply(x)).add(B), P);
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
        return "WeierstrassPrime{a=" + A + ", b=" + B + ", p=" + P + "}";
    }
}
