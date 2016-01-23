# Spring

Usually `FlattrFactory` offers all necessary methods to easily use the Flattr API in your application. However _flattr4j_ also offers a special service factory that is specially made for Spring. It's advantage over `FlattrFactory` is that the application key and secret is preconfigured in the bean definition, so it is ready for use in the component class where it is injected. Since the application key and secret is already passed in at construction time of the factory bean, there is no need to provide those secrets to other beans.

A possible bean definition would look like this:

```xml
<bean id="flattrServiceFactory" class="org.shredzone.flattr4j.spring.DefaultFlattrServiceFactory">
  <constructor-arg>
    <bean class="org.shredzone.flattr4j.oauth.ConsumerKey">
      <constructor-arg index="0" value="...your-key..."/>
      <constructor-arg index="1" value="...your-secret..."/>
    </bean>
  </constructor-arg>
</bean>
```

In the component bean, the factory would be used like this:

```java
private FlattrServiceFactory flattrServiceFactory;

@Resource
public void setFlattrServiceFactory(FlattrServiceFactory flattrServiceFactory) {
  this.flattrServiceFactory = flattrServiceFactory;
}

public void invokeFlattr() {
  AccessToken accessToken = new AccessToken("......");
  FlattrService flattrService = flattrServiceFactory.getFlattrService(accessToken);
  User myself = flattrService.getMyself();
}
```

As you can see, there is no need to create a `ConsumerKey`, as it was already set in the bean definition.

## Preconfiguring an AccessToken

In a web application, you usually have to deal with a single `ConsumerKey` (as it is only one application), but multiple `AccessToken` (as there are multiple users using the application). You would usually get the consumer key from a configuration file or a bean definition, and the access token from a database. Anyhow if only a single Flattr account needs to be accessed, the `AccessToken` can also be pre-set in the bean definition:

```xml
<bean id="flattrServiceFactory" class="org.shredzone.flattr4j.spring.DefaultFlattrServiceFactory">
  <constructor-arg>
    <bean class="org.shredzone.flattr4j.oauth.ConsumerKey">
      <constructor-arg index="0" value="...your-key..."/>
      <constructor-arg index="1" value="...your-secret..."/>
    </bean>
  </constructor-arg>
  <constructor-arg>
    <bean class="org.shredzone.flattr4j.oauth.AccessToken">
      <constructor-arg index="0" value="...your-token..."/>
    </bean>
  </constructor-arg>
</bean>
```

The example business code is even simpler now:

```java
private FlattrServiceFactory flattrServiceFactory;

@Resource
public void setFlattrServiceFactory(FlattrServiceFactory flattrServiceFactory) {
  this.flattrServiceFactory = flattrServiceFactory;
}

public void invokeFlattr() {
  FlattrService flattrService = flattrServiceFactory.getFlattrService();
  User myself = flattrService.getMyself();
}
```

Note that even though a default `AccessToken` is preconfigured here, it is still possible to pass a different `AccessToken` instance to `getFlattrService()`.

## FlattrAuthenticator

`FlattrServiceFactory` also offers methods to get `FlattrAuthenticator` instances. The `FlattrAuthenticator` is already preconfigured with the application's `ConsumerKey` for your convenience.

```java
private FlattrServiceFactory flattrServiceFactory;

@Resource
public void setFlattrServiceFactory(FlattrServiceFactory flattrServiceFactory) {
  this.flattrServiceFactory = flattrServiceFactory;
}

public void invokeFlattr() {
  FlattrAuthenticator authenticator = flattrServiceFactory.getFlattrAuthenticator();
  authenticator.setScope(EnumSet.of(Scope.THING));
}
```

## Injecting the FlattrFactory

A different approach is to inject a `org.shredzone.flattr4j.FlattrFactory` instance. This is especially useful when there is no single, application wide consumer key, but the application has to deal with multiple consumer keys. The bean definition would be:

```xml
  <bean id="flattrFactory" class="org.shredzone.flattr4j.FlattrFactory" factory-method="getInstance"/>
```

An example:

```java
private FlattrFactory flattrFactory;

@Resource
public void setFlattrFactory(FlattrFactory flattrFactory) {
  this.flattrFactory = flattrFactory;
}

public void invokeFlattr() {
  ConsumerKey ck = new ConsumerKey("......", "......");
  AccessToken at = new AccessToken("......");
  FlattrService flattrService = flattrFactory.createFlattrService(ck, at);
  User myself = flattrService.getMyself();
}
```

However there is usually only one consumer key/secret pair per application. I suggest to use the `FlattrServiceFactory` bean whereever possible, as it prevents the key/secret pair to be passed around.
