import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    { path: '/login', component: () => import("@/views/Login") },
	{ path: '/register', component: () => import("@/views/Register") },
    { path: '/end', component: () => import("@/views/ManagerLayout"),
        children: [
            { path: 'index', component: () => import("@/views/manager/Index") },
			{ path: 'admin', component: () => import("@/views/manager/Admin") },
			{ path: 'user', component: () => import("@/views/manager/User") },
			{ path: 'type', component: () => import("@/views/manager/Type") },
			{ path: 'goods', component: () => import("@/views/manager/Upload.vue") },
			{ path: 'hotgoods', component: () => import("@/views/manager/HotGoods") },
			{ path: 'pAdmin', component: () => import("@/views/manager/pAdmin")  },
			{ path: 'pUser', component: () => import("@/views/manager/pUser")  },
			{ path: 'order', component: () => import("@/views/manager/Order")  },
			{ path: 'comment', component: () => import("@/views/manager/Comment")  },
			{ path: 'notice', component: () => import("@/views/manager/Notice")  },
			//{ path: 'address', component: () => import("@/views/manager/Address")  },
			{ path: 'advertising', component: () => import("@/views/manager/Advertising.vue")  },
			{ path: 'banner', component: () => import("@/views/manager/Banner.vue")  },

            { path: 'password', component: () => import("@/views/manager/Password") },
        ]
    },
	{ path: '/front', redirect: 'front/index', component: () => import("@/views/FrontLayout"),
		children: [
			{ path: 'index', component: () => import("@/views/front/Index") },
			{ path: 'detail', component: () => import("@/views/front/Detail") },
			{ path: 'cart', component: () => import("@/views/front/Cart") },
			{ path: 'order', component: () => import("@/views/front/Order") },
			{ path: 'search', component: () => import("@/views/front/Search") },
			{ path: 'goods', component: () => import("@/views/front/Goods") },
		]
	},

]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

// 路由守卫
router.beforeEach((to ,from, next) => {
    if (to.path === '/') {
        return next("/login");
    }
    let user = localStorage.getItem("user");
    if (!user && to.path !== '/login' && to.path !== '/register' && to.path !== '/front/index') {
        return next("/login");
    }
    next();
})

export default router
