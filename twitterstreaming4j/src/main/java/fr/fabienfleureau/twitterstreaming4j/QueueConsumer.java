package fr.fabienfleureau.twitterstreaming4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueueConsumer<T> implements Runnable {
	
	private static final int POLLING_DELAY = 5000;

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private List<Consumer<T>> consumers = new CopyOnWriteArrayList<>();
	private AtomicBoolean stop = new AtomicBoolean(false);
	private BlockingQueue<T> blockingQueue;
	
	public QueueConsumer(BlockingQueue<T> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}
	
	public void addConsumer(Consumer<T> consumer) {
		consumers.add(consumer);
	}
	
	public void stop() {
		stop.compareAndSet(false, true);
	}
	
	@Override
	public void run() {
		while(!stop.get()) {
			try {
				Optional<T> optional = Optional.ofNullable(blockingQueue.poll(POLLING_DELAY, TimeUnit.MILLISECONDS));
				optional.ifPresent(head -> consumers.forEach(consumer -> consumer.accept(head)));
			} catch (InterruptedException interruptedException) {
				logger.log(Level.SEVERE, interruptedException.getMessage(), interruptedException);
			}
		}
		logger.info("Stopping " + this);
	}

}
