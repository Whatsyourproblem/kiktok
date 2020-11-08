import http from '../../utils/api.js'
let userId = wx.getStorageSync('user').userId;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    hotList: [{
        videoId: '1',
        title: "蔡徐坤喜欢打篮球",
        recommend: 100
      }, {
        videoId: '2',
        title: "蔡徐坤喜欢唱歌",
        recommend: 1000
      },
      {
        videoId: '3',
        title: "蔡徐坤喜欢跳舞",
        recommend: 50
      },
      {
        videoId: '4',
        title: "蔡徐坤喜欢RAP",
        recommend: 10
      }
    ],
    guestList: [{
        videoId: '1',
        title: '洛天依结婚了',
        recommend: 9999999
      },
      {
        videoId: '2',
        title: '初音结婚了',
        recommend: 777777
      },
      {
        videoId: '3',
        title: '洛天依离婚了',
        recommend: 55555
      },
      {
        videoId: '4',
        title: '初音也离婚了',
        recommend: 88888
      }
    ],
    historyList: [{
        videoId: '0',
        title: '完美世界'
      }, {
        videoId: '1',
        title: '圣墟',
      },
      {
        videoId: '2',
        title: '遮天',
      }, {
        videoId: '3',
        title: '最爱喝兽奶',
      },
      {
        videoId: '4',
        title: '我吃西红柿',
      },
      {
        videoId: '5',
        title: '辰东长绿毛了',
      },
      {
        videoId: '6',
        title: '哇哈哈哈哈好喝',
      },
      {
        videoId: '7',
        title: '五类哇梦琪D路飞',
      },
    ],
    // 选中热点榜
    select: 1,
    videoList: [],
    showHistory: false,
    hasHistory: true,
    searchValue: "哇哈哈哈",
    showText: '全部搜索记录',
    // 是否有视频
    searchVideo: false,
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
    enablePlayGesture: true,
    //是否开启控制进度的手势
    enableProgressGesture: false,
  },
  w_back: function(e) {
    wx.switchTab({
      url: "/pages/index/index"
    })
  },
  searchInput: function(e) {
    //console.log(e.detail.value);
    this.setData({
      searchValue: e.detail.value
    })
  },
  searchHistory: function(e) {
    let id;
    // 获取标题
    let title = this.data.searchValue;

    if (this.data.historyList == null) {
      id = 0;
      let newObj = [{
        videoId: "" + id + "",
        title: title
      }]
      this.setData({
        historyList: newObj,
        hasHistory: true
      })
      // console.log(id);
      wx.setStorageSync("historyList", newObj);
    } else {
      id = this.data.historyList.length;
      let newObj = [{
        videoId: "" + id + "",
        title: title
      }]
      // 创建新的对象
      let oldlist = this.data.historyList;
      // 向搜索历史添加数据
      oldlist.push.apply(oldlist, newObj);
      this.setData({
        historyList: oldlist,
        hasHistory: true
      })
      wx.setStorageSync("historyList", oldlist);
    }
  },
  sessionGetHistory: function(e) {
    let list = wx.getStorageSync('historyList');
    // console.log(list)
    if(list == ""){
      this.setData({
        hasHistory:false
      })
    }else{
      this.setData({
        historyList: list
      })
    }
  },
  search: function(e,title) {
    // console.log(title);
    let searchInfo
    if(title == null){
      //获取搜索输入的内容，之后进行查询
      searchInfo = this.data.searchValue;
      if(searchInfo == ""){
        wx.showModal({
          content: '搜搜不能为空',
        })
        return;
      }
    }else{
      searchInfo = title;
    }
    // console.log(searchInfo);
    // 添加搜索历史
    this.searchHistory();
    let that = this;
    http.searchApi(searchInfo, {
      data: {},
      success: res => {
        if (res.status == 0) {
          let list = res.data;
          if (list == null) {
            wx.showToast({
              title: '没有搜索数据',
              icon: 'none',
              duration: 2000
            })
          } else {
            that.setData({
              videoList: list,
              searchVideo: true,
              // 改成取消
              showHistory:true
            })
            // 搜索之后加载视频数据
            that.loadVideo();
          }
        } else {
          wx.showModal({
            content: "网络异常"
          })
        }
      },
      fail: err => {
        wx.showModal({
          content: "网络请求失败"
        })
      }
    })
  },
  // 渲染搜索出来的数据
  loadVideo: function(e) {
    console.log(this.data.videoList);
  },
  // 根据搜索历史获取视频
  selectHistoryValue:function(e){
    let title = e.currentTarget.id;
    // console.log(e);
    this.search(e,title);
  },
  // 根据ID获取视频
  selectVideo: function(e) {
    let videoId = e.currentTarget.id;
    //获取视频之后将视频发送
    let that = this;

    // 测试跳转
    // wx.navigateTo({
    //   url: '/pages/video/video',
    // })

    http.getVideoInfoByVideoId(userId,videoId, {
      data: {},
      success: res => {
        if (res.status == 0) {
          let videoList = res.data;
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
          icon:"none"
        })
      }
    })
  },
  //清空输入框
  clearInput: function(e) {
    this.setData({
      searchValue: "",
      searchVideo:false
    })
  },
  noSearch: function(e) {
    //console.log("取消");
    this.setData({
      showHistory: false,
      showText: "全部搜索记录",
      searchVideo: false,
    })
  },
  //删除单条搜索记录
  deleteHistory: function(e) {
    // console.log("删除单条数据")
    console.log(e.target.id);
    var list = this.data.historyList;
    list.splice(e.target.id, 1);
    this.setData({
      historyList: list
    })
    // 本存
    wx.setStorageSync("historyList", list);
  },
  //获取全部搜索记录
  getAll: function(e) {
    //console.log("全部历史");
    if (this.data.showHistory == false) {
      this.setData({
        showHistory: true,
        showText: "清除全部搜索记录"
      })
    } else {
      this.setData({
        showHistory: false,
        showText: "全部搜索记录"
      })
    }
  },
  clearAll: function(e) {
    var that = this;
    //console.log("清空所有搜索记录");
    wx.showModal({
      title: '清空搜索记录',
      content: "您是否要清空所有搜索记录？",
      success(res) {
        //如果点击确定
        if (res.confirm) {
          that.setData({
            historyList: null,
            hasHistory: false,
            showHistory: false,
            showText: "全部搜索记录"
          })
          wx.removeStorage({
            key: 'historyList',
            success(res) {
              console.log("清除完成")
            }
          })
        }
      }
    })
  },
  select: function(e) {
    let select = e.target.id;
    this.setData({
      select: e.target.id
    })
    if (select == '1') {
      this.getVideoTypeById(1);
    } else if (select == '2') {
      this.getVideoTypeById(2);
    }
  },
  getVideoTypeById: function(id) {
    let that = this;
    if (id == 1) {
      http.getHotVideoApi({
        data: {},
        success: res => {
          if (res.status == 0) {
            // console.log(res);
            // 获取视频集合
            let list = res.data;
            for (var i = 0; i < list.length; i++) {
              // console.log(list[i]);
              if (list[i].recommend > 10000) {
                list[i].recommend = (Math.floor(((list[i].recommend / 10000).toFixed(1)) * 10) / 10) + "w";
              }
            }
            that.setData({
              hotList: list,
              searchValue: res.data[0].title
            })
          } else {
            wx.showToast({
              title: '获取失败',
              icon:"none"
            })
          }
        },
        fail: err => {
          wx.showModal({
            content: '网络请求失败',
          })
        }
      })
    } else if (id == 2) {
      http.getGuestVideoApi({
        data: {},
        success: res => {
          if (res.status == 0) {
            let list = res.data;
            for (var i = 0; i < list.length; i++) {
              if (list[i].recommend > 10000) {
                list[i].recommend = (Math.floor(((list[i].recommend / 10000).toFixed(1)) * 10) / 10) + "w";
              }
            }
            that.setData({
              guestList: res.data,
              searchValue: res.data[0].title
            })
          } else {
            wx.showToast({
              title: '获取失败',
              icon:"none"
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
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    // 页面打开就显示热点榜
    this.getVideoTypeById(1);
    // 获取搜索历史记录
    this.sessionGetHistory();
  },
})