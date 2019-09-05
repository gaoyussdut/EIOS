package top.toptimus.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织架构接口
 *
 * @author lzs
 */
@Api(value = "组织架构接口", tags = "组织架构管理")
@RestController
@RequestMapping(value = "/org")
@Controller
public class OrgnazitionUnitController {
}
