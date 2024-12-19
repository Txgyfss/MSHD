<template>
  <div class="main-body-content">
    <div class="main-body-header">灾情信息上传</div>
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
        <el-select size="small" v-model="form.location" placeholder="请选择灾情发生地区">
          <el-option v-for="(location, index) in locations" :key="index" :label="location" :value="location"></el-option>
        </el-select>
      </el-form-item>

      <!-- 时间 -->
      <el-form-item label="灾情时间">
        <el-date-picker
            v-model="form.time"
            type="datetime"
            placeholder="请选择灾情时间"
            size="small"
        ></el-date-picker>
      </el-form-item>

      <!-- 来源 -->
      <el-form-item label="灾情来源">
        <el-select size="small" v-model="form.source" placeholder="请选择灾情来源">
          <el-option v-for="(source, index) in sources" :key="index" :label="source" :value="source"></el-option>
        </el-select>
      </el-form-item>

      <!-- 载体类型 -->
      <el-form-item label="灾情载体类型">
        <el-select size="small" v-model="form.carrier" placeholder="请选择灾情载体类型">
          <el-option v-for="(carrier, index) in carriers" :key="index" :label="carrier" :value="carrier"></el-option>
        </el-select>
      </el-form-item>

      <!-- 灾情分类 -->
      <el-form-item label="灾情分类">
        <el-select size="small" v-model="form.disasterType" placeholder="请选择灾情分类">
          <el-option v-for="(type, index) in disasterTypes" :key="index" :label="type" :value="type"></el-option>
        </el-select>
      </el-form-item>

      <!-- 灾情指标分类 -->
      <el-form-item label="灾情指标分类">
        <el-select size="small" v-model="form.indicator" placeholder="请选择灾情指标分类">
          <el-option v-for="(indicator, index) in indicators" :key="index" :label="indicator" :value="indicator"></el-option>
        </el-select>
      </el-form-item>

      <!-- 灾情描述（富文本） -->
      <el-form-item label="灾情描述" class="editor-item">
        <div id="wang-editor" class="editor-container"></div>
      </el-form-item>

      <!-- 上传文件 -->
      <el-form-item label="上传文件" class="upload-item">
        <el-upload
            action="http://localhost:8080/files/upload"
            ref="disasterFile"
            :on-success="fileUploadSuccess"
            list-type="text"
            style="width: 100%;">

          <el-button size="small" type="success">点击上传文件</el-button>
        </el-upload>
      </el-form-item>

      <!-- 提交和重置按钮 -->
      <el-form-item class="form-actions">
        <el-button size="small" type="primary" @click="submitForm">提交</el-button>
        <el-button size="small" type="warning" @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>

<script>
import wangEditor from "wangeditor";
import request from "@/utils/request";

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
      },
      disasterCode: '', // 灾情码输入框
      disasterTypes: [], // 灾情类型数据
      locations: [], // 灾情地点数据
      sources: [], // 灾情来源数据
      carriers: [], // 灾情载体类型数据
      indicators: [] // 灾情指标分类数据
    };
  },
  mounted() {
    this.loadDisasterData(); // 加载灾情相关数据
    initWangEditor(""); // 初始化富文本编辑器
  },
  methods: {
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

    loadDisasterData() {
      // 获取灾情类型数据
      request.get("/disaster/types").then(res => {
        if (res.code === "0") {
          this.disasterTypes = res.data;
        }
      });
      // 获取灾情地点数据
      request.get("/disaster/locations").then(res => {
        if (res.code === "0") {
          this.locations = res.data;
        }
      });
      // 获取灾情来源数据
      request.get("/disaster/sources").then(res => {
        if (res.code === "0") {
          this.sources = res.data;
        }
      });
      // 获取灾情载体类型数据
      request.get("/disaster/carriers").then(res => {
        if (res.code === "0") {
          this.carriers = res.data;
        }
      });
      // 获取灾情指标分类数据
      request.get("/disaster/indicators").then(res => {
        if (res.code === "0") {
          this.indicators = res.data;
        }
      });
    },

    submitForm() {
      this.form.description = editor.txt.html(); // 获取富文本编辑器内容
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

    fileUploadSuccess(res) {
      this.disasterCode = res.data.fileName;
      this.parseDisasterCode(); // 解析文件名并填充表单
    },

    resetForm() {
      this.form = {};
      this.disasterCode = '';
      editor.txt.clear();
      this.$nextTick(() => {
        this.$refs.disasterFile.clearFiles();
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
  min-height: 600px;
}

.editor-container {
  width: 100%; /* 使富文本编辑器占满父容器的宽度 */
  max-width: 800px; /* 限制富文本编辑器最大宽度 */
  height: 50px; /* 调整高度，减少空间占用 */
  margin-bottom: 350px; /* 增加底部间距，避免和上传文件区域重叠 */
  position: relative; /* 保证 z-index 生效 */
  z-index: 1; /* 设置富文本框的层级 */
}

.upload-item {
  margin-bottom: 20px; /* 上传区域增加间距，避免富文本编辑器和上传按钮重叠 */
}

.el-select, .el-option {
  position: relative; /* 保证下拉框的层级 */
  z-index: 10; /* 设置下拉框的层级为较高 */
}

.form-actions {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  width: 100%;
}
</style>

