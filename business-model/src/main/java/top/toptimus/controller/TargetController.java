package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.result.Result;
import top.toptimus.filter.FilterDTO;
import top.toptimus.service.TargetService;
import top.toptimus.target.TargetDataDTO;
import top.toptimus.target.TargetTypeDTO;

import java.util.List;
import java.util.Map;

/**
 * 目标管理接口
 *
 * @author lzs
 */
@Api(value = "目标管理接口", tags = "目标管理接口")
@RestController
@RequestMapping(value = "/target")
@Controller
public class TargetController {

    @Autowired
    private TargetService targetSercive;

    /**
     * 目标一览界面
     *
     * @param pageSize
     * @param pageNo
     * @return result
     */
    @ApiOperation(value = "目标一览界面")
    @GetMapping(value = "/targetGeneralView")
    public Result getTargetGeneralView(
            @RequestParam Integer pageSize
            , @RequestParam Integer pageNo
            , @RequestBody List<FilterDTO> filterCondition
    ) {
        return targetSercive.getTargetGeneralView(pageSize, pageNo, filterCondition);
    }

    /**
     * 目标类型一览界面
     *
     * @param pageSize
     * @param pageNo
     * @return result
     */
    @ApiOperation(value = "目标类型一览界面")
    @GetMapping(value = "/targetTypeGeneralView")
    public Result getTargetTypeGeneralView(
            @RequestParam Integer pageSize
            , @RequestParam Integer pageNo
    ) {
        return targetSercive.getTargetTypeGeneralView(pageSize, pageNo);
    }

    /**
     * 目标类型预览
     *
     * @param targetTypeId
     * @return result
     */
    @ApiOperation(value = "目标类型预览")
    @GetMapping(value = "/getTargetTypeDetail")
    public Result getTargetTypeDetail(
            @RequestParam String targetTypeId
    ) {
        return targetSercive.getTargetTypeDetail(targetTypeId);
    }

    /**
     * 目标类型保存
     *
     * @param targetTypeDTO
     * @return result
     */
    @ApiOperation(value = "目标类型保存")
    @PostMapping(value = "/saveTargetType")
    public Result saveTargetType(
            @RequestBody TargetTypeDTO targetTypeDTO
    ) {
        return targetSercive.saveTargetType(targetTypeDTO);
    }

    /**
     * 目标预览
     *
     * @param targetDataId
     * @return result
     */
    @ApiOperation(value = "目标预览")
    @GetMapping(value = "/getTargetDetail")
    public Result getTargetDetail(
            @RequestParam String targetDataId
    ) {
        return targetSercive.getTargetDetail(targetDataId);
    }

    /**
     * 目标保存
     *
     * @param targetDataDTO
     * @return result
     */
    @ApiOperation(value = "目标保存")
    @PostMapping(value = "/saveTarget")
    public Result saveTarget(
            @RequestBody TargetDataDTO targetDataDTO
    ) {
        return targetSercive.saveTarget(targetDataDTO);
    }

    /**
     * 删除目标类型
     *
     * @param targetTypeId
     * @return result
     */
    @ApiOperation(value = "删除目标类型")
    @DeleteMapping(value = "/deleteTargetType")
    public Result deleteTargetType(
            @RequestParam String targetTypeId
    ) {
        return targetSercive.deleteTargetType(targetTypeId);
    }

    /**
     * 删除目标
     *
     * @param targetDataId
     * @return result
     */
    @ApiOperation(value = "删除目标")
    @DeleteMapping(value = "/deleteTarget")
    public Result deleteTarget(
            @RequestParam String targetDataId
    ) {
        return targetSercive.deleteTarget(targetDataId);
    }
}
