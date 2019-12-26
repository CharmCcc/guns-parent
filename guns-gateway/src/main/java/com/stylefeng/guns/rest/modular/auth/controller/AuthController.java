package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthResponse;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.auth.validator.IReqValidator;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Resource(name = "simpleValidator")
//    private IReqValidator reqValidator;

    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI;

    @RequestMapping(value = "${jwt.auth-path}")
    public ResponseVO createAuthenticationToken(AuthRequest authRequest) {

        userAPI.login(authRequest.getUserName(),authRequest.getPassword());

        boolean validate = true; //reqValidator.validate(authRequest);
        // 去掉guns自身携带的用户名密码验证机制，使用我们自己的
        int userId = userAPI.login(authRequest.getUserName(),authRequest.getPassword());

        if(0==userId){
            validate = false;
        }

        if (validate) {
            // randomKey 和 token 已经生成完毕
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(""+userId, randomKey);
            // 返回值
            return ResponseVO.success(new AuthResponse(token,randomKey));
//            return ResponseEntity.ok(new AuthResponse(token, randomKey));
        } else {
            return ResponseVO.serviceFail("用户名或密码错误");
        }
    }
}
