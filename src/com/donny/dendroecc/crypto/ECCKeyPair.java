package com.donny.dendroecc.crypto;

import com.donny.dendroecc.curves.Curve;
import com.donny.dendroecc.points.ECPoint;

import java.math.BigInteger;

public class ECCKeyPair<C extends Curve, P extends ECPoint<P, C>> {
    public final BigInteger PRIVATE;
    public final P PUBLIC;

    public ECCKeyPair(BigInteger r, P u) {
        PRIVATE = r;
        PUBLIC = u;
    }
}
