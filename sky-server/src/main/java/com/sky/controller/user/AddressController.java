package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 新增地址信息
     *
     * @param addressBook 需要新增的地址的实体类对象
     * @return 返回Result格式的对象
     */
    @PostMapping
    public Result<Object> insert(@RequestBody AddressBook addressBook){
        log.info("正在新增地址簿, 参数为: {}", addressBook);
        addressService.insert(addressBook);
        return Result.success();
    }

    /**
     * 查询用户的所有地址的列表
     *
     * @return 返回Result格式的对象
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        log.info("正在查询用户的所有地址的列表");
        List<AddressBook> addressBookList = addressService.list();
        return Result.success(addressBookList);
    }

    /**
     * 查询用户的默认地址
     *
     * @return 返回Result格式的对象
     */
    @GetMapping("/default")
    public Result<AddressBook> listDefault(){
        log.info("正在查询用户的默认地址");
        AddressBook addressBook = addressService.listDefault();
        return Result.success(addressBook);
    }

    /**
     * 修改用户的地址
     *
     * @param addressBook 需要修改的地址的目标地址信息
     * @return 返回Result格式的对象
     */
    @PutMapping
    public Result<Object> update(@RequestBody AddressBook addressBook){
        log.info("修改地址, 参数为: {}", addressBook);
        addressService.update(addressBook);
        return Result.success();
    }

    /**
     * 根据 ID 删除地址信息
     *
     * @param id 需要删除的地址的 ID
     * @return 返回Result格式的对象
     */
    @DeleteMapping
    public Result<Object> delete(Long id){
        log.info("正在删除地址信息, ID 为: {}", id);
        addressService.delete(id);
        return Result.success();
    }

    /**
     * 根据 ID 查询地址信息
     *
     * @param id 需要查询的地址的 ID
     * @return 返回Result格式的对象
     */
    @GetMapping("/{id}")
    public Result<AddressBook> listById(@PathVariable Long id){
        log.info("正在根据 ID 查询地址信息, ID 为: {}", id);
        AddressBook addressBook = addressService.listById(id);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook 用于接收需要设置为默认地址的地址 ID 的实体类对象
     * @return 返回Result格式的对象
     */
    @PutMapping("/default")
    public Result<Object> setDefault(@RequestBody AddressBook addressBook){
        log.info("设置默认地址, 默认地址的参数为: {}", addressBook);
        addressService.setDefault(addressBook);
        return Result.success();
    }
}
