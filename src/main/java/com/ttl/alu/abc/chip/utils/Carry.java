package com.ttl.alu.abc.chip.utils;

import com.ttl.alu.abc.utils.values.Bit;

import java.util.ArrayList;
import java.util.List;

import static com.ttl.alu.abc.utils.values.Bit.*;

public enum Carry {

    ON(ZERO), OFF(ONE);

    private final Bit value;

    Carry(Bit value) {
        this.value = value;
    }

    public Bit value() {
        return value;
    }

    public static Carry of(Bit c){
        return (c.value()) ? Carry.OFF : Carry.ON;
    }
    public static List<String> list(){

        ArrayList<String> list = new ArrayList<>();

        for(Carry e: values()){
            list.add(e.name() + " - ( " + e.value.toString() + " )");
        }

        return list;
    }

}
