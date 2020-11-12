package com.ktt.controller.portal;

import com.ktt.common.Const;
import com.ktt.common.ServerResponse;
import com.ktt.pojo.User;
import com.ktt.service.UserService;
import com.ktt.vo.UserInformation;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ServerResponse<UserInformation> login(@RequestBody User user, HttpSession session){
        return userService.login(user.getPhoneNum(),user.getPassWord());
    }

    @PostMapping("/register/{code}")
    public ServerResponse<User> register(@PathVariable String code,@RequestBody User user){
         return userService.register(code,user);
    }

    @PutMapping("/update_password")
    public ServerResponse<User> updatePassword(@RequestBody User user){
        return userService.updatePassword(user);
    }

    @PostMapping("/get_code/{phoneNum}")
    public ServerResponse<String> getMessageCode(@PathVariable String phoneNum){
        return userService.getMessageCode(phoneNum);
    }

    @GetMapping("/exit")
    public ServerResponse<User> exit(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出登录成功!");
    }

}
