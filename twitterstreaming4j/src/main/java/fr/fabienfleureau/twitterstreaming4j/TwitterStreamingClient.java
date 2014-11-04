package fr.fabienfleureau.twitterstreaming4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import com.twitter.hbc.BasicReconnectionManager;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.StatsReporter.StatsTracker;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;

public class TwitterStreamingClient implements Client {

	private static final int MAX_TWEETS_CAPACITY = 10000;

	private Authentication authentication;
	
	private List<String> terms;
	private LinkedBlockingQueue<String> queue;
	private QueueConsumer<String> queueConsumer;
	
	private Client clientDelegate;

	public TwitterStreamingClient(Authentication authentication) {
		this.authentication = authentication;
		this.terms = new ArrayList<>();
		this.queue = new LinkedBlockingQueue<String>(MAX_TWEETS_CAPACITY);
		this.queueConsumer = new QueueConsumer<>(queue);
	}

	public TwitterStreamingClient stream(String term) {
		terms.add(term);
		return this;
	}
	
	public TwitterStreamingClient addConsumer(Consumer<String> stringConsumer) {
		queueConsumer.addConsumer(stringConsumer);
		return this;
	}
	
	public void connect() {
		clientDelegate = new ClientBuilder()
			.authentication(authentication)
			.endpoint(new StatusesFilterEndpoint().trackTerms(terms))
			.hosts(new HttpHosts(Constants.STREAM_HOST))
			.processor(new StringDelimitedProcessor(queue))
			.reconnectionManager(new BasicReconnectionManager(10))
			.build();
		Executors.newSingleThreadExecutor().execute(queueConsumer);
		clientDelegate.connect();
	}

	public void reconnect() {
		clientDelegate.reconnect();
	}

	public void stop() {
		clientDelegate.stop();
		queueConsumer.stop();
	}

	public void stop(int waitMillis) {
		clientDelegate.stop(waitMillis);
		queueConsumer.stop();
	}

	public boolean isDone() {
		return clientDelegate.isDone();
	}

	public String getName() {
		return clientDelegate.getName();
	}

	public StreamingEndpoint getEndpoint() {
		return clientDelegate.getEndpoint();
	}

	public StatsTracker getStatsTracker() {
		return clientDelegate.getStatsTracker();
	}
}
