import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import { store } from '@/store'
import layout from '../layout/index.vue'
// 静态路由
export const constantRoutes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: layout,
    redirect: '/home',
    meta: {
      title: {
        '/zh-CN': '首页',
        '/en-US': 'Home Page'
      },
      icon: 'ic ic-homepage-fill'
    },
    children: [
      {
        path: '/home',
        name: 'home',
        // component: () => import(/* webpackChunkName: "home" */ '@/views/Home/home.vue'),
        component: () => import(/* webpackChunkName: "home" */ '@/views/Personal/personalCenter.vue'),
        meta: {
          title: {
            '/zh-CN': '个人中心',
            '/en-US': 'Home Page'
          },
          icon: 'ic ic-homepage-fill'
        }
      }
    ]
  },
  {
    path: '/login',
    name: '登录',
    component: () => import(/* webpackChunkName: "login" */ '@/views/Login/index.vue'),
    meta: {
      title: {
        '/zh-CN': '登录',
        '/en-US': 'Login'
      },
      hidden: true,
      hiddenTab: true
    }
  },
  {
    path: '/noFound',
    name: 'NoFound',
    component: () => import(/* webpackChunkName: "noFound" */ '@/views/noFound.vue'),
    meta: {
      title: {
        '/zh-CN': '404',
        '/en-US': '404'
      },
      hidden: true,
      hiddenTab: true
    }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import(/* webpackChunkName: "noFound" */ '@/views/noFound.vue'),
    meta: {
      title: {
        '/zh-CN': '未找到',
        '/en-US': 'NOT FOUND'
      },
      hidden: true,
      hiddenTab: true
    }
  }
]

// 异步路由
// 异步路由
// @ts-ignore
export const asyncRoutes: Array<RouteRecordRaw> = [

  {
    path: '/review',
    component: layout,
    redirect: '/review/studentList',
    meta: {
      title: {
        '/zh-CN': '评阅论文',
        '/en-US': 'Review Essay'
      },
      icon: 'ic ic-tailor'
    },
    children: [
      {
        path: '/review/studentList',
        name: 'studentList',
        component: () => import(/* webpackChunkName: "richText" */ '@/views/Review/studentList.vue'),
        meta: {
          title: {
            '/zh-CN': '评阅论文',
            '/en-US': 'Review Essay'
          },
          icon: 'ic ic-tailor'
        }
      },
      {
        path: '/review/reviewPaper',
        name: 'reviewPage',
        component: () => import(/* webpackChunkName: "richText" */ '@/views/Review/review.vue'),
        meta: {
          title: {
            '/zh-CN': '评阅界面',
            '/en-US': 'review'
          },
          icon: 'ic ic-tailor',
          hidden:true
        }
      }
    ]
  },
  {
    path: '/studentEssay',
    component: layout,
    redirect: '/studentEssay/submitEssay',
    meta: {
      title: {
        '/zh-CN': '毕业论文',
        '/en-US': 'Thesis'
      },
      icon: 'ic ic-tailor'
    },
    children: [
      {
        path: '/studentEssay/submitEssay',
        name: 'submitEssay.vue',
        component: () => import(/* webpackChunkName: "richText" */ '@/views/StudentEssay/submitEssay.vue'),
        meta: {
          title: {
            '/zh-CN': '论文提交',
            '/en-US': 'Essay Submission'
          },
          icon: 'ic ic-tailor'
        }
      }
    ]
  },
  {
    path: '/Feedback',
    component: layout,
    redirect: '/Feedback/feedback',
    meta: {
      title: {
        '/zh-CN': '反馈',
        '/en-US': 'Feedback'
      },
      icon: 'ic ic-tailor'
    },
    children: [
      {
        path: '/Feedback/feedback',
        name: 'feedback.vue',
        component: () => import(/* webpackChunkName: "richText" */ '@/views/Feedback/feedback.vue'),
        meta: {
          title: {
            '/zh-CN': '反馈意见',
            '/en-US': 'Feedback'
          },
          icon: 'ic ic-tailor'
        }
      }
    ]
  },
  {
    path: '/Auxiliary',
    component: layout,
    redirect: '/Auxiliary/auxiliary',
    meta: {
      title: {
        '/zh-CN': '辅助功能',
        '/en-US': 'Auxiliary'
      },
      icon: 'ic ic-tailor'
    },
    children: [
      {
        path: '/Auxiliary/auxiliary',
        name: 'auxiliary.vue',
        component: () => import(/* webpackChunkName: "richText" */ '@/views/Auxiliary/auxiliary.vue'),
        meta: {
          title: {
            '/zh-CN': '辅助功能',
            '/en-US': 'Auxiliary'
          },
          icon: 'ic ic-tailor'
        }
      }
    ]
  },
  {
    path: '/userManagement',
    component: layout,
    redirect: '/userManagement/userList',
    meta: {
      title: {
        '/zh-CN': '用户管理',
        '/en-US': 'User Management'
      },
      icon: 'ic ic-tailor'
    },
    children: [

      {
        path: '/userManagement/userList',
        name: '用户',
        component: () => import(/* webpackChunkName: "richText" */ '@/views/userManagement/UserList.vue'),
        meta: {
          title: {
            '/zh-CN': '用户',
            '/en-US': 'User'
          },
          icon: 'ic ic-tailor'
        }
      },
      {
        path: '/userManagement/relationship',
        name: '分配关系',
        component: () => import(/* webpackChunkName: "richText" */ '@/views/userManagement/relationship.vue'),
        meta: {
          title: {
            '/zh-CN': '分配关系',
            '/en-US': 'Relationship'
          },
          icon: 'ic ic-tailor'
        }
      },
    ]
  },

]
const router = createRouter({
  history: createWebHashHistory(), // hash模式：createWebHashHistory，history模式：createWebHistory
  scrollBehavior: () => ({
    top: 0
  }),
  routes: constantRoutes
})
router.beforeEach((to, from, next) => {
  const tabsOption = store.getters['tabModule/getTabsOption']
  // 判断当前路由中是否已经入栈
  const flag = tabsOption.findIndex((tab: { route: string }) => tab.route === to.path) > -1
  if (!flag && !to.meta.hiddenTab) {
    store.commit('tabModule/ADD_TAB', { route: to.path, title: to.meta.title, name: to.name })
  }
  store.commit('tabModule/SET_TAB', to.path)
  if (sessionStorage.getItem('auth')) {
    next()
  } else if (to.path === '/login') {
    console.log('/login')
    next()
  } else {
    console.log('unauthed into login')
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
  }
})

router.afterEach((to, from, next) => {
  console.log('router.afterEach')
  window.scrollTo(0, 0)
});

export default router
