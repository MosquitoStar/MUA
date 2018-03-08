import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
public class AdvInterpreter {
	private NameMap foreignName;
	private NameMap localName;
	private Scanner scan;
	private Queue<String> inputQueue;
	private Value returnValue;
	private RunMode mode;
	
	public AdvInterpreter(RunMode m){
		mode=m;
		foreignName=new NameMap();
		localName=new NameMap();
		scan=new Scanner(System.in);
		inputQueue=new LinkedList<String>();
		returnValue=new Value("No return value");
	}
	
	public AdvInterpreter(RunMode m,NameMap foreign){
		mode=m;
		foreignName=foreign;
		localName=new NameMap();
		scan=new Scanner(System.in);
		inputQueue=new LinkedList<String>();
		returnValue=new Value("No return value");
	}
	
	public AdvInterpreter(RunMode m,NameMap foreign,NameMap local){
		mode=m;
		foreignName=foreign;
		localName=local;
		scan=new Scanner(System.in);
		inputQueue=new LinkedList<String>();
		returnValue=new Value("No return value");
	}
	
	public void interpret(){
		while(scan.hasNext()){
			String next=scan.next();
			getOp(next);
		}
		scan.close();
	}
	
	public Value interpret(String s){
		String[] val=s.split(" ");
		enqueue(val);
		while(!inputQueue.isEmpty()){
			String next=getNext();
			getOp(next);
		}
		return returnValue;
	}
	
	public Value interpretStmt(String s){
		String[] val=s.split(" ");
		enqueue(val);
		String next=getNext();
		Value v=getOp(next);
		if(!inputQueue.isEmpty()){
			return new Value("AdvInterpreter.interpretStmt can not deal with multiple statement!");
		}
		return v;
	}
	
	private void enqueue(String[] input){
		for(int i=0;i<input.length;i++){
			inputQueue.offer(input[i]);
		}
	}
	
	private String getNext(){
		if(inputQueue.isEmpty()){
			return scan.next();
		}else{
			return inputQueue.poll();
		}
	}
	
	public Value getOp(String s){
		switch(s){
		case "make":
			make();
			return new Value("Operation make do not have a return value!");
		case "thing":
			return thing();
		case "erase":
			erase();
			return new Value("Operation erase do not have a return value!");
		case "isname":
			return isname();
		case "print":
			print();
			return new Value("Operation print do not have a return value!");
		case "read":
			return read();
		case "readlinst":
			return readlinst();
		case "abs":
			return abs();
		case "add":
			return add();
		case "sub":
			return sub();
		case "mul":
			return mul();
		case "div":
			return div();
		case "mod":
			return mod();
		case "eq":
			return eq();
		case "gt":
			return gt();
		case "lt":
			return lt();
		case "and":
			return and();
		case "or":
			return or();
		case "not":
			return not();
		case "repeat":
			repeat();
			return new Value("Operation repeat do not have a return value!");
		case "random":
			return random_f();
		case "sqrt":
			return sqrt();
		case "isnumber":
			return isnumber();
		case "isword":
			return isword();
		case "islist":
			return islist();
		case "isbool":
			return isbool();
		case "isempty":
			return isempty();
		case "int":
			return floor();
		case "word":
			return word();
		case "if":
			if_f();
			return new Value("Operation if do not have a return value!");
		case "sentence":
			return sentence();
		case "list":
			return list();
		case "join":
			join();
			return new Value("Operation repeat do not have a return value!");
		case "first":
			return first();
		case "last":
			return last();
		case "butfirst":
			return butfirst();
		case "butlast":
			return butlast();
		case "wait":
			wait_f();
			return new Value("Operation wait do not have a return value!");
		case "save":
			save();
			return new Value("Operation save do not have a return value!");
		case "load":
			load();
			return new Value("Operation load do not have a return value!");
		case "erall":
			erall();
			return new Value("Operation erall do not have a return value!");
		case "poall":
			poall();
			return new Value("Operation poall do not have a return value!");
		case "pi":
			return new Number(3.14159);
		case "run":
			run();
			return new Value("Opertaion run do not have a return value!");
		case "output":
			if(mode!=RunMode.function){
				System.out.println("Operation output can not use outside function!");
				return new Value();
			}
			output();
			return new Value("Operation output do not have a return value!");
		case "stop":
			if(inputQueue.isEmpty()){
				System.out.println("Operation stop can not be used outside list!");
				return new Value();
			}
			stop();
			return new Value("Operation stop do not have a return value!");
		default:
			if(s.startsWith("//")){
				return new Value("This is zhushi");
			}else if(s.startsWith(":")){
				return colon(s.substring(1));
			}else if(s.startsWith("[")){
				return new List();
			}else if(s.startsWith("(")){
				Boolean si=false;
				if(inputQueue.isEmpty()){
					si=true;
				}
				Value v=getExpression(s,si);
				if(v.getClass()!=new Expression().getClass()){
					return new Value(v.value);
				}
				Expression exp=(Expression)v;
				exp.calculate(NameMap.combine(foreignName, localName));
				return exp.result;
			}else if(Number.isNumber(s)){
				System.out.println("The interpreter don't know how to do with this Number--"+s);
				return new Number(s);
			}else if(Bool.isBool(s)){
				System.out.println("The interpreter don't know how to do with this Bool--"+s);
				return new Bool(s);
			}else if(Word.isWord(s)){
				System.out.println("The interpreter don't know how to do with this Word--"+s);
				return new Word(s.substring(1));
			}else{
				Word w=new Word(s);
				Value glo=foreignName.getValueOf(w);
				if(glo.getClass()==new List().getClass()){
					if(Function.isFunctionBody((List)glo)){
						return function(s,false);
					}
				}
				Value loc=localName.getValueOf(w);
				if(loc.getClass()==new List().getClass()){
					if(Function.isFunctionBody((List)loc)){
						return function(s,true);
					}
				}
				System.out.println(s+" is not an operator or a function or a value!");
				return new Value();
			}
		}
	}
	
	private Value function(String funcName,Boolean isLocal){
		List definition;
		if(!isLocal){
			definition=(List)foreignName.getValueOf(new Word(funcName));
		}else{
			definition=(List)localName.getValueOf(new Word(funcName));
		}
		List parameters=(List)definition.getElement(0);
		List operations=(List)definition.getElement(1);
		String[] pmName=new String[parameters.elementCount()];
		Value[] pmValue=new Value[parameters.elementCount()];
		NameMap local=new NameMap();
		for(int i=0;i<parameters.elementCount();i++){
			pmName[i]=parameters.getElement(i).value;
			Word w=new Word(pmName[i]);
			Value v=getValue();
			if(v.getClass()==new Value().getClass()){
				v.print();
				return new Value("Function "+funcName+" gets a wrong value!");
			}
			pmValue[i]=v;
			local.bindValue(w, v);
		}
		Value ret=operations.exec(RunMode.function,NameMap.combine(foreignName,localName),local);
		if(ret.getClass()==new Value().getClass()){
			return new Value("Function "+funcName+" do not have a return value!");
		}
		return ret;
	}
	
	private void make(){
		Value w=getWord();
		if(w.getClass()!=new Word("").getClass()){
			w.print();
			System.out.println("Operation make failed!");
			return;
		}
		Value v=getValue();
		if(v.getClass()==new Value().getClass()){
			v.print();
			System.out.println("Operation make failed!");
			return;
		}
		localName.bindValue((Word)w, v);
	}
	private Value thing(){
		Value w=getWord();
		if(w.getClass()!=new Word("").getClass()){
			w.print();
			return new Value("Operation thing failed!");
		}
		if(localName.isName((Word)w)){
			return localName.getValueOf((Word)w);
		}
		if(foreignName.isName((Word)w)){
			return foreignName.getValueOf((Word)w);
		}
		System.out.println(w.value+" is not a name!");
		return new Value("Operation thing failed!");
	}
	private Value colon(String s){
		Word w=new Word(s);
		if(localName.isName(w)){
			return localName.getValueOf(w);
		}
		if(foreignName.isName(w)){
			return foreignName.getValueOf(w);
		}
		System.out.println(w.value+" is not a name!");
		return new Value("Operation : failed!");
	}
	private void erase(){
		Value w=getWord();
		if(w.getClass()!=new Word("").getClass()){
			w.print();
			System.out.println("Operation erase failed!");
			return;
		}
		if(localName.isName((Word)w)){
			localName.debindValue((Word)w);
		}else{
			foreignName.debindValue((Word)w);
		}
	}
	private Value isname(){
		Value w=getWord();
		if(w.getClass()!=new Word("").getClass()){
			w.print();
			return new Value("Operation isname failed!");
		}
		if(localName.isName((Word)w)){
			return new Bool(true);
		}else if(foreignName.isName((Word)w)){
			return new Bool(true);
		}else{
			return new Bool(false);
		}
	}
	private void print(){
		Value v=getValue();
		v.print();
		if(v.getClass()==new Value().getClass()){
			System.out.println("Operation print failed!");
		}else{
			System.out.print("\n");
		}
	}
	private Value read(){
		String value=scan.next();
		if(Number.isNumber(value)){
			return new Number(value);
		}else{
			return new Word(value);
		}
	}
	private Value readlinst(){
		Scanner scan2=new Scanner(System.in);
		String line=new String("[");
		line+=scan2.nextLine();
		line+="]";
		List l=new List();
		if(l.readList(line)==false){
			return new Value(line+" is not a list!");
		}
		return l;
	}
	private Value abs(){
		Value n=getNumber();
		if(n.getClass()!=new Number().getClass()){
			n.print();
			return new Value("Operation abs failed!");
		}
		return ((Number)n).abs();
	}
	private Value add(){
		Value n1=getNumber();
		if(n1.getClass()!=new Number().getClass()){
			n1.print();
			return new Value("Operation add failed!");
		}
		Value n2=getValue();
		if(n2.getClass()!=new Number().getClass()){
			n2.print();
			return new Value("Operation add failed!");
		}
		return Number.add((Number)n1, (Number)n2);
	}
	private Value sub(){
		Value n1=getNumber();
		if(n1.getClass()!=new Number().getClass()){
			n1.print();
			return new Value("Operation sub failed!");
		}
		Value n2=getValue();
		if(n2.getClass()!=new Number().getClass()){
			n2.print();
			return new Value("Operation sub failed!");
		}
		return Number.sub((Number)n1, (Number)n2);
	}
	private Value mul(){
		Value n1=getNumber();
		if(n1.getClass()!=new Number().getClass()){
			n1.print();
			return new Value("Operation mul failed!");
		}
		Value n2=getValue();
		if(n2.getClass()!=new Number().getClass()){
			n2.print();
			return new Value("Operation mul failed!");
		}
		return Number.mul((Number)n1, (Number)n2);
	}
	private Value div(){
		Value n1=getNumber();
		if(n1.getClass()!=new Number().getClass()){
			n1.print();
			return new Value("Operation div failed!");
		}
		Value n2=getValue();
		if(n2.getClass()!=new Number().getClass()){
			n2.print();
			return new Value("Operation div failed!");
		}
		return Number.div((Number)n1, (Number)n2);
	}
	private Value mod(){
		Value n1=getNumber();
		if(n1.getClass()!=new Number().getClass()){
			n1.print();
			return new Value("Operation mod failed!");
		}
		Value n2=getValue();
		if(n2.getClass()!=new Number().getClass()){
			n2.print();
			return new Value("Operation mod failed!");
		}
		return Number.mod((Number)n1, (Number)n2);
	}
	private Value eq(){
		Value v1=getValue();
		if(v1.getClass()!=new Word("").getClass()&&v1.getClass()!=new Number().getClass()){
			v1.print();
			return new Value("Operation eq failed!");
		}
		Value v2=getValue();
		if(v2.getClass()!=new Word("").getClass()&&v2.getClass()!=new Number().getClass()){
			v2.print();
			return new Value("Operation eq failed!");
		}
		if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Word("").getClass()){
			return new Bool(Word.eq((Word)v1, (Word)v2));
		}else if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Number().getClass()){
			return new Bool(Word.eq((Word)v1, new Word(((Number)v2).value)));
		}else if(v1.getClass()==new Number().getClass()&&v2.getClass()==new Word("").getClass()){
			return new Bool(Word.eq(new Word(((Number)v1).value), (Word)v2));
		}else{
			return new Bool(Number.eq((Number)v1,(Number)v2));
		}
	}
	private Value gt(){
		Value v1=getValue();
		if(v1.getClass()!=new Word("").getClass()&&v1.getClass()!=new Number().getClass()){
			v1.print();
			return new Value("Operation gt failed!");
		}
		Value v2=getValue();
		if(v2.getClass()!=new Word("").getClass()&&v2.getClass()!=new Number().getClass()){
			v2.print();
			return new Value("Operation gt failed!");
		}
		if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Word("").getClass()){
			return new Bool(Word.gt((Word)v1, (Word)v2));
		}else if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Number().getClass()){
			return new Bool(Word.gt((Word)v1, new Word(((Number)v2).value)));
		}else if(v1.getClass()==new Number().getClass()&&v2.getClass()==new Word("").getClass()){
			return new Bool(Word.gt(new Word(((Number)v1).value), (Word)v2));
		}else{
			return new Bool(Number.gt((Number)v1,(Number)v2));
		}
	}
	private Value lt(){
		Value v1=getValue();
		if(v1.getClass()!=new Word("").getClass()&&v1.getClass()!=new Number().getClass()){
			v1.print();
			return new Value("Operation lt failed!");
		}
		Value v2=getValue();
		if(v2.getClass()!=new Word("").getClass()&&v2.getClass()!=new Number().getClass()){
			v2.print();
			return new Value("Operation lt failed!");
		}
		if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Word("").getClass()){
			return new Bool(Word.lt((Word)v1, (Word)v2));
		}else if(v1.getClass()==new Word("").getClass()&&v2.getClass()==new Number().getClass()){
			return new Bool(Word.lt((Word)v1, new Word(((Number)v2).value)));
		}else if(v1.getClass()==new Number().getClass()&&v2.getClass()==new Word("").getClass()){
			return new Bool(Word.lt(new Word(((Number)v1).value), (Word)v2));
		}else{
			return new Bool(Number.lt((Number)v1,(Number)v2));
		}
	}
	private Value and(){
		Value b1=getBool();
		if(b1.getClass()!=new Bool().getClass()){
			b1.print();
			return new Value("Operation and failed!");
		}
		Value b2=getBool();
		if(b2.getClass()!=new Bool().getClass()){
			b2.print();
			return new Value("Operation and failed!");
		}
		return Bool.and((Bool)b1,(Bool)b2);
	}
	private Value or(){
		Value b1=getBool();
		if(b1.getClass()!=new Bool().getClass()){
			b1.print();
			return new Value("Operation or failed!");
		}
		Value b2=getBool();
		if(b2.getClass()!=new Bool().getClass()){
			b2.print();
			return new Value("Operation or failed!");
		}
		return Bool.or((Bool)b1,(Bool)b2);
	}
	private Value not(){
		Value b=getBool();
		if(b.getClass()!=new Bool().getClass()){
			b.print();
			return new Value("Operation not failed!");
		}
		return ((Bool)b).not();
	}
	private void repeat(){
		Value n=getNumber();
		if(n.getClass()!=new Number().getClass()){
			n.print();
			System.out.println("Operation repeat failed!");
			return;
		}
		Value l=getList();
		if(l.getClass()!=new List().getClass()){
			l.print();
			System.out.println("Operation repeat failed!");
			return;
		}
		Integer repTime=((Number)n).toInt();
		for(int i=0;i<repTime;i++){
			((List)l).exec(mode,NameMap.combine(foreignName,localName));
		}
	}
	private void output(){
		Value v=getValue();
		if(v.getClass()==new Value().getClass()){
			v.print();
			System.out.println("Operation output failed!");
			return;
		}
		returnValue=v;
	}
	private void stop(){
		inputQueue.clear();
	}
	private Value random_f(){
		Value n=getNumber();
		if(n.getClass()!=new Number().getClass()){
			n.print();
			return new Value("Operation random failed!");
		}
		Random r=new Random();
		Number ub=(Number)n;
		Double result=ub.toDouble()*r.nextDouble();
		return new Number(result);
	}
	private Value sqrt(){
		Value n=getNumber();
		if(n.getClass()!=new Number().getClass()){
			n.print();
			return new Value("Operation sqrt failed!");
		}
		Double result=Math.sqrt(((Number)n).toDouble());
		return new Number(result);
	}
	private Value isnumber(){
		Value v=getNumber();
		if(v.getClass()==new Number().getClass()){
			return new Bool(true);
		}else{
			return new Bool(false);
		}
	}
	private Value isword(){
		Value v=getWord();
		if(v.getClass()==new Word("").getClass()){
			return new Bool(true);
		}else{
			return new Bool(false);
		}
	}
	private Value islist(){
		Value v=getList();
		if(v.getClass()==new List().getClass()){
			return new Bool(true);
		}else{
			return new Bool(false);
		}
	}
	private Value isbool(){
		Value v=getBool();
		if(v.getClass()==new Bool().getClass()){
			return new Bool(true);
		}else{
			return new Bool(false);
		}
	}
	private Value isempty(){
		Value v=getValue();
		if(v.getClass()==new Word("").getClass()){
			return new Bool(((Word)v).isempty());
		}else if(v.getClass()==new List().getClass()){
			return new Bool(((List)v).isempty());
		}
		return new Value(v.value+" is not a word or a list!\nOperation isempty failed!");
	}
	private Value floor(){
		Value n=getNumber();
		if(n.getClass()!=new Number().getClass()){
			n.print();
			return new Value("Operation int failed!");
		}
		return new Number(((Number)n).toInt());
	}
	private Value word(){
		Value w=getWord();
		if(w.getClass()!=new Word("").getClass()){
			w.print();
			return new Value("Operation word failed!");
		}
		Value v=getValue();
		if(v.getClass()==new Value().getClass()){
			v.print();
			return new Value("Operation word failed!");
		}else if(v.getClass()==new List().getClass()){
			System.out.println("List can not combine with word!");
			return new Value("Operation word failed!");
		}
		return Word.joinWord((Word)w, v);
	}
	private void if_f(){
		Value b=getBool();
		if(b.getClass()!=new Bool().getClass()){
			b.print();
			System.out.println("Operation if failed!");
		}
		Value l1=getList();
		if(l1.getClass()!=new List().getClass()){
			l1.print();
			System.out.println("Operation if failed!");
		}
		Value l2=getList();
		if(l2.getClass()!=new List().getClass()){
			l2.print();
			System.out.println("Operation if failed!");
		}
		if(((Bool)b).isTrue()){
			((List)l1).exec(mode, NameMap.combine(foreignName, localName));
		}else{
			((List)l2).exec(mode, NameMap.combine(foreignName, localName));
		}
	}
	private Value sentence(){
		Value l1=getList();
		if(l1.getClass()!=new List().getClass()){
			l1.print();
			return new Value("Operation sentence failed!");
		}
		Value l2=getList();
		if(l2.getClass()!=new List().getClass()){
			l2.print();
			return new Value("Operation sentence failed!");
		}
		return List.sentence((List)l1,(List)l2);
	}
	private Value list(){
		Value v1=getValue();
		if(v1.getClass()==new Value().getClass()){
			v1.print();
			return new Value("Operation list failed!");
		}
		Value v2=getValue();
		if(v2.getClass()==new Value().getClass()){
			v2.print();
			return new Value("Operation list failed!");
		}
		return List.formList(v1, v2);
	}
	private void join(){
		Value l=getList();
		if(l.getClass()!=new List().getClass()){
			l.print();
			System.out.println("Operation join failed!");
			return;
		}
		Value v=getValue();
		if(v.getClass()==new Value().getClass()){
			v.print();
			System.out.println("Operation join failed!");
			return;
		}
		((List)l).join(v);
	}
	private Value first(){
		Value v=getValue();
		if(v.getClass()==new List().getClass()){
			return ((List)v).firstElement();
		}else if(v.getClass()==new Word("").getClass()){
			return ((Word)v).firstChar();
		}else{
			v.print();
			return new Value("Operation first failed!");
		}
	}
	private Value last(){
		Value v=getValue();
		if(v.getClass()==new List().getClass()){
			return ((List)v).lastElement();
		}else if(v.getClass()==new Word("").getClass()){
			return ((Word)v).lastChar();
		}else{
			v.print();
			return new Value("Operation last failed!");
		}
	}
	private Value butfirst(){
		Value v=getValue();
		if(v.getClass()==new List().getClass()){
			return ((List)v).listButFirst();
		}else if(v.getClass()==new Word("").getClass()){
			return ((Word)v).wordButFirst();
		}else{
			v.print();
			return new Value("Operation butfirst failed!");
		}
	}
	private Value butlast(){
		Value v=getValue();
		if(v.getClass()==new List().getClass()){
			return ((List)v).listButLast();
		}else if(v.getClass()==new Word("").getClass()){
			return ((Word)v).wordButLast();
		}else{
			v.print();
			return new Value("Operation butlast failed!");
		}
	}
	private void wait_f(){
		Value n=getNumber();
		if(n.getClass()!=new Number().getClass()){
			n.print();
			System.out.println("Operation wait failed!");
		}
		try{
			Thread.sleep(((Number)n).toInt());
		}catch(Exception e){}
	}
	private void save(){
		Value w=getWord();
		if(w.getClass()!=new Word("").getClass()){
			w.print();
			System.out.println("Opertaion save failed!");
		}
		String fileName=w.value;
		FileWriter f;
		try {
			f=new FileWriter(fileName);
			String[] keys=localName.getAllKeys();
			for(int i=0;i<keys.length;i++){
				Value v=localName.getValueOf(new Word(keys[i]));
				f.write(keys[i]+" "+v.value+"\n");
			}
		} catch (IOException e) {
			System.out.print("IOException occurs!\nOperation save failed!\n");
			e.printStackTrace();
		}
	}
	private void load(){
		Value w=getWord();
		if(w.getClass()!=new Word("").getClass()){
			w.print();
			System.out.println("Opertaion load failed!");
		}
		String fileName=w.value;
		FileReader f;
		try {
			f=new FileReader(fileName);
			ArrayList<String> keys=new ArrayList<String>();
			ArrayList<Value> values=new ArrayList<Value>();
			int ch=0;
			String key="";
			String value="";
			int flag=0;
			while((ch=f.read())!=-1){
				if(ch==' '){
					keys.add(key);
					flag=1;
				}else if(ch=='\n'){
					flag=0;
					if(Number.isNumber(value)){
						values.add(new Number(value));
					}else if(Bool.isBool(value)){
						values.add(new Bool(value));
					}else{
						values.add(new Word(value));
					}
				}else{
					if(flag==0){
						key+=""+((char)ch);
					}else{
						value+=""+((char)ch);
					}
				}
			}
			for(int i=0;i<values.size();i++){
				localName.bindValue(new Word(keys.get(i)), values.get(i));
			}
			f.close();
		} catch (FileNotFoundException e) {
			System.out.print(fileName+" not found!\nOperation load failed!\n");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void erall(){
		localName.debindAll();
	}
	private void poall(){
		String[] keys=localName.getAllKeys();
		for(int i=0;i<keys.length;i++){
			Value v=localName.getValueOf(new Word(keys[i]));
			System.out.println(keys[i]+" "+v.value);
		}
	}
	private void run(){
		Value l=getList();
		if(l.getClass()!=new List().getClass()){
			l.print();
			System.out.println("Operation run failed!");
		}
		((List)l).exec(mode, NameMap.combine(foreignName, localName));
	}
	
	private Value getValue(){
		Boolean si=false;
		if(inputQueue.isEmpty()){
			si=true;
		}
		String next=getNext();
		if(Number.isNumber(next)){
			return new Number(next);
		}else if(Bool.isBool(next)){
			return new Bool(next);
		}else if(next.startsWith("[")){
			return getList(next,si);
		}else if(next.startsWith("(")){
			Value v=getExpression(next,si);
			if(v.getClass()!=new Expression().getClass()){
				v.print();
				return new Value(v.value);
			}
			Expression exp=(Expression)v;
			exp.calculate(NameMap.combine(foreignName, localName));
			return exp.result;
		}else if(Word.isOperator(next)||next.startsWith(":")){
			return getOp(next);
		}else if(Word.isWord(next)){
			return new Word(next.substring(1));
		}else{
			Word w=new Word(next);
			Value glo=foreignName.getValueOf(w);
			if(glo.getClass()==new List().getClass()){
				if(Function.isFunctionBody((List)glo)){
					return function(next,true);
				}
			}
			Value loc=localName.getValueOf(w);
			if(loc.getClass()==new List().getClass()){
				if(Function.isFunctionBody((List)loc)){
					return function(next,false);
				}
			}
			return new Value(next+" is not a value(number, word, list or bool)!");
		}
	}
	private Value getList(){
		String list="";
		int bracketCount=0;
		Boolean isFirst=true;
		Boolean readFromSystemIn=false;
		if(inputQueue.isEmpty()){
			readFromSystemIn=true;
		}
		if(readFromSystemIn){
			while(scan.hasNext()){
				String next=getNext();
				for(int i=0;i<next.length();i++){
					if(next.charAt(i)=='['){
						bracketCount++;
					}else if(next.charAt(i)==']'){
						bracketCount--;
					}
					if(bracketCount<=0&&i!=next.length()-1){
						return new Value(list+" is not a list!"); 
					}
				}
				if(isFirst){
					list+=next;
					isFirst=false;
				}else{
					list+=" "+next;
				}
				if(bracketCount==0){
					if(!List.isList(list)){
						return new Value(list+" is not a list!");
					}
					List l=new List();
					l.readList(list);
					return l;
				}
			}
		}else{
			while(!inputQueue.isEmpty()){
				String next=getNext();
				for(int i=0;i<next.length();i++){
					if(next.charAt(i)=='['){
						bracketCount++;
					}else if(next.charAt(i)==']'){
						bracketCount--;
					}
					if(bracketCount<=0&&i!=next.length()-1){
						return new Value(list+" is not a list!"); 
					}
				}
				if(isFirst){
					list+=next;
					isFirst=false;
				}else{
					list+=" "+next;
				}
				if(bracketCount==0){
					if(!List.isList(list)){
						return new Value(list+" is not a list!");
					}
					List l=new List();
					l.readList(list);
					return l;
				}
			}
		}
		return new Value(list+" is not a list!");
	}
	private Value getList(String s,Boolean SystemIn){
		int bracketCount=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='['){
				bracketCount++;
			}else if(s.charAt(i)==']'){
				bracketCount--;
			}
			if(bracketCount<=0&&i!=s.length()-1){
				return new Value(s+" is not a list!"); 
			}
		}
		if(bracketCount==0){
			if(!List.isList(s)){
				return new Value(s+" is not a list!");
			}
			List l=new List();
			l.readList(s);
			return l;
		}
		String list=s;
		if(SystemIn){
			while(scan.hasNext()){
				String next=getNext();
				for(int i=0;i<next.length();i++){
					if(next.charAt(i)=='['){
						bracketCount++;
					}else if(next.charAt(i)==']'){
						bracketCount--;
					}
					if(bracketCount<=0&&i!=next.length()-1){
						return new Value(list+" is not a list!"); 
					}
				}
				list+=" "+next;
				if(bracketCount==0){
					if(!List.isList(list)){
						return new Value(list+" is not a list!");
					}
					List l=new List();
					l.readList(list);
					return l;
				}
			}
		}else{
			while(!inputQueue.isEmpty()){
				String next=getNext();
				for(int i=0;i<next.length();i++){
					if(next.charAt(i)=='['){
						bracketCount++;
					}else if(next.charAt(i)==']'){
						bracketCount--;
					}
					if(bracketCount<=0&&i!=next.length()-1){
						return new Value(list+" is not a list!"); 
					}
				}
				list+=" "+next;
				if(bracketCount==0){
					if(!List.isList(list)){
						return new Value(list+" is not a list!");
					}
					List l=new List();
					l.readList(list);
					return l;
				}
			}
		}
		return new Value(list+" is not a list!");
	}
	private Value getExpression(String s,Boolean SystemIn){
		int bracketCount=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='('){
				bracketCount++;
			}else if(s.charAt(i)==')'){
				bracketCount--;
			}
			if(bracketCount<=0&&i!=s.length()-1){
				return new Value(s+" is not an expression!"); 
			}
		}
		if(bracketCount==0){
			if(!Expression.isExExpression(s)){
				return new Value(s+" is not an expression!");
			}
			return new Expression(s);
		}
		String exp=s;
		if(SystemIn){
			while(scan.hasNext()){
				String next=getNext();
				for(int i=0;i<next.length();i++){
					if(next.charAt(i)=='('){
						bracketCount++;
					}else if(next.charAt(i)==')'){
						bracketCount--;
					}
					if(bracketCount<=0&&i!=next.length()-1){
						return new Value(exp+" is not an expression!"); 
					}
				}
				exp+=" "+next;
				if(bracketCount==0){
					if(!Expression.isExExpression(exp)){
						return new Value(exp+" is not an expression!");
					}
					return new Expression(exp);
				}
			}
		}else{
			while(!inputQueue.isEmpty()){
				String next=getNext();
				for(int i=0;i<next.length();i++){
					if(next.charAt(i)=='('){
						bracketCount++;
					}else if(next.charAt(i)==')'){
						bracketCount--;
					}
					if(bracketCount<=0&&i!=next.length()-1){
						return new Value(exp+" is not an expression!"); 
					}
				}
				exp=" "+next;
				if(bracketCount==0){
					if(!Expression.isExExpression(exp)){
						return new Value(exp+" is not an expression!");
					}
					return new Expression(exp);
				}
			}
		}
		return new Value(exp+" is not an expresssion!");
	}
	private Value getWord(){
		String next=getNext();
		if(Word.isWord(next)){
			return new Word(next.substring(1));
		}else if(Word.isOperator(next)||next.startsWith(":")){
			return getOp(next);
		}else{
			Word w=new Word(next);
			Value glo=foreignName.getValueOf(w);
			if(glo.getClass()==new List().getClass()){
				if(Function.isFunctionBody((List)glo)){
					return function(next,true);
				}
			}
			Value loc=localName.getValueOf(w);
			if(loc.getClass()==new List().getClass()){
				if(Function.isFunctionBody((List)loc)){
					return function(next,false);
				}
			}
		}
		return new Value(next+" is not a word!");
	}
	private Value getNumber(){
		Boolean si=false;
		if(inputQueue.isEmpty()){
			si=true;
		}
		String next=getNext();
		if(Number.isNumber(next)){
			return new Number(next);
		}else if(next.startsWith("(")){
			Value v=getExpression(next,si);
			if(v.getClass()!=new Expression().getClass()){
				return new Value(v.value);
			}
			Expression exp=(Expression)v;
			exp.calculate(NameMap.combine(foreignName, localName));
			return exp.result;
		}else if(Word.isOperator(next)||next.startsWith(":")){
			return getOp(next);
		}else{
			Word w=new Word(next);
			Value glo=foreignName.getValueOf(w);
			if(glo.getClass()==new List().getClass()){
				if(Function.isFunctionBody((List)glo)){
					return function(next,true);
				}
			}
			Value loc=localName.getValueOf(w);
			if(loc.getClass()==new List().getClass()){
				if(Function.isFunctionBody((List)loc)){
					return function(next,false);
				}
			}
		}
		return new Value(next+" is not a number!");
	}
	private Value getBool(){
		String next=getNext();
		if(Bool.isBool(next)){
			return new Bool(next);
		}else if(Word.isOperator(next)||next.startsWith(":")){
			return getOp(next);
		}else{
			Word w=new Word(next);
			Value glo=foreignName.getValueOf(w);
			if(glo.getClass()==new List().getClass()){
				if(Function.isFunctionBody((List)glo)){
					return function(next,true);
				}
			}
			Value loc=localName.getValueOf(w);
			if(loc.getClass()==new List().getClass()){
				if(Function.isFunctionBody((List)loc)){
					return function(next,false);
				}
			}
		}
		return new Value(next+" is not a bool!");
	}
	
}
