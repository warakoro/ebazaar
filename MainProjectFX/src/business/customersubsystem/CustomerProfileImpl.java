package business.customersubsystem;

import business.externalinterfaces.CustomerProfile;

class CustomerProfileImpl implements CustomerProfile{
	private String firstName;
	private String lastName;
	private Integer custId;
	private boolean isAdmin;
	//CustomerProfile(){}
	CustomerProfileImpl(Integer custid, String firstName, String lastName, boolean isAdmin) {
		this.custId = custid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isAdmin = isAdmin;
	}
	CustomerProfileImpl(Integer custid, String fName, String lName) {
		this(custid, fName, lName, false);
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer id) {
		custId = id;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean b) {
		isAdmin = b;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((custId == null) ? 0 : custId.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (isAdmin ? 1231 : 1237);
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerProfileImpl other = (CustomerProfileImpl) obj;
		if (custId == null) {
			if (other.custId != null)
				return false;
		} else if (!custId.equals(other.custId))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}
	
	
}
