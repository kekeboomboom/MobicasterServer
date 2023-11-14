# WebSocket STOMP服务端

此项目主要与App手机端做交互，由于接入的手机较少，并且接口也较少，逻辑较简单。因此只看springboot对于websocket和STOMP的支持和使用方法即可。

## 手机端发送请求，并且要收到响应的

```
    @MessageMapping("/deviceInfo")
    @SendToUser("/topic/deviceInfo")
```

## 对于手机端发送请求，不需要收到响应的

```
    @MessageMapping("/modifyParam")
```

## 对于服务端主动向手机发送的

```
    private final SimpMessagingTemplate template;
    
	this.template.convertAndSendToUser(androidID, "/topic/queryParam", "");
```

## 对于想知道当前有哪些websocket连接的

```
    private final SimpUserRegistry simpUserRegistry;
    Set<SimpUser> users = simpUserRegistry.getUsers();
```

## CustomHandshakeHandler

此类是在STOMP握手时获得url中标识websocket连接的user

比如websocket连接为ws://localhost:8082/mobicaster-websocket/androidId2345，那么user就是androidId2345

服务器据此user来给指定的websocket发送请求。

## MyChannelInterceptor

如果要在STOMP执行各种StompCommand时进行业务处理时，可以使用此拦截器。

```java
public enum StompCommand {
    STOMP(SimpMessageType.CONNECT),
    CONNECT(SimpMessageType.CONNECT),
    DISCONNECT(SimpMessageType.DISCONNECT),
    SUBSCRIBE(SimpMessageType.SUBSCRIBE, true, true, false),
    UNSUBSCRIBE(SimpMessageType.UNSUBSCRIBE, false, true, false),
    SEND(SimpMessageType.MESSAGE, true, false, true),
    ACK(SimpMessageType.OTHER),
    NACK(SimpMessageType.OTHER),
    BEGIN(SimpMessageType.OTHER),
    COMMIT(SimpMessageType.OTHER),
    ABORT(SimpMessageType.OTHER),
    CONNECTED(SimpMessageType.OTHER),
    RECEIPT(SimpMessageType.OTHER),
    MESSAGE(SimpMessageType.MESSAGE, true, true, true),
    ERROR(SimpMessageType.OTHER, false, false, true);
}
```

---

如果想看更详细的文章，请参考此文章[SpringBoot WebSocket STOMP](https://juejin.cn/post/7282603893354676224)