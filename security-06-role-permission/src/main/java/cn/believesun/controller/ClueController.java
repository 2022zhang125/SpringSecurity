package cn.believesun.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClueController {
    /**
     * 线索管理
     * 线索管理
     * 线索管理-列表
     * 线索管理-录入
     * 线索管理-编辑
     * 线索管理-查看
     * 线索管理-导入
     */
    @GetMapping("/api/index")
    public String index() {
        return "index";
    }

    @GetMapping("/api/clue/menu")
    @PreAuthorize("hasAuthority('saler')") // 只有 saler 能访问
    public String clueMenu(){
        return "ClueMenu";
    }
 
    @GetMapping("/api/clue/child")
    @PreAuthorize("hasAnyAuthority('saler','admin')") // 只有 saler 和admin 能访问
    public String clueChild(){
        return "ClueChild";
    }

    @GetMapping("/api/list")
    @PreAuthorize("hasAuthority('admin')")
    public String clueList(){
        return "clueList";
    }
    // 剩下的不想写了，懂我意思就行......
}
