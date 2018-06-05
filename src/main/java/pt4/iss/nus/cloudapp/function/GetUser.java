package pt4.iss.nus.cloudapp.function;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import pt4.iss.nus.cloudapp.model.User;

public class GetUser implements RequestHandler<User, User> {

	private DynamoDBMapper mapper;
	
	public GetUser() {
		 AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
		mapper = new DynamoDBMapper(dynamoDb);		
	}
	
	@Override
	public User handleRequest(User user, Context ctx) {
		LambdaLogger logger = ctx.getLogger();
		logger.log("User: " + user.getId());
		user = mapper.load(User.class, user.getId());
		return user;
	}

}
