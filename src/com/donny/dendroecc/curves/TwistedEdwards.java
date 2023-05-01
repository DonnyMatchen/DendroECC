package com.donny.dendroecc.curves;

import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public class TwistedEdwards extends Curve {
    public final BigInteger A, D, P;

    public TwistedEdwards(BigInteger a, BigInteger d, BigInteger p) {
        A = a;
        D = d;
        P = p;
    }

    @Override
    public boolean validate(BigInteger x, BigInteger y) {
        return A.multiply(x.pow(2)).add(y.pow(2)).mod(P)
                .compareTo(
                        BigInteger.ONE.add(D.multiply(x.pow(2)).multiply(y.pow(2))).mod(P)
                ) == 0;
    }

    /**
     * returns the two y values given x and the curve parameters
     *
     * @param x the x coordinate
     * @return the y coordinates as: [even, odd], or null if there are no solutions for y given x
     */
    public BigInteger[] getY(BigInteger x) {
        BigInteger solution = Functions.modSqrt(
                Functions.modDivide(
                        BigInteger.ONE.subtract(A.multiply(x.pow(2))),
                        BigInteger.ONE.subtract(D.multiply(x.pow(2))),
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
        return "TwistedEdwards{a=" + A + ", d=" + D + ", p=" + P + "}";
    }
}
