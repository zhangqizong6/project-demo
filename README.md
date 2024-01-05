# 堆一个平时自己开发项目或者学习用的demo小框架，集成一些工作以来的一些小经验和代码构造
1. pom文件、yml文件的初始化，编写数据库初始化配置代码DataSourceConfig
2. DruidConfig配置类，新增logFilter注入bean
3. 多数据源配置，新建一个数据源，命名为other后，在com/project/demo/config/database和com/project/demo/config/database同级下创建一个新的数据源配置，源码和master一样，将master全局替换成other即可，同时也要在yml文件下新建一个other的数据源信息

