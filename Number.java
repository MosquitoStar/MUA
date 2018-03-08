
public class Number extends Value {
	enum numType {Integer,Double}
	public Number(){
		value=new String("0");
	}
	public Number(String s){
		if(isNumber(s)){
			value=s;
		}else{
			value=new String("NumberFormatError");
		}
	}
	public Number(Integer i){
		value=i.toString();
	}
	public Number(Double d){
		value=d.toString();
		Character c=value.charAt(0);
		int cur=0;
		while(c!='.'){
			if(cur>=value.length()){
				return;
			}
			c=value.charAt(++cur);
			if(cur>=value.length()){
				return;
			}
		}
		Boolean isInt=true;
		for(int i=cur+1;i<value.length();i++){
			if(value.charAt(i)!='0'){
				isInt=false;
			}
		}
		if(isInt){
			value=value.substring(0, cur);
		}
	}
	public void print(){
		System.out.print(value);
	}
	public static Boolean isNumber(String s){
		try{
			Double.parseDouble(s);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	public numType typeCheck(){
		for(int i=0;i<this.value.length();i++){
			if(this.value.charAt(i)=='.'){
				return numType.Double;
			}
		}
		return numType.Integer;
	}
	public Integer toInt(){
		return Integer.parseInt(this.value);
	}
	public Double toDouble(){
		return Double.parseDouble(this.value);
	}
	public static Number add(Number n1,Number n2){
		if(n1.typeCheck()==numType.Double||n2.typeCheck()==numType.Double){
			Double result=n1.toDouble()+n2.toDouble();
			return new Number(result);
		}else{
			Integer result=n1.toInt()+n2.toInt();
			return new Number(result);
		}
	}
	public static Number sub(Number n1,Number n2){
		if(n1.typeCheck()==numType.Double||n2.typeCheck()==numType.Double){
			Double result=n1.toDouble()-n2.toDouble();
			return new Number(result);
		}else{
			Integer result=n1.toInt()-n2.toInt();
			return new Number(result);
		}
	}
	public static Number mul(Number n1,Number n2){
		if(n1.typeCheck()==numType.Double||n2.typeCheck()==numType.Double){
			Double result=n1.toDouble()*n2.toDouble();
			return new Number(result);
		}else{
			Integer result=n1.toInt()*n2.toInt();
			return new Number(result);
		}
	}
	public static Value div(Number n1,Number n2){
		if(n2.toDouble()==0.0){
			System.out.println("Math error: 0 can not be the divisor!");
			return new Value("Operation div failed!");
		}
		if(n1.typeCheck()==numType.Double||n2.typeCheck()==numType.Double){
			Double result=n1.toDouble()/n2.toDouble();
			return new Number(result);
		}else{
			Integer result=n1.toInt()/n2.toInt();
			return new Number(result);
		}
	}
	public static Value mod(Number n1,Number n2){
		if(n1.typeCheck()==numType.Integer&&n2.typeCheck()==numType.Integer){
			Integer result=n1.toInt()%n2.toInt();
			return new Number(result);
		}else{
			System.out.println("Math error: only integers can do the mod operation!");
			return new Value("Operation mod failed!");
		}
	}
	public static Boolean eq(Number n1,Number n2){
		return n1.toDouble().equals(n2.toDouble());
	}
	public static Boolean gt(Number n1,Number n2){
		return n1.toDouble().compareTo(n2.toDouble())>0;
	}
	public static Boolean lt(Number n1,Number n2){
		return n1.toDouble().compareTo(n2.toDouble())<0;
	}
	public Number abs(){
		if(this.typeCheck()==numType.Integer){
			Integer result=this.toInt();
			if(result>=0){
				return new Number(result);
			}else{
				return new Number(-result);
			}
		}else{
			Double result=this.toDouble();
			if(result>=0.0){
				return new Number(result);
			}else{
				return new Number(result);
			}
		}
	}
}
