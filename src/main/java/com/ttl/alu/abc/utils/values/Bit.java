package com.ttl.alu.abc.utils.values;

public enum Bit {

    ZERO(false), ONE(true);

    private final boolean value;

    Bit(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }

    public static Bit[] parse(String s){

        return s.chars().filter(c -> c == '1' || c == '0').mapToObj(c -> {

            Bit b;

            if (c == '1') {
                b = Bit.ONE;
            } else {
                b = Bit.ZERO;
            }

            return b;

        }).toArray(Bit[]::new);
    }
    public static String valueOf(Bit ...t){

        StringBuilder sb = new StringBuilder();

        for(Bit tmp : t){
            sb.append(tmp.toString());
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return (value()) ? "1" : "0";
    }

}
