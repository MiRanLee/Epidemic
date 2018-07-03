package com.project.bJang.webapp.action.chart;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.project.bJang.webapp.action.BJangController;
import com.project.bJang.env.ServiceManager;
import net.ion.framework.db.Rows;
import net.ion.framework.util.CalendarUtil;
import net.ion.framework.util.StringUtil;

@Controller
public class ChartController extends BJangController {
//Top 5 districts with the most infected people (regional)
  @RequestMapping(value = "/chart/epidemic/region")
  @ResponseBody
  public JSONObject chartRegion(HttpServletRequest request, @ModelAttribute ChartForm form, ModelMap model) throws SQLException, IOException {
	Rows rows = null;
    int year = (form.getYear() == 0 ? Calendar.getInstance().get(Calendar.YEAR) : form.getYear());
    int month = (form.getMonth() == 0 ? Calendar.getInstance().get(Calendar.MONTH) : form.getMonth());
    String epidemicNm = form.getEpidemicNm();
    String siCd = (StringUtil.isBlank(form.getSiCd()) ? "" : form.getSiCd());
    String lng = form.getLng();
    JSONObject result = new JSONObject().put("text", year + "년 " + month + "월 " + epidemicNm + " 발생이 높은 지역 Top5").put("type", "bar");
    if(lng.equals("kor")) {
	    rows = getServiceManager().getEpidemicCarrier().listByMonthlyRegion(form.getEpidemicId(), siCd, year, month, 5);
    }else {
    	rows = getServiceManager().getEpidemicCarrier().listByMonthlyRegionEng(form.getEpidemicId(), siCd, year, month, 5);
    }
    makeChartJSONObject(rows, result, "address", 25);
    return result;
  }
  
  //Top 5 districts with the most infected people (nationwide)
  @RequestMapping(value = "/chart/epidemic/danger")
  @ResponseBody
  public JSONObject chartDanger(HttpServletRequest request, @ModelAttribute ChartForm form, ModelMap model) throws SQLException, IOException {
	Rows rows = null;
    int year = (form.getYear() == 0 ? Calendar.getInstance().get(Calendar.YEAR) : form.getYear());
    int month = (form.getMonth() == 0 ? Calendar.getInstance().get(Calendar.MONTH) : form.getMonth());
    String lng = form.getLng();
    JSONObject result = new JSONObject().put("text", year + "년 " + month + "월 감염위험도 높은 지역 Top5").put("type", "bar");
    rows = getServiceManager().getEpidemicCarrier().listByMonthlyDanger(year, month, 5);
    makeChartJSONObject(rows, result, "address", 25);
    return result;
  }
  
  //Number of infected people by year
  @RequestMapping(value = "/chart/epidemic/yearly")
  @ResponseBody
  public JSONObject chartYearly(HttpServletRequest request, @ModelAttribute ChartForm form, ModelMap model) throws SQLException, IOException {
    String epidemicNm = form.getEpidemicNm();
    JSONObject result = new JSONObject().put("text", epidemicNm + " 연도별 발생률").put("type", "bar");
    Rows rows = getServiceManager().getEpidemicCarrier().listByYearly(form.getEpidemicId());
    makeChartJSONObject(rows, result, "year", 10);
    return result;
  }
  
  //Infection trend during recent 6 months
  @RequestMapping(value = "/chart/epidemic/trend")
  @ResponseBody
  public JSONObject chartTrend(HttpServletRequest request, @ModelAttribute ChartForm form, ModelMap model) throws SQLException, IOException, ParseException {
    String year = String.valueOf((form.getYear() == 0 ? Calendar.getInstance().get(Calendar.YEAR) : form.getYear()));
    String month = StringUtil.leftPad(String.valueOf((form.getMonth() == 0 ? Calendar.getInstance().get(Calendar.MONTH) : form.getMonth())), 2, '0');
    String epidemicNm = form.getEpidemicNm();
    JSONObject result = new JSONObject().put("text", epidemicNm + " 6개월간 감염 추이").put("type", "line");
    Calendar cal = CalendarUtil.makeCalendar(year + month + "01");
    cal.add(Calendar.MONTH, -5);
    String startDate = CalendarUtil.format(cal, "yyyyMM");
    String endDate = year + month;
    Rows rows = getServiceManager().getEpidemicCarrier().listByMonthlyTrend(form.getEpidemicId(), form.getSiCd(), startDate, endDate);
    makeChartJSONObject(rows, result, "targetDate", 10);
    return result;
  }
  
  //Comparison of number of infected people during recent 3 years
  @RequestMapping(value = "/chart/epidemic/compare")
  @ResponseBody
  public JSONObject chartCompare(HttpServletRequest request, @ModelAttribute ChartForm form, ModelMap model) throws SQLException, IOException {
    int year = (form.getYear() == 0 ? Calendar.getInstance().get(Calendar.YEAR) : form.getYear());
    String epidemicNm = form.getEpidemicNm();
    JSONObject result = new JSONObject().put("text", epidemicNm + "3개년 감염자 통계 비교").put("type", "line");
    Rows rows = getServiceManager().getEpidemicCarrier().listByCompare2(year, form.getEpidemicId());
    makeCompareChartJSONObject(rows, result, "month", 10);
    return result;
  }
  
  //Top 5 most common infectious diseases (nationwide)
  @RequestMapping(value = "/chart/epidemic/whole")
  @ResponseBody
  public JSONObject wholeChartPage(HttpServletRequest request, @ModelAttribute ChartForm form, ModelMap model) throws SQLException, IOException {
	Rows rows = null;
    int year = (form.getYear() == 0 ? Calendar.getInstance().get(Calendar.YEAR) : form.getYear());
    int month = (form.getMonth() == 0 ? Calendar.getInstance().get(Calendar.MONTH) : form.getMonth());
    String lng = form.getLng();
    JSONObject result = new JSONObject().put("text", year + "년 " + month + "월 전국 질병 Top5").put("type", "bar");
    if(lng.equals("kor")) {
    	rows = getServiceManager().getEpidemicCarrier().listByWhole(year, month, 5);
    }
    else {
    	rows = getServiceManager().getEpidemicCarrier().listByWholeEng(year, month, 5);
    }
    makeChartJSONObject(rows, result);
    return result;
  }

 //Number of infected people by gender 
  @RequestMapping(value = "/chart/epidemic/gender")
  @ResponseBody
  public JSONObject chartGender(HttpServletRequest request, @ModelAttribute ChartForm form, ModelMap model) throws SQLException, IOException {
    String epidemicNm = form.getEpidemicNm();
    int year = (form.getYear() == 0 ? Calendar.getInstance().get(Calendar.YEAR) : form.getYear());
    int month = (form.getMonth() == 0 ? Calendar.getInstance().get(Calendar.MONTH) : form.getMonth());
    String lng = form.getLng();
    JSONObject result = new JSONObject().put("text", epidemicNm + " 성별 발생률").put("type", "pie");
    Rows rows = getServiceManager().getEpidemicCarrier().listByGender(form.getEpidemicId(), form.getSiCd(), year, month);
    makeGenderChartJSONObject(rows, result, 10, lng);
    return result;
  }

  //Number of infected people by age
  @RequestMapping(value = "/chart/epidemic/age")
  @ResponseBody
  public JSONObject chartAge(HttpServletRequest request, @ModelAttribute ChartForm form, ModelMap model) throws SQLException, IOException {
    String epidemicNm = form.getEpidemicNm();
    int year = (form.getYear() == 0 ? Calendar.getInstance().get(Calendar.YEAR) : form.getYear());
    int month = (form.getMonth() == 0 ? Calendar.getInstance().get(Calendar.MONTH) : form.getMonth());
    String lng = form.getLng();
    JSONObject result = new JSONObject().put("text", epidemicNm + " 연령별 발생률").put("type", "bar");
    Rows rows = getServiceManager().getEpidemicCarrier().listByAge(form.getEpidemicId(), form.getSiCd(), year, month);
    if(lng.equals("kor")) {
    	makeChartJSONObject(rows, result, "age", 10);	
    }else {
    	makeChartJSONObject(rows, result, "age", 10, "eng");
  	}
    return result;
  }

}