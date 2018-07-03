package com.project.bJang.unit.carrier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.project.bJang.env.ServiceManager;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.Row;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.util.Debug;

public class EpidemicCarrier extends Carrier {
  private final static String PACKAGE_NAME = "EPIDEMIC";

  public EpidemicCarrier(IDBController dc) {
    super(dc);
  }

  
  //load CSV files (KCDC)
  public void createWithEXCEL(ServiceManager serviceManager) throws IOException, SQLException {
    File epidemicFolder = new File("./bJang/WEB-INF/excel/epidemic/", "");
    if (epidemicFolder.exists()) {
      for (File file : epidemicFolder.listFiles()) {
        loadEpidemicByEXCEL(file, serviceManager);
        Debug.line(file.getName() + " SUCCESS");
      }
      Debug.line("===== 질병현황 전체완료 =====");
    }
  }

  public void createWithEXCEL2(ServiceManager serviceManager) throws IOException, SQLException {
    File epidemicFolder = new File("./bJang/WEB-INF/excel/epidemic/", "");
    if (epidemicFolder.exists()) {
      for (File file : epidemicFolder.listFiles()) {
        loadEpidemicByEXCEL2(file, serviceManager);
        Debug.line(file.getName() + " SUCCESS");
      }
      Debug.line("===== 질병현황 전체완료 =====");
    }
  }

  //load raw data from CSV files (infectious disease outbreak information : district, gender, age....) 
  public void loadEpidemicByEXCEL(File file, ServiceManager serviceManager) throws IOException, SQLException {
    FileInputStream fis = new FileInputStream(file);
    @SuppressWarnings("resource")

    HSSFWorkbook workbook = new HSSFWorkbook(fis);
    int sheetNum = workbook.getNumberOfSheets();
    int rowIndex = 0;

    String epidemicNm = " ";
    String epidemicCd = " ";
    String addressCd = " ";
    String addressNm = " ";
    int count, cn, startNum = 0;

    IUserProcedures upts = createUserProcedures("createWith");
    for (cn = 0; cn < sheetNum; cn++) {

      HSSFSheet sheet = workbook.getSheetAt(cn);
      int rows = sheet.getPhysicalNumberOfRows();
      int searchNum = 3;
      String siNm = FilenameUtils.removeExtension(file.getName()).split("_")[1];

      HSSFRow row1 = sheet.getRow(searchNum);
      HSSFRow row2 = sheet.getRow(searchNum);
      HSSFRow row3 = sheet.getRow(searchNum);

      String siCd = getAddressIdBySiNm(siNm);

      Map<String, String> sigunguMap = serviceManager.getAddressMap().get(siCd);
      Map<String, String> epidemicMap = serviceManager.getEpidemicMap();
      row1 = sheet.getRow(5);
      startNum = 8;

      for (searchNum = startNum; searchNum < rows - 13; searchNum++) {
        for (rowIndex = 3; rowIndex < 61; rowIndex++) {
          row1 = sheet.getRow(5);
          epidemicNm = String.valueOf(row1.getCell(rowIndex));
          if (!(epidemicNm.equals(""))) {
            epidemicCd = epidemicMap.get(epidemicNm);
            if (epidemicCd != null) {
              row2 = sheet.getRow(searchNum);
              if(row2!=null) {
	              addressNm = String.valueOf(row2.getCell(2));
	              if(!(addressNm.equals(""))){
	            	  addressNm = addressNm.substring(0, addressNm.length() - 1);
	            	  addressCd = sigunguMap.get(addressNm);
	              }
	              if (addressNm.equals("세종시")) {
	                addressCd = sigunguMap.get("세종특별자치시");
	              }
	              if (addressCd != null) {
	                String countString = String.valueOf(row2.getCell(rowIndex));
	                try {
	                  count = Integer.parseInt(String.valueOf(Math.round(Double.valueOf(countString))));
	                
	                  if (count > 0) {
	                    row3 = sheet.getRow(2);
	                    String sdate = String.valueOf(row3.getCell(3));
	                    int year = Integer.valueOf(sdate.substring(0, 4));
	                    int month = Integer.valueOf(sdate.substring(5, 7));
	
	                    Debug.debug(epidemicCd, addressNm, addressCd, count, year, month);
	                    upts.add(makeProcedure(PACKAGE_NAME + "@createWith", epidemicCd, addressCd, count, year, month));
	                  }
	                }
	                catch(NumberFormatException e) {
	                	Debug.debug("NumberFormat Error");
	                }
	              }
              }
            }
          }
        }
      }

    }
    execute2(upts);
    System.out.println("======load Epidemic=======");
  }

  public void loadEpidemicByEXCEL2(File file, ServiceManager serviceManager) throws IOException, SQLException {
    FileInputStream fis = new FileInputStream(file);
    @SuppressWarnings("resource")

    HSSFWorkbook workbook = new HSSFWorkbook(fis);
    int sheetNum = workbook.getNumberOfSheets();
    int rowIndex = 0;

    String epidemicNm = " ";
    String epidemicCd = " ";

    int cn, startNum = 0;

    IUserProcedures upts = createUserProcedures("createWith");
    for (cn = 0; cn < sheetNum; cn++) {

      HSSFSheet sheet = workbook.getSheetAt(cn);
      int rows = sheet.getPhysicalNumberOfRows();
      int searchNum = 3;

      String siNm = FilenameUtils.removeExtension(file.getName()).split("_")[1];

      HSSFRow row1 = sheet.getRow(searchNum);
      HSSFRow row2 = sheet.getRow(searchNum);
      HSSFRow row3 = sheet.getRow(searchNum);
      String siCd = getAddressIdBySiNm(siNm);
      String age = "";

      Map<String, String> epidemicMap = serviceManager.getEpidemicMap();

      row1 = sheet.getRow(5);
      startNum = 6;
      searchNum = startNum;
      for (searchNum = startNum; searchNum < rows - 13; searchNum += 3) {
        for (rowIndex = 3; rowIndex < 62; rowIndex++) {
          epidemicNm = String.valueOf(row1.getCell(rowIndex));
          if (!(epidemicNm.equals(""))) {
            epidemicCd = epidemicMap.get(epidemicNm);
          }

          if (epidemicCd != null) {
            row2 = sheet.getRow(searchNum);
            if(row2!=null) {
            	age = String.valueOf(row2.getCell(1));
	            if (age != null) {
	              row3 = sheet.getRow(searchNum + 1);
	              String manString = String.valueOf(row3.getCell(rowIndex));
	              manString = manString.replaceAll(",", "");
	              int man = Integer.parseInt(manString);
	              row3 = sheet.getRow(searchNum + 2);
	              String womanString = String.valueOf(row3.getCell(rowIndex));
	              womanString = womanString.replaceAll(",", "");
	              int woman = Integer.parseInt(womanString);
	              row3 = sheet.getRow(2);
	              String sdate = String.valueOf(row3.getCell(3));
	              int year = Integer.valueOf(sdate.substring(0, 4));
	              int month = Integer.valueOf(sdate.substring(5, 7));
	
	              upts.add(makeProcedure(PACKAGE_NAME + "@createWith", epidemicCd, siCd, age, woman, man, year, month));
	
	            }
            }
          }
        }
      }
    }
    execute2(upts);
    System.out.println("======load Epidemic=======");
  }

  public Rows listBy(String addressId, int year, int month, int limit) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listBy", addressId, month, limit));
  }

  public Rows listBySiCd(String siCd, int year, int month, int limit) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listBySiCd", siCd, year, month, limit));
  }

  public Rows listBySigunguCd(String sigunguCd, int year, int month, int limit) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listBySigunguCd", sigunguCd, year, month, limit));
  }

  public Rows listByWhole(int year, int month, int limit) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listByWhole", year, month, limit));
  }
  
  public Rows listByWholeEng(int year, int month, int limit) {
	    return getRows(makeProcedure(PACKAGE_NAME + "@listByWholeEng", year, month, limit));
  }
  
  public Rows listByMonthlyDanger(int year, int month, int limit) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listByMonthlyDanger", year, month, limit));
  }

  public Rows listByMonthlyDangerEng(int year, int month, int limit) {
	    return getRows(makeProcedure(PACKAGE_NAME + "@listByMonthlyDangerEng", year, month, limit));
	  }
  
  public Rows listByMonthlyRegion(String epidemicId, String siCd, int year, int month, int limit) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listByMonthlyRegion", epidemicId, siCd, year, month, limit));
  }
  
  public Rows listByMonthlyRegionEng(String epidemicId, String siCd, int year, int month, int limit) {
	return getRows(makeProcedure(PACKAGE_NAME + "@listByMonthlyRegionEng", epidemicId, siCd, year, month, limit));
  }

  public Rows listByMonthlyTrend(String epidemicId, String siCd, String startDate, String endDate) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listByMonthlyTrend", epidemicId, siCd, startDate, endDate));
  }

  public Row totalBySex(String epidemicId) {
    return getRow(makeProcedure(PACKAGE_NAME + "@totalBySex", epidemicId));
  }

  public Rows totalByAge(String epidemicId) {
    return getRows(makeProcedure(PACKAGE_NAME + "@totalByAge", epidemicId));
  }

  public Rows listByCompare(int year, String epidemicId) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listByCompare", year, epidemicId));
  }

  public Rows listByCompare2(int year, String epidemicId) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listByCompare2", year, epidemicId));
  }

  public Rows listByYearly(String epidemicId) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listByYearly", epidemicId));
  }

  public Rows listByGender(String epidemicId, String siCd, int year, int month) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listByGender", epidemicId, siCd, year, month));
  }

  public Rows listByAge(String epidemicId, String siCd, int year, int month) {
    return getRows(makeProcedure(PACKAGE_NAME + "@listByAge", epidemicId, siCd, year, month));
  }

  public Row avgBySigungu(String epidemicId, String start, String end) {
    return getRow(makeProcedure(PACKAGE_NAME + "@avgBySigungu", epidemicId, start, end));
  }

  public Row totalBySigungu(String sigungu, String epidemicId, String start, String end) {
    return getRow(makeProcedure(PACKAGE_NAME + "@totalBySigungu", sigungu, epidemicId, start, end));
  }

  public Rows totalTopBySigungu(String sigungu, String start, String end, int limit) {
    return getRows(makeProcedure(PACKAGE_NAME + "@totalTopBySigungu", sigungu, start, end, limit));
  }

  public Row totalBySi(String siCd, String epidemicId, String start, String end) {
    return getRow(makeProcedure(PACKAGE_NAME + "@totalBySi", siCd, epidemicId, start, end));
  }

}
