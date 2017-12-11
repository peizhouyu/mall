package cn.mrpei.controller.backend;

import cn.mrpei.common.Const;
import cn.mrpei.common.ResponseCode;
import cn.mrpei.common.ServerResponse;
import cn.mrpei.controller.common.CommonMethod;
import cn.mrpei.pojo.Product;
import cn.mrpei.pojo.User;
import cn.mrpei.service.FileService;
import cn.mrpei.service.ProductService;
import cn.mrpei.service.UserService;
import cn.mrpei.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Date:2017/11/13
 * Time:15:17
 *
 * @author 裴周宇
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @RequestMapping("/save.do")
    @ResponseBody
    public ServerResponse productSave(Product product){
        return productService.saveOrUpdateProduct(product);
    }

    @RequestMapping("/set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(Product product){
        return productService.setSaleStatus(product.getId(),product.getStatus());
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    public ServerResponse getDetail(Integer productId){
        //增加产品业务逻辑
        return productService.manageProductDetail(productId);
    }


    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return productService.getProductList(pageNum,pageSize);
    }


    @RequestMapping("/search.do")
    @ResponseBody
    public ServerResponse productSearch(String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return productService.searchProduct(productName,productId,pageNum,pageSize);
    }

    @RequestMapping("/upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+ targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServerResponse.createBySuccess(fileMap);
    }

    @RequestMapping("/richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        //富文本中对于返回值有自己的要求，本项目前端使用 simditor 按照其官方文档要求进行返回
        Map resultMap = Maps.newHashMap();
        //增加业务逻辑
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file,path);
        if (StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+ targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;

    }



}
