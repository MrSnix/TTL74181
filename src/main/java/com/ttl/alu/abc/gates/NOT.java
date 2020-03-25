package com.ttl.alu.abc.gates;

import com.ttl.alu.abc.utils.values.Bit;

import static com.ttl.alu.abc.utils.values.Bit.ONE;
import static com.ttl.alu.abc.utils.values.Bit.ZERO;

public final class NOT {

    public static final String name = "NOT";
    public static final String symbol = "¬";

    private NOT(){}

    public static Bit negate(Bit b){
        return (b.value()) ? ZERO : ONE;
    }

}
