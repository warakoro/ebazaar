package business.customersubsystem;

import business.externalinterfaces.Address;

public class AddressImpl implements Address {
	private String street;
	private String city;
	private String state;
	private String zip;
	private boolean isShippingAddress = false;
	private boolean isBillingAddress = false;
	
	public AddressImpl(String str, String c, String state, String zip, 
			boolean isShip, boolean isBill) {
		street=str;
		city=c;
		this.state=state;
		this.zip=zip;
		isShippingAddress = isShip;
		isBillingAddress = isBill;
	}
	public AddressImpl() {}
	public boolean isShippingAddress() {
		return isShippingAddress;
	}
	
	public boolean isBillingAddress() {
		return isBillingAddress;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + (isBillingAddress ? 1231 : 1237);
		result = prime * result + (isShippingAddress ? 1231 : 1237);
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		AddressImpl other = (AddressImpl) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (isBillingAddress != other.isBillingAddress)
			return false;
		if (isShippingAddress != other.isShippingAddress)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}
	
	
	
}
