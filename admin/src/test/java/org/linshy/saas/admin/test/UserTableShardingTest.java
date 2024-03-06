package org.linshy.saas.admin.test;

public class UserTableShardingTest {
    public static final String SQL = "CREATE TABLE `t_link_stats_today_%d` (\n" +
            "`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "`gid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'default' COMMENT '分组标识',\n" +
            "`full_short_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '短链接',\n" +
            "`date` date DEFAULT NULL COMMENT '日期',\n" +
            "`today_pv` int DEFAULT '0' COMMENT '今日PV',\n" +
            "`today_uv` int DEFAULT '0' COMMENT '今日UV',\n" +
            "`today_uip` int DEFAULT '0' COMMENT '今日IP数',\n" +
            "`create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "`update_time` datetime DEFAULT NULL COMMENT '修改时间',\n" +
            "`del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',\n" +
            "PRIMARY KEY (`id`) USING BTREE,\n" +
            "UNIQUE KEY `idx_unique_today_stats` (`full_short_url`,`gid`,`date`) USING BTREE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;;";

    public static void main(String[] args) {

        for (int i = 0; i < 16; i++) {
            System.out.println(String.format(SQL, i));
        }

    }
}
