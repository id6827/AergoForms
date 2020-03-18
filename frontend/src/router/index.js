import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from '../views/Index.vue'
import Login from '../views/login/Login.vue'
import Form from '../views/vote/Form.vue'

Vue.use(VueRouter)

const routes = [{
    path: '/',
    name: 'index',
    redirect: '/forms',
    component: Index,
    children: [{
      path: '/forms',
      name: 'forms',
      component: Form,
    }, ]
  },
  {
    path: "/login",
    name: "login",
    component: Login
  },
  // {
  //   path: '/about',
  //   name: 'about',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import( /* webpackChunkName: "about" */ '../views/About.vue')
  // }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router