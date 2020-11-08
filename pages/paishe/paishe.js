Page({

  /**
   * 页面的初始数据
   */
  data: {
    //设置摄像头朝向
    devicePosition:"back",
    flash: "off",
  },
  back:function(e){
    //返回上一页
    wx.switchTab({
      url: "/pages/index/index"
    })
  },
  fanzhuan:function(e){
    //console.log("反转");
    if(this.data.devicePosition == "front"){
      this.setData({
        devicePosition: "back"
      })
    }else{
      this.setData({
        devicePosition: "front"
      })
    }
    
  },
  getImageV:function(e){
    //调用相册
    console.log("调用相册");
  },
})