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
              <el-cascader
                  size="small"
                  v-model="form.location"
                  :options="locationOptions"
                  :props="cascaderProps"
                  placeholder="请选择完整的地理位置"
                  @change="handleCascaderChange"
                  change-on-select
              ></el-cascader>
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
        location: [],
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
      locationOptions: [],  // 存储级联下拉框数据

      // 完整的地理位置数据
      sources: [],
      carriers: [],
      disasterTypes: [],
      indicators: [],
      cascaderProps: {
        value: 'code',  // 绑定选择项的字段
        label: 'name',  // 显示的标签字段
        children: 'children',  // 子项的字段名
      }
    };
  },
  methods: {
    // 获取省份数据
    async fetchProvinces() {
      try {
        const response = await request.get('http://localhost:8080/api/areas/getProvinces');
        return response.data.map(province => ({
          code: province.code,
          name: province.name,
          children: []  // 初始化为空，后续会填充城市数据
        }));
      } catch (error) {
        console.error('Error fetching provinces:', error);
      }
    },

    // 获取城市数据
    async fetchCities(provinceCode) {
      try {
        const response = await request.get(`http://localhost:8080/api/areas/getCities?provinceCode=${provinceCode}`);
        return response.data.map(city => ({
          code: city.code,
          name: city.name,
          children: []  // 初始化为空，后续会填充县区数据
        }));
      } catch (error) {
        console.error('Error fetching cities:', error);
      }
    },

    // 获取县区数据
    async fetchCounties(cityCode) {
      try {
        const response = await request.get(`http://localhost:8080/api/areas/getCounties?cityCode=${cityCode}`);
        return response.data.map(county => ({
          code: county.code,
          name: county.name,
          children: []  // 初始化为空，后续会填充乡镇数据
        }));
      } catch (error) {
        console.error('Error fetching counties:', error);
      }
    },

    // 获取乡镇数据
    async fetchTowns(areaCode) {
      try {
        const response = await request.get(`http://localhost:8080/api/areas/getTowns?areaCode=${areaCode}`);
        return response.data.map(town => ({
          code: town.code,
          name: town.name,
          children: []  // 初始化为空，后续会填充村庄数据
        }));
      } catch (error) {
        console.error('Error fetching towns:', error);
      }
    },

    // 获取村庄数据
    async fetchVillages(streetCode) {
      try {
        const response = await request.get(`http://localhost:8080/api/areas/getVillages?streetCode=${streetCode}`);
        return response.data.map(village => ({
          code: village.code,
          name: village.name
        }));
      } catch (error) {
        console.error('Error fetching villages:', error);
      }
    },

    // 级联选择变化时的处理
    async handleCascaderChange(value, selectedData) {
      const [provinceCode, cityCode, countyCode, townCode, villageCode] = value;

      if (provinceCode) {
        const cities = await this.fetchCities(provinceCode);
        this.updateChildren(selectedData, cities);
      }
      if (cityCode) {
        const counties = await this.fetchCounties(cityCode);
        this.updateChildren(selectedData, counties);
      }
      if (countyCode) {
        const towns = await this.fetchTowns(countyCode);
        this.updateChildren(selectedData, towns);
      }
      if (townCode) {
        const villages = await this.fetchVillages(townCode);
        this.updateChildren(selectedData, villages);
      }
    },

    // 更新级联选项的子节点
    updateChildren(selectedData, children) {
      selectedData.forEach(data => {
        if (data.code === selectedData[selectedData.length - 1].code) {
          data.children = children;
        }
      });
    },

    // 获取后端的地理位置、灾情来源等数据
    async fetchData() {
      try {
        const sourcesResponse = await request.get('http://localhost:8080/disaster/sources');
        const carriersResponse = await request.get('http://localhost:8080/disaster/carriers');
        const disasterTypesResponse = await request.get('http://localhost:8080/disaster/types');
        const indicatorsResponse = await request.get('http://localhost:8080/disaster/indicators');

        // 设置其他下拉选项数据
        this.sources = sourcesResponse.data;
        this.carriers = carriersResponse.data;
        this.disasterTypes = disasterTypesResponse.data;
        this.indicators = indicatorsResponse.data;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    // 生成灾情码
    generateDisasterCode() {
      const location = this.form.location.join('/');
      const time = this.form.time;
      const source = this.form.source;
      const carrier = this.form.carrier;
      const disasterType = this.form.disasterType;
      const indicator = this.form.indicator;
      const description = this.form.description;

      this.disasterCode = `${location}|${time}|${source}|${carrier}|${disasterType}|${indicator}|${description}`;
    },

    // 解码灾情码
    decodeDisasterCode() {
      const codeParts = this.disasterCodeInput.split('|');
      this.decodedData = {
        location: codeParts[0],
        time: codeParts[1],
        source: codeParts[2],
        carrier: codeParts[3],
        disasterType: codeParts[4],
        indicator: codeParts[5],
        description: codeParts[6]
      };
    },

    // 上传灾情信息
    async submitDisasterInfo() {
      const disasterInfo = {
        location: this.form.location.join('/'),
        time: this.form.time,
        source: this.form.source,
        carrier: this.form.carrier,
        disasterType: this.form.disasterType,
        indicator: this.form.indicator,
        description: this.form.description
      };

      try {
        const response = await request.post('http://localhost:8080/add', disasterInfo);
        console.log('Disaster information uploaded successfully:', response);
      } catch (error) {
        console.error('Error uploading disaster information:', error);
      }
    },

    // 获取所有灾情信息（解码）
    async fetchAllDisasterInfo() {
      try {
        const response = await request.get(`http://localhost:8080/all?disasterCode=${this.disasterCodeInput}`);
        this.decodedData = response.data;
        console.log('Decoded disaster information:', this.decodedData);
      } catch (error) {
        console.error('Error fetching all disaster information:', error);
      }
    },

    fileUploadSuccess(response) {
      console.log('File uploaded successfully:', response);
    },
  },

  mounted() {
    // 获取初始的省份数据
    this.fetchProvinces()
        .then(provinces => {
          this.locationOptions = provinces;
        })
        .catch(error => {
          console.error("Error fetching provinces:", error);
        });

    this.fetchData().catch(error => {
      console.error("Error fetching other data:", error);
    });
  }
}
</script>


<style scoped>
.form-card {
  margin-top: 20px;
}
.form-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}
.disaster-code-display {
  margin-top: 20px;
}
</style>
