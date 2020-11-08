// const host = "https://101.200.203.175:8081/api";
const host = "https://www.baiding.top/api";
// const host = "https://10.2.179.181:8081/api";
// const host = "http://10.2.179.181:8082/api";

export function http(url, method, params) {
  let token = 'token'
  let sign = 'sign'
  let data = {
    token,
    sign
  }
  // 判断是否有发送数据
  if (params.data) {
    for (let key in params.data) {
      if (params.data[key] == null || params.data[key] == 'null') {
        delete params.data[key]
      }
    }
    data = { ...data, ...params.data }
  }
  wx.request({
    url: host + url,
    method: method == 'post' ? 'post' : 'get',
    data,
    header: {
      'content-type': 'application/json',
      'token':data.token
    }, 
    success(res) {
      params.success && params.success(res.data)
    },
    fail(err) {
      params.fail && params.fail(err)
    }
  })
}
export default {
  host
}