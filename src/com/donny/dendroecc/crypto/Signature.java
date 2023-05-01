package com.donny.dendroecc.crypto;

import java.math.BigInteger;

public class Signature {
    public final BigInteger R, S;

    public Signature(BigInteger r, BigInteger s) {
        R = r;
        S = s;
    }
}
