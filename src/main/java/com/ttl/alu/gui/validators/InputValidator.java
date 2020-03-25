package com.ttl.alu.gui.validators;

import com.ttl.alu.abc.chip.encoder.Operation;
import com.ttl.alu.abc.chip.utils.Carry;
import com.ttl.alu.abc.chip.utils.Mode;
import com.ttl.alu.abc.utils.values.Bit;
import com.ttl.alu.gui.validators.ex.ValidationException;

public final class InputValidator {

    private static final int DEFAULT_BIT = 4;
    private static final int RADIX = 2;

    private final String opcode;

    private String a;
    private String b;

    private final String carry;
    private final String mode;

    private final int alu;
    private final int bit;

    public InputValidator(String opcode, String a, String b, String carry, String mode, int alu) {
        this.opcode = opcode.trim();
        this.a = a.trim();
        this.b = b.trim();
        this.carry = carry.trim();
        this.mode = mode.trim();
        this.alu = alu;
        this.bit = bits(alu);
    }

    public Operation validate() throws ValidationException {

        if(opcode.isEmpty())
            throw new ValidationException("The opcode cannot be empty");

        if(a.isEmpty())
            throw new ValidationException("The input 'A' cannot be empty");

        if(b.isEmpty())
            throw new ValidationException("The input 'B' cannot be empty");

        if(a.length() != bit)
            a = __fill_zeros(a);

        if(b.length() != bit)
            b = __fill_zeros(b);

        Bit[] _opcode = __validate_opcode(opcode);
        Bit[] _a = __validate_input(a);
        Bit[] _b = __validate_input(b);

        Carry carry = __validate_carry(this.carry);
        Mode mode = __validate_mode(this.mode);

        return new Operation(_opcode, _a, _b, mode, carry, alu);

    }

    public static int bits(int alu){
        return alu * DEFAULT_BIT;
    }

    // VALIDATION METHOD ---------------------------------------------------------
    private Bit[] __validate_opcode(String s) throws ValidationException {

        long v = __is_binary_unsigned(s);

        if(s.length() != DEFAULT_BIT)
            throw new ValidationException("The opcode is not valid due to bit size mismatching => expected: " + bit + ", current: "+ s.length());

        if(__overflow(v, DEFAULT_BIT)){
            throw new ValidationException("The opcode is not valid due to overflow => " + s);
        }

        return Bit.parse(s);
    }
    private Bit[] __validate_input(String s) throws ValidationException {

        long v = __is_binary_signed(s);

        if(s.length() != bit)
            throw new ValidationException("The input is not valid due to bit size mismatching => expected: " + bit + ", current: "+ s.length());

        if(__overflow(v, bit)){
            throw new ValidationException("The input is not valid due to overflow => " + s);
        }

        return Bit.parse(s);
    }
    private Carry __validate_carry(String s) {
        return (s.contains("ON")) ? Carry.ON : Carry.OFF;
    }
    private Mode  __validate_mode(String s) {
        return (s.equals("LOGIC")) ? Mode.LOGIC : Mode.ARITHMETIC;
    }

    // UTILITY METHOD ---------------------------------------------------------
    private long __is_binary_unsigned(String s) throws ValidationException {

        long v;

        try {
            v = Long.parseUnsignedLong(s, RADIX);
        }catch (NumberFormatException e){
            throw new ValidationException("The following input is not valid => " + e.getMessage());
        }

        return v;
    }
    private long __is_binary_signed(String s) throws ValidationException {

        long v;

        try {
            v = Long.parseLong(s, RADIX);
        }catch (NumberFormatException e){
            throw new ValidationException("The following input is not valid => " + e.getMessage());
        }

        return v;
    }
    private boolean __overflow(long v, long b){
        return v >= Math.pow(RADIX, b);
    }
    private String __fill_zeros(String s){
        StringBuilder sb = new StringBuilder();

        for(int i = s.length(); i < bit; ++i)
            sb.append("0");

        sb.append(s);

        return sb.toString();
    }
}
