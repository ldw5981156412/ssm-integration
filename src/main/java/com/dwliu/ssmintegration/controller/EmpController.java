package com.dwliu.ssmintegration.controller;

import com.dwliu.ssmintegration.dao.entity.Emp;
import com.dwliu.ssmintegration.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author dwliu
 */
@Controller
@RequestMapping(value = "/emp")
public class EmpController {

    @Autowired
    private EmpService empService;

    @ResponseBody
    @GetMapping("/f")
    public String find() {
        return "find success";
    }

    @ResponseBody
    @PostMapping("/a")
    public String insert(Emp emp) {
        empService.insert(emp);
        return "insert success";
    }
    @ResponseBody
    @GetMapping("/all")
    public Object findAllUser(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                    int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                    int pageSize){
        return empService.findAllEmp(pageNum,pageSize);
    }
}
