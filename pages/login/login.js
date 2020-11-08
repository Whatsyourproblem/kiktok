// pages/login/login.js
import http from '../../utils/api.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgUrl: "/images/icon/defaultHead.png",
    imgCount: "/images/icon/idInput.png",
    imgPas: "/images/icon/pasInput.png",
    //用户个人信息
    userInfo: {
      avatarUrl: "",//用户头像
      nickName: "",//用户昵称
    },
    userId: "",
    userPwd: ""
  },
  getId: function (e) {
    this.setData({
      userId: e.detail.value
    })
  },
  getPwd: function (e) {
    this.setData({
      userPwd: e.detail.value
    })
  },
  // 登陆发送数据
  login: function (e) {
    var userId = this.data.userId;
    var userPwd = this.data.userPwd;

    // 1.判空处理
    if (userId == "" || userPwd == "") {
      wx.showModal({
        content: "请输入正确的账号和密码"
      })
    } else {
      wx.showLoading({
        title: "正在登陆中",
        mask: true,
      })
      http.loginApi({
        data: {
          phoneNum: userId,
          passWord: userPwd
        },
        success: res => {
          wx.hideLoading();
          console.log(res);
          // 如果是0，登陆成功
          if (res.status == 0) {
            // 存放的用户的信息
            let user = {
              // 用户号
              userId: res.data.userId,
              // 地址
              userInfoAddress: res.data.userInfoAddress,
              //生日
              userInfoBirth: res.data.userInfoBirth,
              // 视频收藏量
              userInfoCollects: res.data.userInfoCollects,
              // 邮箱
              userInfoEmail: res.data.userInfoEmail,
              // 粉丝量
              userInfoFans: res.data.userInfoFans,
              // 关注量
              userInfoFocus: res.data.userInfoFocus,
              // 性别
              userInfoGender: res.data.userInfoGender,
              // 头像
              userInfoImg: res.data.userInfoImg,
              // 简介
              userInfoIntro: res.data.userInfoIntro,
              // 名字
              userInfoName: res.data.userInfoName,
              // 发布量
              userInfoPublish: res.data.userInfoPublish,
              // 总获赞量
              userInfoStar: res.data.userInfoStar,
            }
            // session存储用户对象
            wx.setStorageSync("user", user);
            //登陆成功跳转到信息界面
            wx.switchTab({
              url: "/pages/myself/myself"
            })
          } else {
            wx.showModal({
              content: '账号密码错误，请重新登陆',
            })
          }
        },
        fail: err => {
          wx.hideLoading();
          wx.showModal({
            content: "登陆接口还未完成,稍等"
          })
        }
      })
    }
  },
  // 前往注册界面
  register: function (e) {
    wx.navigateTo({
      url: "/pages/register/register"
    })
  },
  //  取消登陆界面，进入首页
  quxiao: function (e) {
    wx.switchTab({
      url: "/pages/index/index"
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    /**
     * 获取用户信息
     */
    wx.getUserInfo({
      success: function (res) {
        //console.log(res);
        var avatarUrl = 'userInfo.avatarUrl';
        var nickName = 'userInfo.nickName';
        that.setData({
          [avatarUrl]: res.userInfo.avatarUrl,
          [nickName]: res.userInfo.nickName,
        })
      }
    })
  }
})