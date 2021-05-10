package com.kk.domain;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

// 호스트 객체(컨택+프로필)
public class HostVO extends MemberVO {
	private int hostId; // 호스트번호 pk
	private String hostPic; // 프로필사진
	private String company; // 회사
	private String uni; // 대학교
	private int hostValue; // 수익
	private String writeDate; // 호스트 신청 일자
	private String authFile; // 호스트 인증 파일
	private String content; // 호스트 소개 내용
	private String result; // 인증 결과

	public HostVO() {
		super();
	}
	

	public HostVO(int hostId, String hostPic, String company, String uni, int hostValue, String writeDate,
			String authFile, String result, String content) {
		super();
		this.hostId = hostId;
		this.hostPic = hostPic;
		this.company = company;
		this.uni = uni;
		this.hostValue = hostValue;
		this.writeDate = writeDate;
		this.authFile = authFile;
		this.content = content;
		this.result = result;
	}

	MultipartFile pic; // ******이 변수 명이 <input name='' 과 동일
	MultipartFile file; // profile에서 확인
	
	public MultipartFile getPic() {
		return pic;
	}

	public void setPic(MultipartFile pic, String id) {
		this.pic = pic;
		
		// 프로필 사진 업로드
		if(!pic.isEmpty()) {
			hostPic = "pic"+id+".jpg";
//			File f = new File("C:\\00.siat\\10.Spring\\kkini_kkili\\src\\main\\webapp\\upload\\host\\" + hostPic);
//			File f = new File("../src/main/webapp/upload/host" + hostPic);
			File f = new File("D:\\kkini_kkili\\src\\main\\webapp\\upload\\host\\" + hostPic);
			
			try {
				pic.transferTo(f);
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
		
		// 명함 자료 업로드
		if(!file.isEmpty()) {
			authFile = file.getOriginalFilename();
//			File f = new File("C:\\00.siat\\10.Spring\\kkini_kkili\\src\\main\\webapp\\upload\\host\\" + authFile);
			File f = new File("D:\\kkini_kkili\\src\\main\\webapp\\upload\\host\\" + authFile);
			try {
				file.transferTo(f);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public String getAuthFile() {
		return authFile;
	}

	public void setAuthFile(String authFile) {
		this.authFile = authFile;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getHostPic() {
		return hostPic;
	}

	public void setHostPic(String hostPic) {
		this.hostPic = hostPic;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getUni() {
		return uni;
	}

	public void setUni(String uni) {
		this.uni = uni;
	}

	public int getHostValue() {
		return hostValue;
	}

	public void setHostValue(int hostValue) {
		this.hostValue = hostValue;
	}

	@Override
	public String toString() {
		return "HostVO [hostId=" + hostId + ", hostPic=" + hostPic + ", company=" + company + ", uni=" + uni
				+ ", hostValue=" + hostValue + ", writeDate=" + writeDate + ", authFile=" + authFile + ", content="
				+ content + ", result=" + result + "]";
	}

}
