package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.toptimus.meta.TalendMetaInfo;
import top.toptimus.service.DataModelRuntimeService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(value = "", tags = "data Model 关联关系")
@RestController
@RequestMapping(value = "/dataModelRuntime")
@Controller
public class DataModelRuntimeController {
    private DataModelRuntimeService dataModelRuntimeService;

    /**
     * 取schema中所有关联定义
     *
     * @param x_pk_x_talend_id        talend model id
     * @param referenceEntityTypeName 联表名
     * @return 关联定义
     */
    @ApiOperation(value = "取schema中所有关联定义", notes = "")
    @RequestMapping(value = {"/getTalendReference"}, method = RequestMethod.GET)
    public List<TalendMetaInfo> getTalendReference(@PathVariable String x_pk_x_talend_id, @PathVariable String referenceEntityTypeName) throws IOException {
        return dataModelRuntimeService.getTalendReference(x_pk_x_talend_id, referenceEntityTypeName);
    }
}
