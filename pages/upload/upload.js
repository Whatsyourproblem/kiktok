// pages/upload/upload.js
import http from '../../utils/api.js'
Page({
  /**
   * 页面的初始数据
   */
  data: {
    uploadImg:"/images/icon/upload2.png",
    titleImg:"/images/icon/title.png",
    typeImg:"/images/icon/type.png",
    downImg:"/images/icon/down.png",
    tagsImg:"/images/icon/tags.png",
    chaImg:"/images/icon/b_cha.png",
    // 显示标签选择
    showTag:false,
    isShowSelect: false,
    // 视频URL
    videoUrl:"",
    titleValue:"",
    flag:0,
    tagList:[
      {
        id:"1",
        title:"搞笑"
      },
      {
        id: "2",
        title: "文艺"
      },
      {
        id: "3",
        title: "科技"
      },
      {
        id: "4",
        title: "风景"
      },
      {
        id: "5",
        title: "生活"
      },
      {
        id: "6",
        title: "美食"
      },
      {
        id: "7",
        title: "惊悚"
      },
      {
        id: "8",
        title: "恐怖"
      },
      {
        id: "9",
        title: "科幻"
      },
      {
        id: "10",
        title: "悬疑"
      },

    ],
    selectTagList:[
    ],
  },
  upVideo:function(e){
    let that = this;
    wx.chooseVideo({
      sourceType: ['album'],
      maxDuration: 60,
      success(res) {
        // console.log(res);
        //选定视频的临时文件路径
        let tempFilePath = res.tempFilePath;
        // 获取大小
        let size = res.size;
        //设置视频大小限制100Mb
        let sizeMust = parseFloat(res.size / 1024 / 1024).toFixed(1);
        // 判断视频大小
        if (parseFloat(sizeMust) > 20){
          wx.showModal({
            content: '上传的视频大小超限，请重新上传，限制在20Mb大小',
          })
        }else{
          // 开始上传
          // console.log("sss");
          that.setData({
            videoUrl: tempFilePath
          })
        }
      }
    })
  },
  deleteUpVideo:function(e){
    this.setData({
      videoUrl:""
    })
  },
  getInput:function(e){
    this.setData({
      titleValue:e.detail.value
    })
  },
  showTag:function(e){
    if(this.data.showTag == false){
      this.setData({
        showTag: true,
        downImg:"/images/icon/up.png"
      })
    }else{
      this.setData({
        showTag: false,
        downImg: "/images/icon/down.png"
      })
    }
  },
  typeInput:function(e){
    this.setData({
      tagText: e.detail.value
    })
  },
  addTag:function(e){
    
    if(this.data.flag > 2){
      wx.showModal({
        content: "最多只能添加三个标签"
      })
      this.setData({
        showTag: false,
        downImg: "/images/icon/down.png"
      })
    }else{
      // flag++;
      // 获取当前选中的index
      let i = e.target.id;
      // console.log(i);
      //获取当前选中的标签的内容的id
      let id = this.data.tagList[i].id;
      // 获取标签数组
      var list = this.data.tagList;
      // 获取选中的数组
      let seList = this.data.selectTagList;
      //获取标题
      let title = this.data.tagList[i].title;
      // console.log(title);
      // 从数组中删除选中的标签
      list.splice(i, 1);
      // 创建新的数组
      let newList = [{ id, title }];
      // 数组添加数据
      seList.push.apply(seList, newList);
      // 最多支持添加三个标签
      let flag = this.data.flag;
      flag++;
      this.setData({
        tagList: list,
        selectTagList: seList,
        isShowSelect:true,
        flag:flag
      })
    }
  },
  deleteTag:function(e){
    //当前选中的索引
    let sTid = e.target.id;
    // 根据索引获取内容
    let id = this.data.selectTagList[sTid].id;
    let title = this.data.selectTagList[sTid].title;
    // console.log(id+":"+title);
    // 创建新的数据添加到原本可选择标签的数组中
    let newList = [{ id , title}];
    let oldList = this.data.tagList;
    // console.log(newList);
    // 老数组添加一个新的数组
    oldList.push.apply(oldList,newList);
    // console.log(oldList);
    //从当前数组删除这个标签，然后添加到选择的数组中
    var currentList = this.data.selectTagList;
    //根据id删除当前选中的
    currentList.splice(sTid, 1);
    // console.log(currentList);
    let flag = this.data.flag;
    flag --;
    this.setData({
      tagList: oldList,
      selectTagList:currentList,
      flag:flag
    })
  },
  // 取消清空
  quxiao:function(e){
    this.setData({
      videoUrl:"",
      titleValue:"",
      selectTagList:[],
      flag:0,
      isShowSelect:false
    })
  },
  // 上传
  upStart:function(e){
    wx.showLoading({
      title: "正在上传中",
      mask: true,
    })
    // 视频路径
    let vidUrl = this.data.videoUrl;
    //获取标题
    let title = this.data.titleValue;
    // 选择的标签数组
    let selectTagList = this.data.selectTagList;
    let list = this.data.selectTagList;
    let str = "";
    for (var i = 0; i < list.length ;i++){
      console.log(list[i].title);
      str = list[i].title + "," + str
    }
    let userId = wx.getStorageSync('user').userId;
    let that = this;
    wx.uploadFile({
        // url: 'http://101.200.203.175:8081/api/video/upload/'+userId,
        // url: 'http://10.2.179.181:8081/api/video/upload/' + userId,
        url: http.uploadVideoUrl+userId,
        filePath: vidUrl,
        name: 'file',
        header: {},
        formData: {
          'title':title,
          'type': str
        },
        success: function(res) {
          //console.log(res);
          wx.hideLoading();
          wx.showToast({
            title:"上传成功",
            icon: 'success',
            duration:2000,
            mask:true
          })
          that.quxiao();
        },
        fail: function(res) {
          wx.hideLoading();
          wx.showModal({
            content: '上传视频失败',
          })
        },
    })
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // 暂时关闭
    // 如果没有缓存数据就跳转到登陆页面
    if(wx.getStorageSync('user') == ""){
      wx.showModal({
        content:"请先登陆使用该功能",
        success:function(res){
          if (res.confirm) {
            console.log('用户点击确定')
            wx.navigateTo({
              url: "/pages/login/login"
            })
          } else if (res.cancel) {
            wx.switchTab({
              url: "/pages/index/index"
            })
          }
        }
      })
    }
  },
})