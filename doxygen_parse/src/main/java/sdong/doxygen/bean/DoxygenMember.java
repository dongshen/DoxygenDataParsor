package sdong.doxygen.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import sdong.common.utils.StringUtil;

public class DoxygenMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -63345280579745357L;
	
	public static final String MEMBER_KIND_DEFINE = "define";
	public static final String MEMBER_KIND_FUNCTION = "function";
	public static final String MEMBER_KIND_VARIABLE = "variable";
	
	private String refid;
	private String kind;
	private String name;

	private String port;
	private boolean isStatic;
	private boolean isMutable;

	private String type;
	private String definition;
	private String argsstring;

	private boolean isConst;
	private boolean isExplicit;
	private boolean isInline;
	private String virt;

	private List<DoxygenParam> params = new ArrayList<DoxygenParam>();

	private DoxygenLocation location = new DoxygenLocation();

	private ConcurrentHashMap<String, DoxygenReference> reference = new ConcurrentHashMap<String, DoxygenReference>();

	private ConcurrentHashMap<String, DoxygenReference> referencedby = new ConcurrentHashMap<String, DoxygenReference>();

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

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;

	}

	public void setStatic(String isStatic) {
		setStatic(StringUtil.convertStringToBoolean(isStatic));

	}

	public boolean isMutable() {
		return isMutable;
	}

	public void setMutable(boolean isMutable) {
		this.isMutable = isMutable;
	}

	public void setMutable(String isMutable) {
		setMutable(StringUtil.convertStringToBoolean(isMutable));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getArgsstring() {
		return argsstring;
	}

	public void setArgsstring(String argsstring) {
		this.argsstring = argsstring;
	}

	public boolean isConst() {
		return isConst;
	}

	public void setConst(boolean isConst) {
		this.isConst = isConst;
	}

	public void setConst(String isConst) {
		setConst(StringUtil.convertStringToBoolean(isConst));
	}

	public boolean isExplicit() {
		return isExplicit;
	}

	public void setExplicit(boolean isExplicit) {
		this.isExplicit = isExplicit;
	}

	public void setExplicit(String isExplicit) {
		setExplicit(StringUtil.convertStringToBoolean(isExplicit));
	}

	public boolean isInline() {
		return isInline;
	}

	public void setInline(boolean isInline) {
		this.isInline = isInline;
	}

	public void setInline(String isInline) {
		setInline(StringUtil.convertStringToBoolean(isInline));
	}

	public String getVirt() {
		return virt;
	}

	public void setVirt(String virt) {
		this.virt = virt;
	}

	public DoxygenLocation getLocation() {
		return location;
	}

	public void setLocation(DoxygenLocation location) {
		this.location = location;
	}

	public ConcurrentHashMap<String, DoxygenReference> getReference() {
		return reference;
	}

	public void setReference(ConcurrentHashMap<String, DoxygenReference> reference) {
		this.reference = reference;
	}

	public ConcurrentHashMap<String, DoxygenReference> getReferencedby() {
		return referencedby;
	}

	public void setReferencedby(ConcurrentHashMap<String, DoxygenReference> referencedby) {
		this.referencedby = referencedby;
	}

	public List<DoxygenParam> getParams() {
		return params;
	}

	public void setParams(List<DoxygenParam> params) {
		this.params = params;
	}

}
