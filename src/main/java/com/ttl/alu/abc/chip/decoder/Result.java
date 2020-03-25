package com.ttl.alu.abc.chip.decoder;

import com.ttl.alu.abc.TTL74181;
import com.ttl.alu.abc.chip.utils.Carry;
import com.ttl.alu.abc.chip.utils.Pin;
import com.ttl.alu.abc.utils.values.Bit;

import java.util.Arrays;
import java.util.Deque;

public final class Result {

    private final Pin[] f;
    private final Pin p;
    private final Pin g;
    private final Carry carry;
    private final Pin equality;

    public Result(Pin[] f, Pin p, Pin g, Carry carry, Pin equality) {
        this.f = f;
        this.p = p;
        this.g = g;
        this.carry = carry;
        this.equality = equality;
    }

    public Pin[] getF() {
        return f;
    }

    public Pin getP() {
        return p;
    }

    public Pin getG() {
        return g;
    }

    public Carry getCarry() {
        return carry;
    }

    public Pin getEquality() {
        return equality;
    }

    public static Result of(Deque<Result> t){

        Pin[] f = new Pin[TTL74181.WORD_SIZE * t.size()];
        int idx = 0;

        // This is the last operation executed
        Result last = t.getFirst();

        // At the start, we assume there is always equality but then we may change it
        Pin equality = new Pin("A=B", Bit.ONE);

        // For each computation, we aggregate the data
        for (Result rs : t) {

            for (int w = 0; w < TTL74181.WORD_SIZE; ++w) {
                // Saving in the new array
                f[idx] = rs.f[w];
                // Incrementing index
                ++idx;
            }

            /*
             If before there was equality, but now there is no more equality
             we set equality as false, otherwise if there is truly equality
             the value is already set.
            */

            if(equality.state().value() && !rs.equality.state().value())
                equality = new Pin("A=B",Bit.ZERO);
        }

        return new Result(f,last.p,last.g,last.carry, equality);

    }

    @Override
    public String toString() {
        return "Result{" +
                "f=" + Arrays.toString(f) +
                ", p=" + p +
                ", g=" + g +
                ", carry=" + carry +
                ", equality=" + equality +
                '}';
    }
}
