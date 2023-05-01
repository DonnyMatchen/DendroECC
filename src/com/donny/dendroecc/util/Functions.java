package com.donny.dendroecc.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Functions {
    /**
     * converts bits in a BigInteger to a boolean array, ordered from LSB to MSB
     *
     * @param x a positive integer
     * @return boolean array of bits from LSB to MSB
     * @throws IllegalArgumentException if x is negative
     */
    public static boolean[] getLsbVector(BigInteger x) {
        String z = x.toString(2);
        if (z.contains("-")) {
            throw new IllegalArgumentException("Only positive BigIntegers can be inverted");
        } else {
            boolean[] out = new boolean[z.length()];
            for (int i = 0; i < out.length; i++) {
                int j = out.length - 1 - i;
                out[i] = z.charAt(j) == '1';
            }
            return out;
        }
    }

    /**
     * returns the number of bits required to express x
     *
     * @param x a positive integer
     * @return the minimum number of bits to express x unsigned
     */
    public static int bitCount(BigInteger x) {
        if (x.compareTo(BigInteger.ZERO) == 0) {
            return 0;
        }
        byte[] bytes = x.toByteArray();
        int y = 0;
        if (bytes[0] == 0) {
            y = 1;
        }
        int bitLength = (bytes.length - 1 - y) * 8;
        for (int i = 8; i > 0; i--) {
            int c = 1 << i - 1;
            if ((bytes[y] & c) != 0) {
                bitLength += i;
                break;
            }
        }
        return bitLength;
    }

    /**
     * modular division
     *
     * @param a dividend
     * @param b divisor
     * @param n modulus
     * @return <code>a / b mod n</code>
     * @throws IllegalArgumentException if b and n are not coprime or b is 0
     */
    public static BigInteger modDivide(BigInteger a, BigInteger b, BigInteger n) {
        BigInteger[] gcd = extendedEuclid(b, n);
        if (gcd[0].compareTo(BigInteger.ONE) == 0) {
            BigInteger z = gcd[1].mod(n);
            return a.multiply(z).mod(n);
        } else {
            throw new IllegalArgumentException("b and n are not coprime, or b is 0.");
        }
    }

    /**
     * modular square root using Shank Tonelli’s algorithm
     * adapted from <a href="https://gist.github.com/LaurentMazare/6745649">LaurentMazare/tonelli_shanks.c</a>
     *
     * @param n modular square
     * @param p modulus
     * @return <code>sqrt(n) mod p</code>, or <code>null</code> if <code>n</code> is not a square in <code>mod p</code>
     */
    public static BigInteger modSqrt(BigInteger n, BigInteger p) {
        BigInteger s = BigInteger.ZERO;
        BigInteger q = p.subtract(BigInteger.ONE);
        while (q.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
            q = q.divide(BigInteger.TWO);
            s = s.add(BigInteger.ONE);
        }
        if (s.compareTo(BigInteger.ONE) == 0) {
            BigInteger r = modExp(n, p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
            if (r.multiply(r).mod(p).compareTo(n) == 0) {
                return r;
            } else {
                return null;
            }
        }
        BigInteger z = BigInteger.TWO;
        while (true) {
            BigInteger zz = modExp(z, p.subtract(BigInteger.ONE).divide(BigInteger.TWO), p);
            if (zz.compareTo(p.subtract(BigInteger.ONE)) == 0) {
                break;
            }
            z = z.add(BigInteger.ONE);
        }
        BigInteger c = modExp(z, q, p);
        BigInteger r = modExp(n, q.add(BigInteger.ONE).divide(BigInteger.TWO), p);
        BigInteger t = modExp(n, q, p);
        BigInteger m = s;
        while (t.compareTo(BigInteger.ONE) != 0) {
            BigInteger tt = t;
            BigInteger i = BigInteger.ZERO;
            while (tt.compareTo(BigInteger.ONE) != 0) {
                tt = tt.pow(2).mod(p);
                i = i.add(BigInteger.ONE);
                if (i.compareTo(m) == 0) {
                    return null;
                }
            }
            BigInteger b = modExp(c, modExp(BigInteger.TWO, m.subtract(i).subtract(BigInteger.ONE), p.subtract(BigInteger.ONE)), p);
            BigInteger b2 = b.pow(2).mod(p);
            r = r.multiply(b).mod(p);
            t = t.multiply(b2).mod(p);
            c = b2;
            m = i;
        }
        if (r.pow(2).mod(p).compareTo(n) == 0) {
            return r;
        } else {
            return null;
        }
    }

    /**
     * Implementation of the Extended Euclidean algorithm
     *
     * @param p an integer
     * @param q an integer
     * @return {GCD, X, Y}; GCD is the greatest common divisor of <code>p</code> and <code>q</code>.  <code>X</code> and <code>Y</code> are solutions to <code>p * X + q * Y = GCD</code>
     */
    public static BigInteger[] extendedEuclid(BigInteger p, BigInteger q) {
        if (q.compareTo(BigInteger.ZERO) == 0) {
            return new BigInteger[]{p, BigInteger.ONE, BigInteger.ZERO};
        }
        BigInteger[] recursive = extendedEuclid(q, p.mod(q));
        BigInteger d = recursive[0];
        BigInteger a = recursive[2];
        BigInteger b = recursive[1].subtract(p.divide(q).multiply(recursive[2]));
        return new BigInteger[]{d, a, b};
    }

    /**
     * This function strips the "0x" from the start of a hex string, if present
     *
     * @param input raw string
     * @return processed string
     */
    public static BigInteger strip(String input) {
        if (input.contains("0x")) {
            input = input.substring(2);
        }
        return new BigInteger(input, 16);
    }

    /**
     * generates a secure random number between <code>1</code> and <code>n - 1</code>
     *
     * @param n the non-inclusive upper bound
     * @return a random number between <code>1</code> and <code>n - 1</code>
     */
    public static BigInteger randBeneath(BigInteger n) {
        SecureRandom rand = new SecureRandom();
        while (true) {
            byte[] bytes = n.toByteArray();
            if (bytes[0] == 0) {
                byte[] temp = new byte[bytes.length - 1];
                System.arraycopy(bytes, 1, temp, 0, temp.length);
                bytes = temp;
            }
            rand.nextBytes(bytes);
            if (bytes[0] < 0) {
                byte[] temp = new byte[bytes.length + 1];
                System.arraycopy(bytes, 0, temp, 1, bytes.length);
                bytes = temp;
            }
            BigInteger k = new BigInteger(bytes);
            if (k.compareTo(BigInteger.ZERO) > 0 && k.compareTo(n) < 0) {
                return k;
            }
        }
    }

    /**
     * raises <code>base</code> to <code>exponent</code> with square and multiply
     *
     * @param base     base
     * @param exponent exponent
     * @return <code>base ^ exponent</code>
     */
    public static BigInteger exp(BigInteger base, BigInteger exponent) {
        boolean[] map = Functions.getLsbVector(exponent);
        BigInteger out = BigInteger.ZERO;
        BigInteger temp = base;
        for (boolean bit : map) {
            if (bit) {
                out = out.add(temp);
            }
            temp = temp.pow(2);
        }
        return out;
    }

    /**
     * raises <code>base</code> to <code>exponent</code> mod <code>modulus</code> with square and multiply
     *
     * @param base     base
     * @param exponent exponent
     * @param modulus  modulus
     * @return <code>(base ^ exponent) mod modulus</code>
     */
    public static BigInteger modExp(BigInteger base, BigInteger exponent, BigInteger modulus) {
        boolean[] map = Functions.getLsbVector(exponent);
        BigInteger out = BigInteger.ONE;
        BigInteger temp = base;
        for (boolean bit : map) {
            if (bit) {
                out = out.multiply(temp).mod(modulus);
            }
            temp = temp.pow(2).mod(modulus);
        }
        return out;
    }
}
