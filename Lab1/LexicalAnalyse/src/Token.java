import java.util.List;

/**
 * Created by Seven on 2016/10/22.
 */
public class Token {
    private String type;
    private String code;
    private String error;

    public Token(){
    }

    public Token(String type, String code){
        this.type=type;
        this.code=code;
        this.error=null;
    }

    public Token(String type,String code,String error){
        this.type="UNKNOWN";
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void printToken(List<Token> tokens){
        //将Token写入文件

    }
}
