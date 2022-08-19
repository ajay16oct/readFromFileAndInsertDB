package school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class School {

	private String name;
	private List<Student> students;
	private Set<Branch> branches;

	private Map<Branch, List<Student>> branchStudentMap;

	public Set<Branch> getBranches() {
		return branches;
	}

	public Map<Branch, List<Student>> getBranchStudentMap() {
		return branchStudentMap;
	}

	public School(String name) {
		this.name = name;
		students = new ArrayList<>();
		// subjects = new HashSet<>();
		branchStudentMap = new HashMap<Branch, List<Student>>();
		branches = new HashSet<Branch>();

	}

	public String getName() {
		return name;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void addBranch(Branch branch) {
		branches.add(branch);
	}
	/*
	 * public void addSubject(String subject) { subjects.add(subject);
	 * 
	 * }
	 */

	/*
	 * public Set<String> getSubjects() { return subjects; }
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof School) {
			School school = (School) o;
			return this.name.equalsIgnoreCase(school.getName());
		} else
			return false;
	}

	@Override
	public int hashCode() {

		return this.name.length();
	}

	public void addStudent(Student student) {

		if ((student instanceof Student) && !students.contains(student)) {

			students.add(student);
			branches.add(student.getBranch());

		}
		List<Student> studentList;
		if (branchStudentMap.containsKey(student.getBranch())) {

			studentList = branchStudentMap.get(student.getBranch());
			studentList.add(student);
		} else {
			studentList = new ArrayList<Student>();
			studentList.add(student);
			branchStudentMap.put(student.getBranch(), studentList);
		}
	}
}