package com.huang.BBS.vo.params;

import com.huang.BBS.vo.CategoryVo;
import com.huang.BBS.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}