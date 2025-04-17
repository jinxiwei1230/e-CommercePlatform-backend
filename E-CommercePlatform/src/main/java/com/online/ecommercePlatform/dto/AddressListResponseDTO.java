package com.online.ecommercePlatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.online.ecommercePlatform.pojo.Address;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址列表分页响应DTO
 */
@Data
public class AddressListResponseDTO {
    private Long total;    // 总记录数
    private Integer pages;    // 总页数
    private Integer current;  // 当前页码
    private Integer size;     // 每页数量
    private List<AddressRecordDTO> records;  // 当前页的记录
    
    /**
     * 默认构造函数
     */
    public AddressListResponseDTO() {
    }
    
    /**
     * 完整构造函数
     */
    public AddressListResponseDTO(Long total, Integer pages, Integer current, Integer size, List<AddressRecordDTO> records) {
        this.total = total;
        this.pages = pages;
        this.current = current;
        this.size = size;
        this.records = records;
    }
    
    /**
     * 从PageBean构建响应DTO
     */
    public static AddressListResponseDTO fromPageBean(Long total, List<Address> items, Integer pageSize, Integer currentPage) {
        // 计算总页数
        int pages = (int) Math.ceil((double) total / pageSize);
        
        // 转换地址记录为前端需要的格式
        List<AddressRecordDTO> recordDTOs = new ArrayList<>();
        if (items != null) {
            for (Address address : items) {
                recordDTOs.add(AddressRecordDTO.fromAddress(address));
            }
        }
        
        return new AddressListResponseDTO(
                total,
                pages,
                currentPage,
                pageSize,
                recordDTOs
        );
    }
    
    /**
     * 地址记录DTO - 用于转换为前端需要的属性名格式
     */
    @Data
    public static class AddressRecordDTO {
        @JsonProperty("address_id")
        private Long addressId;
        
        @JsonProperty("user_id")
        private Long userId;
        
        @JsonProperty("recipient_name")
        private String recipientName;
        
        private String phone;
        
        @JsonProperty("address_line1")
        private String addressDetail;
        
        private String city;
        
        private String state;
        
        @JsonProperty("postal_code")
        private String postalCode;
        
        @JsonProperty("is_default")
        private Boolean isDefault;
        
        @JsonProperty("create_time")
        private String createTime;
        
        @JsonProperty("update_time")
        private String updateTime;
        
        /**
         * 从Address实体转换为DTO
         */
        public static AddressRecordDTO fromAddress(Address address) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            AddressRecordDTO dto = new AddressRecordDTO();
            dto.setAddressId(address.getAddressId());
            dto.setUserId(address.getUserId());
            dto.setRecipientName(address.getRecipientName());
            dto.setPhone(address.getPhone());
            dto.setAddressDetail(address.getAddressDetail());
            dto.setCity(address.getCity());
            dto.setState(address.getState());
            dto.setPostalCode(address.getPostalCode());
            dto.setIsDefault(address.getIsDefault());
            
            // 格式化时间
            if (address.getCreateTime() != null) {
                dto.setCreateTime(address.getCreateTime().format(formatter));
            }
            
            if (address.getUpdateTime() != null) {
                dto.setUpdateTime(address.getUpdateTime().format(formatter));
            } else if (address.getCreateTime() != null) {
                // 如果更新时间为空，使用创建时间
                dto.setUpdateTime(address.getCreateTime().format(formatter));
            }
            
            return dto;
        }
    }
} 