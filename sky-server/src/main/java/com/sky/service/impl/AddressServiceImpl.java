package com.sky.service.impl;

import com.sky.context.UserContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressMapper;
import com.sky.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    /**
     * 新增地址信息
     *
     * @param addressBook 需要新增的地址的实体类对象
     */
    @Override
    public void insert(AddressBook addressBook) {
        addressBook.setUserId(UserContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressMapper.insert(addressBook);
    }

    /**
     * 查询用户的所有地址的列表
     *
     * @return 返回封装了 AddressBook 对象的 List 集合
     */
    @Override
    public List<AddressBook> list() {
        Long userId = UserContext.getCurrentId();
        return addressMapper.list(userId, false);
    }

    /**
     * 查询用户的默认地址
     *
     * @return 返回 AddressBook 对象
     */
    @Override
    public AddressBook listDefault() {
        Long userId = UserContext.getCurrentId();
        List<AddressBook> addressBookList = addressMapper.list(userId, true);
        if (addressBookList.isEmpty())
            return null;
        // 由于默认地址只有一个, 因此直接取第一项返回
        return addressBookList.get(0);
    }

    /**
     * 修改用户的地址
     *
     * @param addressBook 需要修改的地址的目标地址信息
     */
    @Override
    public void update(AddressBook addressBook) {
        addressMapper.update(addressBook);
    }

    /**
     * 根据 ID 删除地址信息
     *
     * @param id 需要删除的地址的 ID
     */
    @Override
    public void delete(Long id) {
        addressMapper.delete(id);
    }

    /**
     * 根据 ID 查询地址信息
     *
     * @param id 需要查询的地址的 ID
     * @return 返回 AddressBook 的实体类对象
     */
    @Override
    public AddressBook listById(Long id) {
        return addressMapper.listById(id);
    }

    /**
     * 设置默认地址
     * @param addressBook 用于接收需要设置为默认地址的地址 ID 的实体类对象
     */
    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        // 先重置该用户所有地址的默认值
        Long userId = UserContext.getCurrentId();
        addressMapper.resetDefault(userId);

        // 再根据提供的 ID 设置地址的默认值
        addressMapper.setDefault(addressBook.getId());
    }

}
