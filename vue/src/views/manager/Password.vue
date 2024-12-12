<template>
  <div>
    <div class="main-body-header">修改密码</div>
    <div class="main-body-content">
      <div style="width: 50%; padding: 10px 0">
        <el-form :model="form" label-position="right" label-width="100px" style="padding-right: 40px">
          <el-form-item label="原密码">
            <el-input size="small" v-model="form.password" placeholder="请输入原密码"></el-input>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input size="small" v-model="form.nmm" placeholder="请输入新密码"></el-input>
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input size="small" v-model="form.n2mm" placeholder="请确认密码"></el-input>
          </el-form-item>
          <div style="text-align: center; margin-top: 20px">
            <el-button size="small" type="primary" @click="update()">保 存</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {
      user: {},
      form: {}
    }
  },
  // 页面加载的时候，做一些事情，在created里面
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'));
  },
  // 定义一些页面上控件出发的事件调用的方法
  methods: {
    update() {
      if (!this.form.password) {
        this.$notify.error("请输入原密码");
        return;
      }
      if (!this.form.nmm) {
        this.$notify.error("请输入新密码");
        return;
      }
      if (!this.form.n2mm) {
        this.$notify.error("请确认新密码");
        return;
      }
      if (this.form.nmm !== this.form.n2mm) {
        this.$notify.error("两次新密码输入不一致");
        return;
      }
      request.put("/updatePassword", this.form).then(res => {
        if (res.code === '0') {
          this.$notify.success("密码修改成功");
          this.$router.push("/login")
        } else {
          this.$notify.error(res.msg);
        }
      })
    },
  }
}
</script>
