package com.online.ecommercePlatform.service.impl;

import com.online.ecommercePlatform.dto.AddressAddDTO;
import com.online.ecommercePlatform.dto.AddressUpdateDTO;
import com.online.ecommercePlatform.dto.PageDTO;
import com.online.ecommercePlatform.mapper.AddressMapper;
import com.online.ecommercePlatform.mapper.OperationLogMapper;
import com.online.ecommercePlatform.mapper.UserMapper;
import com.online.ecommercePlatform.pojo.Address;
import com.online.ecommercePlatform.pojo.OperationLog;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地址服务实现类
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    
    @Autowired
    private OperationLogMapper operationLogMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<PageBean<Address>> getAddressList(Long userId, PageDTO pageDTO) {
        // 计算分页参数
        Integer page = pageDTO.getPage();
        Integer pageSize = pageDTO.getPageSize();
        Integer offset = (page - 1) * pageSize;
        
        // 查询总记录数
        Long total = addressMapper.countByUserId(userId);
        
        // 如果没有记录，直接返回空结果
        if (total == 0) {
            PageBean<Address> emptyPage = new PageBean<>(0L, List.of());
            return Result.success(emptyPage);
        }
        
        // 分页查询数据
        List<Address> addresses = addressMapper.findByUserIdWithPage(userId, offset, pageSize);
        
        // 封装结果
        PageBean<Address> pageBean = new PageBean<>(total, addresses);
        return Result.success(pageBean);
    }

    @Override
    public Result<List<Address>> getAllAddressByUserId(Long userId) {
        List<Address> addresses = addressMapper.findByUserId(userId);
        return Result.success(addresses);
    }
    
    @Override
    @Transactional
    public Result<Long> addAddress(Long userId, AddressAddDTO addressAddDTO) {
        try {
            // 将DTO转换为实体对象
            Address address = new Address();
            address.setUserId(userId);
            address.setRecipientName(addressAddDTO.getRecipient_name());
            address.setPhone(addressAddDTO.getPhone());
            address.setAddressDetail(addressAddDTO.getAddress_line1());
            address.setCity(addressAddDTO.getCity());
            address.setState(addressAddDTO.getState());
            address.setPostalCode(addressAddDTO.getPostal_code());
            address.setIsDefault(addressAddDTO.getIs_default());
            
            // 如果设置为默认地址，先清除该用户的其他默认地址
            if (Boolean.TRUE.equals(address.getIsDefault())) {
                addressMapper.clearDefaultAddress(userId);
            }
            
            // 执行插入操作
            addressMapper.insert(address);
            
            // 记录操作日志
            OperationLog log = new OperationLog();
            log.setUserId(userId);
            log.setAction("添加地址");
            log.setTargetTable("Address");
            log.setTargetId(address.getAddressId());
            log.setDescription("添加了新地址: " + address.getRecipientName() + ", " + address.getAddressDetail());
            log.setResult("成功");
            operationLogMapper.insert(log);
            
            // 返回新增地址的ID
            return Result.success(address.getAddressId());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.BAD_REQUEST, "添加地址失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public Result<?> updateAddress(Long userId, AddressUpdateDTO addressUpdateDTO) {
        try {
            // 查询要更新的地址是否存在
            Address existingAddress = addressMapper.findById(addressUpdateDTO.getAddress_id());
            if (existingAddress == null) {
                return Result.error(Result.NOT_FOUND, "地址不存在");
            }
            
            // 确认该地址属于当前用户
            if (!existingAddress.getUserId().equals(userId)) {
                return Result.error(Result.UNAUTHORIZED, "无权修改此地址");
            }
            
            // 将DTO转换为实体对象
            Address address = new Address();
            address.setAddressId(addressUpdateDTO.getAddress_id());
            address.setUserId(userId);
            address.setRecipientName(addressUpdateDTO.getRecipient_name());
            address.setPhone(addressUpdateDTO.getPhone());
            address.setAddressDetail(addressUpdateDTO.getAddress_line1());
            address.setCity(addressUpdateDTO.getCity());
            address.setState(addressUpdateDTO.getState());
            address.setPostalCode(addressUpdateDTO.getPostal_code());
            address.setIsDefault(addressUpdateDTO.getIs_default());
            
            // 如果设置为默认地址，先清除该用户的其他默认地址
            if (Boolean.TRUE.equals(address.getIsDefault())) {
                addressMapper.clearDefaultAddress(userId);
            }
            
            // 执行更新操作
            addressMapper.update(address);
            
            // 记录操作日志
            OperationLog log = new OperationLog();
            log.setUserId(userId);
            log.setAction("更新地址");
            log.setTargetTable("Address");
            log.setTargetId(address.getAddressId());
            log.setDescription("更新了地址信息: " + address.getRecipientName() + ", " + address.getAddressDetail());
            log.setResult("成功");
            operationLogMapper.insert(log);
            
            return Result.success("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.BAD_REQUEST, "更新地址失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public Result<?> deleteAddress(Long userId, Long addressId) {
        try {
            // 查询要删除的地址是否存在
            Address existingAddress = addressMapper.findById(addressId);
            if (existingAddress == null) {
                return Result.error(Result.NOT_FOUND, "地址不存在");
            }
            
            // 确认该地址属于当前用户
            if (!existingAddress.getUserId().equals(userId)) {
                return Result.error(Result.UNAUTHORIZED, "无权删除此地址");
            }
            
            // 判断是否为默认地址
            boolean isDefault = Boolean.TRUE.equals(existingAddress.getIsDefault());
            
            // 执行删除操作
            addressMapper.deleteById(addressId);
            
            // 如果删除的是默认地址，则自动设置一个新的默认地址
            if (isDefault) {
                // 查询用户的所有地址
                List<Address> remainingAddresses = addressMapper.findByUserId(userId);
                if (!remainingAddresses.isEmpty()) {
                    // 选择第一个地址设为默认地址
                    Address newDefaultAddress = remainingAddresses.get(0);
                    newDefaultAddress.setIsDefault(true);
                    addressMapper.update(newDefaultAddress);
                    
                    // 记录设置新默认地址的操作日志
                    OperationLog defaultLog = new OperationLog();
                    defaultLog.setUserId(userId);
                    defaultLog.setAction("设置默认地址");
                    defaultLog.setTargetTable("Address");
                    defaultLog.setTargetId(newDefaultAddress.getAddressId());
                    defaultLog.setDescription("系统自动设置新默认地址: " + newDefaultAddress.getRecipientName() + ", " + newDefaultAddress.getAddressDetail());
                    defaultLog.setResult("成功");
                    operationLogMapper.insert(defaultLog);
                }
            }
            
            // 记录删除地址的操作日志
            OperationLog log = new OperationLog();
            log.setUserId(userId);
            log.setAction("删除地址");
            log.setTargetTable("Address");
            log.setTargetId(addressId);
            log.setDescription("删除了地址: " + existingAddress.getRecipientName() + ", " + existingAddress.getAddressDetail());
            log.setResult("成功");
            operationLogMapper.insert(log);
            
            return Result.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.BAD_REQUEST, "删除地址失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public Result<?> setDefaultAddress(Long userId, Long addressId) {
        try {
            // 查询要设置为默认的地址是否存在
            Address existingAddress = addressMapper.findById(addressId);
            if (existingAddress == null) {
                return Result.error(Result.NOT_FOUND, "地址不存在");
            }
            
            // 确认该地址属于当前用户
            if (!existingAddress.getUserId().equals(userId)) {
                return Result.error(Result.UNAUTHORIZED, "无权设置此地址为默认地址");
            }
            
            // 如果该地址已经是默认地址，直接返回成功
            if (Boolean.TRUE.equals(existingAddress.getIsDefault())) {
                return Result.success("设置成功");
            }
            
            // 先清除该用户的所有默认地址
            addressMapper.clearDefaultAddress(userId);
            
            // 将指定地址设置为默认
            Address address = new Address();
            address.setAddressId(addressId);
            address.setUserId(userId);
            address.setRecipientName(existingAddress.getRecipientName());
            address.setPhone(existingAddress.getPhone());
            address.setAddressDetail(existingAddress.getAddressDetail());
            address.setCity(existingAddress.getCity());
            address.setState(existingAddress.getState());
            address.setPostalCode(existingAddress.getPostalCode());
            address.setIsDefault(true);
            
            // 执行更新操作
            addressMapper.update(address);
            
            // 记录操作日志
            OperationLog log = new OperationLog();
            log.setUserId(userId);
            log.setAction("设置默认地址");
            log.setTargetTable("Address");
            log.setTargetId(addressId);
            log.setDescription("设置默认地址: " + existingAddress.getRecipientName() + ", " + existingAddress.getAddressDetail());
            log.setResult("成功");
            operationLogMapper.insert(log);
            
            return Result.success("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.BAD_REQUEST, "设置默认地址失败: " + e.getMessage());
        }
    }

    @Override
    public Result<PageBean<Address>> adminGetAddressList(Long userId, Long adminId, PageDTO pageDTO) {
        // 验证管理员权限
        User admin = userMapper.findById(adminId);
        if (admin == null || !"管理员".equals(admin.getRole())) {
            return Result.error(Result.UNAUTHORIZED, "无管理员权限");
        }
        
        // 验证目标用户是否存在
        User targetUser = userMapper.findById(userId);
        if (targetUser == null) {
            return Result.error(Result.NOT_FOUND, "用户不存在");
        }
        
        // 计算分页参数
        Integer page = pageDTO.getPage();
        Integer pageSize = pageDTO.getPageSize();
        Integer offset = (page - 1) * pageSize;
        
        // 查询总记录数
        Long total = addressMapper.countByUserId(userId);
        
        // 如果没有记录，直接返回空结果
        if (total == 0) {
            PageBean<Address> emptyPage = new PageBean<>(0L, List.of());
            return Result.success(emptyPage);
        }
        
        // 分页查询数据
        List<Address> addresses = addressMapper.findByUserIdWithPage(userId, offset, pageSize);
        
        // 记录操作日志
        OperationLog log = new OperationLog();
        log.setUserId(adminId);
        log.setAction("管理员查询用户地址");
        log.setTargetTable("Address");
        log.setTargetId(userId);
        log.setDescription("管理员查询了用户ID: " + userId + " 的地址列表");
        log.setResult("成功");
        operationLogMapper.insert(log);
        
        // 封装结果
        PageBean<Address> pageBean = new PageBean<>(total, addresses);
        return Result.success(pageBean);
    }
} 