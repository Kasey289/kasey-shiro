package com.kasey.test;


import com.kasey.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 自定义Realm 认证
 * @author 谢开欣
 */
public class CustomRealmTest {
    @Test
    public void testCustomRealm(){

        // 1. 创建自定义Realm
        CustomRealm customRealm = new CustomRealm();

        // 2. 构建SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

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
            // 6. 退出
            subject.logout();
            System.out.println("message: Exit the success");
        }else{
            System.out.println("logging status: Not logged in");
        }

    }
}
