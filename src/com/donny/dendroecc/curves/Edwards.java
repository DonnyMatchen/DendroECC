package com.donny.dendroecc.curves;

import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public class Edwards extends Curve {
    public final BigInteger C, D, P;

    public Edwards(BigInteger c, BigInteger d, BigInteger p) {
        C = c;
        D = d;
        P = p;
    }

    @Override
    public boolean validate(BigInteger x, BigInteger y) {
        return x.pow(2).add(y.pow(2)).mod(P)
                .compareTo(
                        C.pow(2).multiply(BigInteger.ONE.add(D.multiply(x.pow(2)).multiply(y.pow(2)))).mod(P)
                ) == 0;
    }

    @Override
    public BigInteger[] getY(BigInteger x) {
        BigInteger solution = Functions.modSqrt(
                Functions.modDivide(
                        C.pow(2).subtract(x.pow(2)),
                        BigInteger.ONE.subtract(C.pow(2).multiply(D).multiply(x.pow(2))),
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
        return "Edwards{c=" + C + ", d=" + D + ", p=" + P + "}";
    }
}
