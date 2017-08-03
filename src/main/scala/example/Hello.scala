package example

import sample.ServerConfig

//The code below compile ok in scala 2.12.2, but doesn't compile in scala 2.12.3
//Exception in thread "main" java.lang.IllegalAccessError: tried to access class sample.BaseConfig from class example.Hello$
object Hello extends App {
  new ServerConfig()
    .setConnectTimeout(1)
    //.asInstanceOf[ServerConfig] //uncomment this will fix java.lang.IllegalAccessError in scala 2.12.3
    .setFailedAttempts(1)
  println("ok")
}
