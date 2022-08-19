package school;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Student implements Comparable {

	private String name;
	private int rollNo;
	private String schoolName;
	private Branch branch;
	// private Set<String> subjects;
	private Map<String, Integer> subjectMarks;
	private int totalMarks;

	public Student(String name, int rollNO, String school, Branch branch) {
		// TODO Auto-generated constructor stub

		this.name = name;
		this.branch = branch;
		this.rollNo = rollNO;
		this.schoolName = school;
		totalMarks = 0;
		//subjects = new HashSet<String>();
		subjectMarks = new HashMap<String, Integer>();
	}
	/*
	 * public void addSubject(String subject) { subjects.add(subject); }
	 * 
	 * public Set<String> getSubjects() { return subjects; }
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRollNo() {
		return rollNo;
	}

	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Map<String, Integer> getSubjectMArks() {
		return subjectMarks;
	}

	public void setSubjectMarks(String subject, int marks) {

		totalMarks += marks;
		subjectMarks.put(subject, marks);

	}

	@Override
	public int hashCode() {

		return name.length() + schoolName.length() + branch.getBranchName().length() + rollNo;
	}

	@Override
	public boolean equals(Object o) {

		if (o instanceof Student) {
			Student student = (Student) o;
			return student.name.equalsIgnoreCase(this.name) && (student.rollNo == this.rollNo)
					&& student.getSchoolName().equalsIgnoreCase(this.getSchoolName())
					&& student.getBranch().equals(this.branch);
		} else
			return false;

	}

	public int getTotalMarks() {
		return totalMarks;
	}

	@Override
	public int compareTo(Object o) {

		Student student2 = (Student) o;
		if (this.totalMarks > student2.totalMarks) {
			return -1;
		} else if (this.totalMarks < student2.totalMarks) {
			return +1;
		} else {

			Integer rollNoInteger = rollNo;
			return rollNoInteger.compareTo(student2.getRollNo());
		}

	}

}