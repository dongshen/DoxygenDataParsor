package sdong.doxygen.bean;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class DoxygenCompound implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3671609518458493432L;

	private String refid;
	private String kind;
	private String name;

	private String language;

	private ConcurrentHashMap<String, DoxygenMember> members = new ConcurrentHashMap<String, DoxygenMember>();

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConcurrentHashMap<String, DoxygenMember> getMembers() {
		return members;
	}

	public void setMembers(ConcurrentHashMap<String, DoxygenMember> members) {
		this.members = members;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
