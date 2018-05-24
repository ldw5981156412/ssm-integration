package com.dwliu.ssmintegration.service;

import com.dwliu.ssmintegration.dao.entity.Emp;
import com.github.pagehelper.PageInfo;

/**
 * @author dwliu
 */
public interface EmpService {

    int insert(Emp record);

    PageInfo<Emp> findAllEmp(int pageNum, int pageSize);
}
