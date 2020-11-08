// pages/myself/myself.js
import http from '../../utils/api.js'
Page({
  /**
   * 页面的初始数据
   */
  data: {
    kabaoImg: "/images/icon/kabao.png",
    kefuImg: "/images/icon/kefu.png",
    likeImg: "/images/icon/unlike1.png",
    moreImg: "/images/icon/delete.png",
    noZanImg: "/images/icon/noZan.png",
    zanImg: "/images/icon/Zan.png",
    commentImg: "/images/icon/comments2.png",
    transforImg: "/images/icon/transfor.png",
    tuichuImg: "/images/icon/quit.png",
    userId: "",
    userInfoAddress: "河南",
    userInfoBirth: "2000-11-05T16:00:00.000+00:00",
    // 视频收藏量
    userInfoCollects: 100,
    userInfoEmail: "2539417782@qq.com",
    userInfoFans: 100,
    userInfoFocus: 100,
    userInfoGender: "男",
    userInfoImg: "/images/icon/defaultHead.png",
    userInfoIntro: "大傻吊",
    userInfoName: "刘鹏辉",
    userInfoPublish: 100,
    userInfoStar: 100,
    // 选择动态页面
    select: 2,
    controls: false,
    //是否显示全屏按钮
    showFullscreenBtn: false,
    //是否显示视频中间的播放按钮
    showCenterPlayBtn: false,
    //指定视频初始播放位置
    initialTime: '1',
    //是否显示视频底部控制栏的播放按钮
    showPlayBtn: true,
    //是否开启播放手势，即双击切换播放/暂停
    enablePlayGesture: false,
    //是否开启控制进度的手势
    enableProgressGesture: false,
    showList: [
      {
        videoId: 1,
        title: '第一个',
        recommand: 100,
        videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/21671258",
      }, {
        videoId: 2,
        title: '第二个',
        recommand: 110,
        videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/22717569",
      }, {
        videoId: 3,
        title: '第三个',
        recommand: 130,
        videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/24735953",
      }, {
        videoId: 4,
        title: '第四个',
        recommand: 1060,
        videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/31396532",
      }, {
        videoId: 5,
        title: '第五个',
        recommand: 1040,
        videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/35516298",
      }, {
        videoId: 6,
        title: '第六个',
        recommand: 1004,
        videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/44676919",
      },
    ],
    dynamicList: [
      {
        publishId: "48266723",
        publishName: "自己",
        publishImg: "/images/headImg/fuluwa.jpg",
        publishTime: "2020/10/11",
        pinglunContext: "你个憨批",
        // 动态的ID，随机数，唯一
        dynamicId: 1,
        // 动态来源
        dynamicFromId: "1234",
        dynamicFromName: "王五",
        dynamicFromContext: "这是一个好搞笑的视频啊",
        videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/21671258",
        isLike: false,
        recommand: 10,
        commentNum: 66,
        transforNum: 100,
        // 动态类型 0:转发的 1：别人评论的
        type: 0
      },
      {
        publishId: 2,
        publishName: "李四",
        publishImg: "/images/icon/defaultHead.png",
        publishTime: "2020/10/11",
        pinglunContext: "你个丑憨批",
        // 动态的ID，随机数，唯一
        dynamicId: 2,
        // 动态来源
        dynamicFromId: "48266723",
        dynamicFromName: "自己",
        dynamicFromContext: "这是一个好搞笑的视频啊",
        videoUrl: "",
        isLike: false,
        recommand: 101,
        commentNum: 636,
        transforNum: 100,
        type: 1
      },
      {
        publishId: 3,
        publishName: "王五",
        publishImg: "/images/icon/defaultHead.png",
        publishTime: "2020/10/11",
        pinglunContext: "憨批",
        // 动态的ID，随机数，唯一
        dynamicId: 3,
        // 动态来源
        dynamicFromId: "48266723",
        dynamicFromName: "自己",
        dynamicFromContext: "这是一个好搞笑的视频啊",
        videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/21671258",
        isLike: false,
        recommand: 10,
        commentNum: 66,
        transforNum: 100,
        type: 0
      },
      {
        publishId: 4,
        publishName: "老刘",
        publishImg: "/images/icon/defaultHead.png",
        publishTime: "2020/10/11",
        pinglunContext: "哎呦不错呦",
        // 动态的ID，随机数，唯一
        dynamicId: 4,
        // 动态来源
        dynamicFromId: "48266723",
        dynamicFromName: "自己",
        dynamicFromContext: "这是一个好搞笑的视asdas十八点和巴萨和本地化八四八的阿萨的便是地板频啊",
        videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/21671258",
        isLike: false,
        recommand: 10,
        commentNum: 66,
        transforNum: 1700,
        type: 1
      },
    ]
  },
  // 数据处理函数
  shuJuChuLi: function (sum) {
    // console.log(sum);
    if (sum > 10000) {
      sum = (Math.floor(((sum / 10000).toFixed(1)) * 10) / 10) + "w";
    }
    return sum;
  },
  // 返回
  goBack: function (e) {
    wx.navigateBack()
  },
  getZuoPinVideo: function (userId) {
    let that = this;
    http.getCurrentUserPublishVideoApi(userId, {
      data: {},
      success: res => {
        if (res.status == 0) {
          that.setData({
            showList: res.data
          })
        } else {
          wx.showToast({
            title: '请求失败',
            icon: "none"
          })
        }
      },
      fail: err => {
        wx.showModal({
          content: '网络请求失败',
        })
      }
    })
  },
  // 加载
  onLoad: function (options) {
    // 加载获取用户信息
    let that = this;
    const eventChannel = that.getOpenerEventChannel()
    eventChannel.on('acceptDataFromOpenerPage', function (data) {
      console.log(data.data);
      let userId = data.data.userId;
      that.getZuoPinVideo(userId);

      let user = data.data;
      that.setData({
        userId: user.userId,
        userInfoAddress: user.userInfoAddress,
        userInfoBirth: user.userInfoBirth,
        userInfoCollects: that.shuJuChuLi(user.userInfoCollects),
        userInfoEmail: user.userInfoEmail,
        userInfoFans: that.shuJuChuLi(user.userInfoFans),
        userInfoFocus: that.shuJuChuLi(user.userInfoFocus),
        userInfoGender: user.userInfoGender,
        userInfoImg: user.userInfoImg,
        userInfoIntro: user.userInfoIntro,
        userInfoName: user.userInfoName,
        userInfoPublish: that.shuJuChuLi(user.userInfoPublish),
        userInfoStar: that.shuJuChuLi(user.userInfoStar),
      })
      
    })
  },
  getAttention: function (e) {
    console.log("关注的人");
    let userId = this.data.userId;
    http.getAttentionApi(userId, 0, {
      data: {
        userId: userId,
        type: 0
      },
      success: res => {
        // console.log(res);
        let list = res.data
        wx.navigateTo({
          url: '/pages/fansAndAttention/fansAndAttention?id=0',
          success: function (res) {
            res.eventChannel.emit('acceptDataFromOpenerPage', { data: list })
          }
        })
      },
      fail: err => {
        wx.showModal({
          content: '接口还未完成',
        })
      }
    })
  },
  getFans: function (e) {
    console.log("粉丝");
    let userId = this.data.userId;
    http.getAttentionApi(userId, 1, {
      data: {
      },
      success: res => {
        console.log(res);
        console.log("获取成功");
        let list = res.data;
        wx.navigateTo({
          url: '/pages/fansAndAttention/fansAndAttention?id=1',
          success: function (res) {
            res.eventChannel.emit('acceptDataFromOpenerPage', { data: list })
          }
        })
      },
      fail: err => {
        wx.showModal({
          content: '接口还未完成',
        })
      }
    })
  },
  select: function (e) {
    this.setData({
      select: e.target.id
    })
    // let id = e.target.id;
    let userId = this.data.userId;
    let that = this;
    if (e.target.id == 1) {
      // 获取当前用户的作品
      http.getCurrentUserPublishVideoApi(userId, {
        data: {},
        success: res => {
          console.log(res);
          if (res.status == 0) {
            that.setData({
              showList: res.data
            })
          } else {
            wx.showToast({
              title: '请求失败',
              icon: "none"
            })
          }
        },
        fail: err => {
          wx.showModal({
            content: '网络请求失败',
          })
        }
      })
    } else {
      http.getCurrentUserCollectVideoApi(userId, {
        data: {},
        success: res => {
          console.log(res);
          if (res.status == 0) {
            that.setData({
              showList: res.data
            })
          } else {
            wx.showToast({
              title: '请求失败',
              icon: "none"
            })
          }
        },
        fail: err => {
          wx.showModal({
            content: '网络请求失败',
          })
        }
      })
    }
  },
  selectVideo: function (e) {
    // 获取视频ID,点击之后跳转视频播放界面
    let videoId = e.target.id
    // console.log(videoId);
    let userId = this.data.userId;
    http.getVideoInfoByVideoId(userId, videoId, {
      data: {},
      success: res => {
        if (res.status == 0) {
          let videoList = res.data;
          // console.log(videoList);
          // 改视频数据
          wx.navigateTo({
            url: '/pages/video/video',
            success: function (res) {
              res.eventChannel.emit('acceptDataFromOpenerPage', { data: videoList })
            }
          })
        } else {
          wx.showToast({
            title: '视频已经消失',
            icon: 'none',
            duration: 2000
          })
        }
      },
      fail: err => {
        wx.showToast({
          title: '网络请求失败',
          icon: "none"
        })
      }
    })
  },
  // 发布者
  publishUserImg: function (e) {
    let publishId = e.target.id;
    console.log("这是一个发布者:" + publishId);
    // 跳转到查询用户信息的界面
    http.getPublishInfo(publishId, {
      data: {},
      success: res => {
        if (res.status == 0) {
          console.log(res);
          let userInfo = res.data;
          // 跳转到查询用户信息的界面
          wx.navigateTo({
            url: '/pages/otherUserInfo/otherUserInfo',
            success: function (res) {
              res.eventChannel.emit('acceptDataFromOpenerPage', { data: userInfo })
            }
          })
        } else {
          wx.showModal({
            content: '获取信息失败',
          })
        }
      },
      fail: err => {
        wx.showModal({
          content: '网络连接失败',
        })
      }
    })
  },
  getDynamicUser: function (e) {
    console.log(e.target.id);
  },
  // 点赞功能
  dianZan: function (e) {
    // console.log(e.target.id);
    let index = e.target.id;
    // 点赞就修改数组中的数据
    let like = this.data.dynamicList[index].isLike;
    let recommand = this.data.dynamicList[index].recommand;
    let isLike = "dynamicList[" + index + "].isLike";
    let recd = "dynamicList[" + index + "].recommand"
    if (like == true) {
      recommand--;
      this.setData({
        [isLike]: false,
        [recd]: recommand
      })
    } else {
      recommand++;
      this.setData({
        [isLike]: true,
        [recd]: recommand
      })
    }
  },
  // 评论功能
  pinglun: function (e) {
    // console.log(e.target.id);
    let dynamicId = e.target.id;
    // console.log("评论功能");
    http.getDynamicCommentApi(dynamicId, {
      data: {},
      success: res => {
        console.log(res);
        console.log("成功");
      },
      fail: err => {
        wx.showModal({
          content: '网络请求失败',
        })
      }
    })
  },
  // 转发
  zhuanfa: function (e) {
    // console.log(e.target.id);
    let dynamicId = e.target.id;
    console.log("转发了");
  },
})