package com.donny.dendroecc.crypto;

import com.donny.dendroecc.curves.*;
import com.donny.dendroecc.points.*;
import com.donny.dendroecc.util.Functions;
import com.donny.dendroroot.json.JsonObject;
import com.donny.dendroroot.json.JsonString;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Registry {
    private static final HashMap<String, DefinedCurve> REGISTRY = new HashMap<>();

    /**
     * adds a <code>DefinedCurve</code> to the registry from a JSON object
     *
     * @param obj raw json object
     */
    public static void register(JsonObject obj) {
        StringBuilder name = new StringBuilder(obj.getString("name").getString());
        String oid;
        if (obj.containsKey("oid")) {
            oid = obj.getString("oid").getString();
        } else {
            oid = "";
        }
        if (obj.containsKey("aliases")) {
            for (JsonString alias : obj.getArray("aliases").getStringArray()) {
                name.append(", ").append(alias);
            }
        }
        String type = obj.getString("form").getString();
        switch (type) {
            case "Weierstrass" -> {
                type = obj.getObject("field").getString("type").getString();
                if (type.equals("Prime")) {
                    DefinedCurve<WeierstrassPrime, WpECPoint> wpParams = new DefinedCurve<>(
                            name.toString(),
                            oid,
                            obj.getString("desc").getString(),
                            new WpECPoint(
                                    Functions.strip(obj.getObject("generator").getObject("x").getString("raw").getString()),
                                    Functions.strip(obj.getObject("generator").getObject("y").getString("raw").getString()),
                                    new WeierstrassPrime(
                                            Functions.strip(obj.getObject("params").getObject("a").getString("raw").getString()),
                                            Functions.strip(obj.getObject("params").getObject("b").getString("raw").getString()),
                                            Functions.strip(obj.getObject("field").getString("p").getString())
                                    )
                            ),
                            Functions.strip(obj.getString("order").getString()),
                            Functions.strip(obj.getString("cofactor").getString()).intValue()
                    );
                    REGISTRY.put(obj.getString("name").getString().toLowerCase(), wpParams);
                    if (obj.containsKey("oid")) {
                        REGISTRY.put(obj.getString("oid").getString(), wpParams);
                    }
                    if (obj.containsKey("aliases")) {
                        for (JsonString alias : obj.getArray("aliases").getStringArray()) {
                            REGISTRY.put(alias.getString().split("/")[1].toLowerCase(), wpParams);
                        }
                    }
                } else if (type.equals("Binary")) {
                    BigInteger f = BigInteger.ZERO;
                    for (JsonObject param : obj.getObject("field").getArray("poly").getObjectArray()) {
                        f = f.xor(BigInteger.ONE.shiftLeft(param.getDecimal("power").decimal.intValue()));
                    }
                    DefinedCurve<WeierstrassBinary, WbECPoint> wbParams = new DefinedCurve<>(
                            name.toString(),
                            oid,
                            obj.getString("desc").getString(),
                            new WbECPoint(
                                    Functions.strip(obj.getObject("generator").getObject("x").getString("raw").getString()),
                                    Functions.strip(obj.getObject("generator").getObject("y").getString("raw").getString()),
                                    new WeierstrassBinary(
                                            Functions.strip(obj.getObject("params").getObject("a").getString("raw").getString()),
                                            Functions.strip(obj.getObject("params").getObject("b").getString("raw").getString()),
                                            f
                                    )
                            ),
                            Functions.strip(obj.getString("order").getString()),
                            Functions.strip(obj.getString("cofactor").getString()).intValue()
                    );
                    REGISTRY.put(obj.getString("name").getString().toLowerCase(), wbParams);
                    if (obj.containsKey("oid")) {
                        REGISTRY.put(obj.getString("oid").getString(), wbParams);
                    }
                    if (obj.containsKey("aliases")) {
                        for (JsonString alias : obj.getArray("aliases").getStringArray()) {
                            REGISTRY.put(alias.getString().split("/")[1].toLowerCase(), wbParams);
                        }
                    }
                }
            }
            case "TwistedEdwards" -> {
                type = obj.getObject("field").getString("type").getString();
                if (type.equals("Prime")) {
                    DefinedCurve<TwistedEdwards, TeECPoint> tParams = new DefinedCurve<>(
                            name.toString(),
                            oid,
                            obj.getString("desc").getString(),
                            new TeECPoint(
                                    Functions.strip(obj.getObject("generator").getObject("x").getString("raw").getString()),
                                    Functions.strip(obj.getObject("generator").getObject("y").getString("raw").getString()),
                                    new TwistedEdwards(
                                            Functions.strip(obj.getObject("params").getObject("a").getString("raw").getString()),
                                            Functions.strip(obj.getObject("params").getObject("d").getString("raw").getString()),
                                            Functions.strip(obj.getObject("field").getString("p").getString())
                                    )
                            ),
                            Functions.strip(obj.getString("order").getString()),
                            Functions.strip(obj.getString("cofactor").getString()).intValue()
                    );
                    REGISTRY.put(obj.getString("name").getString().toLowerCase(), tParams);
                    if (obj.containsKey("oid")) {
                        REGISTRY.put(obj.getString("oid").getString(), tParams);
                    }
                    if (obj.containsKey("aliases")) {
                        for (JsonString alias : obj.getArray("aliases").getStringArray()) {
                            REGISTRY.put(alias.getString().split("/")[1].toLowerCase(), tParams);
                        }
                    }
                }
            }
            case "Montgomery" -> {
                type = obj.getObject("field").getString("type").getString();
                if (type.equals("Prime")) {
                    DefinedCurve<Montgomery, MECPoint> mParams = new DefinedCurve<>(
                            name.toString(),
                            oid,
                            obj.getString("desc").getString(),
                            new MECPoint(
                                    Functions.strip(obj.getObject("generator").getObject("x").getString("raw").getString()),
                                    Functions.strip(obj.getObject("generator").getObject("y").getString("raw").getString()),
                                    new Montgomery(
                                            Functions.strip(obj.getObject("params").getObject("a").getString("raw").getString()),
                                            Functions.strip(obj.getObject("params").getObject("b").getString("raw").getString()),
                                            Functions.strip(obj.getObject("field").getString("p").getString())
                                    )
                            ),
                            Functions.strip(obj.getString("order").getString()),
                            Functions.strip(obj.getString("cofactor").getString()).intValue()
                    );
                    REGISTRY.put(obj.getString("name").getString().toLowerCase(), mParams);
                    if (obj.containsKey("oid")) {
                        REGISTRY.put(obj.getString("oid").getString(), mParams);
                    }
                    if (obj.containsKey("aliases")) {
                        for (JsonString alias : obj.getArray("aliases").getStringArray()) {
                            REGISTRY.put(alias.getString().split("/")[1].toLowerCase(), mParams);
                        }
                    }
                }
            }
            case "Edwards" -> {
                type = obj.getObject("field").getString("type").getString();
                if (type.equals("Prime")) {
                    DefinedCurve<Edwards, EdECPoint> eParams = new DefinedCurve<>(
                            name.toString(),
                            oid,
                            obj.getString("desc").getString(),
                            new EdECPoint(
                                    Functions.strip(obj.getObject("generator").getObject("x").getString("raw").getString()),
                                    Functions.strip(obj.getObject("generator").getObject("y").getString("raw").getString()),
                                    new Edwards(
                                            Functions.strip(obj.getObject("params").getObject("c").getString("raw").getString()),
                                            Functions.strip(obj.getObject("params").getObject("d").getString("raw").getString()),
                                            Functions.strip(obj.getObject("field").getString("p").getString())
                                    )
                            ),
                            Functions.strip(obj.getString("order").getString()),
                            Functions.strip(obj.getString("cofactor").getString()).intValue()
                    );
                    REGISTRY.put(obj.getString("name").getString().toLowerCase(), eParams);
                    if (obj.containsKey("oid")) {
                        REGISTRY.put(obj.getString("oid").getString(), eParams);
                    }
                    if (obj.containsKey("aliases")) {
                        for (JsonString alias : obj.getArray("aliases").getStringArray()) {
                            REGISTRY.put(alias.getString().split("/")[1].toLowerCase(), eParams);
                        }
                    }
                }
            }
        }
    }

    /**
     * retrieves a <code>DefiendCurve</code> using an enforced lowercase key
     *
     * @param key a curve name or oid
     * @return a DefinedCurve if one matches the provided key, or null if there is no match
     */
    public static DefinedCurve get(String key) {
        return REGISTRY.get(key.toLowerCase());
    }

    /**
     * empties the registry
     */
    public static void clear() {
        REGISTRY.clear();
    }

    /**
     * removes a <code>DefiendCurve</code> using an enforced lowercase key and returns it
     *
     * @param key a curve name or oid
     * @return the removed DefinedCurve if one matches the provided key, or null if there is no match
     */
    public static DefinedCurve remove(String key) {
        return REGISTRY.remove(key.toLowerCase());
    }

    /**
     * lists all keys in the registry
     *
     * @return an <code>ArrayList</code> of keys in the registry
     */
    public static ArrayList<String> listKeys() {
        return new ArrayList<>(REGISTRY.keySet());
    }
}
