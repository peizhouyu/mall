# peimall(后台)
> 这是一个用java实现的一个仿京东电商网站,主要实现了以下功能
* 用户 登录 注册 查看个人信息 修改个人信息 修改密码 忘记找回密码
* 商品浏览 详情 加入购物车  提交订单 支付 查看订单状态 
* 后台管理员 登录 授权 商品上下架 查看订单信息 安排发货
* 后台交易数据量 统计 分析 实时查看  


## 技术栈    
- IDE: IntelliJ IDEA 
- 项目架构: 前后端分离 Nginx + Tomcat 集群（后端）
- 使用的框架及工具:SpringMVC MyBatis Spring Spring Session  Spring Schedule+Redisson分布式锁  Maven Jackson Lombok
- 主数据库: Mysql5.7
- 辅助缓存: redis3.2
- 数据库访问层: MyBatis
- 消息中间件: RabbitMQ/3.6.10
## 当前版本 master
主分支为初创分支，正式代码从v1.0分支开始，采用版本渐进式开发，如遇重大功能提交或进行架构重构，将启用新的分支。原有分支仍可作为有效运行之分支。