<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%!
	String email = "";
	String sessionName = "";
	int memberId = 0;
	Boolean login = false;
%>
<%
	if( session.getAttribute("member")!= null ) {
		email = (String) ((MemberVO) session.getAttribute("member")).getEmail();
		memberId = ((MemberVO) session.getAttribute("member")).getMemberId();
		sessionName = ((MemberVO) session.getAttribute("member")).getName();
	}
	if(email.length()>=1 && email.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$")) {
		login = true;
	} 
	pageContext.setAttribute("login", login);
%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <title>인생을 바꿀 한끼, 끼니끼리</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    
    <!-- 한글 관련 css 적용 -->
	<link rel="stylesheet" href="./../resources/css/korean.css">
    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Alex+Brush" rel="stylesheet">
    <link rel="stylesheet" href="./../resources/css/open-iconic-bootstrap.min.css">
    <link rel="stylesheet" href="./../resources/css/animate.css">
    <link rel="stylesheet" href="./../resources/css/owl.carousel.min.css">
    <link rel="stylesheet" href="./../resources/css/owl.theme.default.min.css">
    <link rel="stylesheet" href="./../resources/css/magnific-popup.css">
    <link rel="stylesheet" href="./../resources/css/aos.css">
    <link rel="stylesheet" href="./../resources/css/ionicons.min.css">
    <link rel="stylesheet" href="./../resources/css/bootstrap-datepicker.css">
    <link rel="stylesheet" type="text/css" href="./../resources/assets/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="./../resources/assets/css/style.css">
    <link rel="stylesheet" href="./../resources/css/jquery.timepicker.css">
    <link rel="stylesheet" href="./../resources/css/flaticon.css">
    <link rel="stylesheet" href="./../resources/css/icomoon.css">
    <link rel="stylesheet" href="./../resources/css/style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <style>
		#bid_price:focus{
			outline: none;
		}
		p {margin: 0 auto;}

    </style>
  </head>
  <body>
    
  <!-- 헤더 -->
	<%@include file="/WEB-INF/tiles/header.jsp"%>
	<!-- 헤더 종료 -->
    
  <div class="hero-wrap" style="background-color: #2DAD92; height: 120px;">
    
  </div>
    <section class="ftco-section ftco-degree-bg">
      <div class="container">
        <div class="row">
          <div class="col-md-12 ftco-animate">

            <div class="d-flex p-5">
				<div class="bio align-self-md-center mr-5">
					<img src="./../upload/host/${bidView.HOST_PIC}" alt="Image placeholder"
								class="img-fluid rounded-circle mb-4">
				</div>
				<div class="desc align-self-md-center">
					<span><h3 id="name" class="name font-weight-bold" style="display: inline;">${bidView.NAME}</h3></span>
					<span><h4 id="company" class="font-weight-light" style="display: inline; color: gray;">${bidView.COMPANY}</h4></span>
					<p><span style="font-weight: bold;">최근 접속일</span> : ${bidView.LAST_CONN_DATE}</p><br/>
					<p><span style="font-weight: bold;">소개</span> : ${bidView.CONTENT}</p>
					<p><span style="font-weight: bold;">일시</span> : ${bidView.MEETING_TIME}</p>
					<p><span style="font-weight: bold;">장소</span> : ${bidView.STORE_NAME}</p>
					<p><span style="font-weight: bold;">주소</span> : ${bidView.LOCATION}</p>
					<!-- 
					<div class="tag-widget post-tag-container mb-5 mt-5">
						<div class="tagcloud">
							<a href="#" class="tag-cloud-link">네이버</a> 
							<a href="#" class="tag-cloud-link">백엔드</a> 
							<a href="#" class="tag-cloud-link">자바</a>
							<a href="#" class="tag-cloud-link">여행</a>
						</div>
					</div>
					 -->
				</div>
			</div>
					<div class="row">
						<div class="card col-md-8" style="width: 100%; margin: 0 auto;">
							<div class="card-header">
								<h5>최근 입찰 내역</h5>
								<span>최근 입찰 <code>3건</code>의 내역입니다.</span> 
								<div class="card-header-right">
									<ul class="list-unstyled card-option">
										<li><i class="fa fa fa-wrench open-card-option"></i></li>
										<li><i class="fa fa-window-maximize full-card"></i></li>
										<li><i class="fa fa-minus minimize-card"></i></li>
										<li><i class="fa fa-refresh reload-card"></i></li>
										<li><i class="fa fa-trash close-card"></i></li>
									</ul>
								</div>
							</div>
							<div class="card-block table-border-style">
								<div class="table-responsive">
									<table class="table table-hover" style="text-align: center;">
										<thead >
											<tr>
												<th></th>
												<th>입찰자</th>
												<th>일시</th>
												<th>금액</th>
											</tr>
										</thead>
										<tbody>
										
										<c:forEach items="${recentBidList}" var="recentBidList">
											<tr>
												<th scope="row">${recentBidList.RN }</th>
												<td>${recentBidList.NAME }</td=>
												<td>${recentBidList.BID_TIME }</td=>
												<td>${recentBidList.PRICE }원</td=>
											</tr>
										</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						
						<!-- 
						 <div class="col-md-4"></div>
						 -->

						<div class="card-block col-md-4">
							<div class="row align-items-center b-b-default" style="height: 50%; border: 1px solid rgba(0, 0, 0, 0.125); border-radius: 0.25rem;">
								<div class="col-sm-12 b-r-default p-b-20 p-t-20">
									<div class="row align-items-center text-center">
										<div class="col-12 p-l-0">
											<p class="text-muted m-b-0" id="count_time_con">마감 시간<h5 id="count_time">${bidView.REGI_DATE}</h5>
											</p>
										</div>
									</div>
								</div>
							</div>
							<div class="row align-items-center" style="height: 50%; border: 1px solid rgba(0, 0, 0, 0.125); border-radius: 0.25rem;">
								<div class="col-sm-12 p-b-20 p-t-20">
									<div class="row align-items-center text-center">
										<div class="col-12 p-l-0">
											<p class="text-muted m-b-0" id="lastValue_con">현재 최고가<h5 id="lastValue">${bidView.LAST_VALUE}원</h5>
											</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						<!-- 
						<h4 class="col-md-2" id="count_time_con">마감 시간<h5 class="col-md-4" id="count_time">${bidView.REGI_DATE}</h5></h4>
						<h4 class="col-md-2" id="lastValue_con">현재 최고가</h4><h5 id="lastValue" class="col-md-4">${bidView.LAST_VALUE}원</h5>
						 -->
						<br />
						<!-- <div class="col-md-2"></div> -->
						<span class="col-md-12" style="width: 100%; height: 42px;"></span>
						<div class="col-md-6" id="bid_result"
							style="text-align: right; line-height: 42px"></div>
						<input id="bid_price" type="text" class="col-md-4" 
						style="border: 0; border-bottom: 1px solid; font-size: 15px; text-align: center;" placeholder="새 경매가를 입력하세요"></input><br/>
						<input id="bid" type="text" value="경매 참여" class="btn py-2 px-2 btn-primary col-md-2" />
					</div>
				</div>
          </div>
        </div>
      </div>
      
      <!-- ajax로 넘겨주는 값 -->
	  <!--
		  <div id="upName" style="display:none">${recentBidListUpdate[1].NAME}</div>
		  <div id="upBidTime" style="display:none">${recentBidListUpdate[1].BID_TIME}</div>
		  <div id="upPrice" style="display:none">${recentBidListUpdate[1].PRICE}</div>
		-->
	  <div id="upName" style="display:none"><%=sessionName %></div>
      <div id="hostId" style="display:none">${bidView.HOST_ID}</div>
      <div id="memberId" style="display:none"><%=memberId%></div>
      <div id="loginFlag" style="display:none"><%=login%></div>
      <div id="contactId" style="display:none">${bidView.CONTACT_ID}</div>
    </section> <!-- .section -->

    <!-- footer.html -->
	<%@include file="/WEB-INF/tiles/footer.jsp"%>
    
  

  <!-- loader -->
  <div id="ftco-loader" class="show fullscreen"><svg class="circular" width="48px" height="48px"><circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke="#eeeeee"/><circle class="path" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke-miterlimit="10" stroke="#F96D00"/></svg></div>

  <script src="./../resources/js/jquery.min.js"></script>
  <script src="./../resources/js/jquery-migrate-3.0.1.min.js"></script>
  <script src="./../resources/js/popper.min.js"></script>
  <script src="./../resources/js/bootstrap.min.js"></script>
  <script src="./../resources/js/jquery.easing.1.3.js"></script>
  <script src="./../resources/js/jquery.waypoints.min.js"></script>
  <script src="./../resources/js/jquery.stellar.min.js"></script>
  <script src="./../resources/js/owl.carousel.min.js"></script>
  <script src="./../resources/js/jquery.magnific-popup.min.js"></script>
  <script src="./../resources/js/aos.js"></script>
  <script src="./../resources/js/jquery.animateNumber.min.js"></script>
  <script src="./../resources/js/bootstrap-datepicker.js"></script>
  <script src="./../resources/js/jquery.timepicker.min.js"></script>
  <script src="./../resources/js/scrollax.min.js"></script>
  <script src="./../resources/js/main.js"></script>
  <script src="./../resources/js/bid.js"></script>
    
  </body>
</html>