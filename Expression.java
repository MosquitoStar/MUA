import java.util.ArrayList;
import java.util.Stack;
public class Expression extends Value{
	public Value result;
	public Expression(){
		super();
	}
	public Expression(String s){
		super(s);
	}
	public static Boolean isExpression(String s){
		/*check unrelated symbols*/
		for(int i=0;i<s.length();i++){
			Character c=s.charAt(i);
			if(!(c=='+'||c=='-'||c=='*'||c=='/'||c=='%'||c=='('||c==')'||
				c=='.'||Character.isDigit(c))){
				return false;
			}
		}
		/*check brackets match*/
		int bracketCount=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='('){
				bracketCount++;
			}else if(s.charAt(i)==')'){
				bracketCount--;
			}
		}
		if(bracketCount!=0){
			return false;
		}
		/*check number format and abstract expression*/
		String tmpNum="";
		ArrayList<Character> op=new ArrayList<Character>();
		for(int i=0;i<s.length();i++){
			if(Character.isDigit(s.charAt(i))||s.charAt(i)=='.'){
				tmpNum+=s.charAt(i);
			}else if(s.charAt(i)=='-'){
				if(i==0){
					tmpNum+=s.charAt(i);
					continue;
				}
				if(Character.isDigit(s.charAt(i-1))||s.charAt(i-1)==')'){
					if(!tmpNum.equals("")){
						if(!Number.isNumber(tmpNum)){
							return false;
						}
						op.add('n');
					}
					op.add(s.charAt(i));
					tmpNum="";
				}else{
					tmpNum+=s.charAt(i);
				}
			}else{
				if(!tmpNum.equals("")){
					if(!Number.isNumber(tmpNum)){
						return false;
					}
					op.add('n');
				}
				op.add(s.charAt(i));
				tmpNum="";
			}
		}
		if(!tmpNum.equals("")){
			op.add('n');
		}
		/*check expression format*/
		for(int i=0;i<op.size();i++){
			if(i==0){
				if(op.get(i)==')'||op.get(i)=='+'||op.get(i)=='-'||
				   op.get(i)=='*'||op.get(i)=='/'||op.get(i)=='%'){
					return false;
				}
			}else if(i==op.size()-1){
				if(op.get(i)=='('||op.get(i)=='+'||op.get(i)=='-'||
				   op.get(i)=='*'||op.get(i)=='/'||op.get(i)=='%'){
					return false;
				}
			}else if(op.get(i)=='('){
				if(op.get(i-1)==')'||op.get(i-1)=='n'){
					return false;
				}
				if(op.get(i+1)==')'||op.get(i+1)=='+'||op.get(i+1)=='-'||
				   op.get(i+1)=='*'||op.get(i+1)=='/'||op.get(i+1)=='%'){
					return false;
				}
			}else if(op.get(i)==')'){
				if(op.get(i-1)=='('||op.get(i-1)=='+'||op.get(i-1)=='-'||
				   op.get(i-1)=='*'||op.get(i-1)=='/'||op.get(i-1)=='%'){
					return false;
				}
				if(op.get(i+1)=='('||op.get(i+1)=='n'){
					return false;
				}
			}else if(op.get(i)=='n'){
				if(op.get(i-1)==')'||op.get(i-1)=='n'){
					return false;
				}
				if(op.get(i+1)=='('||op.get(i+1)=='n'){
					return false;
				}
			}else{
				if(op.get(i-1)=='('||op.get(i-1)=='+'||op.get(i-1)=='-'||
				   op.get(i-1)=='*'||op.get(i-1)=='/'||op.get(i-1)=='%'){
					return false;
				}
				if(op.get(i+1)==')'||op.get(i+1)=='+'||op.get(i+1)=='-'||
				   op.get(i+1)=='*'||op.get(i+1)=='/'||op.get(i+1)=='%'){
					return false;
				}
			}
		}
		return true;
	}
	
	public static Boolean isExExpression(String s){
		/*check relative symbols*/
		Boolean hasSymbol=false;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='+'||s.charAt(i)=='-'||s.charAt(i)=='*'||s.charAt(i)=='/'||
			   s.charAt(i)=='%'||s.charAt(i)=='('||s.charAt(i)==')'){
				hasSymbol=true;
				break;
			}
		}
		if(!hasSymbol){
			return false;
		}
		/*check brackets match*/
		int bracketCount=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='('){
				bracketCount++;
			}else if(s.charAt(i)==')'){
				bracketCount--;
			}
		}
		if(bracketCount!=0){
			return false;
		}
		/*abstract expression*/
		String tmpNum="";
		ArrayList<Character> op=new ArrayList<Character>();
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='-'){
				if(i==0){
					tmpNum+=s.charAt(i);
					continue;
				}
				if(s.charAt(i-1)=='+'||s.charAt(i-1)=='-'||s.charAt(i-1)=='*'||
				   s.charAt(i-1)=='/'||s.charAt(i-1)=='%'||s.charAt(i-1)=='('){
					tmpNum+=s.charAt(i);
				}else{
					if(!tmpNum.equals("")){
						op.add('n');
					}
					op.add(s.charAt(i));
					tmpNum="";
				}
			}else if(s.charAt(i)=='+'||s.charAt(i)=='*'||s.charAt(i)=='/'||s.charAt(i)=='%'){
				if(!tmpNum.equals("")){
					op.add('n');
				}
				op.add(s.charAt(i));
				tmpNum="";
			}else{
				tmpNum+=s.charAt(i);
			}
		}
		if(!tmpNum.equals("")){
			op.add('n');
		}
		/*check expression format*/
		for(int i=0;i<op.size();i++){
			if(i==0){
				if(op.get(i)==')'||op.get(i)=='+'||op.get(i)=='-'||
				   op.get(i)=='*'||op.get(i)=='/'||op.get(i)=='%'){
					return false;
				}
			}else if(i==op.size()-1){
				if(op.get(i)=='('||op.get(i)=='+'||op.get(i)=='-'||
				   op.get(i)=='*'||op.get(i)=='/'||op.get(i)=='%'){
					return false;
				}
			}else if(op.get(i)=='('){
				if(op.get(i-1)==')'||op.get(i-1)=='n'){
					return false;
				}
				if(op.get(i+1)==')'||op.get(i+1)=='+'||op.get(i+1)=='-'||
				   op.get(i+1)=='*'||op.get(i+1)=='/'||op.get(i+1)=='%'){
					return false;
				}
			}else if(op.get(i)==')'){
				if(op.get(i-1)=='('||op.get(i-1)=='+'||op.get(i-1)=='-'||
				   op.get(i-1)=='*'||op.get(i-1)=='/'||op.get(i-1)=='%'){
					return false;
				}
				if(op.get(i+1)=='('||op.get(i+1)=='n'){
					return false;
				}
			}else if(op.get(i)=='n'){
				if(op.get(i-1)==')'||op.get(i-1)=='n'){
					return false;
				}
				if(op.get(i+1)=='('||op.get(i+1)=='n'){
					return false;
				}
			}else{
				if(op.get(i-1)=='('||op.get(i-1)=='+'||op.get(i-1)=='-'||
				   op.get(i-1)=='*'||op.get(i-1)=='/'||op.get(i-1)=='%'){
					return false;
				}
				if(op.get(i+1)==')'||op.get(i+1)=='+'||op.get(i+1)=='-'||
				   op.get(i+1)=='*'||op.get(i+1)=='/'||op.get(i+1)=='%'){
					return false;
				}
			}
		}
		return true;
	}
	
	public void calculate(NameMap names){
		if(!formulate(names)){
			System.out.println("Expression "+value+" is invalid!");
			result=new Value("Expression error!");
			return;
		}
		result=calculate(value);
	}
	
	private Boolean formulate(NameMap names){
		String tmpNum="";
		ArrayList<String> expr=new ArrayList<String>();
		for(int i=0;i<value.length();i++){
			if(value.charAt(i)=='-'){
				if(i==0){
					tmpNum+=value.charAt(i);
					continue;
				}
				if(value.charAt(i-1)=='+'||value.charAt(i-1)=='-'||value.charAt(i-1)=='*'||
				   value.charAt(i-1)=='/'||value.charAt(i-1)=='%'||value.charAt(i-1)=='('){
					tmpNum+=value.charAt(i);
				}else{
					if(!tmpNum.equals("")){
						if(Number.isNumber(tmpNum)){
							expr.add(tmpNum);
							expr.add(value.charAt(i)+"");
							tmpNum="";
							continue;
						}else{
							AdvInterpreter a=new AdvInterpreter(RunMode.main,names);
							Value ret=a.interpretStmt(tmpNum);
							if(ret.getClass()==new Number().getClass()){
								expr.add(ret.value);
								expr.add(value.charAt(i)+"");
								tmpNum="";
								continue;
							}
							return false;
						}						
					}
				}
			}else if(value.charAt(i)=='+'||value.charAt(i)=='*'||value.charAt(i)=='/'||
					 value.charAt(i)=='%'||value.charAt(i)==')'){
				if(!tmpNum.equals("")){
					if(Number.isNumber(tmpNum)){
						expr.add(tmpNum);
						expr.add(value.charAt(i)+"");
						tmpNum="";
						continue;
					}else{
						AdvInterpreter a=new AdvInterpreter(RunMode.main,names);
						Value ret=a.interpretStmt(tmpNum);
						if(ret.getClass()==new Number().getClass()){
							expr.add(ret.value);
							expr.add(value.charAt(i)+"");
							tmpNum="";
							continue;
						}
						return false;
					}
				}
				expr.add(value.charAt(i)+"");
			}else if(value.charAt(i)=='('){
				expr.add(value.charAt(i)+"");
			}else if(value.charAt(i)==' '){
				if(!tmpNum.equals("")){
					tmpNum+=value.charAt(i);
				}
			}else{
				tmpNum+=value.charAt(i);
			}
		}
		String nvalue="";
		for(int i=0;i<expr.size();i++){
			if(Number.isNumber(expr.get(i))){
				Double n;
				try{
					n=Double.parseDouble(expr.get(i));
				}catch(Exception e){
					System.out.println("gg");
					return false;
				}
				nvalue+=n.toString();
			}else{
				nvalue+=expr.get(i);
			}
		}
		value=nvalue;
		return true;
	}
	
	public static Value calculate(String exp){
		/*Interpret expression*/
		ArrayList<String> nifixExp=new ArrayList<String>();
		String tmpNum="";
		for(int i=0;i<exp.length();i++){
			if(Character.isDigit(exp.charAt(i))||exp.charAt(i)=='.'){
				tmpNum+=exp.charAt(i);
			}else if(exp.charAt(i)=='-'){
				if(i==0){
					tmpNum+=exp.charAt(i);
					continue;
				}
				if(Character.isDigit(exp.charAt(i-1))||exp.charAt(i-1)==')'){
					if(!tmpNum.equals("")){
						nifixExp.add(tmpNum);
					}
					nifixExp.add(exp.charAt(i)+"");
					tmpNum="";
				}else{
					tmpNum+=exp.charAt(i);
				}
			}else{
				if(!tmpNum.equals("")){
					nifixExp.add(tmpNum);
				}
				nifixExp.add(exp.charAt(i)+"");
				tmpNum="";
			}
		}
		if(!tmpNum.equals("")){
			nifixExp.add(tmpNum);
		}
		
		/*Nifix to Postfix*/
		ArrayList<String> postfixExp=new ArrayList<String>();
		Stack<String> opSt=new Stack<String>();
		for(int i=0;i<nifixExp.size();i++){
			if(Number.isNumber(nifixExp.get(i))){
				postfixExp.add(nifixExp.get(i));
			}else if(nifixExp.get(i).equals("(")){
				opSt.push(nifixExp.get(i));
			}else if(nifixExp.get(i).equals(")")){
				String topOp=opSt.peek();
				while(!topOp.equals("(")){
					postfixExp.add(opSt.pop());
					topOp=opSt.peek();
				}
				opSt.pop();
			}else if(nifixExp.get(i).equals("+")||nifixExp.get(i).equals("-")||
					nifixExp.get(i).equals("*")||nifixExp.get(i).equals("/")||
					nifixExp.get(i).equals("%")){
				while(!opSt.isEmpty()){
					String topOp=opSt.peek();
					if(priorityHigher(topOp,nifixExp.get(i))){
						postfixExp.add(opSt.pop());
					}else{
						break;
					}
				}
				opSt.push(nifixExp.get(i));
			}else{
				return new Value(exp+" is not a correct expression!");
			}
		}
		while(!opSt.isEmpty()){
			postfixExp.add(opSt.pop());
		}

		/*Calculate postfix expression*/
		Stack<Double> numbers=new Stack<Double>();
		for(int i=0;i<postfixExp.size();i++){
			if(Number.isNumber(postfixExp.get(i))){
				numbers.push(Double.parseDouble(postfixExp.get(i)));
				continue;
			}
			Double right=numbers.pop();
			Double left=numbers.pop();
			if(postfixExp.get(i).equals("+")){
				numbers.push(left+right);
			}else if(postfixExp.get(i).equals("-")){
				numbers.push(left-right);
			}else if(postfixExp.get(i).equals("*")){
				numbers.push(left*right);
			}else if(postfixExp.get(i).equals("/")){
				numbers.push(left/right);
			}else if(postfixExp.get(i).equals("%")){
				numbers.push(left%right);
			}
		}
		return new Number(numbers.peek());
	}
	
	static Boolean priorityHigher(String thisOp,String thatOp){
		int thisPriority=0;
		int thatPriority=0;
		if(thisOp.equals("+")||thisOp.equals("-")){
			thisPriority=1;
		}else if(thisOp.equals("*")||thisOp.equals("/")||thisOp.equals("%")){
			thisPriority=2;
		}
		if(thatOp.equals("+")||thatOp.equals("-")){
			thatPriority=1;
		}else if(thatOp.equals("*")||thatOp.equals("/")||thatOp.equals("%")){
			thatPriority=2;
		}
		return thisPriority>=thatPriority;
	}
}
