package com.kkrepo.blog.controller;

import com.kkrepo.blog.common.Constant;
import com.kkrepo.blog.common.enums.CommentSortEnum;
import com.kkrepo.blog.common.enums.CommonStateEnum;
import com.kkrepo.blog.common.model.ArticleModel;
import com.kkrepo.blog.common.util.AddressUtil;
import com.kkrepo.blog.common.util.IPUtil;
import com.kkrepo.blog.domain.Comment;
import com.kkrepo.blog.manager.ArticleManager;
import com.kkrepo.blog.manager.CommentManager;
import com.kkrepo.blog.service.CommentService;
import com.kkrepo.blog.service.LinkService;
import com.kkrepo.blog.support.R;
import com.kkrepo.blog.support.ResultWrap;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 博客前端接口
 * @author WangRuofei
 * @create 2020-05-25 4:06 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Controller
public class BlogController {

    private static final String INDEX_PAGE_PATH = "site/index";
    private static final String CATEGORY_PAGE_PATH = "site/category";
    private static final String TAG_PAGE_PATH = "site/tag";
    private static final String PAGE_DETAIL = "site/page/article";
    private static final String PAGE_ARCHIVES = "site/page/archives";
    private static final String PAGE_LINKS = "site/page/links";
    private static final String PAGE_OBOUT = "site/page/about";

    @Autowired
    private ArticleManager articleManager;
    @Autowired
    private CommentManager commentManager;
    @Autowired
    private LinkService linkService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/error/500")
    public String error() {
        return "error/500";
    }

    @RequestMapping({"", "/", "/page/{pageNum}"})
    public String index(
        @PathVariable(required = false) Integer pageNum,
        Model model
    ) {
        pageNum = pageNum == null ? 1 : pageNum;
        articleManager.index(pageNum, Constant.DEFAULT_PAGE_LIMIT, model);
        return INDEX_PAGE_PATH;
    }

    @RequestMapping({"/category/{categoryId}", "/category/{categoryId}/{pageNum}"})
    public String category(
        @PathVariable(required = true) Integer categoryId,
        @PathVariable(required = false) Integer pageNum,
        Model model
    ) {
        pageNum = pageNum == null ? 1 : pageNum;
        articleManager.queryPageByCategory(categoryId,pageNum, Constant.DEFAULT_PAGE_LIMIT, model);
        return CATEGORY_PAGE_PATH;
    }

    @RequestMapping({"/tag/{tagId}", "/tag/{tagId}/{pageNum}"})
    public String tag(
        @PathVariable(required = true) Integer tagId,
        @PathVariable(required = false) Integer pageNum,
        Model model
    ) {
        pageNum = pageNum == null ? 1 : pageNum;
        articleManager.queryPageByTag(tagId,pageNum, Constant.DEFAULT_PAGE_LIMIT, model);
        return TAG_PAGE_PATH;
    }

    @RequestMapping("/article/{id}")
    public String article(
        @PathVariable(value = "id") long id,
        @RequestParam(name = "pageNum", required = false) Integer pageNum,
        Model model
    ) {
        if (id == 0) {
            return this.error();
        }
        pageNum = pageNum == null ? 1 : pageNum;
        ArticleModel articleModel = articleManager.queryById(id);
        if (articleModel == null || !CommonStateEnum.PUBLISHED.getCode().equals(articleModel.getState())) {
            return "redirect:/error/500";
        }
        model.addAttribute(Constant.ARTICLE_MODEL, articleModel);
        commentManager.buidComments(id, pageNum, CommentSortEnum.DEFAULT, model);
        articleManager.init(model);
        return PAGE_DETAIL;
    }

    @RequestMapping("/archives")
    public String archives(Model model) {
        articleManager.qeuryArchives(model);
        return PAGE_ARCHIVES;
    }

    @RequestMapping("/links")
    public String links(
        @RequestParam(name = "pageNum", required = false) Integer pageNum,
        Model model
    ) {
        pageNum = pageNum == null ? 1 : pageNum;
        model.addAttribute(Constant.LINKS_MODEL, linkService.queryAll());
        commentManager.buidComments(null, pageNum, CommentSortEnum.FRIENDURL, model);
        articleManager.init(model);
        return PAGE_LINKS;
    }

    @RequestMapping("/about")
    public String about(
        @RequestParam(name = "pageNum", required = false) Integer pageNum,
        Model model
    ) {
        pageNum = pageNum == null ? 1 : pageNum;
        commentManager.buidComments(null, pageNum, CommentSortEnum.ABOUT, model);
        articleManager.init(model);
        return PAGE_OBOUT;
    }

    @RequestMapping(value = "/api/comment", method = RequestMethod.POST)
    @ResponseBody
    public R commentSubmit(
        @RequestBody Comment comment
    ) {
        String ip = IPUtil.getIpAddr(request);
        comment.setCreateTime(new Date());
        comment.setIp(ip);
        comment.setAddress(AddressUtil.getAddress(ip));
        String userAgentHeader = request.getHeader(Constant.USER_AGENT);
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentHeader);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        comment.setDevice(browser.getName() + "," + operatingSystem.getName());
        commentService.add(comment);
        return ResultWrap.ok(1);
    }

}
