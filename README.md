# auto-generator

负责生成实体类和Mapper文件

## 全局配置

参见`globleConfig.properties`

全局配置中的参数可以单独放在配置文件中.比如放在`t_user_all.properties`中,那么这个配置会覆盖全局的配置

```
# 如果为true，将使用数据库字段
useDbColumn=false

# 存放mapper文件的包名
mapperPackageName=mapper

# mapper文件后缀，如：Dao
mapperClassSuffix=Mapper

# 存放实体类文件的包名
entityPackageName=entity

# 实体类文件后缀, 如：Entity
entitySuffix=

# 如果为true，实体类默认实现Serializable接口
serializable=false

# 全局父类，如果设置了，所有实体类会继承这个父类
globalExt=

# 删除字段名称
deleteColumn=is_deleted

# 如果主键是UUID，设为true
uuid=false

# 指定表名称前缀，实体类会移除前缀。tableName:a_order -> Order.java
tablePrefix=

# 指定表名称后缀，实体类会移除后缀。tableName:order_ext -> Order.java
tableSuffix=

# 设置实体类实现的接口
# 例子:implMap=Student:Clonable,com.xx.PK;User:com.xx.BaseParam
# 结果:
# 	public class Student implements Clonable, com.xx.PK {}
#   public class User implements com.xx.BaseParam {}
implMap=

# 设置实体类继承的类。
# 例子:extMap=Student:Clonable;User:com.xx.BaseParam
# 结果:
# 	public class Student extend Clonable {}
#   public class User extend com.xx.BaseParam {}
#
# 此操作会覆盖globalExt属性
extMap=
```

## 模板

velocity变量参考`velocity变量说明.md`

- entity.tpl_cont 实体类模板
- mapper.tpl_cont mapper类模板