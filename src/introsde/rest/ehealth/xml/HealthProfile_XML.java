package introsde.rest.ehealth.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name="healthprofile")
@XmlType(propOrder = { "weight", "height", "steps", "blood_pressure", "heart_rate" })
@XmlAccessorType(XmlAccessType.FIELD)

public class HealthProfile_XML implements Serializable {
	
	private static final long serialVersionUID = 1L;


	private Double weight; 
	
	private Double height; 
	
	private Integer steps;
	
	private Double blood_pressure;
	
	private Integer heart_rate;
	
		
	public HealthProfile_XML() {

	}
	

	public Integer getsteps(){
		return steps;
	}
	
	public void setstepts(Integer steps){
		this.steps = steps;
	}
	
	
	public Double getblood_pressure(){
		return blood_pressure;
	}
	
	public void setblood_pressure(Double blood_pressure){
		this.blood_pressure = blood_pressure;
	}
	
	
	public Integer getheart_rate(){
		return heart_rate;
	}
	
	public void setheart_rate(Integer heart_rate){
		this.heart_rate = heart_rate;
	}
	
		
	public Double getweight() {
		return weight;
	}

	public void setweight(Double _weight) {
		this.weight = _weight;
	}

	public Double getheight() {
		return height;
	}

	public void setHeight(Double _height) {
		this.height = _height;
	}

}
