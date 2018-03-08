import java.util.Scanner;
public class Interpreter {
	private static NameMap global=new NameMap();
	
	public static void interpret(){
		Scanner scan=new Scanner(System.in);
		while(scan.hasNextLine()){
			String row=scan.nextLine();
			Integer[] cursor={0};
			String[] val=row.split(" ");
			getOp(val,cursor);
		}
		scan.close();
	}
	public static Value getOp(String[] val,Integer[] cursor){
		if(val.length==0){
			return new Value("Empty expression!");
		}
		switch (val[cursor[0]]){
		case "make":
			cursor[0]++;
			make(val,cursor);
			return new Value("Operation make do not have a return value!");
		case "thing":
			cursor[0]++;
			return thing(val,cursor);
		case "erase":
			cursor[0]++;
			erase(val,cursor);
			return new Value("Operation erase do not have a return value!");
		case "isname":
			cursor[0]++;
			return isname(val,cursor);
		case "print":
			cursor[0]++;
			print(val,cursor);
			return new Value("Operation print do not have a return value!");
		case "read":
			cursor[0]++;
			return read(val,cursor);
		case "readlinst":
			cursor[0]++;
			return readlinst(val,cursor);
		case "add":
			cursor[0]++;
			return add(val,cursor);
		case "sub":
			cursor[0]++;
			return sub(val,cursor);
		case "mul":
			cursor[0]++;
			return mul(val,cursor);
		case "div":
			cursor[0]++;
			return div(val,cursor);
		case "mod":
			cursor[0]++;
			return mod(val,cursor);
		case "eq":
			cursor[0]++;
			return eq(val,cursor);
		case "gt":
			cursor[0]++;
			return gt(val,cursor);
		case "lt":
			cursor[0]++;
			return lt(val,cursor);
		case "and":
			cursor[0]++;
			return and(val,cursor);
		case "or":
			cursor[0]++;
			return or(val,cursor);
		case "not":
			cursor[0]++;
			return not(val,cursor);
		case "repeat":
			cursor[0]++;
			repeat(val,cursor);
			return new Value("Operation repeat do not have a return value!");
		default:
			if(val[cursor[0]].startsWith("//")){
				cursor[0]=val.length;
			}else if(val[cursor[0]].startsWith(":")){
				val[cursor[0]]="\""+val[cursor[0]].substring(1);
				return colon(val,cursor);
			}else if(val[cursor[0]].startsWith("[")){
				String s=new String();
				for(int i=cursor[0];i<val.length;i++){
					s+=val[i];
					if(i!=val.length-1){
						s+=" ";
					}
				}
				List l=new List();
				if(l.readList(s)==false){
					System.out.println("Syntax error: "+val[cursor[0]]+" is not an operator or a value!");
					cursor[0]=val.length;
					break;
				}
				System.out.println("The interpreter don't know how to do with this List--"+val[cursor[0]]);
				cursor[0]=val.length;
				return l;
			}else if(Number.isNumber(val[cursor[0]])){
				System.out.println("The interpreter don't know how to do with this Number--"+val[cursor[0]]);
				Number n=new Number(val[cursor[0]]);
				cursor[0]++;
				return n;
			}else if(Bool.isBool(val[cursor[0]])){
				System.out.println("The interpreter don't know how to do with this Bool--"+val[cursor[0]]);
				Bool b=new Bool(val[cursor[0]]);
				cursor[0]++;
				return b;
			}else if(val[0].startsWith("\"")){
				System.out.println("The interpreter don't know how to do with this Word--"+val[cursor[0]]);
				Word w=new Word(val[cursor[0]]);
				cursor[0]++;
				return w;
			}else{
				System.out.println("Syntax error: "+val[cursor[0]]+" is not an operator or a value!");
				cursor[0]=val.length;
			}
		}
		return new Value();
	}
	private static void make(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Word("").getClass()){
			System.out.println("Error in operation make: "+v1.value);
			return;
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()==new Value().getClass()){
			System.out.println("Error in operation make: "+v2.value);
			return;
		}
		Word w=(Word)v1;
		global.bindValue(w, v2);
	}
	private static Value thing(String[] input,Integer[] cursor){
		Boolean needToBeExplained=false;
		if(cursor[0]==1){
			needToBeExplained=true;
		}
		Value v=getValue(input,cursor);
		if(v.getClass()!=new Word("").getClass()){
			System.out.println("Error in operation thing: "+v.value);
			return new Value();
		}
		Word w=(Word)v;
		if(needToBeExplained){
			Integer[] cur={0};
			String[] in=global.getValueOf(w).value.split(" ");
			getOp(in,cur);
		}
		Value vv=global.getValueOf(w);
		if(vv.getClass()==new Value().getClass()){
			System.out.println("Syntax error: "+vv.value);
		}
		return vv;
	}
	private static Value colon(String[] input,Integer[] cursor){
		Value v=getValue(input,cursor);
		if(v.getClass()!=new Word("").getClass()){
			System.out.println("Error in operation \":\": "+v.value);
			return new Value();
		}
		Word w=(Word)v;
		Value vv=global.getValueOf(w);
		if(vv.getClass()==new Value().getClass()){
			System.out.println("Syntax error: "+vv.value);
		}
		return vv;
	}
	private static void erase(String[] input,Integer[] cursor){
		Value v=getValue(input,cursor);
		if(v.getClass()!=new Word("").getClass()){
			System.out.println("Error in operation erase: "+v.value);
		}
		Word w=(Word)v;
		global.debindValue(w);
	}
	private static Bool isname(String[] input,Integer[] cursor){
		Value v=getValue(input,cursor);
		if(v.getClass()!=new Word("").getClass()){
			System.out.println("Error in operation isname: "+v.value);
			return new Bool(false);
		}
		Word w=(Word)v;
		if(global.isName(w)){
			return new Bool(true);
		}else{
			return new Bool(false);
		}
	}
	private static void print(String[] input,Integer[] cursor){
		Value v=getValue(input,cursor);
		if(v.getClass()==new Value().getClass()){
			System.out.println("Error in operation print: "+v.value);
		}else{
			v.print();
			System.out.print("\n");
		}
	}
	private static Value read(String[] input,Integer[] cursor){
		Scanner scan=new Scanner(System.in);
		String value=scan.next();
		if(Number.isNumber(value)){
			return new Number(value);
		}else{
			return new Word(value);
		}
	}
	private static Value readlinst(String[] input,Integer[] cursor){
		Scanner scan=new Scanner(System.in);
		String line=new String("[");
		line+=scan.nextLine();
		line+="]";
		List l=new List();
		if(l.readList(line)==false){
			return new Value("Incorrect list!");
		}
		return l;
	}
	private static Value add(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Number().getClass()){
			System.out.println("Error in operation add: "+v1.value);
			return new Value("Incorrect number!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Number().getClass()){
			System.out.println("Error in operation add: "+v2.value);
			return new Value("Incorrect number!");
		}
		Number n1=(Number)v1;
		Number n2=(Number)v2;
		return Number.add(n1, n2);
	}
	private static Value sub(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Number().getClass()){
			System.out.println("Error in operation sub: "+v1.value);
			return new Value("Incorrect number!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Number().getClass()){
			System.out.println("Error in operation sub: "+v2.value);
			return new Value("Incorrect number!");
		}
		Number n1=(Number)v1;
		Number n2=(Number)v2;
		return Number.sub(n1, n2);
	}
	private static Value mul(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Number().getClass()){
			System.out.println("Error in operation mul: "+v1.value);
			return new Value("Incorrect number!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Number().getClass()){
			System.out.println("Error in operation mul: "+v2.value);
			return new Value("Incorrect number!");
		}
		Number n1=(Number)v1;
		Number n2=(Number)v2;
		return Number.mul(n1, n2);
	}
	private static Value div(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Number().getClass()){
			System.out.println("Error in operation div: "+v1.value);
			return new Value("Incorrect number!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Number().getClass()){
			System.out.println("Error in operation div: "+v2.value);
			return new Value("Incorrect number!");
		}
		Number n1=(Number)v1;
		Number n2=(Number)v2;
		return Number.div(n1, n2);
	}
	private static Value mod(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Number().getClass()){
			System.out.println("Error in operation mod: "+v1.value);
			return new Value("Incorrect number!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Number().getClass()){
			System.out.println("Error in operation mod: "+v2.value);
			return new Value("Incorrect number!");
		}
		Number n1=(Number)v1;
		Number n2=(Number)v2;
		return Number.mod(n1, n2);
	}
	private static Value eq(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Word("").getClass()&&v1.getClass()!=new Number().getClass()){
			System.out.println("Error in operation eq: "+v1.value);
			return new Value("Incorrect value!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Word("").getClass()&&v2.getClass()!=new Number().getClass()){
			System.out.println("Error in operation eq: "+v2.value);
			return new Value("Incorrect value!");
		}
		if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Word("").getClass()){
			Word w1=(Word)v1;
			Word w2=(Word)v2;
			return new Bool(Word.eq(w1, w2));
		}else if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Number().getClass()){
			Word w1=(Word)v1;
			Word w2=new Word(((Number)v2).value);
			return new Bool(Word.eq(w1, w2));
		}else if(v1.getClass()==new Number().getClass()&&v2.getClass()==new Word("").getClass()){
			Word w1=new Word(((Number)v1).value);
			Word w2=(Word)v2;
			return new Bool(Word.eq(w1, w2));
		}else{
			Number n1=(Number)v1;
			Number n2=(Number)v2;
			return new Bool(Number.eq(n1, n2));
		}
	}
	private static Value gt(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Word("").getClass()&&v1.getClass()!=new Number().getClass()){
			System.out.println("Error in operation gt: "+v1.value);
			return new Value("Incorrect value!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Word("").getClass()&&v2.getClass()!=new Number().getClass()){
			System.out.println("Error in operation gt: "+v2.value);
			return new Value("Incorrect value!");
		}
		if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Word("").getClass()){
			Word w1=(Word)v1;
			Word w2=(Word)v2;
			return new Bool(Word.gt(w1, w2));
		}else if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Number().getClass()){
			Word w1=(Word)v1;
			Word w2=new Word(((Number)v2).value);
			return new Bool(Word.gt(w1, w2));
		}else if(v1.getClass()==new Number().getClass()&&v2.getClass()==new Word("").getClass()){
			Word w1=new Word(((Number)v1).value);
			Word w2=(Word)v2;
			return new Bool(Word.gt(w1, w2));
		}else{
			Number n1=(Number)v1;
			Number n2=(Number)v2;
			return new Bool(Number.gt(n1, n2));
		}
	}
	private static Value lt(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Word("").getClass()&&v1.getClass()!=new Number().getClass()){
			System.out.println("Error in operation lt: "+v1.value);
			return new Value("Incorrect value!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Word("").getClass()&&v2.getClass()!=new Number().getClass()){
			System.out.println("Error in operation lt: "+v2.value);
			return new Value("Incorrect value!");
		}
		if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Word("").getClass()){
			Word w1=(Word)v1;
			Word w2=(Word)v2;
			return new Bool(Word.lt(w1, w2));
		}else if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Number().getClass()){
			Word w1=(Word)v1;
			Word w2=new Word(((Number)v2).value);
			return new Bool(Word.lt(w1, w2));
		}else if(v1.getClass()==new Number().getClass()&&v2.getClass()==new Word("").getClass()){
			Word w1=new Word(((Number)v1).value);
			Word w2=(Word)v2;
			return new Bool(Word.lt(w1, w2));
		}else{
			Number n1=(Number)v1;
			Number n2=(Number)v2;
			return new Bool(Number.lt(n1, n2));
		}
	}
	private static Value and(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Bool().getClass()){
			System.out.println("Error in operation and: "+v1.value);
			return new Value("Incorrect value!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Bool().getClass()){
			System.out.println("Error in operation and: "+v2.value);
			return new Value("Incorrect value!");
		}
		Bool b1=(Bool)v1;
		Bool b2=(Bool)v2;
		return Bool.and(b1, b2);
	}
	private static Value or(String[] input,Integer[] cursor){
		Value v1=getValue(input,cursor);
		if(v1.getClass()!=new Bool().getClass()){
			System.out.println("Error in operation or: "+v1.value);
			return new Value("Incorrect value!");
		}
		Value v2=getValue(input,cursor);
		if(v2.getClass()!=new Bool().getClass()){
			System.out.println("Error in operation or: "+v2.value);
			return new Value("Incorrect value!");
		}
		Bool b1=(Bool)v1;
		Bool b2=(Bool)v2;
		return Bool.or(b1, b2);
	}
	private static Value not(String[] input,Integer[] cursor){
		Value v=getValue(input,cursor);
		if(v.getClass()!=new Bool().getClass()){
			System.out.println("Error in operation not: "+v.value);
			return new Value("Incorrect value!");
		}
		Bool b=(Bool)v;
		return b.not();
	}
	private static void repeat(String[] input,Integer[] cursor){
		Value v=getValue(input,cursor);
		if(v.getClass()!=new Number().getClass()){
			System.out.println("Error in operation repeat: "+v.value);
			return;
		}
		Value l=getValue(input,cursor);
		if(l.getClass()!=new List().getClass()){
			System.out.println("Error in operation repeat: "+l.value);
			return;
		}
		Number n=(Number)v;
		Integer repTime=n.toInt();
		String[] val=l.value.split(" ");
		for(int i=0;i<repTime;i++){
			Integer[] cur={0};
			getOp(val,cur);
		}
	}
	private static Value getValue(String[] input,Integer[] cursor){
		if(cursor[0]>=input.length){
			return new Value("Can not get a value!");
		}
		if(Number.isNumber(input[cursor[0]])){
			Number n=new Number(input[cursor[0]]);
			cursor[0]++;
			return n;
		}else if(Bool.isBool(input[cursor[0]])){
			Bool b=new Bool(input[cursor[0]]);
			cursor[0]++;
			return b;
		}else if(input[cursor[0]].startsWith("[")){
			String list=new String();
			for(int i=cursor[0];i<input.length;i++){
				list+=input[i];
				if(i!=input.length-1){
					list+=" ";
				}
			}
			if(List.isList(list)==false){
				Word w=new Word(input[cursor[0]]);
				cursor[0]++;
				return w;
			}
			List l=new List();
			l.readList(list);
			cursor[0]=input.length;
			return l;
		}else if(Word.isOperator(input[cursor[0]])||input[cursor[0]].startsWith(":")){
			return getOp(input,cursor);
		}else if(input[cursor[0]].startsWith("\"")){
			if(Word.isWord(input[cursor[0]])){
				Word w=new Word(input[cursor[0]].substring(1));
				cursor[0]++;
				return w;
			}else{
				return new Value("Incorrect word!");
			}
		}else{
			return new Value("Incorrect value!");
		}
	}
}
