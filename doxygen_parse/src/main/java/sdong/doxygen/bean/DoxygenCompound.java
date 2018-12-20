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

	private ConcurrentHashMap<String, DoxygenMember> membersDefine = new ConcurrentHashMap<String, DoxygenMember>();
	private ConcurrentHashMap<String, DoxygenMember> membersVariable = new ConcurrentHashMap<String, DoxygenMember>();
	private ConcurrentHashMap<String, DoxygenMember> membersFunction = new ConcurrentHashMap<String, DoxygenMember>();

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

	public ConcurrentHashMap<String, DoxygenMember> getMembersDefine() {
		return membersDefine;
	}

	public void setMembersDefine(ConcurrentHashMap<String, DoxygenMember> membersDefine) {
		this.membersDefine = membersDefine;
	}

	public ConcurrentHashMap<String, DoxygenMember> getMembersVariable() {
		return membersVariable;
	}

	public void setMembersVariable(ConcurrentHashMap<String, DoxygenMember> membersVariable) {
		this.membersVariable = membersVariable;
	}

	public ConcurrentHashMap<String, DoxygenMember> getMembersFunction() {
		return membersFunction;
	}

	public void setMembersFunction(ConcurrentHashMap<String, DoxygenMember> membersFunction) {
		this.membersFunction = membersFunction;
	}

	public ConcurrentHashMap<String, DoxygenMember> getMembers(String memberKind) {
		if (memberKind == null) {
			return null;
		}

		ConcurrentHashMap<String, DoxygenMember> members;
		if (DoxygenMember.MEMBER_KIND_FUNCTION.equals(memberKind)) {
			members = getMembersFunction();
		} else if (DoxygenMember.MEMBER_KIND_VARIABLE.equals(memberKind)) {
			members = getMembersVariable();
		} else if (DoxygenMember.MEMBER_KIND_DEFINE.equals(memberKind)) {
			members = getMembersDefine();
		} else {
			return null;
		}

		return members;
	}

}
