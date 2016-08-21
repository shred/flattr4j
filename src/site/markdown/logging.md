# Logging

Starting with v2.2, `flattr4j` logs the communication with the Flattr server. The `java.util.logging` API is used for logging.

By default, an API call is logged with INFO level. If the call should fail, an error is logged with WARNING level. These levels are enabled by default, so you should see the respective logging messages on your logging device.

Details of the API call (like URLs, parameters and JSON responses) are also logged with the `FINE` level. It is usually not necessary for you to see them, unless you want to debug `flattr4j` or want to find out more about why an API call actually failed.

By default, `java.util.logging` does not write `FINE` level to the logging device. There are several ways to enable FINE level logging, but actually none of them are really fine. For example, you can create a file named `logging.properties` with the following content:

```
org.shredzone.flattr4j.level = ALL
```

Now invoke your Java VM like this:

```
java -Djava.util.logging.config.file=path/to/logging.properties ...
```

Sadly, if you just place a `logging.properties` file into your classpath, it is ignored by `java.util.logging`.

## Logging on Android

Since flattr4j v2.3, all log output is logged via `android.util.Log` on Android environments. Depending on your configuration, you will see some or all of the log levels on logcat.

For debugging purposes, you can enable verbose logging by executing this command:

```
adb shell setprop log.tag.flattr4j VERBOSE
```

At flattr4j v2.2, `java.util.logging` was also used on Android, which means that log messages at `FINE` level were filtered out and not shown on logcat. If you need `FINE` level output on Android, make sure you're using at least flattr4j v2.3.

## Why `java.util.logging` instead of `log4j` or `slf4j`?

I know that even though the API of `java.util.logging` is quite fine, configuring it can be a real PITA. Other logging frameworks like `log4j` and `slf4j` are widespread and easier to use.

However, I decided to use `java.util.logging` because it is the only API that is common both to Java and Android. The alternative was to use `android.util.Log` on Android, and a log framework on Java. The implementation would be too complex just for the simple purpose of logging a few lines.

If you use `slf4j` in your project, there is a [jul to slf4j bridge](http://www.slf4j.org/legacy.html#jul-to-slf4j) that routes the `java.util.logging` output to `slf4j`.
