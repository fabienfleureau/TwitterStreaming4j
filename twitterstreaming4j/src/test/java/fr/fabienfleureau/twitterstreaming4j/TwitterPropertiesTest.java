package fr.fabienfleureau.twitterstreaming4j;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class TwitterPropertiesTest {
	
	@Test
	public void twitterPropertiesRead() {
		String consumerKey = null;
		String consumerSecret = null;
		String token = null;
		String secret = null;
		
		Properties prop = new Properties();
		try(InputStream inputStream = getClass().getResourceAsStream("/twitter.properties")) {
			prop.load(inputStream);
			consumerKey = prop.getProperty("consumerKey");
			consumerSecret = prop.getProperty("consumerSecret");
			token = prop.getProperty("token");
			secret = prop.getProperty("secret");
		} catch (Exception exception) { 
			consumerKey = System.getenv("consumerKey");
			consumerSecret = System.getenv("consumerSecret");
			token = System.getenv("token");
			secret = System.getenv("secret");
		}
		assertNotNull(consumerKey);
		assertNotNull(consumerSecret);
		assertNotNull(token);
		assertNotNull(secret);
	}
}
