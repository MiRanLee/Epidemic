package com.project.bJang.webapp.action;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.project.bJang.env.BJangContext;
import com.project.bJang.env.KeywordManager;
import com.project.bJang.env.ServiceManager;
import com.project.bJang.env.UserEnv;
import com.project.bJang.env.UserSessionBean;
import com.project.bJang.webapp.Constants;
import net.ion.framework.db.Row;
import net.ion.framework.db.Rows;
import net.ion.framework.db.Transformer;
import net.ion.framework.db.bean.RowsHandler;

public class BJangController implements Constants {

protected JSONObject makeChartJSONObject(Rows rows, JSONObject result) throws SQLException {
    return makeChartJSONObject(rows, result, "epidemicnm", 20);
  }

  protected JSONObject makeChartJSONObject(Rows rows, JSONObject result, String labelName,int labelSize) throws SQLException {
	  JSONArray labels = new JSONArray();
	  JSONArray data = new JSONArray();
	    while (rows.next()) {
	      String label = rows.getString(labelName);	      
	      labels.put(label.length() > labelSize ? label.substring(0, labelSize) : label);
	      data.put(rows.getInt("total"));
	    }
	    result.put("labels", labels).put("data", data);
	    return result;
  }
																																																		  
  protected JSONObject makeChartJSONObject(Rows rows, JSONObject result, String labelName, int labelSize, String lng) throws SQLException {
    JSONArray labels = new JSONArray();
    JSONArray data = new JSONArray();
    while (rows.next()) {
      String label = rows.getString(labelName);
      if(label.equals("0~9技")) {
    	  label = "0~9";
      }else if (label.equals("10~19技")) {
    	  label = "10~19";
      }else if (label.equals("20~29技")) {
    	  label = "20~29";
      }else if (label.equals("30~39技")) {
    	  label = "30~39";
      }else if (label.equals("40~49技")) {
    	  label = "40~49";
      }else if (label.equals("50~59技")) {
    	  label = "50~59";
      }else if (label.equals("60~69技")) {
    	  label = "60~69";
      }else if (label.equals("70技 捞惑")) {
    	  label = "over 70";
      }else {
    	  label = "total";
      }
      labels.put(label.length() > labelSize ? label.substring(0, labelSize) : label);
      data.put(rows.getInt("total"));
    }
    result.put("labels", labels).put("data", data);
    return result;
  }


  protected JSONObject makeGenderChartJSONObject(Rows rows, JSONObject result, int labelSize, String lng) throws SQLException {
    JSONArray labels = new JSONArray();
    JSONArray data = new JSONArray();
    String label = "";
    if(lng.equals("eng")) {
	    label = "man";
	    labels.put(label.length() > labelSize ? label.substring(0, labelSize) : label);
	    label = "woman";
	    labels.put(label.length() > labelSize ? label.substring(0, labelSize) : label);
    }else {
    	label = "巢己";
    	labels.put(label.length() > labelSize ? label.substring(0, labelSize) : label);
    	label = "咯己";
    	labels.put(label.length() > labelSize ? label.substring(0, labelSize) : label);
    }
    while (rows.next()) {
      data.put(rows.getInt("mantotal"));
      data.put(rows.getInt("womantotal"));
    }
    result.put("labels", labels).put("data", data);
    return result;
  }

  protected JSONObject makeCompareChartJSONObject(Rows rows, JSONObject result, String labelName, int labelSize) throws SQLException {
    JSONArray labels = new JSONArray();
    JSONArray data = new JSONArray();
    JSONArray data2 = new JSONArray();
    JSONArray data3 = new JSONArray();
    int count = 1;
    while (rows.next()) {
      if (count < 13) {
        String label = rows.getString(labelName);
        labels.put(label.length() > labelSize ? label.substring(0, labelSize) : label);
        data.put(rows.getInt("total"));
      } else if (12 < count && count < 25) {
        data2.put(rows.getInt("total"));
      } else {
        data3.put(rows.getInt("total"));
      }
      count++;
    }
    result.put("labels", labels).put("data", data).put("data2", data2).put("data3", data3);
    return result;
  }

}