package cn.mrpei.controller.backend;

import cn.mrpei.common.Const;
import cn.mrpei.common.ResponseCode;
import cn.mrpei.common.ServerResponse;
import cn.mrpei.controller.common.CommonMethod;
import cn.mrpei.pojo.User;
import cn.mrpei.service.CategoryService;
import cn.mrpei.service.OrderService;
import cn.mrpei.service.UserService;
import cn.mrpei.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Date:2017/11/21
 * Time:18:52
 *
 * @author 裴周宇
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;


    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return orderService.manageList(pageNum,pageSize);
    }


    @RequestMapping("/detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(Long orderNo){
        return orderService.manageDetail(orderNo);
    }


    @RequestMapping("/search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(Long orderNo,
                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return orderService.manageSearch(orderNo,pageNum,pageSize);
    }


    @RequestMapping("/send_goods.do")
    @ResponseBody
    public ServerResponse<String> send_goods(Long orderNo){
        return orderService.manageSendGoods(orderNo);
    }



}
