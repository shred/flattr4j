# A Peek behind the Curtain

Even though `flattr4j` offers a number of classes for accessing the Flattr API, internally it all boils down to a few classes that are used for the actual communication. This article gives a peek behind the curtain. It will show you how to fetch a Thing object by its ID, by using the communication classes directly. However, it is not the recommended way to use `flattr4j`. Also, it requires some in-depth knowlege of the [Flattr API](http://developers.flattr.net/).

The communication with the Flattr server is handled by exchanging JSON objects via HTTP. At first, we need a `FlattrConnector` instance for creating connections to the server:

```java
FlattrConnector connector = new FlattrConnector();
```

If an `AccessToken` is required for the call, it needs to be set in a second step:

```java
FlattrConnector connector = new FlattrConnector();
connector.setAccessToken(accessToken);
```

For open calls (like our call to get a Thing), the access token is not required, but it would not hurt to set it anyways.

Once we have a `FlattrConnector`, it can be reused for as many calls as we like to. For each call, a new `Connection` instance is created by the `FlattrConnector`. At this stage, we have to decide which HTTP method is required. This is documented in the Flattr API documentation. The HTTP method is passed as `RequestType` parameter to `create(RequestType type)`. For the GET method (which is used most of the time), a convenience method (`create()`) is also available. The generated `Connection` can be used for one API call only.

For fetching a Thing, we could use the `create()` method, but for the sake of this example, let's use the `RequestType` instead:

```java
Connection conn = connector.create(RequestType.GET);
```

The documentation says that for getting a Thing, the URL `https://api.flattr.com/rest/v2/things/:id` needs to be called, where `:id` is a placeholder for the thing ID. The base URL is already set in the `FlattrConnector`, so we only need to set a relative URL in the `Connection`:

```java
conn.call("things/:id");
```

Note that the placeholder (`:id`) is explicitly set in the call string. The corresponding value is set in the next step. `flattr4j` does the job of resolving the placeholders for us.

```java
conn.parameter("id", "164751");
```

We're done! We set up the URL and the parameter. Now it's time to place the actual call against the Flattr server. As we are only expecting a single result (a single Thing resource), `singleResult()` is invoked. It sends the request to the server, parses the JSON response and creates a `FlattrObject` for the result. If an error occures, a `FlattrException` is thrown instead.

```java
FlattrObject fo = conn.singleResult();
```

If we would expect multiple results (e.g. a list of Things), we'd invoke `result()` instead, which returns a `Collection` of `FlattrObject`. It is safe to use `result()` all the time. For API calls that return a single resource, it would still return a collection of a single entry. However it is more convenient to use `singleResult()` if only one result object is expected. `singleResult()` would also safely fail if more than one entry or nothing was returned.

The `FlattrObject` is basically a wrapper around the JSON result. It gives access to the properties within the JSON structure. Let's say we want to read the URL of the returned Thing. The Flattr REST API documentation says it is stored in a property called (not much surprising) "`url`":

```java
String url = fo.get("url");
System.out.println("Thing's URL is: " + url);
```

And that's it! We just placed a call against the Flattr REST API and read the result.

## Fine Tuning

The code example above works, but it is a little difficult to read. Most of the `Connection` methods return itself, so it is possible to chain multiple calls, like in a builder. The example above could also be written like this:

```java
FlattrConnector connector = new FlattrConnector();
FlattrObject fo = connector.create(RequestType.GET)
        .call("things/:id")
        .parameter("id", "164751")
        .singleResult();
```

`FlattrObject` gives access to the properties in the JSON result. But it's much easier to use `flattr4j`'s `Thing` instead. Luckily, it takes the `FlattrObject` as constructor parameter:

```java
Thing thing = new Thing(fo);
String url = thing.getUrl();
System.out.println("Thing's URL is: " + url);
```

## Other Methods

The `Connection` class is able to place all types of calls to the Flattr server (even the OAuth ones, which are a little magic).

For example, this code sends a `Submission` to the Flattr server. The submission is converted to JSON and sent in the body of a POST request. The returned JSON object contains the ID of the Thing that was created, which is read and displayed.

```java
Submission submission = // your Submission to be sent
FlattrConnector connector = new FlattrConnector();
FlattrObject fo = connector.create(RequestType.POST)
        .call("things")
        .data(submission.toFlattrObject())
        .singleResult();

System.out.println("The submission got Thing ID " + fo.get("id"));
```

To actually place a call, either `singleResult()` or `result()` must be invoked. However, some calls return no result. To check if they succeeded, just invoke `result()` and ignore the returned value. The next code example deletes a Thing. `result()` is only invoked to place the actual call and check if the server responded with an error. `singleResult()` cannot be used here, because it expects exactly one result resource.

```java
FlattrConnector connector = new FlattrConnector();
connector.create(RequestType.DELETE)
        .call("things/:id")
        .parameter("id", "1234567")
        .result();
```

## Checking Rate Limits

In order to avoid too much load, Flattr limits the number of calls that can be placed in a certain timespan. The maximum number of calls, and the number of calls that are left, are returned in special HTTP headers. To read them, pass a `RateLimit` instance to the `Connection`:

```java
RateLimit lastRateLimit = new RateLimit();
Connection conn = connector.create()
        .rateLimit(lastRateLimit);

// now set the other Connection parameters, place the call and read the result

System.out.println("Maximum number of calls: " + lastRateLimit.getLimit());
System.out.println("Number of calls left: " + lastRateLimit.getRemaining());
```

A `RateLimit` instance can be reused. It is set by every invocation of the `result()` or `singleResult()` methods.
