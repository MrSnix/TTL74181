package com.ttl.alu.abc.chip.utils;

import com.ttl.alu.abc.utils.values.Bit;

import java.util.ArrayList;
import java.util.List;

import static com.ttl.alu.abc.utils.values.Bit.ONE;
import static com.ttl.alu.abc.utils.values.Bit.ZERO;

public enum Mode {

    ARITHMETIC(ZERO), LOGIC(ONE);

    private final Bit value;

    Mode(Bit value) {
        this.value = value;
    }

    public Bit value() {
        return value;
    }

    public static List<String> list(){

        ArrayList<String> list = new ArrayList<>();

        for(Mode e: values()){
            list.add(e.name());
        }

        return list;
    }

}
