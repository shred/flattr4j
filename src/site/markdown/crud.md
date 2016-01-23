# How to CRUD

With the API v2, it is now possible to control all the steps of a Thing's lifecycle. This how-to shows how to use the methods for creating, updating and deleting a Thing.

For this example, we will think about a blog software having Article objects. Each article is automatically published and updated at Flattr by the blog software. The article is stored in an `Article` class which implements `ThingId` and has a property called `thingId`. There is also a method `createSubmission()`, which creates a `Submission` object of its content.

```java
public class Article implements ThingId {
  private String thingId;

  @Override
  public String getThingId() {
    return thingId;
  }

  public void setThingId(String thingId) {
    this.thingId = thingId;
  }

  // more getters and setters

  public Submission createSubmission() {
    Submission sub = new Submission();
    // The Submission properties are populated here...
    return sub;
  }
}
```

With this class, it will be pretty easy to synchronize the article with Flattr.

## Creating

After publishing a new article, it is only required to create a `Submission` object, invoke `create()` and set the Thing ID in the article:

```java
Article article = // article to be created
FlattrService flattr = FlattrFactory.getInstance().createFlattrService(accessToken);

Submission sub = article.createSubmission();
ThingId thingId = flattr.create(sub);
article.setThingId(thingId);
```

The article is now registered with Flattr, and the Thing ID is stored in the article.

## Updating

When the article has been changed, the Flattr Thing should be updated to reflect the changes. The easiest way is to fetch the Article from Flattr, invoke `merge()` to merge the changes, and then update the Thing by invoking `update()`. Since the `Article` class implements `ThingId`, it can just be passed directly to the `FlattrService`.

```java
Article article = // article to be updated
FlattrService flattr = FlattrFactory.getInstance().createFlattrService(accessToken);

Thing thing = flattr.getThing(article);
thing.merge(article.createSubmission());
flattr.update(thing);
```

Instead of using `merge()`, it is also possible to invoke the `Thing` setters individually. However, it is much easier to generate a new `Submission` object and merge it with the `Thing`.

Note that the `Submission` url must be either `null`, or must _exactly_ match the `Thing`'s url. If this is uncertain, just create a `Submission` object, and then set the url to `null`:

```java
Submission sub = article.createSubmission();
sub.setUrl(null);
thing.merge(sub);
```

## Deleting

When the article is expired or deleted, it should be deleted from Flattr as well. Since `Article` implements `ThingId`, this is very easy, too:

```java
Article article = // article to be deleted
FlattrService flattr = FlattrFactory.getInstance().createFlattrService(accessToken);

flattr.delete(article);
```

That's all... The `Thing` has been deleted from the Flattr database, which concludes its lifecycle.
