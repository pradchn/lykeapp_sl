package pt4.iss.nus.cloudapp.function;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import pt4.iss.nus.cloudapp.model.Picture;
import pt4.iss.nus.cloudapp.util.MLUtil;

public class PostPicture implements RequestHandler<Picture, Picture> {

	private DynamoDBMapper mapper;
	
	public PostPicture() {
		 AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
		mapper = new DynamoDBMapper(dynamoDb);
	}
	
	@Override
	public Picture handleRequest(Picture p, Context ctx) {
		LambdaLogger logger = ctx.getLogger();
		String photo =p.getPicUrl();
		String bucket = System.getenv("S3_IMAGE_BUCKET");
		//String tags = MLUtil.getLabels("sample.jpg", "pt4-iss-nus-bucket-image");
		String tags = MLUtil.getLabels("public/"+photo, bucket);
		p.setTags(tags);
		p.setCreatedTime(new Date().getTime());
		logger.log("From ML::"+tags+"::"+p.getTags());
		mapper.save(p);		
		Picture pp = p;
		logger.log("Picture: " + pp.getId());
		return pp;
	}

}