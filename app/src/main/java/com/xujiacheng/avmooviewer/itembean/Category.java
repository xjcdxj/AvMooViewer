package com.xujiacheng.avmooviewer.itembean;

/*
主题
角色
服装
体型
行为
玩法
类别
其他
 */
public class Category extends Info {
    public static final int COUNT = 8;
    public static final String[] TABS = {
            "主题",
            "角色",
            "服装",
            "体型",
            "行为",
            "玩法",
            "类别",
            "其他"};
    private static final long serialVersionUID = -9071156318964146280L;

    public Category(String name, String url) {
        super(name, url);
    }
}
