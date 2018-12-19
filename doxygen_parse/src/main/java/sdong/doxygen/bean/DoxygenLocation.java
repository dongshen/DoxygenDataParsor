package sdong.doxygen.bean;

import java.io.Serializable;

import sdong.common.utils.StringUtil;

public class DoxygenLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5716727843523764711L;
	private String file;
	private int line;
	private int column;

	private String bodyfile;
	private int bodystart;
	private int bodyend;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public void setLine(String line) {
		setLine(StringUtil.convertStringToInt(line));
	}

	public int getColumn() {
		return column;
	}

	void setColumn(int column) {
		this.column = column;
	}

	public void setColumn(String column) {
		setColumn(StringUtil.convertStringToInt(column));
	}

	public String getBodyfile() {
		return bodyfile;
	}

	public void setBodyfile(String bodyfile) {
		this.bodyfile = bodyfile;
	}

	public int getBodystart() {
		return bodystart;
	}

	public void setBodystart(int bodystart) {
		this.bodystart = bodystart;
	}

	public void setBodystart(String bodystart) {
		setBodystart(StringUtil.convertStringToInt(bodystart));
	}

	public int getBodyend() {
		return bodyend;
	}

	public void setBodyend(int bodyend) {
		this.bodyend = bodyend;
	}

	public void setBodyend(String bodyend) {
		setBodyend(StringUtil.convertStringToInt(bodyend));
	}

}
