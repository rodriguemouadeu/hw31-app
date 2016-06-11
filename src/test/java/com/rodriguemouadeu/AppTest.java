package com.rodriguemouadeu;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.MongoCollection;

/**
 * Unit test for simple App.
 */
public class AppTest {

	private static int NUMBER_OF_MATCHING_ITEMS = 100;

	private App app;

	@Before
	public void setup() {
		app = new App();
	}

	@Test
	public void getAllMatchingItemsTest() {
		assertThat(app, is(notNullValue()));

		MongoCollection<Document> docCollection = app.getAllMatchingItems();
		assertThat(docCollection, is(notNullValue()));
	}
}
