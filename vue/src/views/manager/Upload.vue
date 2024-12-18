<template>
  <div class="main-body-content">
    <div class="main-body-header">灾情信息上传</div>
    <!-- 上传表单 -->
    <el-form :model="form" label-position="right" label-width="100px" class="form-container">

      <!-- 灾情码输入 -->
      <el-form-item label="灾情码">
        <el-input
            size="small"
            v-model="disasterCode"
            placeholder="请输入36位灾情码"
            @blur="parseDisasterCode"
        ></el-input>
      </el-form-item>

      <!-- 地理位置 -->
      <el-form-item label="灾情地理位置">
        <el-input size="small" v-model="form.location" placeholder="请输入灾情发生地区"></el-input>
      </el-form-item>

      <!-- 时间 -->
      <el-form-item label="灾情时间">
        <el-input size="small" v-model="form.time" placeholder="请输入灾情时间"></el-input>
      </el-form-item>

      <!-- 来源 -->
      <el-form-item label="灾情来源">
        <el-input size="small" v-model="form.source" placeholder="请输入灾情来源"></el-input>
      </el-form-item>

      <!-- 载体类型 -->
      <el-form-item label="灾情载体类型">
        <el-input size="small" v-model="form.carrier" placeholder="请输入灾情载体类型"></el-input>
      </el-form-item>

      <!-- 灾情分类 -->
      <el-form-item label="灾情分类">
        <el-input size="small" v-model="form.disasterType" placeholder="请输入灾情信息分类"></el-input>
      </el-form-item>

      <!-- 灾情指标 -->
      <el-form-item label="灾情指标分类">
        <el-input size="small" v-model="form.indicator" placeholder="请输入灾情指标分类"></el-input>
      </el-form-item>

      <!-- 灾情描述（富文本） -->
      <el-form-item label="灾情描述">
        <div id="wang-editor"></div>
      </el-form-item>

      <!-- 上传文件 -->
      <el-form-item label="上传文件">
        <el-upload
            action="http://localhost:8080/files/upload"
            ref="disasterFile"
            :on-success="fileUploadSuccess"
            list-type="text"
        >
          <el-button size="small" type="success">点击上传文件</el-button>
        </el-upload>
      </el-form-item>

      <!-- 提交和重置 -->
      <el-form-item class="form-actions">
        <el-button size="small" type="primary" @click="submitForm">提交</el-button>
        <el-button size="small" type="warning" @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import request from "@/utils/request";
import wangEditor from "wangeditor";

let editor;
let baseUrl = "/disaster/";

function initWangEditor(content) {
  setTimeout(() => {
    if (!editor) {
      editor = new wangEditor("#wang-editor");
      editor.config.placeholder = "请输入灾情描述";
      editor.config.uploadFileName = "file";
      editor.config.uploadImgServer = "http://localhost:8080/files/wang/upload";
      editor.create();
    }
    editor.txt.html(content);
  }, 0);
}

export default {
  data() {
    return {
      form: {
        location: '',
        time: '',
        source: '',
        carrier: '',
        disasterType: '',
        indicator: '',
        description: ''
      }, // 表单数据
      disasterCode: '', // 灾情码输入框
      typeData: [] // 灾情分类数据
    };
  },
  mounted() {
    this.loadType(); // 加载灾情分类数据
    initWangEditor(""); // 初始化富文本编辑器
  },
  methods: {
    // 解析灾情码并自动填充
    parseDisasterCode() {
      if (this.disasterCode.length === 36) {
        this.form.location = this.disasterCode.slice(0, 12); // 地理位置
        this.form.time = this.disasterCode.slice(12, 26); // 时间
        this.form.source = this.disasterCode.slice(26, 29); // 来源
        this.form.carrier = this.disasterCode.slice(29, 30); // 载体类型
        this.form.disasterType = this.disasterCode.slice(30, 33); // 灾情信息分类
        this.form.indicator = this.disasterCode.slice(33, 36); // 灾情指标分类
      }
    },

    // 保存灾情信息
    submitForm() {
      this.form.description = editor.txt.html();
      if (!this.form.id) {
        // 新增灾情
        request.post(baseUrl, this.form).then(res => {
          if (res.code === "0") {
            this.$notify.success("灾情信息上传成功");
            this.resetForm();
          } else {
            this.$notify.error(res.msg);
          }
        });
      } else {
        // 编辑灾情
        request.put(baseUrl, this.form).then(res => {
          if (res.code === "0") {
            this.$notify.success("灾情信息更新成功");
            this.resetForm();
          } else {
            this.$notify.error(res.msg);
          }
        });
      }
    },

    // 上传文件成功回调
    fileUploadSuccess(res) {
      // 假设上传的文件名就是灾情码
      this.disasterCode = res.data.fileName;
      this.parseDisasterCode(); // 解析文件名并填充表单
    },

    // 重置表单
    resetForm() {
      this.form = {};
      this.disasterCode = '';
      editor.txt.clear();
      this.$nextTick(() => {
        this.$refs.disasterFile.clearFiles();
      });
    },

    // 加载灾情分类
    loadType() {
      request.get("/type").then(res => {
        if (res.code === "0") {
          this.typeData = res.data;
        } else {
          this.$notify.error(res.msg);
        }
      });
    }
  }
};
</script>

<style scoped>
.main-body-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  height: 100%;
  max-width: 100%;
}

.main-body-header {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 15px;
}

.form-container {
  max-width: 800px;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

.el-form-item {
  margin-bottom: 12px;
}

.el-button {
  margin-right: 10px;
}

.form-actions {
  display: flex;
  justify-content: space-between;
}

#wang-editor {
  max-height: 200px;
  overflow-y: auto;
}
</style>
