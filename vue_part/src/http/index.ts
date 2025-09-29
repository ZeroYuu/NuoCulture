import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse, type InternalAxiosRequestConfig } from 'axios';
import { ElMessage } from 'element-plus'
import { userStore } from '@/store/user/index'
axios.defaults.withCredentials = true
//1.创建axios实例
const config = {
    //接口
    baseURL: 'http://localhost:8090/',
    //超时时间
    timeout: 10000
}

export interface Result<T = any> {
    code: number;
    msg: string;
    data: T;
}
class Http {
    private instance: AxiosInstance;
    constructor(config: AxiosRequestConfig) {
        this.instance = axios.create(config)
        this.interceptors()
    }
    private interceptors() {
        //2.请求拦截器
        this.instance.interceptors.request.use((config: InternalAxiosRequestConfig) => {
            //解引用方式
            const store = userStore()
            let token = store.getToken;
            console.log(token)
            if (token) {
                config.headers!['token'] = token
            }
            return config;
        }, (error: any) => {
            error.data = {}
            error.data.msg = '服务器异常,请联系管理员!'
            return error;
        })

        //3.响应拦截器
        this.instance.interceptors.response.use((res: AxiosResponse) => {
            if (res.data.code == 200||res.data.code == 201) {
                return res.data
            } else {
                ElMessage.error(res.data.msg || '服务器出错!')
                return Promise.reject(res.data.msg || '服务器出错')
            }
        }, (error) => {
            console.log('进入错误')
            error.data = {};
            if (error && error.response) {
                const status = error.response.status
                switch (status) {
                    case 400:
                        ElMessage.error("请求错误");
                        break;
                    case 401:
                        ElMessage.error("未登录，请先登录");
                        break;
                    case 403:
                        ElMessage.error("登录过期，请重新登录");
                        break;
                    case 404:
                        ElMessage.error("请求错误，未找到相应的资源");
                        break;
                    case 408:
                        ElMessage.error("请求超时");
                        break;
                    case 500:
                        ElMessage.error("服务器错误");
                        break;
                    case 504:
                        ElMessage.error("网络超时");
                        break;
                    default:
                        ElMessage.error("请求失败");
                }
            } else {
                if (JSON.stringify(error).includes('timeout')) {
                    error.code = 'TIMEOUT';
                    error.message = '服务器响应超时'
                }
            }
            return Promise.reject(error);
        })
    }

    get<T = Result>(url: string, params?: object): Promise<T> {
        return this.instance.get(url, { params })
    }
    post<T = Result>(url: string, data?: object): Promise<T> {
        return this.instance.post(url, data)
    }
    put<T = Result>(url: string, data?: object): Promise<T> {
        return this.instance.put(url, data)
    }
    delete<T = Result>(url: string): Promise<T> {
        return this.instance.delete(url)
    }
    //图片上传
    upload<T = Result>(url: string, params?: object): Promise<T> {
        return this.instance.post(url, params, {
            headers: {
                'Content-Type': 'multipart/from-data'
            }
        })
    }

}

//超出axios实例
export default new Http(config) 