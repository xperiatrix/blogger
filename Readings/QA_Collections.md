1. <a href="#01">HTTP, HTTPS实现的过程，区别？</a>
2. <a href="#02">iOS与蓝牙设备通信使用的协议？ 简单描述下连接操作？</a>
3. <a href="#03">蓝牙设备如何打印图片和文字、打印机上的水平/竖直方向上怎么校验？</a>
4. <a href="#04">UIImage如何转成NSData? PNG, JPEG, BITMAP区别？更深层次的图片encode的内容是否了解过？</a>
5. <a href="#05">iOS上图形图像在屏幕上的显示原理？</a>
6. <a href="#06">如何给一个view/imageView加圆角？什么是离屏渲染？</a>
7. <a href="#07">Runloop的运行机制？</a>
8. <a href="#08">iOS的事件响应链？</a>
9. <a href="#09">Runtime中你的常用用法有哪些 分别在什么场景下这么用？</a>
10. <a href="#10">聊一聊GCD？</a>
11. <a href="#11">nil, Nil, Null, NSNull区别？ </a>
12. <a href="#12">+load() +initialize()区别？</a>
13. <a href="#13">堆上的block跟栈上的block有什么区别？分别在什么场景下会用？</a>
14. <a href="#14">怎么发现循环引用？instrument你都怎么用？</a>
15. <a href="#15">KVO, KVC, iOS13上KVC有什么变化？</a>
16. <a href="#16">如何防止crash，你如何实现的？</a>
17. <a href="#17">Networking层如何实现？</a>
18. <a href="#18">Model层如何实现？</a>
19. <a href="#19">为什么用FMDB不是CoreData? </a>
20. <a href="#20">测试用例怎么写？</a>
21. <a href="#21">你一般怎么做性能优化？</a>
22. <a href="#22">什么是傅立叶变换？有哪些例子用到了这个？</a>  

TODO1.  BL 4.0/4.2/5.0协议栈的区别？
TODO2.  对称加密/非对称加密的全过程？  
<br><br>

Q1:  <a id="01"/>HTTP, HTTPS实现的过程，区别?  
参考引用：
 [What happens when you type a URL in the browser and press enter?](https://medium.com/@maneesha.wijesinghe1/what-happens-when-you-type-an-url-in-the-browser-and-press-enter-bb0aa2449c1a)   
 [从输入URL到页面加载发生了什么](https://segmentfault.com/a/1190000006879700)  
 [一次完整的HTTP事务是怎样一个过程？](https://blog.51cto.com/linux5588/1351007)  
 [浏览器发送http请求过程分析](https://segmentfault.com/a/1190000010156898)  
 [经典面试题：从URL输入到页面展现到底发生什么？](https://juejin.im/post/5c773dd251882519610194c1)  
 [HTTP1.0、HTTP1.1 和 HTTP2.0 的区别](https://juejin.im/entry/5981c5df518825359a2b9476) 
 [DNS 原理入门](http://www.ruanyifeng.com/blog/2016/06/dns.html)
 [图解SSL/TLS协议](http://www.ruanyifeng.com/blog/2014/09/illustration-ssl.html) 
 [IP，TCP 和 HTTP](https://objccn.io/issue-10-6/)
 
 A1: 一次完整的HTTP请求主要过程如下  
***1. DNS解析***  (实际上是一个IP地址与网络域名的一个翻译作用 一个递归的查询过程 直到查询到最终想要访问的IP地址).   
应用会先去读取 本地域名服务器[DNS高速缓存]()， 如果没有去查询 [根域名服务器]()，如果没有接着去查询 [COM顶级/NET顶级/ORG顶级-域名服务器]()， 顶级域名服务器会查询到[目的IP]()所在的服务器，在[目的IP]()所在服务器上找到TargetIP，将结果返回给[本地域名服务器]()，直到[本地域名服务器]()将此[网络域名/IP地址]()缓存在[DNS高速缓存]()中去，供下次查询时使用。  
这里要单独强调的是，[根域名服务器]()的解析过程: [www.google.com]()  ----->  [www.google.com.]() 这里不是多了一个".",
       真正的网址是 [www.google.com.](), 这个'.'对应的就是 [根域名服务器](). (默认情况下，所有网址的最后一位都是'.', 只不过DNS解析的过程中，会帮我们省略这个'.')。所以，真正的解析过程是
       
         . --> .com --> google.com. --> www.google.com.  
       
   在这里会展开说一下DNS的优化策略:  (其基本前提是，***如果每次请求IP地址 都经历那么多次查询，效率一定很慢 并且网络开销成本也很大，首选会考虑引入[缓存]()来优化，其次考虑[DNS负载均衡]()***)  
       DNS缓存 如果按照 离浏览器的远近距离进行排序的话，会分为:
       
	浏览器缓存    (chrome中访问 [chrome://net-internals/#dns](chrome://net-internals/#dns)查看,如果有 且没有过期，则DNS解析到此结束)
	操作系统缓存    (如果有且没有过期，则DNS解析到此结束,没有找到则去找host文件)
	系统的host文件 (Linux一般在 /etc/hosts, Windows C:\Windows\System32\drivers\etc 如果有则解析成功 否则发起DNS的系统调用，向本地配置的首选DNS服务器发起解析请求，使用UDP协议向DNS的53端口发起请求，所以在某些andoird手机上，填写DNS正确的格式是 8.8.8.8:53, 开始在网络上递归请求)
	路由器缓存     (**********************.......................到此结束)
	IPS服务器缓存  (**********************.......................到此结束)
	根域名服务器缓存 (*********************........................到此结束)
	顶级域名服务器缓存(********************.........................到此结束)
	主域名服务器缓存  (********************.........................到此结束)
	
如果每次访问一个域名，DNS返回的IP地址都一样， 会话粘滞的请求路径--session.
其实是DNS基于每台机器的负载量、该机器距离用户的距离等因素,返回给用户一个合适的机器的IP给用户提供服务和资源,这个过程就是DNS负载均衡，也叫做DNS重定向。 CDN利用的就是DNS重定向技术实现的。  


***2. 建立TCP连接***  
拿到域名对应的IP地址后，User-Agent(一般指浏览器)发起TCP的三次握手，会以一个随机端口 （2^10=1024 ～ 65535=2^16-1=25536-1 端口0是反向的，二进制数字 [1111111111111111]这是TCP/UDP端口的可用范围）  向服务器的web程序（httpd, nginx）的80端口，发起TCP连接请求。   
这个连接请求，原始的http请求经过TCP/IP 4层模型层层封包，经过各种路由器（局域网除外），到达服务器的网卡后，进入到内核的TCP/IP协议栈（用于识别连接请求，解封包，一层一层的解包），还有可能会经过防火墙的过滤（内核模块），最终到达web程序，并最终建立起TCP/IP连接。  
***TCP的三次握手***  “三次握手”的目的是 [为了防止已失效的连接请求报文段突然又传送到了服务端，因而产生错误]().

	1 client先发送一个连接试探，（ACK=0 表示确认无效； SYN=1 表示这是一个连接请求 或 连接接受报文，同时也表明了这个数据报中 
	  不能携带数据； Seq=X 表示client自己的初始发送序号， 比如 Seq=0,表明这是第0号包)，
	  此时client进入syn_sent状态，表示client等待server的确认回复.
	  简单理解为（第一次握手，浏览器发起，告诉服务器，我要发送请求了）**** client发送 -->> SYN=1， SEQ=X

	2 server监听到请求报文后，如果同意建立连接，则向client发送确认。 (SYN=1; ACK=X+1 表示"期望收到对方的下一个报文段的
	   第一个数据字节序号是X+1, 同时也表明X为止的所有数据已正确收到"， 因为ACK=1, 其实是ACK = 0+1,也就是期望客户端的第一个包; 
	   Seq=Y， 表示server自己的初始序号，(seq=0 代表这是服务器这边发出的第0号包))。
	   这时，服务器进入syn_rcvd, 表明服务器已经收到了client的连接请求，等待client的确认。
	   简单理解为（第二次握手，由服务器发起，告诉浏览器我准备接收了，你赶紧发送吧）**** server发送  -->> SYN=1, ACK=X+1, SEQ=Y
  
	3 client收到server的确认后，还需要再次发送确认，同时携带要发送给server的数据。 (ACK=1 表示期望收到服务器的第一个包，
	   client自己的序号SEQ=X+1, 表示这就是发送的第1个包（相对于第0个包而言）, 当服务器一旦收到client的确认后，
	   这个TCP连接就进入了Established状态。 接下来就可以发起HTTP请求了). 
	   简单理解为 (第三次握手，由浏览器发送，告诉服务器，我马上就发了，准备接收吧).  **** client发送  -->> ACK=Y+1 SEQ=Z


***3. 发送HTTP请求报文***  
TCP是一个端到端的可靠的面向连接的、可靠的、基于字节流的协议, 所以HTTP基于传输层TCP协议不用担心数据的传输的各种问题。
        HTTP是一个基于请求与响应、无状态的、应用层协议。简单的理解（四个基于:）  
        
	1. 请求与响应： client requests and server reponses 
	2. 无状态的： 协议对于事务处理 没有记忆能力，客户端第一次与服务器建立连接发送请求时，需要进行一系列安全认证匹配，
	   因此增加页面等待时间，当client发起请求，server响应完毕后，两者断开连接，也不保存连接状态。 下次client向同样server发送
	   请求时，需要重新建立连接。
	3. HTTP属于应用层协议，基于TCP/IP使用
	4. TCP/IP： HTTP基于TCP所为传输协议。HTTP客户机发起一个与服务器的TCP连接，一旦连接建立，client浏览器和server进程就可以通过socket访问TCP 
        
针对HTTP无状态的特性，进一步优化： （引入了Cookie技术）
        HTTP1.1 提出了持久连接 [HTTP keep-alive]()方法。 旨在 只要任意一端没有明确提出要断开连接，则保持TCP连接状态。需要在
        HTTP-Header中添加： Connection: keep-alive, 表示使用持久连接。
        HTTP请求报文由以下4部分组成：
        
	@1. 请求行 request line --->  请求方法(GET)+请求地址(URL)+协议版本
              HTTP1.1 定义了8种方法：
              - GET        用于请求访问已经被URI的资源
              - POST       用于传输信息给server
              - PUT        传输文件，报文主体中包含文件内容，保存到对应URI位置
              - DELETE     删除文件 删除对应URI位置的文件
              - PATCH  (???)
              - HEAD       获得报文首部，与GET方法类似，只是不返回报文主体，一般用于验证URI是否有效
              - OPTIONS    查询相应URI支持的HTTP方法
              - TRACE (???)

	@2. 请求头 header
            * 通用header字段 (request/response header都会使用的)
              - Date
              - Connection
              - Cache-Control
              - Transfer-Encoding
              - ...

            * request-header使用字段   
              - Host
              - Accept
              - Accept-Charset
              - Accept-Encoding
              - Accept-Language
              - ...

            * response-header使用字段 
              - Accept-Ranges
              - Location
              - Server
              - ...

            * request/response 报文的实体部分使用的字段              
              - Allow
              - Content-Type
              - Content-Length
              - Content-Language
              - Content-Encoding
              - Content-Range
              - ...

	@3. 空行 
              请求头最后会有一个空行，表示header到此结束，接下来为请求体，这一行也非常重要，必不可少  

	@4. 请求体
              请求体中包含        

	HTTP1.1相比较HTTP1.0多了一些优化，主要体现在
		1. 缓存处理
		   在HTTP1.0中主要使用header里的If-Modified-Since,Expires来做为缓存判断的标准，HTTP1.1则引入了更多的缓存控制策略例如Entity tag，If-Unmodified-Since, If-Match, If-None-Match等更多可供选择的缓存头来控制缓存策略。 (cache-control也在HTTP1.1中)
		2. 带宽优化及网络连接的使用
		   HTTP1.0中，存在一些浪费带宽的现象，例如客户端只是需要某个对象的一部分，而服务器却将整个对象送过来了，并且不支持断点续传功能，HTTP1.1则在请求头引入了range头域，它允许只请求资源的某个部分，即返回码是206（Partial Content），这样就方便了开发者自由的选择以便于充分利用带宽和连接。
		3. 错误通知的管理
		   在HTTP1.1中新增了24个错误状态响应码，如409（Conflict）表示请求的资源与资源的当前状态发生冲突；410（Gone）表示服务器上的某个资源被永久性的删除。
		4. Host头处理
		   在HTTP1.0中认为每台服务器都绑定一个唯一的IP地址，因此，请求消息中的URL并没有传递主机名（hostname）。但随着虚拟主机技术的发展，在一台物理服务器上可以存在多个虚拟主机（Multi-homed Web Servers），并且它们共享一个IP地址。HTTP1.1的请求消息和响应消息都应支持Host头域，且请求消息中如果没有Host头域会报告一个错误（400 Bad Request）。
		5. 长连接
		   HTTP 1.1支持长连接（Persistent Connection）和请求的流水线（Pipelining）处理，在一个TCP连接上可以传送多个HTTP请求和响应，减少了建立和关闭连接的消耗和延迟，在HTTP1.1中默认开启Connection： keep-alive，一定程度上弥补了HTTP1.0每次请求都要创建连接的缺点，降低网络开销成本。 
	   
	HTTP2.0
		1. 新的二进制格式（Binary Format）
		   HTTP1.x的解析是基于文本。基于文本协议的格式解析存在天然缺陷，文本的表现形式有多样性，要做到健壮性考虑的场景必然很多，二进制则不同，只认0和1的组合。基于这种考虑HTTP2.0的协议解析决定采用二进制格式，实现方便且健壮。
		2. 多路复用（MultiPlexing）
		   即连接共享，即每一个request都是是用作连接共享机制的。一个request对应一个id，这样一个连接上可以有多个request，每个连接的request可以随机的混杂在一起，接收方可以根据request的 id将request再归属到各自不同的服务端请求里面。
		3. header压缩
		   如上文中所言，对前面提到过HTTP1.x的header带有大量信息，而且每次都要重复发送，HTTP2.0使用encoder来减少需要传输的header大小，通讯双方各自cache一份header fields表，既避免了重复header的传输，又减小了需要传输的大小。
		4. 服务端推送（server push）
		   HTTP2.0具有server push功能。
	   
	   
HTTP报文是明文，如果中间被截取的话会存在一些信息泄露的风险。那么在进入TCP报文之前对HTTP做一次加密就可以解决这个问题了。HTTPS协议的本质就是HTTP + SSL(or TLS)。在HTTP报文进入TCP报文之前，先使用SSL对HTTP报文进行加密。从网络的层级结构看它位于HTTP协议与TCP协议之间。

HTTPS在传输数据之前需要客户端与服务器进行一个握手(TLS/SSL握手)，在握手过程中将确立双方加密传输数据的密码信息。TLS/SSL使用了非对称加密，对称加密以及hash等。Ref to [阮一峰先生的博客TLS/SSL握手过程](http://www.ruanyifeng.com/blog/2014/09/illustration-ssl.html)。
HTTPS相比于HTTP，虽然提供了安全保证，但是势必会带来一些时间上的损耗，如握手和加密等过程，是否使用HTTPS需要根据具体情况在安全和性能方面做出权衡。

		HTTPS的工作原理
		1. 首先HTTP请求服务端生成证书，客户端对证书的有效期、合法性、域名是否与请求的域名一致、证书的公钥（RSA加密）等进行校验
		2. 客户端如果校验通过后，就根据证书的公钥的有效， 生成随机数，随机数使用公钥进行加密（RSA加密）
		3. 消息体产生后，对它的摘要进行MD5（或者SHA1）算法加密，此时就得到了RSA签名；
		4. 发送给服务端，此时只有服务端（RSA私钥）能解密。（非对称加密解密）
		5. 解密得到的随机数，再用AES加密，作为密钥（此时的密钥只有客户端和服务端知道）。


		HTTPS与HTTP的区别
		1. HTTP 的URL 以http:// 开头，而HTTPS 的URL 以https:// 开头
		2. HTTP 是不安全的（明文传输），而 HTTPS 是相对安全的（加密）
		3. HTTP 标准端口是80 ，而 HTTPS 的标准端口是443 防止运营商劫持
		4. 在OSI 网络模型中，HTTP工作于应用层，而HTTPS 的安全传输机制工作在传输层
		5. HTTP 无法加密，而HTTPS 对传输的数据进行加密
		6. HTTP无需证书，而HTTPS 需要CA机构的颁发的SSL证书

		
		

***4. 服务器应答，服务器发送应答头，并处理请求，返回HTTP报文等响应数据***  
服务器在固定的端口接收到TCP报文开始，这一部分对应于编程语言中的socket。它会对TCP连接进行处理，对HTTP协议进行解析，并按照报文格式进一步封装成HTTP Request对象，供上层使用。这一部分工作一般是由Web服务器去进行，如Tomcat, Nginx.  HTTP响应报文由三部分组成: ***状态码, 响应报头和响应报文***。

	1. 状态码
	状态码是由3位数组成，第一个数字定义了响应的类别，且有五种可能取值:
	1xx：指示信息 only  –表示请求已接收，继续处理。
	2xx：成功的一些状态  –表示请求已被成功接收、理解、接受。
	3xx：重定向 link to another url   –要完成请求必须进行更进一步的操作。
	4xx：客户端错误   –请求有语法错误或请求无法实现。
	5xx：服务器端错误 – 服务器未能实现合法的请求。
	2. 响应报头
	Server, Connection
	3. 响应报文
	HTML, CSS, JS, 图片等文件就放在这一部分
	

***5. 浏览器解析渲染页面***
浏览器是一个边解析边渲染的过程。首先浏览器解析HTML文件构建DOM树，然后解析CSS文件构建渲染树，等到渲染树构建完成后，浏览器开始布局渲染树并将其绘制到屏幕上。这个过程比较复杂，涉及到两个概念: reflow(回流)和repain(重绘)。DOM节点中的各个元素都是以盒模型的形式存在，这些都需要浏览器去计算其位置和大小等，这个过程称为relow;当盒模型的位置,大小以及其他属性，如颜色,字体,等确定下来之后，浏览器便开始绘制内容，这个过程称为repain。页面在首次加载时必然会经历reflow和repain。reflow和repain过程是非常消耗性能的，尤其是在移动设备上，它会破坏用户体验，有时会造成页面卡顿。所以我们应该尽可能少的减少reflow和repain。

JS的解析是由浏览器中的JS解析引擎完成的。JS是单线程运行，也就是说，在同一个时间内只能做一件事，所有的任务都需要排队，前一个任务结束，后一个任务才能开始。但是又存在某些任务比较耗时，如IO读写等，所以需要一种机制可以先执行排在后面的任务，这就是：同步任务(synchronous)和异步任务(asynchronous)。JS的执行机制就可以看做是一个主线程加上一个任务队列(task queue)。同步任务就是放在主线程上执行的任务，异步任务是放在任务队列中的任务。所有的同步任务在主线程上执行，形成一个执行栈;异步任务有了运行结果就会在任务队列中放置一个事件；脚本运行时先依次运行执行栈，然后会从任务队列里提取事件，运行任务队列中的任务，这个过程是不断重复的，所以又叫做事件循环(Event loop)。

***浏览器在解析过程中，如果遇到请求外部资源时，如图像,iconfont,JS时，浏览器将下载该资源。请求过程是异步的，并不会影响HTML文档进行加载，但是当文档加载过程中遇到JS文件，HTML文档会挂起渲染过程，不仅要等到文档中JS文件加载完毕还要等待解析执行完毕，才会继续HTML的渲染过程。原因是因为JS有可能修改DOM结构，这就意味着JS执行完成前，后续所有资源的下载是没有必要的，这就是JS阻塞后续资源下载的根本原因***。CSS文件的加载不影响JS文件的加载，但是却影响JS文件的执行。JS代码执行前浏览器必须保证CSS文件已经下载并加载完毕。

如何尽快的加载资源？就是能不从网络中加载的资源就不从网络中加载，当我们合理使用缓存，将资源放在浏览器端，这是最快的方式。如果资源必须从网络中加载，则要考虑缩短连接时间，即DNS优化部分;减少响应内容大小，即对内容进行压缩。另一方面，如果加载的资源数比较少的话，也可以快速的响应用户。当资源到达浏览器之后，浏览器开始进行解析渲染，浏览器中最耗时的部分就是reflow，所以围绕这一部分就是考虑如何减少reflow的次数。
  	
***6. Web服务器关闭TCP连接***  
<br><br>





Q2: <a id="02">iOS与蓝牙设备通信使用的协议？ 简单描述下连接操作？</a>  
参考引用：[Identify your iPhone model,](https://support.apple.com/en-us/HT201296)    [WiKi-BlueTooth](https://en.wikipedia.org/wiki/Bluetooth#Bluetooth_4.0)  
A2: 考虑到兼容性的功耗问题，一般移动平台上会使用蓝牙4.0以上的蓝牙协议，既BLE协议（Bluetooth Low Energy）配合 CoreBluetooth.framework完成开发，需要注意的是蓝牙之间的高速传输，是基于WI-FI的。在蓝牙通信的过程中，通用场景会包含一个 central(主控中心, BTW,这个单词 对应香港的【中环】)，n个外围设备（Peripheral 蓝牙打印机）。central设备可以发现并连接那些正在发送广播的peripherals设备。以C/S结构去理解central/peripheral的关系，可能跟直觉相反，client实际上是central，peripherals才是server。当pherpherals广播着自己的service和charateristic（特征值），central会订阅其中一个具体特征值，这时，两者通过该特征值，建立起双向的数据通道。
	
	iPhone/iPhone 3G    -->  BlueTooth 2.0
	iPhone3GS/4    -->  BlueTooth 2.1
	iPhone4s/5/5c/5s    -->  4s第一次引入BlueTooth4.0(低功耗 超长续航设备成为可能)
	iPhone6/6P/6s/6sP/7/7P/SE 1st Gen	    -->  BlueTooth 4.2(引入大量IoT功能 可穿戴设备成为技术基石)
	iPhone8/8P/X/XS/XS Max/XR/11/11Pro/11Pro Max/SE 2nd Gen    -->  BlueTooth 5.0(主流可穿戴设备的广泛应用 HomePod音响)

	central-device
	建立中心角色
	扫描外设
	发现外设
	连接外设 (失败、成功、断开连接的状态)
	扫描外设中的服务 (发现并获取外设中的服务, 扫描外设对应服务的特征)
	发现并获取外设对应服务的特征
	给对应特征写数据
	订阅特征的通知
	根据特征读取数据

	peripheral-device
	建立外设角色
	设置本地外设的服务和特征
	发布外设和特征
	广播服务
	响应中心的读写请求
	发送更新的特征值，订阅central
	
	TODO: 4.0/4.2/5.0协议栈的区别？
	区别：  4.0协议栈中 规定 最多同时链接7台设备, 但实际上链接的设备越多 越卡顿
		   4.2协议栈中 60m远的通信距离
		   5.0协议栈中 240m远的通信距离  传输速度是4.2的2两倍 可以达到2Mbps
	
	
<br><br>
Q3. <a id="03">蓝牙设备如何打印图片和文字、打印机上的水平/竖直方向上怎么校验？</a>  
A3: 我使用的是蓝牙热敏打印机，首选使用热敏打印机需要热敏纸，这样就不需要对打印机频繁加打印纸又加墨，热敏打印机工作原理是打印头上安装有半导体加热元件，打印头加热并接触热敏打印纸后就可以打印出需要的图案，文字和图象是通过加热，在热敏纸的膜中产生化学反应而生成的。  
基于ESC/POS的打印机指令集完成自定义打印功能的，而且打印机只能接受ASCII, 十进制、十六进制的数据。基本的打印流程就是 1. 连接到打印机 2.编辑内容并 3.将打印内容发送到打印机进行打印。BTW，在蓝牙通讯中，central与peripheral都是以<b>二进制流</b>的方式发送与接收的，用 <b>NSData</b>封装，基于API与打印纸的宽度，定义好样式类型（enum），慢慢调试出打印的最终效果。

```

// ASCII	    十进制		 十六进制
// ESC @		27 64		1B 40
- (NSData *)printerInititaionData { // 当需要初始化打印机时，需要发送给打印机的数据
    Byte bytes[] = {0x1B,0x40};
    return [[NSData alloc] initWithBytes:bytes length:2];
}

- (NSData *)printedTextStringData:(NSString *)text {
    NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
    NSData *textData = [text dataUsingEncoding:enc];
    return textData;
}

- (NSData *)printedUIImageData:(UIImage *)image {  // 注意这么做 图片原始数据是有损失的！而且打印出来不清楚！
    NSData *imageData = UIImageJPEGRepresentation(image, 0.7);
    				or
    NSData *imageData = UIImagePNGRepresentation(image);
    return imageData;  // 因为返回的并不是原始image的nsdata数据，it just alters the given-data and do a reconversion.
}
```

[用于打印机打印的图片数据 正确的打开方式](https://github.com/moming2k/ThermalPrinterKit/blob/68e8608ec2e99e347ef10f39e0e272938dca5e1a/IGThermalSupport.m)是：  

```
+ (NSData *)imageToThermalData:(UIImage*)image {
	CGImageRef imageRef = image.CGImage;
	// Create a bitmap context to draw the uiimage into
	CGContextRef context = [self newBitmapRGBA8ContextFromImage:imageRef];
	if (!context) {
		return NULL;
	}
    
	size_t width = CGImageGetWidth(imageRef);
	size_t height = CGImageGetHeight(imageRef);
	CGRect rect = CGRectMake(0, 0, width, height);
    
	// Draw image into the context to get the raw image data
	CGContextDrawImage(context, rect, imageRef);
	// Get a pointer to the data	
	uint32_t *bitmapData = (uint32_t *)CGBitmapContextGetData(context);
	if (bitmapData) {
        uint8_t *m_imageData = (uint8_t *) malloc(width * height/8 + 8*height/8);
        memset(m_imageData, 0, width * height/8 + 8*height/8);
        int result_index = 0;
        
        for(int y = 0; (y + 24) < height; ) {
            m_imageData[result_index++] = 27;
            m_imageData[result_index++] = 51;
            m_imageData[result_index++] = 0;
            
            m_imageData[result_index++] = 27; 
            m_imageData[result_index++] = 42; 
            m_imageData[result_index++] = 33;
            
            m_imageData[result_index++] = width%256; 
            m_imageData[result_index++] = width/256;
            for (int x = 0; x < width; x++) {
                int value = 0;
                
                for (int temp_y = 0 ; temp_y < 8; ++temp_y) {
                    uint8_t *rgbaPixel = (uint8_t *) &bitmapData[(y+temp_y) * width + x];
                    uint32_t gray = 0.3 * rgbaPixel[RED] + 0.59 * rgbaPixel[GREEN] + 0.11 * rgbaPixel[BLUE];
                    if (gray < 127) {
                        value += 1<<(7-temp_y)&255;
                    }
                }
                m_imageData[result_index++] = value;
                
                value = 0;
                for (int temp_y = 8 ; temp_y < 16; ++temp_y) {
                    uint8_t *rgbaPixel = (uint8_t *) &bitmapData[(y+temp_y) * width + x];
                    uint32_t gray = 0.3 * rgbaPixel[RED] + 0.59 * rgbaPixel[GREEN] + 0.11 * rgbaPixel[BLUE];
                    if (gray < 127) {
                        value += 1<<(7-temp_y%8)&255;
                    }
                }
                m_imageData[result_index++] = value;
                
                value = 0;
                for (int temp_y = 16 ; temp_y < 24; ++temp_y) {
                    uint8_t *rgbaPixel = (uint8_t *) &bitmapData[(y+temp_y) * width + x];
                    uint32_t gray = 0.3 * rgbaPixel[RED] + 0.59 * rgbaPixel[GREEN] + 0.11 * rgbaPixel[BLUE];
                    if (gray < 127) {
                        value += 1<<(7-temp_y%8)&255;
                    }
                }
                m_imageData[result_index++] = value;
            }
            m_imageData[result_index++] = 13; 
            m_imageData[result_index++] = 10;
            y += 24;
        }
        
        NSMutableData *data = [NSMutableData data];
        [data appendBytes:m_imageData length:result_index];
        
        free(bitmapData);
        return data;
	} else {
		NSLog(@"Error getting bitmap pixel data\n");
	}
	CGContextRelease(context);
	return nil ; 
}
```
<br><br>
Q4. <a id="04">UIImage如何转成NSData? PNG, JPEG, BITMAP区别？更深层次的图片encode的内容是否了解过？</a>  
参考引用：[JPEG](https://en.wikipedia.org/wiki/JPEG) &nbsp;&nbsp; [PNG](https://en.wikipedia.org/wiki/Portable_Network_Graphics)&nbsp;&nbsp; [GIF](https://en.wikipedia.org/wiki/GIF) &nbsp;&nbsp;[BMP](https://en.wikipedia.org/wiki/BMP_file_format) &nbsp;&nbsp;[TIFF](https://en.wikipedia.org/wiki/TIFF) &nbsp;&nbsp; [A Pretty Good Answer from StackOverFlow](https://stackoverflow.com/questions/419584/what-is-the-difference-between-jpg-jpeg-png-bmp-gif-tiff-i)   &nbsp;&nbsp; [Differences](https://www.widen.com/blog/whats-the-difference-between-png-jpeg-gif-and-tiff)  
A4. 图片的压缩技术常被分为三类  

1. 有损压缩  摄影照片较为适合，并不适合插图、绘画和文字，并不encode文件的所有内容（JPEG），回复压缩前状态时，并不能全部恢复  
2. 无损压缩  插图、绘画和文字，恢复压缩前状态时，可以完全恢复初始数据(GIF PNG)  
3. 未压缩 所见即所得的原始数据 它们包含着压缩/未压缩的数据内容 (BMP TIFF 用于编辑与存储)
<br><br>

Q5. <a id="05"><b>iOS上图形图像在屏幕上的显示原理？</b></a>  
A5: <b>这是一道经典面试题，如果你曾经用到过[YYKit](https://github.com/ibireme/YYKit)， 你应该看过这篇文章[iOS 保持界面流畅的技巧](https://blog.ibireme.com/2015/11/12/smooth_user_interfaces_for_ios/)和[绘制像素到屏幕上](https://objccn.io/issue-3-1/)。</b>
<br><br>

Q6. <a id="06">如何给一个view/imageView加圆角？什么是离屏渲染？</a>  
A6: [原始博客](https://bestswifter.com/efficient-rounded-corner/)已经访问不到了 ，附加一下[参考源码](https://github.com/bestswifter/MySampleCode) 和转发链接[iOS 高效添加圆角效果](https://juejin.im/post/5a309c2c6fb9a0450407dcf3) 及 [UIKit性能调优实战讲解](https://www.jianshu.com/p/cb9d229df001)
<br><br>

Q7. <a id="07">Runloop的运行机制？</a>  
A7:  [深入理解RunLoop](https://blog.ibireme.com/2015/05/18/runloop/)
<br><br>

Q8. <a id="08"> iOS的事件响应链？ </a>  
A8. [事件分类](https://hit-alibaba.github.io/interview/iOS/Cocoa-Touch/Event-Handling.html)与[事件响应](https://www.jianshu.com/p/4155c9ffe1a8)  
<br><br>

Q9.  <a id="09">Runtime中你的常用用法有哪些 分别在什么场景下这么用？</a>  
A9.  自己用的runtime处理，以前会常用到objc_msgSend，后来从32位升级到64位后，调用起来必须做一次强制转换，嫌太麻烦就用的越来越少，推荐一个专门hook了objc_msgSend的成熟解决方案[比系统API好用的VKMsgSend](http://www.awhisper.net/2015/12/31/vk-msgSend/)。
 
* 获取一个对象的所有属性  

```

#include <objc/runtime.h>
//http://stackoverflow.com/questions/754824/get-an-object-properties-list-in-objective-c

@implementation NSObject (ClassPropertiesUtils)

+ (NSDictionary *)currentClassProperties {
    NSMutableDictionary *dictionaries = [[NSMutableDictionary alloc] init];    
    unsigned int count, i;
    objc_property_t *propertyList = class_copyPropertyList([self class], &count);
    for (i = 0; i < count; i++) {
        objc_property_t singleProperty = propertyList[i];
        const char *propertyName = property_getName(singleProperty);
        if (propertyName) {
            const char *propertyType = getPropertyType(singleProperty);
            NSString *finalPropertyName = [NSString stringWithUTF8String:propertyName];
            NSString *finalPropertyType = [NSString stringWithUTF8String:propertyType];
            [dictionaries setObject:finalPropertyType forKey:finalPropertyName];
        }
    }
    free(propertyList);
    return [dictionaries mutableCopy];
}

static const char *getPropertyType(objc_property_t property) {
    const char *attributes = property_getAttributes(property);
    printf("attributes=%s\n", attributes);    // print singleObj property-attribute
    /*
     attributes=T@"NSString",C,N,V_identifier
     attributes=T@"NSString",C,N,V_userName
     attributes=T@"NSNumber",&,N,V_age
     attributes=T@"NSString",C,N,V_addressDetail
     attributes=T@"NSString",C,N,V_carName
     */
    
    char buffer[1 + strlen(attributes)];      // buffer[x] will find the char of '\0', so the length will plus 1
    strcpy(buffer, attributes);               // copy attributes in this buffer
    char *state = buffer, *attribute;
    while ((attribute = strsep(&state, ",")) != NULL) {
        if (attribute[0] == 'T' && attribute[1] != '@') {
            // it's a C primitive type:
            /*
             if you want a list of what will be returned for these primitives, search online for
             "objective-c" "Property Attribute Description Examples"
             apple docs list plenty of examples of what you get for int "i", long "l", unsigned "I", struct, etc.
             */
            NSString *name = [[NSString alloc] initWithBytes:attribute + 1 length:strlen(attribute) - 1 encoding:NSASCIIStringEncoding];
            return (const char *)[name cStringUsingEncoding:NSASCIIStringEncoding];
        }
        else if (attribute[0] == 'T' && attribute[1] == '@' && strlen(attribute) == 2) {
            // it's an ObjC id type:
            return "id";
        }
        else if (attribute[0] == 'T' && attribute[1] == '@') {
            // it's another ObjC object type:
            NSString *name = [[NSString alloc] initWithBytes:attribute + 3 length:strlen(attribute) - 4 encoding:NSASCIIStringEncoding];
            return (const char *)[name cStringUsingEncoding:NSASCIIStringEncoding];
        }
    }
    return "";
}
@end

```

* 方法混写

```
#import <objc/runtime.h>

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wincomplete-implementation"

@implementation NSString (ContainsString)
#pragma GCC diagnostic pop

#if __IPHONE_OS_VERSION_MIN_REQUIRED < 80000

+ (void)load {
    @autoreleasepool {
        [self test_modernizeSelector:NSSelectorFromString(@"containsString:") withSelector:@selector(test_containsString:)];
    }
}

+ (void)test_modernizeSelector:(SEL)originalSelector withSelector:(SEL)newSelector {
    if (![NSString instancesRespondToSelector:originalSelector]) {
        Method newMethod = class_getInstanceMethod(self, newSelector);
        class_addMethod(self, originalSelector, method_getImplementation(newMethod), method_getTypeEncoding(newMethod));
    }
}

// containsString: has been added in iOS 8. We dynamically add this if we run on iOS 7.
- (BOOL)test_containsString:(NSString *)aString {
    return [self rangeOfString:aString].location != NSNotFound;
}

#endif

@end
```

网上内容的扩展, [iOS 开发中 runtime 常用的几种方法](https://juejin.im/post/5b4febf05188251ac554f1ab)和[Runtime 的常用方法](https://www.jianshu.com/p/ff313acdbc8e)  
<br><br>

Q10. <a id="10">聊一聊GCD？</a>  
A10.  [常见的后台实践](https://objccn.io/issue-2-2/)  
 [GCD 深入理解：第一部分](https://juejin.im/post/5a30cb5d6fb9a0451a765f39)  
  [GCD源码分析1 —— 开篇](http://lingyuncxb.com/2018/01/31/GCD%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%901%20%E2%80%94%E2%80%94%20%E5%BC%80%E7%AF%87/)  
  [Modernizing Grand Central Dispatch Usage WWDC-2019](https://developer.apple.com/videos/play/wwdc2017/706)
<br><br>


Q11. <a id="11">nil, Nil, Null, NSNull区别？</a>  
A11. [nil / Nil / NULL / NSNull](https://nshipster.com/nil/)
<br><br>

Q12. <a id="12">+load() +initialize()区别？</a>  
A12. [load 与 initialize 的区别](https://www.jianshu.com/p/eb8db32c3853)和[iOS类方法load和initialize详解](https://juejin.im/post/5a31dc40f265da4307034712)
<br><br>

Q13. <a id="13">堆上的block跟栈上的block有什么区别？分别在什么场景下会用？</a>  
A13.
[BLOCKS AND MEMORY MANAGEMENT (STACK VS HEAP)](https://www.solstice.com/fwd/blocks-and-memory-management-stack-vs-heap)  
[IOS
深入研究 Block 捕获外部变量和 __block 实现原理](https://halfrost.com/ios_block/)  
[ARC环境下Block的内存管理](https://www.jianshu.com/p/0fad960d6795)  
[从 Block 谈堆栈](https://joakimliu.github.io/2018/03/18/Block-heap-stack/)
[iOS的Block的内存分配](http://ibloodline.com/articles/2016/01/20/memory2-block.html)
[深入理解iOS的block](https://juejin.im/post/5d3171d7f265da1bd26126a0)  
<br><br>

Q14. <a id="14">怎么发现循环引用？instrument你都怎么用？</a>  
A14. ViewController的销毁方法中断点调试 + instrument leaks。 [官方文档](https://help.apple.com/instruments/mac/current/) + [常规用法](http://www.samirchen.com/use-instruments/)  
<br><br>

Q15. <a id="15">KVO, KVC, iOS13上KVC有什么变化？</a>   
A15.  [KVC 和 KVO](https://objccn.io/issue-7-3/)  iOS13上面 有些私有属性不能像之前那样随意调用了 会crash.
<br><br>

Q16. <a id="16">如何防止crash，你如何实现的？</a>   
A16. [被赶尽杀绝的Crash](https://xietao3.com/2017/05/avoidCrash/) + [iOS runtime实用篇--和常见崩溃say good-bye](https://www.jianshu.com/p/5d625f86bd02)
<br><br>

开放型问题 按照自己的理解作答。  
Q17. <a id="17">Networking层如何实现？</a>  
Q18. <a id="18">Model层如何实现？</a>  
Q19. <a id="19">为什么用FMDB不是CoreData?</a>  
<br><br>

Q20. <a id="20">测试用例怎么写？</a>  
A20. 我自己使用的是[Quick](https://github.com/Quick/Quick)+[Nimble](https://github.com/Quick/Nimble), 也推荐使用[KiWi](https://github.com/kiwi-bdd/Kiwi). 推荐阅读这个[测试相关的专栏](https://github.com/Quick/Nimble)
<br><br>

Q21. <a id="21">你一般怎么做性能优化？</a>  
A21. [iOS 保持界面流畅的技巧](https://blog.ibireme.com/2015/11/12/smooth_user_interfaces_for_ios/)  
[Image and Graphics Best Practices-WWDC-2018](https://developer.apple.com/videos/play/wwdc2018/219/)  
[iOS 处理图片的一些小 Tip](https://blog.ibireme.com/2015/11/02/ios_image_tips/)  
<br><br>

Q22. <a id="22">什么是傅立叶变换？有哪些例子用到了这个？</a>  
A22. [傅立叶变换如何理解？美颜和变声都是什么原理？李永乐老师告诉你](https://www.youtube.com/watch?v=0LuyxzqI3Hk)  