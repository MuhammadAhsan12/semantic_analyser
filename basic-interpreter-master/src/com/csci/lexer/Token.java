package com.csci.lexer;

public class Token {

    private TokenType type;
    private String data;
    private Integer position;

    public Token(TokenType type, String data, Integer position) {
        this.type = type;
        this.data = data;
        this.position = position;
    }

    public boolean is(TokenType tokenType) {
        return this.getType() == tokenType;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public Integer getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return String.format("(%s %s at position %d)", this.getType().name(), this.getData(), this.getPosition());
    }

}
