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
                  :props="cascaderProps"
                  placeholder="请选择完整的地理位置"
                  @change="handleCascadeChange"
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
              <el-cascader
                  size="small"
                  v-model="form.source"
                  :props="sourceProps"
                  placeholder="请选择灾情来源"
                  @change="handleSourceChange"
                  change-on-select
              ></el-cascader>
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

              <el-cascader
                  size="small"
                  v-model="form.disasterType"
                  :props="disasterTypeProps"
                  placeholder="请选择灾情分类"
                  @change="handleDisasterTypeChange"
                  change-on-select
              ></el-cascader>
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
        <el-input
            size="small"
            v-model="disasterCode" placeholder="生成的灾情码" readonly>
        </el-input>
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
        source: [],
        carrier: '',
        disasterType: [],
        indicator: '',
        description: ''
      },
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
      SourcesName:'',
      subSourcesName:'',
      carriers: [],
      disasterTypes: [],
      indicators: [],
      selectedDisasterTypeCode: '',  // 用于记录当前选中的灾情类型编码
      selectedDisasterSubTypeCode: '',  // 用于记录当前选中的灾情子类型编码
      isSelectingDisasterType: true,  // 用于标记是否在选择灾情类型

      cascaderProps: {
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
  methods: {
    // 懒加载函数，根据级别来加载不同的区域数据
    lazyLoadData(node, resolve) {
      const {level, value} = node;  // 获取当前节点的级别和选中的值
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
      const {level, value} = node;  // 获取当前节点的级别和选中的值
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
      const {level, value} = node;  // 获取当前节点的级别和选中的值
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
        return response.data;
      } catch (error) {
        console.error('Error fetching provinces:', error);
      }
    },

    // 获取城市数据
    async fetchCities(provinceCode) {
      try {
        const response = await request.get(`http://localhost:8080/api/areas/getCities?provinceCode=${provinceCode}`);
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
        return response.data;  // 返回后端的数据，格式与后端一致
      } catch (error) {
        console.error('Error fetching towns:', error);
      }
    },


    // 获取村庄数据
    async fetchVillages(streetCode) {
      try {
        // const response = await request.get(`http://localhost:8080/api/areas/getVillages?streetCode=${streetCode}`);
        // return response.data;
        const data = [
          {
            code: '110101001001',
            name: '多福巷社区居委会'
          },
          {
            code: '110101001002',
            name: '银闸社区居委会'
          },
          {
            code: '110101001005',
            name: '东厂社区居委会'
          },
          {
            code: '110101001006',
            name: '智德社区居委会'
          },
        ];
        return  data.map(item => {
          return {
            code: item.code,
            name: item.name,
            leaf:true // 最后一级，为叶子节点
          }
        });
      } catch (error) {
        console.error('Error fetching villages:', error);
      }
    },

    // 获取来源大类
    async fetchSources() {
      try {
        const sourcesResponse = await request.get('http://localhost:8080/disaster/sources');
        const sourcesNameResponse = await request.get('http://localhost:8080/disaster/sourcesName');
        console.log("Source:",sourcesResponse.data);
        console.log("SourceName",sourcesNameResponse);
        this.SourcesName=sourcesNameResponse.data[1].name;
        console.log("SourceName",this.SourcesName);
        return sourcesResponse.data; // 从后端获取数据
        console.log("Source:",sourcesResponse.data);
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
        const subSourcesNameResponse = await request.get('http://localhost:8080/disaster/subSourcesName');
        this.subSourcesName=subSourcesNameResponse.data[sourceCode].name;
        console.log("SourceName",this.subSourcesName);
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
      console.log("地理位置",Location);
      const time = this.form.time.toISOString().split('T')[0].replace(/-/g, '');;
      const source = this.form.source;
      const carrier = this.form.carrier;
      const disasterType = this.form.disasterType;
      const indicator = this.form.indicator;
      const description = this.form.description;

      // 生成符合要求的结构

      // const disasterData = {
      //   province: location.split('/')[0],  // 北京
      //   city: location.split('/')[1],  // 北京市
      //   country: location.split('/')[2],  // 门头沟区
      //   town: location.split('/')[3],  // 永定路街道
      //   village: location.split('/')[4],  // 采石路7号社区居委会
      //   category: disasterType,  // 房屋破坏
      //   date: time,  // 2024-12-24T14:00:00
      //   location: location.split('/')[1],  // 北京市
      //   detail: description,  // 发生山洪
      //   reportingUnit: this.form.source,  // 门头沟地质局
      //   sourceCategory: this.SourcesName,  // 业务报送数据
      //   sourceSubCategory: this.subSourcesName,  // 灾情快速上报接收处理系统
      //   carrierForm: carrier,  // 图像
      //   link: null,  // 传输数据链接
      //   disasterMajorCategory: disasterType,  // 房屋破坏
      //   disasterSubCategory: '土木',  // 固定示例，若有其他信息可以动态填充
      //   disasterIndicator: indicator  // 一般损坏面积
      // };
      const disasterData = {
        province: "北京市",  // 北京
        city: "市辖区",  // 北京市
        country: "东城区",  // 门头沟区
        town: "东华门街道",  // 永定路街道
        village: "多福巷社区居委会",  // 采石路7号社区居委会
        category: disasterType,  // 房屋破坏
        date: time,  // 2024-12-24T14:00:00
        location: location,  // 北京市
        detail: description,  // 发生山洪
        reportingUnit: this.form.source,  // 门头沟地质局
        sourceCategory: this.SourcesName,  // 业务报送数据
        sourceSubCategory: this.subSourcesName,  // 灾情快速上报接收处理系统
        carrierForm: carrier,  // 图像
        link: null,  // 传输数据链接
        disasterMajorCategory: disasterType,  // 房屋破坏
        disasterSubCategory: "土木",  // 固定示例，若有其他信息可以动态填充
        disasterIndicator: indicator  // 一般损坏面积
      };

      this.sendDisasterData(disasterData);  // 调用发送接口方法
    },

    // 发送数据到后端接口
    async sendDisasterData(data) {
      try {
        // 发送 POST 请求到后端接口
        const response = await request.post('http://localhost:8080/add', data);
        console.log('数据发送成功:', response);
        console.log("发送的灾情信息：", data);

        // 检查 response.data 是否有效
        if (response && response.data) {
          // 如果返回的 code 是 0，则表示成功
          if (response.data.code === "0") {
            // 获取返回的灾情码并填充到 disasterCode 输入框
            this.disasterCode = response.data.data || '无灾情码返回'; // 如果没有返回数据，默认显示 '无灾情码返回'
            console.log('生成的灾情码:', this.disasterCode);
          } else {
            // 如果 code 不是 0，显示错误信息
            console.error('错误:', response.data.msg || '未知错误');
          }
        } else {
          console.error('响应数据无效');
        }
      } catch (error) {
        // 捕获并打印错误信息
        console.error('数据发送失败:', error);
      }
    },


    // 解码灾情码
    async decodeDisasterCode() {
      try {
        // 发起请求，获取解码后的灾情信息
        const response = await request.get(`http://localhost:8080/all?disasterCode=${this.disasterCodeInput}`);

        // 从后端响应中提取灾情信息字符串，并解析为对象
        const disasterInfo = response.data.data;

        // 将灾情信息字符串转换为对象
        const disasterInfoObj = this.parseDisasterInfo(disasterInfo);

        // 假设 disasterInfoObj 格式如下：{province: '河北省', city: '承德市', ... }
        // 只填充前端需要的字段
        this.decodedData = {
          //...this.decodedData,  // 保留已有的数据
          location: disasterInfoObj.location || '',
          time: disasterInfoObj.date ? new Date(disasterInfoObj.date).toLocaleString() : '',  // 格式化时间
          source: disasterInfoObj.sourceCategory || '',
          carrier: disasterInfoObj.carrierForm || '',
          disasterType: disasterInfoObj.disasterMajorCategory || '',
          indicator: disasterInfoObj.disasterIndicator || '',
          description: disasterInfoObj.detail || ''  // 假设为灾情描述字段
        };
      } catch (error) {
        console.error('Error fetching disaster info:', error);
      }
    },

// 解析灾情信息字符串为对象
    parseDisasterInfo(disasterInfoStr) {
      // 使用正则表达式提取字段中的内容
      const regex = /(\w+)='([^']*)'/g;
      let match;
      const disasterInfoObj = {};

      while ((match = regex.exec(disasterInfoStr)) !== null) {
        disasterInfoObj[match[1]] = match[2];
      }

      return disasterInfoObj;
    },



    fileUploadSuccess(response) {
      console.log('File uploaded successfully:', response);
    },
  },

  mounted() {
    this.initData();
  }
}
</script>


// 全局样式，可能有问题
// https://www.cnblogs.com/alioth01/p/18527300
<style >
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
