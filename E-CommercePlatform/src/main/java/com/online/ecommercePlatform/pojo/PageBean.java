package com.online.ecommercePlatform.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//分页返回结果对象
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T>{
    private Long total;//总条数
    @JsonProperty("list") // 序列化为 list
    private List<T> items;//当前页数据集合
}
