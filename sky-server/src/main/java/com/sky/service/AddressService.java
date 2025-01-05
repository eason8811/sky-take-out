package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressService {

    /**
     * 新增地址信息
     *
     * @param addressBook 需要新增的地址的实体类对象
     */
    void insert(AddressBook addressBook);

    /**
     * 查询用户的所有地址的列表
     *
     * @return 返回封装了 AddressBook 对象的 List 集合
     */
    List<AddressBook> list();

    /**
     * 查询用户的默认地址
     *
     * @return 返回 AddressBook 对象
     */
    AddressBook listDefault();

    /**
     * 修改用户的地址
     *
     * @param addressBook 需要修改的地址的目标地址信息
     */
    void update(AddressBook addressBook);

    /**
     * 根据 ID 删除地址信息
     *
     * @param id 需要删除的地址的 ID
     */
    void delete(Long id);

    /**
     * 根据 ID 查询地址信息
     *
     * @param id 需要查询的地址的 ID
     * @return 返回 AddressBook 的实体类对象
     */
    AddressBook listById(Long id);

    /**
     * 设置默认地址
     * @param addressBook 用于接收需要设置为默认地址的地址 ID 的实体类对象
     */
    void setDefault(AddressBook addressBook);
}
