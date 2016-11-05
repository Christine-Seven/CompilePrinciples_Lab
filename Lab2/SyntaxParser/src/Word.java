/**
 * Created by Seven on 2016/10/22.
 */
public class Word {
    private char[] value;
    private int index;

    public Word(){
        value=new char[50];
        index=0;
    }

    public void add(char c){
        value[index++]=c;
        value[index]='\0';
//        System.out.println("c="+c+"pos="+index);
    }

    public char[] getValue() {
        return value;
    }

    public void setValue(char[] value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
