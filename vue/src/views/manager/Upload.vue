<template>
  <div class="main-body-content">
    <div class="main-body-header">灾情信息上传</div>

    <!-- 编码部分 -->
    <el-card class="form-card" :body-style="{ padding: '20px' }">
      <div class="form-title">灾情信息编码</div>
      <el-form :model="form" label-position="right" label-width="120px">
        <el-row>
          <!-- 地理位置 -->
          <el-col :span="12">
            <el-form-item label="灾情地理位置">
              <el-select size="small" v-model="form.location" placeholder="请选择灾情发生地区">
                <el-option v-for="(location, index) in locations" :key="index" :label="location" :value="location"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <!-- 时间 -->
          <el-col :span="12">
            <el-form-item label="灾情时间">
              <el-date-picker
                  v-model="form.time"
                  type="datetime"
                  placeholder="请选择灾情时间"
                  size="small"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <!-- 来源 -->
          <el-col :span="12">
            <el-form-item label="灾情来源">
              <el-select size="small" v-model="form.source" placeholder="请选择灾情来源">
                <el-option v-for="(source, index) in sources" :key="index" :label="source" :value="source"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <!-- 载体类型 -->
          <el-col :span="12">
            <el-form-item label="灾情载体类型">
              <el-select size="small" v-model="form.carrier" placeholder="请选择灾情载体类型">
                <el-option v-for="(carrier, index) in carriers" :key="index" :label="carrier" :value="carrier"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <!-- 灾情分类 -->
          <el-col :span="12">
            <el-form-item label="灾情分类">
              <el-select size="small" v-model="form.disasterType" placeholder="请选择灾情分类">
                <el-option v-for="(type, index) in disasterTypes" :key="index" :label="type" :value="type"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <!-- 灾情指标分类 -->
          <el-col :span="12">
            <el-form-item label="灾情指标分类">
              <el-select size="small" v-model="form.indicator" placeholder="请选择灾情指标分类">
                <el-option v-for="(indicator, index) in indicators" :key="index" :label="indicator" :value="indicator"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 富文本框，放在编码部分 -->
        <el-form-item label="灾情描述" style="margin-top: 10px;">
          <el-input
              type="textarea"
              size="small"
              v-model="form.description"
              placeholder="请输入灾情描述"
              rows="4"
              style="resize: none; width: 100%;"
          ></el-input>
        </el-form-item>

        <!-- 生成灾情码按钮 -->
        <el-form-item>
          <el-button size="small" type="primary" @click="generateDisasterCode">生成灾情码</el-button>
        </el-form-item>
      </el-form>

      <div class="disaster-code-display">
        <el-input size="small" :value="disasterCode" placeholder="生成的灾情码" readonly></el-input>
      </div>
    </el-card>

    <!-- 解码部分 -->
    <el-card class="form-card" :body-style="{ padding: '20px' }">
      <div class="form-title">灾情信息解码</div>
      <el-form :model="form" label-position="right" label-width="120px">
        <el-form-item label="灾情码">
          <el-input
              size="small"
              v-model="disasterCodeInput"
              placeholder="请输入灾情码"
          ></el-input>
        </el-form-item>

        <el-form-item label="上传文件">
          <el-upload
              action="http://localhost:8080/files/upload"
              ref="disasterFile"
              :on-success="fileUploadSuccess"
              list-type="text"
              style="width: 100%;"
          >
            <el-button size="small" type="success">点击上传文件</el-button>
          </el-upload>
        </el-form-item>

        <!-- 增加margin-top来拉开“解码灾情码”按钮与上传文件之间的间距 -->
        <el-form-item style="margin-top: 15px;">
          <el-button size="small" type="primary" @click="decodeDisasterCode">解码灾情码</el-button>
        </el-form-item>

        <el-row>
          <!-- 解码后的地理位置 -->
          <el-col :span="12">
            <el-form-item label="灾情地理位置">
              <el-input size="small" :value="decodedData.location" readonly></el-input>
            </el-form-item>
          </el-col>

          <!-- 解码后的时间 -->
          <el-col :span="12">
            <el-form-item label="灾情时间">
              <el-input size="small" :value="decodedData.time" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <!-- 解码后的来源 -->
          <el-col :span="12">
            <el-form-item label="灾情来源">
              <el-input size="small" :value="decodedData.source" readonly></el-input>
            </el-form-item>
          </el-col>

          <!-- 解码后的载体类型 -->
          <el-col :span="12">
            <el-form-item label="灾情载体类型">
              <el-input size="small" :value="decodedData.carrier" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <!-- 解码后的灾情分类 -->
          <el-col :span="12">
            <el-form-item label="灾情分类">
              <el-input size="small" :value="decodedData.disasterType" readonly></el-input>
            </el-form-item>
          </el-col>

          <!-- 解码后的灾情指标分类 -->
          <el-col :span="12">
            <el-form-item label="灾情指标分类">
              <el-input size="small" :value="decodedData.indicator" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 解码后的描述 -->
        <el-form-item label="灾情描述">
          <el-input type="textarea" size="small" :value="decodedData.description" readonly></el-input>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";  // 使用封装的 axios 实例

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
      disasterCode: '',
      disasterCodeInput: '',
      decodedData: {
        location: '',
        time: '',
        source: '',
        carrier: '',
        disasterType: '',
        indicator: '',
        description: ''
      },
      locations: [],
      sources: [],
      carriers: [],
      disasterTypes: [],
      indicators: [],
    };
  },
  methods: {

    // 获取后端的地理位置、灾情来源等数据
    async fetchData() {
      try {
        const locationsResponse = await request.get('http://localhost:8080/disaster/locations');
        console.log('Locations Response:', locationsResponse);  // 打印响应内容
        const sourcesResponse = await request.get('http://localhost:8080/disaster/sources');
        const carriersResponse = await request.get('http://localhost:8080/disaster/carriers');
        const disasterTypesResponse = await request.get('http://localhost:8080/disaster/types');
        const indicatorsResponse = await request.get('http://localhost:8080/disaster/indicators');

        this.locations = locationsResponse.data;
        this.sources = sourcesResponse.data;
        this.carriers = carriersResponse.data;
        this.disasterTypes = disasterTypesResponse.data;
        this.indicators = indicatorsResponse.data;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    // 生成灾情码的函数
    generateDisasterCode() {
      this.disasterCode = this.form.location + this.form.time + this.form.source + this.form.carrier + this.form.disasterType + this.form.indicator;
    },

    // 解码灾情码
    decodeDisasterCode() {
      // 假设后端已经提供了一个API来解码灾情码
      request.get(`/disaster/decode/${this.disasterCodeInput}`)
          .then((response) => {
            this.decodedData = response.data;
          })
          .catch((error) => {
            console.error('Error decoding disaster code:', error);
          });
    },

    // 文件上传成功
    fileUploadSuccess(response) {
      console.log('File uploaded successfully:', response);
    },
  },
  mounted() {
    this.fetchData();
  }
};
</script>
