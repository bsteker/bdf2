package com.bstek.bdf2.core.security;

import org.springframework.security.web.context.HttpRequestResponseHolder;

/**
 * 一个供开发人员使用的在登录、认证之前或之后或失败后需要进行业务处理的接口，<br>
 * 开发人员可以根据需要，有选择的覆盖该类中的某个方法，比如需要在用户登录前进行一些处理，那么就可覆盖其中的beforeLogin方法，<br>
 * 依次类推，使用时，将实现类配置到spring当中即可，系统运行时会自动扫描该抽象类实现的存在，如果有就会加载处理
 * @author jacky.gao
 * @since 2013-1-22
 */
public interface ISecurityInterceptor {
    /**
     * 用户登录系统之前进行的处理动作
     * @param holder 一个用于包装HttpRequest/HttpResponse的对象
     */
    void beforeLogin(HttpRequestResponseHolder holder);
    
    /**
     * 用户登录系统成功之后进行的处理动作
     * @param holder 一个用于包装HttpRequest/HttpResponse的对象
     */
    void loginSuccess(HttpRequestResponseHolder holder);
    
    /**
     * 用户登录系统认证失败时需要处理的动作
     * @param holder 一个用于包装HttpRequest/HttpResponse的对象
     */
    void loginFailure(HttpRequestResponseHolder holder);
    
    /**
     * 用户在访问系统资源时(比如访问某URL)，系统安全模块对用户进行授权之前需要处理的动作
     * @param holder 一个用于包装HttpRequest/HttpResponse的对象
     */
    void beforeAuthorization(HttpRequestResponseHolder holder);
    /**
     * 用户在访问系统资源时(比如访问某URL)，系统安全模块对用户进行授权成功之后需要处理的动作
     * @param holder 一个用于包装HttpRequest/HttpResponse的对象
     */
    void authorizationSuccess(HttpRequestResponseHolder holder);
    /**
     * 用户在访问系统资源时(比如访问某URL或某模块)，系统安全模块对用户进行授权失败之后需要处理的动作
     * @param holder 一个用于包装HttpRequest/HttpResponse的对象
     */
    void authorizationFailure(HttpRequestResponseHolder holder);
}
