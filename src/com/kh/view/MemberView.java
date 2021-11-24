package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;

public class MemberView {

	MemberController mc = new MemberController();

	Scanner sc = new Scanner(System.in);

	public void mainMenu() {
		while (true) {
			System.out.println("***** 회원 관리 프로그램 *****");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디로 검색");
			System.out.println("4. 회원 이름 키워드로 검색");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("0. 프로그램 종료");
			System.out.print("이용할 메뉴 선택 > ");
			int menu = sc.nextInt();
			sc.nextLine();

			switch (menu) {
			case 1:
				insertMember();
				break;
			case 2:
				selectALL();
				break;
			case 3:
				selectByUserId();
				break;
			case 4:
				selectByKeyword();
				break;
			 case 5:
			 updateMember();
			 break;
			 case 6:
			 delectMember();
			 break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				return;
			default:
				System.out.println("번호를 잘못 입력했습니다.");
			}

		}

	}

	private void delectMember() {
		System.out.println("---- 회원 삭제 ----");
		System.out.println("삭제하실 회원 id를 입력해주세요");
		String userId = sc.nextLine();
		
		mc.deleteMember(userId);
		
	}

	private void updateMember() {
		
		System.out.println("---- 회원 정보 수정 ----");
		System.out.println("수정하고 싶은 회원 id를 입력해주세용");
		String userId = sc.nextLine();
		System.out.println("새로운 비밀번호를 입력하세요");
		String userPwd = sc.nextLine();
		System.out.println("새로운 E-Mail을 입력하세요");
		String email = sc.nextLine();
		System.out.println("새로운 전화번호를 입력하세요");
		String phone = sc.nextLine();
		System.out.println("새로운 주소를 입력하세요");
		String address = sc.nextLine();
		
		mc.updateMember(userId, userPwd, email, phone, address);
	}

	private void selectByUserId() {
		System.out.println("---- 아이디로 검색하기 ----");
		System.out.println("검색할 아이디를 입력해주세요");
		String userId = sc.nextLine();
		
		mc.selectByUserId(userId);
		
	}

	private void selectByKeyword() {
		System.out.println("---- 이름 키워드로 검색하기 ----");
		System.out.println("검색할 키워드를 입력해 주세용");
		System.out.print("키워드 : ");
		String keyword = sc.nextLine();

		mc.selectByKeyword(keyword);

	}

	private void selectALL() {
		System.out.println("---- 회원 전체 출력 ----");
		mc.selectAllMember();

	}

	public void insertMember() {

		System.out.println("----- 회원 추가 -----");

		System.out.print("아이디 > ");
		String userId = sc.nextLine();

		System.out.print("비밀번호 > ");
		String userPwd = sc.nextLine();

		System.out.print("이름 > ");
		String userName = sc.nextLine();

		System.out.print("성별 (M / F)> ");
		String gender = String.valueOf(sc.nextLine().toUpperCase().charAt(0));

		System.out.print("나이 > ");
		int age = sc.nextInt();
		sc.nextLine();

		System.out.print("이메일 > ");
		String email = sc.nextLine();

		System.out.print("전화번호 (숫자만) > ");
		String phone = sc.nextLine();

		System.out.print("주소 > ");
		String address = sc.nextLine();

		System.out.print("취미 (, 로 공백 없이 나열) > ");
		String hobby = sc.nextLine();

		// 회원 추가 요청 => Controller의 어떤 메서드 호출
		mc.insertMember(userId, userPwd, userName, gender, age, email, phone, address, hobby);

	}

	/**
	 * 서비스 요청 성공 시 보게 될 화면
	 * 
	 * @param message 성공메세지
	 */
	public void displaySuccess(String message) {
		System.out.println("\n서비스 요청 성공 : " + message);
	}

	/**
	 * 서비스 요청 실패 시 보게 될 화면
	 * 
	 * @param message 실패 메세지
	 */
	public void displayFail(String message) {
		System.out.println("\n서비스 요청 실패 : " + message);
	}

	/**
	 * 조회 서비스 요청 시 조회결과가 없을때 보게 될 화면
	 * 
	 * @param message : 사용자에게 보여질 메세지
	 */
	public void displayNodata(String message) {
		System.out.println(message);
	}

	/**
	 * 조회서비스 요청 시 여러 행이 조회된 결과를 받아서 보게 될 화면
	 * 
	 * @param list : 여러 행이 조회된 결과
	 */
	public void displayList(ArrayList<Member> list) {
		System.out.println("\n 조회된 데이터는 " + list.size() + " 건입니다. \n");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	
	public void displayOne(Member m) {

		System.out.println("\n조회된 데이터는 다음과 같슴다.");

		System.out.println(m);
	}


}
