package sdong.doxygen.bean;

import java.io.Serializable;

import sdong.common.utils.StringUtil;

public class DoxygenIncType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1178550011766185363L;
	private String refid;

	private boolean isLocal;
	private String name;

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	
	public void setLocal(String isLocal) {
		setLocal(StringUtil.convertStringToBoolean(isLocal));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
