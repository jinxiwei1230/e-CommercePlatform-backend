package com.online.ecommercePlatform.controller;

import com.online.ecommercePlatform.pojo.Result;
import com.online.ecommercePlatform.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.online.ecommercePlatform.dto.CartDTO;
import com.online.ecommercePlatform.pojo.Cart;
import com.online.ecommercePlatform.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取当前用户的购物车列表
     * @param request HTTP请求对象，用于获取Authorization头
     * @return 包含购物车列表的Result对象
     */
    @GetMapping("/list")
    public Result<List<Cart>> cartList(HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error(Result.UNAUTHORIZED);
            }
            List<Cart> cartItems = cartService.selectByUserId(userId);
            return Result.success(cartItems);
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error(Result.UNAUTHORIZED);
        }

    }

    /**
     * 添加商品到购物车
     * @param cart 包含商品ID和数量的购物车对象
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<Cart> addToCart(@RequestBody Cart cart, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED);
        }
        Cart updatedCart = cartService.addToCart(userId, cart.getProductId(), cart.getQuantity());
        return Result.success(updatedCart);
    }

    /**
     * 更新购物车中指定商品的数量
     * @param cartDTO 新的商品数量
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/update")
    public Result<Cart> updateCartItem(@RequestBody CartDTO cartDTO,
                                       HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED);
        }
        Cart updatedCart = cartService.updateCartItem(userId, cartDTO.getProductId(), cartDTO.getQuantity());
        return Result.success(updatedCart);
    }

    /**
     * 从购物车中移除指定商品项
     * @param cartId 要移除的购物车项ID
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @DeleteMapping("/remove/{cartId}")
    public Result<Void> removeFromCart(@PathVariable Long cartId,
                                       HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED);
        }
        cartService.removeFromCart(userId, cartId);
        return Result.success();
    }

    /**
     * 清空当前用户的购物车
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @DeleteMapping("/clear")
    public Result<Void> clearCart(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error(Result.UNAUTHORIZED);
        }
        cartService.clearCart(userId);
        return Result.success();
    }

//    /**
//     * 购物车结算
//     * @param request HTTP请求对象
//     * @return 结算信息
//     */
//    @PostMapping("/checkout")
//    public Result<CartCheckoutDTO> checkout(HttpServletRequest request) {
//        Long userId = getUserIdFromRequest(request);
//        if (userId == null) {
//            return Result.error(Result.UNAUTHORIZED);
//        }
//        try {
//            CartCheckoutDTO checkoutDTO = cartService.checkout(userId);
//            return Result.success(checkoutDTO);
//        } catch (RuntimeException e) {
//            return Result.error(e.getMessage());
//        }
//    }


    /**
     * 从请求头中解析并验证token，获取用户ID
     * @param request HTTP请求对象
     * @return 用户ID，如果验证失败则返回null
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            DecodedJWT jwt = jwtUtil.verifyToken(token);
            if (jwt == null || jwtUtil.isTokenExpired(token)) {
                return null;
            }

            String userIdStr = jwt.getClaim("userId").asString();
            return StringUtils.hasText(userIdStr) ? Long.valueOf(userIdStr) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
