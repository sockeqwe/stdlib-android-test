package com.qwe.stdlib.test.data;

public class Address {

	public String street;

	public int houseNumber;

	public String zip;

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Address))
			return false;

		if (other == this)
			return true;

		Address o = (Address) other;

		if (street.equals(o.street) && houseNumber == o.houseNumber
				&& zip.equals(o.zip))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return (street + houseNumber + zip).hashCode();
	}
}
