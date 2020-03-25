package com.ttl.alu.abc.gates;

import com.ttl.alu.abc.utils.values.Bit;

public final class NOR {

    public static final String name = "NOR";
    public static final String symbol = "↓";

    private NOR() {

    }

    public static Bit joint_denial(Bit b0, Bit b1){
        return NOT.negate(OR.disjunction(b0,b1));
    }

    public static Bit joint_denial(Bit b0, Bit b1, Bit b2){
        return NOT.negate(OR.disjunction(b0,b1,b2));
    }

    public static Bit joint_denial(Bit b0, Bit b1, Bit b2, Bit b3){
        return NOT.negate(OR.disjunction(b0,b1,b2,b3));
    }

    public static Bit joint_denial(Bit b0, Bit b1, Bit b2, Bit b3, Bit b4){
        return NOT.negate(OR.disjunction(b0,b1,b2,b3,b4));
    }

}
