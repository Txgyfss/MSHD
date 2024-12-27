
<template>
  <div class="login-container">
    <div style="display: flex">

      <el-form style="padding-left: 600px">
        <el-form-item class="form-box" style="padding-top: 50px;border-radius: 10px">
          <el-form-item >
            <el-input style="margin-bottom: 30px;" size="large" prefix-icon="el-icon-s-custom" placeholder="请输入用户名" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item >
            <el-input style="margin-bottom: 30px" size="large" show-password prefix-icon="el-icon-key" placeholder="请输入密码" v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item >
            <el-select style="width: 100%;margin-bottom: 30px" size="large"  placeholder="请选择角色" v-model="form.role">
              <el-option value="1" label="管理员"></el-option>
              <el-option value="2" label="用户"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%;height: 50px;background-color: #fdd85e;border-color: #fdd85e;color:white;font-size:20px;" size="large" @click="login()">登录</el-button>
          </el-form-item>
        </el-form-item>
        <el-form-item style="background-color: #28a4fd; border-radius: 10px;color: white;display: flex; justify-content: center;align-items: center">
          <el-form-item class="regBox">
            <a href="javascript:void(0)" @click="navRegister()">注册新账号</a><span class="el-icon-right"></span>
          </el-form-item>

        </el-form-item>

      </el-form>
    </div>
  </div>
</template>
<script>

import request from "@/utils/request";

export default {
  name: "LoginView",
  data() {
    return {
      form: {}
    }
  },
  // 页面加载的时候，做一些事情，在created里面
  mounted() {

  },
  // 定义一些页面上控件出发的事件调用的方法
  methods: {
    // 登录按钮点击事件入口
    login() {
      // 页面表单数据的输入校验
      if (!this.form.username) {
        this.$notify.error("请输入用户名");
        return;
      }
      if (!this.form.password) {
        this.$notify.error("请输入密码");
        return;
      }
      if (!this.form.role) {
        this.$notify.error("请选择角色");
        return;
      }
      // 调用后台登录接口
      request.post('/login', this.form).then(res => {
        if(res.code === '0') {
          // 将用户信息保存一份到浏览器缓存里
          let user = res.data.user;
          let token = res.data.token;
          localStorage.setItem('user', JSON.stringify(user));
          localStorage.setItem('token', token);
          this.$notify.success("登录成功");
          // 登录成功后跳转到个人信息页
          if (user.role === 1) {
            location.href = "/end/pAdmin"
          }
          if (user.role === 2) {
            location.href = "/front/index"
          }
        } else {
          this.$notify.error(res.msg);
        }
      });
    },
    navRegister() {
      this.$router.push("/register")
    }
  }
}
</script>


<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/login_bg.jpg");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-box {
  width: 400px;
  height: 375px;
  padding: 20px 40px;
  border-radius: 2px;
  background-color: rgba(255,255,255,0);
  opacity: 1;
}
/deep/.el-form-item {
  margin-bottom: 5px;
}
/deep/.el-input__inner {
  border-color: #ccc !important;
  border-radius: 2px;
  background-color: #ffffff !important;
  color: #333;
}
/deep/.el-input__icon {
  color: #666;
}

a {
  font-size: 16px;
  color: white;
  text-indent: 12px;
  text-decoration: none;
  font-weight: bold;
}
.regBox {
  font-size: 28px;
  margin-bottom: 10px;
  margin-top: 10px;
  text-align: right;
}
</style>