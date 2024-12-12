<template>
  <div class="main-content">
		<div class="main-line">
			<div class="main-line-1">
				<div class="main-img">
					<el-carousel height="80vh" :interval="2000">
						<el-carousel-item v-for="(item,index) in carousel_1_1" :key="index">
							<img :src="item.img" alt=""  style="height: 700px" @click="$router.push('/front/detail?id=' + item.goodsId)">
						</el-carousel-item>
					</el-carousel>
				</div>
			</div>
		</div>

    <!--    广告展示位 点击可链接到商品详情-->
    <div class="item-grid"  style="margin-top: 40px;padding: 40px 260px">
      <div class="item-grid-item-firstrow"  v-for="obj in advertisingData" @click="$router.push('/front/detail?id=' + obj.goodsId)">
        <div class="overflowShow" style="width: 70%; font-size: 16px;font-weight:550;letter-spacing:2px;text-align: center; margin: 10px auto">{{obj.title}}</div>
        <div class="overflowShow" style="width: 70%; color:#ffd000;letter-spacing:1px;text-align: center; margin: 10px auto">{{obj.introducing}}</div>
      </div>
      <div class="item-grid-item"  :class="{ line: (index + 1) % 5 != 0 }" :key="index" v-for="(obj,index) in advertisingData" @click="$router.push('/front/detail?id=' + obj.goodsId)">
        <div >
          <img :src="obj.img" alt="" style="width: 50%;height: 120px;  ">
        </div>
      </div>
    </div>

    <!--    热门商品展示-->
    <div style="background-color: rgba(255,243,22,0.09);padding: 20px 260px">
      <!--      1 文字部分-->
      <div style="margin-top: 60px;margin-bottom: 60px">
        <div style="text-align: center;margin:30px auto">
          <div style="color: #1f57af; font-size: 28px;font-weight:550;margin-bottom: 20px; font-family: 'Dubai Medium'">热销产品</div>
        </div>
      </div>
      <!--   3热门商品展示-->
      <div >
        <el-row >
          <el-col :span="6" v-for="obj in goodsData" style="margin-bottom: 10px;"  >
            <el-card :body-style="{ padding: '0px' }" style="height: 400px;width: 95%;margin: auto" >
              <img :src="obj.img" alt="" style="width: 100%;height: 300px;" class="image" @click="$router.push('/front/detail?id=' + obj.id)">
              <div style="padding: 14px; text-align: center">
                <div class="overflowShow" style="font-weight: 550;font-size: 16px; color:#1f57af;letter-spacing: 1px;margin-bottom: 24px">{{obj.name}}</div>
                <div class="bottom clearfix">
                  <span class="time">￥{{obj.price}}</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!--      商城介绍-->
    <div style="text-align: center" >
      <div style="color: #666666; font-size: 30px;margin-bottom: 30px">四季更替 “邮”我陪你</div>
      <div style="color: #747070; font-size: 18px;margin-bottom: 20px">官方文创商城 为您精心打造</div>
      <div style="color: #999797; font-size: 14px;margin-bottom: 20px">欢迎来到北邮文创小世界！我们提供高质量的产品和优质的服务，让您的购物体验更加愉快和舒适。 </div>
      <div style="color: #999797; font-size: 14px;margin-bottom: 2px">在这里，您可以轻松地浏览各种商品，并享受优惠的价格和促销活动。</div>
    </div>
  </div>
</template>

<style>
.el-card {
  transition: box-shadow 0.3s, transform 0.3s; /* 平滑过渡效果 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 初始阴影 */
}

.el-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 悬停时更深的阴影 */
  transform: translateY(-5px); /* 轻微上移，产生弹起效果 */
}

/* 定义关键帧动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 应用动画到每个卡片 */
.el-row {
  animation-name: fadeIn; /* 引用关键帧动画 */
  animation-duration: 1s; /* 动画持续时间 */
  animation-delay: 1s;
  animation-fill-mode: both; /* 动画前后的状态 */
  opacity: 0; /* 初始透明度 */
  transform: translateY(20px); /* 初始位置 */
  transition: opacity 0.3s, transform 0.3s; /* 平滑过渡效果 */
}
</style>

<script>
import request from "@/utils/request";

export default {

  data() {
    return {
			carousel_1_1: [],
      activeName: '',
      typeData: [],
      goodsData: [],
      advertisingData: [],
    }
  },
  // 监听路由
  watch: {
    $route(to, from) {
      if(to.path === '/front/index') {
        location.reload()
      }
    }
  },
  mounted() {
    this.load()
    this.loadAdvertising()
    this.loadBanner()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    //加载热门商品
    load() {
      request.get("/goods/hotgoods").then(res => {
        if (res.code === '0') {
          this.goodsData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    //加载广告位
    loadAdvertising() {
      request.get("/advertising").then(res => {
        if (res.code === '0') {
          this.advertisingData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
   //  加载轮播图
   loadBanner(){
     request.get("/banner").then(res => {
       if (res.code === '0'){
         this.carousel_1_1 = res.data;
         console.log(this.carousel_1_1)
       } else {
         this.$notify.error(res.msg);
       }
     })
   },
    handleClick(tab) {
      this.load(tab.name)
    },
  }
}
</script>
