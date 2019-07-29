package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.result.Result;
import top.toptimus.service.secutiry.UserService;


/**
 * Created by JiangHao on 2018/12/17.
 */
@Api(tags = "用户信息管理")
@RestController
@RequestMapping(value = "/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = {"/getUserInfo"}, method = RequestMethod.GET)
    public Result getUserInfo() {
        return userService.getUserInfoByAccessToken();
    }

    /**
     * 查询所有可用角色
     *
     * @return 角色
     */
    @ApiOperation(value = "获取所有可用角色信息")
    @RequestMapping(value = {"/getAllRoleInfo"}, method = RequestMethod.GET)
    public Result findAllRole() {
        return userService.findAllRole();
    }


    /**
     * 根据用户id 查询用户的基础信息
     *
     * @return 角色
     */
    @ApiOperation(value = "获取所有可用角色信息")
    @GetMapping(value = {"/information"})
    public Result getUserBaseInfoById(@RequestParam String userId) {
        return userService.getUserBaseInfoById(userId);
    }


}
