package sdong.doxygen.bean;

import java.io.Serializable;

public class DoxygenNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8852061029482388036L;
	private String id;
	private String label;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
