package com.csci.grammar;

import com.csci.visitor.EvalVisitor;
import com.csci.visitor.PrintVisitor;

public class EDiv extends Exp {

    public Exp exp_1, exp_2;

    public EDiv(Exp p1, Exp p2) {
        exp_1 = p1;
        exp_2 = p2;
    }


    @Override
    public String accept(PrintVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public CustomObject eval(EvalVisitor visitor) throws Exception {
        return visitor.visit(this);
    }
}
