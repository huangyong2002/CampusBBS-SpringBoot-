package com.huang.BBS;

import com.huang.BBS.dao.mapper.ArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MpApplicationTests {

    @Autowired(required = false)
    ArticleMapper articleMapper;

    @Test
    void test01() {

    }
}
