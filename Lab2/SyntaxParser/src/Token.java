/**
 * Created by Seven on 2016/10/22.
 */
public class Token {
    public static final int ADD = 22;
    public static final int ADD_EQUAL = 23;
    public static final int MIN = 24;
    public static final int MIN_EQUAL = 25;
    public static final int MUL = 26;
    public static final int MUL_EQUAL = 27;
    public static final int DIV = 28;
    public static final int DIV_EQUAL = 29;
    public static final int EQUAL = 30;
    public static final int DOUBLE_EQUAL = 31;
    public static final int AND = 32;
    public static final int DOUBLE_AND = 33;
    public static final int OR = 34;
    public static final int DOUBLE_OR = 35;
    public static final int NOT = 36;
    public static final int NOT_EQUAL = 37;
    public static final int LESS = 38;
    public static final int LESS_EQUAL = 39;
    public static final int MORE = 40;
    public static final int MORE_EQUAL = 41;
    public static final int SLASH_STAR = 42;
    public static final int STAR_SLASH = 43;
    public static final int LEFT_PARENTHESE = 44;
    public static final int RIGHT_PARENTHESE = 45;
    public static final int LEFT_BRACKET = 46;
    public static final int RIGHT_BRACKET = 47;
    public static final int LEFT_BRACE = 48;
    public static final int RIGHT_BRACE = 49;
    public static final int COMMA = 50;
    public static final int COLON = 51;
    public static final int SEMICOLON = 52;
    public static final int DOT= 53;
    public static final int DOUBLE_QUOTE = 54;

    public static final int RESERVEDWORD = 55;
    public static final int ID = 56;
    public static final int NUMBER = 57;
    public static final int EMPTY=58;
    public static final int END = 0;
    public static final int ERROR = 1;

    public static final int S = 100;
    public static final int E = 101;
    public static final int E0 = 102;
    public static final int T = 103;
    public static final int T0 = 104;
    public static final int F = 105;
    public static final int C = 106;
    public static final int C0 = 107;
    public static final int D = 108;

    private int type;
    private String code;
    private String error;

    public Token(int type, String code){
        this.type=type;
        this.code=code;
        this.error=null;
    }

    public Token(int type,String code,String error){
        this.type=type;
        this.code=code;
        this.error=error;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }
}
