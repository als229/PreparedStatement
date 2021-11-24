package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberView;

public class MemberController {

	public void insertMember(String userId, String userPwd, String userName, String gender, int age, String email,
			String phone, String address, String hobby) {

		// 1 . 전달된 데이터들을 Member 객체에 담기 => 가공처리

		Member m = new Member(userId, userPwd, userName, gender, age, email, phone, address, hobby);

		// 2. Dao insert 메서드 호출
		int result = new MemberDao().insertMember(m);

		// 3. 결과값에 따라서 사용자가 보게 될 화면 지정
		if (result > 0) {// 성공했을 경우
			new MemberView().displaySuccess("인서트 성공");
		} else { // 실패했을 경우
			new MemberView().displayFail("인서트 실패");
		}

	}

	public void selectAllMember() {

		ArrayList<Member> list = new MemberDao().selectAllMember();

		if (list.isEmpty()) {
			new MemberView().displayNodata("조회할게 없슴다");
		} else {
			new MemberView().displayList(list);

		}

	}

	public void selectByKeyword(String keyword) {

		ArrayList<Member> list = new MemberDao().selectByKeyword(keyword);

		if (list.isEmpty()) {
			new MemberView().displayNodata("조회할게 없잖여");
		} else {
			new MemberView().displayList(list);
		}

	}

	public void selectByUserId(String UserId) {
		Member m = new MemberDao().selectByUserId(UserId);

		if (m.getUserId() == null) {
			new MemberView().displayNodata("그런 아이디 없는데영?");
		} else {
			new MemberView().displayOne(m);
		}

	}

	public void updateMember(String userId, String userPwd, String email, String phone, String address) {

		int result = new MemberDao().updateMember(userId, userPwd, email, phone, address);

		if (result > 0) {
			new MemberView().displaySuccess("수정에 성공하셨슴다");
		} else {
			new MemberView().displayFail("그런 아이디 없나봐용??");
		}
	}

	public void deleteMember(String userId) {

		int result = new MemberDao().deleteMember(userId);

		if (result > 0) {
			new MemberView().displaySuccess("삭제에 성공하셨슴다");
		} else {
			new MemberView().displayFail("그런 아이디 없나봐용??");
		}

	}

}
