package com.ttl.alu.abc;

import com.ttl.alu.abc.chip.decoder.Result;
import com.ttl.alu.abc.chip.encoder.Operation;
import com.ttl.alu.abc.chip.utils.Carry;
import com.ttl.alu.abc.chip.utils.Pin;
import com.ttl.alu.abc.gates.*;
import com.ttl.alu.abc.utils.values.Bit;

import java.time.LocalTime;
import java.util.*;

public final class TTL74181 {

    public static final int WORD_SIZE = 4;

    // -- INPUT ----------
    private final Pin[] S;
    private final Pin[] A;
    private final Pin[] B;
    private Pin Cn;

    // -- INTERMEDIATE ----------
    private Pin M;
    private Pin[] P;
    private Pin[] G;
    private Pin[] C;

    // -- OUTPUT ---------
    private Pin[] Sx;
    private Pin out_P;
    private Pin out_Cn;
    private Pin out_G;
    private Pin[] out_F;
    private Pin out_E;

    // -- UTILS ----------
    private final List<String> logs;

    public TTL74181() {

        this.S = new Pin[WORD_SIZE];
        this.A = new Pin[WORD_SIZE];
        this.B = new Pin[WORD_SIZE];

        this.P = new Pin[WORD_SIZE];
        this.G = new Pin[WORD_SIZE];

        this.C = new Pin[WORD_SIZE];

        this.logs = new ArrayList<>();
    }

    public Result compute(Operation e) {

        ArrayDeque<Result> rs = new ArrayDeque<>();

        // Setting opcode
        __set__opcode(e.getOpcode());

        log("Starting computation on " + e.getAlu() + " ALU");

        // Saving some data to carry on each alu
        Carry c = null;

        // For each ALU, we calculate result and then append it to the list
        for (int i = 0; i < e.getAlu(); ++i) {

            log("==== ALU #" + i + " ====");

            __set_inputs(e, i);

            // We take the last carry output and set it as input
            if(c == null)
                __set__carry_input(e.getCarry().value());
            else
                __set__carry_input(c.value());

            __set__mode(e.getMode().value());

            // Getting result on each word
            Result r = execute();

            // Saving carry for the next operation
            c = r.getCarry();

            // Appending
            rs.addFirst(r);
        }

        return Result.of(rs);
    }

    // --- INPUTS --------------------------------

    private void __set__opcode(Bit... e) {
        S[0] = new Pin("S0", e[3]);
        S[1] = new Pin("S1", e[2]);
        S[2] = new Pin("S2", e[1]);
        S[3] = new Pin("S3", e[0]);
        log("The opcode has been read as follow => " + Arrays.toString(S));
    }

    private void __set__input_a(Bit... a) {
        A[0] = new Pin("A0", a[3]);
        A[1] = new Pin("A1", a[2]);
        A[2] = new Pin("A2", a[1]);
        A[3] = new Pin("A3", a[0]);
        log("The input 'a' has been read as follow => " + Arrays.toString(A));
    }

    private void __set__input_b(Bit... b) {
        B[0] = new Pin("B0", b[3]);
        B[1] = new Pin("B1", b[2]);
        B[2] = new Pin("B2", b[1]);
        B[3] = new Pin("B3", b[0]);
        log("The input 'b' has been read as follow => " + Arrays.toString(B));
    }

    private void __set__carry_input(Bit c) {
        Cn = new Pin("C(n)", c);
        log("The input 'carry' has been set as follow => " + Cn);
    }

    private void __set__mode(Bit m) {
        M = new Pin("M", m);
        log("The input 'mode' has been set as follow => " + M);
    }

    // --- OPERATIONS -----------------------------

    private Result execute() {

        log("Now, computing...");

        // Calculating intermediate
        this.P = __compute__PX();
        this.G = __compute__GX();
        this.C = __compute__C();
        this.Sx = __compute__SX();

        // Calculating output
        this.out_P = __compute_out_P();
        this.out_Cn = __compute_out_Cn();
        this.out_G = __compute_out_G();
        this.out_F = __compute_out_F();
        this.out_E = __compute_out_E();

        log("Processing is finished");

        return new Result(out_F, out_P, out_G, Carry.of(out_Cn.state()), out_E);
    }

    // == [ INTERMEDIATE ] ========================

    // --- C --------------------------------------

    private Pin[] __compute__C() {

        Pin[] p = new Pin[WORD_SIZE];

        p[0] = __compute__C0();
        p[1] = __compute__C1();
        p[2] = __compute__C2();
        p[3] = __compute__C3();

        log_separator();
        log("C has been been calculated as follow => " + Arrays.toString(p));
        log_separator();

        return p;
    }

    private Pin __compute__C0() {
        return new Pin("C0", NAND.alternative_denial(Cn.state(), NOT.negate(M.state())));
    }

    private Pin __compute__C1() {

        Bit X = AND.conjunction(NOT.negate(M.state()), P[0].state());
        Bit Y = AND.conjunction(NOT.negate(M.state()), G[0].state(), Cn.state());

        return new Pin("C1", NOR.joint_denial(X, Y));
    }

    private Pin __compute__C2() {

        Bit X = AND.conjunction(NOT.negate(M.state()), P[1].state());
        Bit Y = AND.conjunction(NOT.negate(M.state()), P[0].state(), G[1].state());
        Bit Z = AND.conjunction(NOT.negate(M.state()), Cn.state(), G[0].state(), G[1].state());

        return new Pin("C2", NOR.joint_denial(X, Y, Z));
    }

    private Pin __compute__C3() {

        Bit X = AND.conjunction(NOT.negate(M.state()), P[2].state());
        Bit Y = AND.conjunction(NOT.negate(M.state()), P[1].state(), G[2].state());
        Bit Z = AND.conjunction(NOT.negate(M.state()), P[0].state(), G[1].state(), G[2].state());
        Bit W = AND.conjunction(NOT.negate(M.state()), Cn.state(), G[0].state(), G[1].state(), G[2].state());

        return new Pin("C3", NOR.joint_denial(X, Y, Z, W));
    }

    // --- S --------------------------------------

    private Pin[] __compute__SX() {

        Pin[] p = new Pin[WORD_SIZE];

        p[0] = __compute__S0();
        p[1] = __compute__S1();
        p[2] = __compute__S2();
        p[3] = __compute__S3();

        log_separator();
        log("ΣX has been been calculated as follow => " + Arrays.toString(p));
        log_separator();

        return p;
    }

    private Pin __compute__S0() {
        return new Pin("Σ0", XOR.exclusive_disjunction(P[0].state(), G[0].state()));
    }

    private Pin __compute__S1() {
        return new Pin("Σ1", XOR.exclusive_disjunction(P[1].state(), G[1].state()));
    }

    private Pin __compute__S2() {
        return new Pin("Σ2", XOR.exclusive_disjunction(P[2].state(), G[2].state()));
    }

    private Pin __compute__S3() {
        return new Pin("Σ3", XOR.exclusive_disjunction(P[3].state(), G[3].state()));
    }

    // --- PG -------------------------------------

    private Pin[] __compute__PX() {

        Pin[] p = new Pin[WORD_SIZE];

        for (int i = 0; i < WORD_SIZE; ++i) {

            Bit aX = A[i].state();
            Bit bX = B[i].state();

            log_separator();
            log("Computing P => " + A[i].name() + "=" + A[i].state() + " || " + B[i].name() + "=" + B[i].state());
            log_separator();

            Bit X0 = AND.conjunction(aX);
            Bit X1 = AND.conjunction(bX, S[0].state());
            Bit X2 = AND.conjunction(S[1].state(), NOT.negate(bX));
            Bit P = NOR.joint_denial(X0, X1, X2);

            p[i] = new Pin("P" + i, P);

            log("AND_0 => (" + aX + ", " + aX + ") = " + X0);
            log("AND_1 => (" + bX + ", " + S[0].state() + ") = " + X1);
            log("AND_2 => (" + S[1].state() + ", " + NOT.negate(bX) + ") = " + X2);
            log("P" + i + " - (NOR) => (" + X0 + ", " + X1 + ", " + X2 + ") = " + P);

        }

        log_separator();
        log("P has been been calculated as follow => " + Arrays.toString(p));
        log_separator();

        return p;
    }

    private Pin[] __compute__GX() {

        Pin[] g = new Pin[WORD_SIZE];

        for (int i = 0; i < WORD_SIZE; ++i) {

            Bit aX = A[i].state();
            Bit bX = B[i].state();

            log_separator();
            log("Computing G => " + A[i].name() + "=" + A[i].state() + " || " + B[i].name() + "=" + B[i].state());
            log_separator();

            Bit X0 = AND.conjunction(NOT.negate(bX), S[2].state(), aX);
            Bit X1 = AND.conjunction(aX, bX, S[3].state());
            Bit G = NOR.joint_denial(X0, X1);

            g[i] = new Pin("G" + i, G);

            log("AND_0 => (" + NOT.negate(bX) + ", " + S[2].state() + ", " + aX + ") = " + X0);
            log("AND_1 => (" + aX + ", " + bX + ", " + S[3].state() + ") = " + X1);
            log("G (NOR) => (" + X0 + ", " + X1 + ") = " + G);

        }

        log_separator();
        log("G has been been calculated as follow => " + Arrays.toString(g));
        log_separator();

        return g;
    }

    // == [ OUTPUT ] ==============================

    private Pin __compute_out_P() {

        Bit X = AND.conjunction(P[0].state(), G[1].state(), G[2].state(), G[3].state());
        Bit Y = AND.conjunction(P[1].state(), G[2].state(), G[3].state());
        Bit Z = AND.conjunction(P[2].state(), G[3].state());
        Bit W = AND.conjunction(P[3].state());

        Pin p = new Pin("P", NOR.joint_denial(X, Y, Z, W));

        log_separator();
        log("P (OUT) has been been calculated as follow => " + p);
        log_separator();

        return p;
    }

    private Pin __compute_out_Cn() {

        Bit X = NAND.alternative_denial(Cn.state(), G[0].state(), G[1].state(), G[2].state(), G[3].state());
        Bit Y = out_P.state();

        Pin p = new Pin("C(n) + 4", NAND.alternative_denial(X, Y));

        log_separator();
        log("C(n) + 4 (OUT) has been been calculated as follow => " + p);
        log_separator();

        return p;
    }

    private Pin __compute_out_G() {

        Bit X = NAND.alternative_denial(G[0].state(), G[1].state(), G[2].state(), G[3].state());

        Pin p = new Pin("G", X);

        log_separator();
        log("G (OUT) has been been calculated as follow => " + p);
        log_separator();

        return p;

    }

    private Pin[] __compute_out_F() {

        Pin[] F = new Pin[WORD_SIZE];

        F[3] = new Pin("F0", XOR.exclusive_disjunction(C[0].state(), Sx[0].state()));
        F[2] = new Pin("F1", XOR.exclusive_disjunction(C[1].state(), Sx[1].state()));
        F[1] = new Pin("F2", XOR.exclusive_disjunction(C[2].state(), Sx[2].state()));
        F[0] = new Pin("F3", XOR.exclusive_disjunction(C[3].state(), Sx[3].state()));

        log_separator();
        log("F (OUT) has been been calculated as follow => " + Arrays.toString(F));
        log_separator();

        return F;
    }

    private Pin __compute_out_E() {

        Bit e = AND.conjunction(out_F[0].state(), out_F[1].state(), out_F[2].state(), out_F[3].state());

        Pin p = new Pin("A=B", e);

        log_separator();
        log("A=B (OUT) has been been calculated as follow -> " + p);
        log_separator();

        return p;
    }

    // --- LOG ------------------------------------

    private void log(String s) {
        this.logs.add("[ " + LocalTime.now() + " ] " + s);
    }
    private void log_separator() {
        this.logs.add("-----------------");
    }
    public List<String> logs() {
        // Creating reference
        ArrayList<String> logs = new ArrayList<>(this.logs);
        // Cleaning
        this.logs.clear();

        return logs;
    }

    // --- UTILS ----------------------------------

    private void __set_inputs(Operation e, int curr_alu) {

        // Nothing to worry about
        if(e.getAlu() == 1) {

            // Setting inputs
            __set__input_a(
                    e.getA()[0],
                    e.getA()[1],
                    e.getA()[2],
                    e.getA()[3]);

            __set__input_b(
                    e.getB()[0],
                    e.getB()[1],
                    e.getB()[2],
                    e.getB()[3]);
        }
        // We gotta start from the lowest bit
        else{

            List<Bit[]> bits_a = __extract_bits_group__from_lowest(e.getA());
            List<Bit[]> bits_b = __extract_bits_group__from_lowest(e.getB());

            // Setting inputs
            __set__input_a(
                    bits_a.get(curr_alu)[0],
                    bits_a.get(curr_alu)[1],
                    bits_a.get(curr_alu)[2],
                    bits_a.get(curr_alu)[3]);

            __set__input_b(
                    bits_b.get(curr_alu)[0],
                    bits_b.get(curr_alu)[1],
                    bits_b.get(curr_alu)[2],
                    bits_b.get(curr_alu)[3]);

        }

    }
    private List<Bit[]> __extract_bits_group__from_lowest(Bit[] s) {

        List<Bit[]> bits_s = new ArrayList<>();

        for(int i = 0; i < s.length; i += WORD_SIZE){

            Bit[] tmp = new Bit[WORD_SIZE];

            tmp[0] = s[i];
            tmp[1] = s[i + 1];
            tmp[2] = s[i + 2];
            tmp[3] = s[i + 3];

            bits_s.add(tmp);
        }

        // We have to reverse() in order to get the lowest bits
        Collections.reverse(bits_s);

        return bits_s;
    }

}
