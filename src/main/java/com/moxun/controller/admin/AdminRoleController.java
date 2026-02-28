package com.moxun.controller.admin;

import com.moxun.Pojo.Dto.RoleSaveDTO;
import com.moxun.Pojo.Vo.RoleVO;
import com.moxun.service.admin.AdminRoleService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员 - 角色管理接口
 *
 * 权限对应 sys_menu 表：system:role:view / add / edit
 */
@Slf4j
@RestController
@RequestMapping("/admin/roles")
public class AdminRoleController {

    @Autowired
    private AdminRoleService adminRoleService;

    /**
     * 分页查询角色列表
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:role:view')")
    @GetMapping
    public Result<List<RoleVO>> listRoles(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<RoleVO> list = adminRoleService.listRoles(page, pageSize);
        return Result.success(list);
    }

    /**
     * 根据ID获取角色详情（含关联的菜单ID列表）
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:role:view')")
    @GetMapping("/{id}")
    public Result<RoleVO> getRoleById(@PathVariable Long id) {
        RoleVO vo = adminRoleService.getRoleById(id);
        return Result.success(vo);
    }

    /**
     * 新增角色
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:role:add')")
    @PostMapping
    public Result<String> addRole(@RequestBody RoleSaveDTO dto) {
        adminRoleService.addRole(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑角色
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:role:edit')")
    @PutMapping("/{id}")
    public Result<String> updateRole(@PathVariable Long id, @RequestBody RoleSaveDTO dto) {
        dto.setId(id);
        adminRoleService.updateRole(dto);
        return Result.success("编辑成功");
    }

    /**
     * 删除角色
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:role:edit')")
    @DeleteMapping("/{id}")
    public Result<String> deleteRole(@PathVariable Long id) {
        adminRoleService.deleteRole(id);
        return Result.success("删除成功");
    }
}
