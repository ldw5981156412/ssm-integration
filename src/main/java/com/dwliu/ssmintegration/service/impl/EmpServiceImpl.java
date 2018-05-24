package com.dwliu.ssmintegration.service.impl;

import com.dwliu.ssmintegration.dao.entity.Emp;
import com.dwliu.ssmintegration.dao.mapper.EmpMapper;
import com.dwliu.ssmintegration.service.EmpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Override
    public int insert(Emp record) {
        return empMapper.insert(record);
    }

    @Override
    public PageInfo<Emp> findAllEmp(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Emp> empList = empMapper.selectByExample(null);
        PageInfo result = new PageInfo(empList);
        return result;
    }
}
