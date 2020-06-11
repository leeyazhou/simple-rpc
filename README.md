# simple-rpc

simple-rpc是一个简单的RPC框架示例，用于演示如何能够快速定制一套私有通讯协议，基于高性能网络通讯框架Netty开发。

## example

通过运行一下程序，使用simple-rpc框架提供的RPC功能。

- com.github.leeyazhou.example.ProviderMain

```
    public static void main(String[] args) {
        NettyServer server = new NettyServer("127.0.0.1", 25001);
        server.registerProssor(EchoService.class.getName(), new EchoServiceImpl());
        server.startup();
    }
```

- com.github.leeyazhou.example.ConsumerMain

```
    EchoService echoService = ProxyFactory.getProxy(EchoService.class,
        new URL().setHost("127.0.0.1").setPort(25001));
    System.out.println("结果：" + echoService.echo("test echo"));
    System.out.println("结果：" + echoService.hello("Simple RPC"));
    
```