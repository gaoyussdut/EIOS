package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.toptimus.common.fastdfs.FastDFSClient;
import top.toptimus.service.domainService.PlaceService;

import java.io.IOException;

/**
 * Created by JiangHao on 2018/11/16.
 */
@Api(value = "模板生成", tags = "模板管理")
@RestController
@RequestMapping(value = "/templateFile")
@Controller
public class TemplateGeneratorController {

    @Autowired
    private PlaceService placeService;

    /**
     * 将文件上传到文件服务器
     *
     * @param multipartFile multipartFile
     * @return 文件的URl
     * @throws IOException IOException
     */
    @ApiOperation(value = "上传文件")
    @RequestMapping(value = {"/uploadFile"}, method = RequestMethod.POST)
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return FastDFSClient.uploadFile(multipartFile);
    }

    /**
     * 根据数据和word模板生成word文档并输出
     *
     * @param metaId  metaId
     * @param tokenId 数据的tokenId
     * @return url 文档对应的URL
     */
    @ApiOperation(value = "根据数据和word模板生成word文档并输出")
    @RequestMapping(value = {"/generateWord"}, method = RequestMethod.GET)
    public String  generateWordDocByMetaToken(@RequestParam String metaId ,@RequestParam String tokenId) {
        return placeService.generateWordDocByMetaToken(metaId,tokenId);
    }

}
