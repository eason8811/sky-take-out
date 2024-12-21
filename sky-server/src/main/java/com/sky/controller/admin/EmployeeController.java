package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO 员工登录的数据传输对象
     * @return 返回Result格式的结果
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出登录
     *
     * @return 返回Result格式的结果
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 新增员工的数据传输对象
     * @return 返回Result格式的结果
     */

    @PostMapping
    public Result<Object> insert(@RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工: {}", employeeDTO);
        employeeService.insert(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     *
     * @param employeePageQueryDTO 员工分页查询的数据传输对象
     * @return 返回Result格式的结果
     */
    @GetMapping("/page")
    public Result<PageResult> list(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询, 参数为: {}", employeePageQueryDTO);
        PageResult pageResult = employeeService.list(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改员工状态
     *
     * @param id     需要修改的员工 ID
     * @param status 需要修改成的状态
     * @return 返回Result格式的结果
     */
    @PostMapping("/status/{status}")
    public Result<Object> updateStatus(@RequestParam("id") Long id, @PathVariable Integer status) {
        log.info("启用或禁用员工账号, 员工 ID 为: {}, 目标状态为: {}（{}）",
                id, status, status.equals(StatusConstant.ENABLE) ? "启用" : "禁用");
        employeeService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 根据 ID 查询员工
     *
     * @param id 查询的员工 ID
     * @return 返回Result格式的结果
     */
    @GetMapping("/{id}")
    public Result<Employee> listById(@PathVariable Long id) {
        log.info("根据 ID 查询员工信息, ID 为: {}", id);
        Employee employee = employeeService.listById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     *
     * @param employeeDTO 修改员工的数据传输对象
     * @return 返回Result格式的结果
     */
    @PutMapping
    public Result<Object> update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工信息, 参数为: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 修改员工密码
     *
     * @param passwordEditDTO 修改员工密码的数据传输对象
     * @return 返回Result格式的结果
     */
    @PutMapping("/editPassword")
    public Result<Object> updatePassword(@RequestBody PasswordEditDTO passwordEditDTO){
        log.info("修改员工的密码, 员工 ID 为: {}, 新密码为: {}, 旧密码为: {}",
                passwordEditDTO.getEmpId(), passwordEditDTO.getNewPassword(), passwordEditDTO.getOldPassword());
        employeeService.updatePassword(passwordEditDTO);
        return Result.success();
    }

}
