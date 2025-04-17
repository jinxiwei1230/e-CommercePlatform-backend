-- 创建用户表
CREATE TABLE IF NOT EXISTS `User` (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- 用户唯一标识
    username VARCHAR(50) UNIQUE NOT NULL,  -- 用户名（需唯一）
    password VARCHAR(255) NOT NULL,  -- 加密存储的用户密码
    email VARCHAR(100) UNIQUE,  -- 绑定的邮箱（支持解绑）
    phone VARCHAR(20) UNIQUE,  -- 绑定的手机号（支持解绑）
    gender ENUM('男', '女', '保密'),  -- 性别
    address VARCHAR(255),  -- 地址
    age INT,  -- 年龄
    is_vip BOOLEAN DEFAULT FALSE,  -- 是否为 VIP 用户
    role ENUM('普通用户', '管理员') DEFAULT '普通用户',  -- 用户角色，默认是普通用户
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 注册时间
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- 信息更新时间
); 