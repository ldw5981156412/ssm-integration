package com.dwliu.ssmintegration.dao.mapper;

import com.dwliu.ssmintegration.dao.entity.Emp;
import com.dwliu.ssmintegration.dao.entity.EmpCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface EmpMapper {
    int countByExample(EmpCriteria example);

    int deleteByExample(EmpCriteria example);

    int deleteByPrimaryKey(Integer empId);

    int insert(Emp record);

    int insertSelective(Emp record);

    List<Emp> selectByExampleWithRowbounds(EmpCriteria example, RowBounds rowBounds);

    List<Emp> selectByExample(EmpCriteria example);

    Emp selectByPrimaryKey(Integer empId);

    int updateByExampleSelective(@Param("record") Emp record, @Param("example") EmpCriteria example);

    int updateByExample(@Param("record") Emp record, @Param("example") EmpCriteria example);

    int updateByPrimaryKeySelective(Emp record);

    int updateByPrimaryKey(Emp record);
}