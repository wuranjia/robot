package com.ichat4j.wrj.robot.domain;

import java.util.HashSet;
import java.util.Set;

public class Common {
    public static String[] nickNames = new String[]{
            "Wrj",
            "小镜子",
            "mawentao",
            "丁玲娟",
            "炸茄盒",
            "Zhou",
            "诺亚之舟",
            "彪",
            "张宗碧",
            "沈佳伟",
            "wwf",
            "daizhenli",
            "张新昆",
            "左右雨雪星月",
            "AILEEN",
            "李冰",
            "新强",
            "caesarwee",
            "UFO"
    };

    public static Set<String> nickNameSet = new HashSet<String>();
    static{
        for(String s:nickNames){
            nickNameSet.add(s);
        }
    }
}
