package com.kk.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kk.domain.HostVO;
import com.kk.domain.MemberVO;
import com.kk.service.AdminService;
import com.kk.service.ProfileService;

@Controller
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	// 관리자 메인 페이지
	@RequestMapping("admin/main.do")
	public String getAdminMain() {
		System.out.println("getAdminMain 실행");
		return "redirect: hostup.do";
	}
	
	// 스탭 관리
	@RequestMapping("admin/staff.do")
	public void getStaff() {
		System.out.println("getStaff 실행");
	}
	
	// 호스트 인증 관리
	@RequestMapping("admin/hostup.do")
	public void getHostup(Model m) {
		System.out.println("getHostup 실행");
		m.addAttribute("hostUpList", adminService.getHostup());
	}
	// 프로필 인증 페이지로 이동
	@RequestMapping("admin/auth.do")
	public String getAuth(HostVO vo, Model m){
		System.out.println("pageMoveUpdate 실행" + vo);
		System.out.println(adminService.getAuth(vo));
		
		m.addAttribute("host", adminService.getAuth(vo));
		return "admin/auth";
	}
	
	@RequestMapping("admin/levelup.do")
	public String levelup(HostVO vo, Model m){
		System.out.println("levelup 실행" + vo);
		adminService.levelup(vo);
		return "admin/main";
	}
	
	// 휴먼 계정
	@RequestMapping("admin/dormancy.do")
	public void getDormancy() {
		System.out.println("getDormancy 실행");
	}
	
	// 이용자 수 조회
	@RequestMapping("admin/users.do")
	public void getUsers(Model m, HttpSession session) {
		m.addAttribute("numberOfMember", adminService.getChartNumberOfMember());
		m.addAttribute("numberOfContact", adminService.getChartNumberOfContact());
		m.addAttribute("numberOfSuccessContact", adminService.getChartNumberOfSuccessContact());
		m.addAttribute("makingContactUser", adminService.getChartMakingContactUser());
		m.addAttribute("reservation", adminService.getChartReservation());
		System.out.println("getUsers 실행");
	}
	
	// 매칭 현황 조회
	@RequestMapping("admin/match.do")
	public void getMatch(Model m, HttpSession session) {
		
		// 관리자면
//		if( ((MemberVO) session.getAttribute("member")).getAuth() == 2) {
			m.addAttribute("contactList", adminService.getAllContact());
//		}
		
		System.out.println("getMatch 실행");
	}
	
//	// 총 매출 조회
//	@RequestMapping("admin/sales.do")
//	public void totalSales() {
//		System.out.println("getSales 실행");
//	}
	
	// 매출 장부 조회
	@RequestMapping("admin/book.do")
	public void getSalesHistory(Model m, HttpSession session) {
		m.addAttribute("totalSales", adminService.totalSales());
		m.addAttribute("salesHistory", adminService.getSalesHistory());
		
		System.out.println("getBook 실행");
	}
}
