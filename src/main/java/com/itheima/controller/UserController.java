package com.itheima.controller;

import com.github.pagehelper.PageInfo;
import com.itheima.config.PropConfig;
import com.itheima.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itheima.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${testconfig.msg}")
    private String msg;
    @Autowired
    private PropConfig propConfig;
     /*
     * @name:   hello
     * @description: 测试hello方法
     * @return:  String
     * @date: 2020-04-08 09:05
     * @auther: pjy
     *
    */
    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        System.out.println("UserController.hello()");
        return "ok";
    }

  /*
   * @name:  testpage
   * @description:  user用户分页查询
   * @return: PageInfo
   * @date: 2020-04-08 09:06
   * @auther: pjy
   *
  */
    @GetMapping("/testpage")
    @ResponseBody
    public PageInfo testpage() {
        System.out.println("testpage");
        PageInfo<User> userPage = userService.queryPage(2, 5);
        return userPage;
    }

    @RequestMapping("/add")
    public String add() {
        return "/user/add";
    }

    @RequestMapping("/update")
    public String update() {
        return "/user/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/noAuth")
    public String noAuth() {
        return "/noAuth";
    }

    /**
     * 测试thymeleaf
     */
    @RequestMapping("/testThymeleaf")
    @RequiresPermissions("login12")//权限管理;
    public String testThymeleaf(Model model) {
        //把数据存入model
        model.addAttribute("name", "吴先生");
        //返回test.html
        return "test";
    }

    /**
     * 测试thymeleaf
     */
    @RequestMapping("/testTrans")
    @ResponseBody
    public String testTrans() {
        userService.findByName("pjy");
        return "ok";
    }

    /**
     * 登录逻辑处理
     */
    @RequestMapping("/login")
    public String login(String name, String password, Model model) {
        logger.info("当前登录用户名name:{},password:{}", name, password);
        /**
         * 使用Shiro编写认证操作
         */
        //1.获取Subject
        Subject subject = SecurityUtils.getSubject();

        //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);

        //3.执行登录方法
        try {
            subject.login(token);

            //登录成功
            //跳转到test.html
            return "redirect:/testThymeleaf";
        } catch (UnknownAccountException e) {
            //e.printStackTrace();
            //登录失败:用户名不存在，UnknownAccountException是Shiro抛出的找不到用户异常
            model.addAttribute("msg", "用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            //e.printStackTrace();
            //登录失败:密码错误，IncorrectCredentialsException是Shiro抛出的密码错误异常
            model.addAttribute("msg", "密码错误");
            return "login";
        }
    }
  /*
   * @name:  testConfigPro
   * @description: 测试获取配置文件属性值
   * @return:
   * @date: 2020-04-08 13:31
   * @auther: pjy
   *
  */
    @RequestMapping("/testConfigPro")
    public String testConfigPro(){
        logger.info("application.pro.msg：{}",msg);
        logger.info("config.pro.name：{}",propConfig.getName());
        logger.info("config.pro.password：{}",propConfig.getPassword());
        logger.info("====1");
        return "login";
    }
}
