package com.donny.dendroecc.crypto;

import com.donny.dendroecc.curves.Curve;
import com.donny.dendroecc.points.ECPoint;
import com.donny.dendroecc.points.PointPackingHandler;
import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DefinedCurve<C extends Curve, P extends ECPoint<P, C>> {
    public final String NAME, OID, DESCRIPTION;
    public final C E;
    public final P G;
    public final BigInteger N;
    public final int H;

    public DefinedCurve(String name, String oid, String desc, P g, BigInteger n, int h) {
        NAME = name;
        OID = oid;
        DESCRIPTION = desc;
        E = g.CURVE;
        G = g;
        N = n;
        H = h;
    }

    /**
     * generates a new private key and public key
     *
     * @return <code>ECCKeyPair</code> containing a random number between <code>1</code> and <code>N - 1</code>, and <code>G</code> multiplied by that private key
     */
    public ECCKeyPair<C, P> generateKeyPair() {
        BigInteger priv = Functions.randBeneath(N);
        return new ECCKeyPair<>(priv, G.multiply(priv));
    }

    /**
     * the first <code>m</code> bits of the hash of <code>message</code>, where <code>m</code> is the bit-length of N
     *
     * @param message the string being used in ECDSA
     * @return the bit-limited hash of <code>message</code>
     * @throws NoSuchAlgorithmException if your JVM doesn't support SHA-256; this should not be the case
     */
    private BigInteger getLimitedHash(byte[] message) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha.digest(message);
        int bits = Functions.bitCount(N);
        BigInteger z_c = new BigInteger(hash);
        BigInteger z;
        if (Functions.bitCount(z_c) > bits) {
            z = new BigInteger(z_c.toString(2).substring(0, bits), 2);
        } else {
            z = z_c;
        }
        return z;
    }

    /**
     * ECDSA signature of <code>message</code> using <code>privateKey</code>
     *
     * @param message    the message to be signed
     * @param privateKey the private key used to sign the message
     * @return the signature of <code>message</code>
     * @throws NoSuchAlgorithmException if your JVM doesn't support SHA-256; this should not be the case
     */
    public Signature sign(byte[] message, BigInteger privateKey) throws NoSuchAlgorithmException {
        BigInteger z = getLimitedHash(message);
        while (true) {
            BigInteger k = Functions.randBeneath(N);
            P signPoint = G.multiply(k);
            BigInteger r = signPoint.X.mod(N);
            if (r.compareTo(BigInteger.ZERO) > 0) {
                BigInteger s = Functions.modDivide(
                        z.add(r.multiply(privateKey)),
                        k,
                        N
                );
                if (s.compareTo(BigInteger.ZERO) > 0) {
                    return new Signature(r, s);
                }
            }
        }
    }

    /**
     * verify ECDSA signature <code>sig</code> of <code>message</code> using <code>publicKey</code>
     *
     * @param message   the message to be verified
     * @param sig       the signature of the message
     * @param publicKey the public key paired with the private key used to sign
     * @return true if the signature verifies, false otherwise
     * @throws NoSuchAlgorithmException if your JVM doesn't support SHA-256; this should not be the case
     */
    public boolean verifySignature(byte[] message, Signature sig, P publicKey) throws NoSuchAlgorithmException {
        if (!publicKey.validate()) {
            return false;
        }
        if (sig.R.compareTo(BigInteger.ZERO) <= 0 || sig.R.compareTo(N) >= 0) {
            return false;
        }
        if (sig.S.compareTo(BigInteger.ZERO) <= 0 || sig.S.compareTo(N) >= 0) {
            return false;
        }
        BigInteger z = getLimitedHash(message);
        BigInteger u1 = Functions.modDivide(z, sig.S, N);
        BigInteger u2 = Functions.modDivide(sig.R, sig.S, N);
        P testPoint = G.multiply(u1).add(publicKey.multiply(u2));
        if (testPoint == null) {
            return false;
        }
        return sig.R.mod(N).compareTo(testPoint.X.mod(N)) == 0;
    }

    /**
     * this is a simple way to get a correctly parameterized <code>PointPackingHandler</code>
     *
     * @return a new <code>PointPackingHandler</code>
     */
    public PointPackingHandler<C, P> getPackingHandler() {
        return new PointPackingHandler<>();
    }
}
