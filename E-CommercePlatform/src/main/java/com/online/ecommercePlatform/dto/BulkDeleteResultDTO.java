package com.online.ecommercePlatform.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO for representing the result of a bulk delete operation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkDeleteResultDTO {
    private int deletedCount;
    private List<Long> failedIds;
    // Można dodać więcej informacji, np. List<String> errorMessages;
} 