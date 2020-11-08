import http from '../../utils/api.js'
//获取应用实例
const app = getApp()
let userId = wx.getStorageSync('user').userId;
Page({
  data: {
    //屏幕高度
    screenHeight: app.screenHeight,
    unlikeImg:"/images/icon/unlike1.png",
    unlikeImg2: "/images/icon/unlike2.png",
    likeImg:"/images/icon/like1.png",
    commentsImg:"/images/icon/comments.png",
    shareImg:"/images/icon/share.png",
    b_chaImg:"/images/icon/b_cha.png",
    sendImg:"/images/icon/send.png",
    headImg:"/images/headImg/fuluwa.jpg",
    addfansiImg:"/images/icon/addfansi2.png",
    videoList:[{
      videoId:0,
      title:"第一个上帝啊本身具备多级打算把第八十度半打碎对啊说不定大苏博蒂阿不思豆瓣豆瓣第八十奥数八十多巴哈是不断随便丢大师级的巴士迪拜是大厦的变化萨巴赫的吧还是",
      isLike:true,
      publishImg:"/images/headImg/fuluwa.jpg",
      publishId:"6",
      publisName:"张三",
      recommend:100,
      commentNum: 101,
      transmission: 101,
videoUrl:"https://a.teqinju.com/uploads/xcx_ht/XbtujMFvgRu2XT99FkeVe7cRPKXYwbridPogg7jG.mp4"
    },
    {
      videoId: 1,
      title: "第二个",
      isLike: false,
      publishImg: "/images/headImg/fuluwa.jpg",
      publishId: "66",
      publisName: "李六",
      recommend: 101,
      commentNum: 101,
      transmission: 101,
      videoUrl: "https://a.teqinju.com/uploads/xcx_ht/XbtujMFvgRu2XT99FkeVe7cRPKXYwbridPogg7jG.mp4"
    },
    {
      videoId: 2,
      title: "第三个",
      isLike: true,
      publishImg: "/images/headImg/fuluwa.jpg",
      publishId: "666",
      publisName: "李四",
      recommend: 110,
      commentNum: 101,
      transmission: 101,
      videoUrl: "https://a.teqinju.com/uploads/xcx_ht/XbtujMFvgRu2XT99FkeVe7cRPKXYwbridPogg7jG.mp4"
    },
    {
      videoId: 3,
      title: "第四个",
      isLike: false,
      publishImg: "/images/headImg/fuluwa.jpg",
      publishId: "6666",
      publisName: "王五",
      recommend: 666,
      commentNum: 101,
      transmission: 101,
      videoUrl: "http://qii7ny2oj.hn-bkt.clouddn.com/85677481"
    }],
    // 评论列表
    commentsList:[
      {
        userId: "1",
        commentId:"1",
        commentUserName:"路飞",
        commentContent:"不会吧，不会吧，就这？",
        commentUserImg:"/images/headImg/fuluwa.jpg",
        comment_isLike:true,
        commentRecommend:110,
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
      },
      {
        userId: "3",
        commentId: "3",
        commentUserName: "山治",
        commentContent: "西路麓麓",
        commentUserImg: "/images/headImg/fuluwa.jpg",
        comment_isLike: false,
        commentRecommend: 666,
        commentTime: "今天",
      },
      {
        userId: "4",
        commentId: "4",
        commentUserName: "索隆",
        commentContent: "噫嘘唏，微胡搞在",
        commentUserImg: "/images/headImg/fuluwa.jpg",
        comment_isLike: false,
        commentRecommend: 333,
        commentTime: "今天",
      },
      {
        userId: "5",
        commentId: "5",
        commentUserName: "娜美小姐",
        commentContent: "呦呦呦",
        commentUserImg: "/images/headImg/fuluwa.jpg",
        comment_isLike: false,
        commentRecommend: 532,
        commentTime: "今天",
      }, {
        userId: "6",
        commentId: "6",
        commentUserName: "Baby Five",
        commentContent: "哈哈哈哈",
        commentUserImg: "/images/headImg/fuluwa.jpg",
        comment_isLike: true,
        commentRecommend: 9238,
        commentTime: "今天",
      },
      {
        userId: "7",
        commentId: "7",
        commentUserName: "乔巴",
        commentContent: "这是评论？？？",
        commentUserImg: "/images/headImg/fuluwa.jpg",
        comment_isLike: false,
        commentRecommend: 2344,
        commentTime: "今天",
      },
    ],
    attentionList:[],
    // 自动播放
    autoplay:true,
    //是否循环播放发
    loop:true,
    //是否开启控制进度手势
    enableProgressGesture:true,
    //是否显示中间的暂停按钮
    showCenterPlayBtn:true,
    //双击暂停视频
    enablePlayGesture:true,
    //设置播放的时间位置
    initialTime:0,
    //index
    changeIndex:0,
    // 是否正在评论
    isComment:false,
    // 评论内容
    commentInput:"",
    // 是否是粉丝
    isFansi:false,
    // 评论数量
    comment_num: 0,
    curredtVideoIndex:0,
    // 当前视频的ID
    currentVideoId:"",
    // 当前是不是喜欢
    currentIsLike:false,
    // 上一个点赞与否的视频Id
    lastVideoId:"",
    // 上一个视频的喜欢与否
    lastIsLike:-1,
    // 判断是不是粉丝
    fansiIndex:-1,
  },

  paizhao:function(res){
    wx.navigateTo({
      url:"/pages/paishe/paishe"
    })
  },
  search:function(res){
    wx.navigateTo({
      url: "/pages/search/search"
    })
  },
  defindVideoIsLove(){
    let lastVideoId = this.data.lastVideoId;
    let lastIsLike = this.data.lastIsLike;
    let obj = {
      "lastVideoId": lastVideoId,
      "lastIsLike": lastIsLike
    }
    return obj;
  },
  // 页面切换时触发方法
  toLoveOrNo:function(obj){
    let videoId = obj.lastVideoId;
    let isLike = obj.lastIsLike;
    if(isLike == 0){
      // console.log("是正确的爱")
      // 请求
      http.addZanApi(userId, videoId, 0,{
        data:{},
        success:res=>{
          console.log(res);
        },
        fail:err=>{
          console.log("网络请求失败")
        }
      })
    } else if (isLike == 1){
      // console.log("是错误的爱")
      http.addZanApi(userId, videoId, 1, {
        data: {},
        success: res => {
          console.log(res);
        },
        fail: err => {
          console.log("网络请求失败")
        }
      })
    }
  },
  move(e) {
    // 联合方法发喜欢与否的请求
    this.toLoveOrNo(this.defindVideoIsLove());
    // console.log(this.defindVideoIsLove());

    // 获取当前的索引值
    let currentIndex = e.detail.current;
    //根据索引值获取当前视频的id
    let currentVideoId = this.data.videoList[currentIndex].videoId;
    // 根据索引值获得当前视频发布者的id
    let publishId = this.data.videoList[currentIndex].publishId;
    // 判断是否关注了
    this.fansPanDuan(publishId);

    // 根据索引获取当前视频是否喜欢
    let isLike = this.data.videoList[currentIndex].isLike;

    // console.log("索引:"+ currentIndex);
    // console.log(currentVideoId);
    // console.log(isLike);

    //获取当前index
    this.setData({
      changeIndex: e.detail.current,
      curredtVideoIndex: currentIndex,
      currentVideoId: currentVideoId,
      lastIsLike:-1,
      lastVideoId: currentVideoId
    })

    // 如果是最后一个视频，提示没有视频了
    if (currentIndex + 1  == this.data.videoList.length){
      console.log("这是最后一个视频了");
      wx.showToast({
        title: '抱歉,没有更多的视频了',
        icon:"none"
      })
    }
  },
  // 查看发布视频用户的信息
  lookUserInfo:function(e){
    // 获取传递的用户ID
    let userId = e.target.id;
    http.getPublishInfo(userId,{
      data:{},
      success:res=>{
        if(res.status == 0){
          console.log(res);
          let userInfo = res.data;
          // 跳转到查询用户信息的界面
          wx.navigateTo({
            url: '/pages/otherUserInfo/otherUserInfo',
            success: function (res) {
              res.eventChannel.emit('acceptDataFromOpenerPage', { data: userInfo })
            }
          })
        }else{
          wx.showModal({
            content: '获取信息失败',
          })
        }
      },
      fail:err=>{
        wx.showModal({
          content: '网络连接失败',
        })
      }
    })
  },
  addfansi:function(e){
    // console.log("添加关注了");
    // 发布者的Id
    let publishUserId = e.target.id;
    // 数组添加数据
    let list = this.data.attentionList;
    
    console.log(this.data.attentionList);
    // 发送添加请求
    let that = this;
    http.becomeFansiApi(userId,publishUserId,{
      data:{},
      success:res=>{
        console.log(res);
        if(res.status == 0){
          // 关注成功
          list.push(publishUserId); 
          that.setData({
            attentionList: list
          })
          that.fansPanDuan(publishUserId);
        }else{
          wx.showToast({
            title: '关注失败',
            icon:"none"
          })
        }
      },
      fail:err=>{
        wx.showModal({
          content: '网络请求失败',
        })
      }
    })
  },
  // 不喜欢变成喜欢
  like:function(e){
    let that = this;
    // 获取视频列表的索引
    let videoIndex = e.target.id;
    // 获取当前视频的ID
    let videoId = this.data.currentVideoId;
    // console.log(videoIndex);

    // 修改视频集合中的喜欢与否
    let list = "videoList[" + videoIndex + "].isLike";
    // 修改集合中的点赞量
    let num = "videoList[" + videoIndex + "].recommend";
    let sum = that.data.videoList[videoIndex].recommend;
    sum++;
    // console.log(sum);
    that.setData({
      [list]: true,
      [num]: sum,
      lastVideoId: videoId,
      lastIsLike: 0
    })
  },
  // 喜欢变成不喜欢
  unlike:function(e){
    let that = this;
    let videoIndex = e.target.id;
    // 获取视频列表的索引
    
    // 获取当前视频的ID
    let videoId = this.data.currentVideoId;
  
    let list = "videoList[" + videoIndex + "].isLike";
    let num = "videoList[" + videoIndex + "].recommend";
    let sum = that.data.videoList[videoIndex].recommend;
    // 直接修改数组了,可以 -- 
    sum--;
    console.log(sum);
    that.setData({
      [list]: false,
      [num]: sum,
      lastVideoId: videoId,
      lastIsLike:1
    })
  },
  toComment:function(e){
    //获取评论
    this.getComments();
    // 获取评论数量
    this.getCommentNum();
    this.setData({
      isComment: true,
    })
  },
  // 获取评论内容
  getComments: function (e) {
    // 获取视频ID,并根据视频id获取评论内容
    let videoId = this.data.currentVideoId;
    // console.log(videoId);
    http.getCommentApi(userId,videoId, {
      data: {},
      success: res => {
        // 成功获取评论
        // console.log(res);
        let list = res.data;
        this.setData({
          commentsList: list
        })
      },
      fail: err => {
        wx.showModal({
          content: '失败',
        })
      }
    })
  },
  // 重新设置评论数量
  getCommentNum: function (e) {
    let num = this.data.commentsList.length;
    this.setData({
      comment_num: num
    })
  },
  // 转发功能
  reload:function(e){
    wx.showModal({
      content: '前端太累了,不想转发!!!',
    })
    // console.log("转发");
  },
  // 取消正在评论的面板
  quxiaoCom:function(e){
    this.setData({
      isComment:false
    })
  },
  sayCommentInput:function(e){
    this.setData({
      commentInput: e.detail.value
    })
    if (e.detail.value == ""){
      this.setData({
        sendImg: "/images/icon/send.png",
      })
    }else{
      this.setData({
        sendImg: "/images/icon/send1.png",
      })
    }
  },
  sendComment:function(e){
    let videoId = this.data.currentVideoId;
    let that = this;
    if(this.data.commentInput == ""){
      wx.showToast({
        title: '评论内容不可为空',
      })
    }else{
      let commentText = this.data.commentInput;
      wx.showLoading({
        title: "正在发表中",
        mask: true,
      });
      //发表评论请求
      http.addCommentApi(videoId,userId,{
        data: {
          content:commentText,
        },
        success: res => {
          wx.hideLoading();
          // console.log(res);
          //发表之后重新获取信息
          this.getComments();
          // 重新获取评论数量
          // 修改下面的数字
          this.getCommentNum();
          // 当前视频索引
          let curredtVideoIndex = that.data.curredtVideoIndex;
          let num = "videoList[" + curredtVideoIndex + "].commentNum";
          let sum = that.data.videoList[curredtVideoIndex].commentNum;
          sum ++ ;
          that.setData({
            [num]:sum,
            comment_num:sum,
            commentInput:"",
            isComment:false
          })
          wx.showToast({
            title: '评论成功',
            commentInput:""
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
  toLikeComments: function (commentId,type){
    // console.log(type);
    // console.log(commentId);
    if(type == 0){
      // console.log("喜欢");
      http.isLoveCommentsApi(userId, commentId,{
        data:{},
        success:res=>{
          console.log(res);
        },
        fail:err=>{
          wx.showModal({
            content: '网络请求失败',
          })
        }
      })
    }else{
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
  // 评论点赞
  toLike:function(e){
    // 取消点赞或者点赞
    let index = e.target.id;
    // 根据索引获取评论的id
    let commentId = this.data.commentsList[index].commentId;

    // console.log(commentId);
    // 修改是否喜欢
    let isLove = "commentsList[" + index +"].commentIsLike";
    // 修改点赞数量
    let commentRecommend = "commentsList[" + index + "].commentRecommend";
    let num = this.data.commentsList[index].commentRecommend;
    // 如果喜欢改成不喜欢
    if (this.data.commentsList[index].commentIsLike == true){
      num --;
      this.setData({
        [isLove]:false,
        [commentRecommend]: num
      })
      this.toLikeComments(commentId,1);
    }else{
      num ++;
      this.setData({
        [isLove]: true,
        [commentRecommend]: num
      })
      this.toLikeComments(commentId,0);
    }
  },
  // 获取新的视频列表
  getNewVideoList: function (e) {
    let that = this;
    http.getNewVideoListApi(userId,{
      data: {},
      success: res => {
        // console.log(res);
        // 设置全局变量视频id
        if(res.status == 0){
          // 加载时就获取当前视频的ID值
          let videoId = res.data[0].videoId;
          // console.log(videoId);
          let currentIsLike = res.data[0].isLike;
          // console.log("当前是否喜欢:"+currentIsLike);
          // 设置视频列表
          this.setData({
            videoList: res.data,
            currentVideoId:videoId,
            currentIsLike: currentIsLike,
            lastIsLike: -1,
            lastVideoId: videoId
          })
          this.fansPanDuan("");
        }else{
          wx.showModal({
            content: '网络异常'
          })
        }
      },
      fail: err => {
        wx.showModal({
          content: '获取视频接口暂未完成'
        })
      }
    })
  },
  getAttentionAuther:function(e){
    let that = this;
    http.getAttentionApi(userId,0,{
      data:{},
      success:res=>{
        // console.log(res);
        // console.log(res.data);
        let list = res.data;
        let newList = [];
        for(var i = 0 ;i < list.length ;i++){
          newList.push(list[i].userId);
        }
        that.setData({
          attentionList:newList
        })
      }
    })
  },
  fansPanDuan:function(currentPublishId){
    if(currentPublishId == ""){
      // 如果没有传递参数,就是第一个视频
      // 获取当前第一个视频的发布者Id
      currentPublishId = this.data.videoList[0].publishId;
    }
    // 判断是不是关注的人
    let fansiIndex = this.data.attentionList.indexOf(currentPublishId);
    this.setData({
      fansiIndex: fansiIndex
    })
  },
  onLoad:function(e){
    this.getNewVideoList();
    if (wx.getStorageSync('user') != ""){
      this.getAttentionAuther();
    }
  },
})
