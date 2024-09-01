package com.example.demo.crolling;

import org.apache.commons.collections4.map.MultiKeyMap;

// 만약에 ski외의 다른 것들을 크롤링한다고 하면, default로 만드는 것은 별로 안좋은 선택일 듯하다.
public interface CreateCheckTable {
    MultiKeyMap<String, Boolean> createCheckList(String name) ;


}
