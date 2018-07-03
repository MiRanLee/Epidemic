package com.project.bJang.webapp.action.chart;

import com.project.bJang.webapp.action.BJangForm;

public class ChartForm extends BJangForm {
	private String siCd;
	private String sigunguCd;
	private String sigunguNm;

	private String epidemicId;
	private String epidemicNm;

	private int year;
	private int month;
	
	private String lng;

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getSigunguCd() {
		return sigunguCd;
	}

	public void setSigunguCd(String sigunguCd) {
		this.sigunguCd = sigunguCd;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getEpidemicId() {
		return epidemicId;
	}

	public void setEpidemicId(String epidemicId) {
		this.epidemicId = epidemicId;
	}

	public String getSigunguNm() {
		return sigunguNm;
	}

	public void setSigunguNm(String sigunguNm) {
		this.sigunguNm = sigunguNm;
	}

	public String getEpidemicNm() {
		return epidemicNm;
	}

	public void setEpidemicNm(String epidemicNm) {
		this.epidemicNm = epidemicNm;
	}

	public String getSiCd() {
		return siCd;
	}

	public void setSiCd(String siCd) {
		this.siCd = siCd;
	}

}
