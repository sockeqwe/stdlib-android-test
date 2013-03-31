package com.qwe.stdlib.test.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwe.stdlib.parser.JsonParserWriter;
import com.qwe.stdlib.test.data.User;

public class JsonParserTest extends TestCase {

	public void testParseSingleObject() throws Exception {

		String name = "Hannes";
		int age = 25;
		double points = 42.23;
		boolean verified = true;
		String street = "Reiterstr.";
		int houseNumber = 12;
		String zip = "93053";

		JsonParserWriter parser = new JsonParserWriter();
		String json = User.generateJson(name, age, points, verified, street,
				houseNumber, zip);
		InputStream in = new ByteArrayInputStream(json.getBytes());
		User u = (User) parser.parse(in, User.class,
				JsonParserWriter.COLLECTION_TYPE_NONE, null);

		assertEquals(u.name, name);
		assertEquals(u.age, age);
		assertEquals(u.points, points);
		assertEquals(u.verified, verified);
		assertNull(u.details);
		assertEquals(u.address.street, street);
		assertEquals(u.address.houseNumber, houseNumber);
		assertEquals(u.address.zip, zip);

	}

	public void testParseList() throws Exception {

		String name = "Hannes";
		int age = 25;
		double points = 42.23;
		boolean verified = true;
		String street = "Reiterstr.";
		int houseNumber = 12;
		String zip = "93053";

		int userEntries = 20;

		String json = "[";

		for (int i = 0; i < userEntries; i++) {
			if (i != 0)
				json += ",";

			json += User.generateJson(name + i, age + i, points + i, verified,
					street + i, houseNumber + i, zip + i);
		}

		json += "]";

		String n = JsonParserWriter.class.getName();
		String can = JsonParserWriter.class.getCanonicalName();

		JsonParserWriter parser = new JsonParserWriter();
		InputStream in = new ByteArrayInputStream(json.getBytes());

		List<User> users = (List<User>) parser.parse(in, User.class,
				JsonParserWriter.COLLECTION_TYPE_LIST, null);

		assertEquals(users.size(), userEntries);

		for (int i = 0; i < userEntries; i++) {
			User u = users.get(i);
			assertEquals(u.name, name + i);
			assertEquals(u.age, age + i);
			assertEquals(u.points, points + i);
			assertEquals(u.verified, verified);
			assertNull(u.details);
			assertEquals(u.address.street, street + i);
			assertEquals(u.address.houseNumber, houseNumber + i);
			assertEquals(u.address.zip, zip + i);
		}

		ObjectMapper mapper = new ObjectMapper();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		mapper.writeValue(out, users);

		String asJson = out.toString();
		// Wokrs well, only variable name ordering is changed, but it doesnt
		// matters

	}

}
