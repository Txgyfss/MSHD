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
              <el-cascader size="small" v-model="form.location" :props="cascaderProps" placeholder="请选择完整的地理位置"
                @change="handleCascadeChange" change-on-select></el-cascader>
            </el-form-item>
          </el-col>


          <!-- 时间 -->
          <el-col :span="12">
            <el-form-item label="灾情时间">
              <el-date-picker v-model="form.time" type="datetime" placeholder="请选择灾情时间" size="small"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <!-- 来源 -->
          <el-col :span="12">
            <el-form-item label="灾情来源">
              <el-cascader size="small" v-model="form.source" :props="sourceProps" placeholder="请选择灾情来源"
                @change="handleSourceChange" change-on-select></el-cascader>
            </el-form-item>
          </el-col>

          <!-- 载体类型 -->
          <el-col :span="12">
            <el-form-item label="灾情载体类型">
              <el-select size="small" v-model="form.carrier" placeholder="请选择灾情载体类型">
                <el-option v-for="(carrier, index) in carriers" :key="index" :label="carrier"
                  :value="carrier"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <!-- 灾情分类 -->
          <el-col :span="12">
            <el-form-item label="灾情分类">

              <el-cascader size="small" v-model="form.disasterType" :props="disasterTypeProps" placeholder="请选择灾情分类"
                @change="handleDisasterTypeChange" change-on-select></el-cascader>
            </el-form-item>
          </el-col>

          <!-- 灾情指标分类 -->
          <el-col :span="12">
            <el-form-item label="灾情指标分类">
              <el-select size="small" v-model="form.indicator" placeholder="请选择灾情指标分类">
                <el-option v-for="(indicator, index) in indicators" :key="index" :label="indicator"
                  :value="indicator"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 富文本框，放在编码部分 -->
        <el-form-item label="灾情描述" style="margin-top: 10px;">
          <el-input type="textarea" size="small" v-model="form.description" placeholder="请输入灾情描述" rows="4"
            style="resize: none; width: 100%;"></el-input>
        </el-form-item>

        <!-- 生成灾情码按钮 -->
        <el-form-item>
          <el-button size="small" type="primary" @click="generateDisasterCode">生成灾情码</el-button>
        </el-form-item>
      </el-form>

      <div class="disaster-code-display">
        <el-input size="small" v-model="disasterCode" placeholder="生成的灾情码" readonly>
        </el-input>
      </div>

    </el-card>

    <!-- 解码部分 -->
    <el-card class="form-card" :body-style="{ padding: '20px' }">
      <div class="form-title">灾情信息解码</div>
      <el-form :model="form" label-position="right" label-width="120px">
        <el-form-item label="灾情码">
          <el-input size="small" v-model="disasterCodeInput" placeholder="请输入灾情码"></el-input>
        </el-form-item>



        <el-form-item label="上传文件">
          <el-upload :http-request="customUploadRequest" :before-upload="beforeFileUpload" list-type="text"
            style="width: 100%;">
            <el-button size="small" type="success">点击上传文件</el-button>
          </el-upload>
        </el-form-item>


        <el-form-item style="margin-top: 15px;">
          <el-button size="small" type="primary" @click="fetchAllDisasterInfo">解码灾情码</el-button>
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
        source: [],
        carrier: '',
        disasterType: [],
        indicator: '',
        description: ''
      },
      locationOptions: [],  // 存储级联下拉框数据
      uploadParams: {}, // 动态传递给后端的参数
      filePath: '', // 上传后的文件路径
      disasterCode: '',
      disasterCodeInput: '',  // 用户输入的灾情码
      decodedData: {
        location: '',  // 地点
        time: '',  // 时间
        source: '',  // 来源
        carrier: '',  // 承载形式
        disasterType: '',  // 灾情类型
        indicator: '',  // 灾情指标
        description: '',  // 描述
        province: '',  // 省
        city: '',  // 市
        country: '',  // 区县
        town: '',  // 镇
        village: '',  // 村
        disasterMajorCategory: '',  // 灾情主类别
        disasterSubCategory: '',  // 灾情子类别
        disasterIndicator: '',  // 灾情指标
        date: '',  // 日期
        reportingUnit: '',  // 上报单位
        sourceCategory: '',  // 来源分类
        sourceSubCategory: '',  // 来源子分类
        carrierForm: '',  // 承载形式
        link: ''  // 数据链接
      },
      sourceValueLabelMap: [
        { code: "1", name: "业务报送数据" },
        { code: "2", name: "泛在感知数据" },
        { code: "3", name: "其他数据" },
        { code: "00", name: "前方地震应急指挥部", sourceCode: "1" },
        { code: "01", name: "后方地震应急指挥部", sourceCode: "1" },
        { code: "20", name: "应急指挥技术系统", sourceCode: "1" },
        { code: "21", name: "社会服务工程应急救援系统", sourceCode: "1" },
        { code: "40", name: "危险区预评估工作组", sourceCode: "1" },
        { code: "41", name: "地震应急指挥技术协调组", sourceCode: "1" },
        { code: "42", name: "震后政府信息支持工作项目组", sourceCode: "1" },
        { code: "80", name: "灾情快速上报接收处理系统", sourceCode: "1" },
        { code: "81", name: "地方地震局应急信息服务相关技术系统", sourceCode: "1" },
        { code: "99", name: "其他", sourceCode: "1" },
        { code: "00", name: "互联网感知", sourceCode: "2" },
        { code: "01", name: "通信网感知", sourceCode: "2" },
        { code: "02", name: "舆情网感知", sourceCode: "2" },
        { code: "03", name: "电力系统感知", sourceCode: "2" },
        { code: "04", name: "交通系统感知", sourceCode: "2" },
        { code: "05", name: "其他", sourceCode: "2" },
        { code: "03", name: "  ", sourceCode: "3" }

      ],
      catagoryValueLabelMap: [
        { code: "1", name: "震情" },
        { code: "2", name: "人员伤亡及失踪" },
        { code: "3", name: "房屋破坏" },
        { code: "4", name: "生命线工程灾情" },
        { code: "5", name: "次生灾害" },
        { code: "01", name: "震情信息", disasterCode: "1" },
        { code: "01", name: "死亡", disasterCode: "2" },
        { code: "02", name: "受伤", disasterCode: "2" },
        { code: "03", name: "失踪", disasterCode: "2" },
        { code: "01", name: "土木", disasterCode: "3" },
        { code: "02", name: "砖木", disasterCode: "3" },
        { code: "03", name: "砖混", disasterCode: "3" },
        { code: "04", name: "框架", disasterCode: "3" },
        { code: "05", name: "其他", disasterCode: "3" },
        { code: "01", name: "交通", disasterCode: "4" },
        { code: "02", name: "供水", disasterCode: "4" },
        { code: "03", name: "输油", disasterCode: "4" },
        { code: "04", name: "燃气", disasterCode: "4" },
        { code: "05", name: "电力", disasterCode: "4" },
        { code: "06", name: "通信", disasterCode: "4" },
        { code: "07", name: "水利", disasterCode: "4" },
        { code: "01", name: "崩塌", disasterCode: "5" },
        { code: "02", name: "滑坡", disasterCode: "5" },
        { code: "03", name: "泥石流", disasterCode: "5" },
        { code: "04", name: "岩溶塌陷", disasterCode: "5" },
        { code: "05", name: "地裂缝", disasterCode: "5" },
        { code: "06", name: "地面沉降", disasterCode: "5" },
        { code: "07", name: "其他", disasterCode: "5" }
      ],
      valueLabelMap: [

      ],

      SourcesName: '',
      subSourcesName: '',
      carriers: [],
      disasterTypes: [],
      indicators: [],
      selectedDisasterTypeCode: '',  // 用于记录当前选中的灾情类型编码
      selectedDisasterSubTypeCode: '',  // 用于记录当前选中的灾情子类型编码
      isSelectingDisasterType: true,  // 用于标记是否在选择灾情类型

      cascaderProps: {
        //value: 'code', // 显示的数据绑定字段
        value: 'code', // 显示的数据绑定字段
        label: 'name', // 显示的名称字段
        children: 'children', // 子级数据字段
        lazy: true, // 启用懒加载
        lazyLoad: this.lazyLoadData // 动态加载函数
      },
      sourceProps: {
        value: 'code', // 显示的数据绑定字段
        label: 'name', // 显示的名称字段
        children: 'children', // 子级数据字段
        lazy: true, // 启用懒加载
        lazyLoad: this.lazyLoadSourceData // 动态加载函数
      },
      disasterTypeProps: {
        value: 'code', // 显示的数据绑定字段
        label: 'name', // 显示的名称字段
        children: 'children', // 子级数据字段
        lazy: true, // 启用懒加载
        lazyLoad: this.lazyLoadDisasterTypeData // 动态加载函数
      }
    }
  },
  watch: {
    'form.location': function (newValue, oldValue) {
      console.log('form.location 变化了：', newValue);
      console.log('变化前的值：', oldValue);
    }
  },
  methods: {
    // 懒加载函数，根据级别来加载不同的区域数据
    lazyLoadData(node, resolve) {
      const { level, value } = node;  // 获取当前节点的级别和选中的值
      let fetchData;
      // 根据级别来判断加载哪个数据（省 -> 市 -> 县区 -> 乡镇 -> 村庄）
      switch (level) {
        case 0: // 省级
          fetchData = this.fetchProvinces();
          break;
        case 1: // 市级
          fetchData = this.fetchCities(value);
          break;
        case 2: // 区县级
          fetchData = this.fetchCounties(value);
          break;
        case 3: // 乡镇级
          fetchData = this.fetchTowns(value);
          break;
        case 4: // 村庄级
          fetchData = this.fetchVillages(value);
          break;
        default:
          resolve([]);
          return;
      }

      // 获取数据后，通过 resolve 函数返回节点数据
      fetchData.then(data => {
        resolve(data); // 返回数据
      }).catch(error => {
        console.error(error);
        resolve([]); // 如果请求失败，返回空数组
      });
    },
    lazyLoadSourceData(node, resolve) {
      const { level, value } = node;  // 获取当前节点的级别和选中的值
      let fetchData;
      console.log(`Lazy load triggered for level: ${level}, value: ${value}`);
      // 根据级别来判断加载哪个数据（大类 -> 子类）
      switch (level) {
        case 0: // 大类
          fetchData = this.fetchSources();
          break;
        case 1: // 子类
          fetchData = this.fetchSubSources(value);
          break;
        default:
          resolve([]);
          return;
      }

      // 获取数据后，通过 resolve 函数返回节点数据
      fetchData.then(data => {
        resolve(data); // 返回数据
      }).catch(error => {
        console.error(error);
        resolve([]); // 如果请求失败，返回空数组
      });
    },
    lazyLoadDisasterTypeData(node, resolve) {
      const { level, value } = node;  // 获取当前节点的级别和选中的值
      let fetchData;
      // 根据级别来判断加载哪个数据（大类 -> 子类）
      switch (level) {
        case 0: // 大类
          fetchData = this.fetchDisasterTypes();
          break;
        case 1: // 子类
          fetchData = this.fetchSubDisasterTypes(value);
          break;
        default:
          resolve([]);
          return;
      }

      // 获取数据后，通过 resolve 函数返回节点数据
      fetchData.then(data => {
        resolve(data); // 返回数据
      }).catch(error => {
        console.error(error);
        resolve([]); // 如果请求失败，返回空数组
      });
    },
    // 获取省份数据
    async fetchProvinces() {
      try {
        const response = await request.get('http://localhost:8080/api/areas/getProvinces');
        console.log(response.data);
        const provinces = response.data;
        if (provinces && Array.isArray(provinces)) {
          // 将每个省份的数据添加到 valueLabelMap 数组
          provinces.forEach(province => {
            this.valueLabelMap.push({
              code: province.code,
              name: province.name
            });
          });
        }
        console.log("Provinces added to valueLabelMap:", this.valueLabelMap);
        return response.data;
      } catch (error) {
        console.error('Error fetching provinces:', error);
      }
    },

    // 获取城市数据
    async fetchCities(provinceCode) {
      try {
        const response = await request.get(`http://localhost:8080/api/areas/getCities?provinceCode=${provinceCode}`);
        const cities = response.data;
        if (cities && Array.isArray(cities)) {
          // 将每个数据添加到 valueLabelMap 数组
          cities.forEach(city => {
            this.valueLabelMap.push({
              code: city.code,
              name: city.name
            });
          });
        }
        console.log("Cities added to valueLabelMap:", this.valueLabelMap);
        return response.data;
      } catch (error) {
        console.error('Error fetching cities:', error);
      }
    },

    // 获取县区数据
    async fetchCounties(cityCode) {
      try {
        // 调用后端接口获取县区数据
        const response = await request.get(`http://localhost:8080/api/areas/getCounties?cityCode=${cityCode}`);
        const counties = response.data;
        if (counties && Array.isArray(counties)) {
          // 将每个省份的数据添加到 valueLabelMap 数组
          counties.forEach(county => {
            this.valueLabelMap.push({
              code: county.code,
              name: county.name
            });
          });
        }
        console.log("Counties added to valueLabelMap:", this.valueLabelMap);
        return response.data;  // 返回后端的数据，格式与后端一致
      } catch (error) {
        console.error('Error fetching counties:', error);
      }
    },

    // 获取乡镇数据
    async fetchTowns(areaCode) {
      try {
        // 调用后端接口获取乡镇数据
        const response = await request.get(`http://localhost:8080/api/areas/getTowns?areaCode=${areaCode}`);
        const towns = response.data;
        if (towns && Array.isArray(towns)) {
          // 将每个省份的数据添加到 valueLabelMap 数组
          towns.forEach(town => {
            this.valueLabelMap.push({
              code: town.code,
              name: town.name
            });
          });
        }
        console.log("Towns added to valueLabelMap:", this.valueLabelMap);
        return response.data;  // 返回后端的数据，格式与后端一致
      } catch (error) {
        console.error('Error fetching towns:', error);
      }
    },


    // 获取村庄数据
    async fetchVillages(streetCode) {
      try { // 调用后端接口获取乡镇数据
        console.log("streetcode", streetCode);
        const response = await request.get(`http://localhost:8080/api/areas/getVillages?streetCode=${streetCode}`);
        const villages = response.data;
        if (villages && Array.isArray(villages)) {
          // 将每个省份的数据添加到 valueLabelMap 数组
          villages.forEach(village => {
            this.valueLabelMap.push({
              code: village.code,
              name: village.name
            });
          });
        }
        console.log("Villages added to valueLabelMap:", this.valueLabelMap);
        return response.data;  // 返回后端的数据，格式与后端一致
      } catch (error) {
        console.error('Error fetching towns:', error);
      }
    },

    // 获取来源大类
    async fetchSources() {
      try {
        const sourcesResponse = await request.get('http://localhost:8080/disaster/sources');
        console.log("Source:", sourcesResponse.data);

        console.log("SourceName", this.SourcesName);
        return sourcesResponse.data; // 从后端获取数据
        console.log("Source:", sourcesResponse.data);

      } catch (error) {
        console.error('Error fetching sources:', error);
      }
    },

    // 获取来源子类
    async fetchSubSources(sourceCode) {
      try {
        const subSourcesResponse = await request.get('http://localhost:8080/disaster/subSources', {
          params: { sourceCode }  // 确保传递了正确的 sourceCode
        });


        return subSourcesResponse.data; // 从后端获取数据
      } catch (error) {
        console.error('Error fetching sub-sources:', error);
      }
    },

    // 获取灾情类型大类
    async fetchDisasterTypes() {
      try {
        const disasterTypesResponse = await request.get('http://localhost:8080/disaster/types');
        this.disasterTypes = disasterTypesResponse.data;  // 更新 disasterTypes
        return disasterTypesResponse.data; // 从后端获取数据
      } catch (error) {
        console.error('Error fetching disaster types:', error);
      }
    },

    // 获取子灾情类型大类
    async fetchSubDisasterTypes(disasterCode) {
      try {
        const subDisasterTypesResponse = await request.get('http://localhost:8080/disaster/subTypes', {
          params: { disasterCode }  // 传递 disasterCode 作为查询参数
        });
        return subDisasterTypesResponse.data; // 从后端获取数据
      } catch (error) {
        console.error('Error fetching sub-disaster types:', error);
      }
    },

    // 级联选择变化时的处理
    handleCascadeChange(node) {
      // 此处获取的是最后一级选中的数据
      // this.form.location = node[node.length - 1];
      console.log('选中的地理位置:', this.form.location);
      const selectedData = node[node.length - 1];
      console.log(selectedData);
    },
    handleSourceChange(node) {
      // 此处获取的是最后一级选中的数据
      // this.form.source = node[node.length - 1];
      const selectedData = node[node.length - 1];
      console.log(selectedData);
    },
    handleDisasterTypeChange(node) {
      console.log('选中的节点数组:', node);
      console.log('灾情类型数据:', this.disasterTypes);
      if (node && node.length > 0) {
        // 假设你只需要第一个选中的节点的 label
        const selectedDisasterTypeCode = node[0];  // 获取第一个选中的值

        console.log('灾情类型编码:', selectedDisasterTypeCode);
        // 根据 selectedDisasterTypeCode 获取对应的 label
        let selectedLabel = '';
        this.getLabelFromDisasterTypes(selectedDisasterTypeCode, (label) => {
          selectedLabel = label;
          console.log('灾情类型的 label:', selectedLabel);
        });

        if (this.isSelectingDisasterType) {
          // 如果需要继续其他操作
          if (selectedDisasterTypeCode !== this.selectedDisasterTypeCode) {
            this.selectedDisasterTypeCode = selectedDisasterTypeCode;
            this.fetchIndicators(selectedDisasterTypeCode);
          }
        } else {
          console.log('当前是选择灾情子类型，不请求灾情指标');
        }
      } else {
        console.warn('没有选择灾情类型');
      }
    },

    // 根据编码查找对应的label
    getLabelFromDisasterTypes(code, callback) {
      // 假设 disasterTypes 是你所有灾情类型数据的集合，且每个项有 'code' 和 'name'
      const foundItem = this.disasterTypes.find(item => item.code === code);
      if (foundItem) {
        callback(foundItem.name);  // 调用回调函数返回 label
      } else {
        console.warn('找不到对应的灾情类型');
      }
    },

    handleDisasterSubTypeChange(node) {
      // 打印选中的节点数组
      console.log('选中的子节点数组:', node);

      if (node && node.length > 0) {
        const selectedDisasterSubTypeCode = node[0];  // 获取选中的灾情子类型编码

        console.log('灾情子类型编码:', selectedDisasterSubTypeCode);

        // 只在选择灾情子类型时，更新灾情子类型编码
        if (selectedDisasterSubTypeCode !== this.selectedDisasterSubTypeCode) {
          this.selectedDisasterSubTypeCode = selectedDisasterSubTypeCode;
          // 不请求灾情指标，只更新子类型编码
          console.log('灾情子类型已更新:', selectedDisasterSubTypeCode);
        }
      } else {
        console.warn('没有选择灾情子类型');
      }
    },

    // 这个方法请求灾情指标，只有在选择灾情类型时触发
    async fetchIndicators(disasterTypeCode) {
      try {
        console.log('请求灾情指标时传递的灾情类型编码:', disasterTypeCode);
        const response = await request.get('http://localhost:8080/disaster/indicators', {
          params: { sourceCode: disasterTypeCode }
        });

        if (response.data) {
          // 获取到数据后，存储到 indicators 中
          this.indicators = response.data;
          console.log('已获取灾情指标:', this.indicators);
        } else {
          console.warn('没有获取到灾情指标数据');
          this.indicators = [];  // 如果没有数据，设置为空数组
        }
      } catch (error) {
        console.error('获取灾情指标时出错:', error);
        this.indicators = [];  // 如果出现错误，设置为空数组
      }
    },

    // 获取后端的地理位置、灾情来源等数据
    async initData() {
      try {
        const carriersResponse = await request.get('http://localhost:8080/disaster/carriers');
        //const indicatorsResponse = await request.get('http://localhost:8080/disaster/indicators');

        // 设置其他下拉选项数据
        this.carriers = carriersResponse.data;
        //this.indicators = indicatorsResponse.data;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    // 生成灾情码并生成符合格式的数据
    generateDisasterCode() {
      const location = this.form.location.join('/');  // 获取省市区县的组合
      const time = this.form.time.toISOString().split('.')[0];
      const source = this.form.source;
      const carrier = this.form.carrier;
      const disasterType = this.form.disasterType;
      const indicator = this.form.indicator;
      const description = this.form.description;
      console.log("地理位置", Location);

      // 生成字典
      const dict = this.valueLabelMap.reduce((acc, item) => {
        acc[item.code] = item.name;
        return acc;
      }, {});
      const catadict = this.catagoryValueLabelMap.reduce((acc, item) => {
        acc[item.code] = item.name;
        return acc;
      }, {});
      const catadict2 = this.catagoryValueLabelMap.reduce((acc, item) => {
        const key = `${item.code}_${item.disasterCode}`;  // 合并 code 和 name
        acc[key] = item;  // 将 item 本身作为值
        return acc;
      }, {});
      const sourcedict = this.sourceValueLabelMap.reduce((acc, item) => {
        acc[item.code] = item.name;
        return acc;
      }, {});
      console.log("灾情分类", catadict2[`${disasterType[1]}_${disasterType[0]}`]['name']);
      console.log("灾情来源", `${source[1]}_${source[0]}`);

      const disasterData = {
        province: dict[location.split('/')[0]],  // 北京
        city: dict[location.split('/')[1]],     // 北京市
        country: dict[location.split('/')[2]],  // 门头沟区
        town: dict[location.split('/')[3]], // 永定路街道
        village: dict[location.split('/')[4]],   // 采石路7号社区居委会
        category: null,  // 房屋破坏
        date: time,  // 2024-12-24T14:00:00
        location: location.split('/')[1],  // 北京市
        detail: description,  // 发生山洪
        reportingUnit: null,  // 门头沟地质局
        sourceCategory: sourcedict[source[0]],  // 业务报送数据
        sourceSubCategory: sourcedict[source[1]],  // 灾情快速上报接收处理系统
        carrierForm: carrier,  // 图像
        link: null,  // 传输数据链接
        disasterMajorCategory: catadict[disasterType[0]],  // 房屋破坏
        disasterSubCategory: catadict2[`${disasterType[1]}_${disasterType[0]}`]['name'],  // 固定示例，若有其他信息可以动态填充
        disasterIndicator: indicator  // 一般损坏面积
      };
      console.log("disasterData", disasterData);

      this.sendDisasterData(disasterData);  // 调用发送接口方法
    },

    // 发送数据到后端接口
    async sendDisasterData(data) {
      try {
        // 发送 POST 请求到后端接口
        const response = await request.post('http://localhost:8080/add', data);
        console.log('数据发送成功:', response);
        console.log("发送的灾情信息：", data);
        console.log("返回的数据：", response.data);
        // 检查 response.data 是否有效并且是成功的响应
        if (response && response.data) {
          const disasterInfo = response.data;
          console.log("返回的code：", response.code);
          // 如果返回的 code 是 "0"，则表示成功

          // 直接从 disasterInfo 提取灾情码
          // 灾情码为 disasterInfo 字符串中 "编码后的灾情码: " 后面的部分
          this.disasterCode = disasterInfo.split('编码后的灾情码: ')[1] || '无灾情码返回';
          console.log('生成的灾情码:', this.disasterCode);

        } else {
          console.error('响应数据无效');
        }
      } catch (error) {
        // 捕获并打印错误信息
        console.error('数据发送失败:', error);
      }
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
    async fetchAllDisasterInfo() {
      try {
        const requestData = {
          disasterCode: this.disasterCodeInput
        };

        // 发送 POST 请求
        const response = await request.post('http://localhost:8080/decode', requestData, {
          headers: {
            'Content-Type': 'application/json'
          }
        });

        console.log("Response from backend:", response);
        console.log("Response data:", response.data);
        console.log("Response data type:", response.data.code);

        if (response && response.data) {

          // 后端返回的 `data` 是 JSON 对象，直接解析
          const disasterInfo = response.data;

          // 将解析后的字段赋值到 `decodedData`
          this.decodedData = {
            location: disasterInfo.location || "",
            time: disasterInfo.date || "",
            source: `${disasterInfo.sourceCategory || ""} - ${disasterInfo.sourceSubCategory || ""}`,
            carrier: disasterInfo.carrierForm || "",
            disasterType: `${disasterInfo.disasterMajorCategory || ""} - ${disasterInfo.disasterSubCategory || ""}`,
            indicator: disasterInfo.disasterIndicator || "",
            description: disasterInfo.detail || ""
          };

          console.log("Decoded disaster information:", this.decodedData);

        } else {
          console.error("No response data received from backend.");
          this.$notify.error("未从后端接收到数据，请检查服务器！");
        }
      } catch (error) {
        console.error("Error fetching all disaster information:", error);
        this.$notify.error("请求失败，请检查网络或后端服务！");
      }
    },

    // 自定义上传请求逻辑
    async customUploadRequest({ file }) {
      // 获取灾情码
      const disasterCode = this.disasterCodeInput;

      if (!disasterCode) {
        this.$notify.error("请先输入灾情码！");
        return;
      }

      const formData = new FormData();
      formData.append("file", file);
      formData.append("disasterCode", disasterCode);

      try {
        // 调用后端上传接口
        const response = await request.post("http://localhost:8080/files/upload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        if (response.data.code === "0") {
          // 保存文件路径
          this.filePath = response.data.data.link || "";
          this.$notify.success("文件上传成功！");
          // 调用文件解码接口
          this.decodeFile();
        } else {
          this.$notify.error(response.data.msg || "文件上传失败！");
        }
      } catch (error) {
        console.error("File upload failed:", error);
        this.$notify.success("文件上传成功！");
      }
    },

    // 文件解码方法
    async decodeFile() {
      try {
        const decodeResponse = await request.post("http://localhost:8080/decodeFromFile", {
          disasterCode: this.disasterCodeInput,
          fileLink: this.filePath,
        });

        if (decodeResponse.data.code === "0") {
          this.$notify.success("解码并存储灾情信息成功！");
        } else {
          this.$notify.error(decodeResponse.data.msg || "解码失败！");
        }
      } catch (error) {
        console.error("Error decoding and storing disaster info:", error);
        this.$notify.error("解码失败，请检查后端服务！");
      }
    },

    // 在上传文件前动态设置参数
    beforeFileUpload(file) {
      const fileName = file.name;
      const fileParts = fileName.split("."); // 拆分文件名
      const disasterCode = fileParts[0]; // 假定文件名格式为: 灾情编码.文件格式

      if (!disasterCode) {
        this.$notify.error('文件名格式错误，请确保文件名为 "灾情编码.文件格式"');
        return false; // 取消上传
      }

      // 保存灾情编码到变量
      this.disasterCodeInput = disasterCode;

      return true; // 允许上传
    },



  },

  mounted() {
    this.initData();
  }
}
</script>


// 全局样式，可能有问题
// https://www.cnblogs.com/alioth01/p/18527300
<style>
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

.el-radio input[aria-hidden="true"] {
  display: none !important;
}

.el-radio:focus:not(.is-focus):not(:active):not(.is-disabled) .el-radio__inner {
  box-shadow: none !important;
}
</style>
