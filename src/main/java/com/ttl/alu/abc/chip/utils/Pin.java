package com.ttl.alu.abc.chip.utils;

import com.ttl.alu.abc.utils.values.Bit;

public final class Pin {

    private final String name;
    private final Bit state;

    public Pin(String name, Bit state) {
        this.name = name;
        this.state = state;
    }

    public static String valueOf(Pin[] f) {

        StringBuilder sb = new StringBuilder();

        for(Pin p : f){
            sb.append(p.state.toString());
        }

        return sb.toString();

    }

    public String name(){ return name;}

    public Bit state() {
        return state;
    }

    @Override
    public String toString() {
        return name + ": " + state.toString();
    }
}
