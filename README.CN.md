# 关于Rayson
一个支持跨平台通信以及快速开发的API服务器及其开发框架.

# 特性

1. **简单**
	+ 基于标准 HTTP 协议 (支持SSL)
	+ 序列化默认采用快速、压缩结构化数据
	+ 使用 Java Interface 定义 API 接口

1. **轻量**
	+ 嵌入式服务器
	+ 仅依赖一个第三方库

1. **高性能**
	+ 纯 NIO 实现
	+ 多工作线程智能调度

1. **易用**
	+ 客户端直接使用 Java Interface 来调用远程 API 服务
	+ 服务器及客户端的使用接口非常简约
	+ 开发者专注于业务逻辑的实现、无需关注底层通信及调度细节

# 示例代码

* **服务器端**

1. 定义API协议
```java
@Service
public interface SimpleProtocol extends Protocol {
	/**
	 * Echo received message. 
	 * @param msg The message to be echo.
	 * @return Return the given message.
	 * @throws IOException
	 */
	public String echo(String msg) throws IOException, RpcException;

	/**
	 * Echo received message.
	 * @param msg The message to be echo.
	 * @return Return the given message.
	 * @throws IOException
	 */
	public byte[] echo2(byte[] msg) throws IOException, RpcException;
}
```
1. 实现API协议
```java
public class SimpleService implements SimpleProtocol {
	@Override
	public String echo(final String msg) throws RpcException {
		return msg;
	}
	@Override
	public byte[] echo2(byte[] msg) throws RpcException {
		return msg;
	}
}
```
1. 启动服务器并注册服务
```java
public static void main(final String[] args) throws Exception {
	final RaysonServer server = new RaysonServer(8080);
	server.registerService(new SimpleService());
	server.start();
}
```
* **客户端**
1. Java客户端 RPC 调用

```java
public static void main(final String[] args) throws Exception {
	final RaysonServerAddress serverAddr = new RaysonServerAddress("localhost", 8080);
	SimpleProtocol simpleProtocol = Rayson.createProxy(serverAddr, SimpleProtocol.class);
	try {
		String echoMsg = simpleProtocol.echo("Hello World");
		System.out.println(echoMsg);
	} catch (IOException e) {
		System.err.println("Network error occurred");
	} catch (RpcException e) {
		System.err.println("Invoking RPC got logic error: error_code: " + e.getCode() + " error_message: " + e.getMessage());
	}
}	
```

1.  Bash脚本使用curl调用
```bash
curl -v "http://localhost:8080/org.rayson.demo.simple.api.SimpleProtocol/echo?Hello%20World"
> GET /org.rayson.demo.simple.api.SimpleProtocol/echo?msg=Hello%20World HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
>
< HTTP/1.1 200 OK
< Content-Type: application/json; charset=UTF-8
< Content-Length: 13
<
"Hello World"
```

# 性能表格
Rayson server 性能测试.

- **测试环境**

MacBook Pro  
CPU: 2 GHz Intel Core i5 2 Cores  
内存: 8 GB 1867 MHz LPDDR3  

- **测试结果**

| 线程数   |  调用次数(每线程)   |    请求数/秒   |
|:-------:|:----------------:|:-------------:|
|  1      |   1000           |      1429     |
|  5      |   1000           |      1000     |
|  1      |   10000          |      4274     |
|  5      |   10000          |      2421     |
|  50     |   10000          |      430      |
|  1      |   100000         |      7576     |
|  5      |   100000         |      3968     |
|  50     |   100000         |      545      |

* * *
<center>Copyright Creativor Studio&copy; 2020</center>