package cn.mrpei.controller.portal;

import cn.mrpei.common.Const;
import cn.mrpei.common.ResponseCode;
import cn.mrpei.common.ServerResponse;
import cn.mrpei.controller.common.CommonMethod;
import cn.mrpei.pojo.User;
import cn.mrpei.service.CartService;
import cn.mrpei.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Date:2017/11/17
 * Time:15:10
 *
 * @author 裴周宇
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest httpServletRequest){
        User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要转到登录页！");
        }

        return cartService.list(user.getId());
    }

    @RequestMapping("/add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpServletRequest httpServletRequest, Integer count, Integer productId){
        User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要转到登录页！");
        }

        return cartService.add(user.getId(),productId,count);
    }


    @RequestMapping("/update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpServletRequest httpServletRequest, Integer count, Integer productId){
        User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要转到登录页！");
        }
        return cartService.update(user.getId(),productId,count);
    }


    @RequestMapping("/delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest httpServletRequest, String  productIds){
        User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要转到登录页！");
        }
        return cartService.deleteProduct(user.getId(),productIds);
    }

    @RequestMapping("/select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest httpServletRequest){
        User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要转到登录页！");
        }
        return cartService.selectOrUnSelectAll(user.getId(),Const.Cart.CHECKED);
    }

    @RequestMapping("/un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest){
        User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要转到登录页！");
        }
        return cartService.selectOrUnSelectAll(user.getId(),Const.Cart.UN_CHECKED);
    }

    @RequestMapping("/select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest httpServletRequest, Integer productId){
        User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要转到登录页！");
        }
        return cartService.selectOrUnSelectOne(user.getId(),productId,Const.Cart.CHECKED);
    }

    @RequestMapping("/un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest httpServletRequest, Integer productId){
        User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要转到登录页！");
        }
        return cartService.selectOrUnSelectOne(user.getId(),productId,Const.Cart.UN_CHECKED);
    }

    @RequestMapping("/get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest){
        User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user == null){
            return ServerResponse.createBySuccess(0);
        }
        return cartService.getCartProductCount(user.getId());
    }

    //全选
    //全反选

    //单独选
    //单独反选

    //获取当前用户的购物车里面的产品数量，如果一个产品有10个，那么数量就是10

}
