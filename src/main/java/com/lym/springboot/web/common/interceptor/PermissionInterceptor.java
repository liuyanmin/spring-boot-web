package com.lym.springboot.web.common.interceptor;

import com.lym.springboot.web.common.annotation.RequiresPermissions;
import com.lym.springboot.web.common.api.ApiCode;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.CommonConstant;
import com.lym.springboot.web.core.dao.AdminDao;
import com.lym.springboot.web.core.dao.MenuDao;
import com.lym.springboot.web.core.dao.RoleDao;
import com.lym.springboot.web.core.domain.WebAdmin;
import com.lym.springboot.web.core.domain.WebMenuPermissionRe;
import com.lym.springboot.web.core.domain.WebRoleMenuRe;
import com.lym.springboot.web.util.HttpServletResponseUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 权限拦截器
 * Created by liuyanmin on 2019/10/20.
 */
@Log4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminDao adminDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        boolean classAnnotation = handlerMethod.getBeanType().isAnnotationPresent(RequiresPermissions.class);
        boolean methodAnnotation = method.isAnnotationPresent(RequiresPermissions.class);

        // 类和方法上都没有该注解，跳过权限验证
        if (!classAnnotation && !methodAnnotation) {
            return true;
        }

        // 校验是否具有权限
        boolean hasPermission = hasPermission(request);
        if (!hasPermission) {
            log.error("无操作权限");
            ApiResult apiResult = ApiResult.fail(ApiCode.NOT_PERMISSION);
            HttpServletResponseUtil.printJSON(response, apiResult);
            return false;
        }

        return true;
    }

    /**
     * 校验是否有操作权限
     * @return
     */
    private boolean hasPermission(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String adminId = request.getAttribute(CommonConstant.ADMIN_ID).toString();

        // 查询当前用户具有的角色
        WebAdmin admin = adminDao.getById(Integer.valueOf(adminId));
        Integer roleId = admin.getRoleId();

        // 超级管理员跳过角色验证
        if (admin.getSuperAdmin()) {
            return true;
        }

        // 查询当前uri所对应的菜单权限
        List<WebMenuPermissionRe> menuPermissionRes = menuDao.getMenuPermissionByApiUri(requestURI);
        if (menuPermissionRes == null || menuPermissionRes.size() == 0) {
            return false;
        }

        // 查询当前用户角色是否具有此权限
        for (WebMenuPermissionRe menuPermissionRe : menuPermissionRes) {
            Integer menuId = menuPermissionRe.getMenuId();
            Integer permissionId = menuPermissionRe.getPermissionId();
            WebRoleMenuRe roleMenuRe = roleDao.getOneRoleMenu(roleId, menuId, permissionId);
            if (roleMenuRe != null) {
                return true;
            }
        }

        return false;
    }
}
