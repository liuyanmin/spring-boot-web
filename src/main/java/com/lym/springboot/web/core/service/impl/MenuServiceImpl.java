package com.lym.springboot.web.core.service.impl;

import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.core.dao.MenuDao;
import com.lym.springboot.web.core.dao.PermissionDao;
import com.lym.springboot.web.core.domain.WebMenu;
import com.lym.springboot.web.core.domain.WebMenuPermissionRe;
import com.lym.springboot.web.core.domain.WebPermission;
import com.lym.springboot.web.core.dto.request.MenuSaveParam;
import com.lym.springboot.web.core.dto.request.MenuUpdateParam;
import com.lym.springboot.web.core.dto.response.MenuDetailResponse;
import com.lym.springboot.web.core.dto.response.MenuListResponse;
import com.lym.springboot.web.core.dto.response.MenuNewResponse;
import com.lym.springboot.web.core.service.IMenuService;
import com.lym.springboot.web.core.vo.MenuPermissionParamVo;
import com.lym.springboot.web.core.vo.MenuPermissionResponseVo;
import com.lym.springboot.web.core.vo.MenuVo;
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
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult save(MenuSaveParam menuSaveParam) {
        WebMenu menu = new WebMenu();
        menu.setMenuName(menuSaveParam.getMenuName());
        menu.setParentId(menuSaveParam.getParentId());
        menu.setSortBy(menuSaveParam.getSortBy());
        menu.setFrontUrl(menuSaveParam.getFrontUrl());
        menu.setIcon(menuSaveParam.getIcon());
        menu.setStatus(menuSaveParam.getStatus());
        menuDao.insertMenu(menu);

        List<MenuPermissionParamVo> menuPermissions = menuSaveParam.getMenuPermissions();
        if (menuPermissions == null || menuPermissions.size() == 0) {
            return ApiResult.ok();
        }

        List<WebMenuPermissionRe> menuPermissionRes = new ArrayList<>();
        for (MenuPermissionParamVo menuPermissionVo : menuPermissions) {
            WebMenuPermissionRe menuPermissionRe = new WebMenuPermissionRe();
            menuPermissionRe.setMenuId(menu.getId());
            menuPermissionRe.setPermissionId(menuPermissionVo.getPermissionId());
            menuPermissionRe.setFrontUrl(menuPermissionVo.getFrontUrl());
            menuPermissionRe.setApiUri(menuPermissionVo.getApiUri());
            menuPermissionRes.add(menuPermissionRe);
        }
        menuDao.insertMenuPermission(menuPermissionRes);
        return ApiResult.ok();
    }

    @Override
    public ApiResult<List<MenuListResponse>> list(Integer menuId) {
        List<MenuListResponse> menus = menuDao.list(menuId);
        return ApiResult.ok(menus);
    }

    @Override
    public ApiResult<MenuDetailResponse> detail(Integer menuId) {
        WebMenu menu = menuDao.getById(menuId);

        // 查询所有菜单
        List<WebMenu> allMenus = menuDao.getAll();
        Map<Integer, List<WebMenu>> allMenuMap = new HashMap<>();
        if (allMenus != null && allMenus.size() > 0) {
            allMenuMap = allMenus.stream().collect(Collectors.groupingBy(WebMenu::getParentId));
        }

        List<MenuVo> menuList = new ArrayList<>();
        // 查询一级菜单
        List<WebMenu> menus = menuDao.getSubMenu(0);
        for (WebMenu menu1 : menus) {
            MenuVo vo = new MenuVo();
            vo.setMenuId(menu1.getId());
            vo.setMenuName(menu1.getMenuName());
            vo.setParentId(menu1.getParentId());
            menuList.add(treeMenus(vo, allMenuMap));
        }

        // 判断当前菜单是否最后一级菜单，若不是最后一级菜单，则不返回权限列表
        List<WebMenu> subMenus = menuDao.getSubMenu(menu.getId());
        if (subMenus != null && subMenus.size() > 0) {
            MenuDetailResponse response = new MenuDetailResponse()
                    .setMenu(menu)
                    .setMenuList(menuList)
                    .setMenuPermissions(null);
            return ApiResult.ok(response);
        }

        // 查询当前菜单下具有的权限
        List<WebMenuPermissionRe> menuPermissionRes = menuDao.getMenuPermission(menuId);
        Map<Integer, WebMenuPermissionRe> menuPermissionReMap = new HashMap<>();
        if (menuPermissionRes != null) {
            menuPermissionRes.forEach(e -> menuPermissionReMap.put(e.getPermissionId(), e));
        }

        // 查询所有权限
        List<WebPermission> permissions = permissionDao.getAll();

        List<MenuPermissionResponseVo> menuPermissionResponseVos = new ArrayList<>();
        if (permissions != null) {
            for (WebPermission permission : permissions) {
                MenuPermissionResponseVo vo = new MenuPermissionResponseVo();
                vo.setPermissionId(permission.getId());
                vo.setPermissionName(permission.getPermissionName());
                vo.setSelected(false);

                WebMenuPermissionRe menuPermissionRe = menuPermissionReMap.get(permission.getId());
                if (menuPermissionRe != null) {
                    vo.setApiUri(menuPermissionRe.getApiUri());
                    vo.setFrontUrl(menuPermissionRe.getFrontUrl());
                    vo.setSelected(true);
                }
                menuPermissionResponseVos.add(vo);
            }
        }

        MenuDetailResponse response = new MenuDetailResponse()
                .setMenu(menu)
                .setMenuList(menuList)
                .setMenuPermissions(menuPermissionResponseVos);
        return ApiResult.ok(response);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public ApiResult update(MenuUpdateParam menuUpdateParam) {
        WebMenu menu = new WebMenu();
        menu.setId(menuUpdateParam.getMenuId());
        menu.setMenuName(menuUpdateParam.getMenuName());
        menu.setParentId(menuUpdateParam.getParentId());
        menu.setSortBy(menuUpdateParam.getSortBy());
        menu.setFrontUrl(menuUpdateParam.getFrontUrl());
        menu.setIcon(menuUpdateParam.getIcon());
        menu.setStatus(menuUpdateParam.getStatus());
        menuDao.update(menu);

        List<MenuPermissionParamVo> menuPermissions = menuUpdateParam.getMenuPermissions();
        if (menuPermissions == null) {
            return ApiResult.ok();
        }

        List<WebMenuPermissionRe> menuPermissionRes = new ArrayList<>();
        for (MenuPermissionParamVo menuPermissionVo : menuPermissions) {
            WebMenuPermissionRe menuPermissionRe = new WebMenuPermissionRe();
            menuPermissionRe.setMenuId(menu.getId());
            menuPermissionRe.setPermissionId(menuPermissionVo.getPermissionId());
            menuPermissionRe.setFrontUrl(menuPermissionVo.getFrontUrl());
            menuPermissionRe.setApiUri(menuPermissionVo.getApiUri());
            menuPermissionRes.add(menuPermissionRe);
        }
        menuDao.deleteMenuPermission(menuUpdateParam.getMenuId());
        menuDao.insertMenuPermission(menuPermissionRes);

        return ApiResult.ok();
    }

    /**
     * 遍历树菜单
     * @param currentMenu 当前菜单
     * @param allMenuMap 所有菜单，key-父菜单ID value-子菜单列表
     * @return
     */
    private MenuVo treeMenus(MenuVo currentMenu, Map<Integer, List<WebMenu>> allMenuMap) {

        // 查找当前菜单的所有子菜单
        List<WebMenu> subMenus = allMenuMap.get(currentMenu.getMenuId());
        if (subMenus == null || subMenus.size() == 0) {
            return currentMenu;
        }

        // 不是最后一级菜单，继续找子菜单
        subMenus = subMenus.stream().sorted(Comparator.comparing(WebMenu::getSortBy)).collect(Collectors.toList());
        // 当前菜单的子菜单
        List<MenuVo> childrens = currentMenu.getChildrens();
        for (WebMenu subMenu : subMenus) {
            MenuVo vo = new MenuVo ();
            vo.setMenuId(subMenu.getId());
            vo.setMenuName(subMenu.getMenuName());
            vo.setParentId(subMenu.getParentId());

            if (childrens == null) {
                childrens = new ArrayList<>();
            }
            childrens.add(vo);
            currentMenu.setChildrens(childrens);
            treeMenus(vo, allMenuMap);
        }

        return currentMenu;
    }

    @Override
    public ApiResult<MenuNewResponse> newPage() {
        // 查询所有菜单
        List<WebMenu> allMenus = menuDao.getAll();
        Map<Integer, List<WebMenu>> allMenuMap = new HashMap<>();
        if (allMenus != null && allMenus.size() > 0) {
            allMenuMap = allMenus.stream().collect(Collectors.groupingBy(WebMenu::getParentId));
        }

        List<MenuVo> menuList = new ArrayList<>();
        // 查询一级菜单
        List<WebMenu> menus = menuDao.getSubMenu(0);
        for (WebMenu menu1 : menus) {
            MenuVo vo = new MenuVo();
            vo.setMenuId(menu1.getId());
            vo.setMenuName(menu1.getMenuName());
            vo.setParentId(menu1.getParentId());
            menuList.add(treeMenus(vo, allMenuMap));
        }

        // 查询所有权限
        List<WebPermission> permissions = permissionDao.getAll();

        List<MenuPermissionResponseVo> menuPermissionResponseVos = new ArrayList<>();
        if (permissions != null) {
            for (WebPermission permission : permissions) {
                MenuPermissionResponseVo vo = new MenuPermissionResponseVo();
                vo.setPermissionId(permission.getId());
                vo.setPermissionName(permission.getPermissionName());
                vo.setSelected(false);
                menuPermissionResponseVos.add(vo);
            }
        }

        MenuNewResponse response = new MenuNewResponse()
                .setMenuList(menuList)
                .setMenuPermissions(menuPermissionResponseVos);

        return ApiResult.ok(response);
    }
}
