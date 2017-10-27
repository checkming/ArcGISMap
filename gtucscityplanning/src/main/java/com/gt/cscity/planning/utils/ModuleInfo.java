package com.gt.cscity.planning.utils;

import java.io.Serializable;

public class ModuleInfo implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String menuid;
	private String menuname;//模块名称
	private String menucode;//模块代码
	private String menuico;//部件图标
	private String unitname;//部件名称
	private String parentmoduleid;
	private int moduleposition;
	private String datatype;
	private String servicetype;
	public int getModuleposition() {
		return moduleposition;
	}
	public void setModuleposition(int moduleposition) {
		this.moduleposition = moduleposition;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	public String getParentmoduleid() {
		return parentmoduleid;
	}
	public void setParentmoduleid(String parentmoduleid) {
		this.parentmoduleid = parentmoduleid;
	}
	private boolean hasconditon;
	public boolean isHasconditon() {
		return hasconditon;
	}
	public void setHasconditon(boolean hasconditon) {
		this.hasconditon = hasconditon;
	}
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public String getMenuname() {
		return menuname;
	}
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
	public String getMenucode() {
		return menucode;
	}
	public void setMenucode(String menucode) {
		this.menucode = menucode;
	}
	public String getMenuico() {
		return menuico;
	}
	public void setMenuico(String menuico) {
		this.menuico = menuico;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
