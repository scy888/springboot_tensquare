新信贷核心

ACC：new Account Credit Core

### 1. 项目工程结构
```
 acc
  ├─acc-adapter
  │  ├─acc-lxgm-adapter
  │  └─acc-huitong-adapter
  ├─acc-batch
  ├─acc-service
  │  ├─acc-capital-service
  │  ├─acc-loan-service
  │  ├─acc-repay-service
  │  └─acc-pub-service
  ├─acc-service-api
  ├─acc-util
  └─release
```
* acc-adapter-xxx：对接各渠道，主要负责与外围合作方对接，报文、参数等适配工作，不做或少做业务逻辑处理
* acc-service-xxx：核心服务实现，实现的是内部通用流程和业务逻辑
* acc-batch：跑批服务，负责对文件的下载读取、解析后将数据送给业务服务方，此工程本身不处理业务逻辑。
* acc-service-api：服务接口定义，被其他服务工程依赖
* acc-util：工具包
* release：存放一些上线时的ddl、dml脚本或其他与发布相关的脚本

包名对照和命名参考：

|类名的后缀|常用注解|说明|
|----|----|----|
|Controller|`@RestController`<br>`@Controller`|提供http请求服务，<br>示例：LoginController.java|
|Service|`@Service`|业务逻辑处理，Service接口类和Service实现在在同一工程，可以不建接口|
|Repository<br>Repo|`@Repository`|数据处理层，DB、NoSQL等数据的存取与Entity一一对应。|
|Client|-|接口申明，定义于公共依赖包上面，供FeignClient继承，供Provider实现|
|FeignClient|`@FeignClient`|使用Feign组件进行远程服务调用，写在服务的调用方|
|Provider|`@RestController`|实现上面的Client，本质是一个RestController，服务提供方，微服务调用的实现类，同一个Client的FeignClient和Provider分居在两个服务里|
|Service|`@Service`|内部业务处理类，定义接口可选，如果需要被远程服务调用，改名为Provider，并把接口类移至公共依赖包|
|Listener|`@Component`<br>`@Listener`<br>`@KafkaListener`<br>`...`|监听器，可以实现方法级监听器定义，事件监听、消听监听存放此目录|
|Event|-|事件对象|
|Request<br>Req|-|请求参数DTO类，需要符合javabean命名规范，有标准的getter、setter方法|
|Response<br>Res|-|响应参数DTO类，需要符合javabean命名规范，有标准的getter、setter方法|
|Configuration<br>Config|`@Configuration`|配置类，定义各种全局的配置Bean的初始化|
|Application<br>App|`@SpringBootApplication`|Spring Boot工程启动主类|
|Batch相关|-|Job、Step、Reader、Processor、Writer、Tasklet|

### 2. 服务依赖说明
* acc-adapter-xxx：业务适配服务。可以调acc-service-xxx，不可以调acc-batch
* acc-service-xxx：核心服务。不可以调acc-adapter-xxx，不可以调acc-batch， service相互之间不建议调，如果需要调，不要形成环形调用链路。
* acc-batch：跑批服务，只负责跑批任务步骤的编排定义，本身不处理业务逻辑，连独立的跑批元数据库batch，跑批任务由rest请求或定时任务触发。

### 3. 主要的框架和依赖

主要的依赖有：
* spring-boot-starter-web 
* spring-boot-starter-data-jpa
* spring-boot-starter-batch
* spring-cloud-starter-openfeign
* spring-cloud-starter-netflix-eureka-client
* spring-cloud-stream-binder-kafka
* spring-boot-starter-actuator
* spring-boot-starter-admin-client

中间件等其他服务：
* Eureka：http://10.83.0.10:9876/
* Kafka： 10.83.0.47:9092、10.83.0.123:9092、10.83.0.129:9092

acc各工程使用POM工程`weshare-boot-starter-parent`做为父POM。`weshare-boot-starter-parent`是一个POM工程，不含Java代码。
通过继承`spring-boot-starter-parent`工程，定义好spring boot和spring cloud的版本，然后对常用的工具包等进行了版本匹配预定义。
在业务工程里可以直接使用对应的包不用再指定版本，以此提高项目的版本兼容性。
weshare boot与spring boot、spring cloud版本对应关系如下：

|weshare boot|spring boot|spring cloud|
|----|----|----|
|1.0.0-SNAPSHOT|2.1.15.RELEASE|Greenwich.RELEASE|
|2.0.0-SNAPSHOT|2.2.8.RELEASE|Hoxton.RELEASE|
|3.0.0-SNAPSHOT|2.3.1.RELEASE|Hoxton.RELEASE|

### 4. 部署方式
* Maven：使用进行项目工程构建
* Jenkins：进行构建任务管理
* 构建交付物是Docker镜像
* 使用K8s进行服务编排管理

### 5. 版本号约定
版本号规则：
x.x.x
第1位表示大版本号：大需求、大功能、大优化，代码量大
第2位表示小版本号：小功能、小需求、小优化，代码量一般
第3位表示修复版本号：一般是发现问题后的修复版本，代码量一般只有几行的改动

例如：
* 2.0.0 表示第二个大需求版本
* 2.1.0 表示2.0.0的第一个小迭代需求版本
* 2.1.2 表示在2.1.0版本上发现有问题后进行的第2个修复版本

其他：
根据版本号基本上可以知识该版本的代码改动量
参考了spring的版本迭代命名法
开发环境建议使用后缀“SNAPSHOT”，例如“acc-batch-1.0.0-SNAPSHOT，好处是Maven对快照版进行了自动拉取该版本的最新时间戳的依赖包，方便开发人员之间的依赖管理。

主要参考命令：
* 打包至本项目的target：mvn clean package
* 打包发布至本地maven仓库：mvn clean source:jar install
* 跳过单元测试执行, 添加参数：`-Dmaven.test.skip=true`
* 打包发布至私服：mvn clean source:jar deploy -Dmaven.test.skip=true

### 6. 数据规范
数据层面：
* 除了少数配置表不用创建时间、最后修改时间，业务表都需要有创建时间和最后修改时间，且记录被创建时，创建时间需要有值。修改数据时，要记得也要修改最后一次修改时间。
* 序列化和反序列化对象时，子对象不要使用String保存，可以使用Object或者定义泛型来实现json结构的传承。
* 有唯一业务含义的字段可直接做为主键，无法找到业务主键，则使用id作为主键，建议使用全局唯一有序的ID生成器，类里面声明`@Autowired SnowFlake snowFlake`，使用时：`snowFlake.nextId()`获取id值。
* 表名不能中划线“-”、表名不能含有空格、字段名不能含有空格

应用层面：
* 可使用JPA的注解`@CreatedDate`,`@CreatedBy`,`@LastModifiedDate`,`@LastModifiedBy`实现自动保存数据审计四字段。开启审计字段的自动保存方法：
第一步：主类或Config类上`@EnableJpaAuditing`，第二步：entity类上添加注解：`@EntityListeners(AuditingEntityListener.class)`

### 7. 应用开发规范
1。Java8后，官方建议使用全新的时间包`java.time.*`，取代原来的Date + Calendar + SimpleDateFormat等时间加减、与字符串的互转、格式化等操作。
带时分秒`LocalDateTime`和不带时分秒`LocalDate`，entity或dto定义时，结合业务实际情况选用。
2。JSON序列化工具：spring-boot-starter-web已默认引入的是Jacskon的序列化包。
自动序列化反序列化：`@RequestBody`、`@ResponseBody`会使用默认引入JSON序列化工具。
字符串和对象互转的手动序列化反序列化：位于子工程`acc-util`下的`com.weshare.acc.util.JsonUtil`。

Entity里的日期与数据库类型匹配如下：

|Java|MySQL|
|----|----|
|LocalDateTime|datetime|
|LocalDate|date|

AppConfig已全局配置了日期时间和序列化、反序列化配置。
* 不带时分秒的日期格式：yyyy-MM-dd
* 带时分秒的日期格式：yyyy-MM-dd HH:mm:ss


### 8. 代码管理
提交代码的原则：
* 写注释，表明提交代码的功能，非功能性代码酌情写注释
* 不提交本地已经报错的代码
* 一次提交、推送的代码，最好以功能为单位
* 避免积攒太多未提交的代码，减少冲突
* 勤pull

git分支释义：
* feat-001：开发分支
* dev：review后代码合并至dev，开发集成环境基于dev分支构建
* stg：测试环境
* uat：用户验证环境
* rel：发布分支
* master：rel上线后合并至master

### 9. 测试相关资料

乐信测试用例库：
https://www.tapd.cn/52048827/sparrow/tcase/tcase_list?category_id=1152048827001004026

乐信国民二期：
https://www.tapd.cn/52048827/sparrow/tcase/tcase_list?category_id=1152048827001004027

测试环境sftp地址：
文件地址ip: 134.175.158.68
用户：wsgm
密码：a^jJEx0NY9z!M~DY0GR!
端口：20022    
目录：/upload/lxgm/

还款场景模拟测试说明Excel：
https://www.tapd.cn/58730825/documents/show/1158730825001001196

### 9.开发相关资料
每日跑批后的数据校对：
POST http://localhost:9002/checkAll

```json
{
    "csv01":["1120060420501120277159","1120060420501103686498"],
    "csv02":["1120060420501004425604","1120060420501179635031"],
    "csv05":["1120060314224839353567","1120060314533215700393"],
    "csv06":["1120060420501004425604","1120060420501016198387"],
    "csv07":["1120060420501158464265"],
    "csv10":["1120060314224839353567","1120060315434201160683"],
    "csv15":[],
    "csv16":[],
    "batchDate":"2020-06-04" 
}
```




### 附：相关参考资料
* Spring Boot内置配置项：https://docs.spring.io/spring-boot/docs/2.1.15.RELEASE/reference/html/common-application-properties.html
* Spring Batch：https://docs.spring.io/spring-batch/docs/4.1.4.RELEASE/reference/pdf/spring-batch-reference.pdf
* Spring Data JPA：https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

