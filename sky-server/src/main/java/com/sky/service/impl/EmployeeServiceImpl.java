package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 员工登录的数据传输对象
     * @return 返回employee员工实体对象
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (StatusConstant.DISABLE.equals(employee.getStatus())) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 添加员工
     *
     * @param employeeDTO 新增员工的数据传输对象
     */
    @Override
    public void insert(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        // 插入数据
        employeeMapper.insert(employee);
    }

    /**
     * @param employeePageQueryDTO 员工分页查询的数据传输对象
     * @return 返回 PageResult 对象封装的分页信息
     */
    @Override
    public PageResult list(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> employeeList = (Page<Employee>) employeeMapper.list(employeePageQueryDTO);
        long total = employeeList.getTotal();
        return new PageResult(total, employeeList);
    }

    /**
     * 修改员工状态
     *
     * @param id     需要修改的员工 ID
     * @param status 需要修改成的状态
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据 ID 查询员工
     *
     * @param id 查询的员工 ID
     * @return Employee 员工实体类对象
     */
    @Override
    public Employee listById(Long id) {
        return employeeMapper.listById(id);
    }

    /**
     * 修改员工信息
     *
     * @param employeeDTO 修改员工的数据传输对象
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }

    /**
     * 修改员工密码
     *
     * @param passwordEditDTO 修改员工密码的数据传输对象
     */
    @Override
    public void updatePassword(PasswordEditDTO passwordEditDTO) {
        // 获取原密码，与新密码比对
        Employee employee = employeeMapper.listById(BaseContext.getCurrentId());
        String password = employee.getPassword();
        String newPassword = DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes());
        String oldPassword = DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
        // 如果旧密码与原密码不相同，则抛出密码错误异常
        if (!password.equals(oldPassword)) {
            log.error("修改密码时输入的原密码错误, 输入的原密码: {}", passwordEditDTO.getOldPassword());
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 如果旧密码与新密码相同，则抛出密码修改失败异常
        if (password.equals(newPassword)) {
            log.error("修改密码失败, 新密码: {}", passwordEditDTO.getNewPassword());
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
        // 否则调用 update 方法修改密码
        employee.setPassword(newPassword);
        employeeMapper.update(employee);
    }
}
