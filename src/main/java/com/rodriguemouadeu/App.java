package com.rodriguemouadeu;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.rodriguemouadeu.helpers.Helper;

/**
 * Hello world!
 *
 */
public class App {

	private static MongoClient client;
	private static MongoClientOptions options;
	private static MongoDatabase database;
	private static MongoCollection<Document> collection;

	public static void main(String[] args) {
		System.out.println("Hello World!");

	}

	public MongoCollection<Document> getAllMatchingItems() {
		options = MongoClientOptions.builder().connectionsPerHost(50).build();
		client = new MongoClient("localhost", options);
		database = client.getDatabase("school");
		collection = database.getCollection("students");

		// db.students.aggregate([{$unwind: '$scores'}, {$match: {'scores.type': 'homework'}}, {$sort: {'_id': 1, 'scores.score': 1}}]);

		DBObject unwind = new BasicDBObject("$unwind", "$scores");
		DBObject match = new BasicDBObject("$match", new BasicDBObject("scores.type", "homework"));
		DBObject sort = new BasicDBObject("$sort", new Document("_id", 1).append("scores.score", 1));

		// run aggregation
		@SuppressWarnings("unchecked")
		AggregateIterable<Document> resultSet = collection
				.aggregate((List<? extends Bson>) Arrays.asList(unwind, match, sort));

		int counter = 0;
		Set<String> set = new HashSet<String>();
		MongoCursor<Document> iterator = resultSet.iterator();

		while (iterator.hasNext()) {
			Document currDoc = (Document) iterator.next();
			if (set.add(Integer.toString(currDoc.getInteger("_id")))) {
				Helper.printJson(currDoc);
				System.out.println(currDoc.toJson());
				System.out.println(currDoc.get("scores"));
				collection.updateOne(currDoc, new BasicDBObject("$pull", new Document("scores", currDoc.get("scores"))));
				System.out.println(currDoc.get("scores"));
				counter++;
			}
		}

		System.out.println("Number of students removed : " + counter);

		return collection;
	}
}