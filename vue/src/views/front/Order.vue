<template>
  <div class="main-content">
    <div  style="padding: 0 260px">
      <div class="main-table-box">
        <div style="height: 50px; line-height: 50px; font-weight: bold; font-size: 20px;margin-bottom: 40px; color: #1f57af">我的订单</div>
        <el-table style="width: 100%" :data="orderData" size="small" strip header-cell-class-name="tableHeaderClass" >
          <el-table-column label="商品" width="460px">
            <template v-slot="scope">
              <div style="display: flex; align-items: center; margin-bottom: 5px">
                <el-image :src="scope.row.goods.img" :preview-src-list="[scope.row.goods.img]" style="width: 40px; height: 40px; border-radius: 50%"></el-image>
                <div style="flex: 1; font-size: 14px; margin-left: 10px">{{scope.row.goods.name}} x {{scope.row.num}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="订单编号" prop="orderId" width="150px"></el-table-column>
          <el-table-column label="下单人" prop="userName"></el-table-column>
          <el-table-column label="下单时间" prop="time" width="150px"></el-table-column>
          <el-table-column label="订单价格" prop="price"></el-table-column>
          <el-table-column label="收货人" prop="name"></el-table-column>
          <el-table-column label="收货地址" prop="address"></el-table-column>
          <el-table-column label="联系方式" prop="phone" width="100px"></el-table-column>
          <el-table-column label="订单状态" prop="status"></el-table-column>
          <el-table-column label="操作" fixed="right" width="250">
            <template v-slot="scope">
              <el-button type="primary" v-if="scope.row.status === '已发货'" @click="updateStatus(scope.row, '已完成')" style="margin-right: 5px">确认收货</el-button>
              <el-button type="primary" v-if="scope.row.status === '已完成'" @click="updateStatus(scope.row, '退款申请中')" style="margin-right: 5px">退款</el-button>
              <el-button type="primary" v-if="scope.row.status === '已完成'" @click="openDialog(scope.row.goodsId)" style="margin-right: 5px">评价</el-button>
              <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
                <el-button size="small" type="danger" slot="reference">删除</el-button>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog title="请填写评价信息" :visible.sync="dialogVisible" width="40%">
      <el-form label-position="right" label-width="100px" style="padding-right: 40px">
        <el-form-item label="评价内容">
          <el-input size="small" type="textarea" v-model="content" placeholder="请输入评价内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="save">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem("user") || {}),
      orderData: [],
      content: '',
      goodsId: '',
      dialogVisible: false,
    }
  },
  mounted() {
    this.load()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load() {
      request.get("/order").then(res => {
        if (res.code === '0') {
          this.orderData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    del(id) {
      request.delete("/order/" + id).then(res => {
        if (res.code === '0') {
          this.$notify.success("删除成功")
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    updateStatus(data, status) {
      data.status = status
      request.put("/order", data).then(res => {
        if (res.code === '0') {
          this.$notify.success("操作成功")
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    openDialog(goodsId) {
      this.goodsId = goodsId
      this.dialogVisible = true;
    },
    save() {
      if (!this.content) {
        this.$notify.warning("请输入内容")
      }
      let data = {content: this.content, userId: this.user.id, role: this.user.role, parentId: 0, relId: this.goodsId}
      request.post("/comment", data).then(res => {
        if (res.code ==='0') {
          this.$notify.success("评价成功")
          this.dialogVisible = false
          this.content = ""
          this.goodsId = ""
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>
