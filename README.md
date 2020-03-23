# About Rayson
An API server and it's development framework which support cross-platform communication and Rapid Application Development (RAD).

# Features

1. **Simple**
	+ Based on standard HTTP protocol (Support SSL)
	+ Serializing using rapid an compressed structured data by default
	+ Using Java Interface to define API

1. **Light weight**
	+ Embedded server
	+ Dependent on one third party library only

1. **High performance**
	+ Pure NIO implementation
	+ Multiple threads intelligent scheduling

1. **Easy to use**
	+ Client side using Java Interface directly to call remote API service
	+ Concise Interfaces used both in server side and client side
	+ Developer can focus on  their business logic only, but not the underling communication and scheduling detail

# Code examples

* **Server side**

1. Define API protocol
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
1. Implements API protocol
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
1. Start the server and register service.

```java
public static void main(final String[] args) throws Exception {
	final RaysonServer server = new RaysonServer(8080);
	server.registerService(new SimpleService());
	server.start();
}
```
* **Client side**
1. Java client RPC invoing

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

1.  Bash script invoking by curl
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

# Performance table
Rayson server performance testing.

- **Testing environment**

MacBook Pro  
CPU: 2 GHz Intel Core i5 2 Cores  
Memory: 8 GB 1867 MHz LPDDR3 
- **Testing result**

| Threads | Call/Per Thread  |Requests/second|
|:-------:|:----------------:|:-------------:|
|  1      |   1000           |      1062     |
|  5      |   1000           |      714      |
|  1      |   10000          |      3650     |
|  5      |   10000          |      3731     |
|  50     |   10000          |      430      |
|  1      |   100000         |      7576     |
|  5      |   100000         |      3968     |
|  50     |   100000         |      521      |

* * *
<center>Copyright Creativor Studio&copy; 2020</center>