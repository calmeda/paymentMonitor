package ph.almeda.payment.monitor.load;

import java.util.ArrayList;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import ph.almeda.payment.monitor.model.Policy;

public class MongoDBLoader {
	
	MongoClient mongoClient = null;

	public MongoDatabase getDatabase() {
		// To connect to mongodb server
		mongoClient = new MongoClient();
		// Now connect to your databases
		System.out.println("Connect to database successfully");
		return mongoClient.getDatabase("SunlifePoliciesSample");
	}
	
	public void insertOneGson(Policy ps) {
		MongoDatabase db = getDatabase();

		Gson gson = new Gson();
		String json = gson.toJson(ps);
		Document doc = Document.parse(json);
		db.getCollection("PaymentDueDate").insertOne(doc);
	}
	
	public void insertManyGson(ArrayList<Policy> psList) {
		MongoDatabase db = getDatabase();
		ArrayList<Document> docList = new ArrayList<Document>();
		for (Policy ps : psList)
		{
			Gson gson = new Gson();
			String json = gson.toJson(ps);
			Document doc = Document.parse(json);
			docList.add(doc);
		}
		db.getCollection("PaymentDueDate").insertMany(docList);
	}



}
