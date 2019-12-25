package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.user.UserAPI;
import org.springframework.stereotype.Component;

/**
 * @author chm
 * @date 2019/12/25 20:59
 */
@Component
@Service(interfaceClass = UserAPI.class)
public class UserAPIImpl implements UserAPI {
    @Override
    public boolean login(String username, String password) {
        return true;
    }
}
