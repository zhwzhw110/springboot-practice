package com.souche.db.controller;

import com.souche.db.annotation.MethodCache;
import com.souche.db.annotation.Validate;
import com.souche.db.model.User;
import com.souche.db.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo() {
        User user = userService.getUserInfo();
        if (user != null) {
            log.info("userName" + user.getName());
        }
        return user;
    }

    @RequestMapping("/test1")
    @ResponseBody

    @Validate("zxc")
    public User getUserInfo1(@RequestParam("name") String name) {
        log.info("userName",name);
        return userService.getUserInfo();
    }

    @RequestMapping("/test2")

    @ResponseBody
    public User getUserInfo2() {

        User u = new User();
        u.setName("andy");
        userService.createUser(u);
        return new User();
    }

    @RequestMapping("/test3")
    @ResponseBody
    @MethodCache(expire = 10)
    public String getUserInfo3(@RequestParam("name") String name,@RequestParam("year") String year) {
        log.info("name [{}]",name);
        return userService.getUserInfo().getName();
    }
}
