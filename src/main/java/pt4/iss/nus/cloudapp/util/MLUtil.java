package pt4.iss.nus.cloudapp.util;


import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.Celebrity;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesRequest;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesResult;
import com.amazonaws.services.rekognition.model.S3Object;

public class MLUtil {

   public static String getLabels(String photo,String bucket)  {
	   
	   StringBuilder sb = new StringBuilder();

/*
      AWSCredentials credentials;
      try {
          credentials = new ProfileCredentialsProvider("AdminUser").getCredentials();
      } catch(Exception e) {
         throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
          + "Please make sure that your credentials file is at the correct "
          + "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
      }
*/
      AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
    	         .standard()
    	 //        .withRegion(Regions.US_WEST_2)
    	   //      .withCredentials(new AWSStaticCredentialsProvider(credentials))
    	         .build();

      DetectLabelsRequest request = new DetectLabelsRequest()
    		  .withImage(new Image()
    		  .withS3Object(new S3Object()
    		  .withName(photo).withBucket(bucket)))
    		  .withMaxLabels(3)
    		  .withMinConfidence(75F);

      try {
         DetectLabelsResult result = rekognitionClient.detectLabels(request);
         List <Label> labels = result.getLabels();

         System.out.println("Detected labels for " + photo);
         for (Label label: labels) {
            System.out.println(label.getName() + ": " + label.getConfidence().toString());
            sb.append(label.getName());
            sb.append(",");
         }
         
         //--celebrity identification
         RecognizeCelebritiesRequest crequest = new RecognizeCelebritiesRequest()
          		  .withImage(new Image()
          	    	  .withS3Object(new S3Object()
          	 		  .withName(photo).withBucket(bucket)));
                 

              System.out.println("Looking for celebrities in image " + photo + "\n");

              RecognizeCelebritiesResult cresult=rekognitionClient.recognizeCelebrities(crequest);

              //Display recognized celebrity information
              List<Celebrity> celebs=cresult.getCelebrityFaces();
              System.out.println(celebs.size() + " celebrity(s) were recognized.\n");
              sb.append("Celebrities:: ");
              for (Celebrity celebrity: celebs) {
                  System.out.println("Celebrity recognized: " + celebrity.getName());
                  System.out.println("Celebrity ID: " + celebrity.getId());
                  sb.append(celebrity.getName());
                  sb.append(",");
               }
             // sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(","), "");
              //sb.substring(0, sb.length()-1);
 
         //--
         
      } catch(AmazonRekognitionException e) {
         e.printStackTrace();
      }
      return sb.toString();
   }
   
}