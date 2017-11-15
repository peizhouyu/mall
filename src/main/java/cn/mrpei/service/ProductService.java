package cn.mrpei.service;

import cn.mrpei.common.ServerResponse;
import cn.mrpei.pojo.Product;
import cn.mrpei.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * Date:2017/11/13
 * Time:15:24
 *
 * @author 裴周宇
 */
public interface ProductService {

    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    ServerResponse getProductList(int pageNum, int pageSize);
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    //portal
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

}
