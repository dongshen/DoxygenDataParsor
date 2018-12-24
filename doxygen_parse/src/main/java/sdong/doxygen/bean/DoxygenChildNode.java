package sdong.doxygen.bean;

import java.io.Serializable;

public class DoxygenChildNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7863467883886039694L;
	private String refid;
	private String relation;

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

}
