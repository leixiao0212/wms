package com.tdt.sys.modular.system.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import com.tdt.base.shiro.ShiroUser;
import com.tdt.sys.core.constant.DefaultAvatar;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.core.properties.TDTProperties;
import com.tdt.sys.core.shiro.ShiroKit;
import com.tdt.sys.modular.system.entity.FileInfo;
import com.tdt.sys.modular.system.entity.User;
import com.tdt.sys.modular.system.mapper.FileInfoMapper;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.sys.core.constant.DefaultAvatar;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.core.properties.TDTProperties;
import com.tdt.sys.core.shiro.ShiroKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

/**
 * <p>
 * 文件信息表
 * 服务实现类
 * </p>
 *
 * @author thedreamtree
 * @since 2018-12-07
 */
@Service
@Slf4j
public class FileInfoService extends ServiceImpl<FileInfoMapper, FileInfo> {

    @Autowired
    private UserService userService;

    @Autowired
    private TDTProperties tdtProperties;

    /**
     * 更新头像
     *
     * @author thedreamtree
     * @Date 2018/11/10 4:10 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String fileId) {
        ShiroUser currentUser = ShiroKit.getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }

        User user = userService.getById(currentUser.getId());

        //更新用户的头像
        user.setAvatar(fileId);
        userService.updateById(user);
    }

    /**
     * 预览当前用户头像
     *
     * @author thedreamtree
     * @Date 2019-05-04 17:04
     */
    public byte[] previewAvatar() {

        ShiroUser currentUser = ShiroKit.getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }

        //获取当前用户的头像id
        User user = userService.getById(currentUser.getId());
        String avatar = user.getAvatar();

        //如果头像id为空就返回默认的
        if (ToolUtil.isEmpty(avatar)) {
            return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
        } else {

            //文件id不为空就查询文件记录
            FileInfo fileInfo = this.getById(avatar);
            if (fileInfo == null) {
                return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
            } else {
                try {
                    String filePath = fileInfo.getFilePath();
                    return IoUtil.readBytes(new FileInputStream(filePath));
                } catch (FileNotFoundException e) {
                    log.error("头像未找到！", e);
                    return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
                }
            }
        }

    }

    /**
     * 上传文件
     *
     * @author thedreamtree
     * @Date 2019-05-04 17:18
     */
    public String uploadFile(MultipartFile file) {

        //生成文件的唯一id
        String fileId = IdWorker.getIdStr();

        //获取文件后缀
        String fileSuffix = ToolUtil.getFileSuffix(file.getOriginalFilename());

        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();

        //生成文件的最终名称
        String finalName = fileId + "." + ToolUtil.getFileSuffix(originalFilename);

        try {
            //保存文件到指定目录
            String fileSavePath = tdtProperties.getFileUploadPath();
            File newFile = new File(fileSavePath + finalName);
            file.transferTo(newFile);

            //保存文件信息
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(fileId);
            fileInfo.setFileName(originalFilename);
            fileInfo.setFileSuffix(fileSuffix);
            fileInfo.setFilePath(fileSavePath + finalName);
            fileInfo.setFinalName(finalName);

            //计算文件大小kb
            long kb = new BigDecimal(file.getSize())
                    .divide(BigDecimal.valueOf(1024))
                    .setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            fileInfo.setFileSizeKb(kb);
            this.save(fileInfo);
        } catch (Exception e) {
            log.error("上传文件错误！", e);
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }

        return fileId;

    }
}
