package com.sky.mapper;

import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username 用户名
     * @return 返回Employee对象结构的数据
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 添加员工
     * @param employee 新增员工的实体对象
     * */
    void insert(Employee employee);

    /**
     * @param employeePageQueryDTO 员工分页查询的数据传输对象
     * @return 返回 Employee 的实体对象
     * */
    List<Employee> list(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工状态
     *
     * @param id 需要修改的员工 ID
     * @param status 需要修改成的状态
     * */
    @Update("update employee set status = #{status} where id = #{id}")
    void updateStatus(Integer id, Integer status);
}
