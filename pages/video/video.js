import http from '../../utils/api.js'
//获取应用实例
let userId = wx.getStorageSync('user').userId;
Page({
  data: {
    likeImg: "/images/icon/like1.png",
    unlikeImg: "/images/icon/unlike1.png",
    unlikeImg2: "/images/icon/unlike2.png",
    commentsImg: "/images/icon/comments.png",
    shareImg: "/images/icon/share.png",
    b_chaImg: "/images/icon/b_cha.png",
    sendImg: "/images/icon/send.png",
    headImg: "/images/headImg/fuluwa.jpg",
    addfansiImg: "/images/icon/addfansi2.png",
    videoInfo:{
      commentNum: 101,
      isLike: true,
      publishId: "6",
      publishImg: "/images/headImg/fuluwa.jpg",
      publisName: "张三",
      recommend: 100,
      title: "第一个上帝啊本身具备多级打算把第八十度半打碎对啊说不定大苏博蒂阿不思豆瓣豆瓣第八十奥数八十多巴哈是不断随便丢",
      transmission: 101,
      videoId: 0,
      videoUrl: "https://a.teqinju.com/uploads/xcx_ht/XbtujMFvgRu2XT99FkeVe7cRPKXYwbridPogg7jG.mp4",
    },
    // 评论列表
    commentsList: [
      {
        userId: "1",
        commentId: "1",
        commentUserName: "路飞",
        commentContent: "不会吧，不会吧，就这？",
        commentUserImg: "/images/headImg/fuluwa.jpg",
        comment_isLike: true,
        commentRecommend: 110,
        commentTime: "今天",
      },
      {
        userId: "2",
        commentId: "2",
        commentUserName: "女帝",
        commentContent: "女帝好看",
        commentUserImg: "/images/headImg/fuluwa.jpg",
        comment_isLike: true,
        commentRecommend: 1990,
        commentTime: "今天",
      }],
    // 关注列表
    attentionList: [],
    // 自动播放
    autoplay: true,
    //是否循环播放发
    loop: true,
    //是否开启控制进度手势
    enableProgressGesture: true,
    //是否显示中间的暂停按钮
    showCenterPlayBtn: true,
    //双击暂停视频
    enablePlayGesture: true,
    //设置播放的时间位置
    initialTime: 0,
    // 是否正在评论
    isComment: false,
    // 评论内容
    commentInput: "",
    // 评论数量
    comment_num: 0,
    // 喜欢的数量
    likeNum:1,
    isLike:false,
    fansIndex:-1,
  },
  // 查看用户信息
  lookUserInfo: function (e) {
    let userId = e.target.id;
    http.getPublishInfo(userId, {
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
  fansPanDuan: function (currentPublishId) {
    // 判断是不是关注的人
    let fansiIndex = this.data.attentionList.indexOf(currentPublishId);
    this.setData({
      fansiIndex: fansiIndex
    })
  },
  addfansi: function (e) {
    console.log("添加关注了");
    // 获取发布者ID
    let publishId = e.target.id;
    // 获取关注数组
    let list = this.data.attentionList;
    // console.log(publishId);
    let that = this;
    // 请求
    http.becomeFansiApi(userId, publishId, {
      data: {},
      success: res => {
        console.log(res);
        if (res.status == 0) {
          // 关注成功
          list.push(publishId);
          that.setData({
            attentionList: list
          })
          // 修改是否粉丝的判断
          that.fansPanDuan(publishId);
        } else {
          wx.showToast({
            title: '关注失败',
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
  like: function (e) {
    let recommend = this.data.videoInfo.recommend;
    if (this.data.videoInfo.isLike == false) {
      recommend++;
      this.setData({
        likeNum: recommend
      })
    }
    this.setData({
      isLike: true,
      likeNum: recommend
    })
  },
  unlike: function (e) {
    let recommend = this.data.videoInfo.recommend;
    // 如果是喜欢的就要减一
    if(this.data.videoInfo.isLike == true){
      recommend -- ;
      this.setData({
        likeNum: recommend
      })
    }
    this.setData({
      isLike: false,
      likeNum: recommend
    })
  },
  toComment: function (e) {
    //获取评论
    this.getComments();
    this.setData({
      isComment: true,
    })
  },
  // 获取评论内容
  getComments: function (e) {
    let videoId = this.data.videoInfo.videoId;
    // console.log(videoId);
    let that = this;
    http.getCommentApi(userId,videoId, {
      data: {},
      success: res => {
        // console.log(res);
        if(res.status == 0){
          let list = res.data;
          let legth = list.length;
          that.setData({
            commentsList: list,
            comment_num: legth
          })
        }else{
          wx.showToast({
            title: '网络延迟',
            icon:"none"
          })
        }
      },
      fail: err => {
        wx.showModal({
          content: '失败',
        })
      }
    })
  },
  reload: function (e) {
    wx.showModal({
      content: '前端太累了,不想转发!!!',
    })
  },
  quxiaoCom: function (e) {
    this.setData({
      isComment: false
    })
  },
  sayCommentInput: function (e) {
    this.setData({
      commentInput: e.detail.value
    })
    if (e.detail.value == "") {
      this.setData({
        sendImg: "/images/icon/send.png",
      })
    } else {
      this.setData({
        sendImg: "/images/icon/send1.png",
      })
    }
  },
  sendComment: function (e) {
    let that = this;
    if (this.data.commentInput == "") {
      console.log("评论不要为空");
    } else {
      let commentText = this.data.commentInput;
      wx.showLoading({
        title: "正在发表中",
        mask: true,
      });
      // 获取视频ID
      let videoId = that.data.videoInfo.videoId; 
      //发表评论请求
      http.addCommentApi(videoId,userId,{
        data: {
          content: commentText,
        },
        success: res => {
          wx.hideLoading();
          //发表之后重新获取信息
          that.getComments();
          // 当前视频的评论数量
          that.setData({
            commentInput: "",
            isComment: false
          })
          wx.showToast({
            title: '评论成功',
            commentInput: ""
          });
        },
        fail: err => {
          wx.hideLoading();
          wx.showModal({
            content: '网络太慢，请稍后发表评论',
          })
        }
      })
    }
  },
  toLikeComments: function (commentId, type) {
    if (type == 0) {
      // console.log("喜欢");
      http.isLoveCommentsApi(userId, commentId, {
        data: {},
        success: res => {
          console.log(res);
        },
        fail: err => {
          wx.showModal({
            content: '网络请求失败',
          })
        }
      })
    } else {
      // console.log("不喜欢")
      http.noLoveCommentsApi(userId, commentId, {
        data: {},
        success: res => {
          console.log(res);
        },
        fail: err => {
          wx.showModal({
            content: '网络请求失败',
          })
        }
      })
    }
  },
  toLike: function (e) {
    // 取消点赞或者点赞
    let index = e.target.id;
    // console.log(index);
    // 获取评论ID
    let commentId = this.data.commentsList[index].commentId;
    // 修改是否喜欢
    let isLove = "commentsList[" + index + "].comment_isLike";
    // 修改点赞数量
    let commentRecommend = "commentsList[" + index + "].commentRecommend";
    let num = this.data.commentsList[index].commentRecommend;
    if (this.data.commentsList[index].comment_isLike == true) {
      num--;
      this.setData({
        [isLove]: false,
        [commentRecommend]: num
      })
      this.toLikeComments(commentId, 1);
    } else {
      num++;
      this.setData({
        [isLove]: true,
        [commentRecommend]: num
      })
      this.toLikeComments(commentId, 0);
    }
  },
  goBack:function(e){
    wx.navigateBack();
  },
  loveOrNo:function(e,id){
    // console.log(id);
    let videoId = this.data.videoInfo.videoId;
    http.addZanApi(userId,videoId,id,{
      data:{},
      success:res=>{
        if(res.data == 0){
          console.log(res);
        }
      },
      fail:err=>{
        wx.showModal({
          content: '网络请求失败',
        })
      }
    })
  },
  // 获取关注列表
  getAttentionAuther: function (e) {
    let that = this;
    http.getAttentionApi(userId, 0, {
      data: {},
      success: res => {
        let list = res.data;
        let newList = [];
        for (var i = 0; i < list.length; i++) {
          newList.push(list[i].userId);
        }
        that.setData({
          attentionList: newList
        })
        let publishId = that.data.videoInfo.publishId;
        // console.log("视频发布者ID:" + publishId);
        that.fansPanDuan(publishId);
      }
    })
  },
  // 加载使用
  onLoad: function (e) {
    // 加载获取关注列表
    let that = this;
    const eventChannel = this.getOpenerEventChannel()
    eventChannel.on('acceptDataFromOpenerPage', function (e) {
      let videoInfo = e.data;
      // console.log(videoInfo);
      // 加载时获取视频并且设置数据
      that.setData({
        videoInfo: videoInfo,
        isLike: videoInfo.isLike,
        likeNum: videoInfo.recommend
      })
      that.getAttentionAuther();
      
    })
    that.getComments();
  },
  // 离开页面时触发
  onUnload:function(e){
    let isLike = this.data.isLike;
    if(isLike == true){
      this.loveOrNo(e,0);
    }else{
      this.loveOrNo(e,1);
    }
  }
})