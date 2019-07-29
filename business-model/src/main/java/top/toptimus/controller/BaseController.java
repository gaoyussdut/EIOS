package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.result.Result;
import top.toptimus.service.domainService.MetaService;
import top.toptimus.service.domainService.TokenService;
import top.toptimus.tokendata.TokenDataDto;

/**
 * 基础接口
 */
@Api(value = "基础接口", tags = "基础管理")
@RestController
@RequestMapping(value = "/base")
@Controller
public class BaseController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MetaService metaService;


    /**
     * 获取指定metaId和tokenId数据
     *
     * @param tokenId token id
     * @param metaId  meta id
     * @return Result (tokenDataDto数据)
     */
    @ApiOperation(value = "取得Meta详细数据")
    @RequestMapping(value = {"/metaId/tokenId"}, method = RequestMethod.GET, produces = "application/json")
    public Result getDataByTokenIdAndMetaId(@RequestParam String metaId, @RequestParam String tokenId) {
        return tokenService.getTokenData(metaId, tokenId);
    }


    /**
     * 根据metaId取得一览信息
     *
     * @param metaId           metaId
     * @param pageSize        单页数量
     * @param pageNo          页码
     * @param access_token    用户令牌 swagger专用
     * @return TokenDataPageableDto
     */
    @ApiOperation(value = "取得Meta一览数据")
    @RequestMapping(value = {"/metaId"}, method = RequestMethod.GET, produces = "application/json")
    public Result getTokenDataList(@RequestParam String metaId, @RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam String access_token) {
        return tokenService.getTokenDataList(metaId, pageSize, pageNo);
    }

    /**
     * 保存指定metaData
     *
     * @param tokenDataDto token数据
     * @param metaId       meta id
     * @return result
     */
    @ApiOperation(value = "保存Meta详细数据")
    @PostMapping(value = "/metaId")
    public Result saveDataByTokenIdAndMetaId(@RequestBody TokenDataDto tokenDataDto, @RequestParam String metaId) {

        return tokenService.saveDatas(tokenDataDto, metaId);
    }

    /**
     * 删除指定metaId的tokenId数据
     *
     * @param tokenId token id
     * @param metaId  meta id
     * @return Result
     */
    @ApiOperation(value = "删除Meta数据")
    @RequestMapping(value = {"/metaId/tokenId"}, method = RequestMethod.DELETE, produces = "application/json")
    public Result delTokenData(@RequestParam String metaId, @RequestParam String tokenId) {
        return tokenService.delTokenData(metaId, tokenId);
    }

    /**
     * 根据fkey查询常量
     *
     * @param fkey fkey
     * @return
     */
    @ApiOperation(value = "根据fkey查询常量")
    @RequestMapping(value = {"/constant/{fkey}"}, method = RequestMethod.GET, produces = "application/json")
    public Result getConstant(@PathVariable String fkey) {
        return metaService.getConstantByKey(fkey);
    }
}
