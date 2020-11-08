// pages/register/register.js
import http from '../../utils/api.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pnoneImg:"/images/icon/phone.png",
    yz_codeImg:"/images/icon/yanZcode.png",
    chaImg:"/images/icon/black_cha.png",
    pwdImg:"/images/icon/pasInput.png",
    phone:"",
    yz_code:"",
    pwd1:"",
    pwd2:"",
    setCodeTime:"获取验证码",
    real_yz_code:"",
    getCodeStyle:false,
    time:60,
  },
  phoneInput:function(e){
    this.setData({
      phone: e.detail.value
    })
  },
  getYz_Code:function(e){
    // 传递手机号
    let phone = this.data.phone;
    let that = this;
    http.getMessageCodeApi(phone, {
      data: {},
      success: res => {
        console.log(res);
        if (res.status == 0) {
          let yz_code = res.data;
          that.setData({
            real_yz_code: yz_code,
            getYz_code: '60s',
          })
        } else {
          wx.showToast({
            title: "获取失败",
            icon: 'none',
            duration: 2000,
            mask: true
          })
        }
      },
      fail: err => {
        wx.showModal({
          content: '网络请求异常',
        })
      }
    })
    // 倒计时
    var timeOut = setInterval(function(){
      let phone = this.data.phone;
      this.setData({
        getCodeStyle: true,
        setCodeTime: this.data.time + 's后重发',
        time: this.data.time - 1
      })
      if(this.data.time < 0 ){
        clearInterval(timeOut);
        this.setData({
          getCodeStyle: false,
          setCodeTime: '获取验证码',
          time: 60
        })
      }
    }.bind(this),1000);
  },
  yz_codeInput:function(e){
    this.setData({
      yz_code: e.detail.value
    })
    // 验证验证码
    let real_yz_code = this.data.real_yz_code;
    if (e.detail.value == real_yz_code){
      this.setData({
        yz_codeImg: "/images/icon/yanZcode2.png",
      })
    }else{
      this.setData({
        yz_codeImg: "/images/icon/yanZcode.png",
      })
    }
  },
  clearYzCode:function(e){
    // console.log("清除验证码");
    this.setData({
      yz_code: ""
    })
  },
  pwdInput1:function(e){
    let value = (e.detail.value || '').trim()
    if (value) {
      const reg = /[\u4e00-\u9fa5]/ig
      if ((reg.test(value))) {
        value = value.replace(reg, '')
      }
    }
    // 获取输入的密码 
    this.setData({
      pwd1: value
    })
  },
  clearMiMa1:function(e){
    // console.log("清除一次密码");
    this.setData({
      pwd1: ""
    })
  },
  pwdInput2: function (e) {
    let value = (e.detail.value || '').trim()
    if (value) {
      const reg = /[\u4e00-\u9fa5]/ig
      if ((reg.test(value))) {
        value = value.replace(reg, '')
      }
    }
    this.setData({
      pwd2: value
    })
  },
  clearMiMa2:function(e){
    // console.log("清除二次密码");
    this.setData({
      pwd2: ""
    })
  },
  register:function(e){
    // console.log("注册了");
    let phone = this.data.phone;
    let pwd1 = this.data.pwd1;
    let pwd2 = this.data.pwd2;
    let yz_code = this.data.yz_code;
    let realCode = this.data.real_yz_code;

    const reg = /^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/;
    const myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
    //0.判空处理
    if(phone == "" || pwd1 == "" || pwd2 == "" || yz_code == ""){
      wx.showModal({
        content: "请输入完整数据"
      })
    }else{
      // 1.验证手机号是否存在
      if (myreg.test(phone)) {
        //2.验证验证码是否正确(默认)
        if (yz_code == realCode ) {
          //3.密码长度不可少于6位
          if (pwd1.length < 6){
            wx.showModal({
              content: "密码长度不得少于6位"
            })
          }else{
            //3.密码一致
            if (pwd1 == pwd2) {
              //完成就加载
              wx.showLoading({
                title: "正在注册中",
                mask: true,
              })
              //4.基本无误，发送请求注册
              http.registApi(yz_code,{
                data:{
                  phoneNum: phone,
                  passWord:pwd2,
                },
                success: res => {
                  wx.hideLoading();
                  //console.log("完成");
                  wx.showToast({
                    title: "注册完成了",
                    icon: "success",
                    duration: 2000,
                    mask: true,
                    success: function (e) {
                      wx.hideLoading();
                      setTimeout(function () {
                        //回到登录页
                        wx.navigateTo({
                          url: '/pages/login/login',
                        })
                      }, 1000)
                    }
                  })
                },
                fail: err => {
                  wx.hideLoading();
                  wx.showModal({
                    content: "注册接口还未完成"
                  })
                }
              })
            } else {
              wx.showModal({
                content: "密码不一致"
              })
            }
          }
        } else {
          wx.showModal({
            content: "验证码错误"
          })
        }
      } else {
        wx.showModal({
          content:"请输入正确的手机号"
        })
      }
    }
  },
  clearInfo:function(e){
    this.setData({
      phone:"",
      yz_code:"",
      pwd1:"",
      pwd2:""
    })
  },
  leave:function(e){
    this.clearInfo();
    wx.navigateTo({
      url: '/pages/login/login',
    })
    //回到登录页
  },
})