package introsde.rest.ehealth.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="measureTypes")
public class MeasureTypeXML implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "measureType")
	List<String> measureType = new ArrayList<String>();
	
	public void add(String x){
		this.measureType.add(x);
	}
	
	public MeasureTypeXML(){
		
	}
	
	public List<String> get(){
		return measureType;
	}
	
	public void set(List<String> measureTypes){
		this.measureType = measureTypes;
	}
}