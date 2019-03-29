# 根据json文件格式批量禁止、允许输入,格式如下：
```
[{
	"name": "DFWTINTRA",  #队列管理器名称
	"ip": "127.0.0.1",   #ip地址
	"port": 1800,       #端口号
	"ccsid": 1381,      #字符集编码
	"channel": "JAVA.CHANNEL",  #连接通首名称
	"queues": ["CUS_TO_ENT", "CUS_TO_ENT_INVT"]  #队列数组
}]
```
复制mqlist-example.json 修改成其他名称，修改其中内容

# 运行方式, 用bat可双击运行
`java -Dfile.encoding=GBK -jar mqtask-0.0.1.RELEASE.jar -f mqlist-example.json -o stop`

# 查看命令帮助用下面命令
`java -Dfile.encoding=GBK -jar mqtask-0.0.1.RELEASE.jar -h`

