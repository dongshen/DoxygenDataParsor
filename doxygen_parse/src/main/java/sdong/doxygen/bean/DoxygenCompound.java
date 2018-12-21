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

	private ConcurrentHashMap<String, ConcurrentHashMap<String, DoxygenMember>> members = new ConcurrentHashMap<String, ConcurrentHashMap<String, DoxygenMember>>();

	private DoxygenLocation location = new DoxygenLocation();

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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public DoxygenLocation getLocation() {
		return location;
	}

	public void setLocation(DoxygenLocation location) {
		this.location = location;
	}

	public ConcurrentHashMap<String, DoxygenMember> getMembers(String memberKind) {
		if (memberKind == null) {
			return null;
		}

		ConcurrentHashMap<String, DoxygenMember> member = members.get(memberKind);
		if (member == null) {
			member = new ConcurrentHashMap<String, DoxygenMember>();
			members.put(memberKind, member);
		}

		return member;
	}

}
