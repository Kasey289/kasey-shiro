package com.kasey.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 *  IniRealm 的认证和授权代码示例
 *  @author 谢开欣
 */
public class IniRealmTest {

    @Test
    public void testIniRealm(){

        // 1. 加载ini文件
        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        // 2. 构建SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        // 3. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();


        UsernamePasswordToken token = new UsernamePasswordToken("mark","123456");

        // 4. 登录
        try {
            subject.login(token);
        }catch (UnknownAccountException e){
            System.out.println("message: Incorrect username");
        }catch (IncorrectCredentialsException e){
            System.out.println("message: The password is wrong");
        }

        // 5. 认证
        if(subject.isAuthenticated()){
            System.out.println("logging status: login successfully");
            // 6. 授权
            try{
                // 检查是否有该角色
                subject.checkRole("admin");

                try {
                    // 检查是否有该权限
                    subject.checkPermission("user:update");
                }catch (UnauthorizedException e){
                    // 没有此权限
                    System.out.println("permission: do not have permission");
                }
            }catch (UnauthorizedException e){
                // 没有此角色
                System.out.println("checkRole: No such role");
            }

            // 7. 退出
            subject.logout();
            System.out.println("message: Exit the success");
        }else{
            System.out.println("logging status: Not logged in");
        }

    }
}
