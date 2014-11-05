package fr.fabienfleureau.twitterstreaming4j;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class ConnectionTest {

	private OAuth1 oAuth1;

	@Before
	public void readTwitterProperties() {
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
		oAuth1 = new OAuth1(consumerKey, consumerSecret, token, secret);
	}
	
	@Test
	public void canBuildClient() {
		TwitterStreamingClient client = new TwitterStreamingClient(oAuth1);
		assertNotNull(client);
	}

	@Test
	public void clientCanConnect() {
		TwitterStreamingClient client = new TwitterStreamingClient(oAuth1);
		client.connect();
		assertTrue(!client.isDone());
	}
	
	@Test
	public void clientShouldNotStayConnectedIfNoKeywordSet() throws InterruptedException {
		TwitterStreamingClient client = new TwitterStreamingClient(oAuth1);
		client.connect();
		Thread.sleep(2000);
		assertTrue(client.isDone());
	}
	
	@Test
	public void clientShouldStayConnected() throws InterruptedException {
		TwitterStreamingClient client = new TwitterStreamingClient(oAuth1);
		client.stream("test");
		client.connect();
		Thread.sleep(2000);
		assertTrue(!client.isDone());
	}
	
	@Test
	public void clientCanStreamMultipleTerms() {
		TwitterStreamingClient client = new TwitterStreamingClient(oAuth1);
		client.stream("test");
		client.stream("twitter");
		client.connect();
		assertTrue (client.getEndpoint() instanceof StatusesFilterEndpoint);
		StatusesFilterEndpoint endpoint = (StatusesFilterEndpoint) client.getEndpoint();
		assertTrue(endpoint.getPostParamString().contains("test") && endpoint.getPostParamString().contains("twitter"));
	}
	
	@Test
	public void clientCanAttachConsumerToStream() {
		TwitterStreamingClient connection = new TwitterStreamingClient(oAuth1);
		connection.stream("test");
	}
	
}
