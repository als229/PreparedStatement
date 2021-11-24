package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.kh.model.vo.Member;

public class MemberDao {
	/**
	 * JDBC 용 객체 - Connection : DB의 연결정보를 담고 있는 객체(ip주소 , port 번호, 계정명, 비밀번호) -
	 * (Prepared) Statement : 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체 - ResultSet : 만일
	 * 실행한 SQL문이 SELECT문일 경우 조회된 결과들이 담겨있는 객체
	 * 
	 * ** PreparedStatement 특징 : SQL문을 바로 실행하지 않고 잠시 보관하는 개념 미완성된 SQL문을 먼저 전달하고
	 * 실행하기전에 완성형태로 만든 후 실행만 하면 됨 미완성된 SQL문 만들기(사용자가 입력한 값들이 들어갈 수 있는 공간을 ?(위치홀더)로
	 * 확보) 각 위치홀더에 맞는 값들을 세팅 ** Statement(부모)와 PreparedStatement(자식) 관계 *차이점 1)
	 * Statement 는 완성된 SQL문, PreparedStatement는 미완성된 SQL문
	 * 
	 * 2) Statement 객체 생성시 stmt = conn.createStatement(); PreparedStatement 객체 생성 시
	 * pstmt = conn.prepareStatement(sql);
	 * 
	 * 3) Statement 로 SQL문 실행 시 결과 = stmt.excuteXXX(sql); PreparedStatement 로 SQL문
	 * 실행시 ?로 빈 공간을 실제 값으로 채워 준뒤 실행한다. pstmt.setString(?위치, 실제값); pstmt.setInt(?위치,
	 * 실제값); 결과 = pstmt.executeXXXX();
	 * 
	 * ** JDBC 처리순서 1) JDBC Driver 등록 : 해당 DBMS가 제공하는 클래스 등록 2) Connection 객체 생성 :
	 * 접속하고자 하는 DB의 정보를 입력해서 DB에 접속하면서 생성(url, 계정, 비번) 3_1) PreparedStatement 객체 생성
	 * : Connection객체를 이용해서 생성(미완성된 SQL문을 담아서) 3_2) 현재 미완성된 SQL문을 완성형태로 채우기 => 미완성된
	 * 경우에만 해당 / 완성된 경우에는 생략 가능 4) SQL문 실행 : executeXXX() = sql 매개변수 없음!! > SELECT문의
	 * 경우 : executeQuery() 메서드호출 > 나머지 DML문의 경우 : executeUpdate() 메서드호출 5) 결과 받기 >
	 * SELECT문의 경우 : ResultSet 객체(조회된 데이터들이 담겨있음)로 받기 =>6_1) > 나머지의 DML의 경우 :
	 * int형(처리된 행의 갯수)으로 받기 => 6_2)
	 *
	 * 6_1) ResultSet에 담겨있는 데이터들을 하나씩 뽑아서 VO객체에 담기(많으면 ArrayList로 관리) 6_2)
	 * 트랜젝션처리(성공이면 COMMIT, 실패면 ROLLBACK) 7) 다 쓴 JDBC용 객체들은 반드시 자원반납(close()) => 생성된
	 * 순서의 역순으로!!! 8) 결과 반환() > SELECT 문의 경우 6_1) 에서 만들어진 결과 > 나머지 DML문의 경우 처리된 행의
	 * 갯수
	 */

	public int insertMember(Member m) {

		// 0) 필요한 변수들 먼저 셋팅
		int result = 0; // 처리된 결과(처리된 행의 갯수)를 담아줄 변수
		Connection conn = null; // 접속된 DB의 연결정보를 담는 변수
		PreparedStatement pstmt = null; // SQL문 실행 후 결과를 받기 위한 변수

		// + 필요한 변수 : 실행할 SQL(미완성된 형태로)
		// INSERT INTO MEMBER
		// VALUES(SEQ_USERNO.NEXTVAL, 'xxx', 'xxx', 'xxx', 'xxx','x','xxx@xxx'
		// , 'xxxx', 'xxxx','xxx',SYSDATE)

//		String sql = "ISERT INTO MEMBER "
//					+ " VALUES(SEQ_UERNO.NEXTVAL, ?,?,?,?,?,?,?,?,?, DEFAULT)";

		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL,?,?,?,?,?,?,?,?,?,SYSDATE)";
		// 1) JDBC드라이버 등록
		try {
			// 1) JDBC드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2) DB와 연결시키겠다 => url, 계정명, 비밀번호
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			// 3_1) PreparedStatement 객체 생성 (SQL문을 미리 넘겨준다.)
			pstmt = conn.prepareStatement(sql);

			// 3_2) 미완성된 SQL문일 경우 완성시켜주기
			// pstmt.setXXX(?의 위치, 실제값)
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());

			// PreparedStatement 의 단점
			// => 완성된 SQL문을 볼수 없다.

			// 4, 5) DB에 완성된 SQL문을 실행 후 결과(처리된 행의 갯수) 받기
			result = pstmt.executeUpdate();

			// 6_2) 트랜잭션 처리
			if (result > 0) { // 1개 이상의 행이 INSERT 되었다면 == 성공했을 경우 => 커밋
				conn.commit();
			} else { // 실패했을 경우 => 롤백
				conn.rollback();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("너 지금 오타냈어?");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("너지금 빵꾸 이상하게 채웠어?");
			e.printStackTrace();
		} finally {
			try {
				// 7) 다 쓴 JDBC용 객체 자원 반납 => 생성된 순서의 역순으로 close()
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ArrayList<Member> selectAllMember() {

		ArrayList<Member> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		String sql = "SELECT * FROM MEMBER";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			rSet = pstmt.executeQuery(sql);

			while (rSet.next()) {
				Member m = new Member();

				m.setUserNo(rSet.getInt("USERNO"));
				m.setUserId(rSet.getString("USERID"));
				m.setUserPwd(rSet.getString("USERPWD"));
				m.setUserName(rSet.getString("USERNAME"));
				m.setGender(rSet.getString("GENDER"));
				m.setAge(rSet.getInt("AGE"));
				m.setEmail(rSet.getString("EMAIL"));
				m.setPhone(rSet.getString("PHONE"));
				m.setAddress(rSet.getString("ADDRESS"));
				m.setHobby(rSet.getString("HOBBY"));
				m.setEnrollDate(rSet.getDate("ENROLLDATE"));

				list.add(m);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rSet.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public ArrayList<Member> selectByKeyword(String keyword) {

		ArrayList<Member> list = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;

		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, "%" + keyword + "%");
			rSet = pstmt.executeQuery(); // ㅅㅂ 이새기 순서를 조심하자
											// **** Prepared쓸때는 rset에는 인자값 안넣어주고
											// setString해서 쓴다음 rset써야 된다 !!!!!!!!!!!!!!!!!

			while (rSet.next()) {
				Member m = new Member();

				m.setUserNo(rSet.getInt("USERNO"));
				m.setUserId(rSet.getString("USERID"));
				m.setUserPwd(rSet.getString("USERPWD"));
				m.setUserName(rSet.getString("USERNAME"));
				m.setGender(rSet.getString("GENDER"));
				m.setAge(rSet.getInt("AGE"));
				m.setEmail(rSet.getString("EMAIL"));
				m.setPhone(rSet.getString("PHONE"));
				m.setAddress(rSet.getString("ADDRESS"));
				m.setHobby(rSet.getString("HOBBY"));
				m.setEnrollDate(rSet.getDate("ENROLLDATE"));

				list.add(m);

			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rSet.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public Member selectByUserId(String userId) {

		Member m = new Member();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;
		
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, userId);

			rSet = pstmt.executeQuery();
			if (rSet.next()) {
				m = new Member(rSet.getInt("USERNO"), rSet.getString("USERID"), rSet.getString("USERPWD"),
						rSet.getString("USERNAME"), rSet.getString("GENDER"), rSet.getInt("AGE"),
						rSet.getString("EMAIL"), rSet.getString("PHONE"), rSet.getString("ADDRESS"),
						rSet.getString("HOBBY"), rSet.getDate("ENROLLDATE"));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rSet.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return m;
	}

	public int updateMember(String userId, String userPwd, String email, String phone, 
			String address) {

		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		// 실행할 SQL문
		// UPDATE MEMBER
		// SET USERPWD = 'xxx',
		//	   EMAIL = 'xxx',
		//	   PHONE = 'xxx',
		//	   ADDRESS = 'xxx'
		// WHERE USERID = userId
		
		String sql = "UPDATE MEMBER SET USERPWD = ? , EMAIL = ? ,"
				+ " PHONE = ?, ADDRESS = ? WHERE USERID = ?";
					
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userPwd);
			pstmt.setString(2, email);
			pstmt.setString(3, phone);
			pstmt.setString(4, address);
			pstmt.setString(5, userId);
			result = pstmt.executeUpdate(); //****이새기도 set 다하고 그다음에 있어야함 !!!!!!!!
			
			if(result > 0) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public int deleteMember(String userId) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC","JDBC");
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			result = pstmt.executeUpdate();
			
			if(result >0) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
