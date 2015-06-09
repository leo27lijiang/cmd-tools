# cmd-tools

cmd-tools是一个为应用提供与外部交互的工具模块，类似JMX扩展，但是使用上更便捷，可以像普通的shell命令一样使用命令的方式与服务应用交互。cmd-tools会支持2种交互方式，socket和jmx，目前只实现了socket方式。

现在项目包含2个模块：client和server，client是调用方，使用方式在下面介绍，server的逻辑也很简单，对外零依赖。

# client usage(shell)

在项目根目录下执行(打包需要python环境)：
mvn clean package -Dmaven.test.skip=true

cd 至 cmd-tools-client/target/lib下

CmdClient目录就是可执行的目录，可以拷贝到其他地方使用。cd 到 bin下，chmod +x cmd-client.sh 赋执行权限，查看使用帮助命令：

./cmd-client.sh -h

使用上面的命令查看具体的用法，在命令行模式下，client使用 -Dkey=value 的方式进行参数传递，其他的选项均为shell下的使用方式功能。服务端提供的操作列表也是基于这种格式进行解析，在服务端表现为键值均为 String 的 Map。 

# client usage(api)

参考测试用例：

com.lefu.cmdtools.client.test.ParallelApiTest

com.lefu.cmdtools.client.test.SerialApiTest

# server usage(socket)

实例化 com.lefu.cmdtools.server.net.NetServer 并注入一个 com.lefu.cmdtools.server.CmdServer 的回调实例即可，默认会监听本地13400端口提供服务。

# server usage(jmx)

实现 com.lefu.cmdtools.server.CmdServerMBean 接口，并注入JMX服务即可(现在client还不支持JMX协议)。
