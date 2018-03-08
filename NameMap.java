import java.util.HashMap;
import java.util.Map;
import java.util.Set;
public class NameMap {
	public HashMap<String,Value> stringMap;
	public NameMap(){
		stringMap=new HashMap<String,Value>();
	}
	public void bindValue(Word w,Value v){
		stringMap.put(w.value, v);
	}
	public void debindValue(Word w){
		if(stringMap.containsKey(w.value)){
			Value v=stringMap.get(w.value);
			stringMap.remove(w.value, v);	
		}
	}
	public void debindAll(){
		stringMap.clear();
	}
	public Value getValueOf(Word w){
		if(stringMap.containsKey(w.value)){
			return stringMap.get(w.value);	
		}else{
			return new Value("Can not get value from the word \""+w.value+" which is not a name!");
		}
	}
	public Boolean isName(Word w){
		return stringMap.containsKey(w.value);
	}
	public String[] getAllKeys(){
		Set<String> ss=stringMap.keySet();
		return ss.toArray(new String[0]);
	}
	public Value[] getAllValues(){
		String[] keys=stringMap.keySet().toArray(new String[0]);
		Value[] values=new Value[keys.length];
		for(int i=0;i<keys.length;i++){
			values[i]=stringMap.get(keys[i]);
		}
		return values;
	}
	public int nameCount(){
		return stringMap.size();
	}
	public static NameMap combine(NameMap n1,NameMap n2){
		NameMap n=new NameMap();
		n.stringMap.putAll((Map<String,Value>)n1.stringMap);
		n.stringMap.putAll((Map<String,Value>)n2.stringMap);
		return n;
	}
}
