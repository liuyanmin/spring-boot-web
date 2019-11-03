SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `web_admin`
-- ----------------------------
DROP TABLE IF EXISTS `web_admin`;
CREATE TABLE `web_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员用户ID',
  `username` varchar(255) DEFAULT NULL COMMENT '登录用户名',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '登录密码',
  `fullname` varchar(30) NOT NULL DEFAULT '' COMMENT '姓名',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱',
  `gender` tinyint(3) NOT NULL DEFAULT '0' COMMENT '性别：0 未知， 1男， 2 女',
  `age` tinyint(3) DEFAULT NULL COMMENT '年龄',
  `mobile` varchar(30) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像图片url',
  `last_login_ip` varchar(63) DEFAULT NULL COMMENT '最近一次登录IP地址',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近一次登录时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态，0-禁用 1-启用',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `super_admin` tinyint(1) DEFAULT '0' COMMENT '是否超级管理员，1-是 0-否',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0-未删除 1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of web_admin
-- ----------------------------
INSERT INTO `web_admin` VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', '', '0', null, null, null, null, null, '1', '1', '1', '2019-10-21 16:51:01', '2019-10-21 16:51:04', '0');

-- ----------------------------
-- Table structure for `web_log`
-- ----------------------------
DROP TABLE IF EXISTS `web_log`;
CREATE TABLE `web_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `ip` varchar(30) DEFAULT NULL COMMENT '请求IP',
  `url` varchar(256) DEFAULT NULL COMMENT '请求url',
  `method` varchar(30) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(1024) DEFAULT NULL COMMENT '请求参数',
  `times` int(11) DEFAULT NULL COMMENT '耗时，单位：毫秒',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态，1-成功 0-失败',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除，1-已删除 0-未删除',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请求日志表';

-- ----------------------------
-- Records of web_log
-- ----------------------------

-- ----------------------------
-- Table structure for `web_menu`
-- ----------------------------
DROP TABLE IF EXISTS `web_menu`;
CREATE TABLE `web_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(255) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `parent_id` int(11) DEFAULT '0' COMMENT '父菜单ID',
  `sort_by` tinyint(3) DEFAULT '0' COMMENT '菜单顺序',
  `front_url` varchar(255) DEFAULT NULL COMMENT '前台路径',
  `icon` varchar(255) DEFAULT NULL COMMENT 'icon路径',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态，0-禁用 1-启用',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0-未删除 1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of web_menu
-- ----------------------------
INSERT INTO `web_menu` VALUES ('1', '系统管理', '0', '1', null, null, '1', '2019-10-21 16:54:34', '2019-10-21 16:54:36', '0');
INSERT INTO `web_menu` VALUES ('2', '权限管理', '1', '1', null, null, '1', '2019-10-21 16:54:52', '2019-10-21 16:54:54', '0');
INSERT INTO `web_menu` VALUES ('3', '菜单管理', '1', '2', null, null, '1', '2019-10-21 16:55:11', '2019-10-21 16:55:14', '0');
INSERT INTO `web_menu` VALUES ('4', '角色管理', '1', '3', null, null, '1', '2019-10-21 16:55:37', '2019-10-21 16:55:39', '0');
INSERT INTO `web_menu` VALUES ('5', '日志管理', '1', '4', null, null, '1', '2019-10-21 16:56:04', '2019-10-21 16:56:06', '0');
INSERT INTO `web_menu` VALUES ('6', '用户管理', '0', '2', null, null, '1', '2019-10-21 16:56:25', '2019-10-21 16:56:27', '0');
INSERT INTO `web_menu` VALUES ('7', '用户管理', '6', '1', null, null, '1', '2019-10-21 16:57:09', '2019-10-21 16:57:12', '0');
INSERT INTO `web_menu` VALUES ('8', '修改密码', '6', '2', null, null, '1', '2019-10-21 16:58:13', '2019-10-21 16:58:16', '0');
INSERT INTO `web_menu` VALUES ('9', '修改邮箱', '6', '3', null, null, '1', '2019-10-21 16:58:51', '2019-10-21 16:58:54', '0');
INSERT INTO `web_menu` VALUES ('10', '个人信息', '6', '4', null, null, '1', '2019-10-21 17:14:27', '2019-10-21 17:14:29', '0');
INSERT INTO `web_menu` VALUES ('11', '参数管理', '1', '5', null, null, '1', '2019-10-22 18:07:55', '2019-10-22 18:07:58', '0');

-- ----------------------------
-- Table structure for `web_menu_permission_re`
-- ----------------------------
DROP TABLE IF EXISTS `web_menu_permission_re`;
CREATE TABLE `web_menu_permission_re` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单ID',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限ID',
  `api_uri` varchar(255) DEFAULT NULL COMMENT '后台接口uri',
  `front_url` varchar(255) DEFAULT NULL COMMENT '前台页面url',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限关系表';

-- ----------------------------
-- Records of web_menu_permission_re
-- ----------------------------
INSERT INTO `web_menu_permission_re` VALUES ('1', '2', '1', '/permission/list', null, '2019-10-21 17:04:03', '2019-10-21 17:04:06');
INSERT INTO `web_menu_permission_re` VALUES ('2', '2', '2', '/permission/detail', null, '2019-10-21 17:04:44', '2019-10-21 17:04:47');
INSERT INTO `web_menu_permission_re` VALUES ('3', '2', '3', '/permission/save', null, '2019-10-21 17:05:04', '2019-10-21 17:05:06');
INSERT INTO `web_menu_permission_re` VALUES ('4', '2', '4', '/permission/update', null, '2019-10-21 17:05:24', '2019-10-21 17:05:27');
INSERT INTO `web_menu_permission_re` VALUES ('5', '3', '1', '/menu/list', null, '2019-10-21 17:05:57', '2019-10-21 17:05:59');
INSERT INTO `web_menu_permission_re` VALUES ('6', '3', '2', '/menu/detail', null, '2019-10-21 17:06:15', '2019-10-21 17:06:18');
INSERT INTO `web_menu_permission_re` VALUES ('7', '3', '3', '/menu/save', null, '2019-10-21 17:06:37', '2019-10-21 17:06:40');
INSERT INTO `web_menu_permission_re` VALUES ('8', '3', '4', '/menu/update', null, '2019-10-21 17:06:59', '2019-10-21 17:07:01');
INSERT INTO `web_menu_permission_re` VALUES ('9', '3', '5', '/menu/new', null, '2019-10-21 17:07:25', '2019-10-21 17:07:28');
INSERT INTO `web_menu_permission_re` VALUES ('10', '4', '1', '/role/list', null, '2019-10-21 17:07:49', '2019-10-21 17:07:52');
INSERT INTO `web_menu_permission_re` VALUES ('11', '4', '2', '/role/detail', null, '2019-10-21 17:08:11', '2019-10-21 17:08:13');
INSERT INTO `web_menu_permission_re` VALUES ('12', '4', '3', '/role/save', null, '2019-10-21 17:08:28', '2019-10-21 17:08:31');
INSERT INTO `web_menu_permission_re` VALUES ('13', '4', '4', '/role/update', null, '2019-10-21 17:08:46', '2019-10-21 17:08:51');
INSERT INTO `web_menu_permission_re` VALUES ('14', '4', '5', '/role/new', null, '2019-10-21 17:09:13', '2019-10-21 17:09:15');
INSERT INTO `web_menu_permission_re` VALUES ('15', '5', '1', '/log/list', null, '2019-10-21 17:09:34', '2019-10-21 17:09:38');
INSERT INTO `web_menu_permission_re` VALUES ('16', '7', '1', '/admin/list', null, '2019-10-21 17:10:09', '2019-10-21 17:10:12');
INSERT INTO `web_menu_permission_re` VALUES ('17', '7', '2', '/admin/detail', null, '2019-10-21 17:10:30', '2019-10-21 17:10:33');
INSERT INTO `web_menu_permission_re` VALUES ('18', '7', '3', '/admin/save', null, '2019-10-21 17:10:52', '2019-10-21 17:10:55');
INSERT INTO `web_menu_permission_re` VALUES ('19', '7', '4', '/admin/update', null, '2019-10-21 17:11:13', '2019-10-21 17:11:16');
INSERT INTO `web_menu_permission_re` VALUES ('20', '7', '5', '/admin/new', null, '2019-10-21 17:11:40', '2019-10-21 17:11:43');
INSERT INTO `web_menu_permission_re` VALUES ('21', '8', '6', '/admin/update/password', null, '2019-10-21 17:12:32', '2019-10-21 17:12:34');
INSERT INTO `web_menu_permission_re` VALUES ('22', '9', '7', '/admin/update/mail', null, '2019-10-21 17:13:28', '2019-10-21 17:13:30');
INSERT INTO `web_menu_permission_re` VALUES ('23', '10', '2', '/admin/detail', null, '2019-10-21 17:15:00', '2019-10-21 17:15:02');
INSERT INTO `web_menu_permission_re` VALUES ('24', '10', '4', '/admin/update', null, '2019-10-21 17:21:39', '2019-10-21 17:21:41');
INSERT INTO `web_menu_permission_re` VALUES ('25', '11', '1', '/system/list', null, '2019-10-22 18:09:38', '2019-10-22 18:09:41');
INSERT INTO `web_menu_permission_re` VALUES ('26', '11', '2', '/system/detail', null, '2019-10-22 18:10:04', '2019-10-22 18:10:06');
INSERT INTO `web_menu_permission_re` VALUES ('27', '11', '3', '/system/save', null, '2019-10-22 18:10:24', '2019-10-22 18:10:26');
INSERT INTO `web_menu_permission_re` VALUES ('28', '11', '4', '/system/update', null, '2019-10-22 18:10:46', '2019-10-22 18:10:48');

-- ----------------------------
-- Table structure for `web_permission`
-- ----------------------------
DROP TABLE IF EXISTS `web_permission`;
CREATE TABLE `web_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(30) DEFAULT NULL COMMENT '权限名称',
  `description` varchar(1023) DEFAULT NULL COMMENT '权限描述',
  `status` tinyint(1) DEFAULT '1' COMMENT '启用状态，0-禁用 1-启用',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '启用状态，1-启用 0-禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ----------------------------
-- Records of web_permission
-- ----------------------------
INSERT INTO `web_permission` VALUES ('1', '列表', '列表查询', '1', '2019-10-21 16:52:23', '2019-10-21 16:52:25', '0');
INSERT INTO `web_permission` VALUES ('2', '详情', '查看详情', '1', '2019-10-21 16:52:43', '2019-10-21 16:52:45', '0');
INSERT INTO `web_permission` VALUES ('3', '保存', '新增保存', '1', '2019-10-21 16:53:06', '2019-10-21 16:53:08', '0');
INSERT INTO `web_permission` VALUES ('4', '修改', '修改保存', '1', '2019-10-21 16:53:23', '2019-10-21 16:53:25', '0');
INSERT INTO `web_permission` VALUES ('5', '新建查询', '新建查询', '1', '2019-10-21 17:00:04', '2019-10-21 17:00:07', '0');
INSERT INTO `web_permission` VALUES ('6', '修改密码', '修改密码', '1', '2019-10-21 17:00:57', '2019-10-21 17:01:00', '0');
INSERT INTO `web_permission` VALUES ('7', '修改邮箱', '修改邮箱', '1', '2019-10-21 17:01:16', '2019-10-21 17:01:18', '0');

-- ----------------------------
-- Table structure for `web_role`
-- ----------------------------
DROP TABLE IF EXISTS `web_role`;
CREATE TABLE `web_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(63) NOT NULL DEFAULT '' COMMENT '角色名称',
  `description` varchar(1023) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '启用状态，0-禁用 1-启用',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0-未删除 1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of web_role
-- ----------------------------
INSERT INTO `web_role` VALUES ('1', '超级管理员', '拥有所有权限', '1', '2019-10-21 16:51:30', '2019-10-21 16:51:32', '0');
INSERT INTO `web_role` VALUES ('2', '管理员', null, '1', '2019-10-21 17:02:42', '2019-10-21 17:02:44', '0');

-- ----------------------------
-- Table structure for `web_role_menu_re`
-- ----------------------------
DROP TABLE IF EXISTS `web_role_menu_re`;
CREATE TABLE `web_role_menu_re` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单ID',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限ID',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单配置表';

-- ----------------------------
-- Records of web_role_menu_re
-- ----------------------------
INSERT INTO `web_role_menu_re` VALUES ('1', '2', '2', '1', '2019-10-21 17:24:23', '2019-10-21 17:24:25');
INSERT INTO `web_role_menu_re` VALUES ('2', '2', '2', '2', '2019-10-21 17:24:39', '2019-10-21 17:24:41');
INSERT INTO `web_role_menu_re` VALUES ('3', '2', '2', '3', '2019-10-21 17:24:57', '2019-10-21 17:24:59');
INSERT INTO `web_role_menu_re` VALUES ('4', '2', '2', '4', '2019-10-21 17:25:12', '2019-10-21 17:25:14');
INSERT INTO `web_role_menu_re` VALUES ('5', '2', '3', '1', '2019-10-21 17:25:28', '2019-10-21 17:25:30');
INSERT INTO `web_role_menu_re` VALUES ('6', '2', '3', '2', '2019-10-21 17:27:27', '2019-10-21 17:27:29');
INSERT INTO `web_role_menu_re` VALUES ('7', '2', '3', '3', '2019-10-21 17:27:37', '2019-10-21 17:27:40');
INSERT INTO `web_role_menu_re` VALUES ('8', '2', '3', '4', '2019-10-21 17:27:51', '2019-10-21 17:27:53');
INSERT INTO `web_role_menu_re` VALUES ('9', '2', '3', '5', '2019-10-21 17:28:03', '2019-10-21 17:28:05');
INSERT INTO `web_role_menu_re` VALUES ('10', '2', '4', '1', '2019-10-21 17:28:26', '2019-10-21 17:28:28');
INSERT INTO `web_role_menu_re` VALUES ('11', '2', '4', '2', '2019-10-21 17:28:36', '2019-10-21 17:28:39');
INSERT INTO `web_role_menu_re` VALUES ('12', '2', '4', '3', '2019-10-21 17:28:53', '2019-10-21 17:28:55');
INSERT INTO `web_role_menu_re` VALUES ('13', '2', '4', '4', '2019-10-21 17:29:04', '2019-10-21 17:29:06');
INSERT INTO `web_role_menu_re` VALUES ('14', '2', '4', '5', '2019-10-21 17:29:17', '2019-10-21 17:29:19');
INSERT INTO `web_role_menu_re` VALUES ('15', '2', '5', '1', '2019-10-21 17:30:31', '2019-10-21 17:30:33');
INSERT INTO `web_role_menu_re` VALUES ('16', '2', '7', '1', '2019-10-21 17:30:46', '2019-10-21 17:30:49');
INSERT INTO `web_role_menu_re` VALUES ('17', '2', '7', '2', '2019-10-21 17:35:19', '2019-10-21 17:35:21');
INSERT INTO `web_role_menu_re` VALUES ('18', '2', '7', '3', '2019-10-21 17:35:31', '2019-10-21 17:35:33');
INSERT INTO `web_role_menu_re` VALUES ('19', '2', '7', '4', '2019-10-21 17:35:43', '2019-10-21 17:35:46');
INSERT INTO `web_role_menu_re` VALUES ('20', '2', '7', '5', '2019-10-21 17:35:57', '2019-10-21 17:35:59');
INSERT INTO `web_role_menu_re` VALUES ('21', '2', '8', '6', '2019-10-21 17:36:26', '2019-10-21 17:36:31');
INSERT INTO `web_role_menu_re` VALUES ('22', '2', '9', '7', '2019-10-21 17:36:45', '2019-10-21 17:36:48');
INSERT INTO `web_role_menu_re` VALUES ('23', '2', '10', '2', '2019-10-21 17:37:04', '2019-10-21 17:37:07');
INSERT INTO `web_role_menu_re` VALUES ('24', '2', '10', '4', '2019-10-21 17:37:17', '2019-10-21 17:37:19');
INSERT INTO `web_role_menu_re` VALUES ('25', '2', '11', '1', '2019-10-22 18:11:22', '2019-10-22 18:11:24');
INSERT INTO `web_role_menu_re` VALUES ('26', '2', '11', '2', '2019-10-22 18:11:35', '2019-10-22 18:11:37');
INSERT INTO `web_role_menu_re` VALUES ('27', '2', '11', '3', '2019-10-22 18:11:44', '2019-10-22 18:11:46');
INSERT INTO `web_role_menu_re` VALUES ('28', '2', '11', '4', '2019-10-22 18:11:55', '2019-10-22 18:11:59');

-- ----------------------------
-- Table structure for `web_system`
-- ----------------------------
DROP TABLE IF EXISTS `web_system`;
CREATE TABLE `web_system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(36) NOT NULL COMMENT '名称',
  `key_value` varchar(1024) DEFAULT NULL COMMENT '值',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公共参数配置表';

-- ----------------------------
-- Records of web_system
-- ----------------------------
