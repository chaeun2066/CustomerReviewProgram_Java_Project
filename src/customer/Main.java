package customer;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	public static Scanner scan = new Scanner(System.in);
	public static final int INPUT = 1, UPDATE = 2, DELETE = 3, SEARCH = 4, OUTPUT = 5, SORT = 6, STATS = 7, EXIT = 8;
	public static final int ID_INPUT = 1, NAME_INPUT = 2, AGE_INPUT = 3, DATE_INPUT = 4, SCORE_INPUT = 5,
			REVIEW_INPUT = 6, COUNT_INPUT = 7, NUM_SEARCH = 8, NUM_STATS = 9, PHONE_INPUT = 10, NUM_UPDATE = 11;
	public static final int ID_SEARCH = 1, NAME_SEARCH = 2, PHONE_SEARCH = 3, DATE_SEARCH = 4;

	public static void main(String[] args) {
		List<CustomerReview> list = new ArrayList<CustomerReview>();

		DBConnection dbConn = new DBConnection();

		dbConn.connect();

		System.out.println("+" + "-".repeat(66) + "+");
		System.out.println("|                       미래의 식당 고객 정보 프로그램                      |");
		System.out.println("+" + "-".repeat(66) + "+");
		
		
		boolean loopFlag = false;

		while (!loopFlag) {
			int num = displayMenu();

			switch (num) {
			case INPUT:
				customerInputData();
				break;
			case UPDATE:
				customerUpDate();
				break;
			case DELETE:
				customerDelete();
				break;
			case SEARCH:
				customerSearch();
				break;
			case OUTPUT:
				customerOutput();
				break;
			case SORT:
				customerSort();
				break;
			case STATS:
				customerStats();
				break;
			case EXIT:
				System.out.println("프로그램이 종료됩니다.");
				loopFlag = true;
				break;
			default:
				System.out.println("번호를 다시 선택해주세요.");
				break;
			}

			System.out.println("해당 기능이 종료됩니다.");

		}

	}

	// Stats Data
	public static void customerStats() {
		final int STAR_STAT = 1, LIKE_STAT = 2, REVIEW_STAT = 3;
		List<CustomerReview> list = new ArrayList<CustomerReview>();

		System.out.print("[1] 별점 통계  [2] 좋아요 통계  [3] 리뷰 통계\n통계 방식 선택 >>");
		int statType = scan.nextInt();

		boolean value = checkInputPattern(String.valueOf(statType), NUM_SEARCH);
		if (!value) {
			return;
		}

		DBConnection dbConn = new DBConnection();
		dbConn.connect();

		switch (statType) {
		case STAR_STAT:
			System.out.print("[1]최고 별점  [2]최저 별점 >> ");
			int type = scan.nextInt();

			value = checkInputPattern(String.valueOf(type), NUM_STATS);
			if (!value) {
				return;
			}

			list = dbConn.selectMaxMin(statType, type);
			break;
		case LIKE_STAT:
			System.out.print("[1]최다 좋아요  [2]최소 좋아요 >> ");
			type = scan.nextInt();

			value = checkInputPattern(String.valueOf(type), NUM_STATS);
			if (!value) {
				return;
			}

			list = dbConn.selectMaxMin(statType, type);
			break;
		case REVIEW_STAT:
			System.out.print("[1]최장 리뷰  [2]최단 리뷰 >> ");
			type = scan.nextInt();

			value = checkInputPattern(String.valueOf(type), NUM_STATS);
			if (!value) {
				return;
			}

			list = dbConn.selectMaxMin(statType, type);
			break;
		}

		if (list.size() <= 0) {
			System.out.println("출력 할 고객 정보가 없습니다.");
			return;
		}

		for (CustomerReview customer : list) {
			System.out.println(customer);
		}

		dbConn.close();

	}

	// Sort Data
	public static void customerSort() {
		List<CustomerReview> list = new ArrayList<CustomerReview>();

		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();

			System.out.print("[1]이름  [2]별점  [3]좋아요\n정렬 방식 선택 >> ");
			int type = scan.nextInt();

			boolean value = checkInputPattern(String.valueOf(type), NUM_SEARCH);
			if (!value) {
				return;
			}

			list = dbConn.selectOrderBy(type);

			if (list.size() <= 0) {
				System.out.println("출력 할 고객 정보가 없습니다. ");
				return;
			}

			for (CustomerReview customer : list) {
				System.out.println(customer);
			}

			dbConn.close();
		} catch (Exception e) {
			System.out.println("Database OrderBy Error " + e.getMessage());
		}
		return;

	}

	// Update Data
	public static void customerUpDate() {
		final int NAME_UPDATE = 1, PHONE_UPDATE = 2, SCORE_UPDATE = 3;
		List<CustomerReview> list = new ArrayList<CustomerReview>();

		System.out.print("수정 할 고객 아이디 입력 >> ");
		String ctId = scan.nextLine();

		boolean value = checkInputPattern(ctId, ID_INPUT);
		if (!value) {
			return;
		}

		DBConnection dbConn = new DBConnection();
		dbConn.connect();

		list = dbConn.selectSearch(ctId, ID_SEARCH);

		if (list.size() <= 0) {
			System.out.println("출력 할 고객 정보가 없습니다.");
			return;
		}

		for (CustomerReview customer : list) {
			System.out.println(customer);
		}

		System.out.println("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 수정 중... ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");
		System.out.print("[1]이름  [2]휴대폰 번호  [3]평가\n수정 항목 선택 >> ");
		int num = scan.nextInt();
		
		value = checkInputPattern(String.valueOf(num),NUM_UPDATE);
		if (!value) {
			return;
		}
		
		CustomerReview upCustomer = list.get(0);
		int updateReturnValue = -1;
		
		switch(num) { 
		case NAME_UPDATE :
			scan.nextLine();
			System.out.print("이름 : " + upCustomer.getCtName() + " -> ");
			String ctName = scan.nextLine();
			value = checkInputPattern(ctName, NAME_INPUT);
			if (!value) {
				return;
			}
			upCustomer.setCtName(ctName);
			
			updateReturnValue = dbConn.update(upCustomer, NAME_UPDATE);
			
			break;
		case PHONE_UPDATE : 
			scan.nextLine();
			System.out.print("휴대폰 번호 : " + upCustomer.getCtPhone() + " -> ");
			String ctPhone = scan.nextLine();
			value = checkInputPattern(ctPhone, PHONE_INPUT);
			if (!value) {
				return;
			}
			upCustomer.setCtName(ctPhone);
					
			updateReturnValue = dbConn.update(upCustomer, PHONE_UPDATE);
			
			break;
		case SCORE_UPDATE :
			scan.nextLine();
			System.out.print("맛 " + upCustomer.getTasteScore() + "점 ->");
			int tasteScore = scan.nextInt();
			value = checkInputPattern(String.valueOf(tasteScore), SCORE_INPUT);
			if (!value)
				return;
			upCustomer.setTasteScore(tasteScore);
			
			System.out.print("청결 " + upCustomer.getCleanScore() + "점 ->");
			int cleanScore = scan.nextInt();
			value = checkInputPattern(String.valueOf(cleanScore), SCORE_INPUT);
			if (!value)
				return;
			upCustomer.setCleanScore(cleanScore);
			
			System.out.print("서비스 " + upCustomer.getServiceScore() + "점 ->");
			int serviceScore = scan.nextInt();
			value = checkInputPattern(String.valueOf(serviceScore), SCORE_INPUT);
			if (!value)
				return;
			upCustomer.setServiceScore(serviceScore);
			
			upCustomer.calStarTotalScore();
			upCustomer.calStarAvgScore();
			upCustomer.calGrade();		
			
			updateReturnValue = dbConn.update(upCustomer, SCORE_UPDATE);
			
			break;		
		}
		

		if (updateReturnValue == -1) {
			System.out.println("고객 정보 수정이 실패하였습니다.");
			return;
		} else {
			System.out.println("고객 정보 수정이 성공하였습니다.");
		}

		dbConn.close();

	}

	// Search Data
	public static void customerSearch() {
		final int NUM_ID = 1, NUM_NAME = 2, NUM_PHONE = 3, NUM_DATE = 4;
		List<CustomerReview> list = new ArrayList<CustomerReview>();

		try {
			System.out.print("[1]아이디 검색  [2]이름 검색  [3] 휴대폰 번호 검색 [4]방문날짜 검색\n검색 방식 선택 >>");
			int num = scan.nextInt();

			boolean num_value = checkInputPattern(String.valueOf(num), NUM_SEARCH);
			if (!num_value) {
				return;
			}

			DBConnection dbConn = null;
			scan.nextLine();

			switch (num) {
			case NUM_ID:
				System.out.print("검색 할 아이디 입력 >> ");
				String ctId = scan.nextLine();

				boolean value = checkInputPattern(ctId, ID_INPUT);
				if (!value) {
					return;
				}

				dbConn = new DBConnection();
				dbConn.connect();

				list = dbConn.selectSearch(ctId, ID_SEARCH);

				break;
			case NUM_NAME:
				System.out.print("검색 할 이름 입력 >> ");
				String ctName = scan.nextLine();

				value = checkInputPattern(ctName, NAME_INPUT);
				if (!value) {
					return;
				}

				dbConn = new DBConnection();
				dbConn.connect();

				list = dbConn.selectSearch(ctName, NAME_SEARCH);

				break;
			case NUM_PHONE:
				System.out.print("검색 할 휴대폰 번호 입력 >> ");
				String ctPhone = scan.nextLine();
				
				value = checkInputPattern(ctPhone, PHONE_INPUT);
				if (!value) {
					return;
				}

				dbConn = new DBConnection();
				dbConn.connect();

				list = dbConn.selectSearch(ctPhone, PHONE_SEARCH);

				break;
				
			case NUM_DATE:
				System.out.print("검색 할 방문 날짜 입력 >> ");
				String visitDate = scan.nextLine();

				value = checkInputPattern(visitDate, DATE_INPUT);
				if (!value) {
					return;
				}

				dbConn = new DBConnection();
				dbConn.connect();

				list = dbConn.selectSearch(visitDate, DATE_SEARCH);

				break;
			}

			if (list.size() <= 0) {
				System.out.println("출력 할 고객 정보가 없습니다. " );
			}

			for (CustomerReview customer : list) {
				System.out.println(customer);
			}

			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("입력 타입이 맞지 않습니다. " + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("Datbase Search Error " + e.getMessage());
		}

	}

	// Output Data
	public static void customerOutput() {
		List<CustomerReview> list = new ArrayList<CustomerReview>();

		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();

			list = dbConn.select();

			if (list.size() <= 0) {
				System.out.println("출력 할 고객 정보가 없습니다. ");
			}

			for (CustomerReview customer : list) {
				System.out.println(customer);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("Database  Select Error " + e.getMessage());
		}

	}

	// Delete Data
	public static void customerDelete() {
		try {
			System.out.print("삭제 할 고객 아이디 입력 >> ");
			String ctId = scan.nextLine();

			boolean value = checkInputPattern(ctId, ID_INPUT);
			if (!value) {
				return;
			}

			DBConnection dbConn = new DBConnection();
			dbConn.connect();

			int deleteReturnValue = dbConn.delete(ctId);

			if (deleteReturnValue == -1) {
				System.out.println("고객 정보 삭제가 실패하였습니다. ");
			} else if (deleteReturnValue == 0) {
				System.out.println("삭제 할 고객 정보가 없습니다. ");
			} else {
				System.out.println("고객 정보 삭제가 성공하였습니다. ");
			}

			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("입력 타입이 맞지 않습니다. " + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("Database Delete Error " + e.getMessage());
		}

	}

	// Input Data
	public static void customerInputData() {
		try {
			System.out.println("-".repeat(25) + "고객 정보를 작성해주세요." + "-".repeat(25));
			System.out.print("아이디 >> ");
			String ctId = scan.nextLine();

			boolean value = checkInputPattern(ctId, ID_INPUT);
			if (!value) {
				return;
			}

			System.out.print("이름 >> ");
			String ctName = scan.nextLine();

			value = checkInputPattern(ctName, NAME_INPUT);
			if (!value) {
				return;
			}
			
			System.out.print("휴대폰 번호(- 포함) >> ");
			String ctPhone = scan.nextLine();

			value = checkInputPattern(ctPhone, PHONE_INPUT);
			if (!value) {
				return;
			}

			System.out.print("나이 >> ");
			int ctAge = scan.nextInt();

			value = checkInputPattern(String.valueOf(ctAge), AGE_INPUT);
			if (!value) {
				return;
			}

			scan.nextLine();

			System.out.print("최근 방문 날짜 >> ");
			String visitDate = scan.nextLine();

			value = checkInputPattern(visitDate, DATE_INPUT);
			if (!value) {
				return;
			}

			System.out.print("누적 방문 횟수 >> ");
			int visitCount = scan.nextInt();

			value = checkInputPattern(String.valueOf(visitCount), COUNT_INPUT);
			if (!value) {
				return;
			}

			System.out.print("맛 평가 >> ");
			int tasteScore = scan.nextInt();

			value = checkInputPattern(String.valueOf(tasteScore), SCORE_INPUT);
			if (!value) {
				return;
			}

			System.out.print("청결 평가 >> ");
			int cleanScore = scan.nextInt();

			value = checkInputPattern(String.valueOf(cleanScore), SCORE_INPUT);
			if (!value) {
				return;
			}

			System.out.print("서비스 평가 >> ");
			int serviceScore = scan.nextInt();

			value = checkInputPattern(String.valueOf(serviceScore), SCORE_INPUT);
			if (!value) {
				return;
			}

			scan.nextLine();

			System.out.print("리뷰를 작성하시겠습니까? (Y/N) >> ");
			String answer = scan.nextLine();
			String ctReview = null;
			int likeCount = 0;
			int reviewLength = 0;

			value = checkInputPattern(answer, REVIEW_INPUT);
			if (!value) {
				return;
			}

			if (answer.equals("y") || answer.equals("Y")) {
				System.out.print("[ 리뷰를 작성해주세요 ] \n>>");
				ctReview = scan.nextLine();
				// 작성된 리뷰를 보고, 불특정 인원이 좋아요를 누른다는 전제 하에 랜덤값 부여
				likeCount = (int) (Math.random() * (9999 - 1 + 1) + (1));
			} else {
				ctReview = "리뷰 미작성";
				likeCount = 0;
			}

			if (ctReview.equals("리뷰 미작성")) {
				reviewLength = 0;
			} else {
				reviewLength = ctReview.length();
			}
			CustomerReview customer = new CustomerReview(ctId, ctName, ctPhone, ctAge, visitDate, visitCount, tasteScore,
					cleanScore, serviceScore, ctReview, likeCount, reviewLength);

			DBConnection dbConn = new DBConnection();
			dbConn.connect();

			int insertReturnValue = dbConn.insert(customer);
			if (insertReturnValue == -1) {
				System.out.println("고객 정보 입력이 실패하였습니다.");
			} else {
				System.out.println("고객 정보 입력이 성공하였습니다.");
			}

			dbConn.close();

		} catch (InputMismatchException e) {
			System.out.println("입력 타입이 맞지 않습니다. " + e.getMessage());

		} catch (Exception e) {
			System.out.println("Database Input Error " + e.getMessage());
		}

	}

	// Check Pattern
	public static boolean checkInputPattern(String data, int patternType) {
		String pattern = null;
		boolean regex = false;
		String message = null;

		switch (patternType) {
		case ID_INPUT:
			pattern = "[a-zA-Z]{4,8}[*0-9]{0,4}";
			message = "아이디를 다시 입력해주세요.";
			break;
		case NAME_INPUT:
			pattern = "^[가-힣]{2,4}$";
			message = "이름을 다시 입력해주세요.";
			break;
		case AGE_INPUT:
			pattern = "^[1-7][0-9]$";
			message = "나이를 다시 입력해주세요.";
			break;
		case DATE_INPUT:
			pattern = "^202[1-2]-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])";
			message = "날짜를 다시 입력해주세요.";
			break;
		case SCORE_INPUT:
			pattern = "^[0-5]$";
			message = "점수를 다시 입력해주세요.";
			break;
		case REVIEW_INPUT:
			pattern = "^(y|Y|n|N)$";
			message = "답을 다시 입력해주세요.";
			break;
		case COUNT_INPUT:
			pattern = "\\d{1,3}";
			message = "방문 횟수를 다시 입력해주세요.";
			break;
		case NUM_SEARCH:
			pattern = "^[1-4]$";
			message = "번호를 다시 입력해주세요.";
			break;
		case NUM_STATS:
			pattern = "^[1-2]$";
			message = "번호를 다시 입력해주세요.";
			break;
		case PHONE_INPUT:
			pattern = "010-\\d{3,4}-\\d{4}";
			message = "휴대폰 번호를 다시 입력해주세요.";
			break;
		case NUM_UPDATE:
			pattern = "^[1-3]$";
			message = "번호를 다시 입력해주세요.";
			break;
		}

		regex = Pattern.matches(pattern, data);

		if (!regex) {
			System.out.println(message);
			return false;
		}

		return regex;

	}

	// Menu Select
	public static int displayMenu() {
		System.out.println("=".repeat(68));
		int num = -1;

		try {
			System.out.print("[1]입력   [2]수정   [3]삭제   [4]검색   [5]출력   [6]정렬   [7]통계   [8]종료\n입력 >> ");
			num = scan.nextInt();

			String pattern = "^[1-8]$";
			boolean regex = Pattern.matches(pattern, String.valueOf(num));
		} catch (Exception e) {
			System.out.println("번호를 다시 선택해주세요.");
			num = -1;
		} finally {
			scan.nextLine();
		}
		return num;
	}

}
