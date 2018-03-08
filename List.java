import java.util.ArrayList;
public class List extends Value {
	public ArrayList<Value> list;
	public List(){
		super();
		list=new ArrayList<Value>();
	}
	public void add(Value v){
		list.add(v);
		reconstructValue();
	}
	public void replace(Integer i,Value v){
		list.set(i, v);
		reconstructValue();
	}
	private void reconstructValue(){
		this.value="";
		for(int i=0;i<list.size();i++){
			if(list.get(i).getClass()!=new List().getClass()){
				this.value+=list.get(i).value;
			}else{
				this.value+="["+list.get(i).value+"]";
			}
			if(i!=list.size()-1)
				this.value+=" ";
		}
	}
	public static Boolean isList(String s){
		int flag=0,i;
		for(i=0;i<s.length();i++){
			if(s.charAt(i)=='['){
				flag++;
			}else if(s.charAt(i)==']'){
				flag--;
			}
			if(flag==0){
				break;
			}
		}
		if(i<s.length()-1||flag>0){
			return false;
		}
		return true;
	}
	public Boolean readList(String s){
		if(!isList(s)){
			return false;
		}
		String value=new String();
		List nl=new List();
		Boolean inSubList=false;
		int flag=0;
		for(int i=1;i<s.length();i++){
			if(inSubList){
				if(s.charAt(i)=='['){
					flag++;
				}else if(s.charAt(i)==']'){
					flag--;
				}
				value+=s.charAt(i);
				if(flag<0){
					nl.readList(value);
					this.add(nl);
					value=new String();
					inSubList=false;
				}
			}else{
				if(s.charAt(i)=='['){
					nl=new List();
					inSubList=true;
					flag=0;
					value=new String("[");
				}else if(s.charAt(i)==']'||s.charAt(i)==' '){
					if(!value.isEmpty()){
						if(Number.isNumber(value)){
							this.add(new Number(value));
						}else if(Bool.isBool(value)){
							this.add(new Bool(value));
						}else{
							this.add(new Word(value));
						}
						value=new String();
					}
				}else{
					value+=s.charAt(i);
				}
			}
		}
		reconstructValue();
		return true;
	}
	public void print(){
		System.out.print("[");
		for(int i=0;i<list.size();i++){
			list.get(i).print();
			if(i!=list.size()-1){
				System.out.print(" ");
			}
		}
		System.out.print("]");
	}
	public Integer elementCount(){
		return list.size();
	}
	public Value getElement(Integer i){
		if(i>=0&&i<list.size()){
			return list.get(i);
		}
		return new Value("Index out of range!");
	}
	public Value exec(RunMode m,NameMap foreign){
		AdvInterpreter a=new AdvInterpreter(m,foreign);
		return a.interpret(this.value);
	}
	public Value exec(RunMode m,NameMap foreign,NameMap local){
		AdvInterpreter a=new AdvInterpreter(m,foreign,local);
		return a.interpret(this.value);
	}
	public Boolean isempty(){
		return this.list.isEmpty();
	}
	public Value firstElement(){
		if(list.isEmpty()){
			return new Value("The list is empty!");
		}
		return list.get(0);
	}
	public Value lastElement(){
		if(list.isEmpty()){
			return new Value("The list is empty!");
		}
		return list.get(list.size()-1);
	}
	public List listButFirst(){
		if(list.isEmpty()){
			return new List();
		}
		List nl=this;
		nl.list.remove(0);
		nl.reconstructValue();
		return nl;
	}
	public List listButLast(){
		if(list.isEmpty()){
			return new List();
		}
		List nl=this;
		nl.list.remove(list.size()-1);
		nl.reconstructValue();
		return nl;
	}
	public void join(Value v){
		list.add(v);
		reconstructValue();
	}
	public static List sentence(List l1,List l2){
		List nl=new List();
		for(int i=0;i<l1.list.size();i++){
			nl.list.add(l1.list.get(i));
		}
		for(int i=0;i<l2.list.size();i++){
			nl.list.add(l2.list.get(i));
		}
		nl.reconstructValue();
		return nl;
	}
	public static List formList(Value v1,Value v2){
		List nl=new List();
		nl.list.add(v1);
		nl.list.add(v2);
		nl.reconstructValue();
		return nl;
	}
}
