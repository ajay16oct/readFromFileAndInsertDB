package driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import school.Branch;
import school.School;
import school.Student;

public class DatabaseConnection {

	static final String USER_NAME = "admin";
	static final String password = "*******";
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/School_Results_DB";
	static final String INSERT_INTO_SCHOOL = "insert into School(School_Name, Branch,Subject_Name)"
			+ " values (?, ?,?)";
	static final String INSERT_INTO_STUDENT = "INSERT INTO Student(Roll_NO,Student_Name,School_Name,Subject_Name,Marks,Branch) VALUES (?,?,?,?,?,?)";
	static final String GET_STUDENTS_FROM_STUDENT = "Select Roll_NO,Student_Name, School_Name, Subject_Name, Marks,Branch from Student";

	static PreparedStatement preparedStatement = null;

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER_NAME, password);

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		System.out.println("Connection Done...");
		return conn;
	}

	public static void insertIntoDatabase(List<School> schoolList, Collection<Student> students) throws SQLException {

		Connection connection = getConnection();

		try {
			System.out.println("Inserting Schools.....");
			insertSchools(connection, schoolList);
			System.out.println("Schools Inserted");
			System.out.println("Inserting students.....");
			insertStudents(connection, students);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			connection.close();
		}

	}

	public static void insertStudents(Connection connection, Collection<Student> students) throws SQLException {
		// TODO Auto-generated method stub

		int totalStudents = 0;
		preparedStatement = connection.prepareStatement(INSERT_INTO_STUDENT);
		for (Student student : students) {
			for (Map.Entry<String, Integer> subjectMark : student.getSubjectMArks().entrySet()) {

				preparedStatement.setInt(1, student.getRollNo());
				preparedStatement.setString(2, student.getName());
				preparedStatement.setString(3, student.getSchoolName());
				preparedStatement.setString(4, subjectMark.getKey());
				preparedStatement.setInt(5, subjectMark.getValue());
				preparedStatement.setString(6, student.getBranch().getBranchName());
				preparedStatement.execute();
				totalStudents++;
			}

		}
		System.out.println("Total Students entries inserted " + totalStudents);

	}

	public static void insertSchools(Connection connection, List<School> schoolList) throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement = connection.prepareStatement(INSERT_INTO_SCHOOL);

		for (School school : schoolList) {

			System.out.println("Inserting for school " + school.getName());
			for (Branch branch : school.getBranches()) {
				System.out.println("Inserting for the branch " + branch.getBranchName());

				for (String subject : branch.getSubjects()) {
					System.out.println("Inserting subject " + subject);
					preparedStatement.setString(1, school.getName());
					preparedStatement.setString(2, branch.getBranchName());
					preparedStatement.setString(3, subject);
					preparedStatement.execute();
				}

			}

		}

	}

	public static Collection<Student> getStudents() throws SQLException {
		Map<String, Student> uniqueIdStudentMap = new HashMap<String, Student>();
		Connection conn = getConnection();

		System.out.println("Getting Students from DB");
		int totalStudents = 0;
		try {
			preparedStatement = conn.prepareStatement(GET_STUDENTS_FROM_STUDENT);
			ResultSet rs = preparedStatement.executeQuery();
			Student student = null;
			while (rs.next()) {
				String uniqueId = "";
				int rollNo = rs.getInt("Roll_NO");
				String studentName = rs.getString("Student_Name");
				String schoolName = rs.getString("School_Name");
				String branch = rs.getString("Branch");
				String subjectName = rs.getString("Subject_Name");
				int marks = rs.getInt("Marks");

				uniqueId = rollNo + studentName + schoolName + branch;
				if (uniqueIdStudentMap.containsKey(uniqueId)) {
					student = uniqueIdStudentMap.get(uniqueId);

				} else {
					student = new Student(studentName, rollNo, schoolName, new Branch(branch));
					uniqueIdStudentMap.put(uniqueId, student);
					totalStudents++;

				}
				student.getBranch().addSubject(subjectName);
				student.setSubjectMarks(subjectName, marks);

			}
			System.out.println("Total Stundets Retrived from database are :" + totalStudents);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return uniqueIdStudentMap.values();
	}
}
