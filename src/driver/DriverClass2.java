package driver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import school.Branch;
import school.School;
import school.Student;

public class DriverClass2 {

	public static void main(String[] args) throws IOException {

		readFromFileAndInsertIntoDb();
		readFromDB();

	}

	private static void readFromDB() {

		Collection<Student> students = null;
		try {
			students = DatabaseConnection.getStudents();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<School> schoolList = setStudentsInSchool(students);
		printSchoolStudents(schoolList);

	}

	public static void readFromFileAndInsertIntoDb() throws IOException {

		System.out.println("Reading Students From File");
		Collection<Student> students = getStudentsfromFile();

		List<School> schoolList = setStudentsInSchool(students);
		try {
			System.out.println("Inserting Schools and Students into DB");
			DatabaseConnection.insertIntoDatabase(schoolList, students);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void printSchoolStudents(List<School> schoolList) {

		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter("Myoutput_final.csv");
			pw = new PrintWriter(fw);
			for (School school : schoolList) {

				String schoolName = school.getName();
				pw.println(schoolName);
				Map<Branch, List<Student>> branchStudentMap = school.getBranchStudentMap();

				for (Map.Entry<Branch, List<Student>> entrySet : branchStudentMap.entrySet()) {

					Branch branch = entrySet.getKey();
					pw.println(branch.getBranchName());

					List<Student> students = entrySet.getValue();
					Collections.sort(students);
					for (Student s : students) {
						pw.print(s.getName());
						pw.print(",");
						pw.print(s.getRollNo());
						pw.print(",");
						pw.print(s.getTotalMarks());
						pw.println();
					}
				}
			}

			pw.println("Branch Toppers");

			for (School school : schoolList) {
				String schoolName = school.getName();
				Map<Branch, List<Student>> branchToStudentMap = school.getBranchStudentMap();
				for (Map.Entry<Branch, List<Student>> entry : branchToStudentMap.entrySet()) {
					String branchName = entry.getKey().getBranchName();
					List<Student> studentList = entry.getValue();
					Collections.sort(studentList);
					Student topStudent = studentList.get(0);
					pw.print(schoolName);
					pw.print(",");
					pw.print(branchName);
					pw.print(",");
					pw.print(topStudent.getName());
					pw.print(",");
					pw.print(topStudent.getRollNo());
					pw.print(",");
					pw.print(topStudent.getTotalMarks());
					pw.println();
				}

			}
			pw.println("School Toppers");

			TreeMap<Integer, List<Student>> marksToToppersMap = new TreeMap<Integer, List<Student>>(
					new Comparator<Integer>() {

						@Override
						public int compare(Integer o1, Integer o2) {

							return o2.compareTo(o1);
						}

					});
			for (School school : schoolList) {
				List<Student> allStudentsInSchool = school.getStudents();
				String schoolName = school.getName();
				Collections.sort(allStudentsInSchool);
				Student topperInSchool = allStudentsInSchool.get(0);

				if (!marksToToppersMap.containsKey(topperInSchool.getTotalMarks())) {
					List<Student> studentList = new ArrayList<>();
					studentList.add(topperInSchool);
					marksToToppersMap.put(topperInSchool.getTotalMarks(), studentList);
				} else {
					List<Student> listOfStudent = marksToToppersMap.get(topperInSchool.getTotalMarks());
					listOfStudent.add(topperInSchool);
				}
				pw.print(schoolName);
				pw.print(",");
				pw.print(topperInSchool.getBranch().getBranchName());
				pw.print(",");
				pw.print(topperInSchool.getName());
				pw.print(",");
				pw.print(topperInSchool.getRollNo());
				pw.print(",");
				pw.print(topperInSchool.getTotalMarks());
				pw.println();
			}

			pw.println("OverAll Topper");
			Map.Entry<Integer, List<Student>> overAllToppers = marksToToppersMap.firstEntry();

			for (Student s : overAllToppers.getValue()) {
				pw.print(s.getSchoolName());
				pw.print(",");
				pw.print(s.getBranch().getBranchName());
				pw.print(",");
				pw.print(s.getName());
				pw.print(",");
				pw.print(s.getRollNo());
				pw.print(",");
				pw.print(s.getTotalMarks());
				pw.println();
			}

		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			pw.flush();
			pw.close();
		}

	}

	public static List<School> setStudentsInSchool(Collection<Student> students) {

		System.out.println("Setting Student Into Schools");
		List<School> schools = new ArrayList<School>();
		Iterator<Student> iterator = students.iterator();
		while (iterator.hasNext()) {
			Student student = iterator.next();
			School school = new School(student.getSchoolName());

			if (!schools.contains(school))
				schools.add(school);

			else {
				int index = schools.indexOf(school);
				school = schools.get(index);
			}
			school.addStudent(student);
			school.addBranch(student.getBranch());

		}
		System.out.println("Schools Populated with students");
		return schools;

	}

	public static Map<School, List<Student>> mapStudentsToSchool(Set<Student> studentSet) {

		Map<School, List<Student>> mapOfSchoolTOStudents = new HashMap<School, List<Student>>();
		Iterator<Student> iterator = studentSet.iterator();

		while (iterator.hasNext()) {
			Student s = iterator.next();
			School school = new School(s.getSchoolName());
			List<Student> listOfStudent = null;
			if (!mapOfSchoolTOStudents.containsKey(school)) {
				listOfStudent = new ArrayList<Student>();
				listOfStudent.add(s);
				mapOfSchoolTOStudents.put(school, listOfStudent);
				school.addStudent(s);

			} else {

				listOfStudent = mapOfSchoolTOStudents.get(school);
				listOfStudent.add(s);
			}
		}

		return null;
	}

	public static Collection<Student> getStudentsfromFile() throws IOException {
		FileReader fileReader = null;

		Map<String, Student> uniqueIdToStudentMap = null;
		try {

			fileReader = new FileReader("/home/ajay/Downloads/input.csv");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			uniqueIdToStudentMap = new HashMap<String, Student>();

			String str = bufferedReader.readLine();
			while ((str = bufferedReader.readLine()) != null) {
				Student tempStudent = null;
				String uniqueId = "";
				String[] arrayOfString = str.split(",");

				int rollNo = Integer.parseInt(arrayOfString[0]);
				uniqueId += rollNo;
				String studentName = arrayOfString[1];
				uniqueId += studentName;
				String schoolName = arrayOfString[2];
				uniqueId += schoolName;
				String branchStr = arrayOfString[3];

				uniqueId += branchStr;
				String subject = arrayOfString[4];
				int marks = Integer.parseInt(arrayOfString[5]);

				if (!uniqueIdToStudentMap.containsKey(uniqueId)) {
					Branch branch = new Branch(branchStr);
					branch.addSubject(subject);
					tempStudent = new Student(studentName, rollNo, schoolName, branch);
					uniqueIdToStudentMap.put(uniqueId, tempStudent);

				} else {

					tempStudent = uniqueIdToStudentMap.get(uniqueId);
					tempStudent.getBranch().addSubject(subject);

				}
				tempStudent.setSubjectMarks(subject, marks);

			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			fileReader.close();
		}

		System.out.println("Reading of student done from file");
		return uniqueIdToStudentMap.values();

	}

}