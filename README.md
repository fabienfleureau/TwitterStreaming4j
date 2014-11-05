TwitterStreaming4j
==================

[ ![Codeship Status for fabienfleureau/TwitterStreaming4j](https://www.codeship.io/projects/ced4e630-46ad-0132-b588-6603b1374d14/status)](https://www.codeship.io/projects/45383)

This application can be used to stream twitter terms.

Usage:

```java
Authentication auth = new BasicAuth(twitterUsername, twitterPassword);
TwitterStreamingClient streamingClient = new TwitterStreamingClient(auth)
	.stream("twitter")
	.addConsumer(tweet -> System.out.println("New Tweet -> " + tweet));
streamingClient.connect();
Thread.sleep(10000);
streamingClient.stop();
```

This example will print tweets containing "twitter" during 10 seconds.

The method addConsumer take a [Java 8 Consumer](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html) in parameter.

You can add multiple consumers.


Alternative authentication:

```java
Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);

```
