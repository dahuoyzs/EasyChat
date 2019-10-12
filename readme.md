<p><center><a href='https://www.bilibili.com/video/av65372361?pop_share=1'><img src = 'https://github.com/dahuoyzs/EasyChat/tree/master/ScreenCapture/logo.png'></a></center></p>
#### Project Profile

"EasyChat"  is not easy, it is not only a WAN chat software, but also a cross-platform chat software, and a remote control software.

#### 项目介绍

“easychat”并不简单，它不仅是一个广域网聊天软件，也是一个跨平台的聊天软件，更是一个远程控制软件

### 视频效果

https://www.bilibili.com/video/av65372361?pop_share=1
### 视频演示：

https://www.bilibili.com/video/av65653369

#### 技术选型

- 后端技术：SpringBoot + Spring Data Jpa + H2数据库+Druid数据库连接池+ WebSocket+ Fastjson+Lombok
- 前端技术：JAVAFX+WebSocketClient+Hutool

##### 环境及插件要求

- Jdk8+
- Maven
- Lombok（重要）

### 项目操作流程

> 1. 下载项目到本地
> 2. 用idea分别打开两个项目，先运行服务端，后运行客户端
> 3. 服务端启动后，可以打开http://localhost:8080/manager.html查看页面
> 4. 客户端登录需要先注册一个账号，注册好后可以直接登录。
> 5. 登录好后网页上的功能即可有效。网页可以自行修改，或者扩展其他的命令。



**如果想广域网操控别人。需要把项目部署到自己的服务器**上。

由于项目没有什么依赖，只需要有java环境即可，把easychat-server.jar上传到自己的服务器上，

运行

```shell
nohup java -jar easychat-server.jar >log.txt &
```

手机访问对应     http://[ip]:8080/manager.html  即可



### 聊天室输入框的集中输入姿势

1. 123321    				 聊天室内在线用户均可收到消息   				

   如输入 **123**        	在线用户收到   **123**

2. [在线用户名]123321    制定的用户收到消息-->私发给某个人的消息    

   如输入   **[dahuo]123**     dahuo用户收到   **【私信】123**   

3. ##cmd命令              聊天室内在线用户均执行接收到的cmd命令

   如输入    **##calc**        在线用户  打开计算器[windows]

4. ##[dahuo]cmd命令        dahuo用户 执行接收到的cmd命令

   如输入   **##[dahuo]calc**  dahuo用户  开打计算器[windows]

由于苹果和linux系统的命令需要root权限才行，所以目前##的操作只对Windows计算机有效。

### 测试命令

##cmd /c start  www.baidu.com

##cmd /c start  http://love.zxgnz.com/html/20190823/15665711538720.html

##cmd /c start

##calc

##shutdown -s -t 60

##shutdown /a



### 运用场景，

恶搞小伙伴

给女朋友表白



ScreenCapture



Client:

![easychat](https://github.com/dahuoyzs/EasyChat/tree/master/ScreenCapture/easychat.jpg)



Mobile  Page

![easychat](https://github.com/dahuoyzs/EasyChat/tree/master/ScreenCapture/phoneControl.jpg)