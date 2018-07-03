# Epidemic (http://epidemic.co.kr)
: Infectious Disease Information System

## 1. An Infectious Disease Outbreak Statistics Visualization System 

    (http://epidemic.co.kr/statistics)
  
  
     1) Development environment
  
        Data : Korea Centers for Disease Control & Prevention (KCDC)

        Languags : JAVA, HTML, CSS, JQuery..

        Web Server : jetty-9.0.6

        Application Server : Spring Framework 4

        DataBase : PostgreSQL 9.6

    2) Source Code
       
       EpidemicCarrier.java 
       : load infectious disease outbreak information (raw) data from CSV file (KCDC) and insert the data to DB
       
       ChartForm.java / ChartController.java 
       : create query, select infectious disease outbreak information from DB and make charts with the data
       
       BJangController.java
       : make charts (label..)
       
       statistics.jsp
       : userinterface (searchform) 
       : request JSON and show charts
       
       epidemic_Funtion.sql 
       : query set (insert(select) infectious disease outbreak information to(from) DB)
       
       example_data.xls
       : example data of KCDC (http://www.cdc.go.kr/npt/)
       
       

