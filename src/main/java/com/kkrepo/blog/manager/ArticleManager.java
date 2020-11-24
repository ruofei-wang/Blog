package com.kkrepo.blog.manager;

import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.common.Constant;
import com.kkrepo.blog.common.enums.CommonStateEnum;
import com.kkrepo.blog.common.model.ArchivesWithArticle;
import com.kkrepo.blog.common.model.ArticleModel;
import com.kkrepo.blog.domain.Article;
import com.kkrepo.blog.domain.Category;
import com.kkrepo.blog.domain.Comment;
import com.kkrepo.blog.domain.Tag;
import com.kkrepo.blog.domain.User;
import com.kkrepo.blog.service.ArticleService;
import com.kkrepo.blog.service.CategoryService;
import com.kkrepo.blog.service.CommentService;
import com.kkrepo.blog.service.TagService;
import com.kkrepo.blog.task.AsyncEsDocumentTask;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * @author WangRuofei
 * @create 2020-05-25 4:43 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class ArticleManager {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private AsyncEsDocumentTask asyncEsDocumentTask;

    /**
     * 封装页面footer中,最新的文章和评论数据
     * @param model
     */
    public void init(Model model) {
        List<Article> articleList = articleService.queryLatestArticles();
        model.addAttribute(Constant.RECENT_POSTS, articleList);
        List<Comment> commentList = commentService.queryLatestArticles();
        model.addAttribute(Constant.RECENT_COMMENTS, commentList);
    }

    /**
     * 首页数据
     * @param pageNum
     * @param pageSize
     * @param model
     */
    public void index(int pageNum, int pageSize, Model model) {
        PageInfo<Article> articlePageInfo = articleService.queryByPage(pageNum, pageSize, CommonStateEnum.PUBLISHED);
        buildArticleModePage(articlePageInfo, model);
    }

    /**
     * 按照分类获取数据
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param model
     */
    public void queryPageByCategory(int categoryId, int pageNum, int pageSize, Model model) {
        PageInfo<Article> articlePageInfo = articleService.queryPageByCategory(categoryId, pageNum, pageSize, CommonStateEnum.PUBLISHED);
        articlePageInfo.setNavigatePages(categoryId);
        buildArticleModePage(articlePageInfo, model);
    }

    /**
     * 按照标签获取数据
     * @param tagId
     * @param pageNum
     * @param pageSize
     * @param model
     */
    public void queryPageByTag(int tagId, int pageNum, int pageSize, Model model) {
        PageInfo<Article> articlePageInfo = articleService.queryPageByTag(tagId, pageNum, pageSize, CommonStateEnum.PUBLISHED);
        articlePageInfo.setNavigatePages(tagId);
        buildArticleModePage(articlePageInfo, model);
    }

    /**
     * 按照作者获取数据
     * @param author
     * @param pageNum
     * @param pageSize
     * @param model
     */
    public void queryPageByAuthor(String author, int pageNum, int pageSize, Model model) {
        PageInfo<Article> articlePageInfo = articleService.queryPageByAuthor(author, pageNum, pageSize, CommonStateEnum.PUBLISHED);
        model.addAttribute("author", author);
        buildArticleModePage(articlePageInfo, model);
    }

    public void buildArticleModePage(PageInfo<Article> articlePageInfo, Model model) {
        PageInfo<ArticleModel> pageInfo = new PageInfo<>();
        BeanUtils.copyProperties(articlePageInfo, pageInfo, "list");
        pageInfo.setList(articlePageInfo.getList().stream().map(x -> buildIndexArticleMode(x)).collect(Collectors.toList()));
        model.addAttribute(Constant.INDEX_PAGEINFO, pageInfo);
        init(model);
    }

    public ArticleModel queryById(long id) {
        Article article = articleService.queryById(id);
        return buildArticleModel(article);
    }

    /**
     * 归档数据
     * @param model
     */
    public void qeuryArchives(Model model) {
        model.addAttribute(Constant.ARCHIVES_MODEL, queryArchives());
        init(model);
    }

    public List<ArchivesWithArticle> queryArchives() {
        List<ArchivesWithArticle> archivesWithArticleList = new ArrayList<>();
        List<String> datas = articleService.queryArchivesDates(CommonStateEnum.PUBLISHED);
        datas.forEach(x -> {
            List<Article> articleList = articleService.queryArchivesByDate(x, CommonStateEnum.PUBLISHED);
            archivesWithArticleList.add(ArchivesWithArticle.builder()
                .date(x)
                .articles(articleList)
                .build()
            );
        });
        return archivesWithArticleList;
    }

    public ArticleModel buildIndexArticleMode(Article article) {
        ArticleModel articleModel = new ArticleModel();
        BeanUtils.copyProperties(article, articleModel);
        buildCategory(articleModel);
        return articleModel;
    }

    public ArticleModel buildArticleModel(Article article) {
        ArticleModel articleModel = new ArticleModel();
        BeanUtils.copyProperties(article, articleModel);
        buildCategory(articleModel);
        buildTags(articleModel);
        return articleModel;
    }

    private void buildCategory(ArticleModel articleModel) {
        Category category = categoryService.queryByArticle(articleModel.getId());
        articleModel.setCategory(category);
    }

    private void buildTags(ArticleModel articleModel) {
        articleModel.setTags(tagService.queryByArticleId(articleModel.getId()));
    }

    public long countAll() {
        return articleService.countAll();
    }

    public PageInfo<ArticleModel> list(String title, String author, Integer pageNum, Integer pageSize) {
        PageInfo<Article> articlePageInfo = articleService.list(title, author, pageNum, pageSize);
        PageInfo<ArticleModel> pageInfo = new PageInfo<>();
        BeanUtils.copyProperties(articlePageInfo, pageInfo, "list");
        pageInfo.setList(articlePageInfo.getList().stream().map(x -> buildArticleModel(x)).collect(Collectors.toList()));
        return pageInfo;
    }

    public long add(ArticleModel articleModel, User user) {
        Date date = new Date();
        String state = articleModel.getState();
        if (state == null) {
            state = CommonStateEnum.INIT.getCode();
            articleModel.setState(state);
        }
        if (CommonStateEnum.PUBLISHED.getCode().equals(state)) {
            articleModel.setPublishTime(date);
        }
        if (articleModel.getType() == null) {
            articleModel.setType(0);
        }
        articleModel.setAuthor(user.getUsername());
        articleModel.setCreateTime(date);
        articleModel.setUpdateTime(date);
        Article article = new Article();
        BeanUtils.copyProperties(articleModel, article);
        int flag = articleService.insert(article);
        articleModel.setId(article.getId());
        updateArticleCategoryTags(articleModel);
        return flag;
    }

    private void updateArticleCategoryTags(ArticleModel articleModel) {
        long articleId = articleModel.getId();
        if (articleId != 0) {
            Category category = articleModel.getCategory();
            Long categoryId = category.getId();
            if (category != null) {
                categoryService.deleteArticleCategoryByArticleId(articleId);
            } else {
                categoryId = 1L;
            }
            categoryService.addArticleCategory(articleId, categoryId);
            List<Tag> tagList = articleModel.getTags();
            if (tagList != null && tagList.size() > 0) {
                tagService.deleteArticleTagByArticleId(articleId);
                tagList.forEach(x -> tagService.addArticleTag(articleId, x.getId()));
            }
        }
    }

    public int update(ArticleModel articleModel, User user) {
        if (articleModel.getPublishTime() == null && CommonStateEnum.PUBLISHED.getCode().equals(articleModel.getState())) {
            articleModel.setPublishTime(new Date());
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleModel, article);
        article.setCreateTime(new Date());
        int flag = articleService.update(article);
        updateArticleCategoryTags(articleModel);
        // 异步更新es document数据
        asyncEsDocumentTask.updateBlogDocumentByArticle(article);
        return flag;
    }

    public int delete(Long id) {
        Article article = articleService.queryById(id);
        if (article != null && !CommonStateEnum.DELETED.getCode().equals(article.getState())) {
            article.setState(CommonStateEnum.DELETED.getCode());
            return articleService.update(article);
        }
        // 异步删除es document数据
        asyncEsDocumentTask.deleteBlogDocumentByArticleId(article.getId().toString());
        return 0;
    }

    public int publish(Long id) {
        Article article = articleService.queryById(id);
        if (article != null && !CommonStateEnum.PUBLISHED.getCode().equals(article.getState())) {
            article.setState(CommonStateEnum.PUBLISHED.getCode());
            article.setPublishTime(new Date());
            // 异步更新es document数据
            asyncEsDocumentTask.updateBlogDocumentByArticle(article);
            return articleService.update(article);
        }
        return 0;
    }

    public List<Article> queryAll() {
        return articleService.queryAll();
    }

    public List<Article> queryByCategory(Long categoryId, CommonStateEnum commonStateEnum) {
        if (commonStateEnum == null) {
            return articleService.queryByCategory(categoryId);
        } else {
            return articleService.queryByCategory(categoryId, commonStateEnum);
        }
    }

    public List<Article> queryByTag(Long tagId, CommonStateEnum commonStateEnum) {
        if (commonStateEnum == null) {
            return articleService.queryByTag(tagId);
        } else {
            return articleService.queryByTag(tagId, commonStateEnum);
        }
    }
}
