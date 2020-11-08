// pages/updateUserInfo/updateUserInfo.js
import http from '../../utils/api.js'
Page({
  data: {
    leftBackImg: "/images/icon/WLeftBack.png",
    title:"编辑资料",
    userImg:"/images/headImg/fuluwa.jpg",
    addImg:"/images/icon/addImg.png",
    rightOpImg:"/images/icon/rightOp.png",
    clearImg:"/images/icon/cha.png",
    currentName:"",
    currentID:"",
    currentIntroductory:"",
    currentSex:"",
    currentBirthday:"",
    currentArea:"",
    currentEmail:"",
    updateView:false,
    updateTitle:"我的名字",
    updateDefaultContenxt:"名字就将计就计",
    placeholderText:"记得填写名字",
    maxNum:20,
    remianNum:12,
    inputId:"12",
    index:"0",
    array: ['男', '女', '不显示'],
    date: '2016-09-01',
    currentTime:"",
  },
  // 修改头像
  updateHeadImg:function(e){
    let that = this;
    let userId = that.data.currentID;
    wx.chooseImage({
      count:1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album'],
      success(res)  {
        // 获取本地路径
        const tempFilePaths = res.tempFilePaths[0];
        // 将图像上传服务器获取图片地址从而修改
        // console.log(tempFilePaths);
        wx.showLoading({
          title: '正在上传中',
        })
        that.uploadImg(tempFilePaths);
      }
    })
  },
  uploadImg: function (path) {
    let userId = this.data.currentID;
    let that = this;
    
    //that.updateUserInfoById();
    // 上传图片
    wx.uploadFile({
      url: http.uploadUserImg + userId,
      filePath: path,
      name: 'file',
      header: {},
      success: function (res) {
        wx.hideLoading();
        let pp = JSON.parse(res.data);
        // console.log(pp);
        let imgUrl = pp.data.imgUrl;
        // console.log(imgUrl);
        if (pp.status == 0){
          wx.showToast({
            title: "上传成功",
            icon: 'success',
            duration: 2000,
            mask: true
          })
          that.setData({
            userImg: imgUrl
          })
          that.updateUserInfoById();
        }else{
          wx.showModal({
            content: '修改失败',
          })
        }
      },
      fail: function (res) {
        wx.hideLoading();
        wx.showModal({
          content: '网络请求失败',
        })
      },
    })
  },
  bindPickerChange: function (e) {
    let userId = this.data.currentID;
    let that = this;
    // 获取修改的性别
    let gender = this.data.array[e.detail.value];
    // console.log(gender);
    http.updateUserSexApi(userId,{
      data:{
        gender:gender
      },
      success:res=>{
        console.log(res);
        if(res.status == 0){
          that.setData({
            index: e.detail.value
          })
          //更新缓存
          that.updateUserInfoById();
        }else{
          wx.showToast({
            title: '修改失败',
            icon: 'none',
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
  bindDateChange: function (e) {

    let userId = this.data.currentID;
    let that = this;
    let birthday = e.detail.value;
    // console.log(e.detail.value);
    // 修改请求
    http.updateUserBirthdayApi(userId,{
      data:{
        birthday: birthday
      },
      success:res =>{
        if(res.status == 0){
          that.setData({
            currentBirthday: e.detail.value
          })
          that.updateUserInfoById();
        }else{
          wx.showToast({
            title: '修改失败',
            icon: 'none',
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
  updateInfo:function(e){
    // console.log(e.currentTarget.id);
    let type = e.currentTarget.id;
    let maxNum = this.data.maxNum;

    let currentName = this.data.currentName;
    let currentIntroductory = this.data.currentIntroductory;
    let currentSex = this.data.currentSex;
    let currentBirthday = this.data.currentBirthday;
    let currentArea = this.data.currentArea;
    let currentEmail = this.data.currentEmail;

    if(type == 'name'){
      let length;
      if (currentName == null) {
        length = 0;
      } else {
        length = currentName.length;
      }
      this.setData({
        updateView: true,
        title: "编辑名字",
        updateTitle: "我的名字",
        updateDefaultContenxt: currentName,
        placeholderText: "记得填写名字",
        remianNum: maxNum -length,
        inputId:"name"
      })
    } else if (type == 'introductory'){
      let length;
      if(currentIntroductory == null){
        length = 0;
      }else{
        length = currentIntroductory.length;
      }
      this.setData({
        updateView: true,
        title: "编辑简介",
        updateTitle: "我的简介",
        updateDefaultContenxt: currentIntroductory,
        placeholderText: "记得填写简介",
        remianNum: maxNum - length,
        inputId: "introductory"
      })
    } else if (type == 'area') {
      let length;
      if (currentArea == null) {
        length = 0;
      } else {
        length = currentArea.length;
      }
      this.setData({
        updateView: true,
        title: "编辑地区",
        updateTitle: "我的地区",
        updateDefaultContenxt: currentArea,
        placeholderText: "记得填写地区",
        remianNum: maxNum - length,
        inputId: "area"
      })
    } else if (type == 'email') {
      let length;
      if (currentEmail == null) {
        length = 0;
      } else {
        length = currentEmail.length;
      }
      this.setData({
        updateView: true,
        title: "编辑邮箱",
        updateTitle: "我的邮箱",
        updateDefaultContenxt: currentEmail,
        placeholderText: "记得填写邮箱",
        remianNum: maxNum - length,
        inputId: "email"
      })
    }else{
      console.log("啥也不是")
    }
  },
  updateInputInfo:function(e){
    let typeId = e.target.id;
    //console.log(e.target.id);
    let inputValue = e.detail.value;
    
    let length = inputValue.length;
    let num = this.data.maxNum - length;
    this.setData({
      remianNum: num,
    })
    if(typeId == 'name'){
      this.setData({
        currentName: inputValue,
      })
    } else if (typeId == 'introductory'){
      this.setData({
        currentIntroductory: inputValue,
      })
    } else if (typeId == 'area') {
      this.setData({
        currentArea: inputValue,
      })
    } else if (typeId == 'email') {
      this.setData({
        currentEmail: inputValue,
      })
    }
  },
  goBack:function(e){
    // console.log("回到上一个页面")
    // 跳转页面
    wx.navigateBack();
  },
  goInfo:function(e){
    this.setData({
      updateView: false
    })
  },
  clearInfo:function(e){
    let num = this.data.maxNum;
    this.setData({
      updateDefaultContenxt:"",
      remianNum: num
    })
  },
  // 获取用户信息，从而修改缓存
  updateUserInfoById:function(){
    // console.log("查询用户信息");
    let userId = this.data.currentID;
    http.getPublishInfo(userId, {
      data: {},
      success: res => {
        // console.log(res);
        // 如果是0，登陆成功
        if (res.status == 0) {
          // 存放的用户的信息
          let user = {
            // 用户号
            userId: res.data.userId,
            // 地址
            userInfoAddress: res.data.userInfoAddress,
            // 生日
            userInfoBirth: res.data.userInfoBirth,
            // 视频收藏量
            userInfoCollects: 100,
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
          // 修改session
          wx.setStorageSync("user", user);
        }
      },
      fail: err => {
        wx.showModal({
          content: '网络请求失败',
        })
      }
    })
  },
  saveUpdate:function(e){
    let currentId = this.data.currentID;
    // console.log(currentId);
    let inputType = this.data.inputId;
    let that = this;

    if(inputType == 'name'){
      let name = that.data.currentName;
      // console.log(name);
      http.updateUserNameApi(currentId,{
        data:{
          infoName:name,
        },
        success:res => {
          // console.log(res);
          // console.log("成功");
          if(res.status == 0){
            that.setData({
              updateView: false,
              currentName:name
            })
            that.updateUserInfoById();
          }else{
            wx.showToast({
              title: '修改失败',
              icon: 'none',
            })
          }
        },
        fail:err=>{
          wx.showModal({
            content: '网络请求失败',
          })
        }
      })
    } else if (inputType == 'introductory'){
      let introductory = that.data.currentIntroductory;
      http.updateUserIntroductoryApi(currentId,{
        data: {
          intro: introductory
        },
        success: res => {
          // console.log("成功");
          console.log(res);
          if (res.status == 0) {
            that.setData({
              updateView: false,
              currentIntroductory:introductory
            })
            that.updateUserInfoById();
          } else {
            wx.showToast({
              title: '修改失败',
              icon: 'none',
            })
          }
        },
        fail: err => {
          wx.showModal({
            content: '网络请求失败',
          })
        }
      })
    } else if (inputType == 'area') {
      let area = that.data.currentArea;
      http.updateUserAreaApi(currentId,{
        data: {
          address: area
        },
        success: res => {
          // console.log("成功");
          if (res.status == 0) {
            that.setData({
              updateView: false,
              currentArea:area
            })
            that.updateUserInfoById();
          } else {
            wx.showToast({
              title: '修改失败',
              icon: 'none',
            })
          }
        },
        fail: err => {
          wx.showModal({
            content: '网络请求失败',
          })
        }
      })
    } else if (inputType == 'email') {
      let email = that.data.currentEmail;
      http.updateUserEmailApi(currentId,{
        data: {
          email:email
        },
        success: res => {
          // console.log("成功");
          if (res.status == 0) {
            that.setData({
              updateView: false,
              currentEmail:email
            })
            that.updateUserInfoById();
          } else {
            wx.showToast({
              title: '修改失败',
              icon: 'none',
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
  onLoad: function (options) {
    let user = wx.getStorageSync('user');
    // console.log(user);
    let sex = user.userInfoGender;
    let index = 0;
    if(sex == '男'){
      index = '0';
    }else if(sex == '女'){
      index = '1';
    }else{
      index = '2'
    }
    var timestamp = Date.parse(new Date());
    var date = new Date(timestamp);
    //获取年份  
    var Y = date.getFullYear();
    //获取月份  
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1);
    //获取当日日期 
    var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
    let time = Y + '-' + M + '-' + D;
    // console.log(time);
    // 修改数据
    this.setData({
      userImg: user.userInfoImg,
      currentName: user.userInfoName,
      currentID: user.userId,
      currentIntroductory: user.userInfoIntro,
      currentSex: user.userInfoGender,
      currentBirthday: user.userInfoBirth,
      currentArea: user.userInfoAddress,
      currentEmail: user.userInfoEmail,
      index:index,
      currentTime:time
    })
  },
})