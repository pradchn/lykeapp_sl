package pt4.iss.nus.cloudapp.function;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import pt4.iss.nus.cloudapp.model.FeedRequest;
import pt4.iss.nus.cloudapp.model.FeedResponse;
import pt4.iss.nus.cloudapp.model.Picture;
import pt4.iss.nus.cloudapp.model.User;

public class GetFeed implements RequestHandler<FeedRequest, FeedResponse> {

	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	static DynamoDB dynamoDb = new DynamoDB(client);

	private DynamoDBMapper mapper;

	public GetFeed() {
		AmazonDynamoDB dynamoDb1 = AmazonDynamoDBClientBuilder.standard().build();
		mapper = new DynamoDBMapper(dynamoDb1);
	}

	@Override
	public FeedResponse handleRequest(FeedRequest freq, Context ctx) {
		LambdaLogger logger = ctx.getLogger();
		logger.log("User: " + freq.getUserId());
		String uid = freq.getUserId();
		User user = new User();
		user = mapper.load(User.class, uid);
		Set<String> friendlist = user.getFriends();
		if (friendlist == null)
		{
			friendlist = new HashSet<>();
		}
		friendlist.add(uid);
		Iterator<String> iter = friendlist.iterator();

		Table table = dynamoDb.getTable("Picture");
		Index index = table.getIndex("userid-index");
		FeedResponse response = new FeedResponse();
		while (iter.hasNext()) {

			QuerySpec spec = new QuerySpec().withKeyConditionExpression("#uid = :v_userId")
					.withNameMap(new NameMap().with("#uid", "userId"))
					.withValueMap(new ValueMap().withString(":v_userId", iter.next()));

			ItemCollection<QueryOutcome> items = index.query(spec);
			logger.log("xxx: " + items.getAccumulatedItemCount());

			for (Item item : items) {
				Picture pic = new Picture();
				pic.setId(item.getString("id"));
				pic.setUserId(item.getString("userId"));
				pic.setCreatedTime(item.getLong("createdTime"));
				pic.setLikes(item.getInt("likes"));
				pic.setPicUrl(item.getString("picUrl"));
				pic.setTags(item.getString("tags"));
				response.getPics().add(pic);
			}
		}
		return response;
	}
}
