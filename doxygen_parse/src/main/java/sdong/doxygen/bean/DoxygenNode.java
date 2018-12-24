package sdong.doxygen.bean;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class DoxygenNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8852061029482388036L;
	private String id;
	private String refid;
	private String label;

	private ConcurrentHashMap<String, DoxygenChildNode> childNodes = new ConcurrentHashMap<String, DoxygenChildNode>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ConcurrentHashMap<String, DoxygenChildNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(ConcurrentHashMap<String, DoxygenChildNode> childNodes) {
		this.childNodes = childNodes;
	}

}
