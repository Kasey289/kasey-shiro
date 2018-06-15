package com.kasey.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JdbcRealmTest  {


    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://120.79.172.167:3306/shiro");
        dataSource.setUsername("root");
        dataSource.setPassword("kasey289");
    }

    @Test
    public void testJdbcRealm(){

        // 1. 创建JdbcRealm
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        /**
         * 自定义sql
         */
        // 验证登录
        String sql = "select password from users where username = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        // 验证角色
        String roleSql = "select role_name from user_roles where username = ?";
        jdbcRealm.setUserRolesQuery(roleSql);

        // 验证权限
        String permissionSql = "select permission from roles_permissions where role_name = ?";
        jdbcRealm.setPermissionsQuery(permissionSql);


        // 2. 构建SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

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
                subject.checkRole("user");

                try {
                    // 检查是否有该权限
                    subject.checkPermission("user:delete");
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
