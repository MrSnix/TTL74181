package com.ttl.alu.abc.chip.encoder;

import com.ttl.alu.abc.chip.utils.Carry;
import com.ttl.alu.abc.chip.utils.Mode;
import com.ttl.alu.abc.utils.values.Bit;

public final class Operation {

    private final Bit[] opcode;
    private final Bit[] a;
    private final Bit[] b;
    private final Mode mode;
    private final Carry carry;
    private final int alu;

    public Operation(Bit[] opcode, Bit[] a, Bit[] b, Mode mode, Carry carry, int alu) {
        this.opcode = opcode;
        this.a = a;
        this.b = b;
        this.mode = mode;
        this.carry = carry;
        this.alu = alu;
    }

    public Bit[] getOpcode() {
        return opcode;
    }

    public Bit[] getA() {
        return a;
    }

    public Bit[] getB() {
        return b;
    }

    public Mode getMode() {
        return mode;
    }

    public Carry getCarry() {
        return carry;
    }

    public int getAlu() {
        return alu;
    }
}
