
package com.ttl.alu.gui.controllers;

import com.ttl.alu.abc.utils.table.component.Column;
import com.ttl.alu.abc.utils.table.component.Data;
import com.ttl.alu.abc.utils.table.component.Row;
import com.ttl.alu.abc.utils.values.Bit;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.ttl.alu.abc.utils.table.utils.Type.IN;
import static com.ttl.alu.abc.utils.table.utils.Type.OUT;
import static com.ttl.alu.abc.utils.values.Bit.ONE;
import static com.ttl.alu.abc.utils.values.Bit.ZERO;

public final class TableViewController {

    @SafeVarargs
    private final List<Data<Bit>> in(Data<Bit> ...in){
        return Arrays.asList(in);
    }

    @SafeVarargs
    private final List<Data<String>> out(Data<String> ...out){
        return Arrays.asList(out);
    }

    public Column[] header() {
        return new Column[]{
                new Column("S3", IN),
                new Column("S2", IN),
                new Column("S1", IN),
                new Column("S0", IN),
                new Column("ARITHMETIC", OUT),
                new Column("LOGIC", OUT)
        };
    }

    public List<Row<Bit, String>> rows() {

        List<Row<Bit, String>> rows = new LinkedList<>();

        // __ <0> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(0, in(new Data<>("S0", ZERO, IN),
                new Data<>("S1", ZERO, IN),
                new Data<>("S2", ZERO, IN),
                new Data<>("S3", ZERO, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "NOT(A)", OUT), new Data<>("ARITHMETIC", "A + C(n)", OUT))));

        // __ <1> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(1, in(new Data<>("S0", ONE, IN),
                new Data<>("S1", ZERO, IN),
                new Data<>("S2", ZERO, IN),
                new Data<>("S3", ZERO, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "NOR(A, B)", OUT), new Data<>("ARITHMETIC", "OR(A, B) + C(n)", OUT))));

        // __ <2> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(2, in(new Data<>("S0", ZERO, IN),
                new Data<>("S1", ONE, IN),
                new Data<>("S2", ZERO, IN),
                new Data<>("S3", ZERO, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "AND(NOT(A), B)", OUT), new Data<>("ARITHMETIC", "OR(A, NOT(B)) + C(n)", OUT))));

        // __ <3> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(3, in(new Data<>("S0", ONE, IN),
                new Data<>("S1", ONE, IN),
                new Data<>("S2", ZERO, IN),
                new Data<>("S3", ZERO, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "0", OUT), new Data<>("ARITHMETIC", "(-1) + C(n)", OUT))));

        // __ <4> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(4, in(new Data<>("S0", ZERO, IN),
                new Data<>("S1", ZERO, IN),
                new Data<>("S2", ONE, IN),
                new Data<>("S3", ZERO, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "NAND(A, B)", OUT), new Data<>("ARITHMETIC", "A + AND(A, NOT(B)) + C(n)", OUT))));

        // __ <5> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(5, in(new Data<>("S0", ONE, IN),
                new Data<>("S1", ZERO, IN),
                new Data<>("S2", ONE, IN),
                new Data<>("S3", ZERO, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "NOT(B)", OUT), new Data<>("ARITHMETIC", "OR(A, B) + AND(A, NOT(B)) + C(n)", OUT))));

        // __ <6> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(6, in(new Data<>("S0", ZERO, IN),
                new Data<>("S1", ONE, IN),
                new Data<>("S2", ONE, IN),
                new Data<>("S3", ZERO, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "XOR(A, B)", OUT), new Data<>("ARITHMETIC", "A + (-B) + (-1) + C(n)", OUT))));

        // __ <7> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(7, in(new Data<>("S0", ONE, IN),
                new Data<>("S1", ONE, IN),
                new Data<>("S2", ONE, IN),
                new Data<>("S3", ZERO, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "AND(A, NOT(B))", OUT), new Data<>("ARITHMETIC", "AND(A, NOT(B)) + (-1) + C(n)", OUT))));

        // __ <8> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(8, in(new Data<>("S0", ZERO, IN),
                new Data<>("S1", ZERO, IN),
                new Data<>("S2", ZERO, IN),
                new Data<>("S3", ONE, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "NOT(A) OR B", OUT), new Data<>("ARITHMETIC", "A + AND(A, B) + C(n)", OUT))));

        // __ <9> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(9, in(new Data<>("S0", ONE, IN),
                new Data<>("S1", ZERO, IN),
                new Data<>("S2", ZERO, IN),
                new Data<>("S3", ONE, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "XNOR(A, B)", OUT), new Data<>("ARITHMETIC", "A + B + C(n)", OUT))));

        // __ <10> _______________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(10, in(new Data<>("S0", ZERO, IN),
                new Data<>("S1", ONE, IN),
                new Data<>("S2", ZERO, IN),
                new Data<>("S3", ONE, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "B", OUT), new Data<>("ARITHMETIC", "AND(A, NOT(B)) + (-1) + C(n)", OUT))));

        // __ <11> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(11, in(new Data<>("S0", ONE, IN),
                new Data<>("S1", ONE, IN),
                new Data<>("S2", ZERO, IN),
                new Data<>("S3", ONE, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "AND(A, B)", OUT), new Data<>("ARITHMETIC", "AND(A, B) + (-1) + C(n)", OUT))));

        // __ <12> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(12, in(new Data<>("S0", ZERO, IN),
                new Data<>("S1", ZERO, IN),
                new Data<>("S2", ONE, IN),
                new Data<>("S3", ONE, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "1", OUT), new Data<>("ARITHMETIC", "A + A + C(n)", OUT))));
        // __ <13> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(13, in(new Data<>("S0", ONE, IN),
                new Data<>("S1", ZERO, IN),
                new Data<>("S2", ONE, IN),
                new Data<>("S3", ONE, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "OR(A, NOT(B))", OUT), new Data<>("ARITHMETIC", "OR(A, B) + A + C(n)", OUT))));

        // __ <14> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(14, in(new Data<>("S0", ZERO, IN),
                new Data<>("S1", ONE, IN),
                new Data<>("S2", ONE, IN),
                new Data<>("S3", ONE, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "OR(A, B)", OUT), new Data<>("ARITHMETIC", "OR(A, NOT(B)) + A + C(n)", OUT))));

        // __ <15> ________________________________________________________________________________________________

        // INPUT
        rows.add(new Row<>(15, in(new Data<>("S0", ONE, IN),
                new Data<>("S1", ONE, IN),
                new Data<>("S2", ONE, IN),
                new Data<>("S3", ONE, IN)),
                // OUTPUT
                out(new Data<>("LOGIC", "A", OUT), new Data<>("ARITHMETIC", "A + (-1) + C(n)", OUT))));

        return rows;
    }

}
