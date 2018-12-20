package sdong.doxygen.bean;

import java.io.Serializable;

public class DoxygenParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8392803487682516014L;
	private String type;
	private String declname;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeclname() {
		return declname;
	}

	public void setDeclname(String declname) {
		this.declname = declname;
	}

}
