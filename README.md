<a href="https://www.kkrepo.com">
    <img src="https://www.kkrepo.com/site/images/logo.png" alt="Blog logo" title="Blog" align="right" height="60" />
</a>

# Blog

![GitHub release (latest by date)](https://img.shields.io/github/v/release/kkrepocom/blog) ![GitHub top language](https://img.shields.io/github/languages/top/kkrepocom/blog) ![GitHub issues](https://img.shields.io/github/issues/kkrepocom/blog) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/kkrepocom/blog) ![Lines of code](https://img.shields.io/tokei/lines/github/kkrepocom/blog) ![GitHub](https://img.shields.io/github/license/kkrepocom/blog)

[Blog](https://github.com/kkrepocom/Blog) 项目是我参照 [TyCoding](https://github.com/TyCoding) 大神的 [Tumo](https://github.com/TyCoding/tumo) 项目写的一个个人博客。项目采用 `SpringBoort` + `Shiro` + `MyBatis` + `Vue` + `Thymeleaf` 技术开发，数据存储方面使用了 `MySQL` 和 `Redis` 。项目采用 `Github Webhooks` 推送触发 `jenkins` 自动打包 `Docker` 镜像部署。默认主题使用了漂亮的 [pinghsu](https://github.com/chakhsu/pinghsu) ，简洁大方。



- 在线预览：[https://www.kkrepo.com](https://www.kkrepo.com)
- 项目搭建：[https://www.kkrepo.com/article/1](https://www.kkrepo.com/article/1)


![image-20201011155636571](https://github.com/kkrepocom/ImageHosting/blob/master/image-20201011155636571-z1NipE.png)



## 目录

- [预览](#预览)
  - [前台展示](#前台展示)
  - [后台管理](#后台管理)
- [介绍](#介绍)
  - [技术选型](#技术选型)
- [快速开始(本地运行)](#快速开始(本地运行))
- [服务区环境配置](#服务区环境配置)
- [Github Webhooks 触发自动部署配置](https://github.com/kkrepocom/Blog#github-webhooks-%E8%A7%A6%E5%8F%91%E8%87%AA%E5%8A%A8%E9%83%A8%E7%BD%B2%E9%85%8D%E7%BD%AE)
- [Jenkins 持续部署配置](https://github.com/kkrepocom/Blog#jenkins-%E6%8C%81%E7%BB%AD%E9%83%A8%E7%BD%B2%E9%85%8D%E7%BD%AE)
- [前端特效](#前端特效)
- [遇到的问题及解决方案](#遇到的问题及解决方案)
- [License](#License)

## 预览

### 前台展示

![image-20201011161751906](https://github.com/kkrepocom/ImageHosting/blob/master/image-20201011161751906-DxOVsV.png)


![image-20201011161837855](https://cdn.kkrepo.com/uPic/2020-10-11/image-20201011161837855-1aLuEY.png)

### 后台管理

![image-20201011205513861](https://cdn.kkrepo.com/uPic/2020-10-11/image-20201011205513861-ujgzSY.png)


![image-20201011162302908](https://cdn.kkrepo.com/uPic/2020-10-11/image-20201011162302908-vjDNis.png)



## 介绍

### 技术选型

|    name    |      版本      |              备注              |
| :--------: | :------------: | :----------------------------: |
| SpringBoot | 2.3.0.RELEASE  |          后端主体框架          |
|   Shiro    |     1.5.3      |              鉴权              |
|    JWT     |     0.9.1      |          jsonwebtoken          |
|   MySQL    |    > 5.7.0     |             数据库             |
|  MyBatis   |     2.1.2      |           持久层框架           |
|   Redis    | 2.3.0.RELEASE  | spring-boot-starter-data-redis |
|    Vue     |    v2.6.11     |      后台管理系统前端使用      |
| thymeleaf  | 3.0.11.RELEASE |            Blog页面            |

## 快速开始(本地运行)

（1）clone 代码

（2）在本地数据库中执行项目根目录下 `ddl` 文件中的 `sql` 文件

（3）修改项目 `application.yml` 文件中的配置

（4）启动项目

（5）在浏览器中访问 `http://localhost:8080/admin`，输入 `admin/123456` 进入后台管理系统

（6）添加标签、分类等，编辑博客、保存

（7）在文章列表中点击发布按钮

（8）拷贝博客链接，然后在访问

## [服务器环境配置](https://github.com/kkrepocom/Blog/wiki/%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%85%8D%E7%BD%AE)

- Nginx
- JDK
- MySQL
- Git
- Maven
- Docker
- Jenkins

## [Github Webhooks 触发自动部署配置](https://github.com/kkrepocom/Blog/wiki/Github-Webhooks-%E8%A7%A6%E5%8F%91%E8%87%AA%E5%8A%A8%E9%83%A8%E7%BD%B2%E9%85%8D%E7%BD%AE)

- [`SSH` 配置](https://github.com/kkrepocom/Blog/wiki/Github-Webhooks-%E8%A7%A6%E5%8F%91%E8%87%AA%E5%8A%A8%E9%83%A8%E7%BD%B2%E9%85%8D%E7%BD%AE#ssh-%E9%85%8D%E7%BD%AE)
- [`Webhooks` 配置](https://github.com/kkrepocom/Blog/wiki/Github-Webhooks-%E8%A7%A6%E5%8F%91%E8%87%AA%E5%8A%A8%E9%83%A8%E7%BD%B2%E9%85%8D%E7%BD%AE#webhooks-%E9%85%8D%E7%BD%AE)
- [`access tokens` 配置](https://github.com/kkrepocom/Blog/wiki/Github-Webhooks-%E8%A7%A6%E5%8F%91%E8%87%AA%E5%8A%A8%E9%83%A8%E7%BD%B2%E9%85%8D%E7%BD%AE#access-tokens-%E9%85%8D%E7%BD%AE)

## [Jenkins 持续部署配置](https://github.com/kkrepocom/Blog/wiki/Jenkins-%E9%85%8D%E7%BD%AE%E5%8F%8A%E6%8C%81%E7%BB%AD%E9%9B%86%E6%88%90)

- [`Jenkins` 配置及持续集成](https://github.com/kkrepocom/Blog/wiki/Jenkins-%E9%85%8D%E7%BD%AE%E5%8F%8A%E6%8C%81%E7%BB%AD%E9%9B%86%E6%88%90#jenkins-%E9%85%8D%E7%BD%AE%E5%8F%8A%E6%8C%81%E7%BB%AD%E9%9B%86%E6%88%90)
- [`Blog` 项目配置](https://github.com/kkrepocom/Blog/wiki/Jenkins-%E9%85%8D%E7%BD%AE%E5%8F%8A%E6%8C%81%E7%BB%AD%E9%9B%86%E6%88%90#blog-%E9%A1%B9%E7%9B%AE%E9%85%8D%E7%BD%AE)

## [前端特效](https://github.com/kkrepocom/Blog/wiki/%E5%89%8D%E7%AB%AF%E7%89%B9%E6%95%88)

- [鼠标点击爱心特效](https://github.com/kkrepocom/Blog/wiki/%E5%89%8D%E7%AB%AF%E7%89%B9%E6%95%88#%E9%BC%A0%E6%A0%87%E7%82%B9%E5%87%BB%E7%88%B1%E5%BF%83%E7%89%B9%E6%95%88)
- [炫彩三角纽带效果](https://github.com/kkrepocom/Blog/wiki/%E5%89%8D%E7%AB%AF%E7%89%B9%E6%95%88#%E7%82%AB%E5%BD%A9%E4%B8%89%E8%A7%92%E7%BA%BD%E5%B8%A6%E6%95%88%E6%9E%9C)
- [右下角人物](https://github.com/kkrepocom/Blog/wiki/%E5%89%8D%E7%AB%AF%E7%89%B9%E6%95%88#%E5%8F%B3%E4%B8%8B%E8%A7%92%E7%9A%84%E5%B0%8F%E5%A7%91%E5%A8%98)
- [背景动态线条](https://github.com/kkrepocom/Blog/wiki/%E5%89%8D%E7%AB%AF%E7%89%B9%E6%95%88#%E8%83%8C%E6%99%AF%E5%8A%A8%E6%80%81%E7%BA%BF%E6%9D%A1)


## [遇到的问题及解决方案](https://github.com/kkrepocom/Blog/wiki/%E9%81%87%E5%88%B0%E7%9A%84%E9%97%AE%E9%A2%98%E5%8F%8A%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88)

- [`mvn` 命令未找到](https://github.com/kkrepocom/Blog/wiki/%E9%81%87%E5%88%B0%E7%9A%84%E9%97%AE%E9%A2%98%E5%8F%8A%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88#mvn-%E5%91%BD%E4%BB%A4%E6%9C%AA%E6%89%BE%E5%88%B0)

- [`jenkins` 拉取代码速度慢](https://github.com/kkrepocom/Blog/wiki/%E9%81%87%E5%88%B0%E7%9A%84%E9%97%AE%E9%A2%98%E5%8F%8A%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88#jenkins-%E6%8B%89%E5%8F%96%E4%BB%A3%E7%A0%81%E9%80%9F%E5%BA%A6%E6%85%A2)


## License

[MIT](LICENSE) © kkrepocom

