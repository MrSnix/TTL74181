package com.ttl.alu.abc.gates;

import com.ttl.alu.abc.utils.values.Bit;

public final class XOR {

    public static final String name = "XOR";
    public static final String symbol = "⊕";

    private XOR(){}

    public static Bit exclusive_disjunction(Bit b0, Bit b1){
       return OR.disjunction(AND.conjunction(b0, NOT.negate(b1)), AND.conjunction(NOT.negate(b0), b1));
    }

}
