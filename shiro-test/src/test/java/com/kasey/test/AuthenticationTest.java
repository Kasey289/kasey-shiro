package com.kasey.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * Shiro  SimpleAccountRealm的认证和授权
 * @author 谢开欣
 */
public class AuthenticationTest {


    SimpleAccountRealm accountRealm = new SimpleAccountRealm();

    @Before
    public void addUser(){
        accountRealm.addAccount("mark","123456","admin");
    }

    @Test
    public void testAuthentication(){

        // 1. 构建SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(accountRealm);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();


        UsernamePasswordToken token = new UsernamePasswordToken("mark","123456");

        // 3. 登录
        try {
            subject.login(token);
        }catch (UnknownAccountException e){
            System.out.println("message: Incorrect username");
        }catch (IncorrectCredentialsException e){
            System.out.println("message: The password is wrong");
        }

        // 3. 认证
        if(subject.isAuthenticated()){
            System.out.println("logging status: login successfully");
            // 4. 授权
            try{
                // 检查是否有该角色
                subject.checkRole("user");
            }catch (UnauthorizedException e){
                // 没有此角色
                System.out.println("checkRole: No such role");
            }
            // 5. 退出
            subject.logout();
            System.out.println("message: Exit the success");
        }else{
            System.out.println("logging status: Not logged in");
        }

    }
}
