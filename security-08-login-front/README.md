# security-08-login-front（前端）

> 前端没啥好说的，就是用Vue2的时候记得Vue-router的版本得控制在3.x即可这里的是3.6.5

## 注意！

这里有个事，就是我们在进行POST请求发送数据的时候，记得要把const定义的JSON数据转为表单数据，不然后端接收到的值就是NULL了。

```vue
<script>
import axios from 'axios';
import qs from 'qs';
export default {
  name: 'LoginVue',
  data() {
    return {
      username: '',
      password: ''
    }
  },
  methods:{
    async onLogin(){ 
      // 注意！这里由于后端框架使用的时request.getParameter()方法获取参数
      // 所以需要将参数转换为application/x-www-form-urlencoded格式也就是字符串格式。不能用json格式。
      const userInfo = qs.stringify({
        username: this.username,
        password: this.password
      });
     await axios.post('http://localhost:8080/user/login', userInfo).then(response => {
        alert(response.data.msg);
        // console.log(response);
        if(response.data.code === 200){
          // 响应正常
          this.$router.push({
            path: '/welcome',
            query: {
              userInfo: JSON.stringify(response.data.data)
            }
          })
        }else{
          // 清空输入框
          this.username = '';
          this.password = '';
        }
      }).catch(error => {
        console.log(error)
      });
    }
  }
}
</script>
```

也就是这一段，记得下载qs

```js
const userInfo = qs.stringify({
    username: this.username,
    password: this.password
  });
```

