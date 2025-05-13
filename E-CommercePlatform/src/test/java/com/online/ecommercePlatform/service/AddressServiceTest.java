package com.online.ecommercePlatform.service;

import com.online.ecommercePlatform.dto.PageDTO;
import com.online.ecommercePlatform.mapper.AddressMapper;
import com.online.ecommercePlatform.mapper.OperationLogMapper;
import com.online.ecommercePlatform.mapper.UserMapper;
import com.online.ecommercePlatform.pojo.Address;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.pojo.User;
import com.online.ecommercePlatform.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private OperationLogMapper operationLogMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdminGetAddressList_SuccessCase() {
        // 准备测试数据
        Long userId = 1L;
        Long adminId = 2L;
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(1);
        pageDTO.setPageSize(10);

        User admin = new User();
        admin.setUserId(adminId);
        admin.setRole("管理员");

        User user = new User();
        user.setUserId(userId);
        user.setUsername("testUser");

        List<Address> addresses = Arrays.asList(
            createTestAddress(1L, userId, "收件人1"),
            createTestAddress(2L, userId, "收件人2")
        );

        // 设置模拟行为
        when(userMapper.findById(adminId)).thenReturn(admin);
        when(userMapper.findById(userId)).thenReturn(user);
        when(addressMapper.countByUserId(userId)).thenReturn(2L);
        when(addressMapper.findByUserIdWithPage(eq(userId), anyInt(), anyInt())).thenReturn(addresses);
        doNothing().when(operationLogMapper).insert(any());

        // 执行测试
        Result<PageBean<Address>> result = addressService.adminGetAddressList(userId, adminId, pageDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(Result.SUCCESS, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2L, result.getData().getTotal());
        assertEquals(2, result.getData().getItems().size());
        
        // 验证方法调用
        verify(userMapper, times(1)).findById(adminId);
        verify(userMapper, times(1)).findById(userId);
        verify(addressMapper, times(1)).countByUserId(userId);
        verify(addressMapper, times(1)).findByUserIdWithPage(eq(userId), anyInt(), anyInt());
        verify(operationLogMapper, times(1)).insert(any());
    }

    @Test
    void testAdminGetAddressList_AdminNotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long adminId = 2L;
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(1);
        pageDTO.setPageSize(10);

        // 设置模拟行为：管理员不存在
        when(userMapper.findById(adminId)).thenReturn(null);

        // 执行测试
        Result<PageBean<Address>> result = addressService.adminGetAddressList(userId, adminId, pageDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(Result.UNAUTHORIZED, result.getCode());
        assertEquals("无管理员权限", result.getMessage());
        
        // 验证方法调用
        verify(userMapper, times(1)).findById(adminId);
        verify(userMapper, never()).findById(userId);
        verify(addressMapper, never()).countByUserId(anyLong());
        verify(addressMapper, never()).findByUserIdWithPage(anyLong(), anyInt(), anyInt());
        verify(operationLogMapper, never()).insert(any());
    }

    @Test
    void testAdminGetAddressList_NotAdmin() {
        // 准备测试数据
        Long userId = 1L;
        Long adminId = 2L;
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(1);
        pageDTO.setPageSize(10);

        User notAdmin = new User();
        notAdmin.setUserId(adminId);
        notAdmin.setRole("普通用户"); // 非管理员角色

        // 设置模拟行为：用户存在但不是管理员
        when(userMapper.findById(adminId)).thenReturn(notAdmin);

        // 执行测试
        Result<PageBean<Address>> result = addressService.adminGetAddressList(userId, adminId, pageDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(Result.UNAUTHORIZED, result.getCode());
        assertEquals("无管理员权限", result.getMessage());
        
        // 验证方法调用
        verify(userMapper, times(1)).findById(adminId);
        verify(userMapper, never()).findById(userId);
        verify(addressMapper, never()).countByUserId(anyLong());
        verify(addressMapper, never()).findByUserIdWithPage(anyLong(), anyInt(), anyInt());
        verify(operationLogMapper, never()).insert(any());
    }

    @Test
    void testAdminGetAddressList_UserNotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long adminId = 2L;
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(1);
        pageDTO.setPageSize(10);

        User admin = new User();
        admin.setUserId(adminId);
        admin.setRole("管理员");

        // 设置模拟行为：管理员存在，但目标用户不存在
        when(userMapper.findById(adminId)).thenReturn(admin);
        when(userMapper.findById(userId)).thenReturn(null);

        // 执行测试
        Result<PageBean<Address>> result = addressService.adminGetAddressList(userId, adminId, pageDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(Result.NOT_FOUND, result.getCode());
        assertEquals("用户不存在", result.getMessage());
        
        // 验证方法调用
        verify(userMapper, times(1)).findById(adminId);
        verify(userMapper, times(1)).findById(userId);
        verify(addressMapper, never()).countByUserId(anyLong());
        verify(addressMapper, never()).findByUserIdWithPage(anyLong(), anyInt(), anyInt());
        verify(operationLogMapper, never()).insert(any());
    }

    @Test
    void testAdminGetAddressList_NoAddresses() {
        // 准备测试数据
        Long userId = 1L;
        Long adminId = 2L;
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(1);
        pageDTO.setPageSize(10);

        User admin = new User();
        admin.setUserId(adminId);
        admin.setRole("管理员");

        User user = new User();
        user.setUserId(userId);
        user.setUsername("testUser");

        // 设置模拟行为：用户存在但没有地址
        when(userMapper.findById(adminId)).thenReturn(admin);
        when(userMapper.findById(userId)).thenReturn(user);
        when(addressMapper.countByUserId(userId)).thenReturn(0L);
        doNothing().when(operationLogMapper).insert(any());

        // 执行测试
        Result<PageBean<Address>> result = addressService.adminGetAddressList(userId, adminId, pageDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(Result.SUCCESS, result.getCode());
        assertNotNull(result.getData());
        assertEquals(0L, result.getData().getTotal());
        assertEquals(0, result.getData().getItems().size());
        
        // 验证方法调用
        verify(userMapper, times(1)).findById(adminId);
        verify(userMapper, times(1)).findById(userId);
        verify(addressMapper, times(1)).countByUserId(userId);
        verify(addressMapper, never()).findByUserIdWithPage(anyLong(), anyInt(), anyInt());
        verify(operationLogMapper, times(1)).insert(any());
    }

    // 辅助方法：创建测试用的地址对象
    private Address createTestAddress(Long addressId, Long userId, String recipientName) {
        Address address = new Address();
        address.setAddressId(addressId);
        address.setUserId(userId);
        address.setRecipientName(recipientName);
        address.setPhone("13800138000");
        address.setAddressDetail("测试地址详情");
        address.setCity("测试城市");
        address.setState("测试省份");
        address.setPostalCode("100000");
        address.setIsDefault(false);
        return address;
    }
} 