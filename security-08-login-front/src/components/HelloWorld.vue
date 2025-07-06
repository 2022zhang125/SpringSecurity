<template>
  <div class="hello">
    <form action="#" method="post">
      账户：<input type="text" name="username" v-model="username"/> <br/>
      密码：<input type="password" name="password" v-model="password"/> <br/>
      <input type="button" value="登 录" @click="onLogin"/>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import qs from 'qs';
export default {
  name: 'HelloWorld',
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
        console.log(response)
      }).catch(error => {
        console.log(error)
      });
    }
  }
}
</script>
<style scoped>

</style>
