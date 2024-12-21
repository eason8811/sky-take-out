package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import io.swagger.models.auth.In;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 员工登录的数据传输对象
     * @return 返回 employee 员工实体对象
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 添加员工
     * @param employeeDTO 新增员工的数据传输对象
     * */
    void insert(EmployeeDTO employeeDTO);

    /**
     * @param employeePageQueryDTO 员工分页查询的数据传输对象
     * @return 返回 PageResult 对象封装的分页信息
     * */
    PageResult list(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工状态
     *
     * @param id 需要修改的员工 ID
     * @param status 需要修改成的状态
     * */
    void updateStatus(Integer id, Integer status);
}
