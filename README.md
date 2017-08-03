# example of issue in scala-2.12.3

I found an issue after upgrading from scala 2.12.2 to 2.12.3. So I made this simple sbt project to demonstrate the issue. (this is a short excerpt from a java library that I use (redisson))


to reporduce
```
sbt run
...
[info] Running example.Hello
[error] (run-main-0) java.lang.IllegalAccessError: tried to access class sample.BaseConfig from class example.Hello$
java.lang.IllegalAccessError: tried to access class sample.BaseConfig from class example.Hello$
```
The same code works fine in scala 2.12.2

see `example.Hello`
```scala
//The code below compile ok in scala 2.12.2, but doesn't compile in scala 2.12.3
//Exception in thread "main" java.lang.IllegalAccessError: tried to access class sample.BaseConfig from class example.Hello$
object Hello extends App {
  new ServerConfig()
    .setConnectTimeout(1)
    //.asInstanceOf[ServerConfig] //uncomment this will fix java.lang.IllegalAccessError in scala 2.12.3
    .setFailedAttempts(1)
  println("ok")
}
```
looks like scala 2.12.3 compiler doesn't cast config class back properly in java
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
