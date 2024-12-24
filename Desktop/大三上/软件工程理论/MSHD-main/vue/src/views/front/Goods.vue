<template>
  <div class="main-content">
    <div class="main-line" style="padding: 40px 260px">
      <el-image :src="carousel_1_2" style="width:100%;height: 400px">
        <div slot="placeholder" class="image-slot">
          加载中<span class="dot">...</span>
        </div>
      </el-image>
    </div>
    <div class="main-line" style="margin-top: 40px;padding: 40px 260px">
      <div class="main-line-1">
        <el-tabs :tab-position="tabPosition" v-model="activeName" @tab-click="handleClick" >
          <el-tab-pane v-for="item in typeData" :label="item.name" :name="item.id + ''" :key="item.id" >
            <el-row >
              <el-col :span="6" v-for="obj in goodsData" style="margin-bottom: 10px;"  >
                <el-card :body-style="{ padding: '0px' }" style="height: 400px;width: 95%;margin: auto" >
                  <img :src="obj.img" alt="" style="width: 100%;height: 300px;" class="image" @click="$router.push('/front/detail?id=' + obj.id)">
                  <div style="padding: 14px; text-align: center">
                    <div class="overflowShow" style="font-weight: 550;font-size: 20px; color:#1673ff;letter-spacing: 1px;margin-bottom: 24px">{{obj.name}}</div>
                    <div class="bottom clearfix">
                      <span class="time">￥{{obj.price}}</span>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <div>

    </div>

  </div>
</template>

<script>
import request from "@/utils/request";

export default {

  data() {
    return {
			carousel_1_1: [
        require('@/assets/imgs/lun-1.jpg'),
        require('@/assets/imgs/lun-2.jpg'),
      ],
      carousel_1_2: require('@/assets/imgs/goods.jpg'),
      activeName: '',
      typeData: [],
      goodsData: [],
      tabPosition: 'right'
    }
  },
  // 监听路由
  watch: {
    $route(to, from) {
      if(to.path === '/front/index') {
        console.log("hahah")
        location.reload()
      }
    }
  },
  mounted() {
    request.get("/type").then(res => {
      if (res.code === '0') {
        this.typeData = res.data;
        this.load(this.typeData[0].id)
      } else {
        this.$notify.error(res.msg)
        if (res.code === '401') {
          this.$router.push("/login")
        }
      }
    })
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load(typeId) {
      this.activeName = typeId + ''
      request.get("/goods/type/" + typeId).then(res => {
        if (res.code === '0') {
          this.goodsData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    handleClick(tab) {
      this.load(tab.name)
    },
  }
}
</script>
