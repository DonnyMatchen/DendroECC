package com.donny.dendroecc.util;

import java.math.BigInteger;

public class Galois {
    /**
     * multiplication in GF(2^m)
     *
     * @param a a GF(2^m) number
     * @param b a GF(2^m) number
     * @return <code>a * b</code> in GF(2^m)
     */
    public static BigInteger multiply(BigInteger a, BigInteger b) {
        BigInteger c = BigInteger.ZERO;
        boolean[] aa = Functions.getLsbVector(a);
        for (int i = 0; i < aa.length; i++) {
            if (aa[i]) {
                c = c.xor(b.multiply(BigInteger.ONE.shiftLeft(i)));
            }
        }
        return c;
    }

    /**
     * Modular multiplication in GF(2^m)
     *
     * @param a a GF(2^m) number
     * @param b a GF(2^m) number
     * @param n a GF(2^m) modulus
     * @return <code>a * b mod n</code> in GF(2^m)
     */
    public static BigInteger modMult(BigInteger a, BigInteger b, BigInteger n) {
        return modulus(multiply(a, b), n);
    }

    /**
     * division in GF(2^m)
     *
     * @param a a GF(2^m) number
     * @param b a GF(2^m) number
     * @return <code>a / b</code> in GF(2^m) {quotient, remainder}
     */
    public static BigInteger divide(BigInteger a, BigInteger b) {
        int a_ = Functions.bitCount(a);
        int b_ = Functions.bitCount(b);
        BigInteger c = BigInteger.ZERO;
        if (a_ >= b_) {
            for (int i = a_ - b_; i >= 0; i--) {
                BigInteger bit = BigInteger.ONE.shiftLeft(i);
                BigInteger bb = b.multiply(bit);
                int bb_ = Functions.bitCount(bb);
                a_ = Functions.bitCount(a);
                if (a_ == bb_) {
                    a = a.xor(bb);
                    c = c.xor(bit);
                }
            }
        }
        return c;
    }

    /**
     * modular division in GF(2^m)
     *
     * @param a a GF(2^m) number
     * @param b a GF(2^m) number
     * @param n a GF(2^m) modulus
     * @return <code>a * b^-1 mod n</code> in GF(2^m)
     */
    public static BigInteger modDivide(BigInteger a, BigInteger b, BigInteger n) {
        return modMult(a, inverse(b, n), n);
    }

    /**
     * modular multiplicative inverse in GF(2^m)
     * [Algorithm 3] from <a href="https://www.lirmm.fr/arith18/papers/kobayashi-AlgorithmInversionUsingPolynomialMultiplyInstruction.pdf">An Algorithm for Inversion in GF(2^m) Suitable for Implementation Using a Polynomial Multiply Instruction on GF(2)</a>
     *
     * @param a an integer
     * @param n modulus
     * @return <code>a^-1 mod n</code>
     */
    public static BigInteger inverse(BigInteger a, BigInteger n) {
        BigInteger s = n, r = a, v = BigInteger.ZERO, u = BigInteger.ONE;
        int delta = 0;
        int m = Functions.bitCount(n) - 1;
        for (int i = 1; i <= 2 * m; i++) {
            if (r.and(BigInteger.ONE.shiftLeft(m)).compareTo(BigInteger.ZERO) == 0) {
                r = r.shiftLeft(1);
                u = u.shiftLeft(1);
                delta++;
            } else {
                if (s.and(BigInteger.ONE.shiftLeft(m)).compareTo(BigInteger.ZERO) != 0) {
                    s = s.xor(r);
                    v = v.xor(u);
                }
                s = s.shiftLeft(1);
                if (delta == 0) {
                    BigInteger temp = r;
                    r = s;
                    s = temp;
                    temp = v.shiftLeft(1);
                    v = u;
                    u = temp;
                    delta = 1;
                } else {
                    u = u.shiftRight(1);
                    delta--;
                }
            }
        }
        return u;
    }

    /**
     * modulus in GF(2^m)
     *
     * @param a a GF(2^m) number
     * @param n a GF(2^m) modulus
     * @return <code>a mod n</code> in GF(2^m)
     */
    public static BigInteger modulus(BigInteger a, BigInteger n) {
        int a_ = Functions.bitCount(a);
        int n_ = Functions.bitCount(n);
        if (a_ >= n_) {
            for (int i = a_ - n_; i >= 0; i--) {
                BigInteger nn = n.shiftLeft(i);
                int nn_ = Functions.bitCount(nn);
                a_ = Functions.bitCount(a);
                if (a_ == nn_) {
                    a = a.xor(nn);
                }
            }
        }
        return a;
    }

    /**
     * modular exponentiation in GF(2^m)
     *
     * @param a a GF(2^m) number
     * @param e an exponent
     * @param n a GF(2^m) modulus
     * @return <code>a^e mod n</code> in GF(2^m)
     */
    public static BigInteger modExp(BigInteger a, BigInteger e, BigInteger n) {
        boolean[] pp = Functions.getLsbVector(e);
        BigInteger z = BigInteger.ONE;
        for (boolean bit : pp) {
            if (bit) {
                z = modulus(Galois.multiply(z, a), n);
            }
            a = modulus(multiply(a, a), n);
        }
        return z;
    }

    /**
     * the modular square root in GF(2^m)
     * this is done by finding <code>a^(m - 1) mod n</code>
     * <code>m = Functions.bitCount(n) - 1</code>
     *
     * @param a a GF(2^m) number
     * @param n a GF(2^m) modulus
     * @return <code>a^(m - 1) mod n</code> in GF(2^m)
     */
    public static BigInteger modSqrt(BigInteger a, BigInteger n) {
        return modExp(a, BigInteger.ONE.shiftLeft(Functions.bitCount(n) - 2), n);
    }

    /**
     * the half trace of <code>a</code> in mod n in GF(2^m)
     *
     * @param a a GF(2^m) number
     * @param n a GF(2^m) modulus
     * @return the half trace of <code>a</code> in mod n in GF(2^m)
     */
    public static BigInteger halfTrace(BigInteger a, BigInteger n) {
        int m = Functions.bitCount(n) - 1;
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i <= (m - 1) / 2; i++) {
            sum = sum.xor(a);
            a = modExp(a, BigInteger.valueOf(4), n);
        }
        return sum;
    }

    /**
     * the trace of <code>a</code> in mod n in GF(2^m)
     *
     * @param a a GF(2^m) number
     * @param n a GF(2^m) modulus
     * @return the trace of a in mod n in GF(2^m)
     */
    public static BigInteger trace(BigInteger a, BigInteger n) {
        int m = Functions.bitCount(n) - 1;
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < m; i++) {
            sum = sum.xor(a);
            a = modulus(multiply(a, a), n);
        }
        return sum;
    }
}
