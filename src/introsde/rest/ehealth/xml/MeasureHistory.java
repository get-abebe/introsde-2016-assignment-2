package introsde.rest.ehealth.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "measure")
@XmlType(propOrder = { "mid", "value", "created" })
@XmlAccessorType(XmlAccessType.FIELD)
public class MeasureHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "mid")
	private Integer mid;
	
	@XmlElement(name = "value")
	private Double value;
	
	@XmlElement(name = "created")
	private String created;

	public MeasureHistory() {

	}

	public MeasureHistory(Integer a, Double b, String c) {
		setmid(a);
		setvalue(b);
		setcreated(c);
	}

	public int getmid() {
		return mid;
	}

	public void setmid(Integer mid) {
		this.mid = mid;
	}

	public Double getvalue() {
		return value;
	}

	public void setvalue(Double value) {
		this.value = value;
	}

	public String getcreated() {
		return created;
	}

	public void setcreated(String created) {
		this.created = created;
	}


}
