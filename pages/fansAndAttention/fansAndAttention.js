// pages/fansAndAttention/fansAndAttention.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    leftBackImg:"/images/icon/leftBack.png",
    title:"",
    attentionList:[
    ],
  },
  goBack:function(e){
    console.log("返回")
    wx.navigateBack();
  },
  getAttentionUserInfo:function(e){
    let userId = e.currentTarget.id;
    console.log(userId);
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let id = options.id;
    if(id == 0){
      this.setData({
        title:"关注"
      })
    }else{
      this.setData({
        title: "粉丝"
      })
    }
    let that = this;
    const eventChannel = this.getOpenerEventChannel()
    //获取发送到用户list集合
    eventChannel.on('acceptDataFromOpenerPage', function (data) {
      // console.log(data.data);
      that.setData({
        attentionList: data.data
      })
    })
  },

})