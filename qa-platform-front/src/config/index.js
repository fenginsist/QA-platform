// src/config/index.js
import baseEnv from './base.env.js';
import devConfig from './dev.env.js';
import prodConfig from './prod.env.js';
// 将 require() 改为 import，因为在现代的 Vue3 项目中，推荐使用 import/export 语法来加载模块。

let envConfig;
/**
 * 
 * config 目的是为了配置axios
 * 
 */
if (process.env.NODE_ENV === 'development') {
    // Object.assign(baseEnv,devConfig)
    envConfig = {...baseEnv,...devConfig}  // 等价于 webpack 的 merge函数进行合并
} else {    // (process.env.NODE_ENV === 'production') 
    envConfig = {...baseEnv,...prodConfig} // require('./dev.env.js');
}

export default envConfig;