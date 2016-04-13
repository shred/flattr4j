# REST API

Flattr offers a REST API to give access to a Flattr user's account. For authentication, OAuth 2 is used.

_flattr4j_ offers a service for accessing the REST API, called `org.shredzone.flattr4j.FlattrService`. The easiest way to get an instance of the service is to use the `FlattrFactory`.

To get access, the [OAuth Procedure](./oauth.html) needs to be taken first. It results in an access token.

With this token, a `FlattrService` instance is created:

```java
String ACCESS_TOKEN = // user's access token

FlattrFactory factory = FlattrFactory.getInstance();
FlattrService service = factory.createFlattrService(ACCESS_TOKEN);
```

The service is now bound to the Flattr user and is ready to use. This is an example of how to get a list of the URLs of all things the current user submitted to Flattr:

```java
List<Thing> things = service.getMyThings();

for (Thing thing : things) {
  System.out.println(thing.getUrl());
}
```

`FlattrService` extends the Open Data API interface, so open calls are also available. Some of the open calls might even return more detailed information for authenticated users.

If you need to handle access to several Flattr accounts, remember that the `FlattrService` is always bound to a single Flattr user. To handle several users, you will need separate instances of `FlattrService` for each of them.
