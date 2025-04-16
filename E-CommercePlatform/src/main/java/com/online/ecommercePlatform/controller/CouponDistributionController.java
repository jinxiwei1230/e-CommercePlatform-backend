package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.CouponDistribution;
import com.online.ecommercePlatform.pojo.PageBean;
import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.CouponDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 优惠券发放记录控制器
 */
@RestController
@RequestMapping("/coupon/distribution")
public class CouponDistributionController {

    @Autowired
    private CouponDistributionService distributionService;

    /**
     * 新增优惠券发放记录
     * @param distribution 优惠券发放记录对象
     * @return 创建结果
     */
    @PostMapping("/add")
    public Result<CouponDistribution> add(@RequestBody CouponDistribution distribution) {
        try {
            CouponDistribution createdDistribution = distributionService.createDistribution(distribution);
            return Result.success(createdDistribution);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 批量发放优惠券
     * @param params 参数map，包含couponId、userIds、genderFilter、regionFilter
     * @return 创建结果
     */
    @PostMapping("/batchAdd")
    public Result<Integer> batchAdd(@RequestBody Map<String, Object> params) {
        try {
            Long couponId = Long.valueOf(params.get("couponId").toString());
            @SuppressWarnings("unchecked")
            List<Long> userIds = (List<Long>) params.get("userIds");
            String genderFilter = params.get("genderFilter") != null ? params.get("genderFilter").toString() : null;
            String regionFilter = params.get("regionFilter") != null ? params.get("regionFilter").toString() : null;
            
            int count = distributionService.batchCreateDistributions(couponId, userIds, genderFilter, regionFilter);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改优惠券发放记录信息
     * @param distribution 更新后的优惠券发放记录对象
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<CouponDistribution> update(@RequestBody CouponDistribution distribution) {
        try {
            CouponDistribution updatedDistribution = distributionService.updateDistribution(distribution);
            return Result.success(updatedDistribution);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询优惠券发放记录
     * @param id 优惠券发放记录ID
     * @return 查询结果
     */
    @GetMapping("/selectById/{id}")
    public Result<CouponDistribution> selectById(@PathVariable Long id) {
        try {
            CouponDistribution distribution = distributionService.getDistributionById(id);
            if (distribution != null) {
                return Result.success(distribution);
            } else {
                return Result.error("优惠券发放记录不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据用户ID查询优惠券发放记录
     * @param userId 用户ID
     * @return 查询结果
     */
    @GetMapping("/selectByUserId/{userId}")
    public Result<List<CouponDistribution>> selectByUserId(@PathVariable Long userId) {
        try {
            List<CouponDistribution> distributions = distributionService.getDistributionsByUserId(userId);
            return Result.success(distributions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据优惠券ID查询发放记录
     * @param couponId 优惠券ID
     * @return 查询结果
     */
    @GetMapping("/selectByCouponId/{couponId}")
    public Result<List<CouponDistribution>> selectByCouponId(@PathVariable Long couponId) {
        try {
            List<CouponDistribution> distributions = distributionService.getDistributionsByCouponId(couponId);
            return Result.success(distributions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除优惠券发放记录
     * @param id 优惠券发放记录ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        try {
            boolean result = distributionService.deleteDistribution(id);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("删除失败，优惠券发放记录可能不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除优惠券发放记录
     * @param ids 优惠券发放记录ID列表
     * @return 删除结果
     */
    @DeleteMapping("/delete/batch")
    public Result<Integer> deleteBatch(@RequestBody List<Long> ids) {
        try {
            int count = distributionService.deleteDistributionBatch(ids);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询所有优惠券发放记录
     * @return 优惠券发放记录列表
     */
    @GetMapping("/selectAll")
    public Result<List<CouponDistribution>> selectAll(CouponDistribution distribution) {
        try {
            List<CouponDistribution> distributions = distributionService.getAllDistributions();
            return Result.success(distributions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询优惠券发放记录
     * @param distribution 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @GetMapping("/selectPage")
    public Result<PageBean<CouponDistribution>> selectPage(
            CouponDistribution distribution,
            @RequestParam(defaultValue = "1") int pageNum, 
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageBean<CouponDistribution> pageBean = distributionService.getDistributionsByPage(pageNum, pageSize, distribution);
            return Result.success(pageBean);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 