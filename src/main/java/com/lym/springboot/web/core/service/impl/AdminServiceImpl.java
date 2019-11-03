package com.lym.springboot.web.core.service.impl;

import com.lym.springboot.web.common.api.ApiCode;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.CommonConstant;
import com.lym.springboot.web.common.constant.CommonRedisKey;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.config.redis.RedisTemplate;
import com.lym.springboot.web.core.dao.AdminDao;
import com.lym.springboot.web.core.dao.MenuDao;
import com.lym.springboot.web.core.dao.PermissionDao;
import com.lym.springboot.web.core.dao.RoleDao;
import com.lym.springboot.web.core.domain.*;
import com.lym.springboot.web.core.dto.request.*;
import com.lym.springboot.web.core.dto.response.*;
import com.lym.springboot.web.core.service.IAdminService;
import com.lym.springboot.web.core.vo.MenuPermissionResponseVo;
import com.lym.springboot.web.config.properties.LoginProperties;
import com.lym.springboot.web.core.service.ILoginService;
import com.lym.springboot.web.util.DigestUtil;
import com.lym.springboot.web.util.MailUtil;
import com.lym.springboot.web.util.Md5Util;
import com.lym.springboot.web.util.PasswordUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by liuyanmin on 2019/10/19.
 */
@Log4j
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AdminDao adminDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${spring-boot-web.gateway}")
    private String gateway;
    @Autowired
    private ILoginService loginService;
    @Autowired
    private LoginProperties loginProperties;

    @Override
    public ApiResult<UserMenuListResponse> userMenuList(Integer adminId) {
        WebAdmin admin = adminDao.getById(adminId);

        Map<Integer, List<WebRoleMenuRe>> hasMenuPermissionMap = new HashMap<>();
        Map<Integer, List<WebMenuPermissionRe>> menuPermissionMap = new HashMap<>();
        List<Integer> hasMenuIds = new ArrayList<>();
        List<WebMenu> menus;
        List<WebMenuPermissionRe> menuPermissionRes;

        // 判断是否是超级管理员
        if (admin.getSuperAdmin()) {
            menus = menuDao.getAll();
            menuPermissionRes = menuDao.getAllMenuPermission();
        } else {
            // 查询当前角色拥有的所有菜单权限
            List<WebRoleMenuRe> roleMenuRes = roleDao.getRolePermission(admin.getRoleId());
            if (roleMenuRes != null && roleMenuRes.size() > 0) {
                // 每个菜单具有的权限
                hasMenuPermissionMap = roleMenuRes.stream().collect(Collectors.groupingBy(WebRoleMenuRe::getMenuId));
                // 所有菜单ID
                hasMenuIds = roleMenuRes.stream().map(WebRoleMenuRe::getId).collect(Collectors.toList());
            }

            if (hasMenuIds == null || hasMenuIds.size() == 0) {
                return ApiResult.ok();
            }
            menus = menuDao.getMenuByIdList(hasMenuIds);

            // 查询菜单权限
            menuPermissionRes = menuDao.getMenuPermissionByMenuId(hasMenuIds);
        }

        if (menuPermissionRes != null && menuPermissionRes.size() > 0) {
            menuPermissionMap = menuPermissionRes.stream().collect(Collectors.groupingBy(WebMenuPermissionRe::getMenuId));
        }

        // 查询权限名称
        Map<Integer, String> permissionNameMap = permissionDao.getAllPermissionNameMap();

        List<UserMenuListResponse> userMenus = new ArrayList<>();
        if (menus != null && menus.size() > 0) {
            Map<Integer, List<WebMenu>> menuMap = menus.stream().collect(Collectors.groupingBy(WebMenu::getParentId));
            // 一级菜单
            List<WebMenu> rootMenus = menuMap.get(0);
            if (rootMenus != null && rootMenus.size() > 0) {
                rootMenus = rootMenus.stream().sorted(Comparator.comparing(WebMenu::getSortBy)).collect(Collectors.toList());
                for (WebMenu rootMenu : rootMenus) {
                    List<WebMenuPermissionRe> permissionReList = menuPermissionMap.get(rootMenu.getId());
                    Map<Integer, WebMenuPermissionRe> permissionReMap = new HashMap<>();
                    if (permissionReList != null && permissionReList.size() > 0) {
                        permissionReList.forEach(e -> permissionReMap.put(e.getPermissionId(), e));
                    }

                    UserMenuListResponse currentMenu = new UserMenuListResponse()
                            .setMenuId(rootMenu.getId())
                            .setParentId(0)
                            .setMenuName(rootMenu.getMenuName());

                    // 递归所有子菜单
                    userMenus.add(treeMenus(currentMenu, menuMap, hasMenuPermissionMap, permissionNameMap, permissionReMap));
                }
            }
        }

        return ApiResult.ok(userMenus);
    }

    /**
     * 遍历菜单树
     * @param currentMenu 当前菜单
     * @param menuMap 拥有的所有菜单，key-父菜单ID value-子菜单列表
     * @param hasMenuPermissionMap 拥有的所有权限，key-菜单ID value-权限列表
     * @param permissionNameMap 权限名称，key-权限ID value-权限名称
     * @param permissionReMap 权限map，key-权限ID value-菜单权限对象
     * @return
     */
    private UserMenuListResponse treeMenus(UserMenuListResponse currentMenu, Map<Integer, List<WebMenu>> menuMap,
                                          Map<Integer, List<WebRoleMenuRe>> hasMenuPermissionMap,
                                          Map<Integer, String> permissionNameMap,
                                          Map<Integer, WebMenuPermissionRe> permissionReMap) {

        List<WebMenu> subMenus = menuMap.get(currentMenu.getMenuId());
        // 最后一级菜单，查找是否有权限
        if (subMenus == null || subMenus.size() == 0) {
            // 当前菜单下具有的权限
            List<WebRoleMenuRe> roleMenuRes = hasMenuPermissionMap.get(currentMenu.getMenuId());
            if (roleMenuRes != null) {
                List<MenuPermissionResponseVo> permissions = new ArrayList<>();
                for (WebRoleMenuRe roleMenuRe : roleMenuRes) {
                    String apiUri = "", frontUrl = "";
                    WebMenuPermissionRe menuPermissionRe = permissionReMap.get(roleMenuRe.getPermissionId());
                    if (menuPermissionRe != null) {
                        apiUri = menuPermissionRe.getApiUri();
                        frontUrl = menuPermissionRe.getFrontUrl();
                    }

                    MenuPermissionResponseVo vo = new MenuPermissionResponseVo()
                            .setPermissionId(roleMenuRe.getPermissionId())
                            .setPermissionName(permissionNameMap.get(roleMenuRe.getPermissionId()))
                            .setApiUri(apiUri)
                            .setFrontUrl(frontUrl);

                    permissions.add(vo);
                }
                currentMenu.setPermissions(permissions);
            }
            return currentMenu;
        }

        // 不是最后一级菜单，继续找子菜单
        subMenus = subMenus.stream().sorted(Comparator.comparing(WebMenu::getSortBy)).collect(Collectors.toList());
        // 当前菜单的子菜单
        List<UserMenuListResponse> childrens = currentMenu.getChildrens();
        for (WebMenu subMenu : subMenus) {
            UserMenuListResponse vo = new UserMenuListResponse();
            vo.setMenuId(subMenu.getId());
            vo.setMenuName(subMenu.getMenuName());
            vo.setParentId(subMenu.getParentId());

            if (childrens == null) {
                childrens = new ArrayList<>();
            }
            childrens.add(vo);
            currentMenu.setChildrens(childrens);
            treeMenus(vo, menuMap, hasMenuPermissionMap, permissionNameMap, permissionReMap);
        }

        return currentMenu;
    }

    @Override
    public ApiResult<PageBean<AdminListResponse>> list(AdminListParam adminListParam) {
        PageBean<AdminListResponse> pageBean = adminDao.list(adminListParam);
        return ApiResult.ok(pageBean);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult save(AdminSaveParam adminSaveParam) {

        // 校验当前用户名是否存在
        WebAdmin admin = adminDao.getByUsername(adminSaveParam.getUsername());
        if (admin != null) {
            return ApiResult.fail(ApiCode.USERNAME_EXISTS);
        }

        admin = new WebAdmin();
        admin.setUsername(adminSaveParam.getUsername());
        admin.setPassword(Md5Util.md5(loginProperties.getInitPassword()));
        admin.setFullname(adminSaveParam.getFullname());
        admin.setEmail(adminSaveParam.getEmail());
        admin.setGender(adminSaveParam.getGender());
        admin.setAge(adminSaveParam.getAge());
        admin.setMobile(adminSaveParam.getMobile());
        admin.setAvatar(adminSaveParam.getAvatar());
        admin.setStatus(adminSaveParam.getStatus());
        admin.setRoleId(adminSaveParam.getRoleId());
        admin.setSuperAdmin(adminSaveParam.getSuperAdmin());
        adminDao.insert(admin);

        return ApiResult.ok();
    }

    @Override
    public ApiResult<AdminNewResponse> newPage() {
        List<WebRole> allRoles = roleDao.getAllRole();
        List<AdminNewResponse> roles = new ArrayList<>();
        if (allRoles != null && allRoles.size() > 0) {
            for (WebRole role : allRoles) {
                AdminNewResponse response = new AdminNewResponse()
                        .setRoleId(role.getId())
                        .setRoleName(role.getRoleName())
                        .setSelected(false);
                roles.add(response);
            }
        }
        return ApiResult.ok(roles);
    }

    @Override
    public ApiResult<AdminDetailResponse> detail(Integer adminId) {

        // 查询管理员信息
        WebAdmin admin = adminDao.getById(adminId);
        // 查询所有角色
        List<WebRole> roles = roleDao.getAllRole();
        List<AdminNewResponse> roleList = new ArrayList<>();
        if (roles != null) {
            for (WebRole role : roles) {
                boolean selected = Integer.compare(role.getId(), admin.getRoleId()) == 0;
                AdminNewResponse response = new AdminNewResponse()
                        .setRoleId(role.getId())
                        .setRoleName(role.getRoleName())
                        .setSelected(selected);
                roleList.add(response);
            }
        }

        AdminDetailResponse response = new AdminDetailResponse()
                .setAdminId(admin.getId())
                .setUsername(admin.getUsername())
                .setFullname(admin.getFullname())
                .setEmail(admin.getEmail())
                .setGender(admin.getGender())
                .setMobile(admin.getMobile())
                .setAvatar(admin.getAvatar())
                .setLastLoginIp(admin.getLastLoginIp())
                .setLastLoginTime(admin.getLastLoginTime())
                .setStatus(admin.getStatus())
                .setSuperAdmin(admin.getSuperAdmin())
                .setAddTime(admin.getAddTime())
                .setUpdateTime(admin.getUpdateTime())
                .setRoles(roleList);

        return ApiResult.ok(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult update(AdminUpdateParam adminUpdateParam) {
        WebAdmin admin = new WebAdmin();
        admin.setId(adminUpdateParam.getAdminId());
        admin.setFullname(adminUpdateParam.getFullname());
        admin.setGender(adminUpdateParam.getGender());
        admin.setAge(adminUpdateParam.getAge());
        admin.setMobile(adminUpdateParam.getMobile());
        admin.setAvatar(adminUpdateParam.getAvatar());
        admin.setStatus(adminUpdateParam.getStatus());
        admin.setRoleId(adminUpdateParam.getRoleId());
        admin.setSuperAdmin(adminUpdateParam.getSuperAdmin());
        adminDao.updateById(admin);
        return ApiResult.ok();
    }

    @Override
    public ApiResult findPassword(String username) {
        WebAdmin admin = adminDao.getByUsername(username);
        if (admin == null) {
            return ApiResult.fail(ApiCode.ADMIN_NOT_EXISTS);
        }
        if (!admin.getStatus()) {
            return ApiResult.fail(ApiCode.ADMIN_FORBIDDEN);
        }
        if (StringUtils.isBlank(admin.getEmail())) {
            return ApiResult.fail(ApiCode.MAIL_NOT_EXISTS);
        }

        // 生成认证码
        String md5 = DigestUtil.md5();
        // 生成新的密码
        String newPassword = PasswordUtil.gen(CommonConstant.NEW_PWD_LEN);

        // 生成的新密码写入缓存
        String key = String.format(CommonRedisKey.FIND_PASSWORD, username);
        redisTemplate.hset(key, md5, newPassword);

        // 发送认证邮件
        try {
            String gatewayUrl = gateway + "admin/confirm/reset/password?u=" + username + "&c=" + md5;
            String title = "密码重置";
            String content = "您好：<br/>" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;新密码: <strong>" + newPassword + "</strong><br/>" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;点击链接，确认修改密码，链接有效期24h，" + gatewayUrl;
            MailUtil.send(admin.getEmail(), admin.getUsername(), title, content, new Date());
        } catch (Exception e) {
            log.error("邮件发送失败，请联系管理员: ", e);
            return ApiResult.fail(ApiCode.MAIL_SEND_FAIL);
        }

        return ApiResult.ok();
    }

    @Override
    public ApiResult confirmResetPassword(String u, String c) {
        String key = String.format(CommonRedisKey.FIND_PASSWORD, u);
        String newPassword = redisTemplate.hget(key, c);

        WebAdmin admin = adminDao.getByUsername(u);
        if (admin == null) {
            return ApiResult.fail(ApiCode.ADMIN_NOT_EXISTS);
        }
        if (StringUtils.isBlank(newPassword)) {
            return ApiResult.fail(ApiCode.CONFIRM_RESET_PASSWORD);
        }

        String newPasswordMd5 = Md5Util.md5(newPassword);
        admin.setPassword(newPasswordMd5);
        adminDao.updateById(admin);

        // 清除用户登录redis
        key = String.format(CommonRedisKey.LOGIN_TOKEN, admin.getId());
        redisTemplate.del(key);

        return ApiResult.ok();
    }

    @Override
    public ApiResult updatePassword(AdminUpdatePwdParam adminUpdatePwdParam) {
        String oldPassword = loginService.decryptPassword(adminUpdatePwdParam.getOldPassword());
        String newPassword = loginService.decryptPassword(adminUpdatePwdParam.getNewPassword());

        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            return ApiResult.fail(ApiCode.OLD_PASSWORD_ERROR);
        }

        String oldPasswordMd5 = Md5Util.md5(oldPassword);
        String newPasswordMd5 = Md5Util.md5(newPassword);

        WebAdmin admin = adminDao.getById(adminUpdatePwdParam.getAdminId());
        if (admin == null) {
            return ApiResult.fail(ApiCode.ADMIN_NOT_EXISTS);
        }
        if (!oldPasswordMd5.equals(admin.getPassword())) {
            return ApiResult.fail(ApiCode.OLD_PASSWORD_ERROR);
        }

        // 修改用户密码
        admin.setPassword(newPasswordMd5);
        adminDao.updateById(admin);

        // 清除用户登录redis
        String key = String.format(CommonRedisKey.LOGIN_TOKEN, admin.getId());
        redisTemplate.del(key);

        return ApiResult.ok();
    }

    @Override
    public ApiResult updateMail(AdminUpdateMailParam adminUpdateMailParam) {
        String password = loginService.decryptPassword(adminUpdateMailParam.getPassword());
        if (StringUtils.isEmpty(password)) {
            log.error("加密密码解密失败");
            return ApiResult.fail(ApiCode.PASSWORD_ERROR);
        }

        String passwordMd5 = Md5Util.md5(password);
        WebAdmin admin = adminDao.getById(adminUpdateMailParam.getAdminId());
        if (admin == null) {
            return ApiResult.fail(ApiCode.ADMIN_NOT_EXISTS);
        }
        if (!passwordMd5.equals(admin.getPassword())) {
            return ApiResult.fail(ApiCode.PASSWORD_ERROR);
        }
        if (!admin.getStatus()) {
            return ApiResult.fail(ApiCode.ADMIN_FORBIDDEN);
        }

        admin.setEmail(adminUpdateMailParam.getNewEmail());
        adminDao.updateById(admin);

        return ApiResult.ok();
    }
}
