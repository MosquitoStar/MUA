
public class Word extends Value {
	static String[] keyName={"make","thing","erase","isname","print","read",
			"readlinst","add","sub","mul","div","mod","eq","gt","lt","and",
			"or","not","repeat","output","stop","first","last","butfirst",
			"butlast","if","join","sentence","random","sqrt","isnumber",
			"islist","isbool","isempty","int","word","list","wait","save",
			"load","erall","poall","pi","run","abs",};
	public void print(){
		System.out.print(value);
	}
	public Word(String n){
		super(n);
	}
	public static Boolean isOperator(String s){
		for(int i=0;i<keyName.length;i++){
			if(s.equals(keyName[i])){
				return true;
			}
		}
		return false;
	}
	public static Boolean isWord(String s){
		if(s.startsWith("\"")){
			String n=s.substring(1);//È¥µô¡°
			if(isOperator(n)){
				return false;
			}else{
				return true;	
			}
		}else{
			return false;
		}
	}
	public static Boolean eq(Word w1,Word w2){
		return w1.value.equals(w2.value);
	}
	public static Boolean gt(Word w1,Word w2){
		return w1.value.compareTo(w2.value)>0;
	}
	public static Boolean lt(Word w1,Word w2){
		return w1.value.compareTo(w2.value)<0;
	}
	public static Word joinWord(Word w1,Value w2){
		return new Word(w1.value+w2.value);
	}
	public Boolean isempty(){
		return this.value.isEmpty();
	}
	public Value firstChar(){
		if(this.value.isEmpty()){
			return new Value("The word is empty!");
		}
		return new Word(this.value.substring(0, 1));
	}
	public Value lastChar(){
		if(this.value.isEmpty()){
			return new Value("The Word is empty");
		}
		return new Word(this.value.substring(this.value.length()-1));
	}
	public Word wordButFirst(){
		if(this.value.isEmpty()){
			return new Word("");
		}
		return new Word(this.value.substring(1));
	}
	public Word wordButLast(){
		if(this.value.isEmpty()){
			return new Word("");
		}
		return new Word(this.value.substring(0, this.value.length()-1));
	}
}
