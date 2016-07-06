后台数据存储采用ES的方式

1 ES服务器搭建说明
ES版本：2.3.3
JDK：1.8
1.1 conf/elasticsearch.yml
以下只列出了和官方不同的行
cluster.name: qzt360-es
1.2 启动
bin/elasticsearch

2 Kibana配置
2.1 config/kibana.yml
以下只列出了和官方不同的行
elasticsearch.url: "http://localhost:9200"
2.2 启动
bin/kibana

3 场所基础信息样例
设备ID，备案状态，备案时间，设备状态，组织结构代码，名称，地址，区域，行业
11:22:33:44:55:66	已备案	2016-04-11 12:33:11	正常	0632323-2	七天连锁酒店天河店	广州市天河区龙口东路118号	广州市-天河区	酒店
AA:BB:CC:DD:EE:FF	未备案	2016-04-16 12:36:21	正常	0639742-1	如家连锁酒店天河店	广州市天河区龙口东路128号	广州市-天河区	酒店