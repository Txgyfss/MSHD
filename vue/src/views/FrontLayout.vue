
<template>
  <div>
    <!--头部-->
    <div class="header">
      <div class="header-left">
        <img src="@/assets/imgs/logo.jpg" alt="">
        <div class="title">自邮制造</div>
      </div>
      <div class="header-center">
        <div class="header-nav">
          <el-menu :default-active="$route.path" mode="horizontal" router>
						<el-menu-item index="/front/index" style="color:white">首页</el-menu-item>
						<el-menu-item index="/front/goods" style="color:white">所有商品</el-menu-item>
						<el-menu-item v-if="user.role !== 1" index="/front/cart" style="color:white">我的购物车</el-menu-item>
						<el-menu-item v-if="user.role !== 1" index="/front/order" style="color:white">我的订单</el-menu-item>
          </el-menu>
        </div>
      </div>
      <div style="width: 300px">
        <el-input size="medium" style="width: 200px" v-model="name" clearable placeholder="请输入商品名称搜索"></el-input>
        <el-button size="medium" @click="search" style="margin-left: 5px; background:#ffd000; font-weight:bold; color:white">搜索</el-button>
      </div>
      <div class="header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="header-dropdown">
              <img :src="user.avatar" alt="" >
              <div style="margin-left: 10px; color:white; font-weight: bold; font-size: 18px">
                <span>{{ user.username }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="font-weight: bold; font-size: 15px" @click="goToPerson()">个人中心</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none; font-weight: bold; font-size: 15px" @click="logout">退出</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="main-body">
      <router-view ref="child" />
    </div>

  </div>

</template>

<script>
import request from "@/utils/request";

export default {
  name: "FrontLayout",

  data () {
    return {
      top: '',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      name: ''
    }
  },

  mounted() {
    request.get('/notice').then(res => {
      this.notice = res.data
      let i = 0
      if (this.notice && this.notice.length) {
        this.top = this.notice[0].content
        setInterval(() => {
          this.top = this.notice[i].content
          i++
          if (i === this.notice.length) {
            i = 0
          }
        }, 2500)
      }
    })
  },

  methods: {
    goToPerson() {
			if (this.user.role === 1) {
				this.$router.push("/end/pAdmin")
			}
			if (this.user.role === 2) {
				this.$router.push("/end/pUser")
			}

    },
    // 退出登录
    logout() {
      localStorage.removeItem("user");
      this.$router.push("/login");
    },
    search() {
      this.$router.replace('/front/search?name=' + this.name + '&random' + Math.random())
      this.$refs.child.searchGoods()
    }
  }

}
</script>

<style scoped>
  @import "@/assets/css/front.css";
</style>