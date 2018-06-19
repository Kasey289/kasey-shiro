package com.kasey.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
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



    @Test
    public void testHelloworld() {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:user.ini");
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("mark", "123456");

        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            System.out.println("logging status: Not logged in");
        }

        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录

        //6、退出
        subject.logout();
    }
}
