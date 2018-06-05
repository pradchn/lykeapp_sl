package pt4.iss.nus.cloudapp.function;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import pt4.iss.nus.cloudapp.model.User;

public class PostUser implements RequestHandler<User, User> {

	private DynamoDBMapper mapper;
	
	public PostUser() {
		 AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
		mapper = new DynamoDBMapper(dynamoDb);
	}
	
	@Override
	public User handleRequest(User c, Context ctx) {
		LambdaLogger logger = ctx.getLogger();
		mapper.save(c);		
		User r = c;
		logger.log("User: " + r.getId());
		return r;
	}

}