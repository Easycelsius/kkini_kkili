package com.kk.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kk.dao.impl.BookmarkDAOImpl;
import com.kk.domain.BookmarkVO;
import com.kk.domain.HostVO;
import com.kk.domain.MemberVO;
import com.kk.service.BookmarkService;
import com.kk.service.ContactService;
import com.kk.service.MemberService;
import com.kk.service.NoticeService;
import com.kk.service.ProfileService;

@Controller
public class MypageController {

	private Logger log = LoggerFactory.getLogger(MypageController.class);

	@Autowired
	private ProfileService profileService;

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private MemberService memberService;

	// 관련 페이지로 이동
//	@RequestMapping("mypage/{step}.do")
//	public String movingPage(@PathVariable("step") String step) {
//		System.out.println("MypageController : " + step + "페이지로 이동 요청");
//		return "mypage/" + step;
//	}

	// 마이페이지 회원 탈퇴
	@RequestMapping("mypage/secession.do")
	public String pageSecession(Model m, HttpSession session) {
		log.info("MypageController : 탈퇴" + "페이지로 이동 요청");
		return "mypage/secession";
	}

	// 마이페이지 내 컨택 히스토리 조회
	@RequestMapping("mypage/contactHistory.do")
	public String contactHistory(Model m, HttpSession session) {
		log.info("MypageController : contactHistory" + "페이지로 이동 요청");

		// 로그인 시
		if (session.getAttribute("member") != null) {
			int userId = ((MemberVO) session.getAttribute("member")).getMemberId();
			m.addAttribute("contactInfo", contactService.getMyContactList(userId));
		}

		return "mypage/contactHistory";
	}

	// 세션 체크 메소드
	public boolean isSessionValid(HttpSession session) {
		MemberVO memberSession = null;
		if (session.getAttribute("member") != null) {
			memberSession = (MemberVO) session.getAttribute("member");
			session.setAttribute("member", memberSession);
			return true;
		} else {
			return false;
		}
	}

	// 마이페이지 내 입찰 내역조회
	@RequestMapping("mypage/bidHistory.do")
	public void bidHistory(Model m, HttpSession session) {
		System.out.println("MypageController.bidHistory() 실행");

		// 로그인 시
		if (session.getAttribute("member") != null) {
			int memberId = ((MemberVO) session.getAttribute("member")).getMemberId();
			System.out.println("반환 : " + m.addAttribute("contactBid", contactService.getBidHistory(memberId)));
		}
	}

	// 컨택 가치 확인
	@RequestMapping("mypage/contactValue")
	public String contactValue(Model m, HttpSession session) {
		log.info("MypageController : contactValue 실행");
		// 로그인 시
		if (session.getAttribute("member") != null) {
			int userId = ((MemberVO) session.getAttribute("member")).getMemberId();
			m.addAttribute("contactValue", contactService.contactValue(userId));
		}
		return "mypage/contactValue";
	}

	// 프로필 페이지 보기
	@RequestMapping("mypage/profile.do")
	public String movePageProfile(Model m, HttpSession session) {
		log.info("MypageController : movePageProfile" + "페이지로 이동 요청");

		HostVO vo = new HostVO();

		vo.setHostId(((MemberVO) session.getAttribute("member")).getMemberId());

		// 가져온게 없다 = DB에 없다 = 작성 아직 안함
		if (profileService.getProfile(vo) == null) {
			return "mypage/profile";
		}
		if (profileService.getProfile(vo) != null) { // 이미 호스트 신청함
			m.addAttribute("host", profileService.getProfile(vo));
			return "mypage/viewProfile";
		}

		return "mypage/profile";
	}

	// 마이 페이지 클릭시 권한에 따라 분기 - 요약 페이지임으로 필요한 DB 전달하기
	@RequestMapping("mypage/enter.do")
	public String enterPage(Model m, HttpServletRequest request, HttpSession session) {
		log.info("MypageController : enterPage 실행");

		MemberVO memberSession;

		// 세션이 존재한다면 member 할당, 존재하지 않으면 로그인 창으로 이동
		if (session.getAttribute("member") != null) {
			memberSession = (MemberVO) session.getAttribute("member");
			session.setAttribute("member", memberSession);
		} else {
			return "redirect:/sign/signin.do";
		}

		// 게스트 권한 : 0 / 호스트 권한 : 1 / 관리자 권한 : 2 --> 분기
		if (memberSession.getAuth() == 0) {

			// 컨택 내역 가져와서 보내기
			int userId = ((MemberVO) session.getAttribute("member")).getMemberId();
			m.addAttribute("contactInfo", contactService.getMyContactList(userId));

			// 북마크 내역 가져와서 보내기
			BookmarkVO bookmark = new BookmarkVO();
			bookmark.setHostId(userId);
			m.addAttribute("memberList", bookmarkService.getMemberListByHostId(bookmark));

			// 프로필 관련 사항 조회
			HostVO hostVO = new HostVO();
			hostVO.setHostId(userId);
			if (profileService.getProfile(hostVO) == null) {
				// 프로필 작성을 안했다면 그냥 통과
			}
			if (profileService.getProfile(hostVO) != null) { // 이미 호스트 신청함
				// 프로필 작성을 했다면 호스트 심사 중 메세지 띄워야 함
				MemberVO memberVO = new MemberVO();

				memberVO.setMemberId(userId);

				if (memberService.getMember(memberVO).getAuth() == 1) {
					m.addAttribute("profile", "호스트 인증 완료");
				} else {
					m.addAttribute("profile", "호스트 심사 중");
				}
			}

			return "mypage/summary";

		} else if (memberSession.getAuth() == 1) {
			// 컨택 내역 가져와서 보내기
			int userId = ((MemberVO) session.getAttribute("member")).getMemberId();
			m.addAttribute("contactInfo", contactService.getMyContactList(userId));
			m.addAttribute("contactValue", contactService.contactValue(userId));

			// 북마크 내역 가져와서 보내기
			BookmarkVO bookmark = new BookmarkVO();
			bookmark.setHostId(userId);
			m.addAttribute("memberList", bookmarkService.getMemberListByHostId(bookmark));
			m.addAttribute("memberList2", bookmarkService.getMemberListByGuestId(bookmark));

			// 프로필 관련 사항 조회
			HostVO hostVO = new HostVO();
			hostVO.setHostId(userId);
			if (profileService.getProfile(hostVO) == null) {
				// 프로필 작성을 안했다면 그냥 통과
			}
			if (profileService.getProfile(hostVO) != null) { // 이미 호스트 신청함
				// 프로필 작성을 했다면 호스트 심사 중 메세지 띄워야 함
				MemberVO memberVO = new MemberVO();

				memberVO.setMemberId(userId);

				if (memberService.getMember(memberVO).getAuth() == 1) {
					m.addAttribute("profile", "호스트 인증 완료");
				} else {
					m.addAttribute("profile", "호스트 심사 중");
				}
			}
			return "mypage/summary";
		} else if (memberSession.getAuth() == 2) {
			return "redirect:../admin/main.do";
		}

		return "redirect:../index.jsp";

	}

	@RequestMapping("mypage/requestresetpassword.do")
	public ModelAndView requestResetPassword(HttpServletRequest request, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		String referrer = request.getHeader("Referer");
		// 세션이 유효하지 않으면
		if (!isSessionValid(session)) {
			// 로그인 페이지로 redirect
			mv.setViewName("redirect:/sign/signin.do");
			return mv;
		}
		if (referrer != null && !referrer.contains("reset")) {
			request.getSession().setAttribute("prevPage", referrer);
		}
		mv.setViewName("mypage/requestresetpasswordview");
		return mv;
	}

	@RequestMapping("mypage/passwordcheck.do")
	public ModelAndView passwordCheck(MemberVO member, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		String memberEmail = null;
		if (!isSessionValid(session)) {
			mv.setViewName("redirect:/sign/signin.do");
			return mv;
		}
		if (member == null) {
			System.out.println("member == null");
			mv.setViewName("mypage/requestresetpasswordview");
		}
		memberEmail = ((MemberVO) (session.getAttribute("member"))).getEmail();
		member.setEmail(memberEmail);

		// 세션에 저장된 이메일과 사용자가 입력한 비밀번호와 일치하는 계정이 있으면
		if (member.getPassword() != null && memberService.memberSigninService(member) != null) {
			mv.setViewName("mypage/resetpasswordview");
		}
		// 그렇지 않으면
		else {
			mv.setViewName("mypage/requestresetpasswordview");
			mv.addObject("msg", "failure");
		}
		return mv;
	}

	@RequestMapping("mypage/changepassword.do")
	public ModelAndView updatePassword(MemberVO member, HttpSession session) {
		String memberEmail = null;
		memberEmail = ((MemberVO) (session.getAttribute("member"))).getEmail();
		member.setEmail(memberEmail);
		int result = memberService.updatePassword(member);
		ModelAndView mv = new ModelAndView();
		if (result >= 1) {
			mv.setViewName("redirect:completechangepassword.do");
			return mv;
		} else {
			mv.setViewName("redirect:errorchangepassword.do");
			return mv;

		}
	}

	@RequestMapping("mypage/completechangepassword.do")
	public String completeChangePassword(HttpSession session) {
		if (!isSessionValid(session)) {
			return "redirect:/sign/signin.do";
		}
		return "mypage/completechangepasswordview";
	}

	@RequestMapping("mypage/errorchangepassword.do")
	public String errorChangePassword(HttpSession session) {
		if (!isSessionValid(session)) {
			return "redirect:/sign/signin.do";
		}
		return "mypage/errorchangepasswordview";
	}

	// 관심 목록 접속시 - 내가 찜한 호스트 목록 가져오기
	@RequestMapping("mypage/interest.do")
	public ModelAndView interestPage(HttpSession session) {
		log.info("MypageController : " + "interest" + "페이지로 이동 요청");

		ModelAndView mv = new ModelAndView();

		// 세션이 존재한다면 member를 이용해서 DB 정보 가져오기, 존재하지 않으면 로그인 창으로 이동
		if (session.getAttribute("member") != null) {

			BookmarkVO bookmark = new BookmarkVO();

			bookmark.setHostId(((MemberVO) session.getAttribute("member")).getMemberId());

			mv.addObject("memberList", bookmarkService.getMemberListByHostId(bookmark));
			mv.setViewName("mypage/interest");

		} else {
			mv.setViewName("/sign/signin");
		}

		return mv;
	}

	// 나를 주목하는 사람 목록 - 나를 찜한 게스트 목록 가져오기
	@RequestMapping("mypage/interestReverse.do")
	public ModelAndView interstReversePage(HttpSession session) {
		log.info("MypageController : " + "interstReverse" + "페이지로 이동 요청");

		ModelAndView mv = new ModelAndView();

		// 세션이 존재한다면 member를 이용해서 DB 정보 가져오기, 존재하지 않으면 로그인 창으로 이동
		if (session.getAttribute("member") != null) {

			BookmarkVO bookmark = new BookmarkVO();

			bookmark.setHostId(((MemberVO) session.getAttribute("member")).getMemberId());

			mv.addObject("memberList", bookmarkService.getMemberListByGuestId(bookmark));
			mv.setViewName("mypage/interestReverse");

		} else {
			mv.setViewName("sign/signin.do");
		}

		return mv;
	}

	// 주목하기 삭제
	@RequestMapping("mypage/deleteInterest.do")
	@ResponseBody
	public String deleteBookmark(String hostId, HttpSession session) {
		log.info("MypageController : " + "deleteBookmark" + "실행");
		// 로그인했다면
		if (session.getAttribute("member") != null) {

			BookmarkVO bookmark = new BookmarkVO(Integer.parseInt(hostId),
					((MemberVO) session.getAttribute("member")).getMemberId());
			if (bookmarkService.deleteBookmark(bookmark) == 1) {
				return "1";
			}
			;

		}
		return "0";
	}

	// 주목하기 추가
	@RequestMapping("mypage/insertInterest.do")
	@ResponseBody
	public String insertBookmark(String hostId, HttpSession session) {
		log.info("MypageController : " + "insertBookmark" + "실행");
		// 로그인했다면
		if (session.getAttribute("member") != null) {

			BookmarkVO bookmark = new BookmarkVO(Integer.parseInt(hostId),
					((MemberVO) session.getAttribute("member")).getMemberId());
//			System.out.println("bookmark");
			if (bookmarkService.insertBookmark(bookmark) == 1) {
				return "1";
			}
			;

		}
		return "0";
	}

	// 프로필 등록
	@RequestMapping(value = "mypage/saveProfile.do")
	public String insertProfile(String company, String uni, MultipartFile pic, MultipartFile file, String content, HttpSession session) throws IOException {
		
		HostVO vo = new HostVO();
		
		vo.setHostId(((MemberVO) session.getAttribute("member")).getMemberId());
		vo.setCompany(company);
		vo.setUni(uni);
		vo.setPic(pic, String.valueOf(((MemberVO) session.getAttribute("member")).getMemberId()));
//		System.out.println(vo.getPic() + " " + vo.getAuthFile() + " " + vo.getFile());
		vo.setFile(file);
		vo.setContent(content);
		
		log.info("MypageController : insertProfile 실행" + vo);
		profileService.insertProfile(vo);
		return "redirect:/mypage/viewProfile.do";
	}

	// 프로필 수정
	@RequestMapping("mypage/updateProfile.do")
	public String updateProfile(HostVO vo, Model m, HttpSession session) {
		vo.setHostId(((MemberVO) session.getAttribute("member")).getMemberId());
		log.info("MypageController : updateProfile 실행" + vo);

		profileService.updateProfile(vo);

		m.addAttribute("host", profileService.getProfile(vo));

		return "mypage/viewProfile";
	}

	// 프로필 수정 페이지로 이동
	@RequestMapping("mypage/pageMoveUpdate.do")
	public String pageMoveUpdate(HostVO vo, Model m, HttpSession session) {
		log.info("MypageController : pageMoveUpdate 실행" + vo);

		vo.setHostId(((MemberVO) session.getAttribute("member")).getMemberId());

		m.addAttribute("host", profileService.getProfile(vo));

		return "mypage/updateProfile";
	}

//	// 프로필 삭제
//	@RequestMapping("")
//	public void deleteProfile(MemberVO vo){
//		System.out.println("deleteProfile 실행");
//	}
	// 프로필 상세 조회
	@RequestMapping("mypage/viewProfile")
	public String getProfile(HostVO vo, Model m, HttpSession session) {
		log.info("MypageController : getProfile 실행" + vo);
		vo.setHostId(((MemberVO) session.getAttribute("member")).getMemberId());
		m.addAttribute("host", profileService.getProfile(vo));
		return "mypage/viewProfile";
	}

}
