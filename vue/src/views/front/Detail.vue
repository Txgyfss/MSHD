<template>
  <div class="detail-background"
       style="background:rgba(255,208,0,0.1)">
  <div class="detail-container" style="background: rgba(255,255,255,0.62); width:80%">
    <div class="detail-title" style="color:#1f57af; font-weight: bold; font-size:20px">详情信息</div>
    <div class="detail-content" style="margin-left: 80px">
      <div class="detail-left-img-fields">
        <div style="flex: 1;"><img :src="goods.img" style="width: 100%; height: 440px; margin-top: 10px; border-radius: 5px; border: #e3e3e3 solid 1px"></div>
        <div style="flex: 2; margin-left: 180px">
          <div class="detail-fields-item overflowShow" style="font-size: 18px;color:#163e7c;font-weight: 550;">{{goods.name}}</div>
          <div class="detail-fields-item overflowShow" style="font-size: 18px;color:#000000;font-weight: 550;background:linear-gradient(to right, #ffb147, rgb(255,243,22));;color:white;width: 500px;height: 100px;
          text-align: left;line-height: 100px;padding: 0 20px">
            <div style="width: 500px;height: 100px;display:flex;text-align: left;align-items: center;">
              <span style="font-size: 20px">￥</span>
              <span style="font-size: 40px">{{goods.price}}</span>
              <span style="font-size: 20px;margin-left: 10px;font-weight: lighter;text-decoration: line-through">￥{{goods.originalprice}}</span>
              <img :src="carousel_1_1"  alt="" style="width: 140px;margin-left: 150px;">
            </div>
          </div>
          <div class="detail-fields-item overflowShow">
            <span style="font-size: 14px;color: #8c8a8a; margin-right: 10px; font-weight: bold">商品地区</span>
            <span style="font-size: 15px;color: rgba(7,7,108,0.8); font-weight: bold">{{goods.location}}</span>
          </div>
          <div class="detail-fields-item overflowShow">
            <span style="font-size: 14px;color: #8c8a8a; margin-right: 10px; font-weight: bold">商品型号</span>
            <span style="font-size: 15px;color: rgba(7,7,108,0.8); font-weight: bold">{{goods.goodsmodel}}</span>
          </div>
          <div class="detail-fields-item overflowShow">
            <span style="font-size: 14px;color: #8c8a8a; margin-right: 10px; font-weight: bold">商品运费</span>
            <span style="font-size: 15px;color: rgba(7,7,108,0.8); font-weight: bold">新用户专享首单满38元免邮（部分商品不可用）</span>
          </div>
          <div class="detail-fields-item overflowShow">
            <span style="font-size: 14px;color: #8c8a8a;  margin-right: 10px; font-weight: bold">购买数量</span>
            <el-input-number :value="1" size="small" style="width: 100px;" v-model="num"></el-input-number>
          </div>
          <div class="detail-fields-item overflowShow" style="margin-top: 50px">
            <el-button style="width: 200px;height: 60px;background-color: #ffd000;color: white;margin-left: 40px;border:none;border-radius: 2%;" @click="addCart">
              <span style="font-size:16px; font-weight:bold">加入购物车</span>
            </el-button>
            <el-button style="width: 200px;height: 60px;background-color: #1673ff;color: white;margin-left: 40px; border:none;border-radius: 2%;;" @click="buy">
              <span style="font-size:16px; font-weight:bold">支付</span>
            </el-button>
          </div>
        </div>
      </div>
    </div>

      <!--   富文本详情页部分   -->
      <div class="detail-richtext">
        <div class="detail-richtext-title">{{goods.name}}</div>
        <div class="detail-richtext-content w-e-text" v-html="goods.description"></div>
      </div>
    </div>
    <div class="detail-title">评论信息</div>
    <div class="detail-comment">
      <div>
        <el-row v-for="item in comments" style="margin-bottom: 10px">
          <el-col :span="4" style="display: flex; align-items: center;">
            <img :src="item.avatar" style="width: 50px; height: 50px; border-radius: 50%;">
            <div style="margin-left: 10px; flex: 1" class="overflowShow">{{item.userName}}</div>
          </el-col>
          <el-col :span="20">
            <el-row>
              <el-col :span="20" style="height: 50px; line-height: 50px">{{item.content}}</el-col>
              <el-col :span="4" style="height: 50px; line-height: 50px; text-align: right">{{item.time}}</el-col>
            </el-row>
            <el-row :span="3" style="margin-top: 20px" v-for="child in item.children">
              <el-col :span="4" style="display: flex; align-items: center;">
                <img :src="child.avatar" style="width: 50px; height: 50px; border-radius: 50%;">
                <div style="margin-left: 10px; flex: 1" class="overflowShow">{{child.userName}} 回复：</div>
              </el-col>
              <el-col :span="16" style="height: 50px; line-height: 50px">{{child.content}}</el-col>
              <el-col :span="4" style="height: 50px; line-height: 50px; text-align: right">{{child.time}}</el-col>
            </el-row>

            <el-row :span="3" style="margin-top: 20px">
              <el-col :span="15">
                <el-input v-model="item.reply"></el-input>
              </el-col>
              <el-col :span="4" style="margin-left: 10px">
                <el-button type="primary" @click="submit(item.id, item.reply)">回复</el-button>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>
    </div>
    <el-dialog title="请选择收货地址" :visible.sync="dialogVisible" width="40%">
      <el-form :model="goods" label-position="right" label-width="100px" style="padding-right: 40px">
        <el-form-item>
          <span style="font-size: 12px;color: #8c8a8a;  margin-right: 10px">收货人</span>
          <el-select size="small" style="width: 80%" placeholder="请选择收货人" v-model="addressId">
            <el-option v-for="item in addressList" :value="item.id" :label="item.username" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <span style="font-size: 12px;color: #8c8a8a;  margin-right: 10px">配送地址</span>
          <el-select size="small" style="width: 80%" placeholder="请选择收货地址" v-model="addressId">
            <el-option v-for="item in addressList" :value="item.id" :label="item.address" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small"  @click="save" style="background-color: #1673ff; color: white;">支 付</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {

  data() {
    let goodsId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem("user") || {}),
      goods: {},
      goodsId: goodsId,
      buyNum: 1,
      comments: [],
      addressList: [],
      dialogVisible: false,
      num: 1,
      content: '',
      addressId: null,
      carousel_1_1: [
          require("@/assets/imgs/jingxi.png")
      ],

    }
  },
  mounted() {
    this.load()
    this.loadComment()
    this.loadAddress()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load() {
      request.get('/goods/' + this.goodsId).then(res => {
        this.goods = res.data
      })
    },
    loadComment() {
      request.get("/comment/" + this.goodsId).then(res => {
        if (res.code === '0') {
          this.comments = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    loadAddress(){
      request.get("/address").then(res => {
        if (res.code === '0') {
          this.addressList = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    buy() {
      this.dialogVisible = true;
    },
    save() {
      if (!this.user && !this.user.username) {
        this.$notify.error("登录失效，请重新登陆")
        this.$router.push("/login")
        return;
      }
      if (this.user.role === 1) {
        this.$notify.warning("您是管理员，不支持该操作")
        return;
      }
      if (!this.addressId) {
        this.$notify.warning("请选择收货地址,如没有地址，请到地址管理设置地址")
        return
      }
      this.goods.userId = this.user.id;
      this.goods.goodsId = this.goods.id;
      this.goods.addressId = this.addressId;
      this.goods.num = this.num;
      this.goods.id = null;
      request.post("/order/buy/one", this.goods).then(res => {
        if (res.code === '0') {
          this.$notify.success("购买成功")
          this.$router.push("/front/order")
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    addCart() {
      if (!this.user && !this.user.username) {
        this.$notify.error("登录失效，请重新登陆")
        this.$router.push("/login")
        return;
      }
      if (this.user.role === 1) {
        this.$notify.warning("您是管理员，不支持该操作")
        return;
      }
      let data = {
        userId: this.user.id,
        goodsId: this.goods.id,
        num: this.num,
      }
      request.post("/cart", data).then(res => {
        if (res.code === '0') {
          this.$notify.success("加入成功")
          this.$router.push("/front/cart")
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    submit(parentId, content) {
      if (!content) {
        this.$notify.warning("请输入内容")
      }
      let data = {content: content, userId: this.user.id, role: this.user.role, parentId: parentId, relId: this.goodsId}
      request.post("/comment", data).then(res => {
        if (res.code ==='0') {
          this.$notify.success("评论成功")
          this.loadComment()
          this.content = ""
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>
