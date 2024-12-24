<template>
  <div>
    <div class="main-body-header">
      个人信息
    </div>
    <div class="main-body-content">
      <div style="width: 50%; padding: 10px 0">
        <el-form :model="form" label-position="right" label-width="100px" style="padding-right: 40px">
					<el-form-item label="用户名">
						<el-input size="small" v-model="form.username" placeholder="请输入用户名"></el-input>
					</el-form-item>
					<el-form-item label="头像">
						<el-upload action="http://localhost:8080/files/upload" ref="tx" :on-success="txSuccessUpload">
							<el-button size="small" type="success">点击上传</el-button>
						</el-upload>
					</el-form-item>
					<el-form-item label="手机号">
						<el-input size="small" v-model="form.phone" placeholder="请输入手机号"></el-input>
					</el-form-item>
					<el-form-item label="性别">
						<el-radio-group size="small" v-model="form.sex">
							<el-radio label="男"></el-radio>
							<el-radio label="女"></el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="年龄">
						<el-input size="small" v-model="form.age" placeholder="请输入年龄"></el-input>
					</el-form-item>
          <el-form-item label="余额">
            <el-input size="small" v-model="form.account" disabled></el-input>
          </el-form-item>
          <div style="text-align: center; margin-top: 20px">
              <el-button size="small" type="primary" @click="update()">保 存</el-button>
              <el-button size="small" type="warning" @click="init()">充 值</el-button>
          </div>
        </el-form>
      </div>
    </div>
    <el-dialog title="请填写信息" :visible.sync="dialogVisible" width="40%">
      <el-form label-position="right" label-width="100px" style="padding-right: 40px">
        <el-form-item label="充值金额">
          <el-input size="small" v-model="account" placeholder="请输入金额"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="recharge()">充 值</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

// 模块接口请求统一前缀
let baseUrl = "/user/"

export default {
  data() {
    return {
      user: {},
      form: {},
      dialogVisible: false,
      account: null
    }
  },
  // 页面加载的时候，做一些事情，在created里面
  mounted() {
    // 初始化当前登录用户信息
    this.getPerson();

  },
  // 定义一些页面上控件出发的事件调用的方法
  methods: {
    // 获取当前登录用户信息
    getPerson() {
      request.get("/getUser").then(res => {
        if (res.code === '0') {
          this.form = res.data;
          localStorage.setItem("user", JSON.stringify(res.data))
        } else {
          this.$notify.error(res.msg);
        }
      })
    },

    // 更新当前用户信息
    update() {
      request.put(baseUrl, this.form).then(res => {
        if (res.code === '0') {
          this.$notify.success("更新成功");
          this.$bus.$emit('updateTx', this.form.avatar)
        } else {
          this.$notify.error(res.msg);
        }
      })
    },

		txSuccessUpload(res) {
			this.form.avatar = res.data;
		},

    init() {
      this.account = 100
      this.dialogVisible = true;
    },

    recharge() {
      let data = {id: this.form.id, account: parseFloat(this.form.account) + parseFloat(this.account)};
      request.put("/user", data).then(res => {
        if (res.code === '0') {
          this.$notify.success('充值成功');
          this.dialogVisible = false;
          this.getPerson();
        } else {
          this.$notify.error(res.msg);
        }
      })
    },
  }
}
</script>
