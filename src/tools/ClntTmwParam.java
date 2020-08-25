package tools;

import java.io.Serializable;

public class ClntTmwParam implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String key;
	private Object value;
	
	public String getKey(){
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public Object getValue(){
		return this.value;
	}
	public void setValue(Object value){
		this.value = value;
	}
	
}