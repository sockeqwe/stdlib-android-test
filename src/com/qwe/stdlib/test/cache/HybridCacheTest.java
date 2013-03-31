package com.qwe.stdlib.test.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.qwe.stdlib.cache.Cache;
import com.qwe.stdlib.cache.CacheEntry;
import com.qwe.stdlib.cache.HybridCache;
import com.qwe.stdlib.parser.JsonParserWriter;
import com.qwe.stdlib.parser.ParserWriter;
import com.qwe.stdlib.test.data.Address;
import com.qwe.stdlib.test.data.User;

public class HybridCacheTest extends TestCase {

	private HybridCache<String, Object> cache;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		String javaTmpDir = System.getProperty("java.io.tmpdir");
		File cacheDir = new File(javaTmpDir, "DiskLruCacheTest");
		cacheDir.mkdir();
		for (File file : cacheDir.listFiles()) {
			file.delete();
		}

		cache = new HybridCache<String, Object>(true, 1, true, 10000,
				cacheDir.getAbsolutePath(), 1);

	}

	public void testNoCollection() throws Exception {

		String mimeType = "application/json";
		JsonParserWriter json = new JsonParserWriter();
		cache.addParserWriter(mimeType, json);

		String name = "name";
		String details = "details";
		String street = "street";
		String zip = "zip";

		int testRuns = 20;
		ArrayList<User> insertedUsers = new ArrayList<User>(testRuns);

		for (int i = 0; i < testRuns; i++) {

			User u = new User();
			u.address = new Address();
			u.name = name + i;
			u.age = i;
			u.details = details + i;
			u.points = i;
			u.verified = (i % 2 == 0);
			u.address.houseNumber = i;
			u.address.street = street + i;
			u.address.zip = zip + i;

			// Save the object into the cache
			cache.put("user" + i, u, mimeType, Cache.NEW_ENTRY_PRIORITY_DEFAULT);
			insertedUsers.add(u);

			// Retrieve object from cache
			CacheEntry<Object> retrieved = cache.get("user" + i, User.class,
					ParserWriter.COLLECTION_TYPE_NONE);

			User rUser = retrieved.getValue();
			assertEquals(rUser, u);

		}

		//
		for (int i = testRuns - 1; i >= 0; i--) {

			CacheEntry<Object> e = cache.get("user" + i, User.class,
					ParserWriter.COLLECTION_TYPE_NONE);
			User u = e.getValue();

			assertEquals(u, insertedUsers.get(i));
		}

	}

	public void testList() throws Exception {

		String mimeType = "application/json";
		JsonParserWriter json = new JsonParserWriter();
		cache.addParserWriter(mimeType, json);

		String name = "name";
		String details = "details";
		String street = "street";
		String zip = "zip";

		int testRuns = 20;
		ArrayList<List<User>> inserted = new ArrayList<List<User>>(testRuns);

		for (int i = 0; i < testRuns; i++) {

			User u = new User();
			u.address = new Address();
			u.name = name + i;
			u.age = i;
			u.details = details + i;
			u.points = i;
			u.verified = (i % 2 == 0);
			u.address.houseNumber = i;
			u.address.street = street + i;
			u.address.zip = zip + i;

			ArrayList<User> list = new ArrayList<User>();
			list.add(u);
			// Save the object into the cache
			cache.put("users" + i, list, mimeType,
					Cache.NEW_ENTRY_PRIORITY_DEFAULT);

			inserted.add(list);

			// Retrieve object from cache
			CacheEntry<Object> retrieved = cache.get("users" + i, User.class,
					ParserWriter.COLLECTION_TYPE_LIST);

			List<User> rList = retrieved.getValue();
			assertEquals(list.get(0), rList.get(0));

		}

		for (int i = testRuns - 1; i >= 0; i--) {

			CacheEntry<Object> e = cache.get("users" + i, User.class,
					ParserWriter.COLLECTION_TYPE_LIST);
			List<User> list = e.getValue();

			assertEquals(list.get(0), inserted.get(i).get(0));
		}

	}
}
