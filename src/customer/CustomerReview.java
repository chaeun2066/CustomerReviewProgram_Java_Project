package customer;

import java.io.Serializable;

public class CustomerReview implements Comparable<CustomerReview>, Serializable {
	// Fields
	public static final int SCORE_COUNT = 3;
	private String ctId;
	private String ctName;
	private String ctPhone;
	private int ctAge;
	private String visitDate;
	private int visitCount;
	private int tasteScore;
	private int cleanScore;
	private int serviceScore;
	private int starTotalScore;
	private double starAvgScore;
	private String starGrade;
	private String ctReview;
	private int likeCount;
	private int reviewLength;

	// Constructor
	public CustomerReview(String ctId, String ctName, String ctPhone, int ctAge, String visitDate, int visitCount,
			int tasteScore, int cleanScore, int serviceScore, String ctReview, int likeCount, int reviewLength) {
		super();
		this.ctId = ctId;
		this.ctName = ctName;
		this.ctPhone = ctPhone;
		this.ctAge = ctAge;
		this.visitDate = visitDate;
		this.visitCount = visitCount;
		this.tasteScore = tasteScore;
		this.cleanScore = cleanScore;
		this.serviceScore = serviceScore;
		this.ctReview = ctReview;
		this.likeCount = likeCount;
		this.reviewLength = reviewLength;
	}

	public CustomerReview(String ctId, String ctName, String ctPhone, int ctAge, String visitDate, int visitCount,
			int tasteScore, int cleanScore, int serviceScore, int starTotalScore, double starAvgScore, String starGrade,
			String ctReview, int likeCount, int reviewLength) {
		super();
		this.ctId = ctId;
		this.ctName = ctName;
		this.ctPhone = ctPhone;
		this.ctAge = ctAge;
		this.visitDate = visitDate;
		this.visitCount = visitCount;
		this.tasteScore = tasteScore;
		this.cleanScore = cleanScore;
		this.serviceScore = serviceScore;
		this.starTotalScore = starTotalScore;
		this.starAvgScore = starAvgScore;
		this.starGrade = starGrade;
		this.ctReview = ctReview;
		this.likeCount = likeCount;
		this.reviewLength = reviewLength;
	}

	// Method - Getters/Setters
	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}

	public String getCtName() {
		return ctName;
	}

	public void setCtName(String ctName) {
		this.ctName = ctName;
	}

	public String getCtPhone() {
		return ctPhone;
	}

	public void setCtPhone(String ctPhone) {
		this.ctPhone = ctPhone;
	}

	public int getCtAge() {
		return ctAge;
	}

	public void setCtAge(int ctAge) {
		this.ctAge = ctAge;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public int getTasteScore() {
		return tasteScore;
	}

	public void setTasteScore(int tasteScore) {
		this.tasteScore = tasteScore;
	}

	public int getCleanScore() {
		return cleanScore;
	}

	public void setCleanScore(int cleanScore) {
		this.cleanScore = cleanScore;
	}

	public int getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(int serviceScore) {
		this.serviceScore = serviceScore;
	}

	public int getStarTotalScore() {
		return starTotalScore;
	}

	public void setStarTotalScore(int starTotalScore) {
		this.starTotalScore = starTotalScore;
	}

	public double getStarAvgScore() {
		return starAvgScore;
	}

	public void setStarAvgScore(double starAvgScore) {
		this.starAvgScore = starAvgScore;
	}

	public String getStarGrade() {
		return starGrade;
	}

	public void setStarGrade(String starGrade) {
		this.starGrade = starGrade;
	}

	public String getCtReview() {
		return ctReview;
	}

	public void setCtReview(String ctReview) {
		this.ctReview = ctReview;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getReviewLength() {
		return reviewLength;
	}

	public void setReviewLength(int reviewLength) {
		this.reviewLength = reviewLength;
	}

	// Method - Calculation
	public void calStarTotalScore() {
		this.starTotalScore = this.tasteScore + this.cleanScore + this.serviceScore;
	}

	public void calStarAvgScore() {
		this.starAvgScore = this.starTotalScore / (double) SCORE_COUNT;
	}

	public void calGrade() {
		switch ((int) this.starAvgScore) {
		case 5:
			this.starGrade = "S";
			break;
		case 4:
			this.starGrade = "A";
			break;
		case 3:
			this.starGrade = "B";
			break;
		case 2:
			this.starGrade = "C";
			break;
		case 1:
			this.starGrade = "D";
			break;
		default:
			this.starGrade = "F";
			break;
		}
	}

	// Method Override
	@Override
	public int hashCode() {
		return this.ctId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CustomerReview))
			return false;

		return this.ctId.equals(((CustomerReview) obj).ctId);
	}

	@Override
	public int compareTo(CustomerReview customer) {
		return this.ctId.compareToIgnoreCase(customer.ctId);
	}

	@Override
	public String toString() {
		System.out.println("╋" + "━".repeat(66) + "╋");
		return "  " + ctId +"\t" + ctName + "\t" + ctPhone + "\t" + ctAge + "세\t" + visitDate + "\t" + visitCount
				+ "회\n  " + tasteScore + "점\t" + cleanScore + "점\t" + serviceScore + "점\t" + starTotalScore + "점\t"
				+ starAvgScore + "점\t" + starGrade + "등급\n" + "┠" + "─ ".repeat(14) + "  REVIEW  " + "─ ".repeat(14) + "┨\n \""
				+ ctReview + "\"\n " + "좋아요♥ - " + likeCount + "개\t";
	}

	 
}
