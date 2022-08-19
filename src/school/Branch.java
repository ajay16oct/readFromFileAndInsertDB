package school;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Branch {

	private String branchName;
	private Set<String> subjects;

	public Branch(String branchName) {
		super();
		this.branchName = branchName;
		subjects = new HashSet<String>();
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Set<String> getSubjects() {
		return subjects;
	}

	public void setSubjects(Set<String> subjects) {
		this.subjects = subjects;
	}

	@Override
	public int hashCode() {

		return branchName.length();
	}

	@Override
	public String toString() {
		return branchName;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Branch) {
			Branch branch = (Branch) o;
			return branch.getBranchName().equalsIgnoreCase(this.getBranchName());
		} else
			return false;
	}

	public void addSubject(String subject) {
		// TODO Auto-generated method stub
		subjects.add(subject);

	}

}
