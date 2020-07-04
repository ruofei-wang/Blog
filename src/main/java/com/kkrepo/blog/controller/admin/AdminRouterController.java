package com.kkrepo.blog.controller.admin;

import com.kkrepo.blog.domain.User;
import com.kkrepo.blog.manager.ArticleManager;
import com.kkrepo.blog.manager.CommentManager;
import com.kkrepo.blog.service.TagService;
import com.kkrepo.blog.service.UserService;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 博客后台路由控制
 * @author WangRuofei
 * @create 2020-05-27 5:29 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Controller
public class AdminRouterController extends BaseController {

    @Autowired
    private ArticleManager articleManager;
    @Autowired
    private CommentManager commentManager;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    /**
     * 注销接口
     *
     * @return
     */
    @GetMapping(value = "/logout")
    public String logout() {
        Subject subject = getSubject();
        subject.logout();
        return "redirect:/login";
    }

    /**
     * 登录状态校验
     */
    private boolean auth(HttpServletRequest request, Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        model.addAttribute("user", currentUser);
        return true;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "admin/login";
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "admin/register";
    }

    @GetMapping("/admin")
    public String index(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        model.addAttribute("articleCount", articleManager.countAll());
        model.addAttribute("tagCount", tagService.countAll());
        model.addAttribute("commentCount", commentManager.countAll());
        model.addAttribute("userCount", userService.countAll());
        return "admin/index/index";
    }

    @GetMapping("/admin/profile")
    public String profile(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        return "admin/profile/index";
    }

    @GetMapping("/admin/article/write")
    public String articleWrite(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        return "admin/article/write/index";
    }

    @GetMapping("/admin/article/list")
    public String articleList(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        return "admin/article/list/index";
    }

    @GetMapping("/admin/blog/tag")
    public String blogTag(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        return "admin/blog/tag/index";
    }

    @GetMapping("/admin/blog/category")
    public String blogCategory(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        return "admin/blog/category/index";
    }

    @GetMapping("/admin/blog/link")
    public String blogLink(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        return "admin/blog/link/index";
    }

    @GetMapping("/admin/blog/comment")
    public String blogComment(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        return "admin/blog/comment/index";
    }

    @GetMapping("/admin/setting/log")
    public String settingLog(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        return "admin/setting/log/index";
    }

    @GetMapping("/admin/setting/qiniu")
    public String settingQiniu(HttpServletRequest request, Model model) {
        if (!this.auth(request, model)) {
            return "redirect:/login";
        }
        return "admin/setting/qiniu/index";
    }

}
