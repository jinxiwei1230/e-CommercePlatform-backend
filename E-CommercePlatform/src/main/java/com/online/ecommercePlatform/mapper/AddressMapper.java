package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 地址数据访问接口
 */
@Mapper
public interface AddressMapper {
    /**
     * 根据用户ID查询地址列表
     * @param userId 用户ID
     * @return 地址列表
     */
    List<Address> findByUserId(@Param("userId") Long userId);
    
    /**
     * 查询某用户的地址总数
     * @param userId 用户ID
     * @return 地址总数
     */
    Long countByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID分页查询地址
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 每页条数
     * @return 地址列表
     */
    List<Address> findByUserIdWithPage(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 新增地址
     * @param address 地址对象
     * @return 影响的行数
     */
    int insert(Address address);
    
    /**
     * 将用户的所有地址设置为非默认
     * @param userId 用户ID
     * @return 影响的行数
     */
    int clearDefaultAddress(@Param("userId") Long userId);
    
    /**
     * 根据地址ID查询地址
     * @param addressId 地址ID
     * @return 地址对象
     */
    Address findById(@Param("addressId") Long addressId);
    
    /**
     * 更新地址
     * @param address 地址对象
     * @return 影响的行数
     */
    int update(Address address);
    
    /**
     * 删除地址
     * @param addressId 地址ID
     * @return 影响的行数
     */
    int deleteById(@Param("addressId") Long addressId);
} 