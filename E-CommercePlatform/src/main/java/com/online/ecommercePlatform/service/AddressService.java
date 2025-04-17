package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.AddressAddDTO;
import com.online.ecommercePlatform.dto.AddressUpdateDTO;
import com.online.ecommercePlatform.dto.PageDTO;
import com.online.ecommercePlatform.pojo.Address;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Result;

import java.util.List;

/**
 * 地址服务接口
 */
public interface AddressService {
    
    /**
     * 获取用户地址列表（分页）
     * @param userId 用户ID
     * @param pageDTO 分页参数
     * @return 分页结果
     */
    Result<PageBean<Address>> getAddressList(Long userId, PageDTO pageDTO);
    
    /**
     * 获取用户的所有地址
     * @param userId 用户ID
     * @return 地址列表
     */
    Result<List<Address>> getAllAddressByUserId(Long userId);
    
    /**
     * 添加新地址
     * @param userId 用户ID
     * @param addressAddDTO 地址信息
     * @return 添加结果，包含新地址ID
     */
    Result<Long> addAddress(Long userId, AddressAddDTO addressAddDTO);
    
    /**
     * 更新地址
     * @param userId 当前用户ID
     * @param addressUpdateDTO 要更新的地址信息
     * @return 更新结果
     */
    Result<?> updateAddress(Long userId, AddressUpdateDTO addressUpdateDTO);
    
    /**
     * 删除地址
     * @param userId 当前用户ID
     * @param addressId 要删除的地址ID
     * @return 删除结果
     */
    Result<?> deleteAddress(Long userId, Long addressId);
    
    /**
     * 设置默认地址
     * @param userId 当前用户ID
     * @param addressId 要设置为默认的地址ID
     * @return 设置结果
     */
    Result<?> setDefaultAddress(Long userId, Long addressId);
} 