package com.donny.dendroecc.points;

import com.donny.dendroecc.curves.Curve;
import com.donny.dendroecc.util.Functions;

import java.math.BigInteger;

public class PointPackingHandler<C extends Curve, P extends ECPoint<P, C>> {
    /**
     * packs a public key
     *
     * @param point public key
     * @return the packed public key
     */
    public BigInteger pack(P point) {
        byte[] bytes = point.X.toByteArray();
        byte first = (byte) (2 + point.Y.mod(BigInteger.TWO).intValue());
        if (bytes[0] != 0) {
            byte[] temp = new byte[bytes.length + 1];
            System.arraycopy(bytes, 0, temp, 1, bytes.length);
            bytes = temp;
        }
        bytes[0] = first;
        return new BigInteger(bytes);
    }

    /**
     * packs a public key to a hex string
     *
     * @param point public key
     * @return the packed public key as a hex string
     */
    public String packToString(P point) {
        return "0x" + pack(point).toString(16);
    }

    /**
     * unpacks a public key
     *
     * @param hex   packed public key
     * @param curve related curve
     * @return the coordinates of the public key: [x, y]
     */
    public BigInteger[] unpack(BigInteger hex, C curve) {
        byte[] temp = hex.toByteArray();
        int lsb = temp[0] % 2;
        temp[0] = 0;
        BigInteger x = new BigInteger(temp);
        return new BigInteger[]{x, curve.getY(x)[lsb]};
    }

    /**
     * unpacks a public key from a hex string
     *
     * @param hex   packed public key as a hex string
     * @param curve related curve
     * @return the coordinates of the public key: [x, y]
     */
    public BigInteger[] unpack(String hex, C curve) {
        return unpack(Functions.strip(hex), curve);
    }
}
