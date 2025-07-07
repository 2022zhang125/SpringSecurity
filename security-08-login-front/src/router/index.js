import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginVue from '../components/LoginVue';
import WelcomeVue from '../components/WelcomeVue';

Vue.use(VueRouter)

const routes = [
  { path: '/', component: LoginVue },
  { path: '/welcome', component: WelcomeVue },
]
const router = new VueRouter({
  mode: 'history',
  routes
});

export default router;