package cn.mrpei.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Date:2017/11/14
 * Time:16:01
 *
 * @author 裴周宇
 */
public interface FileService {

    String upload(MultipartFile file, String path);

}
