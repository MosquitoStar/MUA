
public class Function {
	public static Boolean isFunctionBody(List list){
		if(list.elementCount()==2){
			if(list.getElement(0).getClass()==new List().getClass()){
				List para=(List)list.getElement(0);
				for(int i=0;i<para.elementCount();i++){
					if(!Word.isWord("\""+para.getElement(i).value)){
						return false;
					}
				}
				if(list.getElement(1).getClass()==new List().getClass()){
					return true;
				}
			}
		}
		return false;
	}
}
