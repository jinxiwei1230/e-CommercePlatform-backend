package com.online.ecommercePlatform.mapper;

import com.online.ecommercePlatform.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper {
//    查询用户购物车列表
    @Select("")
    List<Product> cartList();
}
