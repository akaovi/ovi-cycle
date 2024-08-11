package com.sunyy.usercentor.pojo.vo;

import java.util.Date;

/**
 * @author ovi
 * @since 2024/7/17
 */

public class SysUserVo {
    /**
     * 用户id 雪花算法
     */
    private Long userId;

    /**
     * 姓名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别 0 男 1 女
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 给个默认头像
     */
    private String avatar;

    /**
     * 签名
     */
    private String signature;

    /**
     * GitHub
     */
    private String github;

    /**
     * Gitee
     */
    private String gitee;

    /**
     * 博客
     */
    private String blog;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getGitee() {
        return gitee;
    }

    public void setGitee(String gitee) {
        this.gitee = gitee;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SysUserVo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", signature='" + signature + '\'' +
                ", github='" + github + '\'' +
                ", gitee='" + gitee + '\'' +
                ", blog='" + blog + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
