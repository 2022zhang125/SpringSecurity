package cn.believesun.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClueController {
    /**
     * 线索管理
     * 线索管理
     * 线索管理-列表 clue:list
     * 线索管理-录入 clue:add
     * 线索管理-编辑 clue:edit
     * 线索管理-查看 clue:view
     * 线索管理-导入 clue:import
     */
    @GetMapping("/api/index")
    public String index() {
        return "index";
    }

    @GetMapping("/api/clue/menu")
    public String clueMenu(){
        return "ClueMenu";
    }

    @GetMapping("/api/clue/child")
    public String clueChild(){
        return "ClueChild";
    }

    @GetMapping("/api/list")
    @PreAuthorize("hasAuthority('clue:list')")
    // 总而言之，言而总之，角色权限控制时，在add集合时添加ROLE_ 前缀并且PreAuthorize中使用hasRole;
    // 资源权限控制时，不添加ROLE_前缀，使用PreAuthorize的hasAuthority即可。
    public String clueList(){
        return "clueList";
    }
    // 剩下的不想写了，懂我意思就行......
}
