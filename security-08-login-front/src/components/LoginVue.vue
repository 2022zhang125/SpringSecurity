<template>
  <div class="hello">
    <form action="#" method="post">
      账户：<input type="text" name="username" v-model="username" /> <br />
      密码：<input type="password" name="password" v-model="password" /> <br />
      <input type="button" value="登 录" @click="onLogin"/>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import qs from 'qs';
export default {
  name: 'LoginVue',
  data() {
    return {
      username: '',
      password: '',
    }
  },
  methods: {
    async onLogin() {
      const loadingInstance = this.$loading({
        lock: true,
        text: '登录中...',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      // 注意！这里由于后端框架使用的时request.getParameter()方法获取参数
      // 所以需要将参数转换为application/x-www-form-urlencoded格式也就是字符串格式。不能用json格式。
      try {
        const userInfo = qs.stringify({
          username: this.username,
          password: this.password
        });

        const response = await axios.post('http://localhost:8080/user/login', userInfo);
        if (response.data.code === 200) {
          this.$message.success(response.data.msg)
          // console.log("用户携带数据：" + JSON.stringify(response.data.data));
          sessionStorage.setItem('loginToken', response.data.data);
          this.$router.push({
            path: '/welcome',
          })
        } else {
          this.$message.error(response.data.msg);
          this.username = '';
          this.password = '';
        }
      } catch (error) {
        console.log(error);
      } finally {
        loadingInstance.close();
      }
    }
  }
}
</script>
<style scoped>

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}</style>
