package com.qwe.stdlib.test.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class User {
	public String name;

	public int age;

	public boolean verified;

	public double points;

	public String details;

	public Address address;

	public static String generateJson(String name, int age, double points,
			boolean verified, String street, int houseNumber, String zip)
			throws IOException {
		JsonFactory fac = new JsonFactory();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JsonGenerator gen = fac.createGenerator(out);

		gen.writeStartObject(); // {

		gen.writeStringField("name", name); // "name" : "mkyong"
		gen.writeNumberField("age", age); // "age" : 29
		gen.writeNumberField("points", points); // "age" : 29
		gen.writeBooleanField("verified", verified);
		gen.writeNullField("details");

		gen.writeObjectFieldStart("address");
		gen.writeStringField("street", street);
		gen.writeNumberField("houseNumber", houseNumber);
		gen.writeStringField("zip", zip);
		gen.writeEndObject();

		gen.writeEndObject();
		gen.close();

		return out.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof User))
			return false;

		if (other == this)
			return true;

		User o = (User) other;

		if (name.equals(o.name) && age == o.age && verified == o.verified
				&& points == o.points && details.equals(o.details))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return (name + age + verified + points + details + address.hashCode())
				.hashCode();
	}
}