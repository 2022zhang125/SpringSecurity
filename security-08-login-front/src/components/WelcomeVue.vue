<template>
    <div class="welcome">
        <h1>欢迎来到我们的应用!</h1>
        <p>这是一个简单的欢迎页面。</p>
        <p>{{ userInfo }}</p>
    </div>
</template>

<script>
export default {
    name: 'WelcomeVue',
    data(){
        return{
           userInfo: this.$route.query.userInfo
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
