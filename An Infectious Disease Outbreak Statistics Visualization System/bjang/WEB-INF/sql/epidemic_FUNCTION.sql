--Top 5 districts with the most infected people (regional)
CREATE OR REPLACE FUNCTION EPIDEMIC$listByMonthlyRegion(v_epidemicId character varying, v_siCd character varying, v_year int, v_month int, v_limit int) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
        select
          t1.total,
          ((select t2.sinm from si_code_vw t2 where t2.sicd = substring(t1.addressid,1,2))||' '||(select t2.sigungunm from sigungu_code_vw t2 where t2.sigungucd= t1.addressid)) as address
        from
          epidemic_tbl t1
        where 
          t1.year = v_year and
          t1.month = v_month and
          t1.epidemicId = v_epidemicId and
          (case UTILS$nvl(v_siCd) when '' then 1 else strpos(substring(t1.addressid,1,2), v_siCd) end) > 0
		order by t1.total desc
		limit v_limit;
	return rtn_cursor;
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION EPIDEMIC$listByMonthlyRegionEng(v_epidemicId character varying, v_siCd character varying, v_year int, v_month int, v_limit int) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR		
		select
          t1.total,
          ((select t2.engnm from eng_tbl t2 where t2.groupid = 'si' and t2.codeid = substring(t1.addressid,1,2))||' '||(select t2.engnm from eng_tbl t2 where t2.codeid= t1.addressid)) as address
        from
          epidemic_tbl t1
        where 
          t1.year = v_year and
          t1.month = v_month and
          t1.epidemicId = v_epidemicId and
          (case UTILS$nvl(v_siCd) when '' then 1 else strpos(substring(t1.addressid,1,2), v_siCd) end) > 0
		order by t1.total desc
		limit v_limit;
		
	return rtn_cursor;
END $$ LANGUAGE plpgsql;

--Top 5 districts with the most infected people (nationwide)
CREATE OR REPLACE FUNCTION EPIDEMIC$listByMonthlyDanger(v_year int,v_month int, v_limit int) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
		select 
		  t3.sinm||' '|| t2.sigungunm as address, 
		  sum(total) as total
		from 
		  epidemic_tbl t1, 
		  sigungu_code_vw t2,
		  si_code_vw t3 
		where 
		  t1.addressid = t2.sigungucd and 
		  t2.sicd = t3.sicd and 
		  year = v_year and 
		  month = v_month
		group by t3.sinm, t2.sigungunm
		order by total desc
		limit v_limit;
	return rtn_cursor; 
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION EPIDEMIC$listByMonthlyDangerEng(v_year int,v_month int, v_limit int) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
		select
		  t3.engnm||' '|| t2.engnm as address, 
		  sum(total) as total
		from 
		  epidemic_tbl t1, 
		  eng_tbl t2,
          eng_tbl t3
		where 
		  t1.addressid = t2.codeid and 
          t2.groupid = 'sigungu' and
		  SUBSTR(t2.codeid, 1, 2) = t3.codeid and 
          t3.groupid = 'si' and 
		  year = v_year and 
		  month = v_month
		group by t3.engnm, t2.engnm
		order by total desc
		limit v_limit;
	return rtn_cursor; 
END $$ LANGUAGE plpgsql;

--Number of infected people by year
CREATE OR REPLACE FUNCTION EPIDEMIC$listByYearly(v_epidemicId varchar) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
		select 
		  year,sum(total) as total
		from 
		  epidemic_tbl
		where 
		  epidemicId = v_epidemicId
		group by year
		order by year;
	return rtn_cursor; 
END $$ LANGUAGE plpgsql;

--Infection trend during recent 6 months
CREATE OR REPLACE FUNCTION EPIDEMIC$listByMonthlyTrend(v_epidemicId varchar, v_sicd varchar,v_startDate varchar,v_endDate varchar) RETURNS refcursor AS
$$
DECLARE 
    rtn_cursor refcursor := 'rcursor';
BEGIN
    OPEN rtn_cursor FOR
        select 
           sum(t1.total) as total,
           (t1.year::varchar||LPAD(t1.month::varchar, 2, '0')) as targetDate
        from epidemic_tbl t1
        where 
            t1.epidemicid=v_epidemicId and
            ((t1.year::varchar||LPAD(t1.month::varchar, 2, '0')) >= v_startDate and (t1.year::varchar||LPAD(t1.month::varchar, 2, '0')) <= v_endDate) and
            (case UTILS$nvl(v_siCd) when '' then 1 else strpos(substring(t1.addressid,1,2), v_siCd) end) > 0
        group by t1.epidemicid, targetDate
        order by targetDate ASC;
    return rtn_cursor; 
END $$ LANGUAGE plpgsql;

--Comparison of number of infected people during recent 3 years
CREATE OR REPLACE FUNCTION EPIDEMIC$listByCompare(v_year int,v_epidemicId varchar) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
		SELECT year, month, sum(total) as total
		FROM epidemic_tbl
		WHERE 
		(year = v_year-1 OR year = v_year)
    	AND epidemicId=v_epidemicId
		GROUP BY year, month
		ORDER BY year, month;
	return rtn_cursor; 
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION EPIDEMIC$listByCompare2(v_year int,v_epidemicId varchar) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
		SELECT year, month, sum(total) as total
		FROM epidemic_tbl
		WHERE 
		(year = v_year-1 OR year = v_year-2 OR year = v_year)
    	AND epidemicId=v_epidemicId
		GROUP BY year, month
		ORDER BY year, month;
	return rtn_cursor; 
END $$ LANGUAGE plpgsql;

--Top 5 most common infectious diseases (nationwide)
CREATE OR REPLACE FUNCTION EPIDEMIC$listByWhole(v_year int,v_month int, v_limit int) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
		select 
		  (select t2.epidemicnm from epidemic_code_vw t2 where t2.epidemicid = t1.epidemicId) as epidemicNm,
		  sum(t1.total) as total
		from epidemic_tbl t1
		where
		  t1.year = v_year and 
		  t1.month = v_month
		group by t1.epidemicId
		order by total desc
		limit v_limit;
	return rtn_cursor; 
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION EPIDEMIC$listByWholeEng(v_year int,v_month int, v_limit int) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
		select 
		  (select t2.engnm from eng_tbl t2 where t2.groupid = 'epidemic' and t2.codeid = t1.epidemicId) as epidemicNm,
		  sum(t1.total) as total
		from epidemic_tbl t1
		where
		  t1.year = v_year and 
		  t1.month = v_month
		group by t1.epidemicId
		order by total desc
		limit v_limit;
	return rtn_cursor; 
END $$ LANGUAGE plpgsql;

--Number of infected people by gender
CREATE OR REPLACE FUNCTION EPIDEMIC$listByGender(v_epidemicId varchar, v_siCd varchar, v_year int, v_month int) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
		select 
		  sum(man) as mantotal,
		  sum(woman) as womantotal
		from 
		  epidemic_detail_tbl
		where 
		  epidemicId = v_epidemicId and
		  year = v_year and
		  month = v_month and 
		  (case UTILS$nvl(v_siCd) when '' then 1 else strpos(siCd, v_siCd) end) > 0 ;
	return rtn_cursor; 
END $$ LANGUAGE plpgsql;

--Number of infected people by age
CREATE OR REPLACE FUNCTION EPIDEMIC$listByAge(v_epidemicId varchar, v_siCd varchar, v_year int, v_month int) RETURNS refcursor AS
$$
DECLARE 
	rtn_cursor refcursor := 'rcursor';
BEGIN
	OPEN rtn_cursor FOR
		select 
		  age,
		  sum(man+woman) as total
		from 
		  epidemic_detail_tbl
		where 
		  epidemicId = v_epidemicId and
		  year = v_year and
		  month = v_month and 
		  (case UTILS$nvl(v_siCd) when '' then 1 else strpos(siCd, v_siCd) end) > 0 ;
		group by age;
	return rtn_cursor; 
END $$ LANGUAGE plpgsql;