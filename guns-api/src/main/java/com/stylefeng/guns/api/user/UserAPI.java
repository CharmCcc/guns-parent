package com.stylefeng.guns.api.user;

/**
 * @author chm
 * @date 2019/12/25 20:58
 */
public interface UserAPI {

    int login(String username, String password);

    boolean register(UserModel userModel);

    boolean checkUsername(String username);

    UserInfoModel getUserInfo(int uuid);

    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);

}
