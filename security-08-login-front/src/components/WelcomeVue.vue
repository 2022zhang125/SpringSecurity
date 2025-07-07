<template>
    <div class="welcome">
        <h1>欢迎来到我们的应用!</h1>
        <p>这是一个简单的欢迎页面。</p>
        <p>{{ userInfo }}</p>
        <p><button @click="logout">登出</button></p>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'WelcomeVue',
    data(){
        return{
           userInfo: this.$route.query.userInfo
        }
    },
    methods:{
        logout(){
            axios.get('http://localhost:8080/user/logout').then(response => {
                alert(response.data.msg);
                if(response.data.code === 200){
                    // 登出成功，跳转到登录页面
                    this.$router.push('/');
                }
            }).catch(error => {
                console.log(error);
            });
        }
    }
}
</script>

<style scoped>
.welcome {
    text-align: center;
    margin-top: 50px;
}
</style>
