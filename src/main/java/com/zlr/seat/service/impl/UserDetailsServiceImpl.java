package com.zlr.seat.service.impl;

import com.zlr.seat.service.IUserService;
import com.zlr.seat.utils.ValidateUtil;
import com.zlr.seat.vo.StudentUserDetails;
import com.zlr.seat.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service.impl
 * @Description
 * @create 2022-10-13-下午10:49
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean isMobile = ValidateUtil.validateMobile(username);
        UserVo userVo;
        if (isMobile) {
            userVo = userService.selectUserVoByUsernameOtherwiseMobile(null,username);
        } else {
            userVo = userService.selectUserVoByUsernameOtherwiseMobile(username,null);
        }
        if (userVo == null) {
            throw new UsernameNotFoundException("user not found:" + username);
        }
        UserDetails userDetails = new StudentUserDetails();
        BeanUtils.copyProperties(userVo,userDetails);
        return userDetails;
    }
}
