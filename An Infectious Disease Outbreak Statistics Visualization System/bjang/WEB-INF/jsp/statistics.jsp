<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.project.bJang.webapp.action.*"%>
<!DOCTYPE html>
<html>
<%=Face.loadMainHead(request, "Disease outbreak statistics")%>
<body class='pace-done mini-navbar'>
	<div id="wrapper">
		<!-- Navigator -->
		<%=Face.loadNavigator(request, "/statistics")%>
		<!-- End Navigator -->

		<div id="page-wrapper" class="gray-bg dashbard-1">
			<%=Face.loadTopNavigator(request)%>
			<br>
			<div class="btn-group" style="float:right">
                 <button class="btn btn-primary" type="button" style="border: 1px solid white;"  onClick="LngKor();">한국어</button>
                 <button class="btn btn-primary" type="button" style="border: 1px solid white;" onClick="LngEng();">English</button>
             </div><br>
			<div class="wrapper wrapper-content">
			
				<div class="row border-bottom white-bg page-heading">
					<div class="col-lg-12">
						<div class="div_kor" style="display : none">
							<h2>
								<strong>질병 발병 통계</strong>
							</h2>
							<p>
								대한민국에서 발병한 질병 발병 통계를 다양한 시각화 기술들을 사용하여 보여 줍니다.<br> 사용자는 연도,
								월, 지역, 그리고 병 이름을 선택하여 관심이 있는 질병 발병 통계를 검색할 수 있습니다.<br> 지역,
								시간, 질병, 성별, 연령별  발병한 질병 건수를 시각화된 사용자 친화적 인터페이스를 통해 보여 줍니다.<br>
							</p>
						</div>
						<div class="div_eng">
							<h2>
								<strong>Disease outbreak statistics</strong>
							</h2>
							<p>
								This page shows the statistics of disease outbreaks in Republic
								of Korea using various visualization techniques.<br> Users
								can search for disease outbreak statistics by selecting year,
								month, region, and disease name.<br> This page provides the
								number of infected people by region, time, disease, sex, and age
								through various visualized user-friendly interfaces<br>
							</p>
						</div>	
					</div>
				</div>
				<div class="row">
					<div class="ibox">
						<div class="ibox-title">
							<h5 class="font-bold m-b-xs" style="color: #000000;">
								<i class="fa fa-table"></i> Search
							</h5>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-md-2">
									<select
										class="select2_demo_1 form-control select2-hidden-accessible"
										tabindex="-1" aria-hidden="true" id="yearSelect"
										onchange="search()"
										style="border: 1px solid #000000; height: 40px; font-size: 20px; color: #000000;">
										<option value="2015">2015</option>
										<option value="2016">2016</option>
										<option value="2017">2017</option>
										<option value="2018">2018</option>
									</select>
								</div>
								<div class="col-md-2">
									<select
										class="select2_demo_1 form-control select2-hidden-accessible"
										tabindex="-1" aria-hidden="true" id="monthSelect"
										onchange="search()"
										style="border: 1px solid #000000; height: 40px; font-size: 20px; color: #000000;">
									</select>
								</div>
								<div class="col-md-2">
									<select
										class="select2_demo_1 form-control select2-hidden-accessible"
										tabindex="-1" aria-hidden="true" id="siSelect"
										onchange="search()"
										style="border: 1px solid #000000; height: 40px; font-size: 20px; color: #000000;">
									</select>
								</div>
								<div class="col-md-6">
									<select
										class="select2_demo_1 form-control select2-hidden-accessible"
										tabindex="-1" aria-hidden="true" id="epidemicSelect"
										onchange="search()"
										style="border: 1px solid #000000; height: 40px; font-size: 20px; color: #000000;">
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-6">
					<div class="ibox product-detail">
						<div class="ibox-title">
							<h1 style="color: #000000;" class="div_eng">
								<i class='fa fa-bar-chart-o'></i>Top 5 districts with the most infected people (regional)
							</h1>
							<h1 style="color: #000000;display:none;" class="div_kor">
								<i class='fa fa-bar-chart-o'></i>지역구별 Top 5 감염자 통계
							</h1>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div id="region">
									<canvas id="regionChart"></canvas>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox product-detail">
						<div class="ibox-title">
							<h1 style="color: #000000;" class="div_eng">
								<i class='fa fa-bar-chart-o'></i> Top 5 districts with the most infected people (nationwide)
							</h1>
							<h1 style="color: #000000;display:none;" class="div_kor">
								<i class='fa fa-bar-chart-o'></i> 전국구 Top 5 감염자 통계
							</h1>
						</div>
						<div class="ibox-content">
							<div id="danger">
								<canvas id="dangerChart"></canvas>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-6">
					<div class="ibox product-detail">
						<div class="ibox-title">
							<h1 style="color: #000000;" class="div_eng">
								<i class='fa fa-bar-chart-o'></i> Number of infected people by year
							</h1>
							<h1 style="color: #000000;display:none;" class="div_kor">
								<i class='fa fa-bar-chart-o'></i> 연간 감염자 통계
							</h1>
						</div>
						<div class="ibox-content">
							<div id="yearly">
								<canvas id="yearlyChart"></canvas>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox product-detail">
						<div class="ibox-title">
							<h1 style="color: #000000;" class="div_eng">
								<i class='fa fa-area-chart'></i> Infection trend during recent 6 months
							</h1>
							<h1 style="color: #000000;display:none;" class="div_kor">
								<i class='fa fa-area-chart'></i> 최근 6개월 전염병 추이
							</h1>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div id="trend">
									<canvas id="trendChart"></canvas>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-6">
					<div class="ibox product-detail">
						<div class="ibox-title">
							<h1 style="color: #000000;" class="div_eng">
								<i class='fa fa-area-chart'></i> Comparison of number of infected people during recent 3 years
							</h1>
							<h1 style="color: #000000;display:none;" class="div_kor">
								<i class='fa fa-area-chart'></i> 3개년 전염병 통계 비교
							</h1>
						</div>
						<div class="ibox-content">
							<div id="compare">
								<canvas id="compareChart"></canvas>
							</div>
						</div>
					</div>
				</div>

				<div class="col-lg-6">
					<div class="ibox product-detail">
						<div class="ibox-title">
							<h1 style="color: #000000;" class="div_eng">
								<i class='fa fa-bar-chart-o'></i> Top 5 most common infectious diseases (nationwide)
							</h1>
							<h1 style="color: #000000;display:none;" class="div_kor">
								<i class='fa fa-bar-chart-o'></i> 전국구 전염병 통계
							</h1>
						</div>
						<div class="ibox-content">
							<div id="whole">
								<canvas id="wholeChart"></canvas>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6">
					<div class="ibox product-detail">
						<div class="ibox-title">
							<h1 style="color: #000000;" class="div_eng">
								<i class='fa fa-area-chart'></i> Number of infected people by gender
							</h1>
							<h1 style="color: #000000;display:none;" class="div_kor">
								<i class='fa fa-area-chart'></i> 성별 감염자 통계
							</h1>
						</div>
						<div class="ibox-content">
							<div id="gender">
								<canvas id="genderChart"></canvas>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox product-detail">
						<div class="ibox-title">
							<h1 style="color: #000000;" class="div_eng">
								<i class='fa fa-bar-chart-o'></i> Number of infected people by age
							</h1>
							<h1 style="color: #000000;display:none;" class="div_kor">
								<i class='fa fa-area-chart'></i> 연령별 감염자 통계
							</h1>
						</div>
						<div class="ibox-content">
							<div id="age">
								<canvas id="ageChart"></canvas>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%-- <%=Face.loadFooter(request)%> --%>
	</div>

	<%=Face.loadScript(request)%>
	<script>
		var lng = "eng";
		
		$(document).ready(function() {
			var now = new Date();
			var year = now.getFullYear();
			var month = now.getMonth();

			if (month == 0) {
				month = 12;
				year -= 1;
			}
			//default language : English
			LngEng();
			
		});
		
		//Language setting(Korean/English)
		function LngKor() {
			
			lng = "kor";
			changeLng(lng);
	
			loadSelectEpidemic(lng);
			loadSelectDate(lng);
			loadSelectSi(lng);
			$('.div_kor').css("display","block");
			$('.div_eng').css("display","none");
			
		}
		
		function LngEng() {

			lng = "eng";
			changeLng(lng);

			loadSelectEpidemic(lng);
			loadSelectDate(lng);
			loadSelectSi(lng);
			$('.div_eng').css("display","block");
			$('.div_kor').css("display","none");
		}
		
		//initialization (chart)
		function changeLng(lng) {
			var now = new Date();
			var year = now.getFullYear();
			var month = now.getMonth();
			
			if (month == 0) {
				month = 12;
				year -= 1;
			}
			var whole = $("#whole").empty();
			var danger = $("#danger").empty();
			var region = $("#region").empty();
			var trend = $("#trend").empty();
			var compare = $("#compare").empty();
			var yearly = $("#yearly").empty();
			var gender = $("#gender").empty();
			var age = $('#age').empty();

			whole.append('<canvas id="wholeChart"\>');
			danger.append('<canvas id="dangerChart"\>');
			region.append('<canvas id="regionChart"\>');
			trend.append('<canvas id="trendChart"\>');
			compare.append('<canvas id="compareChart"\>');
			yearly.append('<canvas id="yearlyChart"\>');
			gender.append('<canvas id="genderChart"\>');
			age.append('<canvas id="ageChart"\>');
			
			makeChart('whole', '5', year, month, '', lng);
			makeChart('danger', '5', year, month, '', lng);

			makeChart('region', '5', year, month, '',lng);
			makeChart('trend', '5', year, month, '', lng);

			makeChart('compare', '5', year, month, '',lng);
			makeChart('yearly', '5', year, month, '',lng);

			makeChart('gender', '5', year, month, '',lng);
			makeChart('age', '5', year, month, '', lng);
			
		}
		 
		/*
		search() function
		run every time you select a value in the select box(search form)
		*/
		function search() {

			var whole = $("#whole").empty();
			var danger = $("#danger").empty();
			var region = $("#region").empty();
			var trend = $("#trend").empty();
			var compare = $("#compare").empty();
			var yearly = $("#yearly").empty();
			var gender = $("#gender").empty();
			var age = $('#age').empty();

			whole.append('<canvas id="wholeChart"\>');
			danger.append('<canvas id="dangerChart"\>');
			region.append('<canvas id="regionChart"\>');
			trend.append('<canvas id="trendChart"\>');
			compare.append('<canvas id="compareChart"\>');
			yearly.append('<canvas id="yearlyChart"\>');
			gender.append('<canvas id="genderChart"\>');
			age.append('<canvas id="ageChart"\>');

			$("#wholeChart").attr("height", "250");
			$("#regionChart").attr("height", "250");
			$("#dangerChart").attr("height", "250");
			$("#yearlyChart").attr("height", "250");
			$("#genderChart").attr("height", "250");
			$("#ageChart").attr("height", "250");
			$("#compareChart").attr("height", "250");
			$("#trendChart").attr("height", "250");

			var epidemicId = $('#epidemicSelect option:selected').val();
			var year = $('#yearSelect option:selected').val();
			var month = $('#monthSelect option:selected').val();
			var siCd = $('#siSelect option:selected').val();

			if (siCd == "all") {
				siCd = '';
			}

			makeChart('whole', epidemicId, year, month, siCd, lng);
			makeChart('danger', epidemicId, year, month, siCd, lng);
			makeChart('region', epidemicId, year, month, siCd, lng);
			makeChart('trend', epidemicId, year, month, siCd, lng);
			makeChart('compare', epidemicId, year, month, siCd, lng);
			makeChart('yearly', epidemicId, year, month, siCd, lng);
			makeChart('gender', epidemicId, year, month, siCd, lng);
			makeChart('age', epidemicId, year, month, siCd, lng);

		}
		
		/*
		makeChart() function
		parameter : path, _epidemicId, _year, _month, _siCd, lng (user's selected result)
		use AJAX to transfer data to a web server and draw chart. 
		*/
		function makeChart(path, _epidemicId, _year, _month, _siCd, lng) {
			var chart = $("#" + path + "Chart");
			var _epidemicNm = $('#epidemicSelect option:selected').text();
			
			if (_epidemicNm == '') {
				_epidemicNm = "A형간염";
			}
			
			$.ajax({
				url : '/chart/epidemic/' + path,
				data : {
					'epidemicId' : _epidemicId,
					'epidemicNm' : _epidemicNm,
					'siCd' : _siCd,
					'year' : _year,
					'month' : _month,
					'lng' : lng,
				},
				success : function(json) {
					if (path == "compare") {
						new Chart(chart, getLineChartConfig(_year, json));
					} else if (path == "trend") {
						new Chart(chart, getTrendChartConfig(json));
					} else {
						new Chart(chart, getChartConfig(json));
					}
				}
			});
		}

		/*
		loadSelect..() function
		load epidemic name,month, district from database 
		and list the data in the select box
		*/
		function loadSelectEpidemic(lng) {
			$('#epidemicSelect').html("");
			if(lng == "kor"){
				$.ajax({
					url : '/epidemic/code/select',
					success : function(html) {
						$('#epidemicSelect').html(html);
					}
				});
			}
			else {
				$.ajax({
					url : '/epidemic/engcode/select',
					success : function(html) {
						$('#epidemicSelect').html(html);
					}
				});
			}
		}

		function loadSelectDate(lng) {
			$('#monthSelect').html("");
			var now = new Date();
			var year = now.getFullYear();
			var month = now.getMonth();
			
			if (month == 0) {
				month = 12;
				year -= 1;
			}
			if(lng == "eng"){
				$.ajax({
					url : '/month/engcode/select',
					success : function(html){
						$('#monthSelect').html(html);
						$('#yearSelect').val(year).prop("selected", true);
						$('#monthSelect').val(month).prop("selected", true);
					}
				});
			}
			else {
				$('#monthSelect').html('<option value="1">1월</option><option value="2">2월</option><option value="3">3월</option><option value="4">4월</option><option value="5">5월</option><option value="6">6월</option><option value="7">7월</option><option value="8">8월</option><option value="9">9월</option><option value="10">10월</option><option value="11">11월</option><option value="12">12월</option>');
				$('#yearSelect').val(year).prop("selected", true);
				$('#monthSelect').val(month).prop("selected", true);
			}	
		}
		
		function loadSelectSi(lng) {
			$('#siSelect').html("");
			if(lng == "eng"){
				$.ajax({
					url : '/si/engcode/select',
					success : function(html){
						$('#siSelect').html("<option selected = 'selected' value='all'>Nationwide</option>");
						$('#siSelect').append(html);
						
					}
				});
			}
			else {
				$.ajax({
					url : '/si/code/select',
					success : function(html){
						$('#siSelect').html("<option selected = 'selected' value='all'>전국</option>");
						$('#siSelect').append(html);
					
					}
				});
			}	
		}
	</script>
</body>
</html>
