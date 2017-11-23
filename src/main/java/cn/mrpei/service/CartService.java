package cn.mrpei.service;

import cn.mrpei.common.ServerResponse;
import cn.mrpei.vo.CartVo;

/**
 * Date:2017/11/17
 * Time:15:13
 *
 * @author 裴周宇
 */
public interface CartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);
    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo> selectOrUnSelectAll(Integer userId, Integer checked);
    ServerResponse<CartVo> selectOrUnSelectOne(Integer userId, Integer productId,Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);

}
