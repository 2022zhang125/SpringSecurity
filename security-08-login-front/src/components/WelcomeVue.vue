<template>
    <div class="welcome">
        <h1>欢迎来到我们的应用!</h1>
        <p>这是一个简单的欢迎页面。</p>
        <button @click="getUserInfo">获取用户信息</button>
        <button @click="clueMenu">hasAuthority('saler')</button>
        <button @click="clueList">hasAuthority('admin')</button>
        <p><button @click="logout">登出</button></p>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'WelcomeVue',
    data() {
        return {
            userInfo: ""
        }
    },
    methods: {
        clueMenu(){
            axios.get('http://localhost:8080/api/clue/menu', {
                headers: {
                    "Authorization": sessionStorage.getItem("loginToken")
                }
            }).then(response => {
                if (response.data.code === 200) {
                    this.$message.success("获取菜单成功");
                    console.log(response.data.data);
                } else {
                    this.$message.error(response.data.msg);
                }
            }).catch(error => {
                console.log(error);
                this.$message.error("获取菜单失败，请重新登录！");
                this.$router.push('/'); // 如果获取菜单失败，跳转到登录页面
            });
        },
        clueList() {
            axios.get('http://localhost:8080/api/list', {
                headers: {
                    "Authorization": sessionStorage.getItem("loginToken")
                }
            }).then(response => {
                if (response.data.code === 200) {
                    this.$message.success(response.data.msg);
                } else {
                    this.$message.error(response.data.msg);
                }
            }).catch(error => {
                console.log(error);
                this.$message.error("获取线索列表失败，请重新登录！");
                this.$router.push('/'); // 如果获取线索列表失败，跳转到登录页面
            });
        },
        async getUserInfo() {
            const loadingInstance = this.$loading({
                lock: true,
                text: '获取用户信息中...',
                background: 'rgba(0, 0, 0, 0.7)'
            });
            await axios.get("http://localhost:8080/test", {
                headers: {
                    "Authorization": sessionStorage.getItem("loginToken")
                }
            }).then(response => {
                if (response.data.code === 200) {
                    loadingInstance.close(); // 关闭加载动画
                    this.userInfo = response.data.data;
                    this.$message.success("获取用户信息成功");
                } else {
                    loadingInstance.close(); // 关闭加载动画
                    this.$message.error(response.data.msg);
                }
            }).catch(error => {
                console.log(error);
                this.$message.error("获取用户信息失败，请重新登录！");
                this.$router.push('/'); // 如果获取用户信息失败，跳转到登录页面
                loadingInstance.close(); // 关闭加载动画

            });
        },
        logout() {
            axios.get('http://localhost:8080/user/logout',{
                headers:{
                    "Authorization": sessionStorage.getItem("loginToken")
                }
            }).then(response => {
                if (response.data.code === 200) {
                    this.$message.success(response.data.msg);
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
