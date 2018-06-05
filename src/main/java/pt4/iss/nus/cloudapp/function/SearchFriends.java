package pt4.iss.nus.cloudapp.function;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import pt4.iss.nus.cloudapp.model.SearchRequest;
import pt4.iss.nus.cloudapp.model.SearchResponse;
import pt4.iss.nus.cloudapp.model.User;

public class SearchFriends implements RequestHandler<SearchRequest, SearchResponse> {

	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDb = new DynamoDB(client);
    static String tableName = "user";
	

	@Override
	public SearchResponse handleRequest(SearchRequest sreq, Context ctx) {
		LambdaLogger logger = ctx.getLogger();
		logger.log("User: " + sreq.getSearchStr());
		String sstr= sreq.getSearchStr();
		
		
		/*Table table = dynamoDb.getTable("user");
		Index index = table.getIndex("name-index");

		QuerySpec spec = new QuerySpec()
		    .withKeyConditionExpression("#uname = :v_name")
		    .withNameMap(new NameMap()
		        .with("#uname", "name"))
		    .withValueMap(new ValueMap()
		        .withString(":v_name",sstr));
		        

		ItemCollection<QueryOutcome> items = index.query(spec);
		logger.log("xxx: " + items.getAccumulatedItemCount());
		*/
		
		Table table = dynamoDb.getTable("user");

		ScanSpec scanSpec = new ScanSpec()//.withProjectionExpression("id, name, email")
	            .withFilterExpression("begins_with(#uname, :v_name)").withNameMap(new NameMap().with("#uname", "name"))
	            .withValueMap(new ValueMap().withString(":v_name", sstr));
		
	            ItemCollection<ScanOutcome> items = table.scan(scanSpec);
		
		SearchResponse response = new SearchResponse();
		for ( Item item : items) {
		    User usr = new User();
		    usr.setId(item.getString("id"));
		    usr.setName(item.getString("name"));
		    usr.setEmail(item.getString("email"));

		    response.getUser().add(usr);
	}
		return response;
	}
}
