package cn.mrpei.service;

import cn.mrpei.common.ServerResponse;
import cn.mrpei.pojo.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * Date:2017/11/18
 * Time:17:06
 *
 * @author 裴周宇
 */
public interface ShippingService {

    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse<String> del(Integer userId, Integer shippingId);
    ServerResponse update(Integer userId, Shipping shipping);
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

}
