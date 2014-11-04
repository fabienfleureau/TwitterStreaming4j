package fr.fabienfleureau.twitterstreaming4j;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class TwitterPropertiesTest {
	
	@Test
	public void twitterPropertiesRead() throws IOException {
		Properties prop = new Properties();
		try(InputStream inputStream = getClass().getResourceAsStream("/twitter.properties")) {
			prop.load(inputStream);
		}
		assertNotNull(prop.getProperty("consumerKey"));
		assertNotNull(prop.getProperty("consumerSecret"));
		assertNotNull(prop.getProperty("token"));
		assertNotNull(prop.getProperty("secret"));
	}
}
