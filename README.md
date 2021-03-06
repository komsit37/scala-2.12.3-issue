# Example of issue in scala-2.12.3

run time error `java.lang.IllegalAccessError: tried to access class sample.BaseConfig from class example.Hello$`  

I found an issue after upgrading from scala 2.12.2 to 2.12.3. So I made this simple sbt project to demonstrate the issue. (this is a short excerpt from a java library that I use (redisson))

update 2017-08-04: this issue is fixed in scala-2.12.4. see https://github.com/scala/bug/issues/10450

to reporduce
```
sbt run
...
[info] Running example.Hello
[error] (run-main-0) java.lang.IllegalAccessError: tried to access class sample.BaseConfig from class example.Hello$
java.lang.IllegalAccessError: tried to access class sample.BaseConfig from class example.Hello$
```

The same code works fine in scala 2.12.2

This happen when calling java class with some complicated type casting from scala

see `example.Hello`
```scala
//The code below runs ok in scala 2.12.2, but doesn't runs in scala 2.12.3
//Exception in thread "main" java.lang.IllegalAccessError: tried to access class sample.BaseConfig from class example.Hello$
object Hello extends App {
  new ServerConfig()
    .setConnectTimeout(1)
    //.asInstanceOf[ServerConfig] //uncomment this will fix java.lang.IllegalAccessError in scala 2.12.3
    .setFailedAttempts(1)
  println("ok")
}
```
I don't know why this happen but looks like scala 2.12.3 compiler doesn't handle casting config class back properly in java
```java
class BaseConfig<T extends BaseConfig<T>> {
    private int connectTimeout = 10000;
    private int failedAttempts = 3;

    public T setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return (T) this;
    }

    public T setFailedAttempts(int slaveFailedAttempts) {
        this.failedAttempts = slaveFailedAttempts;
        return (T) this;
    }
}
```
```java
public class ServerConfig extends BaseConfig<ServerConfig> {
}
```
