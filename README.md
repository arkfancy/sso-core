# sso-core 
单点登录核心包

## 使用说明

1. 引入sso-core
```xml
	<dependency>
	  <groupId>com.arkfancy.sso</groupId>
	  <artifactId>sso-core</artifactId>
	  <version>${version}</version>
	</dependency>	
```

2. 添加启动注解
```java
@SpringBootApplication
@EnableSSO
public class YourApplication {

	public static void main(String[] args) {
		SpringApplication.run(YourApplicationApplication.class, args);
	}

}
```

3. 在需要验证的方法上加入拦截注解，在控制器类上加入时整个类拦截
```java
@RestController
// @Login
public class TestController {

	@GetMapping("/admin/need-login")
	@Login
	public String needLogin() {
		return "success";
	}
```

## 默认参数
```
kisso.config.signkey=
kisso.config.cookie-name=arkfancy-sso
kisso.config.login-url=http://www.arkfancy.com/sso/#/login
kisso.config.cookie-maxage=864000
kisso.config.paramReturl=returnUrl

sso.interceptor.path-pattern=/**
```

## 参考链接

kisso网址： [https://gitee.com/baomidou/kisso](https://gitee.com/baomidou/kisso)

## 更新日志

### [v0.0.1] 2019-09-26 
- 项目初始化。
