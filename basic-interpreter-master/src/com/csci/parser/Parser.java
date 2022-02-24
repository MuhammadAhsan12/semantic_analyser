package com.csci.parser;

import com.csci.grammar.*;
import com.csci.lexer.Token;
import com.csci.lexer.TokenType;

import java.util.LinkedList;

public class Parser implements ParserInterface {

    /**
     * Token list
     */
    private LinkedList<Token> tokenList;

    /**
     * lookahead token
     */
    private Token lookahead;

    /**
     * Parser constructor
     *
     * @param tokenList token list
     */
    public Parser(LinkedList<Token> tokenList) {
        this.tokenList = tokenList;
        lookahead = this.tokenList.getFirst();
    }

    /**
     * Pop and get first token
     */
    private void nextToken() {
        tokenList.pop();
        lookahead = tokenList.getFirst();
    }

    /**
     * @param index token index
     * @return token at index
     */
    private Token lookahead(Integer index) {
        if (tokenList.size() > index) {
            return tokenList.get(index);
        }
        return new Token(TokenType.WHITESPACE, "", 0);
    }

    /**
     * Check expected token
     *
     * @param expected token to eat
     * @throws Exception syntax exception
     */
    private void expect(TokenType expected) throws Exception {
        tokenList.pop();
        if (tokenList.isEmpty()) {
            throw new Exception(String.format("Parse error: %s expected", expected.name()));
        } else {
            lookahead = tokenList.getFirst();
            if (lookahead.getType() != expected) {
                throw new Exception(
                        String.format(
                                "Parse error: Unexpected token \"%s\" at position %d. %s expected.",
                                lookahead.getData(),
                                lookahead.getPosition(),
                                expected.name()
                        )
                );
            }
        }
    }

    /**
     * Parse Program
     *
     * @return Program
     * @throws Exception syntax exception
     */
    @Override
    public Program parseProgram() throws Exception {
        ListDef listDef = parseListDef();
        return new PDefs(listDef);
    }

    /**
     * Parse definition list
     *
     * @return ListDef
     * @throws Exception syntax exception
     */
    @Override
    public ListDef parseListDef() throws Exception {

        ListDef listDef = new ListDef();

        Def def = parseDef();

        if (def != null) {
            listDef.add(def);
            listDef.addAll(parseListDef());
        }

        return listDef;
    }

    /**
     * Parse single definition
     *
     * @return Def
     * @throws Exception syntax exception
     */
    public Def parseDef() throws Exception {

        if (lookahead.is(TokenType.TYPEINT)) {

            Type typeInt = new TypeInt();
            expect(TokenType.IDENT);
            String functionName = lookahead.getData();
            expect(TokenType.BRASTART);
            ListArg listArg = parseListArg();
            expect(TokenType.BRAEND);
            expect(TokenType.SCOPESTART);
            ListStm listStm = parseListStm();
            expect(TokenType.SCOPEEND);

            return new DFun(typeInt, functionName, listArg, listStm);

        } else if (lookahead.is(TokenType.TYPEBOOL)) {

            Type typeBool = new TypeBool();
            expect(TokenType.IDENT);
            String functionName = lookahead.getData();
            expect(TokenType.BRASTART);
            ListArg listArg = parseListArg();
            expect(TokenType.BRAEND);
            expect(TokenType.SCOPESTART);
            ListStm listStm = parseListStm();
            expect(TokenType.SCOPEEND);

            return new DFun(typeBool, functionName, listArg, listStm);

        } else if (lookahead.is(TokenType.TYPESTRING)) {

            Type typeString = new TypeString();
            expect(TokenType.IDENT);
            String functionName = lookahead.getData();
            expect(TokenType.BRASTART);
            ListArg listArg = parseListArg();
            expect(TokenType.BRAEND);
            expect(TokenType.SCOPESTART);
            ListStm listStm = parseListStm();
            expect(TokenType.SCOPEEND);

            return new DFun(typeString, functionName, listArg, listStm);

        } else if (lookahead.is(TokenType.TYPEVOID)) {

            Type typeVoid = new TypeVoid();
            expect(TokenType.IDENT);
            String functionName = lookahead.getData();
            expect(TokenType.BRASTART);
            ListArg listArg = parseListArg();
            expect(TokenType.BRAEND);
            expect(TokenType.SCOPESTART);
            ListStm listStm = parseListStm();
            expect(TokenType.SCOPEEND);

            return new DFun(typeVoid, functionName, listArg, listStm);

        }

        return null;
    }

    /**
     * Parse argument list
     *
     * @return ListArg
     * @throws Exception syntax exception
     */
    @Override
    public ListArg parseListArg() throws Exception {
        return null;
    }


    /**
     * Parse single argument
     *
     * @return Arg
     * @throws Exception syntax exception
     */
    public Arg parseArg() throws Exception {
        return null;
    }

    /**
     * Parse expression
     *
     * @return Exp
     * @throws Exception syntax exception
     */
    @Override
    public Exp parseExp() throws Exception {

        Token first = lookahead(1);
        Token second = lookahead(2);

        if (first.is(TokenType.IDENT) && second.is(TokenType.GREATER)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp2 = parseExp();

            return new EGt(eId, exp2);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.GREATEROREQUALS)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp2 = parseExp();

            return new EGtEq(eId, exp2);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.LESS)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp2 = parseExp();

            return new ELt(eId, exp2);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.LESSOREQUALS)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp2 = parseExp();

            return new ELtEq(eId, exp2);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.EQUALS)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp2 = parseExp();

            return new EEq(eId, exp2);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.NOTEQUAL)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp2 = parseExp();

            return new ENEq(eId, exp2);

        } else if (first.is(TokenType.INT) && second.is(TokenType.GREATER)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new EGt(eInt, exp2);

        } else if (first.is(TokenType.INT) && second.is(TokenType.GREATEROREQUALS)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new EGtEq(eInt, exp2);

        } else if (first.is(TokenType.INT) && second.is(TokenType.LESS)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new ELt(eInt, exp2);

        } else if (first.is(TokenType.INT) && second.is(TokenType.LESSOREQUALS)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new ELtEq(eInt, exp2);

        } else if (first.is(TokenType.INT) && second.is(TokenType.EQUALS)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new EEq(eInt, exp2);

        } else if (first.is(TokenType.INT) && second.is(TokenType.NOTEQUAL)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new ENEq(eInt, exp2);

        }else if (first.is(TokenType.FLOAT) && second.is(TokenType.GREATER)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new EGt(eDouble, exp2);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.GREATEROREQUALS)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new EGtEq(eDouble, exp2);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.LESS)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new ELt(eDouble, exp2);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.LESSOREQUALS)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new ELtEq(eDouble, exp2);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.EQUALS)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new EEq(eDouble, exp2);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.NOTEQUAL)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp2 = parseExp();

            return new ENEq(eDouble, exp2);

        }else if (first.is(TokenType.STRING) && second.is(TokenType.EQUALS)) {

            nextToken();
            EString eString = new EString(lookahead.getData());
            nextToken();
            Exp exp2 = parseExp();

            return new EEq(eString, exp2);

        } else if (first.is(TokenType.STRING) && second.is(TokenType.NOTEQUAL)) {

            nextToken();
            EString eString = new EString(lookahead.getData());
            nextToken();
            Exp exp2 = parseExp();

            return new ENEq(eString, exp2);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.PLUS)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp = parseExp();
            return new EPlus(eId, exp);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.MINUS)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp = parseExp();
            return new EMinus(eId, exp);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.DIV)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp = parseExp();
            return new EDiv(eId, exp);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.PROD)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            Exp exp = parseExp();
            return new ETimes(eId, exp);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.INCREMENT)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            return new EIncr(eId);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.DECREMENT)) {

            nextToken();
            EId eId = new EId(lookahead.getData());
            nextToken();
            return new EDecr(eId);

        } else if (first.is(TokenType.INT) && second.is(TokenType.PLUS)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp = parseExp();
            return new EPlus(eInt, exp);

        } else if (first.is(TokenType.INT) && second.is(TokenType.MINUS)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp = parseExp();
            return new EMinus(eInt, exp);

        } else if (first.is(TokenType.INT) && second.is(TokenType.DIV)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp = parseExp();
            return new EDiv(eInt, exp);

        } else if (first.is(TokenType.INT) && second.is(TokenType.PROD)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            Exp exp = parseExp();
            return new ETimes(eInt, exp);

        } else if (first.is(TokenType.INT) && second.is(TokenType.INCREMENT)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            return new EIncr(eInt);

        } else if (first.is(TokenType.INT) && second.is(TokenType.DECREMENT)) {

            nextToken();
            EInt eInt = new EInt(Integer.parseInt(lookahead.getData()));
            nextToken();
            return new EDecr(eInt);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.PLUS)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp = parseExp();
            return new EPlus(eDouble, exp);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.MINUS)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp = parseExp();
            return new EMinus(eDouble, exp);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.DIV)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp = parseExp();
            return new EDiv(eDouble, exp);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.PROD)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            Exp exp = parseExp();
            return new ETimes(eDouble, exp);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.INCREMENT)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            return new EIncr(eDouble);

        } else if (first.is(TokenType.FLOAT) && second.is(TokenType.DECREMENT)) {

            nextToken();
            EDouble eDouble = new EDouble(Double.parseDouble(lookahead.getData()));
            nextToken();
            return new EDecr(eDouble);

        } else if (first.is(TokenType.STRING) && second.is(TokenType.PLUS)) {

            nextToken();
            EString eString = new EString(lookahead.getData());
            nextToken();
            Exp exp = parseExp();
            return new EPlus(eString, exp);

        } else if (first.is(TokenType.TRUE) && second.is(TokenType.AND)) {

            nextToken();
            ETrue eTrue = new ETrue();
            nextToken();
            Exp exp = parseExp();
            return new EAnd(eTrue, exp);

        } else if (first.is(TokenType.TRUE) && second.is(TokenType.OR)) {

            nextToken();
            ETrue eTrue = new ETrue();
            nextToken();
            Exp exp = parseExp();
            return new EOr(eTrue, exp);

        }else if (first.is(TokenType.FALSE) && second.is(TokenType.AND)) {

            nextToken();
            EFalse eFalse = new EFalse();
            nextToken();
            Exp exp = parseExp();
            return new EAnd(eFalse, exp);

        } else if (first.is(TokenType.FALSE) && second.is(TokenType.OR)) {

            nextToken();
            EFalse eFalse = new EFalse();
            nextToken();
            Exp exp = parseExp();
            return new EOr(eFalse, exp);

        } else if (first.is(TokenType.IDENT)) {

            nextToken();
            return new EId(lookahead.getData());

        } else if (first.is(TokenType.TRUE)) {

            nextToken();
            return new ETrue();

        } else if (first.is(TokenType.FALSE)) {

            nextToken();
            return new EFalse();

        } else if (first.is(TokenType.STRING)) {

            nextToken();
            return new EString(lookahead.getData());

        } else if (first.is(TokenType.FLOAT)) {

            nextToken();
            return new EDouble(Double.parseDouble(lookahead.getData()));

        } else if (first.is(TokenType.INT)) {

            nextToken();
            return new EInt(Integer.parseInt(lookahead.getData()));

        } else {

            throw new Exception("Parse error: Expression expected!");

        }
    }

    /**
     * Parse statement list
     *
     * @return ListStm
     * @throws Exception syntax exception
     */
    @Override
    public ListStm parseListStm() throws Exception {

        ListStm listStm = new ListStm();

        Stm stm = parseStm();

        if (stm != null) {
            listStm.add(stm);
            listStm.addAll(parseListStm());
        }

        return listStm;
    }

    /**
     * Parse single statement
     *
     * @return ListStm
     * @throws Exception syntax exception
     */
    public Stm parseStm() throws Exception {

        Token first = lookahead(1);
        Token second = lookahead(2);
        Token third = lookahead(3);

        if (first.is(TokenType.RETURN)) {

            nextToken();
            Exp exp = parseExp();
            expect(TokenType.SEMICOLON);

            return new SReturn(exp);

        } else if (first.is(TokenType.IF)) {

            nextToken();
            expect(TokenType.BRASTART);
            Exp condition = parseExp();
            expect(TokenType.BRAEND);
            expect(TokenType.SCOPESTART);
            ListStm stmIf = parseListStm();
            expect(TokenType.SCOPEEND);
            expect(TokenType.ELSE);
            expect(TokenType.SCOPESTART);
            ListStm stmElse = parseListStm();
            expect(TokenType.SCOPEEND);

            return new SIfElse(condition, stmIf, stmElse);

        } else if (first.is(TokenType.WHILE)) {

            nextToken();
            expect(TokenType.BRASTART);
            Exp condition = parseExp();
            expect(TokenType.BRAEND);
            expect(TokenType.SCOPESTART);
            ListStm stmts = parseListStm();
            expect(TokenType.SCOPEEND);

            return new SWhile(condition, stmts);


        } else if (first.is(TokenType.TYPEINT) && second.is(TokenType.IDENT) && third.is(TokenType.ASSIGNMENT)) {

            nextToken();
            TypeInt typeInt = new TypeInt();
            nextToken();
            String varName = lookahead.getData();
            nextToken();
            Exp exp = parseExp();
            expect(TokenType.SEMICOLON);

            return new SInit(typeInt, varName, exp);


        } else if (first.is(TokenType.TYPEBOOL) && second.is(TokenType.IDENT) && third.is(TokenType.ASSIGNMENT)) {

            nextToken();
            TypeBool typeBool = new TypeBool();
            nextToken();
            String varName = lookahead.getData();
            nextToken();
            Exp exp = parseExp();
            expect(TokenType.SEMICOLON);

            return new SInit(typeBool, varName, exp);


        } else if (first.is(TokenType.TYPEFLOAT) && second.is(TokenType.IDENT) && third.is(TokenType.ASSIGNMENT)) {

            nextToken();
            TypeDouble typeDouble = new TypeDouble();
            nextToken();
            String varName = lookahead.getData();
            nextToken();
            Exp exp = parseExp();
            expect(TokenType.SEMICOLON);

            return new SInit(typeDouble, varName, exp);


        } else if (first.is(TokenType.TYPESTRING) && second.is(TokenType.IDENT) && third.is(TokenType.ASSIGNMENT)) {

            nextToken();
            TypeString typeString = new TypeString();
            nextToken();
            String varName = lookahead.getData();
            nextToken();
            Exp exp = parseExp();
            expect(TokenType.SEMICOLON);

            return new SInit(typeString, varName, exp);

        } else if (first.is(TokenType.TYPEINT) && second.is(TokenType.IDENT) && third.is(TokenType.SEMICOLON)) {

            nextToken();
            TypeInt typeInt = new TypeInt();
            nextToken();
            String varName = lookahead.getData();
            nextToken();

            return new SDecls(typeInt, varName);


        } else if (first.is(TokenType.TYPEBOOL) && second.is(TokenType.IDENT) && third.is(TokenType.SEMICOLON)) {

            nextToken();
            TypeBool typeBool = new TypeBool();
            nextToken();
            String varName = lookahead.getData();
            nextToken();

            return new SDecls(typeBool, varName);


        } else if (first.is(TokenType.TYPEFLOAT) && second.is(TokenType.IDENT) && third.is(TokenType.SEMICOLON)) {

            nextToken();
            TypeDouble typeDouble = new TypeDouble();
            nextToken();
            String varName = lookahead.getData();
            nextToken();

            return new SDecls(typeDouble, varName);


        } else if (first.is(TokenType.TYPESTRING) && second.is(TokenType.IDENT) && third.is(TokenType.SEMICOLON)) {

            nextToken();
            TypeString typeString = new TypeString();
            nextToken();
            String varName = lookahead.getData();
            nextToken();

            return new SDecls(typeString, varName);

        } else if (first.is(TokenType.IDENT) && second.is(TokenType.ASSIGNMENT)) {

            nextToken();
            String varName = lookahead.getData();
            nextToken();
            Exp exp = parseExp();
            expect(TokenType.SEMICOLON);

            return new SAss(varName, exp);

        } else if (first.is(TokenType.IDENT)) {

            Exp exp = parseExp();
            expect(TokenType.SEMICOLON);

            return new SExp(exp);

        }

        return null;
    }
}
