package fr.fabienfleureau.twitterstreaming4j;


import static org.junit.Assert.assertTrue;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

public class QueueConsumerTest {


	LinkedBlockingQueue<String> blockingQueue;
	
	@Before
	public void createQueue() {
		blockingQueue = new LinkedBlockingQueue<>();
	}
	
	@Test
	public void checkConsumingQueue() throws InterruptedException {
		AtomicBoolean booleanTrueWhenConsuming = new AtomicBoolean(false);
		QueueConsumer<String> queueConsumer = new QueueConsumer<>(blockingQueue);
		queueConsumer.addConsumer(str -> booleanTrueWhenConsuming.compareAndSet(false, true));
		Executors.newSingleThreadExecutor().execute(queueConsumer);
		assertTrue(!booleanTrueWhenConsuming.get());
		blockingQueue.put("switch atomic boolean");
		Thread.sleep(1000);
		assertTrue(booleanTrueWhenConsuming.get());
		queueConsumer.stop();
	}
	
	@Test
	public void canAddMultipleConsumers() throws InterruptedException {
		AtomicBoolean booleanTrueWhenConsuming = new AtomicBoolean(false);
		AtomicBoolean secondBooleanTrueWhenConsuming = new AtomicBoolean(false);
		QueueConsumer<String> queueConsumer = new QueueConsumer<>(blockingQueue);
		queueConsumer.addConsumer(str -> booleanTrueWhenConsuming.compareAndSet(false, true));
		Executors.newSingleThreadExecutor().execute(queueConsumer);
		assertTrue(!booleanTrueWhenConsuming.get());
		blockingQueue.put("switch atomic boolean");
		Thread.sleep(1000);
		assertTrue(booleanTrueWhenConsuming.get());
		queueConsumer.addConsumer(str -> secondBooleanTrueWhenConsuming.compareAndSet(false, true));
		assertTrue(!secondBooleanTrueWhenConsuming.get());
		blockingQueue.put("switch atomic boolean");
		Thread.sleep(1000);
		assertTrue(secondBooleanTrueWhenConsuming.get());
		
		queueConsumer.stop();
	}

}
