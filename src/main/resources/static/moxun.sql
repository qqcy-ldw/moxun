-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: moxun
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answer_likes`
--

DROP TABLE IF EXISTS `answer_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer_likes` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `answer_id` bigint NOT NULL COMMENT '回答ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_answer` (`user_id`,`answer_id`),
  KEY `fk_answer_likes_answer` (`answer_id`),
  CONSTRAINT `fk_answer_likes_answer` FOREIGN KEY (`answer_id`) REFERENCES `answers` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_answer_likes_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='回答点赞表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_likes`
--

LOCK TABLES `answer_likes` WRITE;
/*!40000 ALTER TABLE `answer_likes` DISABLE KEYS */;
INSERT INTO `answer_likes` VALUES (1,1,1,'2025-11-21 13:45:30'),(2,1,2,'2025-11-21 13:45:30'),(3,1,5,'2025-11-21 13:45:30'),(4,1,6,'2025-11-21 13:45:30'),(5,1,8,'2025-11-21 13:45:30'),(6,1,9,'2025-11-21 13:45:30'),(7,2,1,'2025-11-21 13:45:30'),(8,2,2,'2025-11-21 13:45:30'),(9,2,5,'2025-11-21 13:45:30'),(10,2,9,'2025-11-21 13:45:30'),(11,3,2,'2025-11-21 13:45:30'),(12,3,5,'2025-11-21 13:45:30'),(13,4,1,'2025-11-21 13:45:30'),(14,4,2,'2025-11-21 13:45:30'),(15,4,5,'2025-11-21 13:45:30'),(16,4,6,'2025-11-21 13:45:30'),(17,4,8,'2025-11-21 13:45:30'),(18,4,9,'2025-11-21 13:45:30'),(19,5,1,'2025-11-21 13:45:30'),(20,5,2,'2025-11-21 13:45:30'),(21,5,6,'2025-11-21 13:45:30'),(22,6,1,'2025-11-21 13:45:30'),(23,6,8,'2025-11-21 13:45:30'),(24,7,2,'2025-11-21 13:45:30'),(25,7,9,'2025-11-21 13:45:30'),(26,8,1,'2025-11-21 13:45:30'),(27,8,2,'2025-11-21 13:45:30'),(28,8,5,'2025-11-21 13:45:30'),(29,8,9,'2025-11-21 13:45:30'),(30,9,1,'2025-11-21 13:45:30'),(31,9,5,'2025-11-21 13:45:30'),(32,10,2,'2025-11-21 13:45:30'),(33,10,6,'2025-11-21 13:45:30'),(34,11,1,'2025-11-21 13:45:30'),(35,11,2,'2025-11-21 13:45:30'),(36,11,5,'2025-11-21 13:45:30'),(37,11,9,'2025-11-21 13:45:30'),(38,12,1,'2025-11-21 13:45:30'),(39,12,2,'2025-11-21 13:45:30'),(40,13,5,'2025-11-21 13:45:30'),(41,13,9,'2025-11-21 13:45:30');
/*!40000 ALTER TABLE `answer_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answers`
--

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answers` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回答ID',
  `question_id` bigint NOT NULL COMMENT '问题ID',
  `user_id` bigint NOT NULL COMMENT '回答用户ID',
  `content` text NOT NULL COMMENT '回答内容',
  `is_accepted` tinyint(1) DEFAULT '0' COMMENT '是否被采纳',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_question_id` (`question_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_answers_question` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_answers_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='回答表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
INSERT INTO `answers` VALUES (1,1,3,'Vue组件通信有多种方式：\n1. Props和Events：父子组件通信的基本方式\n2. Vuex：复杂组件间的状态管理\n3. EventBus：简单的跨组件通信\n4. Provide/Inject：深层嵌套组件通信\n\n对于你的问题，Vuex适合用于大型应用或多组件共享状态，而事件总线适合简单的场景。建议先掌握Props和Events，再学习其他方式。',1,24,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(2,1,7,'补充一点，Vue 3中新增了Composition API，可以使用hooks方式共享状态和逻辑，比传统的通信方式更灵活。另外，如果是小型项目，可以考虑使用provide/inject代替Vuex，简化状态管理。',0,15,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(3,1,4,'我在实际项目中，对于中小型应用，更倾向于使用EventBus而非Vuex，因为配置简单。但要注意在组件销毁时解绑事件，避免内存泄漏。Vue 3中推荐使用mitt或tiny-emitter替代原生EventBus。',0,8,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(4,2,3,'JavaScript闭包是指有权访问另一个函数作用域中变量的函数。简单说，闭包就是一个函数，它记住了它创建时的环境。\n\n闭包的主要用途：\n1. 数据私有化\n2. 函数工厂\n3. 模块化开发\n\n示例：\n```javascript\nfunction createCounter() {\n  let count = 0;\n  return function() {\n    return ++count;\n  };\n}\n\nconst counter = createCounter();\nconsole.log(counter()); // 1\nconsole.log(counter()); // 2\n```\n\n这里counter函数就是一个闭包，它\"记住\"了count变量。',1,32,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(5,2,7,'闭包确实是JavaScript中较难理解的概念。补充一点，闭包常见的问题是内存泄漏，因为闭包会保持对外部变量的引用。在使用时要注意及时释放不需要的引用。',0,18,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(6,2,1,'我理解闭包的一个好方法是把它看作是一个\"背包\"，函数带着这个装有变量的\"背包\"到处跑。这在实现私有变量、柯里化、记忆化等场景非常有用。',0,12,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(7,2,5,'闭包在前端开发中非常常见，比如事件处理函数、定时器回调、Ajax请求回调等。理解闭包对理解JavaScript的异步编程模型很有帮助。',0,9,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(8,3,4,'CSS Grid和Flexbox是两种强大的布局工具，但适用场景不同：\n\nFlexbox（一维布局）：\n- 适合处理一行或一列的元素排列\n- 内容优先，尺寸根据内容调整\n- 适合组件级别的小布局\n\nGrid（二维布局）：\n- 同时控制行和列\n- 布局优先，先定义网格，再放置元素\n- 适合整页或大区域的布局\n\n简单说：小布局用Flexbox，大布局用Grid。两者可以结合使用，Grid处理整体，Flexbox处理细节。',1,21,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(9,3,3,'补充一个实际例子：导航菜单通常用Flexbox更合适，因为它是一维的；而整个页面布局，包括头部、侧边栏、主内容区、底部这种结构，用Grid更合适。',0,14,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(10,4,7,'Pandas处理缺失值有多种策略：\n\n1. 删除缺失值：\n```python\ndf.dropna()  # 删除包含任何缺失值的行\ndf.dropna(axis=1)  # 删除包含任何缺失值的列\n```\n\n2. 填充缺失值：\n```python\ndf.fillna(0)  # 用0填充\ndf.fillna(method=\"ffill\")  # 用前一个值填充\ndf.fillna(df.mean())  # 用平均值填充\n```\n\n选择策略取决于：\n- 缺失值比例（比例高时不宜删除）\n- 数据类型（数值型可用统计量填充）\n- 缺失原因（了解业务背景）\n\n建议：先探索性分析了解缺失情况，再决定策略，不要盲目删除。',1,19,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(11,4,3,'处理缺失值时，还可以考虑使用更高级的方法，如KNN或随机森林进行插补，这在scikit-learn中有实现。另外，缺失值本身可能包含信息，可以创建\"是否缺失\"的新特征。',0,11,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(12,4,5,'在实际工作中，我发现理解缺失值的业务含义非常重要。例如，客户调查中的缺失可能表示\"不适用\"而非\"不知道\"，这会影响处理策略。',0,7,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(13,5,3,'Spring Security实现权限控制的步骤：\n\n1. 添加依赖：\n```xml\n<dependency>\n    <groupId>org.springframework.boot</groupId>\n    <artifactId>spring-boot-starter-security</artifactId>\n</dependency>\n```\n\n2. 创建安全配置类：\n```java\n@Configuration\n@EnableWebSecurity\npublic class SecurityConfig extends WebSecurityConfigurerAdapter {\n    @Override\n    protected void configure(HttpSecurity http) throws Exception {\n        http.authorizeRequests()\n            .antMatchers(\"/admin/**\").hasRole(\"ADMIN\")\n            .antMatchers(\"/user/**\").hasAnyRole(\"USER\", \"ADMIN\")\n            .anyRequest().authenticated()\n            .and()\n            .formLogin();\n    }\n}\n```\n\n3. 实现UserDetailsService接口，从数据库加载用户信息\n\n4. 使用@PreAuthorize注解进行方法级权限控制\n\n完整实现可以参考Spring官方文档或我的GitHub示例项目。',1,25,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(14,5,7,'补充一点，在微服务架构中，可以考虑使用OAuth2和JWT实现分布式权限控制。Spring Security OAuth2提供了良好的支持。',0,13,'2025-11-21 13:45:30','2025-11-21 13:45:30');
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignment_attachments`
--

DROP TABLE IF EXISTS `assignment_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignment_attachments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '附件ID',
  `assignment_id` bigint NOT NULL COMMENT '作业ID',
  `file_url` varchar(255) NOT NULL COMMENT '文件URL',
  `file_name` varchar(100) NOT NULL COMMENT '文件名',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小',
  PRIMARY KEY (`id`),
  KEY `idx_assignment_id` (`assignment_id`),
  CONSTRAINT `fk_assignment_attachments_assignment` FOREIGN KEY (`assignment_id`) REFERENCES `assignments` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='作业附件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignment_attachments`
--

LOCK TABLES `assignment_attachments` WRITE;
/*!40000 ALTER TABLE `assignment_attachments` DISABLE KEYS */;
INSERT INTO `assignment_attachments` VALUES (1,1,'/assignments/1/design.png','网页结构设计稿.png',1245678),(2,2,'/assignments/2/responsive-design.zip','响应式设计参考.zip',3456789),(3,4,'/assignments/4/shopping-cart-demo.mp4','购物车功能演示.mp4',8765432),(4,5,'/assignments/5/sales_data.csv','销售数据集.csv',2345678);
/*!40000 ALTER TABLE `assignment_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignment_submissions`
--

DROP TABLE IF EXISTS `assignment_submissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignment_submissions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '提交ID',
  `assignment_id` bigint NOT NULL COMMENT '作业ID',
  `user_id` bigint NOT NULL COMMENT '学生ID',
  `content` text COMMENT '提交内容',
  `score` int DEFAULT NULL COMMENT '得分',
  `feedback` text COMMENT '反馈',
  `submit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `graded_time` datetime DEFAULT NULL COMMENT '评分时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_assignment` (`user_id`,`assignment_id`),
  KEY `idx_assignment_id` (`assignment_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_submit_time` (`submit_time`),
  KEY `idx_graded_time` (`graded_time`),
  CONSTRAINT `fk_assignment_submissions_assignment` FOREIGN KEY (`assignment_id`) REFERENCES `assignments` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_assignment_submissions_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='作业提交表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignment_submissions`
--

LOCK TABLES `assignment_submissions` WRITE;
/*!40000 ALTER TABLE `assignment_submissions` DISABLE KEYS */;
INSERT INTO `assignment_submissions` VALUES (1,1,1,'# HTML5页面结构设计作业\n\n我按照要求完成了页面结构设计，使用了HTML5语义化标签，包括header, nav, main, article, section, aside和footer等。\n\n## 实现要点\n\n1. 使用header标签包含网站标题和主导航\n2. 使用main标签包含页面主要内容\n3. 使用article和section组织内容区块\n4. 使用aside实现侧边栏\n5. 使用footer包含版权信息和次要导航\n\n代码已附在提交文件中。',92,'很好的完成了作业要求，HTML结构清晰，语义化标签使用恰当。有几点小建议：1. 可以在nav中使用ul/li结构组织导航项；2. 可以为section添加更多的语义化id或class。继续保持！','2023-06-28 15:30:45','2023-07-01 10:15:22'),(2,1,2,'# HTML5页面结构设计\n\n我完成了HTML5页面结构的设计，严格按照设计稿实现，并使用了语义化标签。\n\n## 结构说明\n\n- header: 包含logo和主导航\n- main: 主内容区\n  - section: 产品介绍\n  - section: 特色功能\n  - section: 用户评价\n- aside: 侧边栏，包含相关链接和广告\n- footer: 页脚信息\n\n所有代码已通过W3C验证。',95,'出色的工作！代码结构清晰，语义化标签使用准确，页面结构与设计稿高度一致。特别赞赏你对W3C标准的重视。一个小建议：可以考虑添加ARIA属性以提高可访问性。','2023-06-29 09:45:12','2023-07-01 11:20:35'),(3,2,1,'# CSS3响应式布局实现\n\n我使用CSS3媒体查询和Flexbox实现了响应式布局，在不同设备上都能良好展示。\n\n## 实现技术\n\n1. 使用Flexbox进行整体布局\n2. 媒体查询断点设置：\n   - 手机：<768px\n   - 平板：768px-1024px\n   - 桌面：>1024px\n3. 使用相对单位(rem, %)确保布局灵活性\n\n已提交HTML、CSS文件和各设备测试截图。',88,'响应式布局实现得很好，断点设置合理，Flexbox使用恰当。有两点需要改进：1. 在小屏幕上导航菜单可以考虑使用汉堡菜单；2. 部分图片在小屏幕上没有等比例缩放。请在下次作业中注意这些细节。','2023-07-12 16:40:23','2023-07-16 14:30:18'),(4,3,2,'# JavaScript闭包与模块模式\n\n我实现了一个计数器模块，使用闭包实现了私有变量，并提供了增加、减少、重置和获取当前值的方法。\n\n## 代码实现\n\n```javascript\nconst createCounter = () => {\n  let count = 0;\n  \n  return {\n    increment: () => ++count,\n    decrement: () => --count,\n    reset: () => { count = 0; return count; },\n    getValue: () => count\n  };\n};\n```\n\n测试用例和结果已在提交文件中。',98,'优秀的实现！代码简洁清晰，完美展示了闭包的应用。闭包的私有性保护得很好，接口设计合理。特别欣赏你的测试用例设计，覆盖了各种使用场景。继续保持这样的高质量代码风格！','2023-06-24 11:25:38','2023-06-26 09:10:45'),(5,4,5,'# Vue.js购物车组件\n\n我开发了一个Vue.js购物车组件，实现了所有要求的功能。\n\n## 组件结构\n\n- ShoppingCart (主组件)\n  - ProductList (商品列表组件)\n  - CartList (购物车列表组件)\n  - CartSummary (汇总组件)\n\n## 功能实现\n\n1. 使用Vuex管理购物车状态\n2. 实现了商品添加、数量修改、删除功能\n3. 使用计算属性计算总价和商品总数\n4. 实现了本地存储，刷新页面不丢失购物车数据\n\n代码和演示截图已提交。',NULL,NULL,'2023-07-09 20:15:33',NULL);
/*!40000 ALTER TABLE `assignment_submissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '作业ID',
  `title` varchar(100) NOT NULL COMMENT '作业标题',
  `description` text COMMENT '作业描述',
  `course_id` bigint NOT NULL COMMENT '关联课程ID',
  `deadline` datetime NOT NULL COMMENT '截止日期',
  `total_score` int DEFAULT '100' COMMENT '总分',
  `content` text COMMENT '作业内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `attachments` text COMMENT '附件URL列表(JSON格式)',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='作业表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments`
--

LOCK TABLES `assignments` WRITE;
/*!40000 ALTER TABLE `assignments` DISABLE KEYS */;
INSERT INTO `assignments` VALUES (1,'HTML5页面结构设计','根据提供的设计稿，使用HTML5语义化标签创建页面结构，包含头部、导航、主内容区、侧边栏和底部。',1,'2023-06-30 23:59:59',100,'# 作业要求\n\n1. 使用HTML5语义化标签（header, nav, main, article, section, aside, footer等）\n2. 页面结构需符合设计稿\n3. 代码需符合W3C规范\n4. 提交HTML文件和说明文档\n\n# 设计稿\n请参考附件中的设计稿图片。','2025-11-21 13:45:30','2025-11-21 13:45:30',NULL),(2,'CSS3响应式布局实现','使用CSS3技术实现一个响应式网页，要求在不同设备上（手机、平板、电脑）都能良好展示。',1,'2023-07-15 23:59:59',100,'# 作业要求\n\n1. 使用CSS3媒体查询实现响应式布局\n2. 使用Flexbox或Grid布局技术\n3. 断点设置合理（至少包含手机、平板、桌面三种视图）\n4. 提交HTML、CSS文件和测试截图\n\n# 参考设计\n请参考附件中的设计图片。','2025-11-21 13:45:30','2025-11-21 13:45:30',NULL),(3,'JavaScript闭包与模块模式','实现一个计数器模块，使用闭包实现私有变量，提供增加、减少、重置和获取当前值的方法。',2,'2023-06-25 23:59:59',100,'# 作业要求\n\n1. 使用闭包实现私有变量\n2. 提供四个公共方法：increment(), decrement(), reset(), getValue()\n3. 模块可以创建多个独立的计数器实例\n4. 提交JavaScript文件和测试用例\n\n# 示例代码结构\n```javascript\nconst createCounter = () => {\n  // 你的实现\n};\n\nconst counter1 = createCounter();\nconst counter2 = createCounter();\n// 测试代码\n```','2025-11-21 13:45:30','2025-11-21 13:45:30',NULL),(4,'Vue.js组件开发','开发一个Vue.js购物车组件，实现商品列表展示、添加商品、修改数量、删除商品和计算总价的功能。',3,'2023-07-10 23:59:59',100,'# 作业要求\n\n1. 使用Vue.js组件化开发\n2. 实现商品列表展示、添加商品、修改数量、删除商品功能\n3. 使用计算属性计算总价\n4. 使用合适的事件处理机制\n5. 提交Vue组件代码和演示截图\n\n# 组件结构\n- ShoppingCart（主组件）\n  - ProductList（商品列表）\n  - CartList（购物车列表）\n  - CartSummary（购物车汇总）','2025-11-21 13:45:30','2025-11-21 13:45:30',NULL),(5,'Python数据分析报告','使用Python对提供的销售数据集进行分析，生成销售趋势、产品类别分析和客户群体分析报告。',4,'2023-07-20 23:59:59',100,'# 作业要求\n\n1. 使用Pandas进行数据清洗和处理\n2. 使用Matplotlib或Seaborn进行数据可视化\n3. 分析内容包括：销售趋势、产品类别分析、客户群体分析\n4. 提交Python代码、数据分析报告（PDF格式）\n\n# 数据集\n请使用课程提供的sales_data.csv文件。','2025-11-21 13:45:30','2025-11-21 13:45:30',NULL);
/*!40000 ALTER TABLE `assignments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chapters`
--

DROP TABLE IF EXISTS `chapters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chapters` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '章节ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `title` varchar(100) NOT NULL COMMENT '章节标题',
  `sort` int DEFAULT '0' COMMENT '排序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`),
  CONSTRAINT `fk_chapters_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='章节表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chapters`
--

LOCK TABLES `chapters` WRITE;
/*!40000 ALTER TABLE `chapters` DISABLE KEYS */;
INSERT INTO `chapters` VALUES (1,1,'HTML5基础入门',1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(2,1,'CSS3核心技术',2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(3,1,'响应式布局实战',3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(4,1,'综合项目实战',4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(5,2,'JavaScript基础回顾',1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(6,2,'ES6+新特性详解',2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(7,2,'异步编程与Promise',3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(8,2,'函数式编程思想',4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(9,2,'设计模式与最佳实践',5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(10,3,'Vue.js基础入门',1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(11,3,'组件化开发',2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(12,3,'Vuex状态管理',3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(13,3,'Vue Router路由配置',4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(14,3,'电商项目实战',5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(15,4,'Python基础回顾',1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(16,4,'NumPy数据处理',2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(17,4,'Pandas数据分析',3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(18,4,'Matplotlib数据可视化',4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(19,4,'数据分析项目实战',5,'2025-11-21 13:45:30','2025-11-21 13:45:30');
/*!40000 ALTER TABLE `chapters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_categories`
--

DROP TABLE IF EXISTS `course_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
  `parent_id` bigint DEFAULT NULL COMMENT '父分类ID',
  `sort` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_categories`
--

LOCK TABLES `course_categories` WRITE;
/*!40000 ALTER TABLE `course_categories` DISABLE KEYS */;
INSERT INTO `course_categories` VALUES (1,'编程开发','/icons/programming.png',NULL,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(2,'设计创作','/icons/design.png',NULL,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(3,'语言学习','/icons/language.png',NULL,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(4,'职业技能','/icons/career.png',NULL,4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(5,'兴趣爱好','/icons/hobby.png',NULL,5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(6,'学历教育','/icons/education.png',NULL,6,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(7,'Web前端','/icons/frontend.png',1,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(8,'后端开发','/icons/backend.png',1,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(9,'移动开发','/icons/mobile.png',1,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(10,'数据科学','/icons/datascience.png',1,4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(11,'UI设计','/icons/ui.png',2,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(12,'平面设计','/icons/graphic.png',2,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(13,'3D建模','/icons/3d.png',2,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(14,'英语','/icons/english.png',3,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(15,'日语','/icons/japanese.png',3,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(16,'韩语','/icons/korean.png',3,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(17,'项目管理','/icons/projectmanagement.png',4,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(18,'营销推广','/icons/marketing.png',4,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(19,'职场办公','/icons/office.png',4,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(20,'音乐','/icons/music.png',5,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(21,'烹饪','/icons/cooking.png',5,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(22,'摄影','/icons/photography.png',5,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(23,'考研','/icons/postgraduate.png',6,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(24,'考公','/icons/civilservice.png',6,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(25,'专升本','/icons/undergraduate.png',6,3,'2025-11-21 13:45:30','2025-11-21 13:45:30');
/*!40000 ALTER TABLE `course_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_comments`
--

DROP TABLE IF EXISTS `course_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_comments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '评论内容',
  `rating` tinyint unsigned DEFAULT '5' COMMENT '评分(1-5)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_comments_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `fk_comments_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_comments`
--

LOCK TABLES `course_comments` WRITE;
/*!40000 ALTER TABLE `course_comments` DISABLE KEYS */;
INSERT INTO `course_comments` VALUES (1,1,1,'课程内容非常详细，老师讲解清晰，对初学者很友好，学完后对HTML和CSS有了系统的了解。',5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(2,1,2,'课程质量很高，但是有些知识点讲解有点快，希望能够放慢节奏。',4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(3,1,5,'实战项目很有帮助，让我学以致用，巩固了所学知识。',5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(4,2,6,'老师对JavaScript的理解很深入，讲解的例子也很贴近实际工作场景，收获很大。',5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(5,2,8,'课程内容有深度，但是有些概念讲解不够清晰，需要自己额外查阅资料。',3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(6,3,1,'Vue.js课程非常实用，项目实战部分设计得很好，学完后能够独立开发Vue应用。',5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(7,3,5,'老师讲解思路清晰，代码规范，对提升我的Vue开发能力帮助很大。',5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(8,4,2,'Python数据分析课程内容丰富，案例生动，学完后能够处理实际工作中的数据分析任务。',4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(9,4,6,'课程内容与实际工作结合紧密，老师经验丰富，讲解到位。',5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(10,5,1,'Java企业级应用开发课程很全面，从基础到高级都有覆盖，学习曲线设计合理。',4,'2025-11-21 13:45:30','2025-11-21 13:45:30');
/*!40000 ALTER TABLE `course_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_daily_statistics`
--

DROP TABLE IF EXISTS `course_daily_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_daily_statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `study_count` int NOT NULL DEFAULT '0' COMMENT '学习人数',
  `study_time` int NOT NULL DEFAULT '0' COMMENT '学习时长(分钟)',
  `new_student_count` int NOT NULL DEFAULT '0' COMMENT '新增学员数',
  `comment_count` int NOT NULL DEFAULT '0' COMMENT '评论数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_course_date` (`course_id`,`stat_date`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程每日学习统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_daily_statistics`
--

LOCK TABLES `course_daily_statistics` WRITE;
/*!40000 ALTER TABLE `course_daily_statistics` DISABLE KEYS */;
INSERT INTO `course_daily_statistics` VALUES (1,1,'2023-06-01',85,68,3200,5,6,'2025-11-21 13:45:30'),(2,1,'2023-06-02',92,75,3500,4,8,'2025-11-21 13:45:30'),(3,1,'2023-06-03',88,70,3300,3,7,'2025-11-21 13:45:30'),(4,1,'2023-06-04',95,78,3600,5,9,'2025-11-21 13:45:30'),(5,1,'2023-06-05',90,72,3400,4,8,'2025-11-21 13:45:30'),(6,2,'2023-06-01',78,62,2800,4,5,'2025-11-21 13:45:30'),(7,2,'2023-06-02',85,68,3100,3,7,'2025-11-21 13:45:30'),(8,2,'2023-06-03',82,65,2900,4,6,'2025-11-21 13:45:30'),(9,2,'2023-06-04',88,70,3200,5,8,'2025-11-21 13:45:30'),(10,2,'2023-06-05',84,67,3000,3,7,'2025-11-21 13:45:30'),(11,3,'2023-06-01',65,52,2400,3,4,'2025-11-21 13:45:30'),(12,3,'2023-06-02',70,56,2600,2,5,'2025-11-21 13:45:30'),(13,3,'2023-06-03',68,54,2500,3,5,'2025-11-21 13:45:30'),(14,3,'2023-06-04',72,58,2700,4,6,'2025-11-21 13:45:30'),(15,3,'2023-06-05',69,55,2550,3,5,'2025-11-21 13:45:30'),(16,4,'2023-06-01',55,44,2000,2,3,'2025-11-21 13:45:30'),(17,4,'2023-06-02',60,48,2200,3,4,'2025-11-21 13:45:30'),(18,4,'2023-06-03',58,46,2100,2,4,'2025-11-21 13:45:30'),(19,4,'2023-06-04',62,50,2300,3,5,'2025-11-21 13:45:30'),(20,4,'2023-06-05',59,47,2150,2,4,'2025-11-21 13:45:30'),(21,5,'2023-06-01',45,36,1600,2,3,'2025-11-21 13:45:30'),(22,5,'2023-06-02',48,38,1700,1,3,'2025-11-21 13:45:30'),(23,5,'2023-06-03',47,37,1650,2,3,'2025-11-21 13:45:30'),(24,5,'2023-06-04',50,40,1800,2,4,'2025-11-21 13:45:30'),(25,5,'2023-06-05',49,39,1750,2,3,'2025-11-21 13:45:30'),(26,6,'2023-06-01',0,0,0,0,0,'2025-11-21 13:45:30'),(27,6,'2023-06-02',0,0,0,0,0,'2025-11-21 13:45:30'),(28,6,'2023-06-03',120,1,2,0,0,'2025-11-21 13:45:30'),(29,6,'2023-06-04',0,0,0,0,0,'2025-11-21 13:45:30'),(30,6,'2023-06-05',135,1,3,0,0,'2025-11-21 13:45:30'),(31,9,'2023-06-01',0,0,0,0,0,'2025-11-21 13:45:30'),(32,9,'2023-06-02',0,0,0,0,0,'2025-11-21 13:45:30'),(33,9,'2023-06-03',0,0,0,0,0,'2025-11-21 13:45:30'),(34,9,'2023-06-04',165,1,3,0,0,'2025-11-21 13:45:30'),(35,9,'2023-06-05',0,0,0,0,0,'2025-11-21 13:45:30');
/*!40000 ALTER TABLE `course_daily_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_favorites`
--

DROP TABLE IF EXISTS `course_favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_favorites` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_course` (`user_id`,`course_id`),
  KEY `fk_favorites_course` (`course_id`),
  CONSTRAINT `fk_favorites_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `fk_favorites_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_favorites`
--

LOCK TABLES `course_favorites` WRITE;
/*!40000 ALTER TABLE `course_favorites` DISABLE KEYS */;
INSERT INTO `course_favorites` VALUES (1,1,4,'2025-11-21 13:45:30'),(2,1,7,'2025-11-21 13:45:30'),(3,2,3,'2025-11-21 13:45:30'),(4,2,6,'2025-11-21 13:45:30'),(5,5,1,'2025-11-21 13:45:30'),(6,5,9,'2025-11-21 13:45:30'),(7,6,2,'2025-11-21 13:45:30'),(8,6,8,'2025-11-21 13:45:30'),(9,8,5,'2025-11-21 13:45:30'),(10,8,10,'2025-11-21 13:45:30'),(11,9,3,'2025-11-21 13:45:30'),(12,9,7,'2025-11-21 13:45:30');
/*!40000 ALTER TABLE `course_favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `title` varchar(100) NOT NULL COMMENT '课程标题',
  `description` text COMMENT '课程描述',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片URL',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `level` enum('初级','中级','高级') DEFAULT NULL COMMENT '难度级别(初级/中级/高级)',
  `duration` int DEFAULT '0' COMMENT '课程总时长(分钟)',
  `teacher_id` bigint NOT NULL COMMENT '教师ID',
  `status` enum('draft','published','offline') DEFAULT 'draft' COMMENT '状态(draft/published/offline)',
  `student_count` int DEFAULT '0' COMMENT '学生数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_category_id` (`category_id`),
  FULLTEXT KEY `ft_title_desc` (`title`,`description`),
  CONSTRAINT `fk_courses_category` FOREIGN KEY (`category_id`) REFERENCES `course_categories` (`id`),
  CONSTRAINT `fk_courses_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'HTML5+CSS3零基础入门到精通','本课程从零开始，详细讲解HTML5和CSS3的基础知识和高级应用，通过实战项目帮助学员掌握前端开发的核心技能。适合零基础学员和需要提升技能的前端开发者。','/covers/html-css.jpg',99.00,7,'初级',1800,3,'published',1250,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(2,'JavaScript高级程序设计','深入学习JavaScript语言精髓，掌握ES6+新特性，理解闭包、原型链、异步编程等核心概念，提升前端开发能力。','/covers/javascript.jpg',129.00,7,'中级',2400,3,'published',980,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(3,'Vue.js实战项目开发','基于Vue.js框架的实战课程，从组件化思想到Vuex状态管理，从路由配置到项目优化，全方位讲解Vue开发技巧。','/covers/vuejs.jpg',159.00,7,'中级',2100,7,'published',865,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(4,'Python数据分析与可视化','使用Python进行数据分析和可视化的实用课程，涵盖NumPy、Pandas、Matplotlib等库的使用，以及数据清洗、转换、分析和可视化的完整流程。','/covers/python-data.jpg',199.00,10,'中级',2700,7,'published',723,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(5,'Java企业级应用开发','从Java基础到企业级应用开发，包括Spring Boot、Spring Cloud、MyBatis等框架的使用，数据库设计与优化，以及微服务架构的实践。','/covers/java.jpg',249.00,8,'高级',3600,3,'published',542,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(6,'UI设计入门到精通','系统学习UI设计原则、色彩搭配、字体排版、图标设计等内容，掌握Sketch、Figma等主流设计工具，提升产品设计能力。','/covers/ui-design.jpg',149.00,11,'初级',2100,4,'published',1105,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(7,'商业插画设计实战','学习商业插画设计的核心技法，包括人物角色、场景、构图等，掌握Procreate、Photoshop等工具的使用技巧，提升插画创作能力。','/covers/illustration.jpg',179.00,12,'中级',2400,4,'published',658,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(8,'新概念英语精讲','系统讲解新概念英语教程，从发音、词汇到语法、阅读，全方位提升英语能力，适合英语学习者和备考人群。','/covers/english.jpg',129.00,14,'初级',3000,7,'published',1560,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(9,'日语N2备考指南','针对日语能力考试N2级别的系统备考课程，包括词汇、语法、听力、阅读等全方位训练，助你一次通过N2考试。','/covers/japanese.jpg',159.00,15,'中级',2700,4,'published',432,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(10,'项目管理PMP认证课程','系统讲解项目管理知识体系(PMBOK)，结合实际案例分析，帮助学员掌握项目管理方法论，顺利通过PMP认证考试。','/covers/pmp.jpg',299.00,17,'高级',3600,3,'published',378,'2025-11-21 13:45:30','2025-11-21 13:45:30');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learning_progress`
--

DROP TABLE IF EXISTS `learning_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learning_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '进度ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `section_id` bigint NOT NULL COMMENT '课时ID',
  `position` int DEFAULT '0' COMMENT '视频播放位置(秒)',
  `finished` tinyint(1) DEFAULT '0' COMMENT '是否完成',
  `last_learn_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后学习时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_section` (`user_id`,`section_id`),
  KEY `idx_user_course` (`user_id`,`course_id`),
  KEY `fk_progress_course` (`course_id`),
  KEY `fk_progress_section` (`section_id`),
  CONSTRAINT `fk_progress_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `fk_progress_section` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`),
  CONSTRAINT `fk_progress_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学习进度表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learning_progress`
--

LOCK TABLES `learning_progress` WRITE;
/*!40000 ALTER TABLE `learning_progress` DISABLE KEYS */;
INSERT INTO `learning_progress` VALUES (1,1,1,1,1800,1,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30'),(2,1,1,2,1500,1,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30'),(3,1,1,3,900,0,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30'),(4,2,1,1,1800,1,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30'),(5,2,1,2,1500,1,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30'),(6,2,2,18,600,0,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30'),(7,5,3,10,1800,1,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30'),(8,5,3,11,1200,0,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30'),(9,6,4,15,1800,1,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30'),(10,6,4,16,1500,1,'2025-11-21 13:45:30','2025-11-21 13:45:30','2025-11-21 13:45:30');
/*!40000 ALTER TABLE `learning_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` bigint NOT NULL COMMENT '接收用户ID',
  `type` enum('system','course','comment','qa') NOT NULL COMMENT '消息类型(system/course/comment/qa)',
  `title` varchar(100) NOT NULL COMMENT '消息标题',
  `content` text NOT NULL COMMENT '消息内容',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `link` varchar(255) DEFAULT NULL COMMENT '相关链接',
  `sender_id` bigint DEFAULT NULL COMMENT '发送者ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_type` (`type`),
  KEY `fk_notifications_sender` (`sender_id`),
  CONSTRAINT `fk_notifications_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,1,'course','课程更新通知','HTML5+CSS3零基础入门到精通课程已更新第4章内容，快来学习吧！',0,'/courses/1/chapters/4',3,'2025-11-21 13:45:30'),(2,1,'comment','评论回复通知','王强老师回复了你在JavaScript高级程序设计课程中的评论',1,'/courses/2/comments',3,'2025-11-21 13:45:30'),(3,1,'qa','问题回答通知','你的问题\"Vue组件之间如何进行通信？\"收到了新回答',0,'/community/questions/1',7,'2025-11-21 13:45:30'),(4,1,'system','作业评分通知','你的作业\"HTML5页面结构设计\"已评分完成，得分：92',1,'/assignments/1/submissions/1',NULL,'2025-11-21 13:45:30'),(5,2,'course','课程更新通知','JavaScript高级程序设计课程已更新第5章内容，快来学习吧！',0,'/courses/2/chapters/5',3,'2025-11-21 13:45:30'),(6,2,'qa','问题采纳通知','你的回答被问题\"JavaScript中的闭包是什么？\"采纳为最佳答案',1,'/community/questions/2',1,'2025-11-21 13:45:30'),(7,2,'system','作业评分通知','你的作业\"HTML5页面结构设计\"已评分完成，得分：95',0,'/assignments/1/submissions/2',NULL,'2025-11-21 13:45:30'),(8,2,'system','作业评分通知','你的作业\"JavaScript闭包与模块模式\"已评分完成，得分：98',0,'/assignments/3/submissions/4',NULL,'2025-11-21 13:45:30'),(9,5,'course','课程更新通知','Vue.js实战项目开发课程已更新第5章内容，快来学习吧！',0,'/courses/3/chapters/5',7,'2025-11-21 13:45:30'),(10,5,'qa','问题回答通知','你的问题\"CSS Grid和Flexbox有什么区别？\"收到了新回答',1,'/community/questions/3',3,'2025-11-21 13:45:30'),(11,5,'system','作业截止提醒','作业\"Vue.js组件开发\"将在明天截止，请及时提交',0,'/assignments/4',NULL,'2025-11-21 13:45:30'),(12,6,'course','课程更新通知','Python数据分析与可视化课程已更新第4章内容，快来学习吧！',1,'/courses/4/chapters/4',7,'2025-11-21 13:45:30'),(13,6,'qa','问题回答通知','你的问题\"Python数据分析中如何处理缺失值？\"收到了新回答',0,'/community/questions/4',3,'2025-11-21 13:45:30'),(14,6,'system','作业截止提醒','作业\"Python数据分析报告\"将在3天后截止，请及时提交',0,'/assignments/5',NULL,'2025-11-21 13:45:30'),(15,9,'course','课程更新通知','Java企业级应用开发课程已更新第3章内容，快来学习吧！',1,'/courses/5/chapters/3',3,'2025-11-21 13:45:30'),(16,9,'qa','问题回答通知','你的问题\"Java Spring Boot如何实现权限控制？\"收到了新回答',0,'/community/questions/5',7,'2025-11-21 13:45:30'),(17,9,'system','课程推荐','根据你的学习记录，推荐你学习\"Spring Cloud微服务架构\"课程',1,'/courses/11',NULL,'2025-11-21 13:45:30');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_daily_statistics`
--

DROP TABLE IF EXISTS `platform_daily_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_daily_statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `total_users` int NOT NULL COMMENT '平台总用户数',
  `new_users` int NOT NULL COMMENT '新增用户数',
  `active_users` int NOT NULL COMMENT '活跃用户数',
  `course_views` int NOT NULL COMMENT '课程浏览次数',
  `lesson_views` int NOT NULL COMMENT '课时浏览次数',
  `total_study_time` int NOT NULL COMMENT '总学习时长(分钟)',
  `assignment_submissions` int NOT NULL COMMENT '作业提交数',
  `question_posts` int NOT NULL COMMENT '问题发布数',
  `answer_posts` int NOT NULL COMMENT '回答发布数',
  `comment_posts` int NOT NULL COMMENT '评论发布数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='平台每日汇总数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_daily_statistics`
--

LOCK TABLES `platform_daily_statistics` WRITE;
/*!40000 ALTER TABLE `platform_daily_statistics` DISABLE KEYS */;
INSERT INTO `platform_daily_statistics` VALUES (1,'2023-06-01',120,15,78,356,289,12450,12,8,15,24,'2025-11-21 13:45:30'),(2,'2023-06-02',125,5,82,378,305,13200,14,10,18,31,'2025-11-21 13:45:30'),(3,'2023-06-03',132,7,85,412,328,14100,15,12,22,35,'2025-11-21 13:45:30'),(4,'2023-06-04',140,8,90,435,342,15300,18,15,26,42,'2025-11-21 13:45:30'),(5,'2023-06-05',145,5,88,410,325,14800,16,14,24,38,'2025-11-21 13:45:30'),(6,'2023-06-06',150,5,92,425,338,15600,19,16,28,45,'2025-11-21 13:45:30'),(7,'2023-06-07',158,8,95,442,356,16200,21,18,32,49,'2025-11-21 13:45:30'),(8,'2023-06-08',165,7,98,458,372,16800,23,20,35,52,'2025-11-21 13:45:30'),(9,'2023-06-09',172,7,102,475,385,17500,25,22,38,56,'2025-11-21 13:45:30'),(10,'2023-06-10',180,8,105,492,398,18200,28,25,42,62,'2025-11-21 13:45:30'),(11,'2023-06-11',185,5,100,465,375,17200,24,21,36,54,'2025-11-21 13:45:30'),(12,'2023-06-12',190,5,103,478,382,17600,26,23,39,58,'2025-11-21 13:45:30'),(13,'2023-06-13',198,8,108,495,402,18500,29,26,43,64,'2025-11-21 13:45:30'),(14,'2023-06-14',205,7,112,512,418,19200,32,28,46,68,'2025-11-21 13:45:30'),(15,'2023-06-15',212,7,115,528,432,19800,34,30,49,72,'2025-11-21 13:45:30'),(16,'2023-06-16',220,8,118,545,445,20500,36,32,52,76,'2025-11-21 13:45:30'),(17,'2023-06-17',225,5,114,520,425,19500,33,29,48,70,'2025-11-21 13:45:30'),(18,'2023-06-18',230,5,110,505,410,18800,30,27,45,66,'2025-11-21 13:45:30'),(19,'2023-06-19',238,8,116,525,428,19700,34,31,50,73,'2025-11-21 13:45:30'),(20,'2023-06-20',245,7,120,542,442,20300,37,33,53,78,'2025-11-21 13:45:30'),(21,'2023-06-21',252,7,125,560,458,21000,39,35,56,82,'2025-11-21 13:45:30'),(22,'2023-06-22',260,8,130,578,472,21800,42,38,60,87,'2025-11-21 13:45:30'),(23,'2023-06-23',268,8,135,595,485,22500,45,40,63,92,'2025-11-21 13:45:30'),(24,'2023-06-24',275,7,140,612,498,23200,48,43,67,96,'2025-11-21 13:45:30'),(25,'2023-06-25',282,7,145,630,512,24000,51,45,70,102,'2025-11-21 13:45:30'),(26,'2023-06-26',290,8,150,648,525,24800,54,48,74,108,'2025-11-21 13:45:30'),(27,'2023-06-27',295,5,145,625,508,23800,50,44,69,100,'2025-11-21 13:45:30'),(28,'2023-06-28',302,7,152,645,522,24500,53,47,73,106,'2025-11-21 13:45:30'),(29,'2023-06-29',310,8,158,662,535,25300,56,50,77,112,'2025-11-21 13:45:30'),(30,'2023-06-30',318,8,165,680,548,26100,59,53,81,118,'2025-11-21 13:45:30');
/*!40000 ALTER TABLE `platform_daily_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_tags`
--

DROP TABLE IF EXISTS `question_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_tags` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `question_id` bigint NOT NULL COMMENT '问题ID',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_question_tag` (`question_id`,`tag_name`),
  KEY `idx_question_id` (`question_id`),
  CONSTRAINT `fk_question_tags_question` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_tags`
--

LOCK TABLES `question_tags` WRITE;
/*!40000 ALTER TABLE `question_tags` DISABLE KEYS */;
INSERT INTO `question_tags` VALUES (1,1,'Vue.js'),(3,1,'Vuex'),(2,1,'组件通信'),(4,2,'JavaScript'),(6,2,'作用域'),(5,2,'闭包'),(7,3,'CSS'),(9,3,'Flexbox'),(8,3,'Grid'),(10,3,'响应式布局'),(12,4,'Pandas'),(11,4,'Python'),(13,4,'数据清洗'),(14,5,'Java'),(15,5,'Spring Boot'),(17,5,'Spring Security'),(16,5,'权限控制');
/*!40000 ALTER TABLE `question_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '问题ID',
  `title` varchar(200) NOT NULL COMMENT '问题标题',
  `content` text NOT NULL COMMENT '问题内容',
  `user_id` bigint NOT NULL COMMENT '提问用户ID',
  `course_id` bigint DEFAULT NULL COMMENT '关联课程ID',
  `view_count` int DEFAULT '0' COMMENT '浏览次数',
  `answer_count` int DEFAULT '0' COMMENT '回答数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_answer_time` datetime DEFAULT NULL COMMENT '最后回答时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_id` (`course_id`),
  FULLTEXT KEY `ft_title_content` (`title`,`content`),
  CONSTRAINT `fk_questions_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'Vue组件之间如何进行通信？','我在学习Vue.js课程时，对于父子组件、兄弟组件之间的通信方式有些混淆，特别是Vuex和事件总线的使用场景，希望能有详细解答。',1,3,156,3,'2025-11-21 13:45:30','2025-11-21 13:45:30',NULL),(2,'JavaScript中的闭包是什么？','在学习JavaScript高级程序设计时，对闭包的概念理解不清晰，特别是它与作用域的关系，以及在实际开发中的应用场景，希望能有详细解释。',2,2,245,4,'2025-11-21 13:45:30','2025-11-21 13:45:30',NULL),(3,'CSS Grid和Flexbox有什么区别？','在学习响应式布局时，不太理解CSS Grid和Flexbox的适用场景和区别，希望能有具体的实例说明。',5,1,189,2,'2025-11-21 13:45:30','2025-11-21 13:45:30',NULL),(4,'Python数据分析中如何处理缺失值？','在使用Pandas进行数据分析时，遇到了大量缺失值，不知道应该采用什么策略处理，是直接删除还是填充，希望能有最佳实践分享。',6,4,132,3,'2025-11-21 13:45:30','2025-11-21 13:45:30',NULL),(5,'Java Spring Boot如何实现权限控制？','在开发企业应用时，需要实现基于角色的权限控制，不太清楚Spring Security的配置方法，希望能有详细的步骤指导。',9,5,178,2,'2025-11-21 13:45:30','2025-11-21 13:45:30',NULL);
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sections`
--

DROP TABLE IF EXISTS `sections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sections` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课时ID',
  `chapter_id` bigint NOT NULL COMMENT '章节ID',
  `title` varchar(100) NOT NULL COMMENT '课时标题',
  `video_url` varchar(255) DEFAULT NULL COMMENT '视频URL',
  `duration` int DEFAULT '0' COMMENT '时长(分钟)',
  `is_free` tinyint(1) DEFAULT '0' COMMENT '是否免费试看',
  `sort` int DEFAULT '0' COMMENT '排序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_chapter_id` (`chapter_id`),
  CONSTRAINT `fk_sections_chapter` FOREIGN KEY (`chapter_id`) REFERENCES `chapters` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课时表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sections`
--

LOCK TABLES `sections` WRITE;
/*!40000 ALTER TABLE `sections` DISABLE KEYS */;
INSERT INTO `sections` VALUES (1,1,'HTML5简介与环境搭建','/videos/html5-intro.mp4',30,1,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(2,1,'HTML5文档结构','/videos/html5-structure.mp4',25,1,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(3,1,'HTML5常用标签','/videos/html5-tags.mp4',45,0,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(4,1,'HTML5表单元素','/videos/html5-forms.mp4',40,0,4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(5,1,'HTML5多媒体元素','/videos/html5-media.mp4',35,0,5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(6,2,'CSS3选择器','/videos/css3-selectors.mp4',30,1,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(7,2,'CSS3盒模型','/videos/css3-box.mp4',25,0,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(8,2,'CSS3布局技术','/videos/css3-layout.mp4',45,0,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(9,2,'CSS3动画与过渡','/videos/css3-animation.mp4',40,0,4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(10,2,'CSS3媒体查询','/videos/css3-media.mp4',35,0,5,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(11,3,'响应式设计原则','/videos/responsive-principles.mp4',30,0,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(12,3,'Flexbox布局详解','/videos/flexbox.mp4',40,0,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(13,3,'Grid布局详解','/videos/grid.mp4',45,0,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(14,3,'移动端适配技巧','/videos/mobile-adaptation.mp4',35,0,4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(15,4,'个人简历项目开发','/videos/resume-project.mp4',60,0,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(16,4,'企业官网项目开发','/videos/company-website.mp4',90,0,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(17,4,'电商网站项目开发','/videos/ecommerce-project.mp4',120,0,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(18,5,'JavaScript语言特性','/videos/js-features.mp4',35,1,1,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(19,5,'变量、数据类型与运算符','/videos/js-variables.mp4',40,1,2,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(20,5,'流程控制与循环','/videos/js-control.mp4',30,0,3,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(21,5,'函数与作用域','/videos/js-functions.mp4',45,0,4,'2025-11-21 13:45:30','2025-11-21 13:45:30'),(22,5,'对象与原型链','/videos/js-objects.mp4',50,0,5,'2025-11-21 13:45:30','2025-11-21 13:45:30');
/*!40000 ALTER TABLE `sections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statistics`
--

DROP TABLE IF EXISTS `statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `course_id` bigint DEFAULT NULL COMMENT '课程ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `stat_type` varchar(50) NOT NULL COMMENT '统计类型(learning_time/income/login)',
  `value` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '统计值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`,`stat_date`),
  KEY `idx_course_date` (`course_id`,`stat_date`),
  KEY `idx_type_date` (`stat_type`,`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='统计数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statistics`
--

LOCK TABLES `statistics` WRITE;
/*!40000 ALTER TABLE `statistics` DISABLE KEYS */;
/*!40000 ALTER TABLE `statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission_attachments`
--

DROP TABLE IF EXISTS `submission_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `submission_attachments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '附件ID',
  `submission_id` bigint NOT NULL COMMENT '提交ID',
  `file_url` varchar(255) NOT NULL COMMENT '文件URL',
  `file_name` varchar(100) NOT NULL COMMENT '文件名',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小',
  PRIMARY KEY (`id`),
  KEY `idx_submission_id` (`submission_id`),
  CONSTRAINT `fk_submission_attachments_submission` FOREIGN KEY (`submission_id`) REFERENCES `assignment_submissions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='提交附件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission_attachments`
--

LOCK TABLES `submission_attachments` WRITE;
/*!40000 ALTER TABLE `submission_attachments` DISABLE KEYS */;
INSERT INTO `submission_attachments` VALUES (1,1,'/submissions/1/index.html','index.html',4567),(2,1,'/submissions/1/screenshot.png','页面截图.png',567890),(3,2,'/submissions/2/structure.html','structure.html',5678),(4,2,'/submissions/2/screenshot.jpg','页面截图.jpg',678901),(5,3,'/submissions/3/responsive.html','responsive.html',6789),(6,3,'/submissions/3/styles.css','styles.css',3456),(7,3,'/submissions/3/screenshots.zip','多设备截图.zip',1234567),(8,4,'/submissions/4/counter.js','counter.js',2345),(9,4,'/submissions/4/test.js','test.js',3456),(10,5,'/submissions/5/ShoppingCart.vue','ShoppingCart.vue',7890),(11,5,'/submissions/5/ProductList.vue','ProductList.vue',5678),(12,5,'/submissions/5/CartList.vue','CartList.vue',6789),(13,5,'/submissions/5/CartSummary.vue','CartSummary.vue',4567),(14,5,'/submissions/5/demo.gif','功能演示.gif',2345678);
/*!40000 ALTER TABLE `submission_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID，0表示根菜单',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `menu_type` enum('DIR','MENU','BUTTON') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单类型：DIR-目录，MENU-菜单，BUTTON-按钮',
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限标识，如：system:menu:edit',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单图标',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由路径',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组件路径',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_permission` (`permission`) USING BTREE COMMENT '权限标识唯一索引',
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'系统管理','DIR',NULL,'system','/system',NULL,1,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(2,1,'用户管理','MENU','system:user:view','user','user','system/user/index',1,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(3,2,'用户新增','BUTTON','system:user:add',NULL,NULL,NULL,1,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(4,2,'用户编辑','BUTTON','system:user:edit',NULL,NULL,NULL,2,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(5,2,'用户删除','BUTTON','system:user:delete',NULL,NULL,NULL,3,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(6,1,'角色管理','MENU','system:role:view','role','role','system/role/index',2,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(7,6,'角色新增','BUTTON','system:role:add',NULL,NULL,NULL,1,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(8,6,'角色编辑','BUTTON','system:role:edit',NULL,NULL,NULL,2,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(9,1,'菜单管理','MENU','system:menu:view','menu','menu','system/menu/index',3,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(10,9,'菜单新增','BUTTON','system:menu:add',NULL,NULL,NULL,1,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(11,9,'菜单编辑','BUTTON','system:menu:edit',NULL,NULL,NULL,2,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(12,9,'菜单删除','BUTTON','system:menu:delete',NULL,NULL,NULL,3,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(13,0,'课程管理','DIR',NULL,'book','/course',NULL,2,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(14,14,'课程列表','MENU','course:view','list','list','course/index',1,1,'2026-01-23 23:12:15','2026-01-24 22:24:26'),(15,14,'课程新增','BUTTON','course:add',NULL,NULL,NULL,1,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(16,14,'课程编辑','BUTTON','course:edit',NULL,NULL,NULL,2,1,'2026-01-23 23:12:15','2026-01-23 23:12:15'),(17,14,'课程删除','BUTTON','course:delete',NULL,NULL,NULL,3,1,'2026-01-23 23:12:15','2026-01-23 23:12:15');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称（如"ROLE_USER"、"ROLE_ADMIN"）',
  `role_desc` varchar(200) DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'ROLE_ADMIN','系统管理员（拥有全部权限）'),(2,'ROLE_TEACHER','教师（基础操作权限）'),(3,'ROLE_STUDENT','学生（基础操作权限）');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE,
  KEY `idx_menu_id` (`menu_id`) USING BTREE,
  CONSTRAINT `fk_role_menu_menu` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_role_menu_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(2,13),(3,13),(1,14),(2,14),(3,14),(1,15),(2,15),(1,16),(2,16),(1,17),(2,17);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID（关联sys_user.id）',
  `role_id` bigint NOT NULL COMMENT '角色ID（关联sys_role.id）',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (10,1),(3,2),(4,2),(7,2),(1,3),(2,3),(5,3),(6,3),(8,3),(9,3);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uploaded_files`
--

DROP TABLE IF EXISTS `uploaded_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uploaded_files` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `user_id` bigint NOT NULL COMMENT '上传用户ID',
  `file_name` varchar(100) NOT NULL COMMENT '文件名',
  `file_url` varchar(255) NOT NULL COMMENT '文件URL',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小(字节)',
  `file_type` enum('image','video') NOT NULL COMMENT '文件类型(image/video)',
  `duration` int DEFAULT NULL COMMENT '视频时长(秒)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_file_type` (`file_type`),
  CONSTRAINT `fk_uploaded_files_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件上传记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uploaded_files`
--

LOCK TABLES `uploaded_files` WRITE;
/*!40000 ALTER TABLE `uploaded_files` DISABLE KEYS */;
/*!40000 ALTER TABLE `uploaded_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_action_logs`
--

DROP TABLE IF EXISTS `user_action_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_action_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `login_status` tinyint NOT NULL COMMENT '登录状态：1-成功，0-失败',
  `fail_reason` varchar(200) DEFAULT NULL COMMENT '失败原因（如"密码错误"、"账号锁定"）',
  `action_type` varchar(50) NOT NULL COMMENT '操作类型',
  `action_desc` varchar(255) DEFAULT NULL COMMENT '操作描述',
  `resource_type` varchar(50) DEFAULT NULL COMMENT '资源类型',
  `resource_id` bigint DEFAULT NULL COMMENT '资源ID',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(255) DEFAULT NULL COMMENT '用户代理',
  `request_url` varchar(255) DEFAULT NULL COMMENT '请求URL',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `request_params` text COMMENT '请求参数',
  `response_code` int DEFAULT NULL COMMENT '响应状态码',
  `duration` int DEFAULT NULL COMMENT '操作耗时(毫秒)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_action_type` (`action_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_action_logs`
--

LOCK TABLES `user_action_logs` WRITE;
/*!40000 ALTER TABLE `user_action_logs` DISABLE KEYS */;
INSERT INTO `user_action_logs` VALUES (1,1,1,NULL,'login','用户登录',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-01 08:30:15'),(2,1,1,NULL,'view_course','查看课程: HTML5+CSS3零基础入门到精通',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-01 08:35:22'),(3,1,1,NULL,'study_lesson','学习课时: HTML5基础语法',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-01 08:40:35'),(4,1,1,NULL,'submit_assignment','提交作业: HTML5页面结构设计',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-28 15:30:45'),(5,1,1,NULL,'post_question','发布问题: Vue组件之间如何进行通信？',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-05 14:25:18'),(6,1,1,NULL,'logout','用户退出',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-01 10:15:42'),(7,1,1,NULL,'login','用户登录',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-02 09:10:25'),(8,1,1,NULL,'view_course','查看课程: JavaScript高级程序设计',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-02 09:15:33'),(9,1,1,NULL,'favorite_course','收藏课程: JavaScript高级程序设计',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-02 09:20:12'),(10,1,1,NULL,'post_comment','发表评论: JavaScript高级程序设计',NULL,NULL,'192.168.1.101','Chrome 95.0.4638.69 / Windows 10',NULL,NULL,NULL,NULL,NULL,'2023-06-02 09:45:28'),(11,2,1,NULL,'login','用户登录',NULL,NULL,'192.168.2.205','Safari 15.1 / macOS 12.0.1',NULL,NULL,NULL,NULL,NULL,'2023-06-01 10:20:35'),(12,2,1,NULL,'view_course','查看课程: JavaScript高级程序设计',NULL,NULL,'192.168.2.205','Safari 15.1 / macOS 12.0.1',NULL,NULL,NULL,NULL,NULL,'2023-06-01 10:25:42'),(13,2,1,NULL,'study_lesson','学习课时: JavaScript基本语法',NULL,NULL,'192.168.2.205','Safari 15.1 / macOS 12.0.1',NULL,NULL,NULL,NULL,NULL,'2023-06-01 10:30:18'),(14,2,1,NULL,'post_question','发布问题: JavaScript中的闭包是什么？',NULL,NULL,'192.168.2.205','Safari 15.1 / macOS 12.0.1',NULL,NULL,NULL,NULL,NULL,'2023-06-03 16:45:22'),(15,2,1,NULL,'submit_assignment','提交作业: JavaScript闭包与模块模式',NULL,NULL,'192.168.2.205','Safari 15.1 / macOS 12.0.1',NULL,NULL,NULL,NULL,NULL,'2023-06-24 11:25:38'),(16,2,1,NULL,'logout','用户退出',NULL,NULL,'192.168.2.205','Safari 15.1 / macOS 12.0.1',NULL,NULL,NULL,NULL,NULL,'2023-06-01 12:05:33'),(17,3,1,NULL,'login','用户登录',NULL,NULL,'10.0.0.15','Firefox 94.0 / Ubuntu 20.04',NULL,NULL,NULL,NULL,NULL,'2023-06-01 07:50:12'),(18,3,1,NULL,'update_course','更新课程: HTML5+CSS3零基础入门到精通',NULL,NULL,'10.0.0.15','Firefox 94.0 / Ubuntu 20.04',NULL,NULL,NULL,NULL,NULL,'2023-06-01 08:15:33'),(19,3,1,NULL,'reply_comment','回复评论: JavaScript高级程序设计',NULL,NULL,'10.0.0.15','Firefox 94.0 / Ubuntu 20.04',NULL,NULL,NULL,NULL,NULL,'2023-06-02 10:30:45'),(20,3,1,NULL,'grade_assignment','评分作业: HTML5页面结构设计',NULL,NULL,'10.0.0.15','Firefox 94.0 / Ubuntu 20.04',NULL,NULL,NULL,NULL,NULL,'2023-07-01 10:15:22'),(21,3,1,NULL,'answer_question','回答问题: JavaScript中的闭包是什么？',NULL,NULL,'10.0.0.15','Firefox 94.0 / Ubuntu 20.04',NULL,NULL,NULL,NULL,NULL,'2023-06-04 09:25:18'),(22,3,1,NULL,'logout','用户退出',NULL,NULL,'10.0.0.15','Firefox 94.0 / Ubuntu 20.04',NULL,NULL,NULL,NULL,NULL,'2023-06-01 16:45:22'),(23,5,1,NULL,'login','用户登录',NULL,NULL,'172.16.10.25','Edge 95.0.1020.40 / Windows 11',NULL,NULL,NULL,NULL,NULL,'2023-06-02 14:20:15'),(24,5,1,NULL,'view_course','查看课程: Vue.js实战项目开发',NULL,NULL,'172.16.10.25','Edge 95.0.1020.40 / Windows 11',NULL,NULL,NULL,NULL,NULL,'2023-06-02 14:25:33'),(25,5,1,NULL,'study_lesson','学习课时: Vue.js组件基础',NULL,NULL,'172.16.10.25','Edge 95.0.1020.40 / Windows 11',NULL,NULL,NULL,NULL,NULL,'2023-06-02 14:35:42'),(26,5,1,NULL,'post_question','发布问题: CSS Grid和Flexbox有什么区别？',NULL,NULL,'172.16.10.25','Edge 95.0.1020.40 / Windows 11',NULL,NULL,NULL,NULL,NULL,'2023-06-04 11:30:18'),(27,5,1,NULL,'submit_assignment','提交作业: Vue.js组件开发',NULL,NULL,'172.16.10.25','Edge 95.0.1020.40 / Windows 11',NULL,NULL,NULL,NULL,NULL,'2023-07-09 20:15:33'),(28,5,1,NULL,'logout','用户退出',NULL,NULL,'172.16.10.25','Edge 95.0.1020.40 / Windows 11',NULL,NULL,NULL,NULL,NULL,'2023-06-02 16:50:22'),(29,6,1,NULL,'login','用户登录',NULL,NULL,'192.168.5.78','Chrome 95.0.4638.69 / Android 12',NULL,NULL,NULL,NULL,NULL,'2023-06-03 19:15:33'),(30,6,1,NULL,'view_course','查看课程: Python数据分析与可视化',NULL,NULL,'192.168.5.78','Chrome 95.0.4638.69 / Android 12',NULL,NULL,NULL,NULL,NULL,'2023-06-03 19:20:45'),(31,6,1,NULL,'study_lesson','学习课时: Pandas基础',NULL,NULL,'192.168.5.78','Chrome 95.0.4638.69 / Android 12',NULL,NULL,NULL,NULL,NULL,'2023-06-03 19:30:22'),(32,6,1,NULL,'post_question','发布问题: Python数据分析中如何处理缺失值？',NULL,NULL,'192.168.5.78','Chrome 95.0.4638.69 / Android 12',NULL,NULL,NULL,NULL,NULL,'2023-06-05 20:45:18'),(33,6,1,NULL,'logout','用户退出',NULL,NULL,'192.168.5.78','Chrome 95.0.4638.69 / Android 12',NULL,NULL,NULL,NULL,NULL,'2023-06-03 21:10:33'),(34,7,1,NULL,'login','用户登录',NULL,NULL,'10.0.0.28','Chrome 95.0.4638.69 / macOS 12.0.1',NULL,NULL,NULL,NULL,NULL,'2023-06-01 09:30:15'),(35,7,1,NULL,'update_course','更新课程: Vue.js实战项目开发',NULL,NULL,'10.0.0.28','Chrome 95.0.4638.69 / macOS 12.0.1',NULL,NULL,NULL,NULL,NULL,'2023-06-01 10:15:33'),(36,7,1,NULL,'answer_question','回答问题: Vue组件之间如何进行通信？',NULL,NULL,'10.0.0.28','Chrome 95.0.4638.69 / macOS 12.0.1',NULL,NULL,NULL,NULL,NULL,'2023-06-05 15:20:18');
/*!40000 ALTER TABLE `user_action_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_courses`
--

DROP TABLE IF EXISTS `user_courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_courses` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `join_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `status` varchar(20) DEFAULT 'active' COMMENT '状态(active/expired)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_course` (`user_id`,`course_id`),
  KEY `idx_course_id` (`course_id`),
  CONSTRAINT `fk_user_courses_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `fk_user_courses_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生课程关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_courses`
--

LOCK TABLES `user_courses` WRITE;
/*!40000 ALTER TABLE `user_courses` DISABLE KEYS */;
INSERT INTO `user_courses` VALUES (1,1,1,'2023-01-15 10:30:00','2024-01-15 10:30:00','active'),(2,1,2,'2023-02-20 14:15:00','2024-02-20 14:15:00','active'),(3,1,3,'2023-03-10 09:45:00','2024-03-10 09:45:00','active'),(4,1,5,'2023-04-05 16:20:00','2024-04-05 16:20:00','active'),(5,2,1,'2023-01-18 11:20:00','2024-01-18 11:20:00','active'),(6,2,2,'2023-02-25 15:30:00','2024-02-25 15:30:00','active'),(7,2,4,'2023-03-15 10:45:00','2024-03-15 10:45:00','active'),(8,5,3,'2023-02-10 14:00:00','2024-02-10 14:00:00','active'),(9,5,6,'2023-03-20 09:30:00','2024-03-20 09:30:00','active'),(10,6,4,'2023-01-25 13:15:00','2024-01-25 13:15:00','active'),(11,6,7,'2023-02-28 16:45:00','2024-02-28 16:45:00','active'),(12,8,2,'2023-03-05 11:30:00','2024-03-05 11:30:00','active'),(13,8,8,'2023-04-10 14:45:00','2024-04-10 14:45:00','active'),(14,9,5,'2023-02-15 10:00:00','2024-02-15 10:00:00','active'),(15,9,10,'2023-03-25 15:15:00','2024-03-25 15:15:00','active');
/*!40000 ALTER TABLE `user_courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_study_statistics`
--

DROP TABLE IF EXISTS `user_study_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_study_statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `total_study_time` int NOT NULL DEFAULT '0' COMMENT '总学习时长(分钟)',
  `course_count` int NOT NULL DEFAULT '0' COMMENT '学习课程数',
  `lesson_count` int NOT NULL DEFAULT '0' COMMENT '学习课时数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`,`stat_date`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户每日学习统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_study_statistics`
--

LOCK TABLES `user_study_statistics` WRITE;
/*!40000 ALTER TABLE `user_study_statistics` DISABLE KEYS */;
INSERT INTO `user_study_statistics` VALUES (1,1,'2023-06-01',120,2,3,'2025-11-21 13:45:30'),(2,1,'2023-06-02',135,2,4,'2025-11-21 13:45:30'),(3,1,'2023-06-03',0,0,0,'2025-11-21 13:45:30'),(4,1,'2023-06-04',90,1,2,'2025-11-21 13:45:30'),(5,1,'2023-06-05',150,2,5,'2025-11-21 13:45:30'),(6,2,'2023-06-01',180,1,3,'2025-11-21 13:45:30'),(7,2,'2023-06-02',0,0,0,'2025-11-21 13:45:30'),(8,2,'2023-06-03',165,1,3,'2025-11-21 13:45:30'),(9,2,'2023-06-04',195,2,4,'2025-11-21 13:45:30'),(10,2,'2023-06-05',120,1,2,'2025-11-21 13:45:30'),(11,5,'2023-06-01',0,0,0,'2025-11-21 13:45:30'),(12,5,'2023-06-02',150,1,3,'2025-11-21 13:45:30'),(13,5,'2023-06-03',135,1,2,'2025-11-21 13:45:30'),(14,5,'2023-06-04',165,2,4,'2025-11-21 13:45:30'),(15,5,'2023-06-05',180,2,3,'2025-11-21 13:45:30'),(16,6,'2023-06-01',0,0,0,'2025-11-21 13:45:30'),(17,6,'2023-06-02',0,0,0,'2025-11-21 13:45:30'),(18,6,'2023-06-03',120,1,2,'2025-11-21 13:45:30'),(19,6,'2023-06-04',0,0,0,'2025-11-21 13:45:30'),(20,6,'2023-06-05',135,1,3,'2025-11-21 13:45:30'),(21,9,'2023-06-01',0,0,0,'2025-11-21 13:45:30'),(22,9,'2023-06-02',0,0,0,'2025-11-21 13:45:30'),(23,9,'2023-06-03',0,0,0,'2025-11-21 13:45:30'),(24,9,'2023-06-04',165,1,3,'2025-11-21 13:45:30'),(25,9,'2023-06-05',0,0,0,'2025-11-21 13:45:30');
/*!40000 ALTER TABLE `user_study_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户真实姓名',
  `password` varchar(100) NOT NULL COMMENT '密码(加密)',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '账号状态：1-正常，0-禁用，2-锁定（登录失败次数过多时）',
  `gender` tinyint NOT NULL COMMENT '性别：性别状态：1-男，0-女',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `password_expire_time` datetime DEFAULT NULL COMMENT '密码过期时间（配合密码有效期策略）',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`,`gender`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'张明','张明','e10adc3949ba59abbe56e057f20f883e','zhangming@example.com','13812345678',1,1,'/avatars/zhangming.jpg','2025-11-21 13:45:30','2026-01-21 23:02:45','2025-11-29 18:49:15','127.0.0.1',NULL),(2,'李娜','李娜','e10adc3949ba59abbe56e057f20f883e','lina@example.com','13912345678',1,0,'/avatars/lina.jpg','2025-11-21 13:45:30','2026-01-21 23:02:48',NULL,NULL,NULL),(3,'王强','王强','e10adc3949ba59abbe56e057f20f883e','wangqiang@example.com','13712345678',1,1,'/avatars/wangqiang.jpg','2025-11-21 13:45:30','2026-01-21 23:02:51',NULL,NULL,NULL),(4,'赵婷','赵婷','e10adc3949ba59abbe56e057f20f883e','zhaoting@example.com','13612345678',1,0,'/avatars/zhaoting.jpg','2025-11-21 13:45:30','2026-01-21 23:02:54',NULL,NULL,NULL),(5,'陈伟','陈伟','e10adc3949ba59abbe56e057f20f883e','chenwei@example.com','13512345678',1,1,'/avatars/chenwei.jpg','2025-11-21 13:45:30','2026-01-21 23:02:56',NULL,NULL,NULL),(6,'刘芳','刘芳','e10adc3949ba59abbe56e057f20f883e','liufang@example.com','13412345678',1,0,'/avatars/liufang.jpg','2025-11-21 13:45:30','2026-01-21 23:02:59',NULL,NULL,NULL),(7,'杨涛','杨涛','e10adc3949ba59abbe56e057f20f883e','yangtao@example.com','13312345678',1,1,'/avatars/yangtao.jpg','2025-11-21 13:45:30','2026-01-21 23:03:01',NULL,NULL,NULL),(8,'周红','周红','e10adc3949ba59abbe56e057f20f883e','zhouhong@example.com','13212345678',1,0,'/avatars/zhouhong.jpg','2025-11-21 13:45:30','2026-01-21 23:03:04',NULL,NULL,NULL),(9,'吴刚','吴刚','e10adc3949ba59abbe56e057f20f883e','wugang@example.com','13112345678',1,1,'/avatars/wugang.jpg','2025-11-21 13:45:30','2026-01-21 23:03:07',NULL,NULL,NULL),(10,'郑静','郑静','$2a$10$MdK3rBorUatICzoH8QYkJuJ/TdptIzet9Sd42p4bVigYNzu8k7lEa','zhengjing@example.com','13012345678',1,0,'/avatars/zhengjing.jpg','2025-11-21 13:45:30','2026-01-25 16:05:38',NULL,'127.0.0.1',NULL),(11,'小张','小张','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,1,1,'https://spring-tian.oss-cn-beijing.aliyuncs.com/头像/2026/01/18/3e00b48a67234486a72e23da56d0ef6e.jpg','2025-12-14 13:14:13','2026-01-21 23:03:13',NULL,'127.0.0.1',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-25 16:46:00
