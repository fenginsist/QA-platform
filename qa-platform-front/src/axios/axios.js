import axios from 'axios';
import config from '../config/index'
console.log('process.env.NODE_ENV: ', process.env.NODE_ENV)
console.log('config.API_URL: ', config.BASE_API)
console.log('baseURL: ', process.env.NODE_ENV === 'production'? config.BASE_API : config.BASE_API)
// 创建 Axios 实例//
const service = axios.create({
    // baseURL: process.env.NODE_ENV === 'production'? 'http://192.168.16.21:8009' : '/',  // 设置后端API的基础URL 
    baseURL: config.BASE_API,  // config.BASE_API，设置后端API的基础URL 
    timeout: 40000, // 设置请求超时时间
    headers: {
        'Content-Type': 'application/json', // 设置请求头
    },
});

// 请求拦截器
service.interceptors.request.use(
    (config) => {
        // 在发送请求之前做一些操作
        // const token = localStorage.getItem('token'); // 假设你在本地存储了 token
        // if (token) {
        //   config.headers.Authorization = `Bearer ${token}`; // 为每个请求添加 token
        // }
        return config;
    },
    (error) => {
        // 对请求错误做一些处理
        console.error('请求错误：', error);
        return Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(
    (response) => {
        // 对响应数据做一些处理
        console.log('axios success: ', JSON.stringify(response))
        return response.data; // 直接返回响应数据
        // return response; // 直接返回响应数据
    },
    (error) => {
        // 对响应错误做一些处理
        console.log('axios error: ', JSON.stringify(error))
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    console.error('未授权，重新登录');
                    // 可以根据需要进行跳转或其他操作
                    break;
                case 403:
                    console.error('权限不足');
                    break;
                case 404:
                    console.error('请求的资源不存在');
                    break;
                case 504:
                    console.error('响应超时');
                    // return error;
                default:
                    console.error('其他错误：', error.response.status);
            }
        }
        return Promise.reject(error);
    }
);

export default service;
