# OAuth Procedure on Android

For OAuth authentication on Android devices, the user is forwarded to the Flattr web site for authentication and accepting the scopes. When the user accepts the scopes, the browser forwards to a special URL, which is handled by your Android application. It reads the authentication code from the URL and completes the OAuth procedure by picking up the access token from the Flattr server.

This article describes how the Android authentication is accomplished. The `org.shredzone.flattr4j.oauth.AndroidAuthenticator` (since flattr4j v2.3) will help you with that task.

## 1. Register your App

First of all, you need to register your app with Flattr at their [registration form](https://flattr.com/apps/new). Describe your app in the _Title_, _Website_ and _Description_ fields. The description will later be presented to the user, so he'll know what app is actually asking for permission to access his Flattr account, and why.

The _Platform_ is set to _Android_, of course...

The _Callback URL_ is somewhat special. It is a concatenation of "`flattr4j://`", your app's namespace, and finally "`/authenticate`". For the sake of this example, let's assume that your app uses the namespace `com.example.myandroidapp`. The callback URL would then be:

```
flattr4j://com.example.myandroidapp/authenticate
```

Make sure your callback URL is assembled exactly like this and does not contain typos. If it is wrong, the Flattr server will very likely reject your authentication request.

Now save the application form. On the next page, a `KEY` and a `SECRET` code will be displayed. They will identify your app at the Flattr server. You will need them in the next steps, so keep them at hand.

You only need to register your app once.

## 2. Configure your `AndroidManifest.xml`

The `AndroidManifest.xml` needs to be configured for handling the callback URL. This is done by adding an `intent-filter` to your activity:

```xml
<intent-filter>
  <action android:name="android.intent.action.VIEW"/>
  <category android:name="android.intent.category.DEFAULT"/>
  <category android:name="android.intent.category.BROWSABLE"/>
  <data android:scheme="flattr4j" android:host="com.example.myandroidapp"/>
</intent-filter>
```

At the `android:host` attribute, enter the namespace that you used for the callback URL when you registered your app with Flattr. (Make sure it is only the namespace, not the entire callback URL.)

With this filter, your activity will be resumed when the user successfully authenticated at Flattr and accepted the requested scopes.

## 3. Open a browser for authentication

The `AndroidAuthenticator` offers useful methods for the OAuth procedure on Android. First of all, you create a new instance of this class. The constructor needs the host name, which is the value you set at the `android:host` attribute in the second step. Also it needs the `KEY` and `SECRET` from the first step. Usually it's a good idea to place them in a private constant or a configuration file.

```java
final String HOST_NAME  = "com.example.myandroidapp";
final String APP_KEY    = "tnVNz6mubX1VW....";
final String APP_SECRET = "j2V9fr2gOG3jE....";

AndroidAuthenticator auth = new AndroidAuthenticator(HOST_NAME, APP_KEY, APP_SECRET);
```

Next you set the scopes required for your application to work (e.g. `FLATTR` and `THING`):

```java
auth.setScope(EnumSet.of(Scope.FLATTR, Scope.THING));
```

Finally you create and start an `Intent` that opens a browser and directs the user to the Flattr site.

```java
Intent intent = auth.createAuthenticateIntent();
startActivity(intent);
```

The user is asked to authenticate at Flattr. After that, he will see your app's description (from the first step) and the scopes you requested. If he accepts, your app is resumed and the callback URL passed to it.

Note that you should not set a callback URL when using `AndroidAuthenticator`, as the class already took take care for it.

## 4. Retrieve the Access Token

When the user gave permission, your activity is resumed and the callback URL is passed in. You can take care for it by overriding the `onResume()` method.

You need an `AndroidAuthenticator` instance again. This can be either the instance from the previous step or a new instance, this is up to you.

The code for retrieving the Access Token may look like this (without exception handling):

```java
@Override
protected void onResume() {
    super.onResume();
    Uri uri = getIntent().getData();
    if (uri != null) {
        AndroidAuthenticator auth = new AndroidAuthenticator(HOST_NAME, APP_KEY, APP_SECRET);
        AccessToken token = auth.fetchAccessToken(uri);
        if (token != null) {
            // store the AccessToken
        }
    }
}
```

With the `AccessToken`, you can now use the `FlattrFactory` for creating a `FlattrService` that is bound to the authenticated user. The `AccessToken` is valid until the user revokes it on his Flattr account. You should store it somewhere in your app, so the user won't need to authentication again.

If you use `FlattrService` and it throws a `ForbiddenException`, it is a hint that the user revoked the permissions on his Flattr account. You could then delete the stored `AccessToken` and ask the user to authenticate again.
