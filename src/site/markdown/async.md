# Async API

Starting with flattr4j v2.9, there is a new Async module that supports communicating with the Flattr servers asynchronously.

One purpose is to limit the connections to Flattr on a server. With the Async API, the Flattr call is prepared in a stateful bean, queued and then executed in a worker thread.

The module is Android compatible, so you can also use it there for easier accessing the Flattr servers in the background without blocking the GUI thread (e. g. in combination with [RoboSpice](https://github.com/stephanenicolas/robospice)).

## How to use it...

The basic idea is to create a `FlattrCallable` instance that holds the Flattr call and all its parameters. `FlattrCallable` itself is a `Callable` which can be queued or processed by an `ExecutorService`. After execution, the result of the call can be retrieved from a `Future` returned by the `ExecutorService`, or by the `FlattrCallable` itself.

Example:

```java
GetMyselfMethod method = new GetMyselfMethod();
method.setAccessToken(accessToken);
Future<User> future = flattrExecutor.submit(method);
User myself = future.get();
User myself2 = method.getResult();
```

First we create a `GetMyselfMethod` instance, which is a `FlattrCallable` that will invoke the `FlattrService.getMyself()` method asynchronously.

In the next line, the `AccessToken` is set. The token is required since `getMyself()` needs proper authorization. This line is optional for Flattr calls that do not require authorization at the Flattr server.

The call is now submitted to a `flattrExecutor`, which is a somehow implemented `ExecutorService` that queues the calls and sends them to the Flattr server sequentially (usually this would be a `ThreadPoolExecutor`). A `Future` instance is returned, which will contain the `User` that was returned by the Flattr server. (All `FlattrCallable` provided by the flattr4j Async API will create a `FlattrService` and connect to the Flattr servers by themselves, so you won't need to take care for this.)

We can do other stuff now, while the Flattr call is waiting for execution in the background. Eventually we will invoke `future.get()` to wait for its completion and retrieve the result of the call.

Besides retrieving the result from the `Future`, it is also possible to invoke `FlattrCallable.getResult()`. However, this method does not block, so it should only be invoked when it is certain that the `FlattrCallable` was actually executed. `Future.get()` should always be preferred.

## Different types of `FlattrCallable`

`FlattrCallable` is just an interface that extends the `Callable` interface.

Every Flattr call is represented by a class that extends `AbstractFlattrCallable`. The abstract implementation takes care for creating a `FlattrService`, so you won't have to care about that. The Flattr calls just magically connect to the Flattr servers by themselves.

There are two extensions of `AbstractFlattrCallable`: `VoidFlattrCallable` represents Flattr calls that do not return a result. It is rather a convenience class for `FlattrCallable` implementors.

The other extension is `PaginatedFlattrCallable`, which represents Flattr calls with a paginated result. With the `page` and `count` property, you can select the desired page number and number of records per page. Since every `FlattrCallable` is stateful and stores its parameters, it is actually quite easy to repeat a Flattr method with a different page selection: just change the `page` property and re-queue the `FlattrCallable` instance.

## RoboSpice

[RoboSpice](https://github.com/stephanenicolas/robospice) is a framework for asynchorous execution of long-running tasks on Android.

For asynchronous requests, the call to be executed must be a subclass of `SpiceRequest`. Strangely there is no generic approach by invoking a `Callable`, so we need to write an adapter class:

```java
public class CallableRequest<T> extends SpiceRequest<T> {
    private final Callable<T> callable;
    public CallableRequest(Callable<T> callable, Class<T> resultType) {
        super(resultType);
        this.callable = callable;
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        return callable.call();
    }
}
```

A `FlattrCallable` instance is now wrapped in a `CallableRequest` and passed to RoboSpice.

```java
GetMyselfMethod method = new GetMyselfMethod();
method.setAccessToken(accessToken);
CallableRequest<User> request = new CallableRequest<User>(method, User.class);
getSpiceManager().execute(request, flattrRequestListener);
```

The `flattrRequestListener` is a `RequestListener` implementation that invokes `method.getResult()` at `onRequestSuccess()`.
