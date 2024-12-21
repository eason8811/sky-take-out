package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.models.auth.In;
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
     * */

    @PostMapping
    public Result<Object> insert(@RequestBody EmployeeDTO employeeDTO){
        log.info("添加员工: {}", employeeDTO);
        employeeService.insert(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     *
     * @param employeePageQueryDTO 员工分页查询的数据传输对象
     * @return 返回Result格式的结果
     * */
    @GetMapping("/page")
    public Result<PageResult> list(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询, 参数为: {}", employeePageQueryDTO);
        PageResult pageResult = employeeService.list(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改员工状态
     *
     * @param id 需要修改的员工 ID
     * @param status 需要修改成的状态
     * @return 返回Result格式的结果
     * */
    @PostMapping("/status/{status}")
    public Result<Object> updateStatus(@RequestParam("id") Integer id, @PathVariable Integer status){
        employeeService.updateStatus(id, status);
        return Result.success();
    }



}
