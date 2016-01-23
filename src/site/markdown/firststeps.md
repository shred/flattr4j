# First Steps

The following text describes how to use the flattr4j API for the most common use cases.

## Getting a FlattrService

In order to use the Flattr REST API, a `FlattrService` instance needs to be created. A description [can be found here](./restapi.html). To get access, the [OAuth Procedure](./oauth.html) needs to be taken first. It results in an access token.

## Languages and Categories

Flattr only accepts a certain set of Languages and Categories. There are two methods to retrieve them:

```java
List<Language> languages = flattrService.getLanguages();
List<Categories> categories = flattrService.getCategories();
```

These lists are publically available, so they can be retrieved by `OpenService`. The advantage of `OpenService` is that no [OAuth Procedure](./oauth.html) is required.

## Accessing Things and Users

Things and Users can be accessed by `getThing()` and `getUser()`, respectively. These methods can be used when only the Thing's or User's ID is known, but also when more details about a Thing or User is needed. In order not to clutter the service API, these methods use an ID interface as parameter, so all objects that implement an ID interface can be passed in. But let me give some examples.

The following line will fetch a Thing with ID 12345.

```java
Thing thing = flattrService.getThing(Thing.withId("12345"));
```

`Thing.withId()` returns a plain object that only implements the `ThingId` interface. It can be used on all methods that take a `ThingId` when only the ID of a Thing is known.

Now, the following code will submit a click on a Thing with ID 12345:

```java
Thing thing = flattrService.getThing(Thing.withId("12345"));
flattrService.click(thing);
```

A `Thing` object implements the `ThingId` interface itself, so it can be passed to methods that require a `ThingId` as argument, too. Alternatively, instead of the two lines above it is possible to submit the click just by a single line:

```java
flattrService.click(Thing.withId("12345"));
```

There are also ID interfaces for `User`, `Category` and `Language` objects which can be used in a similar way.

## Submitting Things

Of course, it is also possible to submit new Things by using the `FlattrService`. To do so, a `Submission` object needs to be created and populated:

```java
Submission sub = new Submission();
sub.setUrl("http://www.shredzone.org/projects/flattr4j/wiki/First_Steps");
sub.setTitle("First Steps");
sub.setDescription("How to use flattr4j");
sub.addTag("flattr4j");
sub.setCategory(Category.withId("text"));
sub.setLanguage(Language.withId("en_UK"));
```

The new Thing is submitted by calling `create()`:

```java
ThingId newThingId = flattrService.create(sub);
```

As you may have noticed, the `setCategory()` and `setLanguage()` methods accept `CategoryId` and `LanguageId` interfaces, respectively. Instead of using the `withId()` construction, it is also possible to pass in an object that was returned by `getCategories()` or `getLanguages()`.

## More fun with ID interfaces

Many other model objects of flattr4j also implement ID interfaces. For example, it's very easy to put an extra click on all the Things that have been flattred by the logged-in user, because `Flattr` implements the `ThingId` interface for the Thing that was flattred:

```java
for (Flattr flattr : flattrService.getMyFlattrs()) {
    flattrService.click(flattr);
}
```

But there are also ways that are not that obvious. For example, a `Thing` implements the `UserID` interface for the user who submitted the Thing. The following lines will get details of the `User` who submitted the Thing with ID 12345:

```java
Thing thing = flattrService.getThing(Thing.withId("12345"));
User user = flattrService.getUser(thing);
```

It is also possible to implement ID interfaces in own classes, like database entities. This way, the own classes can be passed to the service methods as well. For example, to get a `Thing` object for a blog article that is stored in a database, the code would be something like:

```java
BlogArticle article = blogService.getArticle(5678); // BlogArticle implements ThingId
Thing thing = flattrService.getThing(article);
```
