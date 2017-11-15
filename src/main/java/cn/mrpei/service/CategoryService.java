package cn.mrpei.service;

import cn.mrpei.common.ServerResponse;
import cn.mrpei.pojo.Category;

import java.util.List;

/**
 * Date:2017/11/13
 * Time:10:21
 *
 * @author 裴周宇
 */
public interface CategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
