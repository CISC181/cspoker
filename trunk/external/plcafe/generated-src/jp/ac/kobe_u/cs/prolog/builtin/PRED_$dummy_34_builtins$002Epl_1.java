package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.*;
/*
 This file is generated by Prolog Cafe.
 PLEASE DO NOT EDIT!
*/
/**
 <code>'$dummy_34_builtins.pl'/1</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$dummy_34_builtins$002Epl_1 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol("java.io.PushbackReader");
    static SymbolTerm s2 = SymbolTerm.makeSymbol("java.io.PrintWriter");
    static Predicate _$dummy_34_builtins$002Epl_1_sub_1 = new PRED_$dummy_34_builtins$002Epl_1_sub_1();
    static Predicate _$dummy_34_builtins$002Epl_1_1 = new PRED_$dummy_34_builtins$002Epl_1_1();
    static Predicate _$dummy_34_builtins$002Epl_1_2 = new PRED_$dummy_34_builtins$002Epl_1_2();

    public Term arg1;

    public PRED_$dummy_34_builtins$002Epl_1(Term a1, Predicate cont) {
        arg1 = a1;
        this.cont = cont;
    }

    public PRED_$dummy_34_builtins$002Epl_1(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        this.cont = cont;
    }

    public int arity() { return 1; }

    public String toString() {
        return "$dummy_34_builtins.pl(" + arg1 + ")";
    }

    public Predicate exec(Prolog engine) {
        engine.aregs[1] = arg1;
        engine.cont = cont;
        engine.setB0();
        return engine.jtry(_$dummy_34_builtins$002Epl_1_1, _$dummy_34_builtins$002Epl_1_sub_1);
    }
}

class PRED_$dummy_34_builtins$002Epl_1_sub_1 extends PRED_$dummy_34_builtins$002Epl_1 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$dummy_34_builtins$002Epl_1_2);
    }
}

class PRED_$dummy_34_builtins$002Epl_1_1 extends PRED_$dummy_34_builtins$002Epl_1 {
    public Predicate exec(Prolog engine) {
    // '$dummy_34_builtins.pl'(A):-java(A,'java.io.PushbackReader')
        Term a1;
        Predicate cont;
        a1 = engine.aregs[1];
        cont = engine.cont;
    // '$dummy_34_builtins.pl'(A):-[java(A,'java.io.PushbackReader')]
        //START inline expansion of java(a(1),s(1))
        a1 = a1.dereference();
        if (! a1.isJavaObject()) {
            return engine.fail();
        }
        if (! s1.unify(SymbolTerm.makeSymbol(((JavaObjectTerm) a1).object().getClass().getName()), engine.trail)) {
            return engine.fail();
        }
        //END inline expansion
        return cont;
    }
}

class PRED_$dummy_34_builtins$002Epl_1_2 extends PRED_$dummy_34_builtins$002Epl_1 {
    public Predicate exec(Prolog engine) {
    // '$dummy_34_builtins.pl'(A):-java(A,'java.io.PrintWriter')
        Term a1;
        Predicate cont;
        a1 = engine.aregs[1];
        cont = engine.cont;
    // '$dummy_34_builtins.pl'(A):-[java(A,'java.io.PrintWriter')]
        //START inline expansion of java(a(1),s(2))
        a1 = a1.dereference();
        if (! a1.isJavaObject()) {
            return engine.fail();
        }
        if (! s2.unify(SymbolTerm.makeSymbol(((JavaObjectTerm) a1).object().getClass().getName()), engine.trail)) {
            return engine.fail();
        }
        //END inline expansion
        return cont;
    }
}