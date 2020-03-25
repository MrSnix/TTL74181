package com.ttl.alu.abc.gates;

import com.ttl.alu.abc.utils.values.Bit;

import static com.ttl.alu.abc.utils.values.Bit.ONE;
import static com.ttl.alu.abc.utils.values.Bit.ZERO;

public final class OR {

    public static final String name = "OR";
    public static final String symbol = "||";

    private OR(){}

    public static Bit disjunction(Bit b0, Bit b1){
        return (b0.value() || b1.value()) ? ONE : ZERO;
    }

    public static Bit disjunction(Bit b0, Bit b1, Bit b2){
        return (b0.value() || b1.value() || b2.value()) ? ONE : ZERO;
    }

    public static Bit disjunction(Bit b0, Bit b1, Bit b2, Bit b3){
        return (b0.value() || b1.value() || b2.value() || b3.value()) ? ONE : ZERO;
    }

    public static Bit disjunction(Bit b0, Bit b1, Bit b2, Bit b3, Bit b4){
        return (b0.value() || b1.value() || b2.value() || b3.value() || b4.value()) ? ONE : ZERO;
    }

}
