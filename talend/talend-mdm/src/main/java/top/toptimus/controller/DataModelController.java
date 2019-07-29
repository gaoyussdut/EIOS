package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.service.DataModelService;
import top.toptimus.tokendata.TokenDataDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by JiangHao on 2018/8/2.
 */
@Api(value = "", tags = "dataModel管理")
@RestController
@RequestMapping(value = "/dataModel")
@Controller
public class DataModelController {
    @Autowired
    private DataModelService dataModelService;

    /**
     * 从talend model中生成数据视图meta
     *
     * @param x_pk_x_talend_id talend model id
     * @return K:表名 token meta id，V:数据视图meta
     */
    @ApiOperation(value = "数据视图meta", notes = "")
    @RequestMapping(value = {"/getTokenMetaInfoDTOS"}, method = RequestMethod.GET)
    public Map<String, TokenMetaInfoDTO> getTokenMetaInfoDTOS(@PathVariable String x_pk_x_talend_id) throws IOException {
        return dataModelService.getTokenMetaInfoDTOS(x_pk_x_talend_id);
    }

    /**
     * 分页查询data_model_pOJO，返回：K:talend主键，V:data model名
     *
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return K:talend主键，V:data model名
     */
    @ApiOperation(value = "dataModel一览", notes = "")
    @RequestMapping(value = {"/getTalendDataModelMeta"}, method = RequestMethod.GET)
    public Map<String, String> getTalendDataModelMeta(@PathVariable int pageNo, @PathVariable int pageSize) {
        return dataModelService.getTalendDataModelMeta(pageNo, pageSize);
    }

    /**
     * 根据表名返回数据
     *
     * @param tokenMetaInfoDTO meta信息
     * @param pageNo           页码
     * @param pageSize         长度
     * @return token数据列表
     */
    @ApiOperation(value = "根据表名返回数据", notes = "")
    @RequestMapping(value = {"/getTalendData"}, method = RequestMethod.POST)
    public List<TokenDataDto> getTalendData(
            @RequestBody TokenMetaInfoDTO tokenMetaInfoDTO
            , @PathVariable int pageNo
            , @PathVariable int pageSize
    ) {
        return dataModelService.getTalendData(tokenMetaInfoDTO, pageNo, pageSize);
    }
}
