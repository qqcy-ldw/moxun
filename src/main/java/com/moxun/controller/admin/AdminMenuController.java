package com.moxun.controller.admin;

import com.moxun.Pojo.Dto.MenuSaveDTO;
import com.moxun.Pojo.Entity.SysMenu;
import com.moxun.Pojo.Vo.MenuTreeVO;
import com.moxun.service.admin.AdminMenuService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员 - 菜单管理接口
 *
 * 权限对应 sys_menu 表：system:menu:view / add / edit / delete
 */
@Slf4j
@RestController
@RequestMapping("/admin/menus")
public class AdminMenuController {

    @Autowired
    private AdminMenuService adminMenuService;

    /**
     * 获取菜单树（树形结构，用于前端侧边栏等）
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:menu:view')")
    @GetMapping("/tree")
    public Result<List<MenuTreeVO>> listMenuTree() {
        List<MenuTreeVO> tree = adminMenuService.listMenuTree();
        return Result.success(tree);
    }

    /**
     * 分页查询菜单列表（扁平）
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:menu:view')")
    @GetMapping
    public Result<List<SysMenu>> listMenus(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<SysMenu> list = adminMenuService.listMenus(page, pageSize);
        return Result.success(list);
    }

    /**
     * 根据ID获取菜单详情
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:menu:view')")
    @GetMapping("/{id}")
    public Result<SysMenu> getMenuById(@PathVariable Long id) {
        SysMenu menu = adminMenuService.getMenuById(id);
        return Result.success(menu);
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:menu:add')")
    @PostMapping
    public Result<String> addMenu(@RequestBody MenuSaveDTO dto) {
        adminMenuService.addMenu(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑菜单
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:menu:edit')")
    @PutMapping("/{id}")
    public Result<String> updateMenu(@PathVariable Long id, @RequestBody MenuSaveDTO dto) {
        dto.setId(id);
        adminMenuService.updateMenu(dto);
        return Result.success("编辑成功");
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:menu:delete')")
    @DeleteMapping("/{id}")
    public Result<String> deleteMenu(@PathVariable Long id) {
        adminMenuService.deleteMenu(id);
        return Result.success("删除成功");
    }
}
