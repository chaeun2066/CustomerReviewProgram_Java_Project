package customer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
	private Connection connection = null;
	private ResultSet rs = null;
	public static final int ID_SEARCH = 1, NAME_SEARCH = 2, PHONE_SEARCH = 3, DATE_SEARCH = 4;
	public static final int NAME_ORDER = 1, STAR_ORDER = 2, LIKE_ORDER = 3;

	// DB Connect
	public void connect() {
		Properties properties = new Properties();

		FileInputStream fis;

		try {
			fis = new FileInputStream("C:/Database/CustomerReviewProgram/src/customer/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileInputStream Error" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Properties Error" + e.getMessage());
		}

		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("Class.forname Load Error" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Connection Error : " + e.getMessage());
		}
	}

	// Insert
	public int insert(CustomerReview customer) {
		PreparedStatement ps = null;
		int insertReturnValue = -1;
//		String insertQuery = "insert into customer(id, name, phone, age, visitDate, visitCount,tasteScore, cleanScore, serviceScore, total, avg, grade, review, likecount,reviewLength)"
//				+ " values(?, ?, ?,?, ?, ?,?,?,?, ?, ?,?,?,?)";

		String insertQuery = "call procedure_insert_customer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, customer.getCtId());
			ps.setString(2, customer.getCtName());
			ps.setString(3, customer.getCtPhone());
			ps.setInt(4, customer.getCtAge());
			ps.setString(5, customer.getVisitDate());
			ps.setInt(6, customer.getVisitCount());
			ps.setInt(7, customer.getTasteScore());
			ps.setInt(8, customer.getCleanScore());
			ps.setInt(9, customer.getServiceScore());
			ps.setString(10, customer.getCtReview());
			ps.setInt(11, customer.getLikeCount());
			ps.setInt(12, customer.getReviewLength());

			insertReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("INSERT ERROR : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PrepareStatement Close Error " + e.getMessage());
			}
		}
		return insertReturnValue;
	}

	// Delete
	public int delete(String ctId) {
		PreparedStatement ps = null;
		int deleteReturnValue = -1;
		String deleteQuery = "call procedure_delete_customer(?)";

		try {
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, ctId);

			deleteReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("DELETE ERROR : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PrepareStatement Close Error" + e.getMessage());
			}
		}
		return deleteReturnValue;
	}

	// Select_Output
	public List<CustomerReview> select() {
		List<CustomerReview> list = new ArrayList<CustomerReview>();
		PreparedStatement ps = null;
		int selectReturnValue = -1;
		String selectQuery = "select * from customer";

		try {
			ps = connection.prepareStatement(selectQuery);
			rs = ps.executeQuery(selectQuery);

			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}

			while (rs.next()) {
				String ctId = rs.getString("id");
				String ctName = rs.getString("name");
				String ctPhone = rs.getString("phone");
				int ctAge = rs.getInt("age");
				String visitDate = rs.getString("visitdate");
				int visitCount = rs.getInt("visitcount");
				int tasteScore = rs.getInt("tasteScore");
				int cleanScore = rs.getInt("cleanScore");
				int serviceScore = rs.getInt("serviceScore");
				int starTotalScore = rs.getInt("total");
				double starAvgScore = rs.getDouble("avg");
				String stargrade = rs.getString("grade");
				String ctReview = rs.getString("review");
				int likeCount = rs.getInt("likecount");
				int reviewLength = rs.getInt("reviewLength");

				list.add(new CustomerReview(ctId, ctName, ctPhone, ctAge, visitDate, visitCount, tasteScore, cleanScore,
						serviceScore, starTotalScore, starAvgScore, stargrade, ctReview, likeCount, reviewLength));
			}
		} catch (Exception e) {
			System.out.println("SELECT ERROR : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PrepareStatement Close Error " + e.getMessage());
			}
		}
		return list;
	}

	// Select_Search
	public List<CustomerReview> selectSearch(String data, int type) {
		List<CustomerReview> list = new ArrayList<CustomerReview>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectSearchQuery = "select * from customer where ";

		try {
			switch (type) {
			case ID_SEARCH:
				selectSearchQuery += "id like ?";
				break;
			case NAME_SEARCH:
				selectSearchQuery += "name like ?";
				break;
			case PHONE_SEARCH:
				selectSearchQuery += "phone like ?";
				break;
			case DATE_SEARCH:
				selectSearchQuery += "visitdate like ?";
				break;
			default:
				System.out.println("Search Type Error");
				return list;
			}

			ps = connection.prepareStatement(selectSearchQuery);

			ps.setString(1, "%" + data + "%");

			rs = ps.executeQuery();

			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}

			while (rs.next()) {
				String ctId = rs.getString("id");
				String ctName = rs.getString("name");
				String ctPhone = rs.getString("phone");
				int ctAge = rs.getInt("age");
				String visitDate = rs.getString("visitdate");
				int visitCount = rs.getInt("visitcount");
				int tasteScore = rs.getInt("tasteScore");
				int cleanScore = rs.getInt("cleanScore");
				int serviceScore = rs.getInt("serviceScore");
				int starTotalScore = rs.getInt("total");
				double starAvgScore = rs.getDouble("avg");
				String stargrade = rs.getString("grade");
				String ctReview = rs.getString("review");
				int likeCount = rs.getInt("likecount");
				int reviewLength = rs.getInt("reviewLength");

				list.add(new CustomerReview(ctId, ctName, ctPhone, ctAge, visitDate, visitCount, tasteScore, cleanScore,
						serviceScore, starTotalScore, starAvgScore, stargrade, ctReview, likeCount, reviewLength));
			}
		} catch (Exception e) {
			System.out.println("SELECTSEARCH ERROR : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PrepareStatement Close Error" + e.getMessage());
			}

		}
		return list;

	}

	// Update
	public int update(CustomerReview customer, int type) {
		final int NAME_UPDATE = 1, PHONE_UPDATE = 2, SCORE_UPDATE = 3;
		PreparedStatement ps = null;
		int updateReturnValue = -1;
		String updateQuery = null;

		switch (type) {
		case NAME_UPDATE:
			updateQuery = "call procedure_updateName_customer(?,?)";

			try {
				ps = connection.prepareStatement(updateQuery);
				ps.setString(1, customer.getCtName());
				ps.setString(2, customer.getCtId());

				updateReturnValue = ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("UPDATE ERROR : " + e.getMessage());
			} finally {
				try {
					if (ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					System.out.println("PrepareStatement Close Error" + e.getMessage());
				}
			}

			break;
		case PHONE_UPDATE:
			updateQuery = "call procedure_updatePhone_customer(?,?)";

			try {
				ps = connection.prepareStatement(updateQuery);
				ps.setString(1, customer.getCtPhone());
				ps.setString(2, customer.getCtId());

				updateReturnValue = ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("UPDATE ERROR : " + e.getMessage());
			} finally {
				try {
					if (ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					System.out.println("PrepareStatement Close Error" + e.getMessage());
				}
			}

			break;
		case SCORE_UPDATE:
			updateQuery = "call procedure_updateScore_customer(?,?,?,?,?,?,?)";

			try {
				ps = connection.prepareStatement(updateQuery);
				ps.setInt(1, customer.getTasteScore());
				ps.setInt(2, customer.getCleanScore());
				ps.setInt(3, customer.getServiceScore());
				ps.setInt(4, customer.getStarTotalScore());
				ps.setDouble(5, customer.getStarAvgScore());
				ps.setString(6, customer.getStarGrade());
				ps.setString(7, customer.getCtId());

				updateReturnValue = ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("UPDATE ERROR : " + e.getMessage());
			} finally {
				try {
					if (ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					System.out.println("PrepareStatement Close Error" + e.getMessage());
				}
			}

			break;
		}

		return updateReturnValue;

	}

	// Order by
	public List<CustomerReview> selectOrderBy(int type) {
		List<CustomerReview> list = new ArrayList<CustomerReview>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectOrderByQuery = "select * from customer order by ";

		try {
			switch (type) {
			case NAME_ORDER:
				selectOrderByQuery += "name asc";
				break;
			case STAR_ORDER:
				selectOrderByQuery += "avg desc";
				break;
			case LIKE_ORDER:
				selectOrderByQuery += "likecount desc";
				break;
			default:
				System.out.println("OrderBy Type Error");
				return list;
			}

			ps = connection.prepareStatement(selectOrderByQuery);

			rs = ps.executeQuery(selectOrderByQuery);

			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}

			int rank = 0;

			while (rs.next()) {
				String ctId = rs.getString("id");
				String ctName = rs.getString("name");
				String ctPhone = rs.getString("phone");
				int ctAge = rs.getInt("age");
				String visitDate = rs.getString("visitdate");
				int visitCount = rs.getInt("visitcount");
				int tasteScore = rs.getInt("tasteScore");
				int cleanScore = rs.getInt("cleanScore");
				int serviceScore = rs.getInt("serviceScore");
				int starTotalScore = rs.getInt("total");
				double starAvgScore = rs.getDouble("avg");
				String stargrade = rs.getString("grade");
				String ctReview = rs.getString("review");
				int likeCount = rs.getInt("likecount");
				int reviewLength = rs.getInt("reviewLength");

				list.add(new CustomerReview(ctId, ctName, ctPhone, ctAge, visitDate, visitCount, tasteScore, cleanScore,
						serviceScore, starTotalScore, starAvgScore, stargrade, ctReview, likeCount, reviewLength));
			}
		} catch (Exception e) {
			System.out.println("SORT ERROR : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PrepareStatement Close Error " + e.getMessage());
			}
		}
		return list;

	}

	// Stats
	public List<CustomerReview> selectMaxMin(int statType, int type) {
		final int MAX = 1, MIN = 2;
		final int STAR_FIELD = 1, LIKE_FIELD = 2, REVIEW_FIELD = 3;
		List<CustomerReview> list = new ArrayList<CustomerReview>();
		Statement statement = null;
		ResultSet rs = null;
		String field = null;

		String selectMaxMinQuery = "SELECT * FROM customer WHERE ";
		try {
			switch (statType) {
			case STAR_FIELD:
				field = "Avg";
				break;
			case LIKE_FIELD:
				field = "Likecount";
				break;
			case REVIEW_FIELD:
				field = "Reviewlength";
				break;
			}

			switch (type) {
			case MAX:
				selectMaxMinQuery = selectMaxMinQuery + field + "= (SELECT getMax" + field + "()) ";
				break;
			case MIN:
				selectMaxMinQuery = selectMaxMinQuery + field + "= (SELECT getMin" + field + "()) ";
				break;
			default:
				System.out.println("Statistic Type Error");
				return list;
			}

			statement = connection.createStatement();

			rs = statement.executeQuery(selectMaxMinQuery);

			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}

			while (rs.next()) {
				String ctId = rs.getString("id");
				String ctName = rs.getString("name");
				String ctPhone = rs.getString("phone");
				int ctAge = rs.getInt("age");
				String visitDate = rs.getString("visitdate");
				int visitCount = rs.getInt("visitcount");
				int tasteScore = rs.getInt("tasteScore");
				int cleanScore = rs.getInt("cleanScore");
				int serviceScore = rs.getInt("serviceScore");
				int starTotalScore = rs.getInt("total");
				double starAvgScore = rs.getDouble("avg");
				String stargrade = rs.getString("grade");
				String ctReview = rs.getString("review");
				int likeCount = rs.getInt("likecount");
				int reviewLength = rs.getInt("reviewLength");

				list.add(new CustomerReview(ctId, ctName, ctPhone, ctAge, visitDate, visitCount, tasteScore, cleanScore,
						serviceScore, starTotalScore, starAvgScore, stargrade, ctReview, likeCount, reviewLength));

			}
		} catch (Exception e) {
			System.out.println("STATS MAX MIN ERROR : " + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				System.out.println("PrepareStatement Close Error " + e.getMessage());
			}
		}
		return list;
	}

	// DB Connection Close
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("Connection Close Error " + e.getMessage());
		}
	}

}
