import { http } from '../utils/http.js';
import http2 from '../utils/http.js'

// 设置服务端视频上传地址
let uploadVideoUrl = http2.host+"/video/upload/";
// 设置用户头像上传的地址
let uploadUserImg = http2.host+"/info/update_head_user/";
// 当前登陆用户的ID
let userId = wx.getStorageSync('user').userId;
// 参数：1：url 2:method 3:params参数
// 搜索接口
function searchApi(keyWord,params){
  http("/video/select_video_keyword/" + keyWord, 'get', params);
}
// 登陆接口
function loginApi(params){
  http('/user/login','post',params)
}
// 注册接口
function registApi(code,params) {
  http('/user/register/'+code, 'post', params)
}
// 获取新的视频列表的接口
function getNewVideoListApi(userId,params){
  http('/video/get_all_videos/'+userId, 'get', params)
}
// 上传信息
function upLoadInfoApi(params){
  http('/', 'post', params);
}
// 发表评论
function addCommentApi(videoId,userId,params) {
  http('/comment/submit_video_comment_user/' + videoId + '/' + userId, 'post', params);
}
// 获取评论
function getCommentApi(userId,videoId,params) {
  http('/comment/get_comments_video/' + userId + "/" + videoId, 'post', params);
}
// 删除动态
function deleteDynamicApi(params){
  http('/', 'post', params);
}
// 获取关注和粉丝列表
function getAttentionApi(userId,type,params){
  http('/info/get_ff_list/'+userId+'/'+type, 'get', params);
}
// 获取动态的评论
function getDynamicCommentApi(dynamicId,params){
  console.log(dynamicId);
  http("/" + dynamicId,'post',params);
}
// 点赞功能
function addZanApi(userId, videoId , type ,params) {
  http("/video/recommend_video/" + userId + "/" + videoId + "/" + type, 'post', params);
}
// 点击用户头像根据ID查看用户信息
function getPublishInfo(userId, params){
  http("/info/get_info_user/" + userId, 'get', params);
}
// 修改性别
function updateUserSexApi(userId, params){
  http("/info/update_gender_user/" + userId, 'post', params);
}
// 修改生日
function updateUserBirthdayApi(userId, params){
  http("/info/update_birth_user/" + userId, 'post', params);
}
// 修改姓名
function updateUserNameApi(userId, params){
  http("/info/update_name_user/" + userId, 'post', params);
}
// 修改简介
function updateUserIntroductoryApi(userId, params){
  console.log(userId);
  http("/info/update_intro_user/" + userId, 'post', params);
}
// 修改地区
function updateUserAreaApi(userId, params){
  http("/info/update_address_user/" + userId, 'post', params);
}
// 修改邮箱
function updateUserEmailApi(userId, params){
  http("/info/update_email_user/" + userId, 'post', params);
}
// 更改用户头像
function updateUserHeadImgApi(userId, params){
  http("/info/update_head_user/" + userId, 'post', params);
}
// 注册账号验证码
function getMessageCodeApi(phone,params){
  http("/user/get_code/" + phone, 'post', params);
}
// 获取热点榜
function getHotVideoApi(params){
  http("/video/get_hot_info/", 'get', params);
}
// 猜你想搜
function getGuestVideoApi(params){
  http("/video/get_random_info/", 'get', params);
}
// 根据视频ID获取视频信息
function getVideoInfoByVideoId(userId,videoId,params){
  http("/video/get_single_video/" + userId + "/" + videoId, 'get', params);
}
// 评论点赞
function addCommentZanApi(userId,commentId,type,params){
  http("/comment/recommend_comment_user/" + userId + "/" + commentId + "/" + type, 'get', params);
}
// 添加关注
function becomeFansiApi(userId, relationId,params){
  http("/info/focus_user/" + userId + "/" + relationId, 'post', params);
}
// 评论点赞
function isLoveCommentsApi(userId,commentId,params){
  http("/comment/recommend_comment_user/" + userId + "/" + commentId, 'post', params);
}
// 评论取消点赞
function noLoveCommentsApi(userId, commentId, params) {
  http("/comment/cancel_comment_user/" + userId + "/" + commentId, 'post', params);
}
// 获取当前用户的作品
function getCurrentUserPublishVideoApi(userId,params){
  http("/video/get_user_publish/" + userId , 'post', params);
}
// 获取当前用户喜欢的作品
function getCurrentUserCollectVideoApi(userId, params) {
  http("/video/get_user_collect/" + userId, 'post', params);
}
// 获取点播榜
export default{
  uploadVideoUrl,
  uploadUserImg,
  searchApi,
  loginApi,
  registApi, 
  getNewVideoListApi,
  upLoadInfoApi,
  addCommentApi,
  getCommentApi,
  deleteDynamicApi,
  getAttentionApi,
  getDynamicCommentApi,
  addZanApi,
  getPublishInfo,
  updateUserSexApi,
  updateUserBirthdayApi,
  updateUserNameApi,
  updateUserIntroductoryApi,
  updateUserAreaApi,
  updateUserEmailApi,
  updateUserHeadImgApi,
  getMessageCodeApi,
  getHotVideoApi,
  getGuestVideoApi,
  getVideoInfoByVideoId,
  addCommentZanApi,
  becomeFansiApi,
  isLoveCommentsApi,
  noLoveCommentsApi,
  getCurrentUserPublishVideoApi,
  getCurrentUserCollectVideoApi
}