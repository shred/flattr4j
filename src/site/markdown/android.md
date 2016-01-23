# How to use flattr4j on Android devices

For running _flattr4j_ v2 on Android, at least Android 3.0 "Honeycomb" is needed. Lower API levels do not work because the Flattr server makes use of SNI. I recommend to use at least _flattr4j_ v2.12, especially if you run into HTTPS certificate issues.

The `org.apache.http` package has been marked deprecated since Android 5.1, and will be removed on Android "M". I strongly recommend to use _flattr4j_ v2.12 or higher on Android, especially when you target Android "M".

## Requirements

In order to use flattr4j on Android, you need to copy the `flattr4j-core.jar` to your project's `libs` directory.

flattr4j also needs the _json.org_ library. However, it is already provided by Android and does not need to be installed separately. So, you only need to copy the `flattr4j-core.jar` to your project.

Since flattr4j connects to the Flattr API via HTTP, your application needs `INTERNET` permission to run.

## Usage

The flattr4j core API can be used without restrictions on Android, including `FlattrService` and `FlattrAuthenticator`.

## Gradle

Make sure that `json` is excluded when you use build tools with dependency management.

If you are using Gradle for Android development (e.g. Android Studio), add this dependency to your `build.gradle` file:

```groovy
dependencies {
    compile('org.shredzone.flattr4j:flattr4j-core:2.12') {
        exclude module: 'json'
    }
}
```

## Maven

If you are using Maven for Android development, add these dependency lines:

```xml
<dependency>
  <groupId>org.shredzone.flattr4j</groupId>
  <artifactId>flattr4j-core</artifactId>
  <version>2.12</version>
  <scope>compile</scope>
  <exclusions>
    <exclusion>
      <groupId>org.json</groupId>
      <artifactId>json</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```

## Dependencies before v2.12

_flattr4j_ up to v2.11 also requires Apache HttpComponents 4. If you use an older version of _flattr4j_ in your project, make sure to exclude `org.apache.httpcomponents:httpcore` and `org.apache.httpcomponents:httpclient` dependencies, as they are already provided by Android.

Note that Apache HttpComponents cause certificate issues on older Android versions. I recommend to use the latest version of _flattr4j_.
