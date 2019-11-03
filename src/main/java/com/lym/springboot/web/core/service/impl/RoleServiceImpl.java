package com.lym.springboot.web.core.service.impl;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.dao.MenuDao;
import com.lym.springboot.web.core.dao.PermissionDao;
import com.lym.springboot.web.core.dao.RoleDao;
import com.lym.springboot.web.core.domain.WebMenu;
import com.lym.springboot.web.core.domain.WebMenuPermissionRe;
import com.lym.springboot.web.core.domain.WebRole;
import com.lym.springboot.web.core.domain.WebRoleMenuRe;
import com.lym.springboot.web.core.dto.request.RoleListParam;
import com.lym.springboot.web.core.dto.request.RoleSaveParam;
import com.lym.springboot.web.core.dto.request.RoleUpdateParam;
import com.lym.springboot.web.core.dto.response.RoleDetailResponse;
import com.lym.springboot.web.core.service.IRoleService;
import com.lym.springboot.web.core.vo.PermissionVo;
import com.lym.springboot.web.core.vo.RoleMenuDetailResponseVo;
import com.lym.springboot.web.core.vo.RoleMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult save(RoleSaveParam roleSaveParam) {
        WebRole role = new WebRole();
        role.setRoleName(roleSaveParam.getRoleName());
        role.setDescription(roleSaveParam.getDescription());
        role.setStatus(roleSaveParam.getStatus());
        roleDao.insertRole(role);

        List<RoleMenuVo> roleMenus = roleSaveParam.getMenuPermissions();
        if (roleMenus == null || roleMenus.size() == 0) {
            return ApiResult.ok();
        }

        List<WebRoleMenuRe> roleMenuRes = new ArrayList<>();
        for (RoleMenuVo roleMenu : roleMenus) {
            List<Integer> permissionIds = roleMenu.getPermissionIds();
            if (permissionIds == null || permissionIds.size() == 0) {
                WebRoleMenuRe roleMenuRe = new WebRoleMenuRe();
                roleMenuRe.setRoleId(role.getId());
                roleMenuRe.setMenuId(roleMenu.getMenuId());
                roleMenuRes.add(roleMenuRe);
                continue;
            }
            for (Integer permissionId : permissionIds) {
                WebRoleMenuRe roleMenuRe = new WebRoleMenuRe();
                roleMenuRe.setRoleId(role.getId());
                roleMenuRe.setMenuId(roleMenu.getMenuId());
                roleMenuRe.setPermissionId(permissionId);
                roleMenuRes.add(roleMenuRe);
            }
        }
        roleDao.insertRoleMenu(roleMenuRes);

        return ApiResult.ok();
    }

    @Override
    public ApiResult<PageBean<WebRole>> list(RoleListParam roleListParam) {
        PageBean<WebRole> pageBean = roleDao.list(roleListParam);
        return ApiResult.ok(pageBean);
    }

    @Override
    public ApiResult<RoleDetailResponse> detail(Integer roleId) {
        WebRole role = roleDao.getById(roleId);

        Map<Integer, List<WebRoleMenuRe>> hasMenuPermissionMap = new HashMap<>();
        List<Integer> hasMenuIds = new ArrayList<>();

        // 查询当前角色拥有的所有菜单权限
        List<WebRoleMenuRe> roleMenuRes = roleDao.getRolePermission(roleId);
        if (roleMenuRes != null && roleMenuRes.size() > 0) {
            // 每个菜单具有的权限
            hasMenuPermissionMap = roleMenuRes.stream().collect(Collectors.groupingBy(WebRoleMenuRe::getMenuId));
            hasMenuIds = roleMenuRes.stream().map(WebRoleMenuRe::getId).collect(Collectors.toList());
        }

        // 查询所有菜单
        List<WebMenu> allMenus = menuDao.getAll();
        Map<Integer, List<WebMenu>> allMenuMap = new HashMap<>();
        if (allMenus != null && allMenus.size() > 0) {
            allMenuMap = allMenus.stream().collect(Collectors.groupingBy(WebMenu::getParentId));
        }

        // 查询所有菜单权限
        List<WebMenuPermissionRe> allMenuPermissionRes = menuDao.getAllMenuPermission();
        Map<Integer, List<WebMenuPermissionRe>> allMenuPermissionMap = new HashMap<>();
        if (allMenuPermissionRes != null && allMenuPermissionRes.size() > 0) {
            allMenuPermissionMap = allMenuPermissionRes.stream().collect(Collectors.groupingBy(WebMenuPermissionRe::getMenuId));
        }

        // 查询权限名称
        Map<Integer, String> permissionNameMap = permissionDao.getAllPermissionNameMap();

        List<RoleMenuDetailResponseVo> menuList = new ArrayList<>();
        // 一级菜单
        List<WebMenu> rootMenus = allMenuMap.get(0);
        if (rootMenus != null) {
            // 一级菜单排序
            rootMenus = rootMenus.stream().sorted(Comparator.comparing(WebMenu::getSortBy)).collect(Collectors.toList());
            for (WebMenu menu : rootMenus) {
                RoleMenuDetailResponseVo vo = new RoleMenuDetailResponseVo()
                        .setMenuId(menu.getId())
                        .setMenuName(menu.getMenuName())
                        .setParentId(menu.getParentId());
                menuList.add(treeMenus(vo, allMenuMap, allMenuPermissionMap, hasMenuIds, hasMenuPermissionMap, permissionNameMap));
            }
        }

        RoleDetailResponse response = new RoleDetailResponse()
                .setRoleId(role.getId())
                .setRoleName(role.getRoleName())
                .setDescription(role.getDescription())
                .setStatus(role.getStatus())
                .setMenuList(menuList);
        return ApiResult.ok(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult update(RoleUpdateParam roleUpdateParam) {
        WebRole role = new WebRole();
        role.setId(roleUpdateParam.getRoleId());
        role.setRoleName(roleUpdateParam.getRoleName());
        role.setDescription(roleUpdateParam.getDescription());
        role.setStatus(roleUpdateParam.getStatus());
        roleDao.updateRole(role);

        List<RoleMenuVo> roleMenus = roleUpdateParam.getMenuPermissions();
        if (roleMenus == null || roleMenus.size() == 0) {
            return ApiResult.ok();
        }

        List<WebRoleMenuRe> roleMenuRes = new ArrayList<>();
        for (RoleMenuVo roleMenu : roleMenus) {
            List<Integer> permissionIds = roleMenu.getPermissionIds();
            if (permissionIds == null || permissionIds.size() == 0) {
                WebRoleMenuRe roleMenuRe = new WebRoleMenuRe();
                roleMenuRe.setRoleId(role.getId());
                roleMenuRe.setMenuId(roleMenu.getMenuId());
                roleMenuRes.add(roleMenuRe);
                continue;
            }
            for (Integer permissionId : permissionIds) {
                WebRoleMenuRe roleMenuRe = new WebRoleMenuRe();
                roleMenuRe.setRoleId(role.getId());
                roleMenuRe.setMenuId(roleMenu.getMenuId());
                roleMenuRe.setPermissionId(permissionId);
                roleMenuRes.add(roleMenuRe);
            }
        }
        roleDao.deleteRolePermission(roleUpdateParam.getRoleId());
        roleDao.insertRoleMenu(roleMenuRes);
        return ApiResult.ok();
    }

    /**
     * 遍历树菜单
     * @param currentMenu 当前菜单
     * @param allMenuMap 所有菜单，key-父菜单ID value-子菜单列表
     * @param allMenuPermissionMap 所有菜单权限，key-菜单ID value-菜单下的所有权限
     * @param hasMenuIds 当前角色拥有的菜单
     * @param hasMenuPermissionMap 当前角色拥有的菜单权限，key-菜单ID value-菜单下的所有权限
     * @param permissionNameMap 权限名称
     * @return
     */
    private RoleMenuDetailResponseVo treeMenus(RoleMenuDetailResponseVo currentMenu, Map<Integer, List<WebMenu>> allMenuMap,
                                                     Map<Integer, List<WebMenuPermissionRe>> allMenuPermissionMap,
                                                     List<Integer> hasMenuIds,
                                                     Map<Integer, List<WebRoleMenuRe>> hasMenuPermissionMap,
                                               Map<Integer, String> permissionNameMap) {

        // 判断当前菜单是否被选中
        if (hasMenuIds != null && hasMenuIds.size() > 0) {
            currentMenu.setSelected(hasMenuIds.contains(currentMenu.getMenuId()));
        }

        // 查找当前菜单的所有子菜单
        List<WebMenu> subMenus = allMenuMap.get(currentMenu.getMenuId());
        // 最后一级菜单，查找是否有权限
        if (subMenus == null || subMenus.size() == 0) {
            // 当前菜单下具有的所有权限
            List<WebMenuPermissionRe> menuPermissionRes = allMenuPermissionMap.get(currentMenu.getMenuId());
            if (menuPermissionRes != null) {
                List<PermissionVo> permissionVos = new ArrayList<>();
                for (WebMenuPermissionRe menuPermissionRe : menuPermissionRes) {
                    // 当前菜单下已经选中的权限
                    List<WebRoleMenuRe> roleMenuRes = hasMenuPermissionMap.get(menuPermissionRe.getMenuId());
                    List<Integer> hasPermissionIds = new ArrayList<>();
                    if (roleMenuRes != null) {
                        hasPermissionIds = roleMenuRes.stream().map(WebRoleMenuRe::getPermissionId).collect(Collectors.toList());
                    }

                    Integer permissionId = menuPermissionRe.getPermissionId();
                    PermissionVo vo = new PermissionVo()
                            .setPermissionId(permissionId)
                            .setPermissionName(permissionNameMap.get(permissionId))
                            .setSelected(hasPermissionIds.contains(permissionId));
                    permissionVos.add(vo);
                }
                currentMenu.setPermissions(permissionVos);
            }
            return currentMenu;
        }

        // 不是最后一级菜单，继续找子菜单
        subMenus = subMenus.stream().sorted(Comparator.comparing(WebMenu::getSortBy)).collect(Collectors.toList());
        // 当前菜单的子菜单
        List<RoleMenuDetailResponseVo> childrens = currentMenu.getChildrens();
        for (WebMenu subMenu : subMenus) {
            RoleMenuDetailResponseVo vo = new RoleMenuDetailResponseVo();
            vo.setMenuId(subMenu.getId());
            vo.setMenuName(subMenu.getMenuName());
            vo.setParentId(subMenu.getParentId());

            if (childrens == null) {
                childrens = new ArrayList<>();
            }
            childrens.add(vo);
            currentMenu.setChildrens(childrens);
            treeMenus(vo, allMenuMap, allMenuPermissionMap, hasMenuIds, hasMenuPermissionMap, permissionNameMap);
        }

        return currentMenu;
    }

    @Override
    public ApiResult<RoleMenuDetailResponseVo> newPage() {

        // 查询所有菜单
        List<WebMenu> allMenus = menuDao.getAll();
        Map<Integer, List<WebMenu>> allMenuMap = new HashMap<>();
        if (allMenus != null && allMenus.size() > 0) {
            allMenuMap = allMenus.stream().collect(Collectors.groupingBy(WebMenu::getParentId));
        }

        // 查询所有菜单权限
        List<WebMenuPermissionRe> allMenuPermissionRes = menuDao.getAllMenuPermission();
        Map<Integer, List<WebMenuPermissionRe>> allMenuPermissionMap = new HashMap<>();
        if (allMenuPermissionRes != null && allMenuPermissionRes.size() > 0) {
            allMenuPermissionMap = allMenuPermissionRes.stream().collect(Collectors.groupingBy(WebMenuPermissionRe::getMenuId));
        }

        // 查询权限名称
        Map<Integer, String> permissionNameMap = permissionDao.getAllPermissionNameMap();

        List<RoleMenuDetailResponseVo> menuList = new ArrayList<>();
        // 一级菜单
        List<WebMenu> rootMenus = allMenuMap.get(0);
        if (rootMenus != null) {
            // 一级菜单排序
            rootMenus = rootMenus.stream().sorted(Comparator.comparing(WebMenu::getSortBy)).collect(Collectors.toList());
            for (WebMenu menu : rootMenus) {
                RoleMenuDetailResponseVo vo = new RoleMenuDetailResponseVo()
                        .setMenuId(menu.getId())
                        .setMenuName(menu.getMenuName())
                        .setParentId(menu.getParentId())
                        .setSelected(false);
                menuList.add(treeMenus(vo, allMenuMap, allMenuPermissionMap, permissionNameMap));
            }
        }

        return ApiResult.ok(menuList);
    }

    /**
     * 遍历树菜单
     * @param currentMenu 当前菜单
     * @param allMenuMap 所有菜单，key-父菜单ID value-子菜单列表
     * @param allMenuPermissionMap 所有菜单权限，key-菜单ID value-菜单下的所有权限
     * @param permissionNameMap 权限名称
     * @return
     */
    private RoleMenuDetailResponseVo treeMenus(RoleMenuDetailResponseVo currentMenu, Map<Integer, List<WebMenu>> allMenuMap,
                                               Map<Integer, List<WebMenuPermissionRe>> allMenuPermissionMap,
                                               Map<Integer, String> permissionNameMap) {

        // 查找当前菜单的所有子菜单
        List<WebMenu> subMenus = allMenuMap.get(currentMenu.getMenuId());
        // 最后一级菜单，查找是否有权限
        if (subMenus == null || subMenus.size() == 0) {
            // 当前菜单下具有的所有权限
            List<WebMenuPermissionRe> menuPermissionRes = allMenuPermissionMap.get(currentMenu.getMenuId());
            if (menuPermissionRes != null) {
                List<PermissionVo> permissionVos = new ArrayList<>();
                for (WebMenuPermissionRe menuPermissionRe : menuPermissionRes) {
                    Integer permissionId = menuPermissionRe.getPermissionId();
                    PermissionVo vo = new PermissionVo()
                            .setPermissionId(permissionId)
                            .setPermissionName(permissionNameMap.get(permissionId))
                            .setSelected(false);
                    permissionVos.add(vo);
                }
                currentMenu.setPermissions(permissionVos);
            }
            return currentMenu;
        }

        // 不是最后一级菜单，继续找子菜单
        subMenus = subMenus.stream().sorted(Comparator.comparing(WebMenu::getSortBy)).collect(Collectors.toList());
        // 当前菜单的子菜单
        List<RoleMenuDetailResponseVo> childrens = currentMenu.getChildrens();
        for (WebMenu subMenu : subMenus) {
            RoleMenuDetailResponseVo vo = new RoleMenuDetailResponseVo()
                    .setMenuId(subMenu.getId())
                    .setMenuName(subMenu.getMenuName())
                    .setParentId(subMenu.getParentId())
                    .setSelected(false);


            if (childrens == null) {
                childrens = new ArrayList<>();
            }
            childrens.add(vo);
            currentMenu.setChildrens(childrens);
            treeMenus(vo, allMenuMap, allMenuPermissionMap, permissionNameMap);
        }

        return currentMenu;
    }
}
