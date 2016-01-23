# How to use the flattr4j taglib

The _flattr4j-web_ module offers a tag library for adding Flattr buttons to JSP web sites.

## Adding the taglib

To use the taglib in your JSP, you will need to add `flattr4j-web.jar` (and `flattr4j-core.jar`) to your classpath first. After that, just add this line to the head of your JSP file:

```jsp
<%@ taglib prefix="f4j" uri="http://flattr4j.shredzone.org/taglib/f4j" %>
```

## Adding JavaScript based buttons

Flattr offers a [JavaScript API](https://flattr.com/support/integrate/js) for adding buttons to web pages. These buttons will show a counter for the clicks to the related content, and handle all the links to the flattr pages. Also these buttons allow to automatically register new Things with Flattr. On the downside, they require JavaScript, and will somewhat slow down the rendering of your page.

To add JavaScript buttons, you will have to add the JavaScript loader to the page's head first:

```jsp
  <head>
    <f4j:loader/>
  </head>
```

The `f4j:loader` tag offers some optional attributes. Please refer to the taglib javadoc and the [JavaScript API](https://flattr.com/support/integrate/js) to learn how to use them.

To add a Flattr button for a Thing that has already been registered with Flattr, just use a tag like this:

```jsp
  <f4j:button url="http://www.shredzone.org/wiki/flattr4j/Taglib"/>
```

The given URL must be exactly the same URL that was registered with Flattr. Otherwise Flattr will not be able to locate the Thing in its database.

To use a compact Flattr button, just add a `button` attribute:

```jsp
  <f4j:button url="http://www.shredzone.org/wiki/flattr4j/Taglib" button="compact"/>
```

## Adding static buttons

If you do not want to use JavaScript, you can also use static Flattr buttons. They do not need JavaScript and load very fast, but on the other hand they do not show a click counter and do not support auto-registration of new Things. For a static Flattr button, you first need to register your Thing's URL with Flattr (either manually or by the REST API).

Adding a static Flattr button is quite easy. You do not need to add a `f4j:loader` tag, but just place the button where you need it:

```jsp
  <f4j:static thing="https://flattr.com/thing/654321/Just-an-example-Thing"/>
```

*Note* that `f4j:button` needs a link to your own page, but `f4j:static` needs a link to your registered Thing's page at Flattr.

If you want to use the small badge, just add a `badge` attribute, too:

```jsp
  <f4j:static thing="https://flattr.com/thing/654321/Just-an-example-Thing" badge="small"/>
```

If you want to provide a Flattr badge image from your own server, you can also do so by adding a `badgeUrl` attribute:

```jsp
  <f4j:static thing="https://flattr.com/thing/654321/Just-an-example-Thing"
        badgeUrl="http://my.server/img/flattr-button.png"/>
```

This way there are no external resources loaded from a Flattr server.

## Adding style and other attributes

To add `style` or `class` attributes to the links, use the `style` or `styleClass` attributes, respectively:

```jsp
  <f4j:button url="http://www.shredzone.org/wiki/flattr4j/Taglib"
        style="padding:5px" styleClass="myFlattrClass"/>
  <f4j:static thing="https://flattr.com/thing/654321/Just-an-example-Thing"
        style="padding:5px" styleClass="myFlattrClass"/>
```

JavaScript buttons always use the "FlattrButton" class. It is suggested that you add a block like this to your CSS file, to hide the buttons on browsers with JavaScript disabled:

```css
.FlattrButton {
  display: none;
}
```

Other HTML attributes can be added to the link of JavaScript and static buttons by using the `f4j:attribute` tag:

```jsp
  <f4j:static thing="https://flattr.com/thing/654321/Just-an-example-Thing">
    <f4j:attribute name="target" value="_blank"/>
  </f4j:static>
```

## Registering Things by using JavaScript buttons

JavaScript buttons allow to register Things with Flattr automatically. To do so, just provide all the necessary information to the `f4j:button` tag (note the three different ways to add Flattr tags to the Thing: either as a tag attribute, a tag body, or as a `Collection` of Strings):

```jsp
  <f4j:button url="http://www.shredzone.org/wiki/flattr4j/Taglib"
        user="18525" category="text" language="en_US"
        title="flattr4j Taglib">
    This page describes how to use the flattr4j taglib to add Flattr buttons to your web site.
    <f4j:tag value="java"/>
    <f4j:tag>taglib</f4j:tag>
    <f4j:tag list="${standardTagList}"/>
  </f4j:button>
```

Please also see the `ButtonBuilder` JavaDoc for more details about Thing autosubmission.

The generated link will register the content with Flattr on the first click. If the content has already been registered, it will not be re-registered, so you can always use this structure, no matter if the Thing was already registered or not.

A different (and maybe cleaner) approach is to fill a `org.shredzone.flattr4j.model.Thing` object, and pass it to the tag along with the user ID of the respecitve author:

```jsp
  <f4j:button thing="${myThingObject}" user="18525"/>
```
