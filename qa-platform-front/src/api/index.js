import request from '@/axios/axios' // ES6語法

export function getllmAs(params) {
    return request({
        url: '/questionAs?question=' + params,
        method: 'get',
        // params: params
    })
}

export function getllmHr(params) {
    return request({
        url: '/questionHr?question=' + params,
        method: 'get',
        // params: params
    })
}
