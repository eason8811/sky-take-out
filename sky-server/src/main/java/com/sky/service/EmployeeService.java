package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

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
     *
     * @param employeeDTO 新增员工的数据传输对象
     */
    void insert(EmployeeDTO employeeDTO);

    /**
     * @param employeePageQueryDTO 员工分页查询的数据传输对象
     * @return 返回 PageResult 对象封装的分页信息
     */
    PageResult list(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工状态
     *
     * @param id     需要修改的员工 ID
     * @param status 需要修改成的状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据 ID 查询员工
     *
     * @param id 查询的员工 ID
     * @return Employee 员工实体类对象
     */
    Employee listById(Long id);

    /**
     * 修改员工信息
     *
     * @param employeeDTO 修改员工的数据传输对象
     */
    void update(EmployeeDTO employeeDTO);

    /**
     * 修改员工密码
     *
     * @param passwordEditDTO 修改员工密码的数据传输对象
     */
    void updatePassword(PasswordEditDTO passwordEditDTO);
}
