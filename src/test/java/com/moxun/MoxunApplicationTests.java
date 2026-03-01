package com.moxun;

import com.moxun.Pojo.Vo.CourseCategoryVO;
import com.moxun.mapper.admin.AdminCourseCategoryMapper;
import com.moxun.mapper.auth.AuthMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootTest
class MoxunApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private AdminCourseCategoryMapper adminCourseCategoryMapper;

    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(passwordEncoder.matches("123456", "$2a$10$IabdDxiiexD1R/mhJB2L/.wM6SOwDLcdVtjGDjSkxOVPph/K/UHGO"));
        System.out.println(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("郑静", "123456")));
    }

    @Test
    void test() {
        List<String> authorities = new ArrayList<>();

        // 1. 查询角色（格式：ROLE_角色名）
        List<String> roles = authMapper.findUserRoles(10L);
        roles.forEach(role ->
                authorities.add(role)
        );

        // 2. 查询权限（格式：system:user:view）
        List<String> permissions = authMapper.findUserPermissions(10L);
        permissions.forEach(permission ->
                authorities.add(permission)
        );

        System.out.println(permissions);

    }

    @Test
    void test2() {
        buildTreeTraditional(adminCourseCategoryMapper.listCategories()).forEach(System.out::println);
    }
    public List<CourseCategoryVO> buildTreeTraditional(List<CourseCategoryVO> allCategories) {
        // 1. 存放所有顶级（父ID为null）分类
        List<CourseCategoryVO> rootList = new ArrayList<>();

        // 2. 键是父ID，值是该父节点下的所有子节点列表
        Map<Long, List<CourseCategoryVO>> childrenMap = new HashMap<>();

        // 2.1 遍历所有分类，构建 childrenMap
        for (CourseCategoryVO category : allCategories) {
            if (category.getParentId() != null) {
                Long parentId = category.getParentId();

                // 如果 Map 中还没有这个 parentId 的列表，就创建一个
                if (!childrenMap.containsKey(parentId)) {
                    childrenMap.put(parentId, new ArrayList<>());
                }

                // 将当前分类添加到对应的父节点列表中
                childrenMap.get(parentId).add(category);
            }
        }

        // 2.2 对每个父节点下的子节点列表进行排序
        for (List<CourseCategoryVO> children : childrenMap.values()) {
            // 使用 Collections.sort 和 Comparator 进行排序
            Collections.sort(children, new Comparator<CourseCategoryVO>() {
                @Override
                public int compare(CourseCategoryVO c1, CourseCategoryVO c2) {
                    return Integer.compare(c1.getSort(), c2.getSort());
                }
            });
        }

        // 3. 构建每个节点的子节点
        for (CourseCategoryVO category : allCategories) {
            // 从 Map 中获取该节点的子节点
            List<CourseCategoryVO> children = childrenMap.get(category.getId());

            // 设置子节点（可能为 null，转换为空列表）
            if (children != null) {
                category.setChildren(children);
            } else {
                category.setChildren(new ArrayList<>());
            }

            // 如果是根节点，添加到根列表
            if (category.getParentId() == null) {
                rootList.add(category);
            }
        }

        return rootList;
    }

}
