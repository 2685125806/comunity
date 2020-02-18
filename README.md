<h1>简述</h1>
基于springboot完成的初步项目搭建,初始阶段

<hr>
<h2>用到了什么</h2>
springboot+mybatis完成业务逻辑,数据库Mysql<br>
前端使用到了thymeleaf模板引擎<br>
数据库版本控制:flyway<br>
插件:lombok<br>


分页问题还未解决
mybatis-generation  指令:
      mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
      
      
      暂且完成了对登录这条链的注释解读以及getQuestion,发布于详情还未动