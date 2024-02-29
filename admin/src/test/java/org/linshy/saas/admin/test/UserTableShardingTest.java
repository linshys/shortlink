package org.linshy.saas.admin.test;

public class UserTableShardingTest {
    public static final String SQL = "CREATE TABLE `t_group_%d` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',\n" +
            "  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',\n" +
            "  `username` varchar(256) DEFAULT NULL COMMENT '创建分组用户名称',\n" +
            "  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',\n" +
            "  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "  `update_time` datetime DEFAULT NULL COMMENT '修改时间',\n" +
            "  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `idx_unique_username_gid` (`gid`,`username`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1761961348657029123 DEFAULT CHARSET=utf8mb4;";

    public static void main(String[] args) {

        for (int i = 0; i < 16; i++) {
            System.out.println(String.format(SQL, i));
        }

    }
}
