# DESCRIPTION
This is an extension of DendroRoot that adds functionality around elliptic curves and elliptic curve cryptography.

# FUNCTIONALITY
**Point Validation**<br>
*com.donny.dendroecc.points.ECPoint.validate()*<br>
**Point Addition and Multiplication**<br>
*com.donny.dendroecc.points.ECPoint.add(b)*<br>
*com.donny.dendroecc.points.ECPoint.doublePoint()*<br>
*com.donny.dendroecc.points.ECPoint.multiply(multiplicand)*<br>
**Generate Key Pairs**<br>
*com.donny.dendroecc.crypto.DefinedCurve.generateKeyPair()*<br>
**Pack and Unpack Public Key:**<br>
*com.donny.dendroecc.crypto.DefinedCurve.getPackingHandler().pack(point)*<br>
*com.donny.dendroecc.crypto.DefinedCurve.getPackingHandler().unpack(hex, curve)*<br>
**Find ±y Given x**:<br>
*com.donny.dendroecc.curves.Curve.getY(x)*<br>
**Sign Message and Verify Signatures**:<br>
*com.donny.dendroecc.crypto.DefinedCurve.sign(message, privateKey)*<br>
*com.donny.dendroecc.crypto.DefinedCurve.verifySignature(message, signature, publicKey)*<br>
**Load DefinedCurve JSONs from data/stdcrv**:<br>
*com.donny.dendroecc.util.Loader.loadStandardCurves()*

### FUNCTIONS
**getLsbVector(x)**: returns a boolean array ordered from lsb to msb<br>
**bitCount(x)**: returns the bit count of x
**modDivide(a, b, n)**: a / b mod n, or null if b ≡ 0 mod n<br>
**modSqrt(n, p)**: sqrt(n) mod p, or null if n is not a square in mod p<br>
**extendedEuclid(p, q)**: the three extended euclid parameters.  If used for modular division, GCD should be 1, and the second parameter is 1 / p mod q<br>
**strip(input)**: converts from "0x..." to BigInteger<br>
**randBeneath(n)**: a number securely generated random number less than n<br>
**exp(base, exponent)**: base ^ exponent, unlike BigInteger.pow() this handles a BigInteger power<br>
**modExp(base, exponent, modulus)**: base ^ exponent mod modulus, modulus is applied at each step to save on memory

### GALOIS FUNCTIONS
**multiply(a, b)**: GF(2^m) multiplication<br>
**modMult(a, b, n)**: modulus(multiply(a, b), n)<br>
**divide(a, b)**: GF(2^m) division {quotient, remainder}<br>
**modDivide(a, b, n)**: multiply(a, inverse(b, n))<br>
**inverse(a, n)**: modular multiplicative inverse in GF(2^m)<br>
**modulus(a, n)**: modulus in GF(2^m)<br>
**modExp(a, e, n)**: modular exponentiation in GF(2^m)<br>
**modSqrt(a, n)**: modular square root in GF(2^m)<br>
**halfTrace(a, n)**: the half trace of a mod n<br>
**trace(a, n)**: the trace of a mod n<br>

# RESOURCES
[Standard Elliptic Curves](https://neuromancer.sk/std/) - list of standard curve parameters including JSONs<br>
[LaurentMazare/tonelli_shanks.c](https://gist.github.com/LaurentMazare/6745649) - Shank Tonelli implementation
[A helpful paper for understanding binary fields](https://www.lirmm.fr/arith18/papers/kobayashi-AlgorithmInversionUsingPolynomialMultiplyInstruction.pdf)

# SUPPORTED CURVES AND FIELDS
**Weierstrass:** Prime, Binary<br>
**Twisted Edwards:** Prime<br>
**Montgomery:** Prime<br>
**Edwards:** Prime<br><br>
*Note: the Microsoft FourQ curve, Fp254n2BNa, and other extension fields other than Weierstrass Binary are not supported at this time*

# INCLUDED STANDARD ECC PARAMETERS
## BY OID
1.2.250.1.223.101.256.1, 1.2.840.10045.3.1.1, 1.2.840.10045.3.1.7, 1.3.36.3.3.2.8.1.1.7, 1.3.36.3.3.2.8.1.1.11, 1.3.36.3.3.2.8.1.1.13, 1.3.132.0.1, 1.3.132.0.2, 1.3.132.0.3, 1.3.132.0.4, 1.3.132.0.5, 1.3.132.0.6, 1.3.132.0.7, 1.3.132.0.8, 1.3.132.0.9, 1.3.132.0.10, 1.3.132.0.15, 1.3.132.0.16, 1.3.132.0.17, 1.3.132.0.22, 1.3.132.0.23, 1.3.132.0.24, 1.3.132.0.25, 1.3.132.0.26, 1.3.132.0.27, 1.3.132.0.28, 1.3.132.0.29, 1.3.132.0.30, 1.3.132.0.31, 1.3.132.0.32, 1.3.132.0.33, 1.3.132.0.34, 1.3.132.0.35, 1.3.132.0.36, 1.3.132.0.37, 1.3.132.0.38, 1.3.132.0.39

## BY NAME
ansip160k1, ansip160r1, ansip160r2, ansip192k1, ansip224k1, ansip224r1, ansip256k1, ansip384r1, ansip521r1, ansit163k1, ansit163r1, ansit163r2, ansit193r1, ansit193r2, ansit233k1, ansit233r1, ansit239k1, ansit283k1, ansit283r1, ansit409k1, ansit409r1, ansit571k1, ansit571r1, b-163, b-233, b-283, b-409, b-571, bn158, bn190, bn222, bn254, bn286, bn318, bn350, bn382, bn414, bn446, bn478, bn510, bn542, bn574, bn606, bn638, brainpoolp256r1, brainpoolp384r1, brainpoolp512r1, curve22103, curve25519, curve383187, curve41417, e-222, e-382, e-521, ed25519, ed448, ed448-goldilocks, fp254bnb, frp256v1, gost256, gost512, jubjub, k-163, k-233, k-283, k-409, k-571, m-221, m-383, m-511, mdc201601, p-192, p-224, p-256, p-384, p-521, prime192v1, prime256v1, secp112r1, secp112r2, secp128r1, secp128r2, secp160k1, secp160r1, secp160r2, secp192k1, secp192r1, secp224k1, secp224r1, secp256k1, secp256r1, secp384r1, secp521r1, sect113r1, sect113r2, sect131r1, sect131r2, sect163k1, sect163r1, sect163r2, sect193r1, sect193r2, sect233k1, sect233r1, sect239k1, sect283k1, sect283r1, sect409k1, sect409r1, sect571k1, sect571r1, wap-wsg-idm-ecid-wtls10, wap-wsg-idm-ecid-wtls11, wap-wsg-idm-ecid-wtls12, wap-wsg-idm-ecid-wtls3, wap-wsg-idm-ecid-wtls4, wap-wsg-idm-ecid-wtls6, wap-wsg-idm-ecid-wtls7