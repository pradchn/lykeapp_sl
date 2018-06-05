package pt4.iss.nus.cloudapp.function;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import pt4.iss.nus.cloudapp.model.Picture;

public class PutPicture implements RequestHandler<Picture, Picture> {

	private DynamoDBMapper mapper;
	
	public PutPicture() {
		 AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
		mapper = new DynamoDBMapper(dynamoDb);
	}
	
	@Override
	public Picture handleRequest(Picture p, Context ctx) {
		LambdaLogger logger = ctx.getLogger();
		mapper.save(p);				
		Picture pp = p;
		logger.log("Picture: " + pp.getId());
		return pp;
	}

}