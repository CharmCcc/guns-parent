package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.UserInfoModel;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chm
 * @date 2019/12/25 20:59
 */
@Component
@Service(interfaceClass = UserAPI.class)
public class UserServiceImpl implements UserAPI {

    @Autowired
    private MoocUserTMapper moocUserTMapper;

    @Override
    public boolean register(UserModel userModel) {

        // 获取注册信息实体转换为数据实体[mooc_user_t]
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userModel.getUsername());
        moocUserT.setEmail(userModel.getEmail());
        moocUserT.setAddress(userModel.getAddress());
        moocUserT.setUserPhone(userModel.getPhone());

        // 创建时间和修改时间 -> current_timestamp

        // 数据加密 【MD5混淆加密 + 盐值  -> Shiro 加密】
        String md5Paswword = MD5Util.encrypt(userModel.getPassword());
        moocUserT.setUserPwd(md5Paswword); // 注意

        // 将数据实体存入数据库
        Integer insert = moocUserTMapper.insert(moocUserT);
        if(insert>0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int login(String username, String password) {
        // 根据登录账号获取数据库信息
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(username);

        MoocUserT result = moocUserTMapper.selectOne(moocUserT);

        // 获取到的结果，然后与加密以后的密码做匹配
        if(null!=result && result.getUuid()>0){
            String md5Password = MD5Util.encrypt(password);
            if(result.getUserPwd().equals(md5Password)){
                return result.getUuid();
            }
        }
        return 0;
    }


    @Override
    public boolean checkUsername(String username) {
        EntityWrapper<MoocUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",username);
        Integer result = moocUserTMapper.selectCount(entityWrapper);
        if(null!=result && result>0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public UserInfoModel getUserInfo(int uuid) {
        return null;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        return null;
    }
}
