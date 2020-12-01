package miniplc0java.tokenizer;

import miniplc0java.error.TokenizeError;
import miniplc0java.error.ErrorCode;

public class Tokenizer {

    private StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    // 这里本来是想实现 Iterator<Token> 的，但是 Iterator 不允许抛异常，于是就这样了
    /**
     * 获取下一个 Token
     * 
     * @return
     * @throws TokenizeError 如果解析有异常则抛出
     */
    public Token nextToken() throws TokenizeError {
        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return lexUInt();
        } else if (Character.isAlphabetic(peek)) {
            return lexIdentOrKeyword();
        } else {
            return lexOperatorOrUnknown();
        }
    }

    private Token lexUInt() throws TokenizeError {
        // 请填空：
        // 直到查看下一个字符不是数字为止:
        StringBuffer stringBuffer = new StringBuffer();
        while (Character.isDigit(it.peekChar())) {
            char ch = it.nextChar();
            stringBuffer.append(ch);
        }
        // -- 前进一个字符，并存储这个字符
        //
        char ch = it.nextChar();
        // 解析存储的字符串为无符号整数
        // 解析成功则返回无符号整数类型的token，否则返回编译错误
        //
        String word = stringBuffer.toString();
        int value = Integer.parseUnsignedInt(word);
        return new Token(TokenType.Uint, value, it.previousPos(), it.currentPos());
        // Token 的 Value 应填写数字的值
    }

    private Token lexIdentOrKeyword() throws TokenizeError {
        // 请填空：
        // 直到查看下一个字符不是数字或字母为止:
        StringBuffer stringBuffer = new StringBuffer();
        while (Character.isDigit(it.peekChar()) || Character.isAlphabetic(it.peekChar())) {
            char ch = it.nextChar();
            stringBuffer.append(ch);
        }
        // -- 前进一个字符，并存储这个字符
        //
        char ch = it.nextChar();
        // 尝试将存储的字符串解释为关键字
        // -- 如果是关键字，则返回关键字类型的 token
        // -- 否则，返回标识符
        //
        String word = stringBuffer.toString();
        switch (word) {
            case "begin":
                return new Token(TokenType.Begin, "begin", it.previousPos(), it.currentPos());

            case "end":
                return new Token(TokenType.End, "end", it.previousPos(), it.currentPos());

            case "const":
                return new Token(TokenType.Const, "const", it.previousPos(), it.currentPos());

            case "var":
                return new Token(TokenType.Var, "var", it.previousPos(), it.currentPos());

            case "print":
                return new Token(TokenType.Print, "print", it.previousPos(), it.currentPos());

            default:
                return new Token(TokenType.Ident, word, it.previousPos(), it.currentPos());
        }
        // Token 的 Value 应填写标识符或关键字的字符串
    }

    private Token lexOperatorOrUnknown() throws TokenizeError {
        switch (it.nextChar()) {
            case '+':
                return new Token(TokenType.Plus, '+', it.previousPos(), it.currentPos());

            case '-':
                return new Token(TokenType.Minus, '-', it.previousPos(), it.currentPos());

            case '*':
                return new Token(TokenType.Mult, '*', it.previousPos(), it.currentPos());

            case '/':
                return new Token(TokenType.Div, '/', it.previousPos(), it.currentPos());

            case '=':
                return new Token(TokenType.Equal, '=', it.previousPos(), it.currentPos());

            case ';':
                return new Token(TokenType.Semicolon, ';', it.previousPos(), it.currentPos());

            case '(':
                return new Token(TokenType.LParen, '(', it.previousPos(), it.currentPos());

            case ')':
                return new Token(TokenType.RParen, ')', it.previousPos(), it.currentPos());

            default:
                // 不认识这个输入，摸了
                throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
