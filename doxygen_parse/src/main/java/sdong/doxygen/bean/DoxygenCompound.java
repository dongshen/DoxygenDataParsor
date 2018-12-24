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

	private ConcurrentHashMap<String, DoxygenIncType> includes = new ConcurrentHashMap<String, DoxygenIncType>();
	private ConcurrentHashMap<String, DoxygenIncType> includedby = new ConcurrentHashMap<String, DoxygenIncType>();

	private ConcurrentHashMap<String, DoxygenNode> incdepgraph = new ConcurrentHashMap<String, DoxygenNode>();

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

	public ConcurrentHashMap<String, ConcurrentHashMap<String, DoxygenMember>> getMembers() {
		return members;
	}

	public void setMembers(ConcurrentHashMap<String, ConcurrentHashMap<String, DoxygenMember>> members) {
		this.members = members;
	}

	public ConcurrentHashMap<String, DoxygenIncType> getIncludes() {
		return includes;
	}

	public void setIncludes(ConcurrentHashMap<String, DoxygenIncType> includes) {
		this.includes = includes;
	}

	public ConcurrentHashMap<String, DoxygenIncType> getIncludedby() {
		return includedby;
	}

	public void setIncludedby(ConcurrentHashMap<String, DoxygenIncType> includedby) {
		this.includedby = includedby;
	}

	public ConcurrentHashMap<String, DoxygenNode> getIncdepgraph() {
		return incdepgraph;
	}

	public void setIncdepgraph(ConcurrentHashMap<String, DoxygenNode> incdepgraph) {
		this.incdepgraph = incdepgraph;
	}

}
