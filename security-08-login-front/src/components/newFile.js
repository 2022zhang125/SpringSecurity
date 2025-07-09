import axios from 'axios';

export default (await import('vue')).defineComponent({
name: 'WelcomeVue',
data() {
return {
userInfo: ""
};
},
methods: {
getUserInfo() {
axios.get("http://localhost:8080/test", headers, { "Authorization": sessionStorage });
},
logout() {
axios.get('http://localhost:8080/user/logout').then(response => {
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
});
