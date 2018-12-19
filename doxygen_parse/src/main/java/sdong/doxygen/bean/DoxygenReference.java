package sdong.doxygen.bean;

import java.io.Serializable;

import sdong.common.utils.StringUtil;

public class DoxygenReference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 729274993797754428L;
	private String refid;
	private String compoundref;
	private String name;
	private int startline;
	private int endline;

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public String getCompoundref() {
		return compoundref;
	}

	public void setCompoundref(String compoundref) {
		this.compoundref = compoundref;
	}

	public int getStartline() {
		return startline;
	}

	public void setStartline(int startline) {
		this.startline = startline;
	}

	public void setStartline(String startline) {
		setStartline(StringUtil.convertStringToInt(startline));
	}

	public int getEndline() {
		return endline;
	}

	public void setEndline(int endline) {
		this.endline = endline;
	}

	public void setEndline(String endline) {
		setEndline(StringUtil.convertStringToInt(endline));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
