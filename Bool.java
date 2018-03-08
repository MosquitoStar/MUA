
public class Bool extends Value {
	Boolean b;
	public Bool(){
		b=false;
	}
	public Bool(String s){
		if(s.equals("true")){
			b=true;
		}else{
			b=false;
		}
	}
	public Bool(Boolean bool){
		b=bool;
	}
	public static Boolean isBool(String s){
		if(s.equals("true")||s.equals("false")){
			return true;
		}else{
			return false;
		}
	}
	public Bool not(){
		if(b){
			return new Bool(false);	
		}else{
			return new Bool(true);
		}
	}
	public static Bool and(Bool b1,Bool b2){
		return new Bool(Boolean.logicalAnd(b1.b, b2.b));
	}
	public static Bool or(Bool b1,Bool b2){
		return new Bool(Boolean.logicalOr(b1.b, b2.b));
	}
	public void print(){
		System.out.println(b);
	}
	public Boolean isTrue(){
		return b==true;
	}
}
